package com.bizvisionsoft.service.model;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;

@PersistenceCollection("role")
public class Role {
	
	@ReadValue
	@WriteValue
	private ObjectId _id;
	
	@ReadValue
	@WriteValue
	private String id;
	
	@ReadValue
	@WriteValue
	private String name;

	@ReadValue
	@WriteValue
	private String description;
	
	@ReadValue
	@WriteValue
	private ObjectId org_id;
	
	@ReadValue
	private String typeName = "½ÇÉ«";
	
	public ObjectId get_id() {
		return _id;
	}
	
	public Role setOrg_id(ObjectId org_id) {
		this.org_id = org_id;
		return this;
	}
	
	
}
