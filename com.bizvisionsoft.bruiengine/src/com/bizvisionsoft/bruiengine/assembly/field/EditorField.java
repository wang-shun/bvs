package com.bizvisionsoft.bruiengine.assembly.field;

import java.util.Locale;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruiengine.assembly.EditorPart;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;

public abstract class EditorField {

	protected FormField fieldConfig;

	private Label titleLabel;

	private Label infoLabel;

	protected Composite container;

	protected Object input;

	protected Assembly assemblyConfig;

	protected Locale locale;

	protected EditorPart editor;

	protected BruiAssemblyContext context;

	private boolean editorIsEditable;

	public EditorField setFieldConfig(FormField fieldConfig) {
		this.fieldConfig = fieldConfig;
		return this;
	}

	public FormField getFieldConfig() {
		return fieldConfig;
	}

	public EditorField setEditorConfig(Assembly config) {
		this.assemblyConfig = config;
		return this;
	}

	public Assembly getAssemblyConfig() {
		return assemblyConfig;
	}

	public Composite createUI(Composite parent) {
		editor.getContext().add(context = new BruiAssemblyContext().setParent(editor.getContext()));

		locale = RWT.getLocale();
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 16;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = fieldConfig.isHasInfoLabel() ? 3 : 2;
		container.setLayout(layout);

		createTitleLabel(container).setLayoutData(getLabelLayoutData());
		createControl(container).setLayoutData(getControlLayoutData());
		if (fieldConfig.isHasInfoLabel())
			createInfoLabel(container).setLayoutData(getInfoLayoutData());

		setValue(AUtil.readValue(input, assemblyConfig.getName(), fieldConfig.getName(), null));

		container.addListener(SWT.Dispose, e -> {
			dispose();
		});
		return container;
	}

	protected void dispose() {

	}

	public abstract void setValue(Object value);

	protected Control createInfoLabel(Composite parent) {
		infoLabel = new Label(parent, SWT.NONE);
		return infoLabel;
	}

	protected abstract Control createControl(Composite parent);

	protected Control createTitleLabel(Composite parent) {
		titleLabel = new Label(parent, SWT.RIGHT);
		titleLabel.setText(fieldConfig.getFieldText());
		return titleLabel;
	}

	protected Object getInfoLayoutData() {
		GridData gd = new GridData(SWT.FILL, SWT.TOP, true, false);
		gd.verticalIndent = 8;
		return gd;
	}

	protected Object getControlLayoutData() {
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		return gd;
	}

	protected Object getLabelLayoutData() {
		GridData gd = new GridData(SWT.RIGHT, SWT.TOP, false, false);
		gd.widthHint = 100;
		gd.verticalIndent = 8;
		return gd;
	}

	public EditorField setInput(Object input) {
		this.input = input;
		return this;
	}

	public void writeToInput(boolean save) throws Exception {
		if (save) {
			saveBefore();
		}
		check(save);
		AUtil.writeValue(input, assemblyConfig.getName(), fieldConfig.getName(), getValue());
	}

	protected void saveBefore() throws Exception {

	}

	protected abstract void check(boolean saveCheck) throws Exception;

	public abstract Object getValue();

	public EditorField setEditor(EditorPart editor) {
		this.editor = editor;
		return this;
	}

	public boolean isReadOnly() {
		return (!editorIsEditable) || fieldConfig.isReadOnly();
	}

	public EditorField setEditable(boolean editorIsEditable) {
		this.editorIsEditable = editorIsEditable;
		return this;
	}
	
	public Composite getContainer() {
		return container;
	}
}
