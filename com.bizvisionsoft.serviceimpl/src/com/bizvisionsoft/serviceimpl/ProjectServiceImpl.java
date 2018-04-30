package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.bizvisionsoft.service.ProjectService;
import com.bizvisionsoft.service.model.CBSItem;
import com.bizvisionsoft.service.model.Result;
import com.bizvisionsoft.service.model.OBSItem;
import com.bizvisionsoft.service.model.Project;
import com.bizvisionsoft.service.model.ProjectStatus;
import com.bizvisionsoft.serviceimpl.exception.ServiceException;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.result.UpdateResult;

public class ProjectServiceImpl extends BasicServiceImpl implements ProjectService {

	@Override
	public Project insert(Project input) {
		Project project;
		if (input.getProjectTemplate_id() == null) {
			/////////////////////////////////////////////////////////////////////////////
			// ����׼��
			ObjectId obsRoot_id = new ObjectId();// ��֯��
			ObjectId cbsRoot_id = new ObjectId();// Ԥ���

			ObjectId projectSet_id = input.getProjectSet_id();
			ObjectId obsParent_id = null;// ��֯�ϼ�
			ObjectId cbsParent_id = null;// �ɱ��ϼ�
			if (projectSet_id != null) {
				// ����ϼ�obs_id
				Document doc = c("projectSet").find(new BasicDBObject("_id", projectSet_id))
						.projection(new BasicDBObject("obs_id", true).append("cbs_id", true)).first();
				obsParent_id = Optional.ofNullable(doc).map(d -> d.getObjectId("obs_id")).orElse(null);
				cbsParent_id = Optional.ofNullable(doc).map(d -> d.getObjectId("cbs_id")).orElse(null);

			}
			/////////////////////////////////////////////////////////////////////////////
			// 0. ������Ŀ
			project = insert(input.setOBS_id(obsRoot_id).setCBS_id(cbsRoot_id), Project.class);

			/////////////////////////////////////////////////////////////////////////////
			// 1. ��Ŀ�Ŷӳ�ʼ��
			OBSItem obsRoot = new OBSItem()// ��������Ŀ��OBS���ڵ�
					.set_id(obsRoot_id)// ����_id����Ŀ����
					.setScope_id(project.get_id())// ����scope_id��������֯�ڵ��Ǹ���Ŀ����֯
					.setParent_id(obsParent_id)// �����ϼ���id
					.setName(project.getName() + "��Ŀ��")// ���ø���֯�ڵ��Ĭ������
					.setRoleId(OBSItem.ID_PM)// ���ø���֯�ڵ�Ľ�ɫid
					.setRoleName(OBSItem.NAME_PM)// ���ø���֯�ڵ������
					.setManagerId(project.getPmId()) // ���ø���֯�ڵ�Ľ�ɫ��Ӧ����
					.setScopeRoot(true);// ��������ڵ��Ƿ�Χ�ڵĸ��ڵ�
			new OBSServiceImpl().insert(obsRoot);// �����¼

			/////////////////////////////////////////////////////////////////////////////
			// 2. �����Ŀ��ʼ��
			// ������
			CBSItem cbsRoot = new CBSItem()//
					.set_id(cbsRoot_id)//
					.setScope_id(project.get_id())//
					.setParent_id(cbsParent_id)//
					.setName(project.getName())//
					.setScopeRoot(true);// ��������ڵ��Ƿ�Χ�ڵĸ��ڵ�

			new CBSServiceImpl().insertCBSItem(cbsRoot);// �����¼
		} else {
			// TODO

			project = insert(input, Project.class);
		}
		return project;
	}

	@Override
	public Project get(ObjectId _id) {
		List<Project> ds = createDataSet(new BasicDBObject("filter", new BasicDBObject("_id", _id)));
		if (ds.size() == 0) {
			throw new ServiceException("û��_idΪ" + _id + "����Ŀ��");
		}
		return ds.get(0);
	}

	@Override
	public long count(BasicDBObject filter) {
		return count(filter, Project.class);
	}

	@Override
	public List<Project> createDataSet(BasicDBObject condition) {
		Integer skip = (Integer) condition.get("skip");
		Integer limit = (Integer) condition.get("limit");
		BasicDBObject filter = (BasicDBObject) condition.get("filter");
		return query(skip, limit, filter);
	}

	private List<Project> query(Integer skip, Integer limit, BasicDBObject filter) {
		ArrayList<Bson> pipeline = new ArrayList<Bson>();

		if (filter != null)
			pipeline.add(Aggregates.match(filter));

		if (skip != null)
			pipeline.add(Aggregates.skip(skip));

		if (limit != null)
			pipeline.add(Aggregates.limit(limit));

		// TODO
		// 1. �е���֯
		appendOrgFullName(pipeline, "impUnit_id", "impUnitOrgFullName");

		appendUserInfo(pipeline, "pmId", "pmInfo");

		List<Project> result = new ArrayList<Project>();
		c(Project.class).aggregate(pipeline).into(result);
		return result;

	}

	@Override
	public List<Date> getPlanDateRange(ObjectId _id) {
		Project data = c(Project.class).find(new BasicDBObject("_id", _id))
				.projection(new BasicDBObject().append("planStart", 1).append("planFinish", 1)).first();
		ArrayList<Date> result = new ArrayList<Date>();
		result.add(data.getPlanStart());
		result.add(data.getPlanFinish());
		return result;
	}

	@Override
	public long update(BasicDBObject fu) {
		return update(fu, Project.class);
	}

	@Override
	public List<Result> startProject(ObjectId _id, boolean ignoreWarning) {
		List<Result> result = startProjectCheck(_id, ignoreWarning);
		if (!result.isEmpty()) {
			return result;
		}

		// �޸���Ŀ״̬
		UpdateResult ur = c(Project.class).updateOne(new BasicDBObject("_id", _id), new BasicDBObject("$set",
				new BasicDBObject("status", ProjectStatus.Processing).append("actualStart", new Date())));
		
		// ����ur��������Ľ��
		if (ur.getModifiedCount() == 0) {
			result.add(Result.updateFailure());
		}
		return result;
	}

	@Override
	public List<Result> startProjectCheck(ObjectId _id, boolean ignoreWarning) {
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
