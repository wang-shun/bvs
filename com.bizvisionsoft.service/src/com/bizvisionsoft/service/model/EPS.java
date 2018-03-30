package com.bizvisionsoft.service.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.Structure;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.service.EPSService;
import com.bizvisionsoft.service.ServicesLoader;

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

	@Structure("list")
	public List<Object> getChildren() {
		EPSService service = ServicesLoader.get(EPSService.class);

		List<Object> result = new ArrayList<Object>();
		// ȡ�¼�EPS
		List<EPS> subEPSNodes = service.getSubEPS(_id);
		result.addAll(subEPSNodes);
		// ȡ�¼���Ŀ��

		// ȡ�¼���Ŀ
		return result;
	}

	@Structure("count")
	public long countChildren() {
		long cnt = 0;
		EPSService service = ServicesLoader.get(EPSService.class);
		cnt += service.countSubEPS(_id);

		return cnt;
	}

}
