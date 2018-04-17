package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.bizvisionsoft.service.CommonService;
import com.bizvisionsoft.service.model.Calendar;
import com.bizvisionsoft.service.model.Certificate;
import com.bizvisionsoft.service.model.Equipment;
import com.bizvisionsoft.service.model.ResourceType;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Aggregates;

public class CommonServiceImpl extends BasicServiceImpl implements CommonService {

	@Override
	public List<Certificate> getCertificates() {
		List<Certificate> result = new ArrayList<>();
		Service.col(Certificate.class).find().into(result);
		return result;
	}

	@Override
	public Certificate insertCertificate(Certificate cert) {
		return insert(cert, Certificate.class);
	}

	@Override
	public long deleteCertificate(ObjectId _id) {
		// TODO

		return delete(_id, Certificate.class);
	}

	@Override
	public long updateCertificate(BasicDBObject fu) {
		return update(fu, Certificate.class);
	}

	@Override
	public List<String> getCertificateNames() {
		ArrayList<String> result = new ArrayList<String>();
		Service.col(Certificate.class).distinct("name", String.class).into(result);
		return result;
	}

	@Override
	public List<ResourceType> getResourceType() {
		List<ResourceType> result = new ArrayList<>();
		Service.col(ResourceType.class).find().into(result);
		return result;
	}

	@Override
	public ResourceType insertResourceType(ResourceType resourceType) {
		return insert(resourceType, ResourceType.class);
	}

	@Override
	public long deleteResourceType(ObjectId _id) {
		// TODO
		return delete(_id, ResourceType.class);
	}

	@Override
	public long updateResourceType(BasicDBObject fu) {
		return update(fu, ResourceType.class);
	}

	@Override
	public List<Equipment> getERResources(ObjectId _id) {
		return queryEquipments(new BasicDBObject("resourceType_id", _id));
	}

	private ArrayList<Equipment> queryEquipments(BasicDBObject condition) {
		ArrayList<Bson> pipeline = new ArrayList<Bson>();
		if (condition != null)
			pipeline.add(Aggregates.match(condition));

		appendOrgFullName(pipeline,"org_id","orgFullName");

		ArrayList<Equipment> result = new ArrayList<Equipment>();
		Service.col(Equipment.class).aggregate(pipeline).into(result);
		return result;
	}

	@Override
	public ResourceType getResourceType(ObjectId _id) {
		return get(_id, ResourceType.class);
	}

	@Override
	public long countERResources(ObjectId _id) {
		return Service.col(Equipment.class).count(new BasicDBObject("resourceType_id", _id));
	}

	@Override
	public List<Equipment> getEquipments() {
		return queryEquipments(null);
	}

	@Override
	public Equipment insertEquipment(Equipment cert) {
		return insert(cert, Equipment.class);
	}

	@Override
	public long deleteEquipment(ObjectId _id) {
		// TODO
		return delete(_id, Equipment.class);
	}

	@Override
	public long updateEquipment(BasicDBObject fu) {
		return update(fu, Equipment.class);
	}

	@Override
	public List<Calendar> getCalendars() {
		List<Calendar> result = new ArrayList<>();
		Service.col(Calendar.class).find().into(result);
		return result;
	}

	@Override
	public Calendar insertCalendar(Calendar obj) {
		return insert(obj, Calendar.class);
	}

	@Override
	public long deleteCalendar(ObjectId _id) {
		// TODO
		return delete(_id, Calendar.class);
	}

	@Override
	public long updateCalendar(BasicDBObject fu) {
		return update(fu, Calendar.class);
	}

	@Override
	public void addCalendarWorktime(BasicDBObject r, ObjectId _cal_id) {
		Service.col(Calendar.class).updateOne(new BasicDBObject("_id", _cal_id),
				new BasicDBObject("$addToSet", new BasicDBObject("workTime", r)));
	}

	/**
	 * db.getCollection('calendar').updateOne({"workTime._id":ObjectId("5ad49b5a85e0fb335c355fae")},
	 * {$set:{"workTime.$":"aaa"}
	 * 
	 * })
	 */
	@Override
	public void updateCalendarWorkTime(BasicDBObject r) {
		Service.col(Calendar.class).updateOne(new BasicDBObject("workTime._id", r.get("_id")),
				new BasicDBObject("$set", new BasicDBObject("workTime.$", r)));
	}

	@Override
	public void deleteCalendarWorkTime(ObjectId _id) {
		Service.col(Calendar.class).updateOne(new BasicDBObject("workTime._id", _id),
				new BasicDBObject("$pull", new BasicDBObject("workTime", new BasicDBObject("_id", _id))));
	}

}
