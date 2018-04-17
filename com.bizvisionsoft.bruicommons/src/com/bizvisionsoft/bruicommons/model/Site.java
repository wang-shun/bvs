package com.bizvisionsoft.bruicommons.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.Structure;
import com.bizvisionsoft.annotations.md.service.WriteValue;

/**
 * @author hua
 *
 */
public class Site extends ModelObject {

	@ReadValue(ReadValue.TYPE)
	private String typeName = "站点";

	@Override
	@Label
	public String toString() {
		return name + " [" + id + "]";
	}

	@ReadValue
	@WriteValue
	private String id;

	@ReadValue
	@WriteValue
	private String name;

	@ReadValue
	@WriteValue
	private String title;

	@Behavior({ "添加", "编辑" })
	private boolean behavior = true;

	@ReadValue
	@WriteValue
	private String path;

	@ReadValue
	@WriteValue
	private String headHtml;

	@ReadValue
	@WriteValue
	private String bodyHtml;

	@ReadValue
	@WriteValue
	private String pageOverflow;

	@ReadValue
	@WriteValue
	private String aliasOfResFolder;

	@ReadValue
	@WriteValue
	private String favIcon;

	@ReadValue
	@WriteValue
	private String description;

	private String login;

	@ReadValue("login")
	public Assembly getLoginAssembly() {
		Assembly ass = Optional.ofNullable(login).map(id -> getAssembly(id)).orElse(null);
		return ass;
	}

	@WriteValue("login")
	public void setLoginAssembly(Assembly loginAssembly) {
		login = Optional.ofNullable(loginAssembly).map(l -> l.getId()).orElse(null);
	}
	
	@Structure("list")
	public List<Page> getPages() {
		return pages;
	}

	@Structure("count")
	public long countPages() {
		return Optional.ofNullable(pages).map(p -> p.size()).orElse(0);
	}
	
	private Folder rootFolder;
	
	private List<Page> pages;

	private AssemblyLib assyLib;

	public void setPages(List<Page> pages) {
		this.pages = pages;
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

	public Folder getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(Folder rootFolder) {
		this.rootFolder = rootFolder;
	}

	public Page getPageByName(String pageName) {
		return pages.stream().filter(p -> pageName.equals(p.getName())).findFirst().orElse(null);
	}

	public Page getPageById(String pageId) {
		return pages.stream().filter(p -> pageId.equals(p.getId())).findFirst().orElse(null);
	}

}
