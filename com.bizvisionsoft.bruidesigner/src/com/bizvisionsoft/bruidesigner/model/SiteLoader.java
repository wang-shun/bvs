package com.bizvisionsoft.bruidesigner.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.bizvisionsoft.bruicommons.model.Site;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class SiteLoader {

	public static Site site;

	public static void load(File file) throws JsonSyntaxException, JsonIOException, IOException {
		if (!file.exists()) {
			File dir = new File(file.getParent() + "/res/");
			if (!dir.exists() && !dir.mkdirs())
				throw new IOException("不能创建目录" + dir);

			site = ModelToolkit.createSite();
			saveSite(file);
		} else {
			site = new GsonBuilder().create().fromJson(new FileReader(file), Site.class);
		}
	}

	public static void saveSite(File file) throws IOException {
		if (file.exists()) {
			File backupFolder = new File(file.getParent() + "/backup/");
			if (!backupFolder.isDirectory())
				backupFolder.mkdirs();
			File target = new File(file.getParent() + "/backup/" + ModelToolkit.generateId() + file.getName());
			Files.copy(file.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		String json = new GsonBuilder().setPrettyPrinting().create().toJson(site);
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(json.toCharArray()); // 写入char数组
		bw.close();
	}

}
