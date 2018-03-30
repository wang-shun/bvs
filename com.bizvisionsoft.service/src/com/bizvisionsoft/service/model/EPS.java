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
	// 标识属性
	/**
	 * _id
	 */
	private ObjectId _id;

	/**
	 * 编号
	 */
	@ReadValue
	@WriteValue
	private String id;

	/**
	 * 父节点
	 */
	@ReadValue
	@WriteValue
	private ObjectId parent_id;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 描述属性
	/**
	 * 名称
	 */
	@ReadValue
	@WriteValue
	private String name;

	/**
	 * 描述
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
		// 取下级EPS
		List<EPS> subEPSNodes = service.getSubEPS(_id);
		result.addAll(subEPSNodes);
		// 取下级项目集

		// 取下级项目
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
