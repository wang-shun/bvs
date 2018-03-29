package com.bizvisionsoft.service.model;

import java.util.Date;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.mongocodex.SetValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;

@PersistenceCollection("project")
public class Project {

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 标识属性
	/**
	 * _id
	 */
	private ObjectId _id;

	/**
	 * 编号
	 */
	@SetValue("项目编号")
	@WriteValue("项目编号")
	private String number;

	/**
	 * 工作令号
	 */
	@SetValue("工作令号")
	@WriteValue("工作令号")
	private String workOrder;

	/**
	 * 项目集Id
	 */
	private ObjectId projectSet_id;

	/**
	 * 父项目Id
	 */
	private ObjectId parentProject_id;

	/**
	 * WBS上级Id
	 */
	private ObjectId wbsParent_id;

	/**
	 * EPS节点Id
	 */
	private ObjectId eps_id;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 描述属性
	/**
	 * 名称
	 */
	private String name;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 类别：预研、科研(下级：新研、改性、适应性改造)、CBB
	 */
	private String catalog;

	/**
	 * 项目等级 A, B, C
	 */
	private String classfication;

	/**
	 * 密级
	 */
	private String securityLevel;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 计划属性
	/**
	 * 计划开始
	 */
	private Date planStart;
	/**
	 * 计划完成
	 */
	private Date planFinish;
	/**
	 * 计划工期
	 **/
	private Integer planDuration;
	/**
	 * 计划工时
	 */
	private Integer planWorks;

	/**
	 * 实际开始
	 */
	private Date actualStart;

	/**
	 * 实际完成
	 */
	private Date actualFinish;
	/**
	 * 计划工期
	 */
	private Integer actualDuration;

	/**
	 * 计划工时
	 */
	private Integer actualWorks;
	/**
	 * 完工期限
	 */
	private Date deadline;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 指标
	/**
	 * 工作量完成率
	 **/
	private Float war;

	/** 资金完成率（%） **/
	private Float car;

	/** 当前状态 **/
	private String status;

	/** 当前阶段 **/
	private ObjectId stage_id;

	/** 进度状态 **/
	private String scheduleStatus;

	/** 项目总成本 **/
	private Float cost;

	/** 项目总利润 **/
	private Float profit;

	/** 项目总预算 **/
	private Float budget;

	/** 费用状态 **/
	private String costStatus;

	/** 盈亏金额 **/
	private Float profitLost;

	/** 进度完成率 **/
	private Float sar;

	/** 预算偏差率 **/
	private Float bdr;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 客户化基本属性
	/**
	 * 分为：纵向、横向、争取、自主
	 */
	private String type1;

	/**
	 * 分为：独立、联合、部分委托，其它
	 */
	private String type2;

	/**
	 * 军兵种
	 */
	private String amrs;

	/**
	 * 战区
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
