package com.bizvisionsoft.bruiengine.assembly;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.bizvisionsoft.bruicommons.annotation.CreateUI;
import com.bizvisionsoft.bruicommons.annotation.GetReturnCode;
import com.bizvisionsoft.bruicommons.annotation.GetReturnResult;
import com.bizvisionsoft.bruicommons.annotation.Inject;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.BruiAssemblyEngine;
import com.bizvisionsoft.bruiengine.assembly.field.CheckField;
import com.bizvisionsoft.bruiengine.assembly.field.CheckQueryField;
import com.bizvisionsoft.bruiengine.assembly.field.ComboField;
import com.bizvisionsoft.bruiengine.assembly.field.DateTimeField;
import com.bizvisionsoft.bruiengine.assembly.field.DateTimeQueryField;
import com.bizvisionsoft.bruiengine.assembly.field.EditorField;
import com.bizvisionsoft.bruiengine.assembly.field.FileField;
import com.bizvisionsoft.bruiengine.assembly.field.InLineWrapper;
import com.bizvisionsoft.bruiengine.assembly.field.MultiCheckField;
import com.bizvisionsoft.bruiengine.assembly.field.MultiCheckQueryField;
import com.bizvisionsoft.bruiengine.assembly.field.MultiFileField;
import com.bizvisionsoft.bruiengine.assembly.field.MultiSelectionField;
import com.bizvisionsoft.bruiengine.assembly.field.MultiSelectionQueryField;
import com.bizvisionsoft.bruiengine.assembly.field.NumberRangeField;
import com.bizvisionsoft.bruiengine.assembly.field.NumberRangeQueryField;
import com.bizvisionsoft.bruiengine.assembly.field.RadioField;
import com.bizvisionsoft.bruiengine.assembly.field.SelectionField;
import com.bizvisionsoft.bruiengine.assembly.field.TextAreaField;
import com.bizvisionsoft.bruiengine.assembly.field.TextField;
import com.bizvisionsoft.bruiengine.assembly.field.TextQueryField;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiEditorContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.bizvisionsoft.bruiengine.util.BruiToolkit;
import com.bizvisionsoft.bruiengine.util.Util;
import com.mongodb.BasicDBObject;

public class DataEditor {

	private Assembly config;

	@Inject
	private IBruiService bruiService;

	@Inject
	private IBruiEditorContext context;

	private TabFolder folder;

	private Object input;

	private Map<FormField, EditorField> fields;

	@GetReturnResult
	private BasicDBObject result;

	@GetReturnCode
	private int returnCode = Window.CANCEL;

	private Composite container;

	private Composite contentArea;

	private BruiAssemblyContext containerContext;

	private String title;

	private boolean editable;

	private boolean ignoreNull;

	private boolean wrapList;

	public DataEditor(Assembly assembly) {
		this.config = assembly;
		fields = new HashMap<FormField, EditorField>();
	}

	@CreateUI
	private void createUI(Composite parent) {
		title = parent.getShell().getText();

		this.contentArea = parent;
		input = context.getInput();
		ignoreNull = context.isIgnoreNull();
		wrapList = context.isWrapList();
		editable = context.isEditable();

		FormLayout layout = new FormLayout();
		layout.spacing = 16;
		layout.marginWidth = 16;
		layout.marginHeight = 16;

		parent.setLayout(layout);
		folder = new TabFolder(parent, SWT.TOP | SWT.BORDER);
		List<FormField> fields = config.getFields();
		// 判断是否分标签页
		if (FormField.TYPE_PAGE.equals(fields.get(0).getType())) {
			fields.forEach(f -> {
				Composite content = createTabItem(f.getFieldText());
				createFields(content, f.getFormFields());
			});
		}

		Button okBtn = UserSession.bruiToolkit().newStyledControl(Button.class, parent, SWT.PUSH,
				BruiToolkit.CSS_NORMAL);
		okBtn.setText("确定");
		okBtn.addListener(SWT.Selection, e -> {
			try {
				if (editable) {
					save();
					setReturnCode(Window.OK);
				}
				bruiService.closeCurrentPart();
			} catch (Exception e1) {
				MessageDialog.openError(bruiService.getCurrentShell(), "错误", e1.getMessage());
			}
		});

		FormData fd = new FormData();
		okBtn.setLayoutData(fd);
		fd.height = 32;
		fd.width = 120;
		fd.bottom = new FormAttachment(100);
		fd.right = new FormAttachment(100);

		fd = new FormData();
		folder.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(okBtn, -32);

		if (editable) {
			Button cancelBtn = UserSession.bruiToolkit().newStyledControl(Button.class, parent, SWT.PUSH,
					BruiToolkit.CSS_WARNING);
			cancelBtn.setText("取消");
			cancelBtn.addListener(SWT.Selection, e -> {
				setReturnCode(Window.CANCEL);
				bruiService.closeCurrentPart();
			});

			fd = new FormData();
			cancelBtn.setLayoutData(fd);
			fd.height = 32;
			fd.width = 120;
			fd.bottom = new FormAttachment(100);
			fd.right = new FormAttachment(okBtn);

		}

	}

	private void save() throws Exception {
		Collection<EditorField> fs = fields.values();
		for (Iterator<EditorField> iterator = fs.iterator(); iterator.hasNext();) {
			iterator.next().writeToInput(true);
		}

		result = Util.getBson(input, ignoreNull,wrapList);
	}

