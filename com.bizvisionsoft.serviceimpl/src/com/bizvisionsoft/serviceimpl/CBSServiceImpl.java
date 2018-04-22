package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.bizvisionsoft.service.CBSService;
import com.bizvisionsoft.service.model.CBSItem;
import com.mongodb.BasicDBObject;

public class CBSServiceImpl extends BasicServiceImpl implements CBSService {

	public void createCBS(CBSItem cbsRoot) {
		List<CBSItem> items = new ArrayList<>();
		items.add(cbsRoot);
//		appendSubItems(items, cbsRoot.getScope_id(), cbsRoot.get_id(), null);
		Service.col(CBSItem.class).insertMany(items);
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

}
