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

		Composite parent = createTabItemContent("基本信息");

		createTextField(parent, "唯一标识符:", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "站点名称:", inputData, "name", SWT.BORDER);

		createTextField(parent, "描述:", inputData, "description", SWT.BORDER);

		createTextField(parent, "站点标题:", inputData, "title", SWT.BORDER);

		createTextField(parent, "Servlet路径:", inputData, "path", SWT.BORDER).setMessage("/pms");
		

		createTextField(parent, "资源路径别名:", inputData, "aliasOfResFolder", SWT.BORDER).setMessage("如：eres，注意，不要在别名前后加/");

		createPathField(parent, "浏览器图标路径favIcon:", inputData, "favIcon", SWT.BORDER);
		
		createComboField(parent, new String[] { "横向和纵向都可滚动", "横向滚动", "纵向滚动", "不滚动" },
				new Object[] { "scroll", "scrollX", "scrollY", "" }, "页面滚动方式:", inputData, "pageOverflow",
				SWT.READ_ONLY | SWT.BORDER);

		createAssemblyField(parent, "用户登录组件:", inputData, "login", true);

		createPathField(parent, "背景页顶部Logo（宽高150x60）:", inputData, "headLogo", SWT.BORDER);
		
		createIntegerField(parent, "背景页顶部Logo 宽(默认150):", inputData, "headLogoWidth", SWT.BORDER, 150, 600);
		
		createIntegerField(parent, "背景页顶部Logo 高(默认60):", inputData, "headLogoHeight", SWT.BORDER, 32, 600);
		
		createTextField(parent, "背景页底部左边文字:", inputData, "footLeftText", SWT.BORDER);
		
		createPathField(parent, "背景图片:", inputData, "pageBackgroundImage", SWT.BORDER);
		
		createTextField(parent, "背景CSS:", inputData, "pageBackgroundImageCSS", SWT.BORDER);
		
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
				"页面默认CSS类名：", inputData, "defaultPageCSS", SWT.READ_ONLY | SWT.BORDER);
		
		createTextField(parent, "登录欢迎词:", inputData, "welcome", SWT.BORDER);

		createTextField(parent, "超级用户密码（登录名为su）:", inputData, "password", SWT.BORDER);

		parent = createTabItemContent("Head");
		Text field = createTextField(parent, "<Head>标签HTML:", inputData, "headHtml", SWT.MULTI | SWT.BORDER | SWT.WRAP);
		field.setLayoutData(new GridData(GridData.FILL_BOTH));

		parent = createTabItemContent("Body");
		field = createTextField(parent, "<Body>标签HTML:", inputData, "bodyHtml", SWT.MULTI | SWT.BORDER | SWT.WRAP);
		field.setLayoutData(new GridData(GridData.FILL_BOTH));

		parent = createTabItemContent("站点公告");
		createCheckboxField(parent, "禁用公告", inputData, "disableNotice", SWT.CHECK);
		
		addPartNamePropertyChangeListener("name");
		
		createTextField(parent, "第1页内容:", inputData, "noticeContent1", SWT.MULTI | SWT.BORDER | SWT.WRAP);
		createPathField(parent, "第1页背景图:", inputData, "noticeImg1", SWT.BORDER);
		
		createTextField(parent, "第2页内容:", inputData, "noticeContent2", SWT.MULTI | SWT.BORDER | SWT.WRAP);
		createPathField(parent, "第2页背景图:", inputData, "noticeImg2", SWT.BORDER);
		
		createTextField(parent, "第3页内容:", inputData, "noticeContent3", SWT.MULTI | SWT.BORDER | SWT.WRAP);
		createPathField(parent, "第3页背景图:", inputData, "noticeImg3", SWT.BORDER);

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
