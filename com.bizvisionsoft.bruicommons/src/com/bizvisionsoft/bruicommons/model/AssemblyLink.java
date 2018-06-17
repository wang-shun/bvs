package com.bizvisionsoft.bruicommons.model;

import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.bruicommons.ModelLoader;

public class AssemblyLink extends ModelObject{
	
	@ReadValue(ReadValue.TYPE)
	private String typeName = "×é¼þÁ´½Ó";

	@Override
	@Label
	public String toString() {
		return ModelLoader.site.getAssembly(id).toString();
	}
	
	@ReadValue("name")
	public String getName() {
		return ModelLoader.site.getAssembly(id).getName();
	}
	
	@ReadValue("title")
	public String getTitle() {
		return ModelLoader.site.getAssembly(id).getTitle();
	}
	
	@Behavior({"É¾³ý","±à¼­"})
	private boolean behavior = true;
	
	private String id;
	
	private boolean defaultAssembly;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public boolean isDefaultAssembly() {
		return defaultAssembly;
	}
	
	public void setDefaultAssembly(boolean defaultAssembly) {
		Object old = this.defaultAssembly;
		this.defaultAssembly = defaultAssembly;
		firePropertyChange("defaultAssembly", old, this.defaultAssembly);
	}
	
	private String role;
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	

}
