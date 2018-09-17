package com.bizvisionsoft.bruiengine.app.sysman;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizvisionsoft.annotations.md.service.DataSet;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class LicenseDS {
	
	public Logger logger = LoggerFactory.getLogger(getClass());

	@DataSet(DataSet.LIST)
	private List<LicenseItem> listLicenseItem() {
		String path = ModelLoader.sitePath;
		String siteFile = new File(path).getParent() + "/license.dat";
		try {
			Type type = new TypeToken<List<LicenseItem>>() {
			}.getType();
			return new GsonBuilder().create().fromJson(new FileReader(new File(siteFile)), type);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return new ArrayList<LicenseItem>();
	}

	public static void main(String[] args) throws ParseException, IOException {
		List<LicenseItem> items = Arrays.asList(
				new LicenseItem().setContractNo("JZCG3-2017-113").setOwnerName("四川九洲电器集团有限责任公司")
						.setUsage("四川九洲电器集团有限责任公司集团本部").setVersion("5.0").setQty(1).setUnit("套").setProdId("9070")
						.setProdName("BizVision 项目管理系统").setRemark("项目管理系统升级功能包"),

				new LicenseItem().setContractNo("JZCG3-2017-113").setOwnerName("四川九洲电器集团有限责任公司")
						.setExpiredDate(new SimpleDateFormat("yyyyMMdd").parse("20191231"))
						.setUsage("四川九洲电器集团有限责任公司集团本部").setQty(365).setUnit("天").setProdId("9071")
						.setProdName("BizVision 项目管理系统维护服务").setRemark("项目验收后生效"));
		String json = new GsonBuilder().setPrettyPrinting().create().toJson(items);

		FileWriter fw = new FileWriter(new File("d:/license.dat"));
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(json.toCharArray()); // 写入char数组
		bw.close();
		fw.close();

	}
}
