package com.bizvisionsoft.bruiengine.util;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.service.model.Result;

public class CommandHandler {

	public static final int OK = 0;

	public static final int ERROR = 1;

	public static final int CONTINUE = 2;

	public static final int CANCEL = 3;

	public static final int WARNING = 4;

	public static int handle(List<Result> result, String title, String successMsg, String failureMsg) {
		return handle(result, title, successMsg, failureMsg, null);
	}

	public static int handle(List<Result> result, String title, String successMsg, String failureMsg,
			Function<Integer, Integer> continueProcess) {
		Shell shell = Display.getCurrent().getActiveShell();

		boolean hasError = false;
		boolean hasWarning = false;

		String message = "";
		if (!result.isEmpty()) {
			for (Result r : result)
				if (Result.TYPE_ERROR == r.type) {
					hasError = true;
					message += "<span class='layui-badge'>错误</span> " + r.message + "<br>";
				} else if (Result.TYPE_WARNING == r.type) {
					hasWarning = true;
					message += "<span class='layui-badge layui-bg-orange'>警告</span> " + r.message + "<br>";
				} else {
					message += "<span class='layui-badge layui-bg-blue'>信息</span> " + r.message + "<br>";
				}
		}

		int returnType;
		if (message.isEmpty()) {
			Layer.message(successMsg);
			returnType = OK;
		} else {
			if (hasError) {
				MessageDialog.openError(shell, failureMsg, message );
				returnType = ERROR;
			} else if (hasWarning) {
				if (continueProcess != null) {
					boolean confirm = MessageDialog.openQuestion(shell, title, message + "<br>是否继续？");
					if (confirm) {
						returnType = CONTINUE;
					} else {
						returnType = CANCEL;
					}
					returnType = continueProcess.apply(returnType);
				} else {
					MessageDialog.openWarning(shell, title, message);
					returnType = WARNING;
				}
			} else {
				MessageDialog.openInformation(shell, title, message);
				returnType = OK;
			}
		}

		return returnType;
	}

	public static void run(String title, String confirmMsg, String successMsg, String failureMsg,
			Supplier<List<Result>> runWithCheck, Supplier<List<Result>> doConfirmed, Consumer<Integer> doOK) {
		Shell shell = Display.getCurrent().getActiveShell();
		if (!MessageDialog.openConfirm(shell, title, confirmMsg)) {
			return;
		}
		if (doConfirmed != null) {
			int code = CommandHandler.handle(runWithCheck.get(), title, successMsg, failureMsg, c -> {
				if (CONTINUE == c) {
					int _r = CommandHandler.handle(doConfirmed.get(), title, successMsg, failureMsg);
					if (_r == OK || _r == WARNING)
						return OK;
				}
				return c;
			});
			if (OK == code)
				doOK.accept(OK);
		} else {
			if (OK == CommandHandler.handle(runWithCheck.get(), title, successMsg, failureMsg))
				doOK.accept(OK);
		}
	}

	public static void run(String title, String confirmMsg, String successMsg, String failureMsg,
			Supplier<List<Result>> runWithCheck, Consumer<Integer> doOK) {
		run(title, confirmMsg, successMsg, failureMsg, runWithCheck, null, doOK);
	}

}
