package com.bizvisionsoft.service.model;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.Exclude;
import com.bizvisionsoft.annotations.md.mongocodex.Persistence;
import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;

@PersistenceCollection("equipment")
public class Equipment {
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//基本的一些字段
	
	/** 标识 Y **/
	@ReadValue
	@WriteValue
	private ObjectId _id;

	/** 编号 Y **/
	@ReadValue
	@WriteValue
	private String id;
	
	@ReadValue
	@WriteValue
	private String name;
	
	@ReadValue
	@WriteValue
	private String description;

	@ReadValue(ReadValue.TYPE)
	@Exclude
	private String typeName = "设备设施";
	
	@Override
	@Label
	public String toString() {
		return name + " [" + id + "]";
	}
	////////////////////////////////////////////////////////////////////////////////////////////////

	@Persistence
	@ReadValue
	@WriteValue
	private ObjectId resourceType_id;

	public ObjectId get_id() {
		return _id;
	}

}
