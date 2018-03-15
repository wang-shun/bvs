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

import com.bizvisionsoft.bruicommons.model.DataSource;
import com.bizvisionsoft.bruidesigner.model.ModelToolkit;
import com.bizvisionsoft.bruidesigner.model.SiteLoader;

public class DataSourcesView extends ViewPart implements PropertyChangeListener {

	public static final String ID = "com.bizvisionsoft.bruidesigner.view.DataSourcesView";
	private TableViewer viewer;

	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setLabelProvider(ModelToolkit.createLabelProvider());
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		List<DataSource> dataSources = SiteLoader.site.getDataSources();
		if (dataSources == null) {
			dataSources = new ArrayList<DataSource>();
			SiteLoader.site.setDataSources(dataSources);
		}
		viewer.setInput(dataSources);
		viewer.addDoubleClickListener(
				e -> ModelToolkit.openEditor(((IStructuredSelection) e.getSelection()).getFirstElement(), null));
		getSite().setSelectionProvider(viewer);

		dataSources.forEach(a -> a.addPropertyChangeListener("name", DataSourcesView.this));
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void createDataSource() {
		DataSource ds = ModelToolkit.createDataSource();
		ds.addPropertyChangeListener("name", this);
		viewer.add(ds);
	}

	public void removeDataSource(DataSource ds) {
		ds.removePropertyChangeListener("name", this);
		ModelToolkit.deleteDataSource(ds);
		viewer.remove(ds);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		viewer.update(event.getSource(), null);
	}
}
