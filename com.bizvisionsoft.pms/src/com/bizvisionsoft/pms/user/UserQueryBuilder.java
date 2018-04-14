package com.bizvisionsoft.pms.user;

import java.util.List;

import com.bizvisionsoft.annotations.md.mongocodex.Exclude;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.util.Util;
import com.bizvisionsoft.service.model.Organization;
import com.mongodb.BasicDBObject;

public class UserQueryBuilder {

	@Inject
	@Exclude
	private IBruiService bruiService;

	@Inject
	@Exclude
	private IBruiContext context;

	@WriteValue
	public Object userId;

	@WriteValue
	public Object name;

	public Object org_id;

	@WriteValue
	public Object activated;

	@WriteValue("orgIds")
	public void setOrgIds(List<Organization> orgs) {
		if (orgs == null || orgs.isEmpty()) {
			org_id = null;
		} else {
			org_id = new BasicDBObject("$in", Util.getList(orgs, Organization::get_id));
		}
	}

}
