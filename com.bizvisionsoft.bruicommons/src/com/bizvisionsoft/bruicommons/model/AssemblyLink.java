package com.bizvisionsoft.bruicommons.model;

public class AssemblyLink extends ModelObject{
	
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
	

}
