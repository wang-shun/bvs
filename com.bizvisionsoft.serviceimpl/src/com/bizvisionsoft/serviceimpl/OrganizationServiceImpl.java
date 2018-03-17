package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.bizvisionsoft.service.OrganizationService;
import com.bizvisionsoft.service.model.Organization;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Aggregates;

public class OrganizationServiceImpl implements OrganizationService {

	@Override
	public Organization insert(Organization orgInfo) {
		Service.col(Organization.class).insertOne(orgInfo);
		return orgInfo;
	}

	@Override
	public Organization get(ObjectId _id) {
		Organization result = Service.col(Organization.class).find(new BasicDBObject("_id", _id)).first();
		return Optional.ofNullable(result).orElseThrow(NotFoundException::new);
	}

	@Override
	public List<Organization> createDataSet(BasicDBObject condition) {
		Integer skip = (Integer) condition.get("skip");
		Integer limit = (Integer) condition.get("limit");
		BasicDBObject filter = (BasicDBObject) condition.get("filter");
		return query(skip, limit, filter);
	}

	private List<Organization> query(Integer skip, Integer limit, BasicDBObject filter) {

		ArrayList<Bson> pipeline = new ArrayList<Bson>();

		if (filter != null)
			pipeline.add(Aggregates.match(filter));

		if (skip != null)
			pipeline.add(Aggregates.skip(skip));

		if (limit != null)
			pipeline.add(Aggregates.limit(limit));

		List<Organization> result = new ArrayList<Organization>();
		Service.col(Organization.class).aggregate(pipeline).into(result);
		return result;
	}

	@Override
	public long count(BasicDBObject filter) {
		if (filter != null)
			return Service.col(Organization.class).count(filter);
		return Service.col(Organization.class).count();
	}

}
