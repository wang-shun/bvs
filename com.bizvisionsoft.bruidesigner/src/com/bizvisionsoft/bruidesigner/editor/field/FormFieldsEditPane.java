package com.bizvisionsoft.bruidesigner.editor.field;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
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

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.Activator;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;
import com.bizvisionsoft.bruidesigner.model.ModelToolkit;

public class FormFieldsEditPane extends Composite {

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
			return ((List<FormField>) inputElement).toArray();
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof FormField) {
				List<FormField> children = ((FormField) parentElement).getFormFields();
				if (children != null)
					return children.toArray(new FormField[0]);
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

	private List<FormField> FormFields;
	private ModelEditor editor;
	private PropertyChangeListener listener;
	private TreeViewer viewer;
	private Composite rightPane;
	private FormField current;
	private String type;

	public FormFieldsEditPane(Composite parent, List<FormField> FormFields, ModelEditor editor, String type) {
		super(parent, SWT.HORIZONTAL);
		this.FormFields = FormFields;
		this.editor = editor;
		this.type = type;
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
		toolbar.setLayout(new GridLayout(8, false));

		Button addColGrp = new Button(toolbar, SWT.PUSH);
		addColGrp.setText("Ìí¼Ó×Ö¶Î");
		addColGrp.addListener(SWT.Selection, e -> {
			FormFields.add(ModelToolkit.createField());
			viewer.refresh();
		});

		Button addCol = new Button(toolbar, SWT.PUSH);
		addCol.setText("Ìí¼Ó×Ó×Ö¶Î");
		addCol.addListener(SWT.Selection, e -> {
			Object element = viewer.getStructuredSelection().getFirstElement();
			if (element instanceof FormField) {
				List<FormField> cols = ((FormField) element).getFormFields();
				if (cols == null)
					((FormField) element).setFormFields(cols = new ArrayList<FormField>());
				cols.add(ModelToolkit.createField());
				viewer.refresh(element);
			}
		});

		Button remove = new Button(toolbar, SWT.PUSH);
		remove.setText("É¾³ý");
		remove.addListener(SWT.Selection, e -> {
			Object element = viewer.getStructuredSelection().getFirstElement();
			TreeItem itm = (TreeItem) viewer.testFindItem(element);
			TreeItem parentItem = itm.getParentItem();
			if (parentItem == null) {
				FormFields.remove(element);
			} else {
				FormField parentFormField = (FormField) parentItem.getData();
				parentFormField.getFormFields().remove(element);
			}
			viewer.refresh();
		});

		Button moveUp = new Button(toolbar, SWT.PUSH);
		moveUp.setImage(Activator.getImageDescriptor("icons/up.png").createImage());
		moveUp.addListener(SWT.Selection, e -> {
			if (current == null)
				return;
			List<FormField> acts = getChildrenOfParentFormField(current);
			int idx = acts.indexOf(current);
			if (idx == 0) {
				return;
			}
			acts.remove(idx);
			acts.add(idx - 1, current);

			viewer.refresh();
		});
		Button moveRight = new Button(toolbar, SWT.PUSH);
		moveRight.setImage(Activator.getImageDescriptor("icons/right.png").createImage());
		moveRight.addListener(SWT.Selection, e -> {
			if (current == null)
				return;
			// »ñµÃÉÏÐÖµÜ
			List<FormField> acts = getChildrenOfParentFormField(current);
			int idx = acts.indexOf(current);
			if (idx == 0) {
				return;
			}
			FormField upBro = acts.get(idx - 1);
			// ÒÆ³ýcurrent
			acts.remove(idx);
			List<FormField> children = upBro.getFormFields();
			if (children == null) {
				children = new ArrayList<FormField>();
				upBro.setFormFields(children);
			}
			children.add(current);

			viewer.refresh();
		});

		Button moveDown = new Button(toolbar, SWT.PUSH);
		moveDown.setImage(Activator.getImageDescriptor("icons/down.png").createImage());
		moveDown.addListener(SWT.Selection, e -> {
			if (current == null)
				return;
			List<FormField> acts = getChildrenOfParentFormField(current);
			int idx = acts.indexOf(current);
			if (idx == acts.size() - 1) {
				return;
			}
			acts.remove(idx);
			acts.add(idx + 1, current);
			viewer.refresh();
		});

		Button moveLeft = new Button(toolbar, SWT.PUSH);
		moveLeft.setImage(Activator.getImageDescriptor("icons/left.png").createImage());
		moveLeft.addListener(SWT.Selection, e -> {
			if (current == null)
				return;
			FormField parentAction = getParentFormField(current);
			if (parentAction == null) {
				return;
			}
			List<FormField> acts = getChildrenOfParentFormField(current);
			acts.remove(current);

			acts = getChildrenOfParentFormField(parentAction);
			int idx = acts.indexOf(parentAction);
			acts.add(idx + 1, current);

			viewer.refresh();
		});

		viewer = new TreeViewer(left, SWT.FULL_SELECTION | SWT.BORDER);
		viewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		viewer.setContentProvider(new LayoutContentProvider());
		viewer.setLabelProvider(ModelToolkit.createLabelProvider());
		viewer.setInput(FormFields);
		viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.addPostSelectionChangedListener(e -> {
			Object element = e.getStructuredSelection().getFirstElement();
			openFormField((FormField) element, rightPane);
		});
		return left;
	}

	public List<FormField> getChildrenOfParentFormField(FormField col) {
		return Optional.ofNullable(getParentFormField(col)).map(p -> p.getFormFields()).orElse(FormFields);
	}

	public FormField getParentFormField(FormField col) {
		return Optional.ofNullable((TreeItem) viewer.testFindItem(col)).map(itm -> itm.getParentItem())
				.map(pi -> (FormField) pi.getData()).orElse(null);
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

	private void openFormField(FormField element, Composite parent) {
		Optional.ofNullable(current).ifPresent(a -> a.removePropertyChangeListener("name", listener));
		current = element;

		Arrays.asList(parent.getChildren()).stream().filter(c -> !c.isDisposed()).forEach(ctl -> ctl.dispose());

		if (element != null) {
			String fieldType = element.getType();
			if (FormField.TYPE_INLINE.equals(fieldType)) {
				new FormFieldEmptyTypePane(element, editor, parent, type);
				
			} else if (FormField.TYPE_PAGE.equals(fieldType)) {
				new FormFieldPageTypePane(element, editor, parent, type);

			} else if (FormField.TYPE_TEXT.equals(fieldType)) {
				new FormFieldTextTypePane(element, editor, parent, type);
			
			} else if (FormField.TYPE_TEXT_MULTILINE.equals(fieldType)) {
				new FormFieldTextMultiLineTypePane(element, editor, parent, type);
			
			} else if (FormField.TYPE_TEXT_RANGE.equals(fieldType)) {
				new FormFieldTextRangeTypePane(element, editor, parent, type);
			
			} else if (FormField.TYPE_COMBO.equals(fieldType)) {
				new FormFieldComboTypePane(element, editor, parent, type);
			
			} else if (FormField.TYPE_RADIO.equals(fieldType)) {
				new FormFieldRadioTypePane(element, editor, parent, type);
			
			} else if (FormField.TYPE_CHECK.equals(fieldType)) {
				new FormFieldCheckTypePane(element, editor, parent, type);
			
			} else if (FormField.TYPE_MULTI_CHECK.equals(fieldType)) {
				new FormFieldMultiCheckTypePane(element, editor, parent, type);
			
			} else if (FormField.TYPE_DATETIME.equals(fieldType)) {
				new FormFieldDateTimeTypePane(element, editor, parent, type);
			
			} else if (FormField.TYPE_DATETIME_RANGE.equals(fieldType)) {
				new FormFieldDateTimeRangeTypePane(element, editor, parent, type);
			
			} else if (FormField.TYPE_SELECTION.equals(fieldType)) {
				new FormFieldSelectionTypePane(element, editor, parent, type);
			
			} else if (FormField.TYPE_MULTI_SELECTION.equals(fieldType)) {
				new FormFieldMultiSelectionTypePane(element, editor, parent, type);
			
			} else if (FormField.TYPE_FILE.equals(fieldType)) {
				new FormFieldFileTypePane(element, editor, parent, type);
			
			} else if (FormField.TYPE_MULTI_FILE.equals(fieldType)) {
				new FormFieldMultiFileTypePane(element, editor, parent, type);
			
			} else if (FormField.TYPE_QUERY_TEXT.equals(fieldType)) {
				new FormFieldQueryCheckTypePane(element, editor, parent, type);
			
			} else if (FormField.TYPE_QUERY_CHECK.equals(fieldType)) {
				new FormFieldQueryCheckTypePane(element, editor, parent, type);
			
			} else if (FormField.TYPE_QUERY_MULTI_CHECK.equals(fieldType)) {
				new FormFieldQueryMultiCheckTypePane(element, editor, parent, type);
			
			} else if (FormField.TYPE_QUERY_MULTI_SELECTION.equals(fieldType)) {
				new FormFieldQueryMultiSelectionTypePane(element, editor, parent, type);
			
			}

			element.addPropertyChangeListener("name", listener);

			element.addPropertyChangeListener("type", listener);
		}

		parent.layout();

	}

}
