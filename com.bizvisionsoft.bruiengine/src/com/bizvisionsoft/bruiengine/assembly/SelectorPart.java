package com.bizvisionsoft.bruiengine.assembly;

import java.util.ArrayList;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.GetReturnCode;
import com.bizvisionsoft.annotations.ui.common.GetReturnResult;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.BruiAssemblyEngine;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiEditorContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.bruiengine.ui.BruiToolkit;

public class SelectorPart {

	private Assembly config;

	@Inject
	private IBruiService bruiService;

	@Inject
	private IBruiEditorContext context;

	@GetReturnCode
	private int returnCode = Window.CANCEL;

	private Composite container;

	private BruiAssemblyContext containerContext;

	private Assembly gridConfig;

	@GetReturnResult
	private ArrayList<Object> result;

	public SelectorPart(Assembly assembly) {
		this.config = assembly;
		String gridConfigId = assembly.getSelectorGridAssemblyId();
		gridConfig = Brui.site.getAssembly(gridConfigId);
		this.result = new ArrayList<>();
	}
	

	private void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public IBruiService getBruiService() {
		return bruiService;
	}

	public IBruiEditorContext getContext() {
		return context;
	}

	@CreateUI
	private void createUI(Composite contentArea) {

		FormLayout layout = new FormLayout();
		layout.spacing = context.isEmbedded() ? 8 : 16;
		layout.marginWidth = context.isEmbedded() ? 8 : 16;
		layout.marginHeight = context.isEmbedded() ? 8 : 16;
		contentArea.setLayout(layout);

		/////////////////////////////////////////////////////////////////////////////////////////////
		// 扩展DataGrid
		// 1. 实例化，设置上下文
		BruiAssemblyEngine brui = BruiAssemblyEngine.newInstance(gridConfig);
		context.add(containerContext = new BruiAssemblyContext().setParent(context));
		containerContext.setEngine(brui);

		GridPart grid = ((GridPart) brui.getTarget());

		// 如果是多选，添加check
		grid.setCheckOn(config.isSelectorMultiSelection());

		// 如果表格有查询字段定义
		grid.setQueryOn(gridConfig.getFields() != null && !gridConfig.getFields().isEmpty());

		// 2. 设置表格项的选择
		grid.addItemSelector(new ToolItemDescriptor("选择", e -> {
			if ("choice".equals(e.text) ) {
				setReturnCode(Window.OK);
				result.add(e.item.getData());
				bruiService.closeCurrentPart();
			}
		}));

		if (config.isSelectorMultiSelection()) {
			grid.addToolItem(new ToolItemDescriptor("全选", BruiToolkit.CSS_INFO, e -> {
				grid.setCheckAll(true);
			}));

			grid.addToolItem(new ToolItemDescriptor("清除", BruiToolkit.CSS_INFO, e -> {
				grid.setCheckAll(false);
			}));
			
			grid.addToolItem(new ToolItemDescriptor("确定", BruiToolkit.CSS_NORMAL, e -> {
				setReturnCode(Window.OK);
				result.addAll(grid.getCheckedItems());
				bruiService.closeCurrentPart();
			}));
		}

		grid.addToolItem(new ToolItemDescriptor("取消", BruiToolkit.CSS_WARNING, e -> {
			setReturnCode(Window.CANCEL);
			result.clear();
			bruiService.closeCurrentPart();
		}));

		// 4. 初始化并创建UI
		container = new Composite(contentArea, SWT.BORDER);
		container.setBackground(container.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		brui.init(new IServiceWithId[] { bruiService, containerContext }).createUI(container);

		FormData fd = new FormData();
		container.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);
		container.moveAbove(null);
		contentArea.layout();
	}



}
