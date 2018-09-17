package com.bizvisionsoft.serviceconsumer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.glassfish.jersey.client.ClientConfig;

import com.bizvisionsoft.service.FileService;
import com.bizvisionsoft.service.WorkService;
import com.bizvisionsoft.service.model.Work;
import com.bizvisionsoft.service.model.WorkLink;
import com.bizvisionsoft.service.provider.BsonProvider;
import com.eclipsesource.jaxrs.consumer.ConsumerFactory;
import com.mongodb.BasicDBObject;

public class RSClientDemo {

	public static void main(String[] args) {
		ClientConfig config = new ClientConfig().register(new BsonProvider<Object>());
//		testFileService(config);
		testWorkService(config);
	}

	@SuppressWarnings("unused")
	public static void testWorkService(ClientConfig config) {
		WorkService service = ConsumerFactory.createConsumer("http://127.0.0.1:9158/services",config, WorkService.class);		
		List<Work> ds = service.createTaskDataSet(new BasicDBObject());
		List<WorkLink> ls = service.createLinkDataSet(new BasicDBObject());
	}

	public static void testFileService(ClientConfig config) {
		FileService service = ConsumerFactory.createConsumer("http://127.0.0.1:9158/services",config, FileService.class);
		try {
			InputStream fileInputStream;
			fileInputStream = new FileInputStream("D:/documents/M01.00项目基本信息.csv");
			service.upload(fileInputStream, "M01.00项目基本信息.csv", "tempfs","image/jpeg","ok");
		} catch (FileNotFoundException e) {
		}
	}

}
