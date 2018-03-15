package com.bizvisionsoft.bruicommons.model;

public class Page extends ModelObject {

	private String id;

	private String name;

	private String title;

	private Headbar headbar;

	private Sidebar sidebar;

	private Footbar footbar;

	private ContentArea contentArea;

	private String description;

	private boolean home;

	private boolean checkLogin;

	private boolean forceCheckLogin;

	public Headbar getHeadbar() {
		return headbar;
	}

	public void setHeadbar(Headbar headbar) {
		this.headbar = headbar;
	}

	public Sidebar getSidebar() {
		return sidebar;
	}

	public void setSidebar(Sidebar sidebar) {
		this.sidebar = sidebar;
	}

	public Footbar getFootbar() {
		return footbar;
	}

	public void setFootbar(Footbar footbar) {
		this.footbar = footbar;
	}

	public ContentArea getContentArea() {
		return contentArea;
	}

	public void setContentArea(ContentArea contentArea) {
		this.contentArea = contentArea;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		Object old = this.title;
		this.title = title;
		firePropertyChange("title", old, this.title);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		Object old = this.description;
		this.description = description;
		firePropertyChange("description", old, this.description);
	}

	public boolean isHome() {
		return home;
	}

	public void setHome(boolean home) {
		Object old = this.home;
		this.home = home;
		firePropertyChange("home", old, this.home);
	}

	public boolean isCheckLogin() {
		return checkLogin;
	}

	public boolean isForceCheckLogin() {
		return forceCheckLogin;
	}

	public void setCheckLogin(boolean checkLogin) {
		Object old = this.checkLogin;
		this.checkLogin = checkLogin;
		firePropertyChange("checkLogin", old, this.checkLogin);
	}

	public void setForceCheckLogin(boolean forceCheckLogin) {
		Object old = this.forceCheckLogin;
		this.forceCheckLogin = forceCheckLogin;
		firePropertyChange("forceCheckLogin", old, this.forceCheckLogin);
	}

}
