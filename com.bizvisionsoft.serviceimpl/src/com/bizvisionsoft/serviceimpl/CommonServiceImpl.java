package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.bizvisionsoft.service.CommonService;
import com.bizvisionsoft.service.model.Certificate;
import com.bizvisionsoft.service.model.Equipment;
import com.bizvisionsoft.service.model.ResourceType;
import com.bizvisionsoft.service.model.User;
import com.mongodb.BasicDBObject;

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
	public List<User> getHRResources(ObjectId _id) {
		ArrayList<User> r = new ArrayList<User>();
		Service.col(User.class).find(new BasicDBObject("resourceType_id", _id)).into(r);
		return r;
	}

	@Override
	public List<Equipment> getERResources(ObjectId _id) {
		ArrayList<Equipment> r = new ArrayList<Equipment>();
		Service.col(Equipment.class).find(new BasicDBObject("resourceType_id", _id)).into(r);
		return r;
	}

	@Override
	public ResourceType getResourceType(ObjectId _id) {
		return get(_id, ResourceType.class);
	}

	@Override
	public long countHRResources(ObjectId _id) {
		return Service.col(User.class).count(new BasicDBObject("resourceType_id", _id));
	}

	@Override
	public long countERResources(ObjectId _id) {
		return Service.col(Equipment.class).count(new BasicDBObject("resourceType_id", _id));
	}

	@Override
	public List<Equipment> getEquipments() {
		List<Equipment> result = new ArrayList<>();
		Service.col(Equipment.class).find().into(result);
		return result;
	}

	@Override
	public Equipment insertEquipment(Equipment cert) {
		return insert(cert, Equipment.class);
	}

	@Override
	public long deleteEquipment(ObjectId _id) {
		return delete(_id, Equipment.class);
	}

	@Override
	public long updateEquipment(BasicDBObject fu) {
		return update(fu, Equipment.class);
	}

}
