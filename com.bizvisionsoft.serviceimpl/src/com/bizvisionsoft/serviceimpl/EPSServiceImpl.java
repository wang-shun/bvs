package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.bizvisionsoft.service.EPSService;
import com.bizvisionsoft.service.model.EPS;
import com.mongodb.BasicDBObject;

public class EPSServiceImpl extends BasicServiceImpl implements EPSService {

	@Override
	public long update(BasicDBObject fu) {
		return update(fu, EPS.class);
	}

	@Override
	public EPS insert(EPS eps) {
		return insert(eps, EPS.class);
	}

	@Override
	public EPS get(ObjectId _id) {
		return get(_id, EPS.class);
	}

	@Override
	public List<EPS> getRootEPS() {
		return getSubEPS(null);
	}

	@Override
	public long count(BasicDBObject filter) {
		return count(filter, EPS.class);
	}

	@Override
	public long delete(ObjectId _id) {
		// 检查有没有下级的EPS节点
		long cnt = Service.col(EPS.class).count(new BasicDBObject("parent_id", _id));
		if (cnt > 0) {
			throw new ServiceException("不允许删除有下级节点的EPS记录");
		}
		// 检查有没有下级的项目集节点
		// 检查有没有下级的项目节点
		return delete(_id, EPS.class);
	}

	@Override
	public List<EPS> getSubEPS(ObjectId parent_id) {
		ArrayList<EPS> result = new ArrayList<EPS>();
		Service.col(EPS.class).find(new BasicDBObject("parent_id", parent_id)).into(result);
		return result;
	}

	@Override
	public long countSubEPS(ObjectId _id) {
		return Service.col(EPS.class).count(new BasicDBObject("parent_id", _id));
	}

}
