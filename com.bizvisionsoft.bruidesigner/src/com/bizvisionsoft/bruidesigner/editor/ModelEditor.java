package com.bizvisionsoft.bruidesigner.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruidesigner.Activator;
import com.bizvisionsoft.bruidesigner.dialog.AssemblySelectionDialog;
import com.bizvisionsoft.bruidesigner.dialog.ResourceSelectionDialog;
import com.bizvisionsoft.bruidesigner.model.ModelToolkit;

public abstract class ModelEditor extends EditorPart {

	protected DataBindingContext bindingContext;
	protected Realm realm;
	private PropertyChangeListener partNameChangeListener;
	private String[] partNameRelatedProperties;
	protected ModelObject inputData;
	private CTabFolder folder;

	protected void addPartNamePropertyChangeListener(String... properties) {
		partNameChangeListener = new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				setPartName(getEditorInput().getName());
			}
		};

		for (int i = 0; i < properties.length; i++) {
			inputData.addPropertyChangeListener(properties[i], partNameChangeListener);
		}
		partNameRelatedProperties = properties;
	}

	@Override
	public void dispose() {
		if (partNameRelatedProperties != null)
			for (int i = 0; i < partNameRelatedProperties.length; i++) {
				inputData.removePropertyChangeListener(partNameRelatedProperties[i], partNameChangeListener);
			}
		super.dispose();
	}

	protected abstract Class<? extends ModelObject> getDataType();

	@Override
	public void createPartControl(Composite parent) {
		preparingData();

		folder = new CTabFolder(parent, SWT.BOTTOM);

		createContent();

		folder.setSelection(0);
	}

	protected Composite createTabItemContent(String itemTitle) {
		CTabItem item = new CTabItem(folder, SWT.NONE);
		item.setText(itemTitle);
		final ScrolledComposite sc = new ScrolledComposite(folder, SWT.V_SCROLL);
		Composite content = new Composite(sc, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		setuplayout(layout);
		content.setLayout(layout);

		sc.setContent(content);
		sc.setExpandVertical(true);
		sc.setExpandHorizontal(true);
		sc.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				sc.setMinSize(sc.getContent().computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
		item.setControl(sc);
		return content;
	}

	protected void setuplayout(GridLayout layout) {
		layout.marginTop = 16;
		layout.marginBottom = 16;
		layout.marginLeft = 128;
		layout.marginRight = 128;
		layout.horizontalSpacing = 16;
		layout.verticalSpacing = 16;
	}

	private void preparingData() {
		inputData = getEditorInput().getAdapter(getDataType());

		realm = SWTObservables.getRealm(Display.getCurrent());
		bindingContext = new DataBindingContext(realm);
	}

	@Override
	public void setFocus() {
		folder.setFocus();
	}

	protected abstract void createContent();

	protected Text createPathField(Composite parent, String labelText, Object bean, String property, int style) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);
		layoutLabel(label);

		Text text = new Text(parent, style);
		layoutControl(style, text, 1);

		@SuppressWarnings("unchecked")
		IObservableValue<String> observe = BeanProperties.value(bean.getClass(), property).observe(realm, bean);
		bindingContext.bindValue(SWTObservables.observeText(text, SWT.Modify), observe, null, null);

		Button button = new Button(parent, SWT.PUSH);
		button.setText("选择资源..");
		button.addListener(SWT.Selection, event -> {
			ResourceSelectionDialog rsd = new ResourceSelectionDialog(parent.getShell());
			if (ResourceSelectionDialog.OK == rsd.open()) {
				Object[] result = rsd.getResult();
				if (result != null && result.length > 0) {
					String path = ((File) result[0]).getPath();
					int l = new File(Activator.siteFile.getParent() + "/res").getPath().length();
					path = path.substring(l, path.length());
					path = path.replaceAll("\\\\", "/");
					text.setText(path);
				}
			}
		});

		return text;
	}

	protected Text createAssemblyField(Composite parent, String labelText, Object bean, String property,
			boolean editable) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);
		layoutLabel(label);

		int style = SWT.READ_ONLY | SWT.MULTI;
		Text text = new Text(parent, style);
		GridData layoutData;
		layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		if ((style & SWT.MULTI) != 0) {
			layoutData.heightHint = 60;
		}
		text.setLayoutData(layoutData);
		@SuppressWarnings("unchecked")
		IObservableValue<String> modelValue = BeanProperties.value(bean.getClass(), property).observe(realm, bean);

		IValueChangeListener<String> listener = new IValueChangeListener<String>() {
			@Override
			public void handleValueChange(ValueChangeEvent<? extends String> e) {
				if (!text.isDisposed())
					setTextByAssemblyId(text, e.getObservableValue().getValue());
			}
		};

		modelValue.addValueChangeListener(listener);

		// Assembly assembly =
		setTextByAssemblyId(text, modelValue.getValue());
		// PropertyChangeListener listener2 = new PropertyChangeListener() {
		// @Override
		// public void propertyChange(PropertyChangeEvent arg0) {
		// if (!text.isDisposed())
		// text.setText("唯一标识符:" + assembly.getId() + "，名称:" + assembly.getName());
		// }
		//
		// };
		// assembly.addPropertyChangeListener("name", listener2);

		text.addDisposeListener(e -> {
			modelValue.removeValueChangeListener(listener);
			// assembly.removePropertyChangeListener("name", listener2);
		});

		Button button = new Button(parent, SWT.PUSH);
		button.setText("选择组件..");
		button.addListener(SWT.Selection, event -> {
			AssemblySelectionDialog dialog = new AssemblySelectionDialog(parent.getShell(), false);
			if (AssemblySelectionDialog.OK == dialog.open()) {
				Object[] result = dialog.getResult();
				if (result != null && result.length > 0) {
					modelValue.setValue(((Assembly) result[0]).getId());
				}
			}
		});
		button.setEnabled(editable);

		return text;
	}

	private Assembly setTextByAssemblyId(Text text, String value) {
		Assembly assembly = ModelToolkit.getAssembly(value);
		if (assembly != null) {
			text.setText(assembly.getName() + "[" + assembly.getId() + "]\n插件:" + assembly.getBundleId() + "\n类路径:"
					+ assembly.getClassName());
		}
		return assembly;
	}

	protected Text createTextField(Composite parent, String labelText, Object bean, String property, int style) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);
		layoutLabel(label);

		Text text = new Text(parent, style);
		@SuppressWarnings("unchecked")
		IObservableValue<String> observe = BeanProperties.value(bean.getClass(), property).observe(realm, bean);
		bindingContext.bindValue(SWTObservables.observeText(text, SWT.FocusOut), observe, null, null);
		layoutControl(style, text, 2);
		return text;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Spinner createIntegerField(Composite parent, String labelText, Object bean, String property, int style,
			int min, int max) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);
		layoutLabel(label);

		Spinner control = new Spinner(parent, style);
		control.setMaximum(max);
		control.setMinimum(min);
		IObservableValue observe = BeanProperties.value(bean.getClass(), property).observe(realm, bean);
		bindingContext.bindValue(SWTObservables.observeSelection(control), observe, null, null);
		layoutControl(style, control, 2);
		return control;
	}

	protected void layoutControl(int style, Control text, int space) {
		GridData layoutData;
		layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false, space, 1);
		if ((style & SWT.MULTI) != 0) {
			layoutData.heightHint = 120;
		}
		text.setLayoutData(layoutData);
	}

	protected Button createCheckboxField(Composite parent, String labelText, Object bean, String property, int style) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);
		layoutLabel(label);

		Button button = new Button(parent, style);
		layoutControl(style, button, 2);
		GridData layoutData = (GridData) button.getLayoutData();
		layoutData.verticalIndent = 4;

		@SuppressWarnings("unchecked")
		IObservableValue<String> observe = BeanProperties.value(bean.getClass(), property).observe(realm, bean);
		bindingContext.bindValue(SWTObservables.observeSelection(button), observe, null, null);
		return button;
	}

	protected void layoutLabel(Label label) {
		GridData layoutData = new GridData(SWT.RIGHT, SWT.TOP, false, false);
		layoutData.verticalIndent = 6;
		label.setLayoutData(layoutData);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Combo createComboField(Composite parent, String[] labels, Object[] values, String labelText, Object bean,
			String property, int style) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);
		layoutLabel(label);

		ComboViewer viewer = new ComboViewer(parent, style);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				for (int i = 0; i < values.length; i++) {
					if (values[i] == element) {
						return labels[i];
					}
				}
				return super.getText(element);
			}
		});
		viewer.setInput(values);
		layoutControl(style, viewer.getControl(), 2);

		IObservableValue modelObserve = BeanProperties.value(bean.getClass(), property).observe(realm, bean);
		IObservableValue targetObserve = ViewersObservables.observeSingleSelection(viewer);
		bindingContext.bindValue(targetObserve, modelObserve, null, null);
		return viewer.getCombo();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

}
