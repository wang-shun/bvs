package com.bizvisionsoft.service.model;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.Exclude;
import com.bizvisionsoft.annotations.md.mongocodex.GetValue;
import com.bizvisionsoft.annotations.md.mongocodex.Persistence;
import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.mongocodex.SetValue;
import com.bizvisionsoft.annotations.md.mongocodex.Strict;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadOptions;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.service.EPSService;
import com.bizvisionsoft.service.ProjectSetService;
import com.bizvisionsoft.service.ServicesLoader;

/**
 * ��Ŀ����ģ�ͣ����ڴ����ͱ༭
 * 
 * @author hua
 *
 */
@Strict
@PersistenceCollection("project")
public class Project {

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ��ʶ����
	/**
	 * _id
	 */
	@SetValue
	@GetValue
	private ObjectId _id;

	/**
	 * ���
	 */
	@ReadValue
	@Label(Label.ID_LABEL)
	@WriteValue
	@Persistence
	private String id;

	/**
	 * �������
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String workOrder;

	/**
	 * ��Ŀ��Id
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private ObjectId projectSet_id;

	/**
	 * ����ĿId
	 */
	@Persistence
	private ObjectId parentProject_id;

	/**
	 * WBS�ϼ�Id
	 */
	@Persistence
	private ObjectId wbsParent_id;

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
	@Label(Label.NAME_LABEL)
	private String name;

	/**
	 * ����
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String description;

	/**
	 * ���Ԥ�С�����(�¼������С����ԡ���Ӧ�Ը���)��CBB
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String catalog;

	/**
	 * ��Ŀ�ȼ� A, B, C
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String classfication;

	/**
	 * �ܼ�
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String securityLevel;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// �ƻ�����
	/**
	 * �ƻ���ʼ
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Date planStart;
	/**
	 * �ƻ����
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Date planFinish;

	/**
	 * �ƻ�����
	 **/
	@ReadValue
	@WriteValue
	@Persistence
	private Integer planDuration;

	/**
	 * �ƻ���ʱ
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Integer planWorks;

	/**
	 * ʵ�ʿ�ʼ
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Date actualStart;

	/**
	 * ʵ�����
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Date actualFinish;

	/**
	 * �ƻ����� ///TODO
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Integer actualDuration;

	/**
	 * �ƻ���ʱ //TODO
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Integer actualWorks;
	/**
	 * �깤����
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private Date deadline;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// �ͻ�����������
	/**
	 * ��Ϊ�����򡢺�����ȡ������
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String type1;

	/**
	 * ��Ϊ�����������ϡ�����ί�У�����
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private String type2;

	/**
	 * ������
	 */
	@ReadValue
	@WriteValue
	@Persistence
	private List<String> arms;

	/**
	 * ս��
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

	@WriteValue("eps_or_projectset_id")
	public void setEPSorProjectSet(Object element) {
		if (element instanceof EPS)
			this.eps_id = ((EPS) element).get_id();
		if (element instanceof ProjectSet)
			this.projectSet_id = ((ProjectSet) element).get_id();
	}

	@ReadValue("eps_or_projectset_id")
	public Object getEPSOrProjectSet() {
		if (eps_id != null)
			return ServicesLoader.get(EPSService.class).get(eps_id);
		if (projectSet_id != null)
			return ServicesLoader.get(ProjectSetService.class).get(projectSet_id);
		return null;
	}

	@ReadOptions("catalog")
	public Map<String, Object> getCatalogOptions() {
		LinkedHashMap<String, Object> options = new LinkedHashMap<String, Object>();
		options.put("Ԥ��", "Ԥ��");
		options.put("����-����", "����-����");
		options.put("����-����", "����-����");
		options.put("����-��Ӧ�Ը���", "����-��Ӧ�Ը���");
		options.put("CBB", "CBB");
		return options;
	}
	
	@Override
	@Label
	public String toString() {
		return name + " [" + id + "]";
	}
	
	@ReadValue(ReadValue.TYPE)
	@Exclude
	private String typeName = "��Ŀ";
	
	public Date getPlanStart() {
		return planStart;
	}
	
	public Date getPlanFinish() {
		return planFinish;
	}

}
