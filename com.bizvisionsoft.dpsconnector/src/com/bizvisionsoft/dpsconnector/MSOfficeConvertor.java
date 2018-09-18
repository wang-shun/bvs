package com.bizvisionsoft.dpsconnector;

import java.io.File;

import com.bizvpm.dps.client.IProcessorManager;
import com.bizvpm.dps.client.Result;
import com.bizvpm.dps.client.Task;

public class MSOfficeConvertor {

	public static final String MSOFFICE_PDF = "com.bizvpm.dps.processor.msoffice:msoffice.msofficeconverter";

	public static void convert(File inputFile, File outputFile)
			throws Exception {
		IProcessorManager manager = DPSConnector.getProcessManager();
		Task task = new Task();
		task.setName("Convert Office File to PDF");
		task.setPriority(Task.PRIORITY_1);
		String sourceType = inputFile.getName().substring(
				inputFile.getName().lastIndexOf(".") + 1);
		task.setValue("sourceType", sourceType);
		task.setValue("targetType", "pdf");
		task.setFileValue("file", inputFile);

		Result result = manager.runTask(task, MSOFFICE_PDF);
		result.writeToFile("file", outputFile);
	}
	


}
