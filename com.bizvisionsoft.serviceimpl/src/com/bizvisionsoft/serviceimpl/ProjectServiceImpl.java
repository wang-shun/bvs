package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.bizvisionsoft.service.ProjectService;
import com.bizvisionsoft.service.model.Project;
import com.bizvisionsoft.service.model.OBSItem;
import com.bizvisionsoft.serviceimpl.exception.ServiceException;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Aggregates;

public class ProjectServiceImpl extends BasicServiceImpl implements ProjectService {

	@Override
	public Project insert(Project input) {
		Project project;
		if (input.getProjectTemplate_id() == null) {
			/////////////////////////////////////////////////////////////////////////////
			// 0. 创建项目
			project = insert(input.setObs_id(new ObjectId()), Project.class);
			ObjectId projectSet_id = project.getProjectSet_id();

			/////////////////////////////////////////////////////////////////////////////
			// 1. 项目团队初始化
			ObjectId obsParent_id = null;
			if (projectSet_id != null) {
				Document doc = Service.col("projectSet").find(new BasicDBObject("_id", projectSet_id))
						.projection(new BasicDBObject("obs_id", true)).first();
				Optional.ofNullable(doc).map(d -> d.getObjectId("obs_id")).orElse(null);
			}

			new OBSServiceImpl().insert(new OBSItem()// 创建本项目的OBS根节点

					.set_id(project.getObs_id())// 设置_id与项目关联

					.setScope_id(project.get_id())// 设置scope_id表明该组织节点是该项目的组织

					.setParent_id(obsParent_id)// 设置上级的id

					.setName(project.getName() + "项目组")// 设置该组织节点的默认名称

					.setRoleId(OBSItem.ID_PM)// 设置该组织节点的角色id

					.setRoleName(OBSItem.NAME_PM)// 设置该组织节点的名称

					.setManagerId(project.getPmId()) // 设置该组织节点的角色对应的人

					.setScopeRoot(true) // 区分这个节点是范围内的根节点

			);// 插入记录
				//////////////////////////////////////////////////////////////////////////////

		} else {
			// TODO

			project = insert(input, Project.class);
		}
		return project;
	}

	@Override
	public Project get(ObjectId _id) {
		List<Project> ds = createDataSet(new BasicDBObject("filter", new BasicDBObject("_id", _id)));
		if (ds.size() == 0) {
			throw new ServiceException("没有_id为" + _id + "的项目。");
		}
		return ds.get(0);
	}

	@Override
	public long count(BasicDBObject filter) {
		return count(filter, Project.class);
	}

	@Override
	public List<Project> createDataSet(BasicDBObject condition) {
		Integer skip = (Integer) condition.get("skip");
		Integer limit = (Integer) condition.get("limit");
		BasicDBObject filter = (BasicDBObject) condition.get("filter");
		return query(skip, limit, filter);
	}

	private List<Project> query(Integer skip, Integer limit, BasicDBObject filter) {
		ArrayList<Bson> pipeline = new ArrayList<Bson>();

		if (filter != null)
			pipeline.add(Aggregates.match(filter));

		if (skip != null)
			pipeline.add(Aggregates.skip(skip));

		if (limit != null)
			pipeline.add(Aggregates.limit(limit));

		// TODO
		// 1. 承担组织
		appendOrgFullName(pipeline, "impUnit_id", "impUnitOrgFullName");

		appendUserInfo(pipeline, "pmId", "pmInfo");

		List<Project> result = new ArrayList<Project>();
		Service.col(Project.class).aggregate(pipeline).into(result);
		return result;

	}

	@Override
	public List<Date> getPlanDateRange(ObjectId _id) {
		Project data = Service.col(Project.class).find(new BasicDBObject("_id", _id))
				.projection(new BasicDBObject().append("planStart", 1).append("planFinish", 1)).first();
		ArrayList<Date> result = new ArrayList<Date>();
		result.add(data.getPlanStart());
		result.add(data.getPlanFinish());
		return result;
	}

	@Override
	public long update(BasicDBObject fu) {
		return update(fu, Project.class);
	}

}
