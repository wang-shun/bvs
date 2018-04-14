package com.bizvisionsoft.service.model;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.Exclude;
import com.bizvisionsoft.annotations.md.mongocodex.Persistence;
import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.mongocodex.SetValue;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;

@PersistenceCollection("account")
public class UserInfo {

	@Persistence
	private ObjectId _id;

	@SetValue
	@ReadValue
	private String userId;

	@SetValue
	@ReadValue
	private String name;

	@SetValue
	@ReadValue
	private String email;

	@SetValue
	@ReadValue
	private String tel;

	@SetValue
	@ReadValue
	private String mobile;

	@SetValue
	@ReadValue
	private String weixin;

	@SetValue
	@ReadValue
	private boolean activated;

	@SetValue("org_id")
	private ObjectId organizationId;

	@SetValue
	@ReadValue
	private String orgFullName;

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getTel() {
		return tel;
	}

	public String getUserId() {
		return userId;
	}

	public String getOrganizationFullName() {
		return orgFullName;
	}

	public ObjectId getOrganizationId() {
		return organizationId;
	}

	public ObjectId get_id() {
		return _id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	@Label
	public String toString() {
		return name + " [" + userId + "]";
	}

	@ReadValue(ReadValue.TYPE)
	@Exclude
	private String typeName = "”√ªß";
}
