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
import com.bizvisionsoft.service.model.Certificate;
import com.bizvisionsoft.service.model.Equipment;
import com.bizvisionsoft.service.model.ResourceType;
import com.bizvisionsoft.service.model.User;
import com.mongodb.BasicDBObject;

@Path("/common")
public interface CommonService {

	@POST
	@Path("/cert/ds")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet("执业资格列表/" + DataSet.LIST)
	public List<Certificate> getCertificates();

	@POST
	@Path("/cert/names/ds")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet("执业资格选择器列表/" + DataSet.LIST)
	public List<String> getCertificateNames();

	@POST
	@Path("/cert/")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet("执业资格列表/" + DataSet.INSERT)
	public Certificate insertCertificate(@ServiceParam(ServiceParam.OBJECT) Certificate cert);

	@DELETE
	@Path("/cert/_id/{_id}")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet("执业资格列表/" + DataSet.DELETE)
	public long deleteCertificate(@PathParam("_id") @ServiceParam(ServiceParam._ID) ObjectId _id);

	@PUT
	@Path("/cert/")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet("执业资格列表/" + DataSet.UPDATE)
	public long updateCertificate(BasicDBObject filterAndUpdate);
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	@POST
	@Path("/restype/ds")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet("资源类型/" + DataSet.LIST)
	public List<ResourceType> getResourceType();

	@POST
	@Path("/restype/")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	public ResourceType insertResourceType(ResourceType resourceType);

	@DELETE
	@Path("/restype/_id/{_id}")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet("资源类型/" + DataSet.DELETE)
	public long deleteResourceType(@PathParam("_id") @ServiceParam(ServiceParam._ID) ObjectId _id);

	@PUT
	@Path("/restype/")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet("资源类型/" + DataSet.UPDATE)
	public long updateResourceType(BasicDBObject filterAndUpdate);

	@GET
	@Path("/restype/{_id}/hr")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	public List<User> getHRResources(@PathParam("_id") ObjectId _id);

	@GET
	@Path("/restype/{_id}/hr/count")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	public long countHRResources(@PathParam("_id") ObjectId _id);

	@GET
	@Path("/restype/{_id}/er")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	public List<Equipment> getERResources(@PathParam("_id") ObjectId _id);

	@GET
	@Path("/restype/{_id}/er/count")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	public long countERResources(@PathParam("_id") ObjectId _id);

	@GET
	@Path("/restype/{_id}")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	public ResourceType getResourceType(@PathParam("_id") ObjectId _id);
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	@POST
	@Path("/euip/ds")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet({ "设备设施/" + DataSet.LIST, "设备设施选择表格/" + DataSet.LIST })
	public List<Equipment> getEquipments();

	@POST
	@Path("/euip/")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	public Equipment insertEquipment(Equipment cert);

	@DELETE
	@Path("/euip/_id/{_id}")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet("设备设施/" + DataSet.DELETE)
	public long deleteEquipment(@PathParam("_id") @ServiceParam(ServiceParam._ID) ObjectId _id);

	@PUT
	@Path("/euip/")
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@DataSet("设备设施/" + DataSet.UPDATE)
	public long updateEquipment(BasicDBObject filterAndUpdate);
}
