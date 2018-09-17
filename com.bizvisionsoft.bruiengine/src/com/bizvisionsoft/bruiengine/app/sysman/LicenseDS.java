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
				new LicenseItem().setContractNo("JZCG3-2017-113").setOwnerName("�Ĵ����޵��������������ι�˾")
						.setUsage("�Ĵ����޵��������������ι�˾���ű���").setVersion("5.0").setQty(1).setUnit("��").setProdId("9070")
						.setProdName("BizVision ��Ŀ����ϵͳ").setRemark("��Ŀ����ϵͳ�������ܰ�"),

				new LicenseItem().setContractNo("JZCG3-2017-113").setOwnerName("�Ĵ����޵��������������ι�˾")
						.setExpiredDate(new SimpleDateFormat("yyyyMMdd").parse("20191231"))
						.setUsage("�Ĵ����޵��������������ι�˾���ű���").setQty(365).setUnit("��").setProdId("9071")
						.setProdName("BizVision ��Ŀ����ϵͳά������").setRemark("��Ŀ���պ���Ч"));
		String json = new GsonBuilder().setPrettyPrinting().create().toJson(items);

		FileWriter fw = new FileWriter(new File("d:/license.dat"));
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(json.toCharArray()); // д��char����
		bw.close();
		fw.close();

	}
}
