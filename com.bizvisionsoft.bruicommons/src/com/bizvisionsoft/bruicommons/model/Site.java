package com.bizvisionsoft.bruicommons.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author hua
 *
 */
public class Site extends ModelObject {

	private String id;

	private String name;

	private String title;

	private String path;

	private List<Page> pages;

	private AssemblyLib assyLib;

	private TemplateLib templateLib;

	private String headHtml;

	private String bodyHtml;

	private String pageOverflow;

	private String aliasOfResFolder;

	private String favIcon;

	private String description;

	private String login;

	private List<DataSource> dataSources;

	private Formatter formatter;

	private Folder rootFolder;

//	private List<String> extLibPath;

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public List<Page> getPages() {
		return pages;
	}

	public void setAssyLib(AssemblyLib assyLib) {
		this.assyLib = assyLib;
	}

	public AssemblyLib getAssyLib() {
		return assyLib;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		Object old = this.name;
		this.name = name;
		firePropertyChange("name", old, this.name);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		Object old = this.title;
		this.title = title;
		firePropertyChange("title", old, this.title);
	}

	public String getHeadHtml() {
		return headHtml;
	}

	public String getBodyHtml() {
		return bodyHtml;
	}

	public void setBodyHtml(String bodyHtml) {
		this.bodyHtml = bodyHtml;
	}

	public void setHeadHtml(String headHtml) {
		this.headHtml = headHtml;
	}

	public String getPageOverflow() {
		return pageOverflow;
	}

	public void setPageOverflow(String pageOverflow) {
		this.pageOverflow = pageOverflow;
	}

	public String getFavIcon() {
		return favIcon;
	}

	public void setFavIcon(String favIcon) {
		this.favIcon = favIcon;
	}

	public void setAliasOfResFolder(String aliasOfResFolder) {
		this.aliasOfResFolder = aliasOfResFolder;
	}

	public String getAliasOfResFolder() {
		return aliasOfResFolder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * 获得首页Page
	 * 
	 * @return
	 */
	public Page getHomePage() {
		return pages.parallelStream().filter(p -> p.isHome()).findFirst().orElse(null);
	}

	/**
	 * 获得登录组件
	 * 
	 * @return
	 */
	public Assembly getLoginAssembly() {
		return Optional.ofNullable(getLogin()).map(id -> getAssembly(id)).orElse(null);
	}

	/**
	 * 获得组件
	 * 
	 * @param assyId
	 * @return
	 */
	public Assembly getAssembly(String assyId) {
		return getAssyLib().getAssys().stream().filter(a -> a.getId().equals(assyId)).findFirst().orElse(null);
	}

	public Assembly getAssemblyByName(String assemblyName) {
		return getAssyLib().getAssys().stream().filter(a -> a.getName().equals(assemblyName)).findFirst().orElse(null);
	}

	public List<Assembly> getAssysByFolder(String id) {
		ArrayList<Assembly> result = new ArrayList<Assembly>();
		getAssyLib().getAssys().stream().filter(a -> id.equals(a.getFolderId())).forEach(i -> result.add(i));
		return result;
	}

	public TemplateLib getTemplateLib() {
		return templateLib;
	}

	public void setTemplateLib(TemplateLib templateLib) {
		this.templateLib = templateLib;
	}

	public List<DataSource> getDataSources() {
		return dataSources;
	}

	public void setDataSources(List<DataSource> dataSources) {
		this.dataSources = dataSources;
	}

	public Formatter getFormatter() {
		return formatter;
	}

	public void setFormatter(Formatter formatter) {
		this.formatter = formatter;
	}

	public Folder getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(Folder rootFolder) {
		this.rootFolder = rootFolder;
	}

//	public void setExtLibPath(List<String> extLibPath) {
//		this.extLibPath = extLibPath;
//	}
//
//	public List<String> getExtLibPath() {
//		return extLibPath;
//	}
}
