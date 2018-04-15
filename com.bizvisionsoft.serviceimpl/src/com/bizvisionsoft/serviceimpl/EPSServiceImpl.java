package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.bizvisionsoft.service.EPSService;
import com.bizvisionsoft.service.model.EPS;
import com.bizvisionsoft.service.model.Project;
import com.bizvisionsoft.service.model.ProjectSet;
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
		// �����û���¼���EPS�ڵ�
		if (Service.col(EPS.class).count(new BasicDBObject("parent_id", _id)) > 0) {
			throw new ServiceException("������ɾ�����¼��ڵ��EPS��¼");
		}
		// �����û���¼�����Ŀ���ڵ�
		if (Service.col(ProjectSet.class).count(new BasicDBObject("eps_id", _id)) > 0) {
			throw new ServiceException("������ɾ�����¼��ڵ��EPS��¼");
		}
		
		// �����û���¼�����Ŀ�ڵ�
		if (Service.col(Project.class).count(new BasicDBObject("eps_id", _id)) > 0) {
			throw new ServiceException("������ɾ�����¼��ڵ��EPS��¼");
		}
		
		return delete(_id, EPS.class);
	}

	@Override
	public List<EPS> getSubEPS(ObjectId parent_id) {
		ArrayList<EPS> result = new ArrayList<EPS>();
		Service.col(EPS.class).find(new BasicDBObject("parent_id", parent_id)).sort(new BasicDBObject("id",1)).into(result);
		return result;
	}

	@Override
	public long countSubEPS(ObjectId _id) {
		return Service.col(EPS.class).count(new BasicDBObject("parent_id", _id));
	}

	@Override
	public long deleteProjectSet(ObjectId _id) {
		// ������¼���Ŀ�����ɱ�ɾ��
		if (Service.col(ProjectSet.class).count(new BasicDBObject("parent_id", _id)) > 0)
			throw new ServiceException("������ɾ�����¼���Ŀ������Ŀ����¼");

		// �������Ŀ�����˸���Ŀ��������ɾ��
		if (Service.col(Project.class).count(new BasicDBObject("projectSet_id", _id)) > 0)
			throw new ServiceException("������ɾ�����¼���Ŀ����Ŀ����¼");

		return delete(_id, ProjectSet.class);
	}

}
