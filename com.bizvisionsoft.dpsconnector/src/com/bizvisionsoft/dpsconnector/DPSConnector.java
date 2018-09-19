package com.bizvisionsoft.dpsconnector;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizvisionsoft.service.dps.DWGConvertor;
import com.bizvisionsoft.service.dps.EmailSender;
import com.bizvisionsoft.service.dps.OfficeConvertor;
import com.bizvisionsoft.service.dps.ReportCreator;
import com.bizvpm.dps.client.DPS;
import com.bizvpm.dps.client.IProcessorManager;

public class DPSConnector implements BundleActivator {

	public static Logger logger = LoggerFactory.getLogger(DPSConnector.class);

	private static BundleContext context;
	
	private final List<ServiceRegistration<?>> regs = new ArrayList<>();

	static BundleContext getContext() {
		return context;
	}

	private static String[] dpsServerList;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bc) throws Exception {
		DPSConnector.context = bc;
		try {
			String str = context.getProperty("com.bizvisionsoft.service.DPSList");
			if (str == null || str.isEmpty()) {
				logger.warn("没有配置DPS服务器，请使用启动参数-Dcom.bizvisionsoft.service.DPSList=<RS服务器IP>:<端口号>，使用;分割多个服务器。");
			} else {
				dpsServerList = str.split(";");
				logger.info("DPS服务器:" + str);
			}
		} catch (Exception e) {
			logger.error("DPS服务器配置错误", e);
		}
		
		//
		regs.add(bc.registerService(EmailSender.class.getName(), new EmailSenderImpl(), null));

		regs.add(bc.registerService(DWGConvertor.class.getName(), new DWGConvertorImpl(), null));

		regs.add(bc.registerService(OfficeConvertor.class.getName(), new OfficeConvertorMSImpl(), null));
		
		regs.add(bc.registerService(ReportCreator.class.getName(), new ReportCreatorImpl(), null));
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		for (ServiceRegistration<?> serviceRegistration : regs) {
			serviceRegistration.unregister();
		}
		DPSConnector.context = null;
	}

	public static IProcessorManager getProcessManager() {
		for (int i = 0; i < dpsServerList.length; i++) {
			String address = "http://" + dpsServerList[i] + "/dps/server?wsdl";
			DPS service = new DPS(address);
			try {
				IProcessorManager proc = service.getProcessorManager();
				return proc;
			} catch (Exception e) {
				logger.warn("无法连接" + dpsServerList[i] + "," + e.getMessage());
			}
		}
		logger.error("无法连接到所有的DPS服务器。请检查DPS服务器配置。");
		throw new RuntimeException("无法连接到所有的DPS服务器。请检查DPS服务器配置。");
	}

}
