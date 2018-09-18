package com.bizvisionsoft.dpsconnector;

import java.io.File;

import com.bizvpm.dps.client.IProcessorManager;
import com.bizvpm.dps.client.Result;
import com.bizvpm.dps.client.Task;

public class DWGConvertor {

	public static final String DWG_PDF = "com.bizvpm.dps.processor.acmecad:acmecad.acmecadconverter";

	public static void convertDWG(File inputFile, File outputFile)
			throws Exception {
		IProcessorManager manager = DPSConnector.getProcessManager();
		Task task = new Task();
		task.setName("Convert DWG to PDF");
		task.setPriority(Task.PRIORITY_1);
		task.setValue("sourceType", "dwg");
		task.setValue("targetType", "pdf");
		task.setValue("autoZoomExtend", Boolean.TRUE);
		task.setValue("rasterPixel", 1);
		task.setValue("backgroundColor", 7);
		task.setValue("lineWeight", 1);
		task.setValue("autoSize", Boolean.TRUE);
		task.setFileValue("file", inputFile);

		Result result = manager.runTask(task, DWG_PDF);
		result.writeToFile("file", outputFile);
	}


}
