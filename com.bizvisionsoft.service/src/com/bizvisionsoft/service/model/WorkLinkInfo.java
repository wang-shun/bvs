package com.bizvisionsoft.service.model;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;

/**
 * 
 * 
 * <h3 id="link_properties">Properties of a link object</h3>
 * 
 * <ul>
 * <li><b><i>Mandatory properties</i></b></li>
 * <ul>
 * <li><b>id</b> - (<i> string|number </i>) the link id.</li>
 * <li><b>source</b> - (<i> number </i>) the id of a task that the dependency
 * will start from.</li>
 * <li><b>target</b> - (<i> number </i>) the id of a task that the dependency
 * will end with.</li>
 * <li><b>type</b> - (<i>string</i>) the dependency type. The available values
 * are stored in the <a href="api__gantt_links_config.html">links</a> object. By
 * default, they are:</li>
 * <ul>
 * <li><b>"0"</b> - 'finish_to_start'.</li>
 * <li><b>"1"</b> - 'start_to_start'.</li>
 * <li><b>"2"</b> - 'finish_to_finish'.</li>
 * <li><b>"3"</b> - 'start_to_finish'.</li>
 * </ul>
 * If you want to store the dependency types in some way other than the default
 * values('0','1','2'), you may change values of the related properties of the
 * <a href="api__gantt_links_config.html">links</a> object. For example:
 * 
 * <pre>
 * <code><pre class="js">gantt.<span class="me1">config</span>.<span class=
 * "me1">links</span>.<span class="me1">start_to_start</span> <span class=
 * "sy0">=</span> <span class="st0">"start2start"</span><span class=
 * "sy0">;</span></pre></code>
 * </pre>
 * 
 * Note, these values affect only the way the dependency type is stored, not the
 * behaviour of visualization.
 * </ul>
 * <li><b><i>Optional properties</i></b></li>
 * <ul>
 * <li><b>lag</b>-(<i>number</i>) optional, <a href=
 * "desktop__auto_scheduling.html#settinglagandleadtimesbetweentasks">task
 * lag</a>.</li>
 * <li><b>readonly</b>-(<i>boolean</i>) optional, can mark link as
 * <a href="desktop__readonly_mode.html">readonly</a>.</li>
 * <li><b>editable</b>-(<i>boolean</i>) optional, can mark link as
 * <a href="desktop__readonly_mode.html">editable</a>.</li>
 * </ul>
 * </ul>
 * 
 * <!-- Content Area End -->
 * 
 * <script type="text/javascript">var disqus_shortname = 'dhxdocumentation';var
 * disqus_developer = 1;(function() {var dsq = document.createElement('script');
 * dsq.type = 'text/javascript'; dsq.async = true;dsq.src = '//' +
 * disqus_shortname +
 * '.disqus.com/embed.js';(document.getElementsByTagName('head')[0] ||
 * document.getElementsByTagName('body')[0]).appendChild(dsq);})();</script>
 * </div>
 * 
 * @author hua
 *
 */
@PersistenceCollection("worklinks")
public class WorkLinkInfo {

	@ReadValue
	@WriteValue
	private String type;

	@ReadValue("��Ŀ����ͼ#lag")
	@WriteValue("��Ŀ����ͼ#lag")
	private int lag;
	
	@WriteValue("������ӹ�ϵ�༭����1��1��#lag")
	public WorkLinkInfo setLagFromEditor(String lag) {
		this.lag = Integer.parseInt(lag);
		return this;
	}
	
	@ReadValue("������ӹ�ϵ�༭����1��1��#lag")
	public String getLagForEdior(){
		return ""+lag;
	}

	@ReadValue
	@WriteValue
	private Boolean readonly;

	@ReadValue
	@WriteValue
	private Boolean editable;
	
	@ReadValue("��Ŀ����ͼ#id")
	public String getId() {
		return _id.toHexString();
	}

	@WriteValue("��Ŀ����ͼ#id")
	public WorkLinkInfo setId(String id) {
		this._id = new ObjectId(id);
		return this;
	}

	@ReadValue("��Ŀ����ͼ#source")
	public String getSource() {
		return source == null ? null : source.get_id().toHexString();
	}

	@ReadValue("��Ŀ����ͼ#target")
	public String getTarget() {
		return target == null ? null : target.get_id().toHexString();
	}

	@ReadValue("��Ŀ����ͼ#project")
	public String getProject() {
		return project_id == null ? null : project_id.toHexString();
	}

	@WriteValue("��Ŀ����ͼ#project")
	public WorkLinkInfo setProject(String project_id) {
		this.project_id = project_id == null ? null : new ObjectId(project_id);
		return this;
	}
	
	
	@ReadValue("������ӹ�ϵ�༭����1��1��#sourceTask")
	public String getSourceTaskLabel() {
		return source.toString();
	}
	
	@ReadValue("������ӹ�ϵ�༭����1��1��#targetTask")
	public String getTargetTaskLabel() {
		return target.toString();
	}
	
	
	private ObjectId _id;
	
	private ObjectId project_id;

	private WorkInfo source;

	private WorkInfo target;
	
	public WorkLinkInfo set_id(ObjectId _id) {
		this._id = _id;
		return this;
	}

	public ObjectId get_id() {
		return _id;
	}
	
	public WorkLinkInfo setEditable(Boolean editable) {
		this.editable = editable;
		return this;
	}


	public static WorkLinkInfo newInstance(ObjectId project_id) {
		return new WorkLinkInfo().set_id(new ObjectId()).setEditable(true).setProject_id(project_id);
	}
	
	public WorkLinkInfo setSource(WorkInfo source) {
		this.source = source;
		return this;
	}
	
	public WorkLinkInfo setTarget(WorkInfo target) {
		this.target = target;
		return this;
	}
	
	public WorkLinkInfo setType(String type) {
		this.type = type;
		return this;
	}
	
	public WorkLinkInfo setProject_id(ObjectId project_id) {
		this.project_id = project_id;
		return this;
	}

}
