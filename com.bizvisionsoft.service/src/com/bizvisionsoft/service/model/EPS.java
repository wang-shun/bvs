package com.bizvisionsoft.service.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.Exclude;
import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.ImageURL;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.Structure;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.service.EPSService;
import com.bizvisionsoft.service.ProjectService;
import com.bizvisionsoft.service.ProjectSetService;
import com.bizvisionsoft.service.ProjectTemplateService;
import com.bizvisionsoft.service.ServicesLoader;
import com.bizvisionsoft.service.datatools.Query;
import com.mongodb.BasicDBObject;

@PersistenceCollection("eps")
public class EPS implements Comparable<EPS> {

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ��ʶ����
	/**
	 * _id
	 */
	private ObjectId _id;

	/**
	 * ���
	 */
	@ReadValue
	@WriteValue
	private String id;

	@ImageURL("id")
	private String logo = "/img/eps_c.svg";
	/**
	 * ���ڵ�
	 */
	@ReadValue
	@WriteValue
	private ObjectId parent_id;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ��������
	/**
	 * ����
	 */
	@ReadValue
	@WriteValue
	private String name;

	/**
	 * ����
	 */
	@ReadValue
	@WriteValue
	private String description;

	@Behavior({"EPS���/������Ŀ��","��Ŀģ�����/������Ŀģ��"}) // ����action
	@Exclude // ���ó־û�
	private boolean enabledBehaviors = true;

	public ObjectId get_id() {
		return _id;
	}

	public EPS setParent_id(ObjectId parent_id) {
		this.parent_id = parent_id;
		return this;
	}

	public ObjectId getParent_id() {
		return parent_id;
	}

	@Override
	public int compareTo(EPS o) {
		return id.compareTo(o.id);
	}

	@Structure("EPS����/list")
	public List<EPS> listSubEPS() {
		return ServicesLoader.get(EPSService.class).getSubEPS(_id);
	}

	@Structure("EPS����/count")
	public long countSubEPS() {
		return ServicesLoader.get(EPSService.class).countSubEPS(_id);
	}

	@Structure("EPS��� /list")
	public List<Object> listSubNodes() {
		ArrayList<Object> result = new ArrayList<Object>();

		result.addAll(ServicesLoader.get(EPSService.class).getSubEPS(_id));

		result.addAll(ServicesLoader.get(ProjectSetService.class)
				.createDataSet(new Query().filter(new BasicDBObject("eps_id", _id)).bson()));

		result.addAll(ServicesLoader.get(ProjectService.class)
				.createDataSet(new Query().filter(new BasicDBObject("eps_id", _id)).bson()));

		return result;
	}

	@Structure("EPS���/count")
	public long countSubNodes() {
		// ���¼�
		long cnt = ServicesLoader.get(EPSService.class).countSubEPS(_id);
		cnt += ServicesLoader.get(ProjectService.class).count(new BasicDBObject("eps_id", _id));
		cnt += ServicesLoader.get(ProjectSetService.class).count(new BasicDBObject("eps_id", _id));
		return cnt;
	}

	@Structure("��Ŀģ����� /list")
	public List<Object> listSubNodesForProjectTemplate() {
		ArrayList<Object> result = new ArrayList<Object>();

		result.addAll(ServicesLoader.get(EPSService.class).getSubEPS(_id));

		result.addAll(ServicesLoader.get(ProjectTemplateService.class)
				.createDataSet(new Query().filter(new BasicDBObject("eps_id", _id)).bson()));

		return result;
	}

	@Structure("��Ŀģ�����/count")
	public long countSubNodesForProjectTemplate() {
		// ���¼�
		long cnt = ServicesLoader.get(EPSService.class).countSubEPS(_id);
		cnt += ServicesLoader.get(ProjectTemplateService.class).count(new BasicDBObject("eps_id", _id));
		return cnt;
	}

	@Structure("EPS����Ŀ��ѡ�� /list")
	public List<Object> listSubEPSAndProjectSets() {
		ArrayList<Object> result = new ArrayList<Object>();

		result.addAll(ServicesLoader.get(EPSService.class).getSubEPS(_id));

		result.addAll(ServicesLoader.get(ProjectSetService.class)
				.createDataSet(new Query().filter(new BasicDBObject("eps_id", _id)).bson()));

		return result;

	}

	@Structure("EPS����Ŀ��ѡ��/count")
	public long countSubEPSAndProjectSets() {
		// ���¼�
		long cnt = ServicesLoader.get(EPSService.class).countSubEPS(_id);
		cnt += ServicesLoader.get(ProjectSetService.class).count(new BasicDBObject("eps_id", _id));
		return cnt;
	}

	@ReadValue(ReadValue.TYPE)
	@Exclude
	private String typeName = "EPS";

	@Override
	@Label
	public String toString() {
		return name + " [" + id + "]";
	}

}
