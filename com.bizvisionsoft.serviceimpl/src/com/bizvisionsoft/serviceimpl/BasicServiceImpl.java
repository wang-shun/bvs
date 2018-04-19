package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.UnwindOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

public class BasicServiceImpl {

	protected <T> long update(BasicDBObject fu, Class<T> clazz) {
		BasicDBObject filter = (BasicDBObject) fu.get("filter");
		BasicDBObject update = (BasicDBObject) fu.get("update");
		update.remove("_id");
		UpdateOptions option = new UpdateOptions();
		option.upsert(false);
		UpdateResult updateMany = Service.col(clazz).updateMany(filter, update, option);
		long cnt = updateMany.getModifiedCount();
		return cnt;
	}

	protected <T> T insert(T obj, Class<T> clazz) {
		Service.col(clazz).insertOne(obj);
		return obj;
	}

	protected <T> T get(ObjectId _id, Class<T> clazz) {
		T obj = Service.col(clazz).find(new BasicDBObject("_id", _id)).first();
		return Optional.ofNullable(obj).orElse(null);
	}

	protected <T> long delete(ObjectId _id, Class<T> clazz) {
		return Service.col(clazz).deleteOne(new BasicDBObject("_id", _id)).getDeletedCount();
	}

	protected <T> long count(BasicDBObject filter, Class<T> clazz) {
		if (filter != null)
			return Service.col(clazz).count(filter);
		return Service.col(clazz).count();
	}

	protected <T> List<T> createDataSet(BasicDBObject condition, Class<T> clazz) {
		Integer skip = (Integer) condition.get("skip");
		Integer limit = (Integer) condition.get("limit");
		BasicDBObject filter = (BasicDBObject) condition.get("filter");
		return query(skip, limit, filter, clazz);
	}

	private <T> List<T> query(Integer skip, Integer limit, BasicDBObject filter, Class<T> clazz) {
		ArrayList<Bson> pipeline = new ArrayList<Bson>();

		if (filter != null)
			pipeline.add(Aggregates.match(filter));

		if (skip != null)
			pipeline.add(Aggregates.skip(skip));

		if (limit != null)
			pipeline.add(Aggregates.limit(limit));

		List<T> result = new ArrayList<T>();
		Service.col(clazz).aggregate(pipeline).into(result);
		return result;
	}

	protected void appendOrgFullName(List<Bson> pipeline, String inputField, String outputField) {
		String tempField = "_org_" + inputField;

		pipeline.add(Aggregates.lookup("organization", inputField, "_id", tempField));

		pipeline.add(Aggregates.unwind("$" + tempField, new UnwindOptions().preserveNullAndEmptyArrays(true)));

		pipeline.add(Aggregates.addFields(new Field<String>(outputField, "$" + tempField + ".fullName")));

		pipeline.add(Aggregates.project(new BasicDBObject(tempField, false)));//
	}

	protected void appendUserInfo(List<Bson> pipeline, String inputField, String outputField) {
		String tempField = "_user_" + inputField;

		pipeline.add(Aggregates.lookup("account", inputField, "userId", tempField));

		pipeline.add(Aggregates.unwind("$" + tempField, new UnwindOptions().preserveNullAndEmptyArrays(true)));

		pipeline.add(Aggregates.addFields(new Field<BasicDBObject>(outputField, new BasicDBObject("$concat",
				new String[] { "$" + tempField + ".name", " [", "$" + tempField + ".userId", "]" }))));

		pipeline.add(Aggregates.project(new BasicDBObject(tempField, false)));//
	}

	@Deprecated
	protected List<Bson> getOBSRootPipline(ObjectId project_id) {
		List<Bson> pipeline = new ArrayList<Bson>();

		String tempField = "_obs" + project_id;
		pipeline.add(Aggregates.match(new BasicDBObject("_id", project_id)));
		pipeline.add(Aggregates.lookup("obs", "obs_id", "_id", tempField));
		pipeline.add(Aggregates.project(new BasicDBObject(tempField, true).append("_id", false)));
		pipeline.add(Aggregates.replaceRoot(new BasicDBObject("$mergeObjects",
				new Object[] { new BasicDBObject("$arrayElemAt", new Object[] { "$" + tempField, 0 }), "$$ROOT" })));
		pipeline.add(Aggregates.project(new BasicDBObject(tempField, false)));

		return pipeline;
	}

}
