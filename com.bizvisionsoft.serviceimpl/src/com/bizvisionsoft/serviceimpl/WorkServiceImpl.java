package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.bizvisionsoft.service.WorkService;
import com.bizvisionsoft.service.model.Project;
import com.bizvisionsoft.service.model.ProjectStatus;
import com.bizvisionsoft.service.model.Result;
import com.bizvisionsoft.service.model.WorkInfo;
import com.bizvisionsoft.service.model.WorkLinkInfo;
import com.bizvisionsoft.serviceimpl.exception.ServiceException;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;

public class WorkServiceImpl extends BasicServiceImpl implements WorkService {

	@Override
	public List<WorkInfo> createTaskDataSet(BasicDBObject condition) {
		// TODO
		return c(WorkInfo.class).find(condition).into(new ArrayList<WorkInfo>());
	}

	@Override
	public List<WorkLinkInfo> createLinkDataSet(BasicDBObject condition) {
		return c(WorkLinkInfo.class).find(condition).into(new ArrayList<WorkLinkInfo>());
	}

	@Override
	public List<WorkInfo> listProjectRootTask(ObjectId project_id) {
		return createTaskDataSet(new BasicDBObject("project_id", project_id).append("parent_id", null));
	}

	@Override
	public long countProjectRootTask(ObjectId project_id) {
		return count(new BasicDBObject("project_id", project_id).append("parent_id", null), "work");
	}

	@Override
	public List<WorkInfo> listChildren(ObjectId parent_id) {
		return createTaskDataSet(new BasicDBObject("parent_id", parent_id));
	}

	@Override
	public long countChildren(ObjectId parent_id) {
		return count(new BasicDBObject("parent_id", parent_id), "work");
	}

	@Override
	public int nextWBSIndex(BasicDBObject condition) {
		Document doc = c("work").find(condition).sort(new BasicDBObject("index", -1))
				.projection(new BasicDBObject("index", 1)).first();
		return Optional.ofNullable(doc).map(d -> d.getInteger("index", 0)).orElse(0) + 1;
	}

	@Override
	public WorkInfo insertWork(WorkInfo work) {
		return insert(work, WorkInfo.class);
	}

	@Override
	public WorkLinkInfo insertLink(WorkLinkInfo link) {
		return insert(link, WorkLinkInfo.class);
	}

	@Override
	public long updateWork(BasicDBObject filterAndUpdate) {
		return update(filterAndUpdate, WorkInfo.class);
	}

	@Override
	public long updateLink(BasicDBObject filterAndUpdate) {
		return update(filterAndUpdate, WorkLinkInfo.class);
	}

	@Override
	public long deleteWork(ObjectId _id) {
		return delete(_id, WorkInfo.class);
	}

	@Override
	public long deleteLink(ObjectId _id) {
		return delete(_id, WorkLinkInfo.class);
	}

	@Override
	public WorkInfo getWork(ObjectId _id) {
		return get(_id, WorkInfo.class);
	}

	@Override
	public WorkLinkInfo getLink(ObjectId _id) {
		return get(_id, WorkLinkInfo.class);
	}

	@Override
	public List<Result> start(ObjectId _id, String executeBy) {
		List<Result> result = startWorkCheck(_id, executeBy);
		if (!result.isEmpty()) {
			return result;
		}

		// �޸�״̬
		UpdateResult ur = c(WorkInfo.class).updateOne(new BasicDBObject("_id", _id),
				new BasicDBObject("$set", new BasicDBObject("status", ProjectStatus.Processing)
						.append("actualStart", new Date()).append("startBy", executeBy)));

		// ����ur��������Ľ��
		if (ur.getModifiedCount() == 0) {
			throw new ServiceException("û���������������Ĺ�����");
		}

		ObjectId project_id = c("work").distinct("project_id", new BasicDBObject("_id", _id), ObjectId.class).first();
		ur = c(Project.class).updateOne(new BasicDBObject("_id", project_id),
				new BasicDBObject("$set", new BasicDBObject("stage_id", _id)));
		// ����ur��������Ľ��
		if (ur.getModifiedCount() == 0) {
			throw new ServiceException("�޷�������Ŀ��ǰ״̬��");
		}

		// TODO ֪ͨ�Ŷӳ�Ա�������Ѿ�����

		return result;
	}

	private List<Result> startWorkCheck(ObjectId _id, String executeBy) {
		//////////////////////////////////////////////////////////////////////
		// �������Ϣ
		// 1. ����Ƿ񴴽��˵�һ���WBS�����мƻ������û�У���ʾ����
		// 2. �����֯�ṹ�Ƿ���ɣ����ֻ�и�������
		// 3. ����һ���WBS�Ƿ�ָ���˱�Ҫ�Ľ�ɫ�����û�и����˽�ɫ����ʾ���档
		// 4. ȱ�ٸ�ϵ�ˣ�����
		// 5. û����Ԥ�㣬����
		// 6. Ԥ��û���꣬����
		// 7. Ԥ��û�з��䣬����

		return new ArrayList<Result>();
	}

}
