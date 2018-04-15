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
	//������һЩ�ֶ�
	
	/** ��ʶ Y **/
	@ReadValue
	@WriteValue
	private ObjectId _id;

	/** ��� Y **/
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
	private String typeName = "�豸��ʩ";
	
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
