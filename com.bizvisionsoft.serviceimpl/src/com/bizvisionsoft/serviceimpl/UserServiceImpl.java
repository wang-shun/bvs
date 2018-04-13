package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.model.Organization;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.service.model.UserInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;

public class UserServiceImpl extends BasicServiceImpl implements UserService {

	@Override
	public long update(BasicDBObject fu) {
		return update(fu, User.class);
	}

	@Override
	public User check(String userId, String password) {
		User user = Service.col(User.class).find(new BasicDBObject("userId", userId).append("password", password))
				.first();
		if (user != null && user.isActivated()) {
			return user;
		}
		throw new ServiceException("账户无法通过验证");
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
			throw new ServiceException("没有用户Id为" + userId + "的用户。");
		}
		return ds.get(0);
	}

	@Override
	public User insert(User user) {
		return insert(user, User.class);
	}

	@Override
	public List<User> createDataSetForSelector(BasicDBObject condition) {
		return createDataSet(condition, User.class);
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

		pipeline.add(Aggregates.lookup("organization", "org_id", "_id", "org"));

		pipeline.add(Aggregates.replaceRoot(new BasicDBObject("$mergeObjects", //
				new Object[] { new BasicDBObject("$arrayElemAt", new Object[] { "$org", 0 }), "$$ROOT" })));

		pipeline.add(Aggregates.project(new BasicDBObject()//
				.append("name", 1)//
				.append("tel", 1)//
				.append("userId", 1)//
				.append("email", 1)//
				.append("headPics", 1)//
				.append("org_id", 1)//
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
		return count(filter, User.class);
	}

	@Override
	public long delete(ObjectId _id) {
		MongoCollection<Document> col = Service.col("account");
		Document doc = col.find(new BasicDBObject("_id", _id))
				.projection(new BasicDBObject("activated", 1).append("userId", 1)).first();
		if (doc.getBoolean("activated", false))
			throw new ServiceException("不能删除激活状态的用户。");

		String userId = doc.getString("userId");
		BasicDBObject filter = new BasicDBObject().append("managerId", userId);
		if (count(filter, Organization.class) != 0)
			throw new ServiceException("不能删除在组织中担任管理者的用户。");

		// TODO 其他检查
		return delete(_id, User.class);
	}

}