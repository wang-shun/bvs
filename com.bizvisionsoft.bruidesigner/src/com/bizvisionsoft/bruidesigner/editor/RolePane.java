package com.bizvisionsoft.bruidesigner.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.Role;
import com.bizvisionsoft.bruidesigner.model.ModelToolkit;

public class RolePane extends Composite {

	private List<Role> roles;
	private ModelEditor editor;
	private PropertyChangeListener listener;
	private TableViewer viewer;
	private Composite rightPane;
	private Role current;

	public RolePane(Composite parent, List<Role> roles, ModelEditor editor) {
		super(parent, SWT.HORIZONTAL);
		this.roles = roles;
		this.editor = editor;
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		setLayoutData(layoutData);

		setLayout(new GridLayout(2, false));

		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, true);
		gd.widthHint = 500;
		createLeftPane().setLayoutData(gd);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		createRightPane().setLayoutData(gd);

		listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				viewer.update(e.getSource(), null);
			}
		};

		addDisposeListener(
				e -> Optional.ofNullable(current).ifPresent(a -> a.removePropertyChangeListener("name", listener)));
	}

	private Composite createLeftPane() {
		Composite left = new Composite(this, SWT.NONE);
		left.setLayout(new GridLayout());

		Composite toolbar = new Composite(left, SWT.NONE);
		toolbar.setLayout(new GridLayout(7, false));

		Button addColGrp = new Button(toolbar, SWT.PUSH);
		addColGrp.setText("添加角色");
		addColGrp.addListener(SWT.Selection, e -> {
			roles.add(ModelToolkit.createRole());
			viewer.refresh();
		});

		Button remove = new Button(toolbar, SWT.PUSH);
		remove.setText("删除");
		remove.addListener(SWT.Selection, e -> {
			Object element = viewer.getStructuredSelection().getFirstElement();
			roles.remove(element);
			viewer.refresh();
		});

		viewer = new TableViewer(left, SWT.FULL_SELECTION | SWT.BORDER);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(ModelToolkit.createLabelProvider());
		viewer.setInput(roles);
		viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.addPostSelectionChangedListener(e -> {
			Object element = e.getStructuredSelection().getFirstElement();
			openRole((Role) element, rightPane);
		});
		return left;
	}

	private Composite createRightPane() {
		rightPane = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.marginTop = 16;
		layout.marginBottom = 16;
		layout.marginLeft = 16;
		layout.marginRight = 16;
		layout.horizontalSpacing = 16;
		layout.verticalSpacing = 16;
		rightPane.setLayout(layout);
		return rightPane;
	}

	private void openRole(Role element, Composite parent) {
		Optional.ofNullable(current).ifPresent(a -> a.removePropertyChangeListener("name", listener));
		current = element;

		Arrays.asList(parent.getChildren()).stream().filter(c -> !c.isDisposed()).forEach(ctl -> ctl.dispose());

		if (element != null) {

			editor.createTextField(parent, "唯一标识符:", element, "id", SWT.READ_ONLY);

			editor.createTextField(parent, "角色名:", element, "name", SWT.BORDER);

			editor.createTextField(parent, "角色文本:", element, "text", SWT.BORDER);

			editor.createTextField(parent, "描述:", element, "description", SWT.BORDER|SWT.MULTI|SWT.WRAP);

			element.addPropertyChangeListener("name", listener);

			element.addPropertyChangeListener("text", listener);

		}

		parent.layout();

	}

}
