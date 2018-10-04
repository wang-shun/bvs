package com.bizvisionsoft.bruiengine.assembly;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.htmlparser.Htmlparser;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.util.BruiToolkit;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.service.tools.Check;

public class Customizer extends Dialog {

	class ColumnsContentProvider implements ITreeContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object[] getElements(Object inputElement) {
			return ((List<Column>) inputElement).toArray();
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Column) {
				List<Column> children = ((Column) parentElement).getColumns();
				if (children != null)
					return children.toArray(new Column[0]);
			}
			return new Object[0];
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}

	}

	private static final int HEIGHT = 640;
	private static final int WIDTH = 480;
	private Assembly config;
	private List<Column> stored;

	protected Customizer(Shell parentShell) {
		super(parentShell);
	}

	public static List<Column> open(Assembly config, List<Column> stored) {
		Customizer cust = new Customizer(Display.getCurrent().getActiveShell());
		cust.config = config;
		cust.stored = stored;
		cust.open();
		return null;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		String title = config.getStickerTitle();
		title = Check.isAssigned(title) ? title : config.getTitle();
		title = Check.isAssigned(title) ? title : config.getName();
		newShell.setText("设置: " + title);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if ("su".equals(UserSession.current().getUser().getUserId())) {
			createButton(parent, IDialogConstants.CLIENT_ID, "保存到站点", false).setData(RWT.CUSTOM_VARIANT,
					BruiToolkit.CSS_SERIOUS);
		}

		createButton(parent, IDialogConstants.DETAILS_ID, "恢复默认", false).setData(RWT.CUSTOM_VARIANT,
				BruiToolkit.CSS_INFO);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false).setData(RWT.CUSTOM_VARIANT,
				BruiToolkit.CSS_WARNING);
		createButton(parent, IDialogConstants.OK_ID, "确定", true).setData(RWT.CUSTOM_VARIANT, BruiToolkit.CSS_NORMAL);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 24;
		layout.marginHeight = 24;
		panel.setLayout(layout);
		createLeftPanel(panel);
		createButtons(panel);
		createRigthPanel(panel);
		return panel;
	}

	private void createRigthPanel(Composite parent) {
		TreeViewer viewer = new TreeViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		viewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		viewer.getControl().setLayoutData(new GridData(WIDTH, HEIGHT));
		viewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return getColumnLabel((Column) element);
			}
		});
		viewer.setContentProvider(new ColumnsContentProvider());
		viewer.setInput(stored == null ? config.getColumns() : stored);
	}

	private void createButtons(Composite parent) {
		Composite bar = new Composite(parent, SWT.NONE);
		bar.setLayoutData(new GridData(64, HEIGHT));
	}

	private void createLeftPanel(Composite parent) {
		TreeViewer viewer = new TreeViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		viewer.getControl().setLayoutData(new GridData(WIDTH, HEIGHT));
		viewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return getColumnLabel((Column) element);
			}
		});
		viewer.setContentProvider(new ColumnsContentProvider());
		viewer.setInput(getColumnNodes(config.getColumns()));
	}

	private String getColumnLabel(Column col) {
		String text = col.getName() + Check.isAssignedThen(col.getText(), t -> " [" + t + "]").orElse("");
		try {
			return Htmlparser.parser(text);
		} catch (Exception e) {
			return text;
		}
	}

	private List<Column> getColumnNodes(List<Column> columns) {
		List<Column> result = new ArrayList<>();
		columns.forEach(c -> {
			List<Column> cs = c.getColumns();
			if (Check.isNotAssigned(cs)) {
				result.add(c);
			} else {
				result.addAll(getColumnNodes(cs));
			}
		});
		return result;
	}

}
