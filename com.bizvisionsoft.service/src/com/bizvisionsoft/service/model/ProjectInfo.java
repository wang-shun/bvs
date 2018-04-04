package com.bizvisionsoft.service.model;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.mongocodex.SetValue;
import com.bizvisionsoft.annotations.md.mongocodex.Strict;
import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.service.EPSService;
import com.bizvisionsoft.service.ServicesLoader;

/**
 * ��Ŀ��Ϣģ�ͣ����ڲ�ѯ��������ʾ
 * 
 * @author hua
 *
 */
@Strict
@PersistenceCollection("project")
public class ProjectInfo {

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ��ʶ����
	/**
	 * _id
	 */
	@ReadValue
	@SetValue
	private ObjectId _id;

	/**
	 * ���
	 */
	@SetValue
	@ReadValue({"id",ReadValue.ID_LABEL})
	private String id;

	/**
	 * �������
	 */
	@ReadValue
	@SetValue
	private String workOrder;

	/**
	 * ��Ŀ��Id
	 */
	@SetValue
	private ObjectId projectSet_id;

	/**
	 * ����ĿId
	 */
	@SetValue
	private ObjectId parentProject_id;

	/**
	 * WBS�ϼ�Id
	 */
	@SetValue
	private ObjectId wbsParent_id;

	/**
	 * EPS�ڵ�Id
	 */
	@SetValue
	private ObjectId eps_id;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ��������
	/**
	 * ����
	 */
	@ReadValue({"name",ReadValue.NAME_LABEL})
	@SetValue
	private String name;

	/**
	 * ����
	 */
	@ReadValue
	@SetValue
	private String description;

	/**
	 * ���Ԥ�С�����(�¼������С����ԡ���Ӧ�Ը���)��CBB
	 */
	@ReadValue
	@SetValue
	private String catalog;

	/**
	 * ��Ŀ�ȼ� A, B, C
	 */
	@ReadValue
	@SetValue
	private String classfication;

	/**
	 * �ܼ�
	 */
	@ReadValue
	@SetValue
	private String securityLevel;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// �ƻ�����
	/**
	 * �ƻ���ʼ
	 */
	@ReadValue
	@SetValue
	private Date planStart;
	/**
	 * �ƻ����
	 */
	@ReadValue
	@SetValue
	private Date planFinish;

	/**
	 * �ƻ�����
	 **/
	@ReadValue
	@SetValue
	private Integer planDuration;

	/**
	 * �ƻ���ʱ
	 */
	@ReadValue
	@SetValue
	private Integer planWorks;

	/**
	 * ʵ�ʿ�ʼ
	 */
	@ReadValue
	@SetValue
	private Date actualStart;

	/**
	 * ʵ�����
	 */
	@ReadValue
	@SetValue
	private Date actualFinish;

	/**
	 * �ƻ����� ///TODO
	 */
	@ReadValue
	@SetValue
	private Integer actualDuration;

	/**
	 * �ƻ���ʱ //TODO
	 */
	@ReadValue
	@SetValue
	private Integer actualWorks;
	/**
	 * �깤����
	 */
	@ReadValue
	@SetValue
	private Date deadline;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ָ��
	/**
	 * �����������
	 **/
	@ReadValue
	@SetValue
	private Float war;

	/** �ʽ�����ʣ�%�� **/
	@ReadValue
	@SetValue
	private Float car;

	/** ��ǰ״̬ **/
	@ReadValue
	@SetValue
	private String status;

	/** ��ǰ�׶� **/
	@ReadValue
	@SetValue
	private ObjectId stage_id;

	/** ����״̬ **/
	@ReadValue
	@SetValue
	private String scheduleStatus;

	/** ��Ŀ�ܳɱ� **/
	@ReadValue
	@SetValue
	private Float cost;

	/** ��Ŀ������ **/
	@ReadValue
	@SetValue
	private Float profit;

	/** ��Ŀ��Ԥ�� **/
	@ReadValue
	@SetValue
	private Float budget;

	/** ����״̬ **/
	@ReadValue
	@SetValue
	private String costStatus;

	/** ӯ����� **/
	@ReadValue
	@SetValue
	private Float profitLost;

	/** ��������� **/
	@ReadValue
	@SetValue
	private Float sar;

	/** Ԥ��ƫ���� **/
	@ReadValue
	@SetValue
	private Float bdr;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// �ͻ�����������
	/**
	 * ��Ϊ�����򡢺�����ȡ������
	 */
	@ReadValue
	@SetValue
	private String type1;

	/**
	 * ��Ϊ�����������ϡ�����ί�У�����
	 */
	@ReadValue
	@SetValue
	private String type2;

	/**
	 * ������
	 */
	@ReadValue
	@SetValue
	private List<String> arms;

	/**
	 * ս��
	 */
	@ReadValue
	@SetValue
	private List<String> area;

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	@ReadValue("EPS")
	public EPS getEPS() {
		return Optional.ofNullable(eps_id).map(eps_id -> ServicesLoader.get(EPSService.class).get(eps_id)).orElse(null);
	}

	@ReadValue("WBSParent")
	public Work getWBSParent() {
		return null;// TODO
	}

	@ReadValue("parentProject")
	public Project getParentProject() {
		return null;// TODO
	}

	@ReadValue("projectSet")
	public ProjectSet getProjectSet() {
		return null;// TODO
	}

	@ReadValue("epsType")
	public String getEPSNodeType() {
		return "��Ŀ";
	}
	
	@Behavior("EPS���#��") // ����action
	private boolean enableOpen() {
		System.out.println();
		return true;// ����Ȩ�� TODO
	}
	
	@Override
	@ReadValue(ReadValue.LABEL)
	public String toString() {
		return name + " [" + id + "]";
	}
}
