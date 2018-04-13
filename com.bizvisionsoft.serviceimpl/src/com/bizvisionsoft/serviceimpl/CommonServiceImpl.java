package com.bizvisionsoft.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.bizvisionsoft.service.CommonService;
import com.bizvisionsoft.service.model.Certificate;
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

}
