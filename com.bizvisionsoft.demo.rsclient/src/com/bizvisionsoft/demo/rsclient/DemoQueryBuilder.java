package com.bizvisionsoft.demo.rsclient;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.bizvisionsoft.bruicommons.annotation.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.util.Util;
import com.bizvisionsoft.mongocodex.annotations.Exclude;
import com.bizvisionsoft.service.annotations.WriteValue;
import com.bizvisionsoft.service.model.Organization;
import com.mongodb.BasicDBObject;

public class DemoQueryBuilder {

	@Inject
	@Exclude
	private IBruiService bruiService;

	@Inject
	@Exclude
	private IBruiContext context;

	@WriteValue(" 账号")
	public Object userId;

	@WriteValue("位置")
	public Object location;

	@WriteValue("姓名")
	public Object name;

	public Object orgIds;

	public ObjectId orgId;

	private Object leaders;

	@WriteValue("leaders")
	public void setLeaders(List<String> leaders) {
		if (Util.isEmptyOrNull(leaders)) {
			this.leaders = null;
		} else if (leaders.size() == 1) {
			this.leaders = leaders.get(0);
		} else {
			this.leaders = new BasicDBObject("$in", leaders);
		}
	}

	@WriteValue("激活")
	public Object activated;

	@WriteValue(" 组织 ")
	public void setOrganization(Organization org) {
		if (org == null)
			this.orgId = null;
		else
			this.orgId = org.get_id();
	}

	@WriteValue("orgIds")
	public void setOrgIds(List<Organization> orgs) {
		if (orgs == null || orgs.isEmpty()) {
			orgIds = null;
		} else {
			orgIds = new BasicDBObject("$in", Util.getList(orgs, o -> o.get_id()));
		}
	}

	@WriteValue
	private Object max;

	@WriteValue
	private Object min;

	private Date minDate;

	private Date maxDate;

	@WriteValue
	private Object lastLogin;

	@WriteValue("testDateTimeRange")
	public void setTestDateTimeRange(Date[] range) {
		this.minDate = range[0];
		this.maxDate = range[1];
	}
}
