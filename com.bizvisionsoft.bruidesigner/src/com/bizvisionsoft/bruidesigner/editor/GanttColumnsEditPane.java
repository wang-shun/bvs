package com.bizvisionsoft.bruidesigner.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruidesigner.Activator;
import com.bizvisionsoft.bruidesigner.model.ModelToolkit;

public class GanttColumnsEditPane extends Composite {

	class LayoutContentProvider implements ITreeContentProvider {

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

	private List<Column> columns;
	private ModelEditor editor;
	private PropertyChangeListener listener;
	private TreeViewer viewer;
	private Composite rightPane;
	private Column current;

	public GanttColumnsEditPane(Composite parent, List<Column> columns, ModelEditor editor) {
		super(parent, SWT.HORIZONTAL);
		this.columns = columns;
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
		toolbar.setLayout(new GridLayout(4, false));

		Button addColGrp = new Button(toolbar, SWT.PUSH);
		addColGrp.setText("添加");
		addColGrp.addListener(SWT.Selection, e -> {
			columns.add(ModelToolkit.createColumn());
			viewer.refresh();
		});

		Button remove = new Button(toolbar, SWT.PUSH);
		remove.setText("删除");
		remove.addListener(SWT.Selection, e -> {
			Object element = viewer.getStructuredSelection().getFirstElement();
			TreeItem itm = (TreeItem) viewer.testFindItem(element);
			TreeItem parentItem = itm.getParentItem();
			if (parentItem == null) {
				columns.remove(element);
			} else {
				Column parentColumn = (Column) parentItem.getData();
				parentColumn.getColumns().remove(element);
			}
			viewer.refresh();
		});

		Button moveUp = new Button(toolbar, SWT.PUSH);
		moveUp.setImage(Activator.getImageDescriptor("icons/up.png").createImage());
		moveUp.addListener(SWT.Selection, e -> {
			if (current == null)
				return;
			List<Column> acts = getChildrenOfParentColumn(current);
			int idx = acts.indexOf(current);
			if (idx == 0) {
				return;
			}
			acts.remove(idx);
			acts.add(idx - 1, current);

			viewer.refresh();
		});

		Button moveDown = new Button(toolbar, SWT.PUSH);
		moveDown.setImage(Activator.getImageDescriptor("icons/down.png").createImage());
		moveDown.addListener(SWT.Selection, e -> {
			if (current == null)
				return;
			List<Column> acts = getChildrenOfParentColumn(current);
			int idx = acts.indexOf(current);
			if (idx == acts.size() - 1) {
				return;
			}
			acts.remove(idx);
			acts.add(idx + 1, current);
			viewer.refresh();
		});

		viewer = new TreeViewer(left, SWT.FULL_SELECTION | SWT.BORDER);
		viewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		viewer.setContentProvider(new LayoutContentProvider());
		viewer.setLabelProvider(ModelToolkit.createLabelProvider());
		viewer.setInput(columns);
		viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.addPostSelectionChangedListener(e -> {
			Object element = e.getStructuredSelection().getFirstElement();
			openColumn((Column) element, rightPane);
		});
		return left;
	}

	public List<Column> getChildrenOfParentColumn(Column col) {
		return Optional.ofNullable(getParentColumn(col)).map(p -> p.getColumns()).orElse(columns);
	}

	public Column getParentColumn(Column col) {
		return Optional.ofNullable((TreeItem) viewer.testFindItem(col)).map(itm -> itm.getParentItem())
				.map(pi -> (Column) pi.getData()).orElse(null);
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

	private void openColumn(Column element, Composite parent) {
		Optional.ofNullable(current).ifPresent(a -> a.removePropertyChangeListener("name", listener));
		current = element;

		Arrays.asList(parent.getChildren()).stream().filter(c -> !c.isDisposed()).forEach(ctl -> ctl.dispose());

		if (element != null) {

			editor.createTextField(parent, "唯一标识符:", element, "id", SWT.READ_ONLY);

			editor.createTextField(parent, "列名称:", element, "name", SWT.BORDER);
			
			editor.createTextField(parent, "列头显示文本:", element, "text", SWT.BORDER);
			
			editor.createTextField(parent, "描述:", element, "description", SWT.BORDER);

			editor.createComboField(parent, new String[] { "左对齐", "居中", "右对齐" },
					new Integer[] { SWT.LEFT, SWT.CENTER, SWT.RIGHT }, "对齐方式：", element, "alignment",
					SWT.BORDER | SWT.READ_ONLY);

			editor.createIntegerField(parent, "列宽：", element, "width", SWT.BORDER, 0, 1000);

			editor.createCheckboxField(parent, "支持改变列宽：", element, "resizeable", SWT.CHECK);

			editor.createCheckboxField(parent, "隐藏：", element, "hide", SWT.CHECK);
			
			element.addPropertyChangeListener("name", listener);
			
			element.addPropertyChangeListener("text", listener);

		}

		parent.layout();

	}

}
