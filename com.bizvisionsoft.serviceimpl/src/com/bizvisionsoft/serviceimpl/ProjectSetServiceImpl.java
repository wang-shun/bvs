package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.bizvisionsoft.service.ProjectSetService;
import com.bizvisionsoft.service.model.Project;
import com.bizvisionsoft.service.model.ProjectSet;
import com.bizvisionsoft.serviceimpl.exception.ServiceException;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Aggregates;

public class ProjectSetServiceImpl extends BasicServiceImpl implements ProjectSetService {

	@Override
	public ProjectSet insert(ProjectSet projectSet) {
		return insert(projectSet, ProjectSet.class);
	}

	@Override
	public ProjectSet get(ObjectId _id) {
		return get(_id, ProjectSet.class);
	}

	@Override
	public long count(BasicDBObject filter) {
		return count(filter, ProjectSet.class);
	}

	@Override
	public List<ProjectSet> createDataSet(BasicDBObject condition) {
		Integer skip = (Integer) condition.get("skip");
		Integer limit = (Integer) condition.get("limit");
		BasicDBObject filter = (BasicDBObject) condition.get("filter");
		return query(skip, limit, filter);
	}

	private List<ProjectSet> query(Integer skip, Integer limit, BasicDBObject filter) {
		ArrayList<Bson> pipeline = new ArrayList<Bson>();

		if (filter != null)
			pipeline.add(Aggregates.match(filter));

		if (skip != null)
			pipeline.add(Aggregates.skip(skip));

		if (limit != null)
			pipeline.add(Aggregates.limit(limit));

		// TODO ����pipeline

		List<ProjectSet> result = new ArrayList<ProjectSet>();
		c(ProjectSet.class).aggregate(pipeline).into(result);
		return result;

	}

	@Override
	@Deprecated
	public long delete(ObjectId _id) {
		// ������¼���Ŀ�����ɱ�ɾ��
		if (c(ProjectSet.class).count(new BasicDBObject("parent_id", _id)) > 0)
			throw new ServiceException("������ɾ�����¼���Ŀ������Ŀ����¼");

		// �������Ŀ�����˸���Ŀ��������ɾ��
		if (c(Project.class).count(new BasicDBObject("projectSet_id", _id)) > 0)
			throw new ServiceException("������ɾ�����¼���Ŀ����Ŀ����¼");

		return delete(_id, ProjectSet.class);
	}

	@Override
	public long update(BasicDBObject filterAndUpdate) {
		return update(filterAndUpdate, ProjectSet.class);
	}

}
