package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.bizvisionsoft.service.ProjectSetService;
import com.bizvisionsoft.service.model.ProjectSet;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Aggregates;

public class ProjectSetServiceImpl extends BasicServiceImpl implements ProjectSetService {

	@Override
	public ProjectSet insert(ProjectSet project) {
		return insert(project, ProjectSet.class);
	}

	@Override
	public ProjectSet get(ObjectId _id) {
		return get(_id, ProjectSet.class);
	}

	@Override
	public long count(BasicDBObject filter) {
		return count(filter, ProjectSet.class);
	}
	
	@Override
	public List<ProjectSet> createDataSet(BasicDBObject condition) {
		Integer skip = (Integer) condition.get("skip");
		Integer limit = (Integer) condition.get("limit");
		BasicDBObject filter = (BasicDBObject) condition.get("filter");
		return query(skip, limit, filter);
	}

	private List<ProjectSet> query(Integer skip, Integer limit, BasicDBObject filter) {
		ArrayList<Bson> pipeline = new ArrayList<Bson>();

		if (filter != null)
			pipeline.add(Aggregates.match(filter));

		if (skip != null)
			pipeline.add(Aggregates.skip(skip));

		if (limit != null)
			pipeline.add(Aggregates.limit(limit));

		//TODO

		List<ProjectSet> result = new ArrayList<ProjectSet>();
		Service.col(ProjectSet.class).aggregate(pipeline).into(result);
		return result;
		
	}

}
