package com.bizvisionsoft.bruiengine.util;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.service.model.ICommand;
import com.bizvisionsoft.service.model.Result;

public class ResultHandler {

	public static final int OK = 0;

	public static final int ERROR = 1;

	public static final int CONTINUE = 2;

	public static final int CANCEL = 3;

	public static final int WARNING = 4;

	public static int handle(List<Result> result, String title, String successMsg, String failureMsg) {
		return handle(result, title, successMsg, failureMsg, null);
	}

	public static int handle(List<Result> result, String title, String successMsg, String failureMsg,
			Consumer<Integer> continueProcess) {
		Shell shell = Display.getCurrent().getActiveShell();

		boolean hasError = false;
		boolean hasWarning = false;

		String message = "";
		if (!result.isEmpty()) {
			for (Result r : result)
				if (Result.TYPE_ERROR == r.type) {
					hasError = true;
					message += "错误：" + r.message + "<br>";
				} else if (Result.TYPE_WARNING == r.type) {
					hasError = true;
					message += "警告：" + r.message + "<br>";
				} else {
					message += "信息：" + r.message + "<br>";
				}
		}

		int returnType;
		if (message.isEmpty()) {
			Layer.message(successMsg);
			returnType = OK;
		} else {
			if (hasError) {
				MessageDialog.openError(shell, title, message + "<br><br>" + failureMsg);
				returnType = ERROR;
			} else if (hasWarning) {
				if (continueProcess != null) {
					boolean confirm = MessageDialog.openQuestion(shell, title, message + "<br><br>是否继续？");
					if (confirm) {
						returnType = CONTINUE;
					} else {
						returnType = CANCEL;
					}
					continueProcess.accept(returnType);
				} else {
					MessageDialog.openWarning(shell, title, message + "<br><br>");
					returnType = WARNING;
				}
			} else {
				MessageDialog.openInformation(shell, title, message + "<br><br>" + successMsg);
				returnType = OK;
			}
		}

		return returnType;
	}

	public static void run(String title, String successMsg, String failureMsg, Supplier<List<Result>> doCheck,
			Supplier<List<Result>> doConfirmed, Consumer<Integer> switchPage) {
		List<Result> result = doCheck.get();
		ResultHandler.handle(result, title, successMsg, failureMsg, c -> {
			if (ResultHandler.CONTINUE == c) {
				List<Result> _result = doConfirmed.get();
				int _r = ResultHandler.handle(_result, ICommand.Start_Project, successMsg, failureMsg);
				if (_r == ResultHandler.OK || _r == ResultHandler.WARNING) {
					switchPage.accept(_r);
				}
			}
		});
	}

}
