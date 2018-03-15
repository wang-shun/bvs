package com.bizvisionsoft.serviceimpl;

import java.io.InputStream;

import javax.ws.rs.core.Response;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.bizvisionsoft.service.FileService;
import com.bizvisionsoft.service.model.RemoteFile;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;

public class FileServiceImpl implements FileService {

	public Response get(String id, String fileName, String namespace) {
		return Response.ok().entity(GridFSBuckets.create(Service.db(), namespace).openDownloadStream(new ObjectId(id)))
				.header("Content-Disposition", "attachment; filename=" + fileName).build();
	}

	@Override
	public RemoteFile upload(InputStream fileInputStream, String fileName, String namespace, String contentType,
			String uploadBy) {
		GridFSUploadOptions option = new GridFSUploadOptions();
		option.metadata(new Document().append("contentType", contentType).append("uploadBy", uploadBy));
		ObjectId id = GridFSBuckets.create(Service.db(), namespace).uploadFromStream(fileName, fileInputStream, option);
		id.toString();
		RemoteFile rf = new RemoteFile();
		rf._id = id;
		rf.name = fileName;
		rf.namepace = namespace;
		rf.contentType = contentType;
		return rf;
	}

}
