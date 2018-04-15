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
import com.bizvisionsoft.service.OrganizationService;
import com.bizvisionsoft.service.ServicesLoader;

@PersistenceCollection("role")
public class Role {

	@ReadValue
	@WriteValue
	private ObjectId _id;

	@ReadValue
	@WriteValue
	private String id;

	@ReadValue
	@WriteValue
	private String name;

	@ReadValue
	@WriteValue
	private String description;

	@ReadValue
	@WriteValue
	private ObjectId org_id;

	public ObjectId get_id() {
		return _id;
	}

	public Role setOrg_id(ObjectId org_id) {
		this.org_id = org_id;
		return this;
	}

	@Override
	@Label
	public String toString() {
		return name + " [" + id + "]";
	}
	
	@ReadValue(ReadValue.TYPE)
	@Exclude
	private String typeName = "��ɫ";

	@Structure("��֯��ɫ/list")
	public List<User> getUser() {
		return ServicesLoader.get(OrganizationService.class).queryUsersOfRole(_id);
	}

	@Structure("��֯��ɫ/count")
	public long countUser() {
		return ServicesLoader.get(OrganizationService.class).countUsersOfRole(_id);
	}

	@Behavior({"��֯��ɫ/����û�","��֯��ɫ/�༭��ɫ"}) // ����action
	@Exclude // ���ó־û�
	private boolean behaviours = true;
	
}
