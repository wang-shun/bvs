package com.bizvisionsoft.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.service.DataSet;
import com.bizvisionsoft.annotations.md.service.ServiceParam;
import com.bizvisionsoft.service.model.EPS;
import com.mongodb.BasicDBObject;

@Path("/eps")
public interface EPSService {
	
	
	@PUT
	@Path("/")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	public long update(BasicDBObject filterAndUpdate);
	
	@POST
	@Path("/")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	public EPS insert(EPS eps);

	@GET
	@Path("/_id/{_id}")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	public EPS get(@PathParam("_id") ObjectId _id);
	
	@POST
	@Path("/ds/")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet("EPS表格 # list")
	public List<EPS> createDataSet(@ServiceParam(ServiceParam.CONDITION) BasicDBObject condition);
	
	@POST
	@Path("/count/")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet("EPS表格 # count")
	public long count(@ServiceParam(ServiceParam.FILTER) BasicDBObject filter);

	@DELETE
	@Path("/_id/{_id}")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")	
	public long delete(@PathParam("_id") ObjectId get_id);
}
