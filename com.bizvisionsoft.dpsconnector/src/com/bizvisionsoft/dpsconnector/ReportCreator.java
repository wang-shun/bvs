package com.bizvisionsoft.dpsconnector;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;

import com.bizvpm.dps.client.IProcessorManager;
import com.bizvpm.dps.client.Result;
import com.bizvpm.dps.client.Task;

public class ReportCreator {

	public static final String REPORT = "com.bizvpm.dps.processor.report:birtreport";

	public static void createReport(String dataURL, File templateFile, ServletOutputStream os, String id, String db,
			String col) throws Exception {
		IProcessorManager manager = DPSConnector.getProcessManager();
		Task task = new Task();
		task.setName("Create report");
		task.setPriority(Task.PRIORITY_1);
		task.setValue("design", templateFile);
		task.setValue("output", "pdf");
		// task.setValue("output", "excel");

		HashMap<String, String> map = new HashMap<String, String>();
		if (dataURL != null) {
			map.put("FILELIST", dataURL);
		} else {
			map.put("FILELIST", "http://127.0.0.1/xmlpo?id=" + id + "&db=" + db + "&col=" + col);
		}
		task.setValue("datasource_parameter", map);
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
