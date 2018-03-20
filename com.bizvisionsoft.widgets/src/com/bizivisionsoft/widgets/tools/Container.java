package com.bizivisionsoft.widgets.tools;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.widgets.WidgetUtil;
import org.eclipse.swt.widgets.Composite;

import com.bizivisionsoft.widgets.util.WidgetToolkit;

public class Container extends Composite {

	private static final String REMOTE_TYPE = "bizvision.container";
	private RemoteObject remoteObject;

	public Container(Composite parent,int style,String target) {
		super(parent,style);
		
		setData("excludeLayout", true);
		
		String name = WidgetUtil.getId(this);
		setHtmlAttribute("name", name);
		WidgetToolkit.requireWidgetHandlerJs("container");
		remoteObject = RWT.getUISession().getConnection().createRemoteObject(REMOTE_TYPE);
		remoteObject.set("name", name);
		remoteObject.set("target", target);
	}

}
