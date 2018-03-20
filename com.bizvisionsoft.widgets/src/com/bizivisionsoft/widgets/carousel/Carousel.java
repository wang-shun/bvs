package com.bizivisionsoft.widgets.carousel;

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

import com.bizivisionsoft.widgets.util.WidgetToolkit;

public class Carousel extends Composite {

	private static final String REMOTE_TYPE = "bizvision.carousel";

	private RemoteObject remoteObject;

	private JsonObject renderSetting;

	public Carousel(Composite parent, int style) {
		super(parent, style);
		WidgetToolkit.requireWidgetsJs("carousel");
		remoteObject = RWT.getUISession().getConnection().createRemoteObject(REMOTE_TYPE);
		remoteObject.set("parent", getId(this));
		renderSetting = new JsonObject();
		remoteObject.set("renderSetting", renderSetting);
		setAnimation("default");
		setAutoplay(true);
		setInterval(3000);
		setArrow("hover");
		setIndicator("inside");

		super.setLayout(new FormLayout());
	}

	public <T extends Control> T addPage(T control) {
		checkWidget();
		if (control.getParent() != this) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		FormData fd = new FormData();
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.bottom = new FormAttachment(100);
		fd.right = new FormAttachment(100);
		control.setLayoutData(fd);
		return control;
	}

	@Override
	public void setLayout(Layout layout) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	/**
	 * 
	 * default（左右切换） updown（上下切换） fade（渐隐渐显切换）
	 * 
	 * @param anim
	 * @return
	 */
	public Carousel setAnimation(String anim) {
		checkWidget();
		renderSetting.set("anim", anim);
		remoteObject.set("setting", renderSetting);
		return this;
	}

	/**
	 * 是否自动切换 boolean 默认true
	 * 
	 * @param autoplay
	 * @return
	 */
	public Carousel setAutoplay(boolean autoplay) {
		checkWidget();
		renderSetting.set("autoplay", autoplay);
		remoteObject.set("setting", renderSetting);
		return this;
	}

	/**
	 * 自动切换的时间间隔，单位：ms（毫秒），不能低于800 number 默认3000
	 * 
	 * @param interval
	 * @return
	 */
	public Carousel setInterval(int interval) {
		checkWidget();
		if (interval < 800) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		renderSetting.set("interval", interval);
		remoteObject.set("setting", renderSetting);
		return this;
	}

	/**
	 * 初始开始的条目索引 默认0
	 * 
	 * @param index
	 * @return
	 */
	public Carousel setIndex(int index) {
		checkWidget();
		renderSetting.set("index", index);
		remoteObject.set("setting", renderSetting);
		return this;
	}

	/**
	 * 切换箭头默认显示状态，可选值为：
	 * 
	 * hover（悬停显示） always（始终显示） none（始终不显示）
	 * 
	 * 默认hover
	 * 
	 * @param arrow
	 * @return
	 */
	public Carousel setArrow(String arrow) {
		checkWidget();
		renderSetting.set("arrow", arrow);
		remoteObject.set("setting", renderSetting);
		return this;
	}

	/**
	 * 指示器位置，可选值为：
	 * 
	 * inside（容器内部） outside（容器外部） none（不显示）
	 * 
	 * 默认inside
	 * 
	 * 注意：如果设定了 anim:'updown'，该参数将无效
	 * 
	 * @param indicator
	 * @return
	 */
	public Carousel setIndicator(String indicator) {
		checkWidget();
		renderSetting.set("indicator", indicator);
		remoteObject.set("setting", renderSetting);
		return this;
	}

}
