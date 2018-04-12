package com.bizvisionsoft.pms.org.dataset;

import java.util.List;

import com.bizvisionsoft.annotations.md.service.DataSet;
import com.bizvisionsoft.annotations.md.service.ServiceParam;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.OrganizationService;
import com.bizvisionsoft.service.model.Organization;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class OrgMember {

	@Inject
	private BruiAssemblyContext context;

	@Inject
	private IBruiService bruiService;

	private Organization org;

	private OrganizationService service;

	@Init
	private void init() {
		org = (Organization) context.getInput();
		service = Services.get(OrganizationService.class);
	}

	@DataSet("组织成员/" + DataSet.LIST)
	public List<User> list(@ServiceParam(ServiceParam.CONDITION) BasicDBObject condition) {
		return service.getMember( condition,org.get_id());
	}

	@DataSet("组织成员/" + DataSet.COUNT)
	public long count(@ServiceParam(ServiceParam.FILTER) BasicDBObject filter) {
		return service.countMember(filter,org.get_id());
	}

}
