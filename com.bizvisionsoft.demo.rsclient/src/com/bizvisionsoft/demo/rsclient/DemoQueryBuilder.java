package com.bizvisionsoft.demo.rsclient;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.bizvisionsoft.bruicommons.annotation.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.mongocodex.annotations.Exclude;
import com.bizvisionsoft.service.annotations.WriteValue;
import com.bizvisionsoft.service.model.Organization;

public class DemoQueryBuilder {

	@Inject
	@Exclude
	private IBruiService bruiService;

	@Inject
	@Exclude
	private IBruiContext context;

	@WriteValue(" �˺�")
	public Object userId;

	@WriteValue("λ��")
	public Object location;

	@WriteValue("����")
	public Object name;

	public ObjectId organizationId;

	@WriteValue
	private List<String> leaders;

	@WriteValue("����")
	public Boolean activated;

	@WriteValue(" ��֯ ")
	public void setOrganization(Organization org) {
		this.organizationId = org.get_id();
	}

	@WriteValue
	private Object max;
	
	@WriteValue
	private Object min;

	private Date minDate;

	private Date maxDate;

	@WriteValue("testDateTimeRange")
	public void setTestDateTimeRange(Date[] range) {
		this.minDate = range[0];
		this.maxDate = range[1];
	}
}
