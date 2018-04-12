package com.bizvisionsoft.service.model;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.mongocodex.SetValue;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;

@PersistenceCollection("account")
public class UserInfo {

	@SetValue
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

	@SetValue
	@ReadValue(" 用户列表 / 头像 ")
	private String headpicURL;

	@SetValue("orgId")
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

	public String getHeadpicURL() {
		return headpicURL;
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

}
