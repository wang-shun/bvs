package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.bizvisionsoft.service.WorkService;
import com.bizvisionsoft.service.model.WorkInfo;
import com.bizvisionsoft.service.model.WorkLinkInfo;
import com.mongodb.BasicDBObject;

public class WorkServiceImpl extends BasicServiceImpl implements WorkService {

	@Override
	public List<WorkInfo> createGanttDataSet(BasicDBObject condition) {
		List<WorkInfo> result = new ArrayList<WorkInfo>();
		c(WorkInfo.class).find(condition).into(result);
		return result;
	}

	@Override
	public List<WorkLinkInfo> createGanttLinkSet(BasicDBObject condition) {
		List<WorkLinkInfo> result = new ArrayList<WorkLinkInfo>();
		c(WorkLinkInfo.class).find(condition).into(result);
		return result;
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

}
