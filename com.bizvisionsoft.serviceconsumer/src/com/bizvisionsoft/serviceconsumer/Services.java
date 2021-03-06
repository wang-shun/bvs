package com.bizvisionsoft.serviceconsumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.bizvisionsoft.service.CBSService;
import com.bizvisionsoft.service.CommonService;
import com.bizvisionsoft.service.DocumentService;
import com.bizvisionsoft.service.EPSService;
import com.bizvisionsoft.service.FileService;
import com.bizvisionsoft.service.OBSService;
import com.bizvisionsoft.service.OrganizationService;
import com.bizvisionsoft.service.PermissionService;
import com.bizvisionsoft.service.ProductService;
import com.bizvisionsoft.service.ProgramService;
import com.bizvisionsoft.service.ProjectService;
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

public class Services implements BundleActivator {

	private static BundleContext bundleContext;

	private static HashMap<String, Class<?>> nameRegistry = new HashMap<>();

	private static List<ServiceReference<?>> references = new ArrayList<>();

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Services.bundleContext = bundleContext;
		// ע�������
		register(SystemService.class);
		register(ReportService.class);
		register(FileService.class);
		register(CommonService.class);
		register(UserService.class);
		register(OBSService.class);
		register(CBSService.class);
		register(RevenueService.class);
		register(OrganizationService.class);
		register(WorkService.class);
		register(ProjectService.class);
		register(ProductService.class);
		register(ProgramService.class);
		register(ProjectTemplateService.class);
		register(DocumentService.class);
		register(EPSService.class);
		register(RiskService.class);
		register(WorkSpaceService.class);
		register(WorkReportService.class);
		register(PermissionService.class);
		register(UniversalDataService.class);
	}

	private void register(Class<?> type) {
		nameRegistry.put(type.getName(), type);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		references.forEach(reference -> bundleContext.ungetService(reference));
	}

	public static <T> T get(Class<T> clazz) {
		ServiceReference<T> reference = bundleContext.getServiceReference(clazz);
		if (!references.contains(reference))
			references.add(reference);
		return bundleContext.getService(reference);
	}

	public static Object get(String classname) {
		return get(nameRegistry.get(classname));
	}

	public static Object[] getService(String classname) {
		Class<?> clazz = nameRegistry.get(classname);
		return new Object[] { clazz, get(clazz) };
	}

}
