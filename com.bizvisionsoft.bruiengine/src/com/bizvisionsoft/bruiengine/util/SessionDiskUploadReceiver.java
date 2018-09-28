package com.bizvisionsoft.bruiengine.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.fileupload.FileDetails;
import org.eclipse.rap.fileupload.FileUploadReceiver;

public class SessionDiskUploadReceiver extends FileUploadReceiver {

	private static final String DEFAULT_TARGET_FILE_NAME = "upload.tmp";

	private final List<File> targetFiles;
	private final List<FileDetails> targetFileDetails;

	public SessionDiskUploadReceiver() {
		targetFiles = new ArrayList<>();
		targetFileDetails = new ArrayList<>();
	}

	@Override
	public void receive(InputStream dataStream, FileDetails details) throws IOException {
		File targetFile = createTargetFile(details);
		FileOutputStream outputStream = new FileOutputStream(targetFile);
		EngUtil.copyStream(dataStream, outputStream, true);
		targetFiles.add(targetFile);
		targetFileDetails.add(details);
		File contentTypeFile = createContentTypeFile(targetFile, details);
		if (contentTypeFile != null) {
			PrintWriter pw = new PrintWriter(contentTypeFile);
			pw.print(details.getContentType());
			pw.close();
		}
	}

	public File[] getTargetFiles() {
		return targetFiles.toArray(new File[0]);
	}

	protected File createTargetFile(FileDetails details) throws IOException {
		String fileName = DEFAULT_TARGET_FILE_NAME;
		if (details != null && details.getFileName() != null) {
			fileName = details.getFileName();
		}
		File result = new File(EngUtil.createTempDirectory(), fileName);
		result.createNewFile();
		return result;
	}

	protected File createContentTypeFile(File uploadedFile, FileDetails details) throws IOException {
		String fileName = EngUtil.DEFAULT_CONTENT_TYPE_FILE_NAME;
		File result = null;
		if (details != null && details.getContentType() != null) {
			result = new File(uploadedFile.getParentFile(), fileName);
			result.createNewFile();
		}
		return result;
	}


	public void clear() {
		targetFileDetails.clear();
		targetFiles.clear();
	}

}
