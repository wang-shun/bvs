package com.bizvisionsoft.service.model;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.Exclude;
import com.bizvisionsoft.annotations.md.mongocodex.GetValue;
import com.bizvisionsoft.annotations.md.mongocodex.Persistence;
import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.mongocodex.SetValue;
import com.bizvisionsoft.annotations.md.mongocodex.Strict;
import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.ImageURL;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadOptions;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.service.EPSService;
import com.bizvisionsoft.service.OrganizationService;
import com.bizvisionsoft.service.ProjectService;
import com.bizvisionsoft.service.ProjectSetService;
import com.bizvisionsoft.service.ServicesLoader;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.datatools.FilterAndUpdate;
import com.mongodb.BasicDBObject;

/**
 * ��Ŀ����ģ�ͣ����ڴ����ͱ༭
 * 
 * @author hua
 *
 */
@Strict
@PersistenceCollection("project")
public class Project implements IOBSScope, ICBSScope {

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ��ʶ����
	/**
	 * _id
	 */
	@SetValue
	@GetValue
	private ObjectId _id;

	/**
	 * ���
	 */
	@ReadValue
	@Label(Label.ID_LABEL)
	@WriteValue
	@Persistence
	private String id;

	/**
	 * �������
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String workOrder;

	/**
	 * ��Ŀ��Id
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private ObjectId projectSet_id;

	/**
	 * ����ĿId
	 */
	@Persistence
	private ObjectId parentProject_id;

	/**
	 * WBS�ϼ�Id
	 */
	@Persistence
	private ObjectId wbsParent_id;

	/**
	 * EPS�ڵ�Id
	 */
	@Persistence
	private ObjectId eps_id;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ��������
	/**
	 * ����
	 */
	@ReadValue
	@WriteValue
	@Persistence
	@Label(Label.NAME_LABEL)
	private String name;

	/**
	 * ����
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String description;

	/**
	 * ���Ԥ�С�����(�¼������С����ԡ���Ӧ�Ը���)��CBB
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String catalog;

	/**
	 * ��Ŀ�ȼ� A, B, C
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String classfication;

	/**
	 * �ܼ�
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String securityLevel;

	@ReadValue
	@WriteValue
	@Persistence
	private String status;
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// �ƻ�����
	/**
	 * �ƻ���ʼ
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Date planStart;
	/**
	 * �ƻ����
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Date planFinish;

	/**
	 * �ƻ�����
	 **/
	@ReadValue
	@WriteValue
	@Persistence
	private Integer planDuration;

	/**
	 * �ƻ���ʱ
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Integer planWorks;

	/**
	 * ʵ�ʿ�ʼ
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Date actualStart;

	/**
	 * ʵ�����
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Date actualFinish;

	/**
	 * �ƻ����� ///TODO ���ݼƻ���ʼ������Զ�����
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Integer actualDuration;

	/**
	 * �ƻ���ʱ //TODO �ƻ���ʱ�ļ���
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Integer actualWorks;

	/**
	 * �깤����
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Date deadline;

	/**
	 * ���ý׶ι���
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private boolean stageEnable;

	/**
	 * �׶�
	 */
	@ReadValue
	@SetValue
	private WorkInfo stage;

	@Persistence
	private ObjectId projectTemplate_Id;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * ��Ŀ����
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String pmId;

	@SetValue
	@ReadValue
	private String pmInfo;

	@WriteValue("pm")
	private void setPM(User pm) {
		this.pmId = Optional.ofNullable(pm).map(o -> o.getUserId()).orElse(null);
	}

