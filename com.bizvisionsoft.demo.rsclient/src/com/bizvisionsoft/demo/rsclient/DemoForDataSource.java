package com.bizvisionsoft.demo.rsclient;

import java.util.Arrays;
import java.util.List;

import com.bizvisionsoft.annotations.md.service.DataSet;

public class DemoForDataSource {
	
	@DataSet("�ַ���ѡ������ʾ/list")
	public List<String> getDataSource(){
		return Arrays.asList(new String[]{"��ʾ1","Demo2","��ʾ3"});
	}

}
