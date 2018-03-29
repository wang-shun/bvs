package com.bizvisionsoft.server.rs;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.bizvisionsoft.service.EPSService;
import com.bizvisionsoft.service.FileService;
import com.bizvisionsoft.service.OrganizationService;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.WorkService;
import com.bizvisionsoft.service.provider.BsonProvider;
import com.bizvisionsoft.serviceimpl.EPSServiceImpl;
import com.bizvisionsoft.serviceimpl.FileServiceImpl;
import com.bizvisionsoft.serviceimpl.OrganizationServiceImpl;
import com.bizvisionsoft.serviceimpl.UserServiceImpl;
import com.bizvisionsoft.serviceimpl.WorkServiceImpl;

public class Activator implements BundleActivator {

	private static BundleContext context;

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
		//下面开始注册服务
		regs.add(bc.registerService(FileService.class.getName(), new FileServiceImpl(), null));

		regs.add(bc.registerService(UserService.class.getName(), new UserServiceImpl(), null));
		
		regs.add(bc.registerService(OrganizationService.class.getName(), new OrganizationServiceImpl(), null));

		regs.add(bc.registerService(WorkService.class.getName(), new WorkServiceImpl(), null));
		
		regs.add(bc.registerService(EPSService.class.getName(), new EPSServiceImpl(), null));

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
