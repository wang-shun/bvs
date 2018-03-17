package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

import org.bson.conversions.Bson;

import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.service.model.UserInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.UpdateOptions;

public class UserServiceImpl implements UserService {

	@Override
	public long update(BasicDBObject fu) {
		BasicDBObject filter = (BasicDBObject) fu.get("filter");
		BasicDBObject update = (BasicDBObject) fu.get("update");
		UpdateOptions option = new UpdateOptions();
		option.upsert(false);
		return Service.col(User.class).updateOne(filter, update, option).getModifiedCount();
	}

	@Override
	public User check(String userId, String password) {
		User user = Service.col(User.class).find(new BasicDBObject("userId", userId).append("password", password))
				.first();
		return Optional.ofNullable(user).orElseThrow(NotFoundException::new);
	}

	@Override
	public User get(String userId) {
		User user = Service.col(User.class).find(new BasicDBObject("userId", userId)).first();
		return Optional.ofNullable(user).orElseThrow(NotFoundException::new);
	}

	@Override
	public UserInfo info(String userId) {
		List<UserInfo> ds = createDataSet(new BasicDBObject().append("skip", 0).append("limit", 1).append("filter",
				new BasicDBObject("userId", userId)));
		if (ds.size() == 0) {
			throw new NotFoundException();
		}
		return ds.get(0);
	}

	@Override
	public User insert(User user) {
		Service.col(User.class).insertOne(user);
		return user;
	}

	@Override
	public List<UserInfo> createDataSet(BasicDBObject condition) {
		Integer skip = (Integer) condition.get("skip");
		Integer limit = (Integer) condition.get("limit");
		BasicDBObject filter = (BasicDBObject) condition.get("filter");
		return query(skip, limit, filter);
	}

	private List<UserInfo> query(Integer skip, Integer limit, BasicDBObject filter) {

		ArrayList<Bson> pipeline = new ArrayList<Bson>();

		if (filter != null)
			pipeline.add(Aggregates.match(filter));

		if (skip != null)
			pipeline.add(Aggregates.skip(skip));

		if (limit != null)
			pipeline.add(Aggregates.limit(limit));

		pipeline.add(Aggregates.lookup("organization", "orgId", "_id", "org"));

		pipeline.add(Aggregates.replaceRoot(new BasicDBObject("$mergeObjects", //
				new Object[] { new BasicDBObject("$arrayElemAt", new Object[] { "$org", 0 }), "$$ROOT" })));

		pipeline.add(Aggregates.project(new BasicDBObject()//
				.append("name", 1)//
				.append("tel", 1)//
				.append("userId", 1)//
				.append("email", 1)//
				.append("headPics", 1)//
				.append("orgId", 1)//
				.append("activated", 1)//
				.append("mobile", 1)//
				.append("weixin", 1)//
				.append("orgFullName", "$fullName")//
		));

		List<UserInfo> result = new ArrayList<UserInfo>();
		Service.col(UserInfo.class).aggregate(pipeline).into(result);
		return result;
	}

	@Override
	public long count(BasicDBObject filter) {
		if (filter != null)
			return Service.col(UserInfo.class).count(filter);
		return Service.col(UserInfo.class).count();
	}

}