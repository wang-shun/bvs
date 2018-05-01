package com.bizvisionsoft.pms.project.dataset;

import java.util.List;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.service.DataSet;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.ProjectService;
import com.bizvisionsoft.service.model.Project;
import com.bizvisionsoft.service.model.WorkInfo;
import com.bizvisionsoft.serviceconsumer.Services;

public class ProjectSchedule {
	
	@Inject
	private BruiAssemblyContext context;

	@Inject
	private IBruiService brui;


	private ObjectId project_id;

	@Init
	private void init() {
		project_id = ((Project) context.getRootInput()).get_id();
	}
	
	@DataSet("阶段选择器列表/list")
	private List<WorkInfo> listStage(){
		return Services.get(ProjectService.class).listStage(project_id);
	}
	
	@DataSet("阶段选择器列表/count")
	private long countStage(){
		return Services.get(ProjectService.class).countStage(project_id);
	}
	
	

}
