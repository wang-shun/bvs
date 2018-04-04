package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;

import com.bizvisionsoft.service.WorkService;
import com.bizvisionsoft.service.model.WorkInfo;
import com.bizvisionsoft.service.model.WorkLinkInfo;
import com.mongodb.BasicDBObject;

public class WorkServiceImpl implements WorkService {

	@Override
	public List<WorkInfo> createGanttDataSet(BasicDBObject condition) {
		List<WorkInfo> result = new ArrayList<WorkInfo>();
		Service.col(WorkInfo.class).find(condition).into(result);
		return result;
	}

	@Override
	public List<WorkLinkInfo> createGanttLinkSet(BasicDBObject condition) {
		List<WorkLinkInfo> result = new ArrayList<WorkLinkInfo>();
		Service.col(WorkLinkInfo.class).find(condition).into(result);
		return result;
	}

	@Override
	public int nextWBSIndex(BasicDBObject condition) {
		Document doc = Service.col("work").find(condition).sort(new BasicDBObject("index", -1))
				.projection(new BasicDBObject("index", 1)).first();
		return Optional.ofNullable(doc).map(d -> d.getInteger("index", 0)).orElse(0) + 1;
	}

}
