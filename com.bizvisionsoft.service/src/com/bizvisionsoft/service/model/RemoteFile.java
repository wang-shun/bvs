package com.bizvisionsoft.service.model;

import org.bson.types.ObjectId;

import com.bizvisionsoft.mongocodex.annotations.Persistence;

public class RemoteFile {

	@Persistence
	public ObjectId _id;

	@Persistence
	public String name;

	@Persistence
	public String namepace;

	@Persistence
	public String contentType;

}
