package com.bizvisionsoft.service.model;

import java.util.Date;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.GetValue;
import com.bizvisionsoft.annotations.md.mongocodex.Persistence;
import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.mongocodex.SetValue;
import com.bizvisionsoft.annotations.md.mongocodex.Strict;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.service.ServicesLoader;
import com.bizvisionsoft.service.WorkService;
import com.bizvisionsoft.service.tools.Util;
import com.mongodb.BasicDBObject;

/**
 * <div class="doc" id="doc_content">
 * 
 * <a name="specifyingdataproperties">
 * <h2>Specifying Data Properties</h2></a>
 * 
 * <p>
 * A data source for the Gantt chart is an object that stores 2 types of
 * information:
 * </p>
 * 
 * <ul>
 * <li><strong>tasks</strong> - the items of tasks.</li>
 * <li><strong>links</strong> - the items of dependency links.</li>
 * </ul>
 * 
 * <h3 id="task_properties">Properties of a task object</h3>
 * 
 * <ul>
 * <li><b><i>Mandatory properties</i></b> - these properties will always be
 * defined on the client, they must be provided by the datasource in order for
 * gantt to operate correctly.</li>
 * <ul>
 * <li><b>text</b> - (<i> string </i>) the task text.</li>
 * <li><b>start_date</b> - (<i> Date|string </i>) the date when a task is
 * scheduled to begin. Must match
 * <a href="api__gantt_xml_date_config.html">xml_date</a> format if provided as
 * a string.</li>
 * <li><b>duration</b> - (<i> number </i>) the task duration.
 * <a href="desktop__loading.html#loadingtaskdates">Can be replaced with the
 * 'end_date' property</a>.</li>
 * <li><b>id</b> - (<i> string|number </i>) the task id.</li>
 * </ul>
 * <li><b><i>Optional properties</i></b> - these properties may or may not be
 * defined. The default logic and templates of gantt will use these properties
 * if they are defined.</li>
 * <ul>
 * <li><b>type</b> - (<i>string</i>) the task type. The available values are
 * stored in the <a href="api__gantt_types_config.html">types</a> object:</li>
 * <ul>
 * <li><a href="desktop__task_types.html#regulartasks">"task"</a> - a regular
 * task (<i>default value</i>).</li>
 * <li><a href="desktop__task_types.html#projecttasks">"project"</a> - a task
 * that starts, when its earliest child task starts, and ends, when its latest
 * child ends. <i>The <b>start_date</b>, <b>end_date</b>, <b>duration</b>
 * properties are ignored for such tasks.</i></li>
 * <li><a href="desktop__task_types.html#milestones">"milestone"</a> - a
 * zero-duration task that is used to mark out important dates of the project.
 * <i>The <b>duration</b>, <b>progress</b>, <b>end_date</b> properties are
 * ignored for such tasks. </i></li>
 * </ul>
 * <li><b>parent</b> - (<i> string|number </i>) the id of the parent task. The
 * id of the root task is specified by the
 * <a href="api__gantt_root_id_config.html">root_id</a> config.</li>
 * <li><b>progress</b> - (<i> number from 0 to 1 </i>) the task progress.</li>
 * <li><b>open</b> - (<i> boolean </i>) specifies whether the task branch will
 * be opened initially (to show child tasks).</li>
 * <li><b>end_date</b> - (<i> Date|string </i>) the date when a task is
 * scheduled to be completed. Used as an alternative to the <i>duration</i>
 * property for setting the duration of a task. Must match
 * <a href="api__gantt_xml_date_config.html">xml_date</a> format if provided as
 * a string.</li>
 * <li><b>readonly</b>-(<i>boolean</i>) optional, can mark task as <a href=
 * "desktop__readonly_mode.html#readonlymodeforspecifictaskslinks">readonly</a>.
 * </li>
 * <li><b>editable</b>-(<i>boolean</i>) optional, can mark task as <a href=
 * "desktop__readonly_mode.html#readonlymodeforspecifictaskslinks">editable</a>.
 * </li>
 * </ul>
 * <li><b><i>Dynamic properties</i></b> - are created on the client and
 * represent the current state of a task or a link. They shouldn't be saved to
 * the database, gantt will ignore these properties if they are specified in
 * your JSON/XML.</li>
 * <ul>
 * <li><b>$source</b> - (<i> array </i>) ids of links that come out of the
 * task.</li>
 * <li><b>$target</b> - (<i> array </i>) ids of links that come into task.</li>
 * <li><b>$level</b> - (<i> number </i>) the task's level in the tasks hierarchy
 * (zero-based numbering).</li>
 * <li><b>$open</b> - (<i> boolean </i>) specifies whether the task is currently
 * opened.</li>
 * <li><b>$index</b> - (<i> number </i>) the number of the task row in the
 * gantt.</li>
 * </ul>
 * </ul>
 * 
 * <p>
 * The default date format for JSON and XML data is <strong>"%d-%m-%Y
 * %H:%i"</strong> (see the <a href="desktop__date_format.html"> date format
 * specification</a>).<br>
 * To change it, use the <a href="api__gantt_xml_date_config.html">xml_date</a>
 * configuration option.
 * </p>
 * 
 * <pre>
 * <code><pre class="js">gantt.<span class="me1">config</span>.<span class=
 * "me1">xml_date</span><span class="sy0">=</span><span class=
 * "st0">"%Y-%m-%d"</span><span class="sy0">;</span>
gantt.<span class="me1">init</span><span class="br0">(</span><span class=
"st0">"gantt_here"</span><span class="br0">)</span><span class=
"sy0">;</span></pre></code>
 * </pre>
 * 
 * <p>
 * Once loaded into Gantt, the <strong>start_date</strong> and
 * <strong>end_date</strong> properties will be parsed into the Date type.
 * </p>
 * 
 * <p>
 * Date formats that are not supported by the
 * <a href="api__gantt_xml_date_config.html">xml_date</a> config can be parsed
 * manually via the <a href="api__gantt_xml_date_template.html">xml_date</a>
 * template.
 * </p>
 * 
 * @author hua
 *
 */
