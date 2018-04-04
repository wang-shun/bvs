package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.bizvisionsoft.service.ProjectService;
import com.bizvisionsoft.service.model.Project;
import com.bizvisionsoft.service.model.ProjectInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Aggregates;

public class ProjectServiceImpl extends BasicServiceImpl implements ProjectService {

	@Override
	public Project insert(Project project) {
		return insert(project, Project.class);
	}

	@Override
	public Project get(ObjectId _id) {
		return get(_id, Project.class);
	}

	@Override
	public long count(BasicDBObject filter) {
		return count(filter, Project.class);
	}

	@Override
	public List<ProjectInfo> createDataSet(BasicDBObject condition) {
		Integer skip = (Integer) condition.get("skip");
		Integer limit = (Integer) condition.get("limit");
		BasicDBObject filter = (BasicDBObject) condition.get("filter");
		return query(skip, limit, filter);
	}

	private List<ProjectInfo> query(Integer skip, Integer limit, BasicDBObject filter) {
		ArrayList<Bson> pipeline = new ArrayList<Bson>();

		if (filter != null)
			pipeline.add(Aggregates.match(filter));

		if (skip != null)
			pipeline.add(Aggregates.skip(skip));

		if (limit != null)
			pipeline.add(Aggregates.limit(limit));

		// TODO

		List<ProjectInfo> result = new ArrayList<ProjectInfo>();
		Service.col(ProjectInfo.class).aggregate(pipeline).into(result);
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

}
