package com.bizvisionsoft.server.rs;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizvisionsoft.service.CBSService;
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
import com.bizvisionsoft.service.RevenueService;
import com.bizvisionsoft.service.RiskService;
import com.bizvisionsoft.service.SystemService;
import com.bizvisionsoft.service.UniversalDataService;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.WorkReportService;
import com.bizvisionsoft.service.WorkService;
import com.bizvisionsoft.service.WorkSpaceService;
import com.bizvisionsoft.service.provider.BsonProvider;
import com.bizvisionsoft.serviceimpl.CBSServiceImpl;
import com.bizvisionsoft.serviceimpl.DocumentServiceImpl;
import com.bizvisionsoft.serviceimpl.EPSServiceImpl;
import com.bizvisionsoft.serviceimpl.FileServiceImpl;
import com.bizvisionsoft.serviceimpl.OBSServiceImpl;
import com.bizvisionsoft.serviceimpl.OrganizationServiceImpl;
import com.bizvisionsoft.serviceimpl.PermissionServiceImpl;
import com.bizvisionsoft.serviceimpl.ProductServiceImpl;
import com.bizvisionsoft.serviceimpl.ProgramServiceImpl;
import com.bizvisionsoft.serviceimpl.ProjectServiceImpl;
import com.bizvisionsoft.serviceimpl.ProjectTemplateServiceImpl;
import com.bizvisionsoft.serviceimpl.RevenueServiceImpl;
import com.bizvisionsoft.serviceimpl.RiskServiceImpl;
import com.bizvisionsoft.serviceimpl.SystemServiceImpl;
import com.bizvisionsoft.serviceimpl.UniversalDataServiceImpl;
import com.bizvisionsoft.serviceimpl.UserServiceImpl;
import com.bizvisionsoft.serviceimpl.WorkReportServiceImpl;
import com.bizvisionsoft.serviceimpl.WorkServiceImpl;
import com.bizvisionsoft.serviceimpl.WorkSpaceServiceImpl;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	final static Logger logger = LoggerFactory.getLogger(Activator.class);

	@SuppressWarnings("rawtypes")
	private final List<ServiceRegistration> regs = new ArrayList<>();

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext )
	 */
	public void start(BundleContext bc) throws Exception {
		Activator.context = bc;
		
		regs.add(bc.registerService(BsonProvider.class.getName(), new BsonProvider<Object>(), null));
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		// 下面开始注册服务
		regs.add(bc.registerService(SystemService.class.getName(), new SystemServiceImpl(), null));
		
		regs.add(bc.registerService(FileService.class.getName(), new FileServiceImpl(), null));
		
		regs.add(bc.registerService(UserService.class.getName(), new UserServiceImpl(), null));

		regs.add(bc.registerService(OrganizationService.class.getName(), new OrganizationServiceImpl(), null));

		regs.add(bc.registerService(WorkService.class.getName(), new WorkServiceImpl(), null));

		regs.add(bc.registerService(EPSService.class.getName(), new EPSServiceImpl(), null));

		regs.add(bc.registerService(OBSService.class.getName(), new OBSServiceImpl(), null));

		regs.add(bc.registerService(CBSService.class.getName(), new CBSServiceImpl(), null));

		regs.add(bc.registerService(RiskService.class.getName(), new RiskServiceImpl(), null));

		regs.add(bc.registerService(ProjectService.class.getName(), new ProjectServiceImpl(), null));
		
		regs.add(bc.registerService(ProductService.class.getName(), new ProductServiceImpl(), null));

		regs.add(bc.registerService(ProgramService.class.getName(), new ProgramServiceImpl(), null));

		regs.add(bc.registerService(DocumentService.class.getName(), new DocumentServiceImpl(), null));

		regs.add(bc.registerService(ProjectTemplateService.class.getName(), new ProjectTemplateServiceImpl(), null));

		regs.add(bc.registerService(WorkSpaceService.class.getName(), new WorkSpaceServiceImpl(), null));

		regs.add(bc.registerService(WorkReportService.class.getName(), new WorkReportServiceImpl(), null));

		regs.add(bc.registerService(PermissionService.class.getName(), new PermissionServiceImpl(), null));
		
		regs.add(bc.registerService(RevenueService.class.getName(), new RevenueServiceImpl(), null));
		
		regs.add(bc.registerService(UniversalDataService.class.getName(), new UniversalDataServiceImpl(), null));
		
		logger.info("RS服务已注册");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@SuppressWarnings("rawtypes")
	public void stop(BundleContext bundleContext) throws Exception {
		for (ServiceRegistration serviceRegistration : regs) {
			serviceRegistration.unregister();
		}
		Activator.context = null;
	}

}
