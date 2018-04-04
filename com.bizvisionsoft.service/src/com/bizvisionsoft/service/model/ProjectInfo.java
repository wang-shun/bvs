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
 * 项目信息模型，用于查询或表格中显示
 * 
 * @author hua
 *
 */
@Strict
@PersistenceCollection("project")
public class ProjectInfo {

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 标识属性
	/**
	 * _id
	 */
	@ReadValue
	@SetValue
	private ObjectId _id;

	/**
	 * 编号
	 */
	@SetValue
	@ReadValue({"id",ReadValue.ID_LABEL})
	private String id;

	/**
	 * 工作令号
	 */
	@ReadValue
	@SetValue
	private String workOrder;

	/**
	 * 项目集Id
	 */
	@SetValue
	private ObjectId projectSet_id;

	/**
	 * 父项目Id
	 */
	@SetValue
	private ObjectId parentProject_id;

	/**
	 * WBS上级Id
	 */
	@SetValue
	private ObjectId wbsParent_id;

	/**
	 * EPS节点Id
	 */
	@SetValue
	private ObjectId eps_id;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 描述属性
	/**
	 * 名称
	 */
	@ReadValue({"name",ReadValue.NAME_LABEL})
	@SetValue
	private String name;

	/**
	 * 描述
	 */
	@ReadValue
	@SetValue
	private String description;

	/**
	 * 类别：预研、科研(下级：新研、改性、适应性改造)、CBB
	 */
	@ReadValue
	@SetValue
	private String catalog;

	/**
	 * 项目等级 A, B, C
	 */
	@ReadValue
	@SetValue
	private String classfication;

	/**
	 * 密级
	 */
	@ReadValue
	@SetValue
	private String securityLevel;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 计划属性
	/**
	 * 计划开始
	 */
	@ReadValue
	@SetValue
	private Date planStart;
	/**
	 * 计划完成
	 */
	@ReadValue
	@SetValue
	private Date planFinish;

	/**
	 * 计划工期
	 **/
	@ReadValue
	@SetValue
	private Integer planDuration;

	/**
	 * 计划工时
	 */
	@ReadValue
	@SetValue
	private Integer planWorks;

	/**
	 * 实际开始
	 */
	@ReadValue
	@SetValue
	private Date actualStart;

	/**
	 * 实际完成
	 */
	@ReadValue
	@SetValue
	private Date actualFinish;

	/**
	 * 计划工期 ///TODO
	 */
	@ReadValue
	@SetValue
	private Integer actualDuration;

	/**
	 * 计划工时 //TODO
	 */
	@ReadValue
	@SetValue
	private Integer actualWorks;
	/**
	 * 完工期限
	 */
	@ReadValue
	@SetValue
	private Date deadline;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 指标
	/**
	 * 工作量完成率
	 **/
	@ReadValue
	@SetValue
	private Float war;

	/** 资金完成率（%） **/
	@ReadValue
	@SetValue
	private Float car;

	/** 当前状态 **/
	@ReadValue
	@SetValue
	private String status;

	/** 当前阶段 **/
	@ReadValue
	@SetValue
	private ObjectId stage_id;

	/** 进度状态 **/
	@ReadValue
	@SetValue
	private String scheduleStatus;

	/** 项目总成本 **/
	@ReadValue
	@SetValue
	private Float cost;

	/** 项目总利润 **/
	@ReadValue
	@SetValue
	private Float profit;

	/** 项目总预算 **/
	@ReadValue
	@SetValue
	private Float budget;

	/** 费用状态 **/
	@ReadValue
	@SetValue
	private String costStatus;

	/** 盈亏金额 **/
	@ReadValue
	@SetValue
	private Float profitLost;

	/** 进度完成率 **/
	@ReadValue
	@SetValue
	private Float sar;

	/** 预算偏差率 **/
	@ReadValue
	@SetValue
	private Float bdr;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 客户化基本属性
	/**
	 * 分为：纵向、横向、争取、自主
	 */
	@ReadValue
	@SetValue
	private String type1;

	/**
	 * 分为：独立、联合、部分委托，其它
	 */
	@ReadValue
	@SetValue
	private String type2;

	/**
	 * 军兵种
	 */
	@ReadValue
	@SetValue
	private List<String> arms;

	/**
	 * 战区
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
		return "项目";
	}
	
	@Behavior("EPS浏览#打开") // 控制action
	private boolean enableOpen() {
		System.out.println();
		return true;// 考虑权限 TODO
	}
	
	@Override
	@ReadValue(ReadValue.LABEL)
	public String toString() {
		return name + " [" + id + "]";
	}
}
