package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.bizvisionsoft.service.OBSService;
import com.bizvisionsoft.service.model.OBSItem;
import com.bizvisionsoft.service.model.User;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Aggregates;

public class OBSServiceImpl extends BasicServiceImpl implements OBSService {

	/**
	 * db.getCollection('project').aggregate([
	 * {$lookup:{"from":"obs","localField":"obs_id","foreignField":"_id","as":"obs"}},
	 * {$project:{"obs":true,"_id":false}},
	 * 
	 * {$replaceRoot: { newRoot: {$mergeObjects: [ { $arrayElemAt: [ "$obs", 0 ] },
	 * "$$ROOT" ] } }}, {$project:{"obs":false}} ])
	 */
	@Override
	public List<OBSItem> getScopeRootOBS(ObjectId scope_id) {
		return query(new BasicDBObject("scope_id", scope_id).append("scopeRoot", true));
	}

	private List<OBSItem> query(BasicDBObject match) {
		ArrayList<OBSItem> result = new ArrayList<OBSItem>();
		List<Bson> pipeline = new ArrayList<Bson>();
		pipeline.add(Aggregates.match(match));

		appendUserInfoAndHeadPic(pipeline, "managerId", "managerInfo","managerHeadPic");
		Service.col(OBSItem.class).aggregate(pipeline).into(result);
		return result;
	}

	@Override
	public List<OBSItem> getSubOBSItem(ObjectId _id) {
		return query(new BasicDBObject("parent_id", _id));
	}
	
	@Override
	public List<OBSItem> getScopeOBS(ObjectId scope_id) {
		return query(new BasicDBObject("scope_id", scope_id));
	}

	@Override
	public long countSubOBSItem(ObjectId _id) {
		return Service.col(OBSItem.class).count(new BasicDBObject("parent_id", _id));
	}

	@Override
	public OBSItem insert(OBSItem obsItem) {
		return insert(obsItem, OBSItem.class);
	}

	@Override
	public long update(BasicDBObject filterAndUpdate) {
		return update(filterAndUpdate, OBSItem.class);
	}

	@Override
	public OBSItem get(ObjectId _id) {
		return get(_id, OBSItem.class);
	}

	@Override
	public void delete(ObjectId _id) {
		// TODO Auto-generated method stub
		delete(_id, OBSItem.class);
	}

	@Override
	public List<User> getMember(BasicDBObject condition, ObjectId obs_id) {
		List<String> userIds = getMemberUserId(obs_id);
		if (userIds.isEmpty()) {
			return new ArrayList<User>();
		} else {
			BasicDBObject filter = (BasicDBObject) condition.get("filter");
			if (filter == null) {
				filter = new BasicDBObject();
				condition.append("filter", filter);
			}
			if (!filter.containsField("userId")) {
				filter.append("userId", new BasicDBObject("$in", userIds));
			}
			return new UserServiceImpl().createDataSet(condition);
		}
	}

	private ArrayList<String> getMemberUserId(ObjectId obs_id) {
		ArrayList<String> result = new ArrayList<String>();
		Service.col("obs").distinct("member", new BasicDBObject("_id", obs_id), String.class).into(result);
		return result;
	}

	@Override
	public long countMember(BasicDBObject filter, ObjectId obs_id) {
		List<String> userIds = getMemberUserId(obs_id);
		if (userIds == null) {
			return 0;
		} else {
			if (filter == null) {
				filter = new BasicDBObject();
			}
			if (!filter.containsField("userId")) {
				filter.append("userId", new BasicDBObject("$in", userIds));
			}
			return new UserServiceImpl().count(filter);
		}
	}


}
