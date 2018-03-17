package com.bizvisionsoft.bruidesigner.model;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.AssemblyLayouted;
import com.bizvisionsoft.bruicommons.model.AssemblyLib;
import com.bizvisionsoft.bruicommons.model.AssemblyLink;
import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruicommons.model.ContentArea;
import com.bizvisionsoft.bruicommons.model.DataSource;
import com.bizvisionsoft.bruicommons.model.Footbar;
import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruicommons.model.Formatter;
import com.bizvisionsoft.bruicommons.model.Headbar;
import com.bizvisionsoft.bruicommons.model.Layout;
import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruicommons.model.Sidebar;
import com.bizvisionsoft.bruicommons.model.Site;
import com.bizvisionsoft.bruicommons.model.Template;
import com.bizvisionsoft.bruicommons.model.TemplateLib;
import com.bizvisionsoft.bruidesigner.Activator;

public class ModelToolkit {

	private static HashMap<String, Integer> nameNumber = new HashMap<String, Integer>();

	public static String generateId() {
		return Long.toHexString(System.currentTimeMillis());
	}

	public static String generateName(String text, String key) {
		Integer number = nameNumber.get(key);
		if (number == null) {
			number = 1;
		}
		nameNumber.put(key, number + 1);
		return text + number;
	}

	public static String generateName(String text) {
		return generateName(text, text);
	}

	public static String getText(Object model) {
		if (model instanceof Action)
			return ((Action) model).getName();
		if (model instanceof Assembly)
			return ((Assembly) model).getName();
		if (model instanceof DataSource)
			return ((DataSource) model).getName();
		if (model instanceof AssemblyLink)
			return getAssembly(((AssemblyLink) model).getId()).getName()
					+ (((AssemblyLink) model).isDefaultAssembly() ? "（默认）" : "");
		if (model instanceof AssemblyLayouted)
			return getAssembly(((AssemblyLayouted) model).getId()).getName();
		if (model instanceof ContentArea)
			return "内容区";
		if (model instanceof Footbar)
			return ((Footbar) model).isEnabled() ? "底栏" : "底栏（已禁用）";
		if (model instanceof Headbar)
			return ((Headbar) model).isEnabled() ? "顶栏" : "顶栏（已禁用）";
		if (model instanceof Sidebar)
			return ((Sidebar) model).isEnabled() ? "侧边栏" : "侧边栏（已禁用）";
		if (model instanceof Site)
			return ((Site) model).getName();
		if (model instanceof Page)
			return ((Page) model).getName();
		if (model instanceof Template)
			return ((Template) model).getName();
		if (model instanceof Layout)
			return ((Layout) model).getName();
		if (model instanceof Column)
			return ((Column) model).getName() +(((Column) model).getText()==null?"":(" （"+((Column) model).getText()+"）"));
		if (model instanceof FormField) {
			return "["+((FormField) model).getType() +"] "+((FormField) model).getName();
		}
		return "";
	}

	public static String getToolTipText(Object model) {
		String text = getText(model);

		if (model instanceof Action) {
			if (((Action) model).getDescription() != null)
				text += ((Action) model).getDescription();
		} else if (model instanceof Assembly) {
			if (((Assembly) model).getDescription() != null)
				text += ((Assembly) model).getDescription();
		} else if (model instanceof DataSource) {
			if (((DataSource) model).getDescription() != null)
				text += ((Assembly) model).getDescription();
		} else if (model instanceof Site) {
			if (((Site) model).getDescription() != null)
				text += ((Site) model).getDescription();
		} else if (model instanceof Page) {
			if (((Page) model).getDescription() != null)
				text += ((Page) model).getDescription();
		}
		return text;
	}

	public static Image getImage(Object model) {
		return Optional.ofNullable(getImageDescriptor(model)).map(id -> id.createImage()).orElse(null);
	}

	public static ImageDescriptor getImageDescriptor(Object model) {
		if (model instanceof Action)
			return Activator.getImageDescriptor("icons/action.png");
		if (model instanceof Assembly)
			return Activator.getImageDescriptor("icons/assembly.png");
		if (model instanceof AssemblyLink)
			return Activator.getImageDescriptor("icons/assembly.png");
		if (model instanceof Site)
			return Activator.getImageDescriptor("icons/root.png");
		if (model instanceof Page)
			return Activator.getImageDescriptor("icons/page.png");
		if (model instanceof Footbar)
			return Activator.getImageDescriptor("icons/footbar.png");
		if (model instanceof Headbar)
			return Activator.getImageDescriptor("icons/headbar.png");
		if (model instanceof Sidebar)
			return Activator.getImageDescriptor("icons/sidebar.png");
		if (model instanceof ContentArea)
			return Activator.getImageDescriptor("icons/content.png");
		if (model instanceof Template)
			return Activator.getImageDescriptor("icons/template.png");
		if (model instanceof Layout)
			return Activator.getImageDescriptor("icons/layout.png");
		if (model instanceof AssemblyLayouted)
			return Activator.getImageDescriptor("icons/assembly.png");
		if (model instanceof DataSource)
			return Activator.getImageDescriptor("icons/database.png");
		if (model instanceof Column)
			return Activator.getImageDescriptor("icons/column.png");
		return null;
	}

