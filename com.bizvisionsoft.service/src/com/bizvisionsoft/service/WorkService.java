package com.bizvisionsoft.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.bizvisionsoft.annotations.md.service.DataSet;
import com.bizvisionsoft.annotations.md.service.ServiceParam;
import com.bizvisionsoft.service.model.WorkInfo;
import com.bizvisionsoft.service.model.WorkLinkInfo;
import com.mongodb.BasicDBObject;

@Path("/work")
public interface WorkService {
	
	@POST
	@Path("/gantt/data")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet("甘特图组件演示 # data")
	public List<WorkInfo> createGanttDataSet(@ServiceParam(ServiceParam.FILTER) BasicDBObject condition);

	
	@POST
	@Path("/gantt/links")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet("甘特图组件演示 # links")
	public List<WorkLinkInfo> createGanttLinkSet(@ServiceParam(ServiceParam.FILTER) BasicDBObject condition);

}
