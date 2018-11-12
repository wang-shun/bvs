package com.bizvisionsoft.bruiengine.onlinedesigner;

import java.util.Optional;

import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruiengine.ui.View;

public class PageEditor {
	
	public static Logger logger = LoggerFactory.getLogger(PageEditor.class);

	public static void open() {
		Optional.ofNullable(Display.getCurrent().getActiveShell()).map(s -> s.getData("part")).ifPresent(o -> {
			if (o instanceof View)
				openPart((View) o);
		});
	}

	public static void openPart(View part) {
		Page page = part.getPage();
		logger.debug(page.getName()+" ["+page.getId()+"]");
	}

}
