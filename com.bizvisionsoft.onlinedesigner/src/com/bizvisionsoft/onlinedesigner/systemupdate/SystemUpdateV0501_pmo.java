package com.bizvisionsoft.onlinedesigner.systemupdate;

import org.eclipse.jface.dialogs.MessageDialog;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.SystemService;
import com.bizvisionsoft.serviceconsumer.Services;

public class SystemUpdateV0501_pmo {

	@Inject
	private IBruiService brui;

	@Execute
	public void execute() {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("本次更新内容：<br/>");
			sb.append("1. 增加组织模板，在项目创建后，可直接通过套用组织模板的方式创建项目团队。<br/>");
			sb.append("2. 修改组织中项目承担单位字段名称为：qualifiedContractor。<br/>");
			sb.append("3. 增加报告管理，在其中可以查看我管理的项目的日报、周报和月报。<br/>");
			sb.append("4. 增加项目角色：项目管理组（角色编号为：PMO），功能角色：供应链经理、制造经理、财务经理，");
			sb.append("并通过PMO角色对所有项目、采购管理、生产管理、成本管理和报告管理中显示的内容进行细分。<br/>");
			sb.append("所有项目：具有项目总监和项目管理员权限的账户可以访问，具有项目总监权限的用户在其中可以查看所有项目信息。项目管理员权限的账户只能看到其作为项目PMO团队成员的项目。<br/>");
			sb.append(
					"采购管理：具有供应链管理和供应链经理权限的账户可以访问，具有供应链管理权限的用户在其中可以查看所有项目的采购工作。供应链经理权限的账户只能看到其作为项目PMO团队成员的项目的采购工作。<br/>");
			sb.append("生产管理：具有制造管理和制造经理权限的账户可以访问，具有制造管理权限的用户在其中可以查看所有项目的生产工作。制造经理权限的账户只能看到其作为项目PMO团队成员的的生产工作。<br/>");
			sb.append("成本管理：具有成本管理和财务经理权限的账户可以访问，具有成本管理权限的用户在其中可以查看所有项目的成本数据。财务经理权限的账户只能看到其作为项目PMO团队成员的项目成本数据。<br/>");
			sb.append("报告管理：具有项目总监和项目管理员权限的账户可以访问，具有项目总监权限的用户在其中可以查看所有项目的报告。项目管理员权限的账户只能看到其作为项目PMO团队成员的项目的报告。<br/>");
			sb.append("<span class='layui-badge'>注</span>：");
			sb.append("更新该功能时，系统将自动在已创建的项目中添加PMO团队。该功能更新完成后，请在服务器端存放js查询的目类中添加以下三个文件：<br/>");
			sb.append("1.查询-项目PMO成员.js；<br/>");
			sb.append("2.追加-CBS-CBS叶子节点ID.js；<br/>");
			sb.append("3.追加-CBSScope-CBS叶子节点ID.js。");
			sb.append("<br/>");
			sb.append("请确认进行本次更新。");
			if (brui.confirm("更新项目管理组", sb.toString())) {
				Services.get(SystemService.class).updateSystem("5.1M1", "PMO");
				Layer.message("完成项目管理组更新");
			}
		} catch (Exception e) {
			MessageDialog.openError(brui.getCurrentShell(), "项目管理组更新错误", e.getMessage());
		}
	}
}