	@ReadValue("pm")
	private User getPM() {
		return Optional.ofNullable(pmId).map(id -> ServicesLoader.get(UserService.class).get(id)).orElse(null);
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * �е���λ
	 */
	@Persistence // ���ݿ��ȡ
	private ObjectId impUnit_id;

	@SetValue // ��ѯ��������
	@ReadValue // �����
	private String impUnitOrgFullName;

	@WriteValue("impUnit") // �༭����
	public void setOrganization(Organization org) {
		this.impUnit_id = Optional.ofNullable(org).map(o -> o.get_id()).orElse(null);
	}

	@ReadValue("impUnit") // �༭����
	public Organization getOrganization() {
		return Optional.ofNullable(impUnit_id).map(_id -> ServicesLoader.get(OrganizationService.class).get(_id))
				.orElse(null);
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// �ͻ�����������
	/**
	 * ��Ϊ�����򡢺�����ȡ������
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String type1;

	/**
	 * ��Ϊ�����������ϡ�����ί��
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String type2;

	/**
	 * ��Ϊ������
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String type3;

	/**
	 * ������
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private List<String> arms;

	/**
	 * ս��
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private List<String> area;

	@WriteValue("eps_or_projectset_id")
	public void setEPSorProjectSet(Object element) {
		if (element instanceof EPS)
			this.eps_id = ((EPS) element).get_id();
		if (element instanceof ProjectSet)
			this.projectSet_id = ((ProjectSet) element).get_id();
	}

	@ReadValue("eps_or_projectset_id")
	public Object getEPSOrProjectSet() {
		if (eps_id != null)
			return ServicesLoader.get(EPSService.class).get(eps_id);
		if (projectSet_id != null)
			return ServicesLoader.get(ProjectSetService.class).get(projectSet_id);
		return null;
	}

	@ReadOptions("catalog")
	public Map<String, Object> getCatalogOptions() {
		LinkedHashMap<String, Object> options = new LinkedHashMap<String, Object>();
		options.put("Ԥ��", "Ԥ��");
		options.put("����-����", "����-����");
		options.put("����-����", "����-����");
		options.put("����-��Ӧ�Ը���", "����-��Ӧ�Ը���");
		options.put("CBB", "CBB");
		return options;
	}

	@Override
	@Label
	public String toString() {
		return name + " [" + id + "]";
	}

	@ImageURL("id")
	private String logo = "/img/project_c.svg";

	@ReadValue(ReadValue.TYPE)
	@Exclude
	private String typeName = "��Ŀ";

	@Behavior({ "EPS���/��", "�ҵ���Ŀ/��" }) // ����action
	private boolean enableOpen() {
		return true;// TODO ����Ȩ��
	}

	@Behavior("�ҵ���Ŀ/�༭��Ŀ") // ����action
	private boolean enableEdit() {
		return true;// TODO ����Ȩ��
	}

	@Behavior("�ҵ���Ŀ/ɾ����Ŀ") // ����action
	private boolean enableDelete() {
		return true;// TODO ����Ȩ��
	}

	@Persistence
	private CreationInfo creationInfo;

	@Persistence
	private ObjectId obs_id;

	@Persistence
	private ObjectId cbs_id;

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public Date getPlanStart() {
		return planStart;
	}

	public Date getPlanFinish() {
		return planFinish;
	}

	public Project setStageEnable(boolean stageEnable) {
		this.stageEnable = stageEnable;
		return this;
	}

	public Project setCreationInfo(CreationInfo creationInfo) {
		this.creationInfo = creationInfo;
		return this;
	}

	public ObjectId getProjectTemplate_id() {
		return projectTemplate_Id;
	}

	public String getPmId() {
		return pmId;
	}

	public Project setOBS_id(ObjectId obs_id) {
		this.obs_id = obs_id;
		return this;
	}

	public Project setCBS_id(ObjectId cbs_id) {
		this.cbs_id = cbs_id;
		return this;
	}

	public ObjectId getOBS_id() {
		return obs_id;
	}

	public String getName() {
		return name;
	}

	public ObjectId getProjectSet_id() {
		return projectSet_id;
	}

	@Override
	public ObjectId getScope_id() {
		return _id;
	}

	@Override
	public ObjectId getCBS_id() {
		return cbs_id;
	}

	@Override
	public Date[] getCBSRange() {
		return new Date[] { planStart, planFinish };
	}

	public boolean isStageEnable() {
		return stageEnable;
	}

	public String getStatus() {
		return status;
	}

	public Project setStatus(String status) {
		this.status = status;
		return this;
	}

	@Override
	public OBSItem newOBSScopeRoot() {

		ObjectId obsParent_id = Optional.ofNullable(projectSet_id)
				.map(pjset_id -> ServicesLoader.get(ProjectSetService.class).get(pjset_id)).map(ps -> ps.getOBS_id())
				.orElse(null);

		OBSItem obsRoot = new OBSItem()// ��������Ŀ��OBS���ڵ�
				.set_id(new ObjectId())// ����_id����Ŀ����
				.setScope_id(_id)// ����scope_id��������֯�ڵ��Ǹ���Ŀ����֯
				.setParent_id(obsParent_id)// �����ϼ���id
				.setName(getName() + "��Ŀ��")// ���ø���֯�ڵ��Ĭ������
				.setRoleId(OBSItem.ID_PM)// ���ø���֯�ڵ�Ľ�ɫid
				.setRoleName(OBSItem.NAME_PM)// ���ø���֯�ڵ������
				.setManagerId(getPmId()) // ���ø���֯�ڵ�Ľ�ɫ��Ӧ����
				.setScopeRoot(true);// ��������ڵ��Ƿ�Χ�ڵĸ��ڵ�

		return obsRoot;

	}

	@Override
	public void updateOBSRootId(ObjectId obs_id) {
		ServicesLoader.get(ProjectService.class).update(new FilterAndUpdate().filter(new BasicDBObject("_id", _id))
				.set(new BasicDBObject("obs_id", obs_id)).bson());
		this.obs_id = obs_id;
	}
}
