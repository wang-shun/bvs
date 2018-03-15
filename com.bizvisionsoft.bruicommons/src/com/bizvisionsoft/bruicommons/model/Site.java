package com.bizvisionsoft.bruicommons.model;

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
		Object old = this.path;
		this.path = path;
		firePropertyChange("path", old, this.path);
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
		Object old = this.bodyHtml;
		this.bodyHtml = bodyHtml;
		firePropertyChange("bodyHtml", old, this.bodyHtml);
	}

	public void setHeadHtml(String headHtml) {
		Object old = this.headHtml;
		this.headHtml = headHtml;
		firePropertyChange("headHtml", old, this.headHtml);
	}

	public String getPageOverflow() {
		return pageOverflow;
	}

	public void setPageOverflow(String pageOverflow) {
		Object old = this.pageOverflow;
		this.pageOverflow = pageOverflow;
		firePropertyChange("pageOverflow", old, this.pageOverflow);
	}

	public String getFavIcon() {
		return favIcon;
	}

	public void setFavIcon(String favIcon) {
		Object old = this.favIcon;
		this.favIcon = favIcon;
		firePropertyChange("favIcon", old, this.favIcon);
	}

	public void setAliasOfResFolder(String aliasOfResFolder) {
		Object old = this.aliasOfResFolder;
		this.aliasOfResFolder = aliasOfResFolder;
		firePropertyChange("aliasOfResFolder", old, this.aliasOfResFolder);
	}

	public String getAliasOfResFolder() {
		return aliasOfResFolder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		Object old = this.description;
		this.description = description;
		firePropertyChange("description", old, this.description);
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		Object old = this.login;
		this.login = login;
		firePropertyChange("login", old, this.login);
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
		Object old = this.formatter;
		this.formatter = formatter;
		firePropertyChange("formatter", old, this.formatter);
	}

}
