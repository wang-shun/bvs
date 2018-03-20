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
	 * default�������л��� updown�������л��� fade�����������л���
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
	 * �Ƿ��Զ��л� boolean Ĭ��true
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
	 * �Զ��л���ʱ��������λ��ms�����룩�����ܵ���800 number Ĭ��3000
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
	 * ��ʼ��ʼ����Ŀ���� Ĭ��0
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
	 * �л���ͷĬ����ʾ״̬����ѡֵΪ��
	 * 
	 * hover����ͣ��ʾ�� always��ʼ����ʾ�� none��ʼ�ղ���ʾ��
	 * 
	 * Ĭ��hover
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
	 * ָʾ��λ�ã���ѡֵΪ��
	 * 
	 * inside�������ڲ��� outside�������ⲿ�� none������ʾ��
	 * 
	 * Ĭ��inside
	 * 
	 * ע�⣺����趨�� anim:'updown'���ò�������Ч
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
