package com.bizvisionsoft.bruiengine.assembly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruiengine.BruiDataSetEngine;
import com.bizvisionsoft.bruiengine.assembly.field.BannerField;
import com.bizvisionsoft.bruiengine.assembly.field.CheckField;
import com.bizvisionsoft.bruiengine.assembly.field.EditorField;
import com.bizvisionsoft.bruiengine.assembly.field.HtmlPageField;
import com.bizvisionsoft.bruiengine.assembly.field.InLineWrapper;
import com.bizvisionsoft.bruiengine.assembly.field.MultiCheckField;
import com.bizvisionsoft.bruiengine.assembly.field.RadioField;
import com.bizvisionsoft.bruiengine.assembly.field.TextAreaField;
import com.bizvisionsoft.bruiengine.assembly.field.TextField;
import com.bizvisionsoft.bruiengine.assembly.field.TextPageField;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class InfopadPart implements IDataSetEngineProvider{

	private Assembly config;

	@Inject
	private IBruiService bruiService;

	@Inject
	private IBruiContext context;

	private Object input;

	private Map<FormField, EditorField> fields;

	private BruiDataSetEngine dataSetEngine;

	public InfopadPart(Assembly assembly) {
		this.config = assembly;
		fields = new HashMap<FormField, EditorField>();
	}

	@Init
	private void init() {
		dataSetEngine = BruiDataSetEngine.create(config, bruiService, context);
		Assert.isNotNull(dataSetEngine, config.getName() + "组件缺少数据集定义");
	}
	
	
	public void reload() {
		List<?> _data = (List<?>) dataSetEngine.query(null, context, "list");
		if (_data instanceof List<?> && ((List<?>) _data).size() > 0) {
			input = ((List<?>) _data).get(0);
		} else {
			throw new RuntimeException("数据源list方法返回为空。");
		}
		fields.values().forEach(f->f.setInput(input).update());
	}

	@CreateUI
	private void createUI(Composite parent) {
		List<?> _data = (List<?>) dataSetEngine.query(null, context, "list");
		if (_data instanceof List<?> && ((List<?>) _data).size() > 0) {
			input = ((List<?>) _data).get(0);
		} else {
			throw new RuntimeException("数据源list方法返回为空。");
		}

		Composite panel;
		if (config.isHasTitlebar()) {
			panel = createSticker(parent);
		} else {
			panel = parent;
		}

		FormLayout layout = new FormLayout();
		layout.spacing = 8;
		layout.marginWidth = 8;
		layout.marginHeight = 8;

		panel.setLayout(layout);
		List<FormField> fields = config.getFields();
		// 判断是否分标签页
		Composite contentHolder;
		if (FormField.TYPE_PAGE.equals(fields.get(0).getType())) {
			TabFolder folder = new TabFolder(panel, SWT.TOP | SWT.BORDER);
			fields.forEach(f -> {
				Composite content = createTabItem(folder, f.getFieldText());
				if (FormField.TYPE_PAGE_HTML.equals(f.getType())) {
					createField(content, f).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				} else if (FormField.TYPE_PAGE_NOTE.equals(f.getType())) {
					createField(content, f).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				} else {
					createFields(content, f.getFormFields());
				}
			});
			contentHolder = folder;
		} else {
			ScrolledComposite sc = createPage(panel);
			Composite content = (Composite) sc.getContent();
			createFields(content, fields);
			contentHolder = sc;
		}

		FormData fd = new FormData();
		contentHolder.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);
	}

	private Composite createSticker(Composite parent) {
		StickerPart sticker = new StickerPart(config);
		sticker.context = context;
		sticker.service = bruiService;
		sticker.createUI(parent);
		return sticker.content;
	}

	private void createFields(Composite parent, List<FormField> formFields) {
		formFields.forEach(f -> {
			String type = f.getType();
			if (FormField.TYPE_INLINE.equals(type)) {
				Composite container = new InLineWrapper().getContainer(parent, f.getFormFields().size());
				container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
				createFields(container, f.getFormFields());
			} else {
				createField(parent, f);
			}
		});
	}

	private Control createField(Composite parent, FormField f) {
		String type = f.getType();
		EditorField fieldPart = null;
		if (FormField.TYPE_TEXT.equals(type)) {
			fieldPart = new TextField();
		} else if (FormField.TYPE_BANNER.equals(type)) {// 横幅，说明性字段
			fieldPart = new BannerField();
		} else if (FormField.TYPE_LABEL.equals(type)) {
			fieldPart = new TextField();
		} else if (FormField.TYPE_LABEL_MULTILINE.equals(type)) {
			fieldPart = new TextAreaField();
		} else if (FormField.TYPE_RADIO.equals(type)) {
			fieldPart = new RadioField();
		} else if (FormField.TYPE_CHECK.equals(type)) {
			fieldPart = new CheckField();
		} else if (FormField.TYPE_MULTI_CHECK.equals(type)) {
			fieldPart = new MultiCheckField();
		} else if (FormField.TYPE_PAGE_HTML.equals(type)) {// 整页专用
			fieldPart = new HtmlPageField();
		} else if (FormField.TYPE_PAGE_NOTE.equals(type)) {// 整页专用
			fieldPart = new TextPageField();
		} else {
			fieldPart = new TextField();
		}
		fields.put(f, fieldPart.setCompact(true).setEditorConfig(config).setFieldConfig(f).setInput(input));
		fieldPart.createUI(parent)// 创建UI
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));// 布局
		return fieldPart.getContainer();
	}

	private Composite createTabItem(TabFolder folder, String tabText) {
		TabItem item = new TabItem(folder, SWT.NONE);
		item.setText(tabText);
		ScrolledComposite sc = createPage(folder);
		item.setControl(sc);
		return (Composite) sc.getContent();
	}

	private ScrolledComposite createPage(Composite parent) {
		final ScrolledComposite sc = new ScrolledComposite(parent, SWT.V_SCROLL);
		Composite content = new Composite(sc, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.marginBottom = 8;
		layout.marginTop = 8;
		layout.marginLeft = 4;
		layout.marginRight = 4;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.verticalSpacing = 8;
		layout.horizontalSpacing = 0;
		content.setLayout(layout);

		sc.setExpandVertical(true);
		sc.setContent(content);

		sc.addListener(SWT.Resize, e -> {
			Point size = content.computeSize(sc.getBounds().width, SWT.DEFAULT);
			sc.getContent().setSize(size.x, size.y);
			sc.setMinHeight(size.y);
		});
		return sc;
	}

	@Override
	public BruiDataSetEngine getDataSetEngine() {
		return dataSetEngine;
	}


}
