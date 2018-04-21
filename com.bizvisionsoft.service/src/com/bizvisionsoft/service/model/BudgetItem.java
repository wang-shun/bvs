package com.bizvisionsoft.service.model;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.Exclude;
import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.Structure;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.service.CommonService;
import com.bizvisionsoft.service.ServicesLoader;

@PersistenceCollection("cbs")
public class BudgetItem {
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//基本的一些字段
	
	/** 标识 Y **/
	@ReadValue
	@WriteValue
	private ObjectId _id;
	
	@ReadValue
	@WriteValue
	private ObjectId parent_id;
	

	/** 编号 Y **/
	@ReadValue
	@WriteValue
	private String id;
	
	@ReadValue
	@WriteValue
	private String name;
	
	@ReadValue
	@WriteValue
	private String description;
	
	@ReadValue(ReadValue.TYPE)
	@Exclude
	private String typeName = "财务科目";
	
	@Override
	@Label
	public String toString() {
		return name + " [" + id + "]";
	}

	@Structure("list")
	private List<AccountItem> listSubAccountItems(){
		return ServicesLoader.get(CommonService.class).getAccoutItem(_id);
	}
	
	@Structure("count")
	private long countSubAccountItems(){
		return ServicesLoader.get(CommonService.class).countAccoutItem(_id);
	}
	
	@ReadValue
	@WriteValue
	private ObjectId scope_id;

	private Document period; 
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	private boolean scopeRoot;


	public ObjectId get_id() {
		return _id;
	}


	public BudgetItem setParent_id(ObjectId parent_id) {
		this.parent_id = parent_id;
		return this;
	}
	
	public BudgetItem set_id(ObjectId _id) {
		this._id = _id;
		return this;
	}
	
	public BudgetItem setScope_id(ObjectId scope_id) {
		this.scope_id = scope_id;
		return this;
	}

	public BudgetItem setScopeRoot(boolean scopeRoot) {
		this.scopeRoot = scopeRoot;
		return this;
	}

	public boolean isScopeRoot() {
		return scopeRoot;
	}
	
	public BudgetItem setName(String name) {
		this.name = name;
		return this;
	}
	
	public ObjectId getScope_id() {
		return scope_id;
	}

	public BudgetItem setId(String id) {
		this.id = id;
		return this;
	}
	
}
