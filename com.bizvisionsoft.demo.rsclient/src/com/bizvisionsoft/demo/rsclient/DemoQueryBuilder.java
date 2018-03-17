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

	@ReadValue(" �˺�")
	@WriteValue(" �˺�")
	public String userId;

	@ReadValue(" λ��")
	@WriteValue("λ��")
	public String location;

	@ReadValue(" ����")
	@WriteValue("����")
	public String username;

	public ObjectId organizationId;

	
	@ReadValue
	@WriteValue
	private List<String> leaders;

	
	@ReadValue(" ����")
	@WriteValue("����")
	public Boolean activated;

	
	@WriteValue(" ��֯ ")
	public void setOrganization(Organization org) {
		this.organizationId = org.get_id();
	}

	@ReadValue("��֯")
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
