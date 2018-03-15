package com.bizvisionsoft.serviceconsumer;

import com.bizvisionsoft.service.FileService;
import com.bizvisionsoft.service.OrganizationService;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.provider.BsonProvider;
import com.eclipsesource.jaxrs.consumer.ConsumerPublisher;
import org.osgi.service.component.ComponentContext;

public class Publisher {

	private ConsumerPublisher publisher;

	public void activate(ComponentContext context) {
		String url = (String) context.getBundleContext().getProperty("com.bizvisionsoft.service.url");
		publisher.publishConsumers(url,
				new Class<?>[] { FileService.class, UserService.class, OrganizationService.class },
				new Object[] { new BsonProvider<Object>() });
	}

	public void bind(ConsumerPublisher publisher) {
		this.publisher = publisher;
	}

	public void modified(ComponentContext context) {
	}

	public void unbind(ConsumerPublisher publisher) {
	}

	public void deactivate(ComponentContext context) {
	}

}
