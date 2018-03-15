package com.bizvisionsoft.bruiengine.assembly.field;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.fileupload.FileUploadEvent;
import org.eclipse.rap.fileupload.FileUploadHandler;
import org.eclipse.rap.fileupload.FileUploadListener;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.rap.rwt.widgets.FileUpload;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.bizvisionsoft.bruiengine.util.SessionDiskUploadReceiver;
import com.bizvisionsoft.bruiengine.util.Util;
import com.bizvisionsoft.service.FileService;
import com.bizvisionsoft.service.model.RemoteFile;
import com.bizvisionsoft.serviceconsumer.Publisher;
import com.bizvisionsoft.serviceconsumer.Services;

public class FileField extends EditorField implements FileUploadListener {

	private List<RemoteFile> value;
	private Label label;
	private FileUpload fileUpload;
	private SessionDiskUploadReceiver receiver;
	private ProgressBar progress;
	private Display display;
	private ServerPushSession pushSession;

	public FileField() {
	}

	@Override
	protected Control createControl(Composite parent) {
		display = parent.getDisplay();
		pushSession = new ServerPushSession();
		pushSession.start();

		receiver = new SessionDiskUploadReceiver();

		final FileUploadHandler uploadHandler = new FileUploadHandler(receiver);
		uploadHandler.addUploadListener(this);

		int maxFileSize = fieldConfig.getMaxFileSize();
		if (maxFileSize != 0)
			uploadHandler.setMaxFileSize(1024l * 1024 * maxFileSize);
		int textLimit = fieldConfig.getTextLimit();
		if (textLimit != 0)
			uploadHandler.setUploadTimeLimit(1000l * textLimit);

		Composite pane = new Composite(parent, SWT.BORDER);
		pane.setLayout(new FormLayout());
		label = new Label(pane, SWT.NONE);
		UserSession.bruiToolkit().enableMarkup(label);
		progress = new ProgressBar(pane, SWT.NONE);
		progress.setMaximum(100);

		fileUpload = new FileUpload(pane, SWT.NONE);
		String exts = fieldConfig.getFileFilerExts();
		if (exts != null)
			fileUpload.setFilterExtensions(exts.split(","));

		fileUpload.setData(RWT.CUSTOM_VARIANT, "inline");
		fileUpload.setText("上传文件");
		fileUpload.addListener(SWT.Selection, e -> {
			progress.moveAbove(null);
			fileUpload.submit(uploadHandler.getUploadUrl());
		});

		Button fileDelete = new Button(pane, SWT.PUSH);
		fileDelete.setData(RWT.CUSTOM_VARIANT, "inline");
		fileDelete.setText("清空");
		fileDelete.addListener(SWT.Selection, e -> {
			value.clear();
			receiver.clear();
			presentation();
		});

		FormData fd = new FormData();
		fileUpload.setLayoutData(fd);
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);
		fd.height = 36;
		fd.width = 80;

		fd = new FormData();
		fileDelete.setLayoutData(fd);
		fd.right = new FormAttachment(fileUpload);
		fd.bottom = new FormAttachment(100);
		fd.height = 36;
		fd.width = 80;

		fd = new FormData();
		label.setLayoutData(fd);
		fd.right = new FormAttachment(fileDelete);
		fd.bottom = new FormAttachment(100);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();

		fd = new FormData();
		progress.setLayoutData(fd);
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);
		fd.left = new FormAttachment();
		fd.top = new FormAttachment();

		progress.moveBelow(null);
		return pane;
	}

	@Override
	protected void dispose() {
		pushSession.stop();
		super.dispose();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		this.value = (List<RemoteFile>) value;
		presentation();
	}

	private void presentation() {
		if (label.isDisposed())
			return;

		String labelText = "";
		File[] targetFiles = receiver.getTargetFiles();
		if (targetFiles.length == 0) {// 没有上传文件
			if (this.value != null && !this.value.isEmpty()) {
				try {
					RemoteFile remoteFile = value.get(0);
					String url = Publisher.url + "/fs/" + remoteFile.namepace + "/" + remoteFile._id + "/"
							+ URLEncoder.encode(remoteFile.name, "utf-8");
					labelText = "<a style='color:#4a4a4a;' target='_blank' href='" + url + "'>" + remoteFile.name
							+ "</a>";
				} catch (UnsupportedEncodingException e) {
					labelText = "文件名含有非法字符";
				}
			} else {
				labelText = "请上传文件";
			}
		} else {
			String url = UserSession.bruiToolkit()
					.createLocalFileDownloadURL(targetFiles[targetFiles.length - 1].getPath());
			labelText = "<a style='color:#4a4a4a;' target='_blank' href='" + url + "'>"
					+ targetFiles[targetFiles.length - 1].getName() + "</a>";
		}

		label.setText("<div style='margin-top:8px;margin-left:16px'>" + labelText + "</div>");
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
		// 必填检查
		if (saveCheck && (fieldConfig.isRequired() && (value == null || value.isEmpty()))) {
			throw new Exception(fieldConfig.getFieldText() + "必填。");
		}
		// 类型检查

	}

	@Override
	protected void saveBefore() throws Exception {
		// 如果receiver中有文件，需要替换掉
		if (receiver.getTargetFiles().length > 0) {
			FileService fs = Services.get(FileService.class);
			File file = receiver.getTargetFiles()[receiver.getTargetFiles().length - 1];
			RemoteFile remoteFile = fs.upload(new FileInputStream(file), file.getName(), fieldConfig.getFileNamespace(),
					Util.getContentType(file, null), Brui.sessionManager.getSessionUserInfo().getUserId());
			if (value == null)
				value = new ArrayList<RemoteFile>();
			else
				value.clear();
			if (remoteFile == null)
				throw new Exception("文件保存失败。");
			value.add(remoteFile);
		}
		super.saveBefore();
	}

	@Override
	public void uploadProgress(FileUploadEvent event) {
		if (!display.isDisposed())
			display.asyncExec(() -> {
				if (!progress.isDisposed()) {
					int percent = (int) Math.floor(event.getBytesRead() / (double) event.getContentLength() * 100);
					progress.setSelection(percent);
					progress.setToolTipText(percent + "%");
				}
			});
	}

	@Override
	public void uploadFinished(FileUploadEvent event) {
		if (!display.isDisposed())
			display.asyncExec(() -> {
				presentation();
				resetProgress();
			});
	}

	@Override
	public void uploadFailed(FileUploadEvent event) {
		if (!display.isDisposed())
			display.asyncExec(() -> {
				resetProgress();
				if (!label.isDisposed())
					label.setText("上传失败");
			});
	}

	private void resetProgress() {
		if (!progress.isDisposed()) {
			progress.setSelection(0);
			progress.moveBelow(null);
		}
	}

}
