package com.bizvisionsoft.service.model;

import java.util.List;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.Exclude;
import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.Structure;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.service.CBSService;
import com.bizvisionsoft.service.ServicesLoader;

@PersistenceCollection("cbs")
public class CBSItem {

	////////////////////////////////////////////////////////////////////////////////////////////////
	// 基本的一些字段

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
	private String typeName = "CBS";

	@Override
	@Label
	public String toString() {
		return name + " [" + id + "]";
	}

	@Structure("list")
	private List<CBSItem> listSubCBSItems() {
		return ServicesLoader.get(CBSService.class).getSubCBSItems(_id);
	}

	@Structure("count")
	private long countSubCBSItems() {
		return ServicesLoader.get(CBSService.class).countSubCBSItems(_id);
	}

	@ReadValue
	@WriteValue
	private ObjectId scope_id;

	@Behavior({ "CBS/删除", "CBS/编辑", "CBS/科目" })
	private boolean behaviour() {
		return !scopeRoot;
		// TODO 传参数问题
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	private boolean scopeRoot;

	public ObjectId get_id() {
		return _id;
	}

	public CBSItem setParent_id(ObjectId parent_id) {
		this.parent_id = parent_id;
		return this;
	}

	public CBSItem set_id(ObjectId _id) {
		this._id = _id;
		return this;
	}

	public CBSItem setScope_id(ObjectId scope_id) {
		this.scope_id = scope_id;
		return this;
	}

	public CBSItem setScopeRoot(boolean scopeRoot) {
		this.scopeRoot = scopeRoot;
		return this;
	}

	public boolean isScopeRoot() {
		return scopeRoot;
	}

	public CBSItem setName(String name) {
		this.name = name;
		return this;
	}

	public ObjectId getScope_id() {
		return scope_id;
	}

	public CBSItem setId(String id) {
		this.id = id;
		return this;
	}

}
