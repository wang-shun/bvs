package com.bizvisionsoft.bruicommons.model;

import java.util.List;
import java.util.Optional;

import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.Structure;

public class ContentArea extends ModelObject {

	@ReadValue(ReadValue.TYPE)
	private String typeName = "内容区";

	@Override
	@Label
	public String toString() {
		return "内容区";
	}
	
	@Behavior("添加")
	private boolean behavior = true;


	@Structure("list")
	public List<AssemblyLink> getAssemblyLinks() {
		return assemblyLinks;
	}
	
	@Structure("count")
	public long countAssemblyLinks() {
		return Optional.ofNullable(assemblyLinks).map(l -> l.size()).orElse(0);
	}

	private List<AssemblyLink> assemblyLinks;

	public void setAssemblyLinks(List<AssemblyLink> assemblyLinks) {
		this.assemblyLinks = assemblyLinks;
	}

}
