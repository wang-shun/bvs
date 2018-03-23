package com.bizvisionsoft.pms.assembly;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class NestTestContainer2 {

	@Inject
	private IBruiService bruiService;


	@CreateUI
	private void createUI(Composite parent) {
		parent.setLayout(new FillLayout());
		TableViewer viewer = new TableViewer(parent, SWT.BORDER);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new LabelProvider());
		viewer.setInput(
				new String[] { "�����1", "�����2", "�����3", "�����4", "�ϳ��ı����,�ϳ��ı����,�ϳ��ı����,�ϳ��ı����,�ϳ��ı����,�ϳ��ı����,�ϳ��ı����" });

	}

}
