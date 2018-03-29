package com.bizvisionsoft.service.model;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;

@PersistenceCollection("eps")
public class EPS implements Comparable<EPS> {

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 标识属性
	/**
	 * _id
	 */
	private ObjectId _id;

	/**
	 * 编号
	 */
	@ReadValue
	@WriteValue
	private String id;

	/**
	 * 父节点
	 */
	@ReadValue
	@WriteValue
	private ObjectId parent_id;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 描述属性
	/**
	 * 名称
	 */
	@ReadValue
	@WriteValue
	private String name;

	/**
	 * 描述
	 */
	@ReadValue
	@WriteValue
	private String description;

	public ObjectId get_id() {
		return _id;
	}

	public void setParent_id(ObjectId parent_id) {
		this.parent_id = parent_id;
	}

	public ObjectId getParent_id() {
		return parent_id;
	}

	@Override
	public int compareTo(EPS o) {
		return id.compareTo(o.id);
	}

}
