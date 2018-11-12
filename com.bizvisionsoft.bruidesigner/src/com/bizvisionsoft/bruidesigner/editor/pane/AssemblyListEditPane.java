package com.bizvisionsoft.bruidesigner.editor.pane;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruidesigner.Activator;
import com.bizvisionsoft.bruidesigner.dialog.AssemblySelectionDialog;
import com.bizvisionsoft.bruidesigner.model.ModelToolkit;

public class AssemblyListEditPane extends Composite {

	private TableViewer viewer;
	private List<String> assemblies;

	public AssemblyListEditPane(Composite parent, List<String> assemblies) {
		super(parent, SWT.NONE);
		this.assemblies = assemblies;
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		setLayoutData(layoutData);

		setLayout(new GridLayout(2, false));

		Button add = new Button(this, SWT.PUSH);
		add.setText("添加候选组件");
		add.addListener(SWT.Selection, e -> addAssemblies());

		Button remove = new Button(this, SWT.PUSH);
		remove.setImage(Activator.getImageDescriptor("icons/delete.gif").createImage());
		remove.addListener(SWT.Selection, e -> removeSelection());

		viewer = new TableViewer(this, SWT.FULL_SELECTION | SWT.BORDER | SWT.MULTI);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				return Optional.ofNullable(ModelToolkit.getAssembly((String) element)).map(a -> a.toString()).orElse(element + "[已丢失]");
			};
		});
		viewer.setInput(assemblies);
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	}

	private void addAssemblies() {
		AssemblySelectionDialog dialog = new AssemblySelectionDialog(getShell(), true);
		if (AssemblySelectionDialog.OK == dialog.open()) {
			Arrays.asList(dialog.getResult()).forEach(a -> {
				assemblies.add(((Assembly) a).getId());
			});
			viewer.refresh();
		}
	}

	private void removeSelection() {
		Iterator<?> iter = viewer.getStructuredSelection().iterator();
		while (iter.hasNext()) {
			Object id = iter.next();
			assemblies.remove(id);
			viewer.remove(id);
		}
	}

}
