package com.bizvisionsoft.demo.rsclient;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.bizvisionsoft.bruicommons.annotation.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.mongocodex.annotations.Exclude;
import com.bizvisionsoft.service.OrganizationService;
import com.bizvisionsoft.service.ServicesLoader;
import com.bizvisionsoft.service.annotations.ReadValue;
import com.bizvisionsoft.service.annotations.WriteValue;
import com.bizvisionsoft.service.model.Organization;

public class DemoQueryBuilder {

	@Inject
	@Exclude
	private IBruiService bruiService;

	@Inject
	@Exclude
	private IBruiContext context;

	@ReadValue(" 账号")
	@WriteValue(" 账号")
	public String userId;

	@ReadValue(" 位置")
	@WriteValue("位置")
	public String location;

	@ReadValue(" 姓名")
	@WriteValue("姓名")
	public String username;

	public ObjectId organizationId;

	
	@ReadValue
	@WriteValue
	private List<String> leaders;

	
	@ReadValue(" 激活")
	@WriteValue("激活")
	public Boolean activated;

	
	@WriteValue(" 组织 ")
	public void setOrganization(Organization org) {
		this.organizationId = org.get_id();
	}

	@ReadValue("组织")
	public Organization getOrganization() {
		return ServicesLoader.get(OrganizationService.class).get(organizationId);
	}
	
	private Date minDate;

	private Date maxDate;

	@ReadValue("testDateTimeRange")
	public Date[] getTestDateTimeRange() {
		return new Date[] { this.minDate, this.maxDate};
	}

	@WriteValue("testDateTimeRange")
	public void setTestDateTimeRange(Date[] range) {
		this.minDate = range[0];
		this.maxDate = range[1];
	}
}
