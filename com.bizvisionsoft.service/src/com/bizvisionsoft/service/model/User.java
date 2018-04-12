package com.bizvisionsoft.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.Persistence;
import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadOptions;
import com.bizvisionsoft.annotations.md.service.ReadValidation;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.service.OrganizationService;
import com.bizvisionsoft.service.ServicesLoader;
import com.bizvisionsoft.service.datatools.Query;
import com.mongodb.BasicDBObject;

@PersistenceCollection(value = "account")
public class User {

	@Persistence
	private ObjectId _id;// _id字段在插入集合时Codex会自动生成，并在插入完成后写入，无需指定生成器

	@Persistence
	@ReadValue
	private String userId;

	@Persistence
	private String password;

	@Persistence
	@ReadValue
	@WriteValue
	private String name;

	@Persistence
	@ReadValue
	@WriteValue
	private String email;

	@Persistence
	@ReadValue
	@WriteValue
	private String tel;

	@Persistence
	@ReadValue
	@WriteValue
	private String mobile;

	@Persistence
	@ReadValue
	@WriteValue
	private String weixin;

	@Persistence
	@ReadValue
	@WriteValue
	private boolean activated;

	@Persistence
	@ReadValue
	@WriteValue
	private List<RemoteFile> headPics;

	@Persistence
	@ReadValue(" 用户编辑器 / testCombo ")
	@WriteValue(" 用户编辑器 / testCombo ")
	private String location;

	@Persistence("org_id")
	private ObjectId organizationId;

	@Persistence
	@ReadValue(" 用户编辑器 / lastLogin ")
	@WriteValue(" 用户编辑器 / lastLogin ")
	private Date lastLogin;

	@Persistence
	private List<ObjectId> orgIds;

	@Persistence
	@ReadValue
	@WriteValue
	private String description;

	@Persistence
	@ReadValue
	@WriteValue
	private List<String> testMultiCheck;

	@Persistence
	@ReadValue
	@WriteValue
	private List<String> leaders;

	@Persistence
	private Double max;

	@Persistence
	private Double min;

	public String getEmail() {
		return email;
	}

	@ReadValue(" 用户编辑器 / 姓名 ")
	public String getName() {
		return name;
	}

	@ReadValue(" 用户编辑器 / 电话 ")
	public String getTel() {
		return tel;
	}

	@ReadValue("testNumberRange")
	public Double[] getTestNumberRange() {
		return new Double[] { this.min, this.max };
	}

	@WriteValue("testNumberRange")
	public void setTestNumberRange(Double[] range) {
		this.min = range[0];
		this.max = range[1];
	}

	public String getUserId() {
		return userId;
	}

	public String getHeadpicURL() {
		if (headPics != null && headPics.size() > 0)
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

	@ReadOptions({ " 用户编辑器 / testCombo ", "testMultiCheck" })
	public Map<String, Object> getOptionsForTestCombo() {
		LinkedHashMap<String, Object> options = new LinkedHashMap<String, Object>();
		options.put("北京", "PEK");
		options.put("上海", "SHA");
		options.put("广州", "CAN");
		options.put("武汉", "WUH");
		options.put("西安", "SIA");
		return options;
	}

	@ReadOptions(" 用户编辑器 / lastLogin ")
	public Map<String, Object> getMarksForTestDate() {
		LinkedHashMap<String, Object> options = new LinkedHashMap<String, Object>();
		options.put("0-0-15", "中旬");
		return options;
	}

	@WriteValue(" 用户编辑器 / organization ")
	public void setOrganization(Organization org) {
		this.organizationId = org.get_id();
	}

	@ReadValue(" 用户编辑器 / organization ")
	public Organization getOrganization() {
		return ServicesLoader.get(OrganizationService.class).get(organizationId);
	}

	@WriteValue(" 用户编辑器 / organizations ")
	public void setOrganizations(List<Organization> orglist) {
		this.orgIds = new ArrayList<ObjectId>();
		Optional.ofNullable(orglist).ifPresent(os -> os.forEach(o -> orgIds.add(o.get_id())));
	}

	@ReadValue(" 用户编辑器 / organizations ")
	public List<Organization> getOrganizations() {
		if (orgIds != null) {
			BasicDBObject condition = new Query()
					.filter(new BasicDBObject().append("_id", new BasicDBObject("$in", orgIds))).bson();
			return ServicesLoader.get(OrganizationService.class).createDataSet(condition);
		}
		return new ArrayList<Organization>();
	}

	/**
	 * 演示时间的最大值和最小值控制
	 * 
	 * @return
	 */
	@ReadValidation("lastLogin")
	public String[] maxAndMinDateLimit() {
		return new String[] { "2017-8-11 12:30:00", "2018-8-11 11:30:00" };
	}

	/**
	 * 超文本字段
	 */
	@Persistence
	@ReadValue
	@WriteValue
	private String demoHtml;

	/**
	 * 超文本字段
	 */
	@Persistence
	@ReadValue
	@WriteValue
	private String demoNote;

	@Override
	@Label
	public String toString() {
		return name + " [" + userId + "]";
	}

}
