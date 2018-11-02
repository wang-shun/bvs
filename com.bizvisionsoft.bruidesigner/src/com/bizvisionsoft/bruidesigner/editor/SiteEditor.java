package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruicommons.model.Site;

public class SiteEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("������Ϣ");

		createTextField(parent, "Ψһ��ʶ��:", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "վ������:", inputData, "name", SWT.BORDER);

		createTextField(parent, "����:", inputData, "description", SWT.BORDER);

		createTextField(parent, "վ�����:", inputData, "title", SWT.BORDER);

		createTextField(parent, "Servlet·��:", inputData, "path", SWT.BORDER).setMessage("/pms");
		

		createTextField(parent, "��Դ·������:", inputData, "aliasOfResFolder", SWT.BORDER).setMessage("�磺eres��ע�⣬��Ҫ�ڱ���ǰ���/");

		createPathField(parent, "�����ͼ��·��favIcon:", inputData, "favIcon", SWT.BORDER);
		
		createComboField(parent, new String[] { "��������򶼿ɹ���", "�������", "�������", "������" },
				new Object[] { "scroll", "scrollX", "scrollY", "" }, "ҳ�������ʽ:", inputData, "pageOverflow",
				SWT.READ_ONLY | SWT.BORDER);

		createAssemblyField(parent, "�û���¼���:", inputData, "login", true);

		createPathField(parent, "����ҳ����Logo�����150x60��:", inputData, "headLogo", SWT.BORDER);
		
		createIntegerField(parent, "����ҳ����Logo ��(Ĭ��150):", inputData, "headLogoWidth", SWT.BORDER, 150, 600);
		
		createIntegerField(parent, "����ҳ����Logo ��(Ĭ��60):", inputData, "headLogoHeight", SWT.BORDER, 32, 600);
		
		createTextField(parent, "����ҳ�ײ��������:", inputData, "footLeftText", SWT.BORDER);
		
		createPathField(parent, "����ͼƬ:", inputData, "pageBackgroundImage", SWT.BORDER);
		
		createTextField(parent, "����CSS:", inputData, "pageBackgroundImageCSS", SWT.BORDER);
		
		createComboField(parent, //
				new String[] {
						"Login",//
						"Grey Cloud",//
						"Spiky Naga",//
						"Deep Relief",//
						"Dirty Beauty",//
						"Saint Petersburg",//
						"Sharpeye Eagle",//
						"Blessing",//
						"Plum Plate",//
						"New York",//
						"Fly High",//
						"Soft Grass",//
						"Kind Steel",//
						"Great Whale"
				}, //
				new Object[] {
						"brui_login_bg",//
						"brui_grey_bg",//
						"brui_bg_spiky_naga",//
						"brui_bg_deep_relief",//
						"brui_bg_dirty_beauty",//
						"brui_bg_saint_petersburg",//
						"brui_bg_sharpeye_eagle",//
						"brui_bg_blessing",//
						"brui_bg_plum_plate",//
						"brui_bg_new_york",//
						"brui_bg_fly_high",//
						"brui_bg_soft_grass",//
						"brui_bg_kind_steel",//
						"brui_bg_great_whale"
				}, //
				"ҳ��Ĭ��CSS������", inputData, "defaultPageCSS", SWT.READ_ONLY | SWT.BORDER);
		
		createTextField(parent, "��¼��ӭ��:", inputData, "welcome", SWT.BORDER);

		createTextField(parent, "�����û����루��¼��Ϊsu��:", inputData, "password", SWT.BORDER);

		parent = createTabItemContent("Head");
		Text field = createTextField(parent, "<Head>��ǩHTML:", inputData, "headHtml", SWT.MULTI | SWT.BORDER | SWT.WRAP);
		field.setLayoutData(new GridData(GridData.FILL_BOTH));

		parent = createTabItemContent("Body");
		field = createTextField(parent, "<Body>��ǩHTML:", inputData, "bodyHtml", SWT.MULTI | SWT.BORDER | SWT.WRAP);
		field.setLayoutData(new GridData(GridData.FILL_BOTH));

		parent = createTabItemContent("վ�㹫��");
		createCheckboxField(parent, "���ù���", inputData, "disableNotice", SWT.CHECK);
		
		addPartNamePropertyChangeListener("name");
		
		createTextField(parent, "��1ҳ����:", inputData, "noticeContent1", SWT.MULTI | SWT.BORDER | SWT.WRAP);
		createPathField(parent, "��1ҳ����ͼ:", inputData, "noticeImg1", SWT.BORDER);
		
		createTextField(parent, "��2ҳ����:", inputData, "noticeContent2", SWT.MULTI | SWT.BORDER | SWT.WRAP);
		createPathField(parent, "��2ҳ����ͼ:", inputData, "noticeImg2", SWT.BORDER);
		
		createTextField(parent, "��3ҳ����:", inputData, "noticeContent3", SWT.MULTI | SWT.BORDER | SWT.WRAP);
		createPathField(parent, "��3ҳ����ͼ:", inputData, "noticeImg3", SWT.BORDER);

	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Site.class;
	}
	
	@Override
	protected boolean enableJsonViewer() {
		return false;
	}
	
	@Override
	protected boolean enableParameter() {
		return false;
	}
}
