package com.bizvisionsoft.onlinedesigner;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;

import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.AssemblyLayouted;
import com.bizvisionsoft.bruicommons.model.AssemblyLib;
import com.bizvisionsoft.bruicommons.model.AssemblyLink;
import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruicommons.model.ContentArea;
import com.bizvisionsoft.bruicommons.model.DataSource;
import com.bizvisionsoft.bruicommons.model.Folder;
import com.bizvisionsoft.bruicommons.model.Footbar;
import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruicommons.model.Headbar;
import com.bizvisionsoft.bruicommons.model.Layout;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruicommons.model.Sidebar;
import com.bizvisionsoft.bruicommons.model.Site;
import com.bizvisionsoft.bruicommons.model.Template;
import com.bizvisionsoft.bruicommons.model.TemplateLib;
import com.google.gson.GsonBuilder;

public class DesignerlToolkit {

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
					+ (((AssemblyLink) model).isDefaultAssembly() ? "��Ĭ�ϣ�" : "");
		if (model instanceof AssemblyLayouted)
			return getAssembly(((AssemblyLayouted) model).getId()).getName();
		if (model instanceof ContentArea)
			return "������";
		if (model instanceof Footbar)
			return ((Footbar) model).isEnabled() ? "����" : "�������ѽ��ã�";
		if (model instanceof Headbar)
			return ((Headbar) model).isEnabled() ? "����" : "�������ѽ��ã�";
		if (model instanceof Sidebar)
			return ((Sidebar) model).isEnabled() ? "�����" : "��������ѽ��ã�";
		if (model instanceof Site)
			return ((Site) model).getName();
		if (model instanceof Page)
			return ((Page) model).getName();
		if (model instanceof Template)
			return ((Template) model).getName();
		if (model instanceof Folder)
			return ((Folder) model).getName();
		if (model instanceof Layout)
			return ((Layout) model).getName();
		if (model instanceof Column)
			return ((Column) model).getName()
					+ (((Column) model).getText() == null ? "" : (" ��" + ((Column) model).getText() + "��"));
		if (model instanceof FormField) {
			if (FormField.TYPE_INLINE.equals(((FormField) model).getType())) {
				return " [" + ((FormField) model).getType() + "]";
			}
			return " [" + ((FormField) model).getType() + "] " + ((FormField) model).getName();
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


	public static Page createPage(Site site) {
		Page page = new Page();
		page.setId(generateId());
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
		ModelLoader.site.getPages().remove(page);
	}

	public static Action createAction(String type) {
		Action action = new Action();
		action.setType(type);
		action.setId(generateId());
		String name = generateName("�²�����");
		action.setName(name);

		if(Action.TYPE_INSERT.equals(type)) {
			
		}
		
		
		return action;
	}

	public static Layout createLayout() {
		Layout layout = new Layout();
		layout.setAssemblys(new ArrayList<AssemblyLayouted>());
		layout.setId(generateId());
		layout.setName(generateName("�²���"));

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
		site.setName("��վ��");
		site.setPages(new ArrayList<Page>());
		AssemblyLib assyLib = new AssemblyLib();
		assyLib.setAssys(new ArrayList<Assembly>());
		site.setAssyLib(assyLib);
		TemplateLib templateLib = new TemplateLib();
		templateLib.setTemplates(new ArrayList<Template>());
		return site;
	}

	public static Assembly createAssembly(String type, String folderId) {
		Assembly assy = new Assembly();
		assy.setId(generateId());
		if (Assembly.TYPE_STICKER.equals(type)) {
			assy.setName(generateName("���������"));
		} else if ("grid".equals(type)) {
			assy.setName(generateName("�±�����"));
			assy.setGridHeaderVisiable(true);
			assy.setGridHasVScroll(true);
			assy.setGridLineVisiable(true);
			assy.setColumns(new ArrayList<Column>());
		} else {
			assy.setName(generateName("�����"));
		}
		assy.setLayout(new ArrayList<Layout>());
		assy.setType(type);
		assy.setFolderId(folderId);
		ModelLoader.site.getAssyLib().getAssys().add(assy);
		return assy;
	}

	public static Assembly duplicateAssembly(Assembly assy) {
		String json = new GsonBuilder().create().toJson(assy);
		Assembly newAssy = new GsonBuilder().create().fromJson(json, Assembly.class);
		newAssy.setId(generateId());
		newAssy.setName(generateName(newAssy.getName()));
		ModelLoader.site.getAssyLib().getAssys().add(newAssy);
		return newAssy;
	}

	public static void deleteAssembly(Assembly assy) {
		ModelLoader.site.getAssyLib().getAssys().remove(assy);
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
		return ModelLoader.site.getAssembly(assyId);
	}


	public static String generateFolderName(String text, File parent) {
		String name = DesignerlToolkit.generateName(text, parent.getPath());
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
		String generateName = generateName("����");
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
		String generateName = generateName("���ֶ�");
		field.setName(generateName);
		field.setFormFields(new ArrayList<FormField>());
		field.setType(FormField.TYPE_INLINE);
		return field;
	}

	public static Folder createFolder(Folder parent) {
		Folder folder = new Folder();
		folder.setId(generateId());
		if (parent == null) {
			folder.setName("վ��");
		} else {
			folder.setName(generateName("Ŀ¼"));
		}
		folder.setChildren(new ArrayList<Folder>());
		if (parent != null)
			parent.getChildren().add(folder);
		return folder;
	}

}
