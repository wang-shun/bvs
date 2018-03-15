package com.bizvisionsoft.bruidesigner.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruidesigner.model.ModelToolkit;
import com.bizvisionsoft.bruidesigner.model.SiteLoader;

public class AssyLibView extends ViewPart implements PropertyChangeListener {

	public static final String ID = "com.bizvisionsoft.bruidesigner.view.AssyLibView";
	private TableViewer viewer;

	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setLabelProvider(ModelToolkit.createLabelProvider());
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		List<Assembly> assys = SiteLoader.site.getAssyLib().getAssys();
		viewer.setInput(assys);
		viewer.addDoubleClickListener(e -> ModelToolkit.openEditor(
				((IStructuredSelection) e.getSelection()).getFirstElement(), null));
		getSite().setSelectionProvider(viewer);

		assys.forEach(a -> a.addPropertyChangeListener("name", AssyLibView.this));
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void createAssembly(String type) {
		Assembly assy = ModelToolkit.createAssembly(type);
		assy.addPropertyChangeListener("name", this);
		viewer.add(assy);
	}

	public void removeAssembly(Assembly assy) {
		assy.removePropertyChangeListener("name", this);
		ModelToolkit.deleteAssembly(assy);
		viewer.remove(assy);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		viewer.update(event.getSource(), null);
	}
}
