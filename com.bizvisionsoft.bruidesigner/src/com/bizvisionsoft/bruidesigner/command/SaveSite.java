package com.bizvisionsoft.bruidesigner.command;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.bizvisionsoft.bruidesigner.Activator;
import com.bizvisionsoft.bruidesigner.model.SiteLoader;

public class SaveSite extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			SiteLoader.saveSite(Activator.siteFile);
		} catch (IOException e) {
			throw new ExecutionException("±£´æ³öÏÖ´íÎó¡£", e);
		}
		return null;
	}

}
