package com.bizvisionsoft.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.service.DataSet;
import com.bizvisionsoft.annotations.md.service.ServiceParam;
import com.bizvisionsoft.service.model.Project;
import com.bizvisionsoft.service.model.ProjectInfo;
import com.mongodb.BasicDBObject;

@Path("/proj")
public interface ProjectService {

	@POST
	@Path("/")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	public Project insert(Project project);

	@GET
	@Path("/_id/{_id}")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet(DataSet.INPUT)
	public Project get(@PathParam("_id") @ServiceParam("_id") ObjectId _id);

	@POST
	@Path("/count/")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	public long count(@ServiceParam(ServiceParam.FILTER) BasicDBObject filter);

	@POST
	@Path("/ds/")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	public List<ProjectInfo> createDataSet(@ServiceParam(ServiceParam.CONDITION) BasicDBObject condition);
}
