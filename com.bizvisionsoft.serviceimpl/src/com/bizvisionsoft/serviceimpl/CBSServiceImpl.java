package com.bizvisionsoft.serviceimpl;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.bizvisionsoft.service.CBSService;
import com.bizvisionsoft.service.model.CBSItem;
import com.mongodb.BasicDBObject;

public class CBSServiceImpl extends BasicServiceImpl implements CBSService {

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

}
