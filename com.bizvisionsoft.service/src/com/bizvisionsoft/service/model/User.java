package com.bizvisionsoft.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.bizvisionsoft.mongocodex.annotations.Persistence;
import com.bizvisionsoft.mongocodex.annotations.PersistenceCollection;
import com.bizvisionsoft.service.OrganizationService;
import com.bizvisionsoft.service.ServicesLoader;
import com.bizvisionsoft.service.annotations.ReadOptions;
import com.bizvisionsoft.service.annotations.ReadValue;
import com.bizvisionsoft.service.annotations.WriteValue;
import com.bizvisionsoft.service.datatools.Query;
import com.mongodb.BasicDBObject;

@PersistenceCollection(value = "account")
public class User {

	@Persistence
	private ObjectId _id;// _id�ֶ��ڲ��뼯��ʱCodex���Զ����ɣ����ڲ�����ɺ�д�룬����ָ��������

	@Persistence
	@ReadValue(" �û��༭�� # �˺� ")
	private String userId;

	@Persistence
	private String password;

	@Persistence
	@WriteValue(" �û��༭�� # ���� ")
	private String name;

	@Persistence
	@ReadValue(" �û��༭�� # �����ʼ� ")
	@WriteValue(" �û��༭�� # �����ʼ� ")
	private String email;

	@Persistence
	@WriteValue(" �û��༭�� # �绰 ")
	private String tel;

	@Persistence
	@ReadValue(" �û��༭�� # �ƶ��绰 ")
	@WriteValue(" �û��༭�� # �ƶ��绰 ")
	private String mobile;

	@Persistence
	@ReadValue(" �û��༭�� # ΢���˺� ")
	@WriteValue(" �û��༭�� # ΢���˺� ")
	private String weixin;

	@Persistence
	@ReadValue(" �û��༭�� # ���� ")
	@WriteValue(" �û��༭�� # ���� ")
	private boolean activated;

	@Persistence
	@ReadValue
	@WriteValue
	private List<RemoteFile> headPics;

	@Persistence
	@ReadValue(" �û��༭�� # testCombo ")
	@WriteValue(" �û��༭�� # testCombo ")
	private String location;

	@Persistence("orgId")
	private ObjectId organizationId;

	@Persistence
	@ReadValue(" �û��༭�� # lastLogin ")
	@WriteValue(" �û��༭�� # lastLogin ")
	private Date lastLogin;

	@Persistence
	private List<ObjectId> orgIds;

	@Persistence
	@ReadValue
	@WriteValue
	private List<String> leaders;

	public String getEmail() {
		return email;
	}

	@ReadValue(" �û��༭�� # ���� ")
	public String getName() {
		return name;
	}

	@ReadValue(" �û��༭�� # �绰 ")
	public String getTel() {
		return tel;
	}

	public String getUserId() {
		return userId;
	}

	public String getHeadpicURL() {
		if (headPics != null && headPics.size()>0) 
			return headPics.get(0).getURL(ServicesLoader.url);
		return null;
	}

	public User setActivated(boolean activated) {
		this.activated = activated;
		return this;
	}

	public User setEmail(String email) {
		this.email = email;
		return this;
	}

	public User setName(String name) {
		this.name = name;
		return this;
	}

	public User setTel(String tel) {
		this.tel = tel;
		return this;
	}

	public User setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public User setPassword(String password) {
		this.password = password;
		return this;
	}

	public ObjectId get_id() {
		return _id;
	}

	public ObjectId getOrganizationId() {
		return organizationId;
	}

	public User setOrganizationId(ObjectId organizationId) {
		this.organizationId = organizationId;
		return this;
	}

	public String getMobile() {
		return mobile;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	@ReadOptions(" �û��༭�� # testCombo ")
	public Map<String, Object> getOptionsForTestCombo() {
		LinkedHashMap<String, Object> options = new LinkedHashMap<String, Object>();
		options.put("����", "PEK");
		options.put("�Ϻ�", "SHA");
		options.put("����", "CAN");
		options.put("�人", "WUH");
		options.put("����", "SIA");
		return options;
	}

	@ReadOptions(" �û��༭�� # lastLogin ")
	public Map<String, Object> getMarksForTestDate() {
		LinkedHashMap<String, Object> options = new LinkedHashMap<String, Object>();
		options.put("0-0-15", "��Ѯ");
		return options;
	}

	@WriteValue(" �û��༭�� # organization ")
	public void setOrganization(Organization org) {
		this.organizationId = org.get_id();
	}

	@ReadValue(" �û��༭�� # organization ")
	public Organization getOrganization() {
		return ServicesLoader.get(OrganizationService.class).get(organizationId);
	}

	@WriteValue(" �û��༭�� # organizations ")
	public void setOrganizations(List<Organization> orglist) {
		this.orgIds = new ArrayList<ObjectId>();
		Optional.ofNullable(orglist).ifPresent(os -> os.forEach(o -> orgIds.add(o.get_id())));
	}

	@ReadValue(" �û��༭�� # organizations ")
	public List<Organization> getOrganizations() {
		if (orgIds != null) {
			BasicDBObject condition = new Query()
					.filter(new BasicDBObject().append("_id", new BasicDBObject("$in", orgIds))).bson();
			return ServicesLoader.get(OrganizationService.class).createDataSet(condition);
		}
		return new ArrayList<Organization>();
	}

}
