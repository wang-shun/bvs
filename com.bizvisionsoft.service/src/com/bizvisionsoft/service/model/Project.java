package com.bizvisionsoft.service.model;

import java.util.Date;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.mongocodex.SetValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;

@PersistenceCollection("project")
public class Project {

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ��ʶ����
	/**
	 * _id
	 */
	private ObjectId _id;

	/**
	 * ���
	 */
	@SetValue("��Ŀ���")
	@WriteValue("��Ŀ���")
	private String number;

	/**
	 * �������
	 */
	@SetValue("�������")
	@WriteValue("�������")
	private String workOrder;

	/**
	 * ��Ŀ��Id
	 */
	private ObjectId projectSet_id;

	/**
	 * ����ĿId
	 */
	private ObjectId parentProject_id;

	/**
	 * WBS�ϼ�Id
	 */
	private ObjectId wbsParent_id;

	/**
	 * EPS�ڵ�Id
	 */
	private ObjectId eps_id;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ��������
	/**
	 * ����
	 */
	private String name;

	/**
	 * ����
	 */
	private String description;

	/**
	 * ���Ԥ�С�����(�¼������С����ԡ���Ӧ�Ը���)��CBB
	 */
	private String catalog;

	/**
	 * ��Ŀ�ȼ� A, B, C
	 */
	private String classfication;

	/**
	 * �ܼ�
	 */
	private String securityLevel;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// �ƻ�����
	/**
	 * �ƻ���ʼ
	 */
	private Date planStart;
	/**
	 * �ƻ����
	 */
	private Date planFinish;
	/**
	 * �ƻ�����
	 **/
	private Integer planDuration;
	/**
	 * �ƻ���ʱ
	 */
	private Integer planWorks;

	/**
	 * ʵ�ʿ�ʼ
	 */
	private Date actualStart;

	/**
	 * ʵ�����
	 */
	private Date actualFinish;
	/**
	 * �ƻ�����
	 */
	private Integer actualDuration;

	/**
	 * �ƻ���ʱ
	 */
	private Integer actualWorks;
	/**
	 * �깤����
	 */
	private Date deadline;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ָ��
	/**
	 * �����������
	 **/
	private Float war;

	/** �ʽ�����ʣ�%�� **/
	private Float car;

	/** ��ǰ״̬ **/
	private String status;

	/** ��ǰ�׶� **/
	private ObjectId stage_id;

	/** ����״̬ **/
	private String scheduleStatus;

	/** ��Ŀ�ܳɱ� **/
	private Float cost;

	/** ��Ŀ������ **/
	private Float profit;

	/** ��Ŀ��Ԥ�� **/
	private Float budget;

	/** ����״̬ **/
	private String costStatus;

	/** ӯ����� **/
	private Float profitLost;

	/** ��������� **/
	private Float sar;

	/** Ԥ��ƫ���� **/
	private Float bdr;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// �ͻ�����������
	/**
	 * ��Ϊ�����򡢺�����ȡ������
	 */
	private String type1;

	/**
	 * ��Ϊ�����������ϡ�����ί�У�����
	 */
	private String type2;

	/**
	 * ������
	 */
	private String amrs;

	/**
	 * ս��
	 */
	private String area;

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(String workOrder) {
		this.workOrder = workOrder;
	}

	public ObjectId getProjectSet_id() {
		return projectSet_id;
	}

	public void setProjectSet_id(ObjectId projectSet_id) {
		this.projectSet_id = projectSet_id;
	}

	public ObjectId getParentProject_id() {
		return parentProject_id;
	}

	public void setParentProject_id(ObjectId parentProject_id) {
		this.parentProject_id = parentProject_id;
	}

	public ObjectId getWbsParent_id() {
		return wbsParent_id;
	}

	public void setWbsParent_id(ObjectId wbsParent_id) {
		this.wbsParent_id = wbsParent_id;
	}

	public ObjectId getEps_id() {
		return eps_id;
	}

	public void setEps_id(ObjectId eps_id) {
		this.eps_id = eps_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getClassfication() {
		return classfication;
	}

	public void setClassfication(String classfication) {
		this.classfication = classfication;
	}

	public String getSecurityLevel() {
		return securityLevel;
	}

	public void setSecurityLevel(String securityLevel) {
		this.securityLevel = securityLevel;
	}

	public Date getPlanStart() {
		return planStart;
	}

	public void setPlanStart(Date planStart) {
		this.planStart = planStart;
	}

	public Date getPlanFinish() {
		return planFinish;
	}

	public void setPlanFinish(Date planFinish) {
		this.planFinish = planFinish;
	}

	public Integer getPlanDuration() {
		return planDuration;
	}

	public void setPlanDuration(Integer planDuration) {
		this.planDuration = planDuration;
	}

	public Integer getPlanWorks() {
		return planWorks;
	}

	public void setPlanWorks(Integer planWorks) {
		this.planWorks = planWorks;
	}

	public Date getActualStart() {
		return actualStart;
	}

	public void setActualStart(Date actualStart) {
		this.actualStart = actualStart;
	}

	public Date getActualFinish() {
		return actualFinish;
	}

	public void setActualFinish(Date actualFinish) {
		this.actualFinish = actualFinish;
	}

	public Integer getActualDuration() {
		return actualDuration;
	}

	public void setActualDuration(Integer actualDuration) {
		this.actualDuration = actualDuration;
	}

	public Integer getActualWorks() {
		return actualWorks;
	}

	public void setActualWorks(Integer actualWorks) {
		this.actualWorks = actualWorks;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Float getWar() {
		return war;
	}

	public void setWar(Float war) {
		this.war = war;
	}

	public Float getCar() {
		return car;
	}

	public void setCar(Float car) {
		this.car = car;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ObjectId getStage_id() {
		return stage_id;
	}

	public void setStage_id(ObjectId stage_id) {
		this.stage_id = stage_id;
	}

	public String getScheduleStatus() {
		return scheduleStatus;
	}

	public void setScheduleStatus(String scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	public Float getCost() {
		return cost;
	}

	public void setCost(Float cost) {
		this.cost = cost;
	}

	public Float getProfit() {
		return profit;
	}

	public void setProfit(Float profit) {
		this.profit = profit;
	}

	public Float getBudget() {
		return budget;
	}

	public void setBudget(Float budget) {
		this.budget = budget;
	}

	public String getCostStatus() {
		return costStatus;
	}

	public void setCostStatus(String costStatus) {
		this.costStatus = costStatus;
	}

	public Float getProfitLost() {
		return profitLost;
	}

	public void setProfitLost(Float profitLost) {
		this.profitLost = profitLost;
	}

	public Float getSar() {
		return sar;
	}

	public void setSar(Float sar) {
		this.sar = sar;
	}

	public Float getBdr() {
		return bdr;
	}

	public void setBdr(Float bdr) {
		this.bdr = bdr;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public String getType2() {
		return type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
	}

	public String getAmrs() {
		return amrs;
	}

	public void setAmrs(String amrs) {
		this.amrs = amrs;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	

}
