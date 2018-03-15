package com.bizvisionsoft.serviceconsumer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.glassfish.jersey.client.ClientConfig;

import com.bizvisionsoft.service.FileService;
import com.bizvisionsoft.service.provider.BsonProvider;
import com.eclipsesource.jaxrs.consumer.ConsumerFactory;

public class RSClientDemo {

	public static void main(String[] args) {
		ClientConfig config = new ClientConfig().register(new BsonProvider<Object>());
		FileService service = ConsumerFactory.createConsumer("http://127.0.0.1:9158/services",config, FileService.class);
		try {
			InputStream fileInputStream;
			fileInputStream = new FileInputStream("D:/documents/M01.00项目基本信息.csv");
			service.upload(fileInputStream, "M01.00项目基本信息.csv", "tempfs","image/jpeg","ok");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
