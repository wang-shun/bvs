package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import com.bizvisionsoft.service.WorkService;
import com.bizvisionsoft.service.model.WorkInfo;
import com.bizvisionsoft.service.model.WorkLinkInfo;
import com.mongodb.BasicDBObject;

public class WorkServiceImpl implements WorkService {

	@Override
	public List<WorkInfo> createGanttDataSet(BasicDBObject condition) {
		List<WorkInfo> result = new ArrayList<WorkInfo>();
		Service.col(WorkInfo.class).find().into(result);
		return result;
	}

	@Override
	public List<WorkLinkInfo> createGanttLinkSet(BasicDBObject condition) {
		List<WorkLinkInfo> result = new ArrayList<WorkLinkInfo>();
		Service.col(WorkLinkInfo.class).find().into(result);
		return result;
	}

}
