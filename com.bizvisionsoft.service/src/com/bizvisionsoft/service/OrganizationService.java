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
import com.bizvisionsoft.service.model.Organization;
import com.mongodb.BasicDBObject;


@Path("/org")
public interface OrganizationService {

	@POST
	@Path("/")
	@Consumes("application/json")
	@Produces("application/json; charset=UTF-8")
	public Organization insert(Organization orgInfo);
	
	@GET
	@Path("/{_id}")
	@Consumes("application/json")
	@Produces("application/json; charset=UTF-8")
	public Organization get(@PathParam("_id") ObjectId _id);
	
	
	@POST
	@Path("/ds/")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet("组织选择器 # "+ DataSet.LIST)
	public List<Organization> createDataSet(
			@ServiceParam(ServiceParam.CONDITION) BasicDBObject condition);
	
	
	@POST
	@Path("/count/")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet("组织选择器 # count")
	public long count(@ServiceParam(ServiceParam.FILTER) BasicDBObject filter);

}