	public static Page createPage(Site site) {
		Page page = new Page();
		page.setId(generateId());
		page.setName(generateName("新页面"));
		page.setTitle("新页面标题");
		ContentArea contentArea = new ContentArea();
		contentArea.setAssemblyLinks(new ArrayList<AssemblyLink>());
		page.setContentArea(contentArea);
		page.setHeadbar(new Headbar());
		page.setFootbar(new Footbar());
		Sidebar sidebar = new Sidebar();
		sidebar.setSidebarItems(new ArrayList<Action>());
		sidebar.setToolbarItems(new ArrayList<Action>());
		page.setSidebar(sidebar);
		List<Page> pages = site.getPages();
		pages.add(page);
		return page;
	}

	public static void removePage(Page page) {
		SiteLoader.site.getPages().remove(page);
	}

	public static Action createAction() {
		Action action = new Action();
		action.setId(generateId());
		String name = generateName("新操作项");
		action.setName(name);
		return action;
	}

	public static Layout createLayout() {
		Layout layout = new Layout();
		layout.setAssemblys(new ArrayList<AssemblyLayouted>());
		layout.setId(generateId());
		layout.setName(generateName("新布局"));

		layout.setColumnCount(1);
		layout.setHorizontalSpacing(16);
		layout.setVerticalSpacing(16);
		layout.setMarginWidth(16);
		layout.setMarginHeight(16);
		layout.setMakeColumnsEqualWidth(true);
		// layout.setMarginTop(l.marginTop);
		// layout.setMarginBottom(l.marginBottom);
		// layout.setMarginLeft(l.marginLeft);
		// layout.setMarginRight(l.marginRight);
		return layout;
	}

	public static Site createSite() {
		Site site = new Site();
		site.setId(generateId());
		site.setName("新站点");
		site.setPages(new ArrayList<Page>());
		site.setDataSources(new ArrayList<DataSource>());
		AssemblyLib assyLib = new AssemblyLib();
		assyLib.setAssys(new ArrayList<Assembly>());
		site.setAssyLib(assyLib);
		TemplateLib templateLib = new TemplateLib();
		templateLib.setTemplates(new ArrayList<Template>());
		site.setTemplateLib(templateLib);
		site.setFormatter(new Formatter());
		return site;
	}

	public static Assembly createAssembly(String type) {
		Assembly assy = new Assembly();
		assy.setId(generateId());
		if (Assembly.TYPE_STICKER.equals(type)) {
			assy.setName(generateName("新容器组件"));
		} else if ("grid".equals(type)) {
			assy.setName(generateName("新表格组件"));
			assy.setGridHeaderVisiable(true);
			assy.setGridHasVScroll(true);
			assy.setGridLineVisiable(true);
			assy.setColumns(new ArrayList<Column>());
		} else {
			assy.setName(generateName("新组件"));
		}
		assy.setLayout(new ArrayList<Layout>());
		assy.setType(type);
		SiteLoader.site.getAssyLib().getAssys().add(assy);
		return assy;
	}

	public static void deleteAssembly(Assembly assy) {
		SiteLoader.site.getAssyLib().getAssys().remove(assy);
	}

	public static DataSource createDataSource() {
		DataSource ds = new DataSource();
		ds.setId(generateId());
		ds.setName(generateName("新数据源"));
		SiteLoader.site.getDataSources().add(ds);
		return ds;
	}

	public static void deleteDataSource(DataSource ds) {
		SiteLoader.site.getDataSources().remove(ds);
	}

	public static void deleteTemplate(Template template) {
		SiteLoader.site.getTemplateLib().getTemplates().remove(template);
	}

	public static Template createTemplate() {
		Template template = new Template();
		template.setId(generateId());
		template.setName(generateName("新模板"));
		SiteLoader.site.getTemplateLib().getTemplates().add(template);
		return template;
	}