@PersistenceCollection("work")
@Strict
public class WorkInfo {

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// id, 在gantt图中 使用String 类型传递，因此 ReadValue和WriteValue需要用方法重写
	@Persistence
	private ObjectId _id;

	@ReadValue("id")
	public String getId() {
		return _id == null ? null : _id.toHexString();
	}

	@WriteValue("id")
	public WorkInfo setId(String id) {
		this._id = id == null ? null : new ObjectId(id);
		return this;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// parent_id, 在gantt图中 使用的字段为parent, String 类型传递，因此 ReadValue和WriteValue需要用方法重写
	@Persistence
	private ObjectId parent_id;

	@ReadValue("parent")
	public String getParent() {
		return parent_id == null ? null : parent_id.toHexString();
	}

	@WriteValue("parent")
	public WorkInfo setParent(Object parent) {
		if (parent instanceof String) {
			this.parent_id = parent == null ? null : new ObjectId((String) parent);
		} else {
			this.parent_id = null;
		}
		return this;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// project_id, 在gantt图中 使用的字段为project, String 类型传递，
	// 因此 ReadValue和WriteValue需要用方法重写
	// 甘特图组件（是指GanttPart, 并非Gantt）要求任务和关联关系必须带有project属性。
	// 如果不带有该属性，表示这些对象可能是客户端创建的
	@Persistence
	private ObjectId project_id;

	@ReadValue("project")
	public String getProject() {
		return project_id == null ? null : project_id.toHexString();
	}

	@WriteValue("project")
	public WorkInfo setProject(String project_id) {
		this.project_id = project_id == null ? null : new ObjectId(project_id);
		return this;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// index, 在gantt图中用于排序
	@ReadValue("项目甘特图#index")
	@WriteValue("项目甘特图#index")
	@Persistence
	private int index;
	////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// WBS代码 TODO
	@WriteValue
	@ReadValue
	@Persistence
	private String wbsCode;
	////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// text, 在gantt图text字段，数据库中为name字段
	@ReadValue
	@WriteValue
	@Persistence("name")
	private String text;
	////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// fullName, 在gantt图，编辑器中，数据库中均使用
	@WriteValue
	@SetValue
	private String fullName;

	@ReadValue("fullName")
	@GetValue("fullName")
	private String getFullName() {
		if (fullName == null || fullName.trim().isEmpty()) {
			fullName = text;
		}
		return fullName;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// 计划开始日期, 编辑器保存时需要校验
	@ReadValue
	@Persistence("planStart")
	private Date start_date;

	@WriteValue("创建甘特图工作编辑器#start_date")
	public void setStart_date(Date start_date) {
		checkDate(start_date, this.end_date, this.deadline);
		this.start_date = start_date;
	}

	@WriteValue("项目甘特图#start_date")
	public void setStart_date(String start_date) {
		this.start_date = Util.str_date(start_date);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// 计划完成日期, 编辑器保存时需要校验
	@ReadValue
	@Persistence("planFinish")
	private Date end_date;

	@WriteValue("创建甘特图工作编辑器#end_date")
	public void setEnd_date(Date end_date) {
		checkDate(this.start_date, end_date, this.deadline);
		this.end_date = end_date;
	}

	@WriteValue("项目甘特图#end_date")
	public void setEnd_date(String end_date) {
		this.end_date = Util.str_date(end_date);
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// 期限, 编辑器保存时需要校验
	@ReadValue
	@Persistence
	private Date deadline;

	@WriteValue("deadline")
	public void setDeadline(Date deadline) {
		checkDate(this.start_date, this.end_date, deadline);
		this.deadline = deadline;
	}

	@WriteValue("项目甘特图#deadline")
	public void setDeadline(String deadline) {
		this.deadline = Util.str_date(deadline);
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// 工期, 需要保存，但无需传递到gantt和编辑器
	@GetValue("planDuration")
	public int getDuration() {
		if (end_date != null && start_date != null) {
			return (int) (end_date.getTime() - start_date.getTime()) / (1000 * 3600 * 24);
		} else {
			return 0;
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// 完成百分比
	@ReadValue
	@WriteValue
	@Persistence
	private Float progress;
	////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// 如果是里程碑，gantt的type为milestone，否则为task。
	// 如果在gantt中更新了task,使得他有子工作，gantt将type改为project
	@Persistence
	private boolean milestone;

	@Persistence
	private boolean summary;

	@ReadValue("type")
	public String getType() {
		if (milestone)
			return "milestone";
		else if (summary)
			return "project";
		else
			return "task";
	}

	@WriteValue("type")
	public WorkInfo setType(String type) {
		milestone = "milestone".equals(type);
		summary = "project".equals(type);
		return this;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// 存储在数据库中的是管理级别。表现在Gantt中的是barstyle,样式
	@ReadValue
	@WriteValue
	private String barstyle;

	@GetValue("manageLevel")
	private String getManagerLevel() {
		if ("level1_task".equals(barstyle)) {
			return "1";
		} else if ("level2_task".equals(barstyle)) {
			return "2";
		} else if ("level3_task".equals(barstyle)) {
			return "3";
		} else {
			return null;
		}

	}

	@SetValue("manageLevel")
	private void setManageLevel(String level) {
		if ("1".equals(level)) {
			barstyle = "level1_task";
		} else if ("2".equals(level)) {
			barstyle = "level2_task";
		} else if ("3".equals(level)) {
			barstyle = "level3_task";
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// 以下是控制gantt的客户端的属性
	@ReadValue("editable")
	public Boolean getEditable() {
		return true;
	}

	@ReadValue("open")
	public Boolean getOpen() {
		return true;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// 工作的标签文本
	@Label
	public String toString() {
		return text;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////

	public WorkInfo set_id(ObjectId _id) {
		this._id = _id;
		return this;
	}

	public ObjectId get_id() {
		return _id;
	}

	public WorkInfo setProject_id(ObjectId project_id) {
		this.project_id = project_id;
		return this;
	}

	public ObjectId getProject_id() {
		return project_id;
	}

	public WorkInfo setParent_id(ObjectId parent_id) {
		this.parent_id = parent_id;
		return this;
	}

	public ObjectId getParent_id() {
		return parent_id;
	}

	public int index() {
		return index;
	}

	public static WorkInfo newInstance(ObjectId project_id) {
		return newInstance(project_id, null);
	}

	/**
	 * 生成本层的顺序号
	 * 
	 * @param projectId
	 * @param parentId
	 * @return
	 */
	private WorkInfo generateIndex() {
		index = ServicesLoader.get(WorkService.class)
				.nextWBSIndex(new BasicDBObject("project_id", project_id).append("parent_id", parent_id));
		return this;
	}

	public static WorkInfo newInstance(ObjectId project_id, ObjectId parent_id) {
		return new WorkInfo().set_id(new ObjectId()).setProject_id(project_id).setParent_id(parent_id).generateIndex();
	}

	private void checkDate(Date start_date, Date end_date, Date deadline) {
		if (start_date != null && end_date != null && start_date.after(end_date)) {
			throw new RuntimeException("开始日期不得晚于完成日期");
		}

		if (start_date != null && deadline != null && start_date.after(deadline)) {
			throw new RuntimeException("如果设定了期限，开始日期不得晚于期限日期");
		}

		if (end_date != null && deadline != null && end_date.after(deadline)) {
			throw new RuntimeException("如果设定了期限，完成日期不得晚于期限日期");
		}

	}

}
