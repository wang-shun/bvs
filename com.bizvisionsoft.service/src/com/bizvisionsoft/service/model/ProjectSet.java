package com.bizvisionsoft.service.model;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.Persistence;
import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.mongocodex.SetValue;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;

@PersistenceCollection("projectSet")
public class ProjectSet {

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ��ʶ����
	/**
	 * _id
	 */
	@SetValue
	private ObjectId _id;

	/**
	 * ���
	 */
	@ReadValue
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

	@ReadValue("epsType")
	public String getEPSNodeType() {
		return "��Ŀ��";
	}
}
