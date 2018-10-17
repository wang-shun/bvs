package com.bizvisionsoft.bruiengine.assembly.field;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Optional;

import org.bson.Document;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruiengine.assembly.EditorPart;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.service.tools.Check;
import com.mongodb.DBObject;

public abstract class EditorField {

	public Logger logger = LoggerFactory.getLogger(getClass());

	protected FormField fieldConfig;

	private Label titleLabel;

	protected Composite container;

	protected Object input;

	protected Assembly assemblyConfig;

	protected Locale locale;

	protected EditorPart editor;

	protected BruiAssemblyContext context;

	private boolean editorIsEditable;

	private boolean compact;

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
		if (editor != null) {
			editor.getContext().add(context = UserSession.newAssemblyContext().setParent(editor.getContext()));
		}

		locale = RWT.getLocale();
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		if (isRemoveBorder()) {
			layout.horizontalSpacing = compact ? 8 : 16;
		} else {
			layout.horizontalSpacing = 0;
		}
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		container.setLayout(layout);

		Optional.ofNullable(createTitleLabel(container)).ifPresent(l -> l.setLayoutData(getLabelLayoutData()));
		createControl(container).setLayoutData(getControlLayoutData());

		update();

		container.addListener(SWT.Dispose, e -> {
			dispose();
		});
		return container;
	}

	protected void dispose() {

	}

	public void update() {
		setValue(AUtil.readValue(input, assemblyConfig.getName(), fieldConfig.getName(), null));
	}

	public abstract void setValue(Object value);

	protected abstract Control createControl(Composite parent);

	protected Control createTitleLabel(Composite parent) {
		if (isRemoveBorder()) {
			titleLabel = new Label(parent, SWT.RIGHT);
		} else {
			titleLabel = new Label(parent, SWT.CENTER);
			titleLabel.setData(RWT.CUSTOM_VARIANT, "field");
		}

		UserSession.bruiToolkit().enableMarkup(titleLabel);

		String tooltips = fieldConfig.getTooltips();
		String fieldText = fieldConfig.getFieldText();
		if (fieldConfig.isRequired() && !fieldConfig.isReadOnly() && editorIsEditable) {
			fieldText = fieldText + "*";
		}
		String text;
		if (Check.isAssigned(tooltips)) {
			text = Layer.onClick(fieldText, tooltips);
			titleLabel.setCursor(parent.getDisplay().getSystemCursor(SWT.CURSOR_HAND));
		} else {
			text = fieldText;
		}

		titleLabel.setText(text);

		return titleLabel;
	}

	protected Object getControlLayoutData() {
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		return gd;
	}

	protected Object getLabelLayoutData() {
		if (isRemoveBorder()) {
			GridData gd = new GridData(SWT.RIGHT, SWT.FILL, false, false);
			gd.widthHint = compact ? 60 : 100;
			return gd;
		} else {
			GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false);
			gd.widthHint = 100;
			return gd;
		}
	}

	protected boolean isVertivalLayout() {
		return false;
	}

	protected boolean isRemoveBorder() {
		return assemblyConfig.isRemoveBorder();
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

		Object value = convertValueToWrite();

		AUtil.writeValue(input, assemblyConfig.getName(), fieldConfig.getName(), value);

		String[] fieldsTobeReload = Check.isAssignedThen(fieldConfig.getReloadFields(), f -> f.split("#")).orElse(null);

		if (fieldsTobeReload != null) 
			editor.reloadFieldValue(fieldsTobeReload);
	}

	private Object convertValueToWrite()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Object value = getValue();
		if (value != null) {
			// 获得取值的字段名，用于选择器处理返回对象类型时的具体返回值
			String vf = fieldConfig.getValueFieldName();
			if (Check.isAssigned(vf)) {
				Field field = value.getClass().getDeclaredField(vf);
				field.setAccessible(true);
				value = field.get(value);
			}

			// 处理Document input类型
			if (input instanceof Document) {
				value = decodeValue_Document(value);
			} else if (input instanceof DBObject) {
				value = decodeValue_DBObject(value);
			}
		}
		return value;
	}

	protected Object decodeValue_Document(Object value) {
		return value;
	}

	protected Object decodeValue_DBObject(Object value) {
		return value;
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

	public EditorField setCompact(boolean compact) {
		this.compact = compact;
		return this;
	}
}
