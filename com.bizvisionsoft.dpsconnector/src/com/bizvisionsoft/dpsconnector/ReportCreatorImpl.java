package com.bizvisionsoft.dpsconnector;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.bizvisionsoft.service.dps.ReportCreator;
import com.bizvpm.dps.client.IProcessorManager;
import com.bizvpm.dps.client.Result;
import com.bizvpm.dps.client.Task;

public class ReportCreatorImpl implements ReportCreator {

	public static final String REPORT = "com.bizvpm.dps.processor.report:birtreport";

	public void createReport(Map<String, String> parameter, String outputType, Object templateFile, OutputStream os)
			throws Exception {
		IProcessorManager manager = DPSConnector.getProcessManager();
		Task task = new Task();
		task.setName("Create report");
		task.setPriority(Task.PRIORITY_1);
		task.setValue("design", templateFile);
		task.setValue("output", outputType);

		task.setValue("datasource_parameter", parameter);
		Result result = manager.runTask(task, REPORT);
		InputStream is = result.getInputStream("result");
		byte[] data = new byte[1024];
		int length = -1;
		while ((length = is.read(data)) != -1) {
			os.write(data, 0, length);
		}
		is.close();
	}

}
