package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.bizvisionsoft.service.EPSService;
import com.bizvisionsoft.service.model.EPS;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;

public class EPSServiceImpl extends BasicServiceImpl implements EPSService {

	@Override
	public long update(BasicDBObject fu) {
		return update(fu, EPS.class);
	}

	@Override
	public EPS insert(EPS eps) {
		return insert(eps, EPS.class);
	}

	@Override
	public EPS get(ObjectId _id) {
		return get(_id, EPS.class);
	}

	@Override
	public List<EPS> createDataSet(BasicDBObject condition) {
		Integer skip = (Integer) condition.get("skip");
		Integer limit = (Integer) condition.get("limit");
		BasicDBObject filter = (BasicDBObject) condition.get("filter");
		return query(skip, limit, filter);
	}

	public List<EPS> query(Integer skip, Integer limit, BasicDBObject filter) {
		ArrayList<EPS> result = new ArrayList<EPS>();
		FindIterable<EPS> c = Service.col(EPS.class).find(new BasicDBObject("parent_id", null));
		if (skip != null)
			c.skip(skip.intValue());
		if (limit != null)
			c.limit(limit.intValue());
		c.into(result);
		return result;
	}

	@Override
	public long count(BasicDBObject filter) {
		return count(filter, EPS.class);
	}

	@Override
	public long delete(ObjectId _id) {
		return delete(_id,EPS.class);
	}


}
