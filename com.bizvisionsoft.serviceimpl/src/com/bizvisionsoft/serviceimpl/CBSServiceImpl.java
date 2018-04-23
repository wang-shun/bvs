package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.bizvisionsoft.service.CBSService;
import com.bizvisionsoft.service.model.CBSItem;
import com.bizvisionsoft.service.model.CBSPeriod;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Field;

public class CBSServiceImpl extends BasicServiceImpl implements CBSService {

	/**
	 * 
	 * @return
	 */
	public List<CBSItem> query(BasicDBObject match) {
		/*
		 * db.getCollection('cbs').aggregate([
		 * {$lookup:{"from":"cbsPeriod","localField":"_id","foreignField":"cbsItem_id",
		 * "as":"_period"}},
		 * 
		 * {$addFields:{_budget: {$map:{"input":"$_period","as":"itm","in":{"k":
		 * "$$itm.id","v":"$$itm.budget"}}}}},
		 * 
		 * {$addFields:{"budgetTotal":{$sum:"$_budget.v"}}},
		 * 
		 * {$addFields:{"budget":{$arrayToObject: "$_budget" }}},
		 * 
		 * {$project:{"_period":false,"_budget":false}} ])
		 */
		List<Bson> pipeline = new ArrayList<Bson>();
		if (match != null)
			pipeline.add(Aggregates.match(match));
		pipeline.add(Aggregates.lookup("cbsPeriod", "_id", "cbsItem_id", "_period"));
		pipeline.add(Aggregates.addFields(//
				new Field<BasicDBObject>("_budget",
						new BasicDBObject("$map",
								new BasicDBObject("input", "$_period").append("as", "itm").append("in",
										new BasicDBObject("k", "$$itm.id").append("v", "$$itm.budget")))),
				new Field<BasicDBObject>("budgetTotal", new BasicDBObject("$sum", "$_budget.v")),
				new Field<BasicDBObject>("budget", new BasicDBObject("$arrayToObject", "$_budget"))));
		pipeline.add(Aggregates.project(new BasicDBObject("_period", false).append("_budget", false)));
		ArrayList<CBSItem> result = new ArrayList<CBSItem>();
		Service.col(CBSItem.class).aggregate(pipeline).into(result);
		return result;
	}

	/**
	 * db.getCollection('accountItem').aggregate([ {$match:{parent_id:null}},
	 * {$project:{_id:false,name:true,id:true,parent_id:"aaa",scope_id:"bbb"}} ])
	 * 
	 * @param items
	 * @param scope_id
	 * @param parent_id
	 * @param acountItemParent_id
	 */
	public void appendSubItemsFromTemplate(List<CBSItem> items, ObjectId scope_id, ObjectId parent_id,
			ObjectId acountItemParent_id) {
		Iterable<Document> iter = Service.col("accountItem").find(new BasicDBObject("parent_id", acountItemParent_id));
		iter.forEach(d -> {
			ObjectId _id = new ObjectId();
			CBSItem item = new CBSItem().set_id(_id)//
					.setName(d.getString("name"))//
					.setId(d.getString("id"))//
					.setScope_id(scope_id)//
					.setParent_id(acountItemParent_id);//
			items.add(item);
			appendSubItemsFromTemplate(items, scope_id, _id, d.getObjectId("_id"));
		});
	}

	@Override
	public List<CBSItem> getScopeRoot(ObjectId scope_id) {
		return createDataSet(
				new BasicDBObject("filter", new BasicDBObject("scope_id", scope_id).append("scopeRoot", true)),
				CBSItem.class);
	}

	@Override
	public CBSItem insertCBSItem(CBSItem o) {
		return insert(o, CBSItem.class);
	}

	@Override
	public List<CBSItem> getSubCBSItems(ObjectId parent_id) {
		return createDataSet(new BasicDBObject("filter", new BasicDBObject("parent_id", parent_id)), CBSItem.class);
	}

	@Override
	public long countSubCBSItems(ObjectId parent_id) {
		return count(new BasicDBObject("parent_id", parent_id), CBSItem.class);
	}

	@Override
	public void delete(ObjectId _id) {
		delete(_id, CBSItem.class);
	}

	@Override
	public CBSPeriod insertCBSPeriod(CBSPeriod o) {
		return insert(o, CBSPeriod.class);
	}

}