	private void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	private void createFields(Composite parent, List<FormField> formFields) {
		formFields.forEach(f -> {
			String type = f.getType();
			if (FormField.TYPE_INLINE.equals(type)) {
				Composite container = new InLineWrapper().getContainer(parent, f.getFormFields().size());
				container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
				createFields(container, f.getFormFields());
			} else {
				EditorField fieldPart = null;
				if (FormField.TYPE_TEXT.equals(type)) {
					fieldPart = new TextField();
				} else if (FormField.TYPE_COMBO.equals(type)) {
					fieldPart = new ComboField();
				} else if (FormField.TYPE_RADIO.equals(type)) {
					fieldPart = new RadioField();
				} else if (FormField.TYPE_CHECK.equals(type)) {
					fieldPart = new CheckField();
				} else if (FormField.TYPE_DATETIME.equals(type)) {
					fieldPart = new DateTimeField();
				} else if (FormField.TYPE_SELECTION.equals(type)) {
					fieldPart = new SelectionField();
				} else if (FormField.TYPE_MULTI_SELECTION.equals(type)) {
					fieldPart = new MultiSelectionField();
				} else if (FormField.TYPE_FILE.equals(type)) {
					fieldPart = new FileField();
				} else if (FormField.TYPE_MULTI_FILE.equals(type)) {
					fieldPart = new MultiFileField();
				} else if (FormField.TYPE_TEXT_MULTILINE.equals(type)) {
					fieldPart = new TextAreaField();
				} else if (FormField.TYPE_MULTI_CHECK.equals(type)) {
					fieldPart = new MultiCheckField();
				} else if (FormField.TYPE_TEXT_RANGE.equals(type)) {
					fieldPart = new NumberRangeField();
				} else if (FormField.TYPE_QUERY_DATETIME_RANGE.equals(type)) {// 查询专用
					fieldPart = new DateTimeQueryField();
				} else if (FormField.TYPE_QUERY_TEXT.equals(type)) {// 查询专用
					fieldPart = new TextQueryField();
				} else if (FormField.TYPE_QUERY_TEXT_RANGE.equals(type)) {// 查询专用
					fieldPart = new NumberRangeQueryField();
				} else if (FormField.TYPE_QUERY_CHECK.equals(type)) {// 查询专用
					fieldPart = new CheckQueryField();
				} else if (FormField.TYPE_QUERY_MULTI_CHECK.equals(type)) {// 查询专用
					fieldPart = new MultiCheckQueryField();
				} else if (FormField.TYPE_QUERY_MULTI_SELECTION.equals(type)) {// 查询专用
					fieldPart = new MultiSelectionQueryField();
				} else {
					fieldPart = new TextField();
				}
				fields.put(f,
						fieldPart.setEditable(editable).setEditorConfig(config).setFieldConfig(f).setInput(input));
				fieldPart.setEditor(this).createUI(parent)// 创建UI
						.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));// 布局
			}

		});
	}

	private Composite createTabItem(String tabText) {
		TabItem item = new TabItem(folder, SWT.NONE);
		item.setText(tabText);
		final ScrolledComposite sc = new ScrolledComposite(folder, SWT.V_SCROLL);
		Composite content = new Composite(sc, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.marginBottom = 32;
		layout.marginTop = 32;
		layout.marginLeft = 32;
		layout.marginRight = 32;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.verticalSpacing = 16;
		content.setLayout(layout);

		sc.setExpandVertical(true);
		sc.setContent(content);

		sc.addListener(SWT.Resize, e -> {
			Point size = content.computeSize(sc.getBounds().width, SWT.DEFAULT);
			sc.getContent().setSize(size.x, size.y);
			sc.setMinHeight(size.y);
		});
		item.setControl(sc);
		return content;
	}

	public IBruiService getBruiService() {
		return bruiService;
	}

	public IBruiEditorContext getContext() {
		return context;
	}

	public void switchContent(SelectionField field, String assemblyId) {
		contentArea.getShell().setText("选择 " + field.getFieldConfig().getFieldText());
		/////////////////////////////////////////////////////////////////////////////////////////////
		// 扩展DataGrid
		// 1. 实例化，设置上下文
		BruiAssemblyEngine brui = BruiAssemblyEngine.newInstance(Brui.site.getAssembly(assemblyId));
		context.add(containerContext = new BruiAssemblyContext().setParent(context));
		containerContext.setEngine(brui);

		DataGrid grid = ((DataGrid) brui.getTarget());

		if (field instanceof MultiSelectionField) {// 如果是多选，添加check
			grid.setCheckOn();
		}

		// 2. 设置表格项的选择
		grid.addItemSelector(new ToolItemDescriptor("选择", e -> {
			if ("choice".equals(e.text) && field.setSelection(Arrays.asList(new Object[] { e.item.getData() })))
				closeContainer();
		}));

		// 3. 增加工具栏按钮
		grid.addToolItem(new ToolItemDescriptor("查询", BruiToolkit.CSS_INFO, e -> {
			// TODO
		}));

		if (field instanceof MultiSelectionField) {
			grid.addToolItem(new ToolItemDescriptor("确定", BruiToolkit.CSS_NORMAL, e -> {
				if (field.setSelection(grid.getSelectedItems()))
					closeContainer();
			}));
		}

		grid.addToolItem(new ToolItemDescriptor("取消", BruiToolkit.CSS_WARNING, e -> {
			closeContainer();
		}));

		// 4. 初始化并创建UI
		container = new Composite(contentArea, SWT.BORDER);
		container.setBackground(container.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		brui.init(new IServiceWithId[] { bruiService, containerContext }).createUI(container);

		FormData fd = new FormData();
		container.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);
		container.moveAbove(null);
		contentArea.layout();
	}

	private void closeContainer() {
		Optional.ofNullable(container).ifPresent(c -> c.dispose());
		Optional.ofNullable(containerContext).ifPresent(c -> c.dispose());
		context.remove(containerContext);
		contentArea.getShell().setText(title);
	}

}
