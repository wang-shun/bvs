package com.bizvisionsoft.bruiengine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.Application.OperationMode;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.client.WebClient;
import org.eclipse.rap.rwt.service.ResourceLoader;

import com.bizvisionsoft.bruicommons.ModelLoader;

public class BruiApplicationConfiguration implements ApplicationConfiguration {

	public void configure(Application application) {
		String title = ModelLoader.site.getTitle();
		String path = ModelLoader.site.getPath();
		String headHtml = ModelLoader.site.getHeadHtml();
		String bodyHtml = ModelLoader.site.getBodyHtml();
		String pageOverflow = ModelLoader.site.getPageOverflow();
		String aliasResFolder = ModelLoader.site.getAliasOfResFolder();
		String favIcon = ModelLoader.site.getFavIcon();
		bindingApplcationExtenalResource(application, new File(new File(ModelLoader.sitePath).getParent() + "/res"),
				aliasResFolder);

		Map<String, String> properties = new HashMap<String, String>();
		if (title != null && !title.isEmpty())
			properties.put(WebClient.PAGE_TITLE, title);
		if (headHtml != null && !headHtml.isEmpty())
			properties.put(WebClient.HEAD_HTML, headHtml);
		if (bodyHtml != null && !bodyHtml.isEmpty())
			properties.put(WebClient.BODY_HTML, bodyHtml);
		if (pageOverflow != null && !pageOverflow.isEmpty())
			properties.put(WebClient.PAGE_OVERFLOW, pageOverflow);
		if (favIcon != null && !favIcon.isEmpty())
			properties.put(WebClient.FAVICON, aliasResFolder + favIcon);

		application.setOperationMode(OperationMode.SWT_COMPATIBILITY);
	    application.addStyleSheet( RWT.DEFAULT_THEME_ID, "resource/theme/default.css" );
		application.addEntryPoint(path, BruiEntryPoint.class, properties);

	}

	/**
	 * 绑定外部资源
	 * 
	 * @param aliasResFolder
	 */
	public static void bindingApplcationExtenalResource(Application application, File resFolder,
			String aliasResFolder) {
		File[] files = resFolder.listFiles();
		for (int i = 0; i < files.length; i++) {
			final File f = files[i];
			String resourceName = aliasResFolder + "/" + f.getName();
			if (f.isDirectory()) {
				bindingApplcationExtenalResource(application, f, resourceName);
			} else {
				application.addResource(resourceName, new ResourceLoader() {
					public InputStream getResourceAsStream(String resourceName) throws IOException {
						return new FileInputStream(f);
					}
				});
			}
		}
	}

}
