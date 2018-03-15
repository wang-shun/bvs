package com.bizvisionsoft.bruicommons.model;

import java.util.List;

public class ContentArea extends ModelObject {

	private List<AssemblyLink> assemblyLinks;

	public List<AssemblyLink> getAssemblyLinks() {
		return assemblyLinks;
	}
	
	public void setAssemblyLinks(List<AssemblyLink> assemblyLinks) {
		this.assemblyLinks = assemblyLinks;
	}
	
}
