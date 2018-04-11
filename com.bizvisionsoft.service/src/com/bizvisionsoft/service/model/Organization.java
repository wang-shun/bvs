package com.bizvisionsoft.service.model;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.Persistence;
import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;

@PersistenceCollection(value = "organization")
public class Organization {

	@Persistence
	private ObjectId _id;

	/**
	 * 组织编号
	 */
	@Persistence
	@ReadValue
	@WriteValue
	private String id;

	/**
	 * 上级组织
	 */
	@Persistence
	private ObjectId parentId;

	/**
	 * 组织名称
	 */
	@Persistence
	@ReadValue
	@WriteValue
	private String name;

	/**
	 * 全称
	 */
	@Persistence
	@ReadValue
	@WriteValue
	@Label
	private String fullName;

	/**
	 * 组织类型
	 */
	@Persistence
	@ReadValue
	@WriteValue
	private String type;

	public ObjectId getParentId() {
		return parentId;
	}

	public Organization setParentId(ObjectId parentId) {
		this.parentId = parentId;
		return this;
	}

	public String getName() {
		return name;
	}

	public Organization setName(String name) {
		this.name = name;
		return this;
	}

	public String getFullName() {
		return fullName;
	}

	public Organization setFullName(String fullName) {
		this.fullName = fullName;
		return this;
	}

	public String getType() {
		return type;
	}

	public Organization setType(String type) {
		this.type = type;
		return this;
	}

	public ObjectId get_id() {
		return _id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Organization other = (Organization) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}

}
