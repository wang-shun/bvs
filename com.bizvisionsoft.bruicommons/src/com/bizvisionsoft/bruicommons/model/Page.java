package com.bizvisionsoft.bruicommons.model;

import java.util.ArrayList;
import java.util.List;

import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.Structure;
import com.bizvisionsoft.annotations.md.service.WriteValue;

public class Page extends ModelObject {
	
	@ReadValue(ReadValue.TYPE)
	private String typeName = "Ò³Ãæ";

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
	
	@Behavior({"É¾³ý","±à¼­"})
	private boolean behavior = true;
	
	@Structure("list")
	private List<Object> getStructure(){
		ArrayList<Object> result = new ArrayList<Object>();
		result.add(sidebar);
		result.add(headbar);
		result.add(footbar);
		result.add(contentArea);
		return result;
	}
	
	@Structure("count")
	private long countStructure() {
		return 4;
	}

	private Headbar headbar;

	private Sidebar sidebar;

	private Footbar footbar;

	private ContentArea contentArea;

	@ReadValue
	@WriteValue
	private String description;

	@ReadValue
	@WriteValue
	private boolean home;

	@ReadValue
	@WriteValue
	private boolean checkLogin;

	@ReadValue
	@WriteValue
	private boolean forceCheckLogin;
	
	@ReadValue
	@WriteValue
	private String inputDataSetService;

	@ReadValue
	@WriteValue
	private String inputDataSetBundleId;

	@ReadValue
	@WriteValue
	private String inputDataSetClassName;

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
		this.description = description;
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
		this.checkLogin = checkLogin;
	}

	public void setForceCheckLogin(boolean forceCheckLogin) {
		this.forceCheckLogin = forceCheckLogin;
	}
	
	public void setInputDataSetBundleId(String inputDataSetBundleId) {
		this.inputDataSetBundleId = inputDataSetBundleId;
	}
	
	public void setInputDataSetClassName(String inputDataSetClassName) {
		this.inputDataSetClassName = inputDataSetClassName;
	}
	
	public void setInputDataSetService(String inputDataSetService) {
		this.inputDataSetService = inputDataSetService;
	}
	
	public String getInputDataSetBundleId() {
		return inputDataSetBundleId;
	}
	
	public String getInputDataSetClassName() {
		return inputDataSetClassName;
	}
	
	public String getInputDataSetService() {
		return inputDataSetService;
	}

}
