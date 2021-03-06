package com.bizvisionsoft.serviceconsumer;

import com.bizvisionsoft.service.CBSService;
import com.bizvisionsoft.service.CommonService;
import com.bizvisionsoft.service.DocumentService;
import com.bizvisionsoft.service.EPSService;
import com.bizvisionsoft.service.FileService;
import com.bizvisionsoft.service.OBSService;
import com.bizvisionsoft.service.OrganizationService;
import com.bizvisionsoft.service.PermissionService;
import com.bizvisionsoft.service.ProductService;
import com.bizvisionsoft.service.ProjectService;
import com.bizvisionsoft.service.ProgramService;
import com.bizvisionsoft.service.ProjectTemplateService;
import com.bizvisionsoft.service.ReportService;
import com.bizvisionsoft.service.RevenueService;
import com.bizvisionsoft.service.RiskService;
import com.bizvisionsoft.service.SystemService;
import com.bizvisionsoft.service.UniversalDataService;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.WorkReportService;
import com.bizvisionsoft.service.WorkService;
import com.bizvisionsoft.service.WorkSpaceService;
import com.bizvisionsoft.service.provider.BsonProvider;
import com.eclipsesource.jaxrs.consumer.ConsumerPublisher;
import org.osgi.service.component.ComponentContext;

public class Publisher {

	private ConsumerPublisher publisher;

	public void activate(ComponentContext context) {
		publisher.publishConsumers(

				(String) context.getBundleContext().getProperty("com.bizvisionsoft.service.url"),

				new Class<?>[] { SystemService.class,

						ReportService.class, 
						
						FileService.class,

						CommonService.class,

						UserService.class,

						OrganizationService.class,

						OBSService.class,

						CBSService.class,

						RevenueService.class,

						RiskService.class,

						WorkService.class,

						ProjectService.class,

						ProgramService.class,

						ProductService.class,

						DocumentService.class,

						ProjectTemplateService.class,

						EPSService.class,

						WorkSpaceService.class,

						WorkReportService.class,

						PermissionService.class,

						UniversalDataService.class

				},

				new Object[] {

						new BsonProvider<Object>()

				});
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
