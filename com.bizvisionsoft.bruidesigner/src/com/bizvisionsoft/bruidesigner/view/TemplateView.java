package com.bizvisionsoft.bruidesigner.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.bizvisionsoft.bruicommons.model.Template;
import com.bizvisionsoft.bruicommons.model.TemplateLib;
import com.bizvisionsoft.bruidesigner.model.ModelToolkit;
import com.bizvisionsoft.bruidesigner.model.SiteLoader;

public class TemplateView extends ViewPart implements PropertyChangeListener {

	public static final String ID = "com.bizvisionsoft.bruidesigner.view.TemplateView";
	private TableViewer viewer;

	public TemplateView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(ModelToolkit.createLabelProvider());
		TemplateLib templateLib = SiteLoader.site.getTemplateLib();
		if(templateLib==null) {
			templateLib = new TemplateLib();
			SiteLoader.site.setTemplateLib(templateLib);
		}
		List<Template> templates = templateLib.getTemplates();
		if (templates == null) {
			templates = new ArrayList<Template>();
			templateLib.setTemplates(templates);
		}
		viewer.setInput(templates);
		viewer.addDoubleClickListener(
				e -> ModelToolkit.openEditor(((IStructuredSelection) e.getSelection()).getFirstElement(), null));
		getSite().setSelectionProvider(viewer);

		templates.forEach(a -> a.addPropertyChangeListener("name", TemplateView.this));
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		viewer.update(event.getSource(), null);
	}

	public void createTemplate() {
		Template assy = ModelToolkit.createTemplate();
		assy.addPropertyChangeListener("name", this);
		viewer.add(assy);
	}

	public void removeTemplate(Template template) {
		template.removePropertyChangeListener("name", this);
		ModelToolkit.deleteTemplate(template);
		viewer.remove(template);
	}

}
