package com.bizvisionsoft.service.model;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.Exclude;
import com.bizvisionsoft.annotations.md.mongocodex.Persistence;
import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.mongocodex.SetValue;
import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.Structure;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.service.OBSService;
import com.bizvisionsoft.service.ServicesLoader;
import com.bizvisionsoft.service.UserService;

@PersistenceCollection("obs")
public class OBSItem {

	@Exclude
	public static final String ID_PM = "PM";

	@Exclude
	public static final String NAME_PM = "项目经理";

	@Override
	@Label
	public String toString() {
		return name + " [" + id + "]";
	}

	@Behavior("项目团队/添加")
	public boolean behaviorAddItem() {
		return true;
	}

	@Behavior({ "项目团队/编辑", "项目团队/删除" })
	public boolean behaviorEditOrDeleteItem() {
		return parent_id != null;// 根节点能编辑和删除
	}

	@ReadValue(ReadValue.TYPE)
	@Exclude
	private final String typeName = "项目团队";

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@ReadValue
	@WriteValue
	@Persistence
	private ObjectId _id;

	/**
	 * 上级组织
	 */
	@ReadValue
	@WriteValue
	private ObjectId parent_id;

	@ReadValue
	@WriteValue
	private ObjectId eps_id;

	private ObjectId linkedOrg_id;

	private ObjectId linkRole_id;

	private List<String> member;

	@Persistence
	private String managerId;

	@ReadValue("managerInfo")
	@WriteValue("managerInfo")
	@SetValue
	private String managerInfo;

	@WriteValue("manager")
	private void setManager(User manager) {
		if (manager == null) {
			managerId = null;
			managerInfo = "";
		} else {
			managerId = manager.getUserId();
			managerInfo = manager.toString();
		}
	}

	@ReadValue("manager")
	private User getManager() {
		return Optional.ofNullable(managerId).map(id -> {
			try {
				return ServicesLoader.get(UserService.class).get(id);
			} catch (Exception e) {
				return null;
			}
		}).orElse(null);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ReadValue
	@WriteValue
	private String id;

	@ReadValue
	@WriteValue
	private String name;

	@ReadValue
	@WriteValue
	private String description;

	@Structure("项目团队/list")
	public List<OBSItem> getSubOBSItem() {
		return ServicesLoader.get(OBSService.class).getSubOBSItem(_id);
	}

	@Structure("项目团队/count")
	public long countSubOBSItem() {
		return ServicesLoader.get(OBSService.class).countSubOBSItem(_id);
	}

	public OBSItem setId(String id) {
		this.id = id;
		return this;
	}

	public OBSItem setDescription(String description) {
		this.description = description;
		return this;
	}

	public OBSItem setManagerId(String managerId) {
		this.managerId = managerId;
		return this;
	}

	public OBSItem setName(String name) {
		this.name = name;
		return this;
	}

	public OBSItem setParent_id(ObjectId parent_id) {
		this.parent_id = parent_id;
		return this;
	}

	public ObjectId get_id() {
		return _id;
	}

}
