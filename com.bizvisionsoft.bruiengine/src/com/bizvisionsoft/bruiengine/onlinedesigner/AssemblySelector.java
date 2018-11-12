package com.bizvisionsoft.bruiengine.onlinedesigner;

import java.util.Collection;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.ui.ElementViewer;
import com.bizvisionsoft.bruiengine.util.BruiToolkit;
import com.bizvisionsoft.service.tools.Check;

public class AssemblySelector extends Dialog {

	public static Collection<String> select(List<String> assemblyIds) {
		AssemblySelector as = new AssemblySelector(Display.getCurrent().getActiveShell());
		as.assemblyIds = assemblyIds;
		if (OK == as.open()) {
			return as.getSelection();
		} else {
			return null;
		}
	}

	private boolean edit;

	public static Collection<String> edit(List<String> assemblyIds) {
		AssemblySelector as = new AssemblySelector(Display.getCurrent().getActiveShell());
		as.assemblyIds = assemblyIds;
		as.edit = true;
		if (OK == as.open()) {
			return as.getSelection();
		} else {
			return null;
		}
	}

	private Collection<String> getSelection() {
		return viewer.getSelected();
	}

	private AssemblySelector(Shell parentShell) {
		super(parentShell);
	}

	private List<String> assemblyIds;
	private ElementViewer<String> viewer;

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite panel = (Composite) super.createDialogArea(parent);
		Control control = createViewer(panel);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = 628;
		gd.heightHint = 460;
		control.setLayoutData(gd);
		return panel;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.get().CANCEL_LABEL, false).setData(RWT.CUSTOM_VARIANT,
				BruiToolkit.CSS_WARNING);
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.get().OK_LABEL, true).setData(RWT.CUSTOM_VARIANT,
				BruiToolkit.CSS_NORMAL);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("选择组件");
		super.configureShell(newShell);
	}

	private Control createViewer(Composite parent) {
		viewer = new ElementViewer<String>(parent, SWT.NONE);
		viewer.setSize(200, 200);
		viewer.setEdit(edit);
		viewer.setLabelProvider(this::getLabel);
		viewer.setInput(assemblyIds);
		return viewer.getControl();
	}

	private String getLabel(Object assemblyId, boolean selected) {
		Assembly assembly = ModelLoader.site.getAssembly("" + assemblyId);
		if (assembly != null) {
			StringBuffer sb = new StringBuffer();
			if (!selected)
				sb.append("<div class='brui_elem_item' style='width:200px;height:200px;border-radius:4px;padding:16px;cursor:pointer;'>");
			else
				sb.append(
						"<div class='brui_elem_item_selected' style='width:200px;height:200px;border-radius:4px;padding:16px;cursor:pointer;'>");

			String name = assembly.getName();
			sb.append("<div class='label_title brui_card_head' style='word-break:break-word;white-space:pre-line;padding-bottom:16px;'>"
					+ name + "</div>");

			Check.isAssigned(assembly.getTitle(), e -> sb.append("<div>标题：" + e + "</div>"));
			Check.isAssigned(assembly.getDescription(), e -> sb.append("<div>描述：" + e + "</div>"));
			Check.isAssigned(assembly.getId(), e -> sb.append("<div>Id:" + e + "</div>"));
			Check.isAssigned(assembly.getTypeName(), e -> sb.append("<div>类型：" + e + "</div>"));

			sb.append("</div>");
			return sb.toString();
		} else {
			return "";
		}
	}

}
