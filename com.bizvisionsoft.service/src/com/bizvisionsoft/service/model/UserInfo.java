package com.bizvisionsoft.service.model;

import java.util.List;

import org.bson.types.ObjectId;

import com.bizvisionsoft.mongocodex.annotations.PersistenceCollection;
import com.bizvisionsoft.mongocodex.annotations.SetValue;
import com.bizvisionsoft.service.ServicesLoader;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.annotations.ReadValue;
import com.bizvisionsoft.service.annotations.Structure;
import com.bizvisionsoft.service.datatools.Query;

@PersistenceCollection("account")
public class UserInfo {

	@SetValue
	private ObjectId _id;

	@SetValue
	@ReadValue(" 用户列表 # 账户Id ")
	private String userId;

	@SetValue
	@ReadValue(" 用户列表 # 姓名 ")
	private String name;

	@SetValue
	@ReadValue(" 用户列表 # 电子邮件 ")
	private String email;

	@SetValue
	@ReadValue(" 用户列表 # 办公电话 ")
	private String tel;

	@SetValue
	@ReadValue(" 用户列表 # 移动电话 ")
	private String mobile;

	@SetValue
	@ReadValue(" 用户列表 # 微信 ")
	private String weixin;

	@SetValue
	@ReadValue(" 用户列表 # 激活 ")
	private boolean activated;

	@SetValue
	@ReadValue(" 用户列表 # 头像 ")
	private String headpicURL;

	@SetValue("orgId")
	private ObjectId organizationId;

	@SetValue("orgFullName")
	@ReadValue(" 用户列表 # 所属组织 ")
	private String organizationFullName;

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
		return organizationFullName;
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

	@Structure("用户列表#list")
	public List<UserInfo> getChildren() {
		return ServicesLoader.get(UserService.class).createDataSet(new Query().skip(0).limit(5).bson());
	}

	@Structure("用户列表#count")
	public long countChildren() {
		return 5;
	}

}
