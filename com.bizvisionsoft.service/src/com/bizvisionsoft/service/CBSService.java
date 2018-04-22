package com.bizvisionsoft.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.service.DataSet;
import com.bizvisionsoft.annotations.md.service.ServiceParam;
import com.bizvisionsoft.service.model.CBSItem;

@Path("/cbs")
public interface CBSService {

	@GET
	@Path("/scope/root/{_id}")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet("CBS/list")
	public List<CBSItem> getScopeRoot(
			@PathParam("_id") @ServiceParam(ServiceParam.ROOT_CONTEXT_INPUT_OBJECT_ID) ObjectId scope_id);

	@GET
	@Path("/{_id}/sub")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	public List<CBSItem> getSubCBSItems(@PathParam("_id") ObjectId parent_id);
	
	@GET
	@Path("/{_id}/count")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	public long countSubCBSItems(@PathParam("_id") ObjectId parent_id);

	@POST
	@Path("/")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	public CBSItem insertCBSItem(CBSItem o);
	
	@DELETE
	@Path("/_id/{_id}")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet(DataSet.DELETE)
	public void delete(@PathParam("_id") @ServiceParam(ServiceParam._ID) ObjectId _id);

}