	public static void addAssemblies(ContentArea ca, List<String> assysIds) {
		assysIds.forEach(id -> {
			List<AssemblyLink> links = ca.getAssemblyLinks();
			if (!links.stream().anyMatch(al -> id == al.getId())) {
				AssemblyLink link = new AssemblyLink();
				link.setId(id);
				links.add(link);
			}
		});
	}

	public static void removeAssembly(ContentArea ca, Assembly assembly) {
		ca.getAssemblyLinks().removeIf(al -> al.getId() == assembly.getId());
	}

	public static Assembly getAssembly(String assyId) {
		return SiteLoader.site.getAssembly(assyId);
	}

	public static LabelProvider createLabelProvider() {
		return new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ModelToolkit.getText(element);
			}

			@Override
			public Image getImage(Object element) {
				return ModelToolkit.getImage(element);
			}
		};
	}

	public static File getSiteResourceFolder() {
		return new File(Activator.siteFile.getParent() + "/res/");
	}

	public static String generateFolderName(String text, File parent) {
		String name = ModelToolkit.generateName(text, parent.getPath());
		if (parent.listFiles((FileFilter) f -> f.isDirectory() && f.getName().equals(name)).length == 0) {
			return name;
		} else {
			return generateFolderName(text, parent);
		}
	}

	public static boolean isValidFileName(String fileName) {
		if (fileName == null || fileName.length() > 255)
			return false;
		else
			return fileName.matches(
					"[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
	}

	public static IEditorInput createEditorInput(Object element) {
		return new ModelInput(element);
	}

	public static void openEditor(Object obj, String requiredEditorId) {
		if (!(obj instanceof ModelObject)) {
			return;
		}
		ModelObject element = (ModelObject) obj;
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			String editorId;
			if (requiredEditorId != null) {
				editorId = requiredEditorId;
			} else {
				if (element instanceof Site)
					editorId = "bruidesigner.siteEditor";
				else if (element instanceof Page)
					editorId = "bruidesigner.pageEditor";
				else if (element instanceof Sidebar)
					editorId = "bruidesigner.sidebarEditor";
				else if (element instanceof Headbar)
					editorId = "bruidesigner.headbarEditor";
				else if (element instanceof Footbar)
					editorId = "bruidesigner.footbarEditor";
				else if (element instanceof Action)
					editorId = "bruidesigner.actionEditor";
				else if (element instanceof Assembly) {
					String type = ((Assembly) element).getType();
					if (Assembly.TYPE_STICKER.equals(type)) {
						editorId = "bruidesigner.assemblyStickerEditor";
					} else if (Assembly.TYPE_GRID.equals(type)) {
						editorId = "bruidesigner.assemblyGridEditor";
					} else if (Assembly.TYPE_EDITOR.equals(type)) {
						editorId = "bruidesigner.assemblyEditorEditor";
					} else {
						editorId = "bruidesigner.assemblyEditor";
					}
				} else if (element instanceof AssemblyLink)
					editorId = "bruidesigner.assemblyLinkEditor";
				else if (element instanceof Template)
					editorId = "bruidesigner.templateEditor";
				else if (element instanceof DataSource)
					editorId = "bruidesigner.dataSourceEditor";
				// else if (element instanceof ContentArea)
				// editorId = "bruidesigner.contentAreaEditor";
				else
					return;
			}

			page.openEditor(createEditorInput(element), editorId);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	public static AssemblyLayouted createAssemblyLayouted(Layout element, String assemblyId) {
		AssemblyLayouted al = new AssemblyLayouted();
		al.setId(assemblyId);
		al.setHorizontalAlignment(SWT.FILL);
		al.setVerticalAlignment(SWT.CENTER);
		al.setHorizontalSpan(1);
		al.setVerticalSpan(1);
		al.setWidthHint(-1);
		al.setHeightHint(-1);
		al.setGrabExcessHorizontalSpace(true);
		element.getAssemblys().add(al);
		return al;
	}

	public static void removeAssembly(Layout layout, AssemblyLayouted al) {
		layout.getAssemblys().remove(al);
	}

	public static Column createColumn() {
		Column column = new Column();
		column.setId(generateId());
		String generateName = generateName("新列");
		column.setName(generateName);
		column.setText(generateName);
		column.setAlignment(SWT.LEFT);
		column.setResizeable(true);
		column.setWidth(80);
		column.setColumns(new ArrayList<Column>());
		return column;
	}

	public static FormField createField() {
		FormField field = new FormField();
		field.setId(generateId());
		String generateName = generateName("新字段");
		field.setName(generateName);
		field.setFormFields(new ArrayList<FormField>());
		field.setType(FormField.TYPE_INLINE);
		return field;
	}

}
