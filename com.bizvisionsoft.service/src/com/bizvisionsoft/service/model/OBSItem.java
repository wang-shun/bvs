package com.bizvisionsoft.service.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.Exclude;
import com.bizvisionsoft.annotations.md.mongocodex.GetValue;
import com.bizvisionsoft.annotations.md.mongocodex.Persistence;
import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.mongocodex.SetValue;
import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadOptions;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.Structure;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.service.CommonService;
import com.bizvisionsoft.service.OBSService;
import com.bizvisionsoft.service.OrganizationService;
import com.bizvisionsoft.service.ServicesLoader;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.tools.Util;

@PersistenceCollection("obs")
public class OBSItem {

	@Exclude
	public static final String ID_PM = "PM";

	@Exclude
	public static final String NAME_PM = "��Ŀ����";

	@Exclude
	public static final String ID_CHARGER = "WM";

	@Exclude
	public static final String NAME_CHARGER = "������";

	@Override
	@Label
	@ReadValue("��Ŀ�Ŷ�/label")
	public String toString() {
		String txt = "";
		if (name != null && !name.isEmpty())
			txt += name;

		if (roleName != null && !roleName.isEmpty())
			txt += " " + roleName;

		if (roleId != null && !roleId.isEmpty())
			txt += " [" + roleId + "]";

		if (managerInfo != null && !managerInfo.isEmpty())
			txt += " (" + managerInfo + ")";

		return txt;
	}

	@Behavior({ "ѡ���ɫ", "������ɫ", "�����Ŷ�", "�༭" })
	public boolean behaviorAddItem() {
		return true;
	}

	@Behavior({ "ɾ��" })
	public boolean behaviorEditOrDeleteItem() {
		return parent_id != null;// ���ڵ�
	}

	@ReadValue(ReadValue.TYPE)
	@Exclude
	private final String typeName = "��Ŀ�Ŷ�";

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@ReadValue
	@WriteValue
	@Persistence
	private ObjectId _id;

	/**
	 * �ϼ���֯
	 */
	@ReadValue
	@WriteValue
	private ObjectId parent_id;

	@ReadValue
	@WriteValue
	private ObjectId scope_id;

	@ReadValue
	private Integer seq;

	@WriteValue("seq")
	public void writeSeq(String _seq) {
		seq = Integer.parseInt(_seq);
	}

	private ObjectId org_id;

	@WriteValue("organization ")
	public void setOrganization(Organization org) {
		this.org_id = Optional.ofNullable(org).map(o -> o.get_id()).orElse(null);
	}

	@ReadValue("organization ")
	public Organization getOrganization() {
		return Optional.ofNullable(org_id).map(_id -> ServicesLoader.get(OrganizationService.class).get(_id))
				.orElse(null);
	}

	@Persistence
	private String managerId;

	@ReadValue("managerInfo")
	@WriteValue("managerInfo")
	@SetValue
	private String managerInfo;

	@SetValue
	private RemoteFile managerHeadPic;

	@WriteValue("manager")
	private void setManager(User manager) {
		if (manager == null) {
			managerId = null;
			managerInfo = "";
			managerHeadPic = null;
		} else {
			managerId = manager.getUserId();
			managerInfo = manager.toString();
			List<RemoteFile> _pics = manager.getHeadPics();
			managerHeadPic = _pics != null && _pics.size() > 0 ? _pics.get(0) : null;
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
	private String name;

	@ReadValue
	@WriteValue
	private String description;

	@Exclude
	private String selectedRole;

	@ReadValue
	@WriteValue
	@SetValue
	private String roleId;

	@ReadValue
	@WriteValue
	@SetValue
	private String roleName;

	private boolean scopeRoot;

	@Structure("��Ŀ�Ŷ�/list")
	public List<OBSItem> listSubOBSItem() {
		return ServicesLoader.get(OBSService.class).getSubOBSItem(_id);
	}

	@Structure("��Ŀ�Ŷ�/count")
	public long countSubOBSItem() {
		return ServicesLoader.get(OBSService.class).countSubOBSItem(_id);
	}

	@ReadOptions("selectedRole")
	public Map<String, String> getSystemOBSRole() {
		return ServicesLoader.get(CommonService.class).getDictionary("��ɫ����");
	}

	@WriteValue("selectedRole")
	public void writeSelectedRole(String selectedRole) {
		this.selectedRole = selectedRole;
		if (this.selectedRole != null) {
			roleId = selectedRole.split("#")[0];
			roleName = selectedRole.split("#")[1];
		}
	}

	@GetValue("roleId")
	public String getId() {
		if (roleId != null && !roleId.isEmpty())
			return roleId;
		if (selectedRole != null && !selectedRole.isEmpty())
			return selectedRole.split("#")[0];
		return null;
	}

	@GetValue("roleName")
	public String getRoleName() {
		if (roleName != null && !roleName.isEmpty())
			return roleName;
		if (selectedRole != null && !selectedRole.isEmpty())
			return selectedRole.split("#")[1];
		return null;
	}

	@ReadValue("��֯�ṹͼ/title")
	public String getDiagramTitle() {
		String title = "";
		if (!Util.isEmptyOrNull(name))
			title += name;
		return title;
	}

	@ReadValue("��֯�ṹͼ/id")
	public String getDiagramId() {
		return _id.toHexString();
	}

	@ReadValue("��֯�ṹͼ/text")
	public String getDiagramText() {
		String text = "";
		if (!Util.isEmptyOrNull(roleName))
			text += roleName;

		if (!Util.isEmptyOrNull(managerInfo)) {
			text += " " + managerInfo.substring(0, managerInfo.indexOf("["));
		}
		
		if(text.isEmpty()) {
			text += "��";
		}
		return text;
	}

	@ReadValue("��֯�ṹͼ/parent")
	public String getDiagramParent() {
		return parent_id == null ? "" : parent_id.toHexString();
	}

	@ReadValue("��֯�ṹͼ/img")
	public String getDiagramImage() {
		if (managerHeadPic != null) {
			return managerHeadPic.getURL(ServicesLoader.url);
		} else if (roleId != null) {
			try {
				return "/bvs/svg?text=" + URLEncoder.encode(roleId, "utf-8") + "&color=ffffff";
			} catch (UnsupportedEncodingException e) {
			}
		}
		return "";
	}

	public OBSItem setRoleId(String roleId) {
		this.roleId = roleId;
		return this;
	}

	public OBSItem setRoleName(String roleName) {
		this.roleName = roleName;
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

	public OBSItem set_id(ObjectId _id) {
		this._id = _id;
		return this;
	}

	public OBSItem setScope_id(ObjectId scope_id) {
		this.scope_id = scope_id;
		return this;
	}

	public OBSItem setScopeRoot(boolean scopeRoot) {
		this.scopeRoot = scopeRoot;
		return this;
	}

	public boolean isScopeRoot() {
		return scopeRoot;
	}

	public ObjectId getScope_id() {
		return scope_id;
	}

	public ObjectId getOrg_id() {
		return org_id;
	}

}
