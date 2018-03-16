package com.bizvisionsoft.demo.rsclient;

import com.bizvisionsoft.bruicommons.annotation.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.mongocodex.annotations.Exclude;
import com.bizvisionsoft.service.annotations.ReadValue;
import com.bizvisionsoft.service.annotations.WriteValue;

public class DemoQueryBuilder {

	@Inject
	@Exclude
	private IBruiService bruiService;

	@Inject
	@Exclude
	private IBruiContext context;

	@ReadValue(" �û�Id")
	@WriteValue(" �û�Id")
	public String userid;

	@ReadValue(" λ��")
	@WriteValue("λ��")
	public String loc;

}
