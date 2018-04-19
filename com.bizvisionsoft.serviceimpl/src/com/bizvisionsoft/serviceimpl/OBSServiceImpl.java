package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.bizvisionsoft.service.OBSService;
import com.bizvisionsoft.service.model.OBSItem;
import com.mongodb.BasicDBObject;

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
	public List<OBSItem> getProjectOBS(ObjectId project_id) {
		ArrayList<OBSItem> result = new ArrayList<OBSItem>();
		List<Bson> pipeline = getOBSRootPipline(project_id);
		appendUserInfo(pipeline, "managerId", "managerInfo");
		Service.col("project").aggregate(pipeline, OBSItem.class).into(result);
		return result;
	}

	@Override
	public List<OBSItem> getSubOBSItem(ObjectId _id) {
		ArrayList<OBSItem> result = new ArrayList<OBSItem>();
		Service.col(OBSItem.class).find(new BasicDBObject("parent_id", _id)).into(result);
		return result;
	}

	@Override
	public long countSubOBSItem(ObjectId _id) {
		return Service.col(OBSItem.class).count(new BasicDBObject("parent_id", _id));
	}

}
