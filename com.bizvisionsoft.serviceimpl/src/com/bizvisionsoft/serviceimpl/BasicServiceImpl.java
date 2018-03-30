package com.bizvisionsoft.serviceimpl;

import java.util.Optional;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.UpdateOptions;

public class BasicServiceImpl {

	protected <T> long update(BasicDBObject fu, Class<T> clazz) {
		BasicDBObject filter = (BasicDBObject) fu.get("filter");
		BasicDBObject update = (BasicDBObject) fu.get("update");
		UpdateOptions option = new UpdateOptions();
		option.upsert(false);
		return Service.col(clazz).updateOne(filter, update, option).getModifiedCount();
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

}
