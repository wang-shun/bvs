package com.bizvisionsoft.demo.rsclient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.bizivisionsoft.widgets.gantt.Gantt;
import com.bizvisionsoft.bruicommons.annotation.CreateUI;
import com.bizvisionsoft.bruicommons.annotation.GetContainer;
import com.bizvisionsoft.bruicommons.annotation.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class GanttDemo {

	@Inject
	private IBruiService bruiService;

	@GetContainer
	private Composite content;

	@CreateUI
	private void createUI(Composite parent) {
		parent.setLayout(new FillLayout());
		Gantt gantt = new Gantt(parent, SWT.BORDER);
		JsonObject config = new JsonObject().add("xml_date", "%Y-%m-%d %H:%i:%s");
		gantt.setConfig(config);

		Calendar cal = Calendar.getInstance();
		Date from = cal.getTime();
		cal.add(Calendar.MONTH, 6);
		Date to = cal.getTime();
		gantt.setInitDateRange(from, to);

		try {
			InputStream s = GanttDemo.class.getResourceAsStream("demodata.json");
			JsonObject inputData = JsonObject.readFrom(new InputStreamReader(s));
			gantt.setInputData(inputData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
