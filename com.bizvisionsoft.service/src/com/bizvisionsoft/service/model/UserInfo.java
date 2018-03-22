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
	@ReadValue(" �û��б� # �˻�Id ")
	private String userId;

	@SetValue
	@ReadValue(" �û��б� # ���� ")
	private String name;

	@SetValue
	@ReadValue(" �û��б� # �����ʼ� ")
	private String email;

	@SetValue
	@ReadValue(" �û��б� # �칫�绰 ")
	private String tel;

	@SetValue
	@ReadValue(" �û��б� # �ƶ��绰 ")
	private String mobile;

	@SetValue
	@ReadValue(" �û��б� # ΢�� ")
	private String weixin;

	@SetValue
	@ReadValue(" �û��б� # ���� ")
	private boolean activated;

	@SetValue
	@ReadValue(" �û��б� # ͷ�� ")
	private String headpicURL;

	@SetValue("orgId")
	private ObjectId organizationId;

	@SetValue("orgFullName")
	@ReadValue(" �û��б� # ������֯ ")
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

	@Structure("�û��б�#list")
	public List<UserInfo> getChildren() {
		return ServicesLoader.get(UserService.class).createDataSet(new Query().skip(0).limit(5).bson());
	}

	@Structure("�û��б�#count")
	public long countChildren() {
		return 5;
	}

}
