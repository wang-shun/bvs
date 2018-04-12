package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.bizvisionsoft.service.OrganizationService;
import com.bizvisionsoft.service.model.Organization;
import com.bizvisionsoft.service.model.User;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.UnwindOptions;

public class OrganizationServiceImpl extends BasicServiceImpl implements OrganizationService {

	@Override
	public Organization insert(Organization orgInfo) {
		return insert(orgInfo, Organization.class);
	}

	@Override
	public Organization get(ObjectId _id) {
		return get(_id, Organization.class);
	}

	@Override
	public List<Organization> createDataSet(BasicDBObject condition) {
		return createDataSet(condition, Organization.class);
	}

	@Override
	public long count(BasicDBObject filter) {
		return count(filter, Organization.class);
	}

	@Override
	public long update(BasicDBObject fu) {
		return update(fu, Organization.class);
	}

	@Override
	public List<Organization> getRoot() {
		return getSub(null);
	}

	@Override
	public long countRoot() {
		return countSub(null);
	}

	/**
	 * 
	 * db.getCollection('organization').aggregate( [
	 * {"$lookup":{"from":"account","localField":"managerId","foreignField":"userId","as":"user"}}
	 * , {"$unwind":{"path":"$user","preserveNullAndEmptyArrays":true}} ,
	 * {"$addFields":{"managerInfo":{"$concat":["$user.name","
	 * [","$user.userId","]"]}}} , {"$project":{"user":0}} ])
	 * 
	 */
	@Override
	public List<Organization> getSub(ObjectId parent_id) {
		ArrayList<Bson> pipeline = new ArrayList<Bson>();

		pipeline.add(Aggregates.match(new BasicDBObject("parent_id", parent_id)));

		pipeline.add(Aggregates.lookup("account", "managerId", "userId", "user"));

		pipeline.add(Aggregates.unwind("$user", new UnwindOptions().preserveNullAndEmptyArrays(true)));

		pipeline.add(Aggregates.addFields(new Field<BasicDBObject>("managerInfo",
				new BasicDBObject("$concat", new String[] { "$user.name", " [", "$user.userId", "]" }))));

		pipeline.add(Aggregates.project(new BasicDBObject("user", 0)));//

		List<Organization> result = new ArrayList<Organization>();
		Service.col(Organization.class).aggregate(pipeline).into(result);
		return result;
	}

	@Override
	public long countSub(ObjectId parent_id) {
		return Service.col(Organization.class).count(new BasicDBObject("parent_id", parent_id));
	}

	public long countMember(ObjectId _id) {
		return Service.col(User.class).count(new BasicDBObject("org_id", _id));
	}

	@Override
	public long delete(ObjectId _id) {
		// 检查
		if (countSub(_id) > 0)
			throw new ServiceException("不允许删除有下级的组织");

		if (countMember(_id) > 0)
			throw new ServiceException("不允许删除有成员的组织");

		// TODO
		return delete(_id, Organization.class);
	}

	@Override
	public List<User> getMember(BasicDBObject condition,ObjectId org_id) {
		BasicDBObject filter = (BasicDBObject) condition.get("filter");
		if (filter == null) {
			filter = new BasicDBObject();
			condition.put("filter", filter);
		}
		filter.put("org_id", org_id);
		return createDataSet(condition, User.class);
	}

	@Override
	public long countMember(BasicDBObject filter,ObjectId org_id) {
		if(filter == null) {
			filter = new BasicDBObject();
		}
		filter.put("org_id", org_id);
		return count(filter, User.class);
	}

}
