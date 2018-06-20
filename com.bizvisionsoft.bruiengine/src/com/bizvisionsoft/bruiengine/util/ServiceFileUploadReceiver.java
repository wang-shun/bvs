package com.bizvisionsoft.bruiengine.util;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rap.fileupload.FileDetails;
import org.eclipse.rap.fileupload.FileUploadReceiver;

import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.service.FileService;
import com.bizvisionsoft.service.model.RemoteFile;
import com.bizvisionsoft.serviceconsumer.Services;

/**
 * A file upload receiver that stores received files on disk.
 */
public class ServiceFileUploadReceiver extends FileUploadReceiver {

	private String namespace;
	private RemoteFile remoteFile;

	public ServiceFileUploadReceiver(String namespace) {
		this.namespace = namespace;
	}

	@Override
	public void receive(InputStream dataStream, FileDetails details) throws IOException {
		FileService fs = Services.get(FileService.class);
		remoteFile = fs.upload(dataStream, details.getFileName(), namespace, details.getContentType(),
				Brui.sessionManager.getUser().getUserId());
	}

	public RemoteFile getRemoteFile() {
		return remoteFile;
	}
}
