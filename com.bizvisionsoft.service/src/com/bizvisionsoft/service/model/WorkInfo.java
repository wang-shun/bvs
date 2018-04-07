package com.bizvisionsoft.service.model;

import java.util.Date;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.service.ServicesLoader;
import com.bizvisionsoft.service.WorkService;
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
public class WorkInfo {

	private ObjectId _id;

	private ObjectId parent_id;

	private ObjectId project_id;

	@ReadValue("项目甘特图#index")
	@WriteValue("项目甘特图#index")
	private int index;

	@ReadValue
	@WriteValue
	private String wbsCode;

	@ReadValue
	@WriteValue
	private String text;

	@ReadValue
	private Date start_date;

	@ReadValue
	private Date end_date;

	@ReadValue
	private Date deadline;

	@ReadValue
	@WriteValue
	private Integer duration;

	@ReadValue
	@WriteValue
	private Float progress;

	@ReadValue
	@WriteValue
	private Boolean readonly;

	@ReadValue
	@WriteValue
	private Boolean editable;

	@ReadValue
	@WriteValue
	private Boolean open;

	@ReadValue
	@WriteValue
	private String barstyle;

	@ReadValue
	@WriteValue
	private Boolean milestone;

	@ReadValue("id")
	public String getId() {
		return _id == null ? null : _id.toHexString();
	}

	@WriteValue("id")
	public WorkInfo setId(String id) {
		this._id = id == null ? null : new ObjectId(id);
		return this;
	}

	public WorkInfo set_id(ObjectId _id) {
		this._id = _id;
		return this;
	}

	public ObjectId get_id() {
		return _id;
	}

	@ReadValue("parent")
	public String getParent() {
		return parent_id == null ? null : parent_id.toHexString();
	}

	@WriteValue("parent")
	public WorkInfo setParent(String parent) {
		this.parent_id = parent == null ? null : new ObjectId(parent);
		return this;
	}

	@ReadValue("project")
	public String getProject() {
		return project_id == null ? null : project_id.toHexString();
	}

	@WriteValue("project")
	public WorkInfo setProject(String project_id) {
		this.project_id = project_id == null ? null : new ObjectId(project_id);
		return this;
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

	@ReadValue("type")
	public String getType() {
		if (Boolean.TRUE.equals(milestone))
			return "milestone";
		else
			return "task";
	}

	@WriteValue("type")
	public WorkInfo setType(String type) {
		milestone = "milestone".equals(type);
		return this;
	}

	@ReadValue("创建甘特图工作编辑器#index")
	public String getIndex() {
		return "" + index;
	}

	public int index() {
		return index;
	}

	@WriteValue("创建甘特图工作编辑器#index")
	public WorkInfo setIndex(String index) {
		this.index = Integer.parseInt(index);
		return this;
	}

	public WorkInfo setEditable(Boolean editable) {
		this.editable = editable;
		return this;
	}

	public WorkInfo setOpen(Boolean open) {
		this.open = open;
		return this;
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
		return new WorkInfo().set_id(new ObjectId()).setProject_id(project_id).setParent_id(parent_id).setEditable(true)
				.setOpen(true).generateIndex();
	}

	@WriteValue("start_date")
	public void setStart_date(Date start_date) {
		checkDate(start_date, this.end_date, this.deadline);
		this.start_date = start_date;
	}

	@WriteValue("end_date")
	public void setEnd_date(Date end_date) {
		checkDate(this.start_date, end_date, this.deadline);
		this.end_date = end_date;
	}

	@WriteValue("deadline")
	public void setDeadline(Date deadline) {
		checkDate(this.start_date, this.end_date, deadline);
		this.deadline = deadline;
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
	
	@Override
	@Label
	public String toString() {
		return text;
	}

}
