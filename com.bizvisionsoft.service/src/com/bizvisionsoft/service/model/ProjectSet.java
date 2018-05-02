package com.bizvisionsoft.service.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.Exclude;
import com.bizvisionsoft.annotations.md.mongocodex.Persistence;
import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.ImageURL;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.Structure;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.service.ProjectService;
import com.bizvisionsoft.service.ProjectSetService;
import com.bizvisionsoft.service.ServicesLoader;
import com.bizvisionsoft.service.datatools.Query;
import com.mongodb.BasicDBObject;

@PersistenceCollection("projectSet")
public class ProjectSet {

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ��ʶ����
	/**
	 * _id
	 */
	@ReadValue
	@WriteValue
	private ObjectId _id;

	/**
	 * ���
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String id;
	
	@ImageURL("id")
	private String logo = "/img/project_set_c.svg";

	/**
	 * �������
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String workOrder;

	/**
	 * �ϼ���Ŀ��Id
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private ObjectId parent_id;

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
	private String name;

	/**
	 * ����
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String description;

	@ReadValue(ReadValue.TYPE)
	@Exclude
	private String typeName = "��Ŀ��";

	@Persistence
	private ObjectId obs_id;

	public ProjectSet setEps_id(ObjectId eps_id) {
		this.eps_id = eps_id;
		return this;
	}

	public ProjectSet setParent_id(ObjectId parent_id) {
		this.parent_id = parent_id;
		return this;
	}

	public ObjectId get_id() {
		return _id;
	}

	@Structure("EPS��� /list")
	public List<Object> getSubProjectSetsAndProjects() {
		ArrayList<Object> result = new ArrayList<Object>();

		result.addAll(ServicesLoader.get(ProjectSetService.class)
				.createDataSet(new Query().filter(new BasicDBObject("parent_id", _id)).bson()));

		result.addAll(ServicesLoader.get(ProjectService.class)
				.createDataSet(new Query().filter(new BasicDBObject("projectSet_id", _id)).bson()));

		return result;
	}

	@Structure("EPS���/count")
	public long countSubProjectSetsAndProjects() {
		// ���¼�
		long cnt = ServicesLoader.get(ProjectService.class).count(new BasicDBObject("projectSet_id", _id));
		cnt += ServicesLoader.get(ProjectSetService.class).count(new BasicDBObject("parent_id", _id));
		return cnt;
	}

	@Structure("EPS����Ŀ��ѡ�� /list")
	public List<ProjectSet> getSubProjectSets() {
		return ServicesLoader.get(ProjectSetService.class)
				.createDataSet(new Query().filter(new BasicDBObject("parent_id", _id)).bson());
	}

	@Structure("EPS����Ŀ��ѡ��/count")
	public long countSubProjectSets() {
		return ServicesLoader.get(ProjectSetService.class).count(new BasicDBObject("parent_id", _id));
	}

	@Behavior("EPS���/�༭��Ŀ��") // ����action
	private boolean enableEdit() {
		return true;//TODO ����Ȩ�� 
	}

	@Behavior("EPS���/������Ŀ��") // ����action
	private boolean enableAdd() {
		return true;//TODO ����Ȩ��
	}

	@Behavior("EPS���/ɾ����Ŀ��") // ����action
	private boolean enableDelete() {
		return true;//TODO ����Ȩ��
	}

	@Behavior("EPS���/��") // ����action
	private boolean enableOpen() {
		return true;//TODO ����Ȩ��
	}

	@Override
	@Label
	public String toString() {
		return name + " [" + id + "]";
	}

	public ObjectId getOBS_id() {
		return obs_id;
	}

}
