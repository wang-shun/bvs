package com.bizvisionsoft.service.model;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.Persistence;
import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.mongocodex.SetValue;
import com.bizvisionsoft.annotations.md.mongocodex.Strict;
import com.bizvisionsoft.annotations.md.service.ReadOptions;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.service.EPSService;
import com.bizvisionsoft.service.ServicesLoader;

/**
 * 项目基本模型，用于创建和编辑
 * 
 * @author hua
 *
 */
@Strict
@PersistenceCollection("project")
public class Project {

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 标识属性
	/**
	 * _id
	 */
	@SetValue
	private ObjectId _id;

	/**
	 * 编号
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String id;

	/**
	 * 工作令号
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String workOrder;

	/**
	 * 项目集Id
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private ObjectId projectSet_id;

	/**
	 * 父项目Id
	 */
	@Persistence
	private ObjectId parentProject_id;

	/**
	 * WBS上级Id
	 */
	@Persistence
	private ObjectId wbsParent_id;

	/**
	 * EPS节点Id
	 */
	@Persistence
	private ObjectId eps_id;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 描述属性
	/**
	 * 名称
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String name;

	/**
	 * 描述
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String description;

	/**
	 * 类别：预研、科研(下级：新研、改性、适应性改造)、CBB
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String catalog;

	/**
	 * 项目等级 A, B, C
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String classfication;

	/**
	 * 密级
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String securityLevel;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 计划属性
	/**
	 * 计划开始
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Date planStart;
	/**
	 * 计划完成
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Date planFinish;

	/**
	 * 计划工期
	 **/
	@ReadValue
	@WriteValue
	@Persistence
	private Integer planDuration;

	/**
	 * 计划工时
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Integer planWorks;

	/**
	 * 实际开始
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Date actualStart;

	/**
	 * 实际完成
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Date actualFinish;

	/**
	 * 计划工期 ///TODO
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Integer actualDuration;

	/**
	 * 计划工时 //TODO
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Integer actualWorks;
	/**
	 * 完工期限
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Date deadline;


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 客户化基本属性
	/**
	 * 分为：纵向、横向、争取、自主
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String type1;

	/**
	 * 分为：独立、联合、部分委托，其它
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String type2;

	/**
	 * 军兵种
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private List<String> arms;

	/**
	 * 战区
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private List<String> area;

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	@WriteValue("eps_id")
	public void setEPS(EPS eps) {
		this.eps_id = Optional.ofNullable(eps).map(e -> e.get_id()).orElse(null);
	}

	@ReadValue("eps_id")
	public EPS getEPS() {
		return Optional.ofNullable(eps_id).map(eps_id -> ServicesLoader.get(EPSService.class).get(eps_id)).orElse(null);
	}

	@ReadOptions("catalog")
	public Map<String, Object> getCatalogOptions() {
		LinkedHashMap<String, Object> options = new LinkedHashMap<String, Object>();
		options.put("预研", "预研");
		options.put("科研-新研", "科研-新研");
		options.put("科研-改性", "科研-改性");
		options.put("科研-适应性改造", "科研-适应性改造");
		options.put("CBB", "CBB");
		return options;
	}
	
	


}
