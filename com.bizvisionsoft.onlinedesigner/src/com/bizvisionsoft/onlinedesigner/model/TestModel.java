package com.bizvisionsoft.onlinedesigner.model;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.service.CommonService;
import com.bizvisionsoft.service.ServicesLoader;
import com.bizvisionsoft.service.model.RemoteFile;
import com.bizvisionsoft.service.model.ResourceType;

public class TestModel {
	
	@ReadValue
	@WriteValue
	private String f1;
	
	@ReadValue
	@WriteValue
	private String f2;
	
	@ReadValue
	@WriteValue
	private Double[] f3;
	
	@ReadValue
	@WriteValue
	private String f4;
	
	@ReadValue
	@WriteValue
	private String f5;
	
	@ReadValue
	@WriteValue
	private String f6;
	
	@ReadValue
	@WriteValue
	private Boolean f7;
	
	@ReadValue
	@WriteValue
	private List<String> f8;
	
	@ReadValue
	@WriteValue
	private Date f9;
	
	@ReadValue
	@WriteValue
	private String f10;
	
	private ObjectId f11;
	
	
	@WriteValue("f11 ")
	public void setResourceType(ResourceType rt) {
		this.f11 = Optional.ofNullable(rt).map(o -> o.get_id()).orElse(null);
	}

	@ReadValue("f11 ")
	public ResourceType getResourceType() {
		return Optional.ofNullable(f11)
				.map(_id -> ServicesLoader.get(CommonService.class).getResourceType(_id)).orElse(null);
	}

	@ReadValue
	@WriteValue
	private List<RemoteFile> f12;
	
	@ReadValue
	@WriteValue
	private List<RemoteFile> f13;
	
	
}
