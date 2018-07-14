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
		
		createTextField(parent, "背景页底部左边文字:", inputData, "footLeftText", SWT.BORDER);
		
		createPathField(parent, "背景图片:", inputData, "pageBackgroundImage", SWT.BORDER);
		
		createTextField(parent, "登录欢迎词:", inputData, "welcome", SWT.BORDER);

		createTextField(parent, "超级用户密码（登录名为su）:", inputData, "password", SWT.BORDER);

		parent = createTabItemContent("Head");
		Text field = createTextField(parent, "<Head>标签HTML:", inputData, "headHtml", SWT.MULTI | SWT.BORDER | SWT.WRAP);
		field.setLayoutData(new GridData(GridData.FILL_BOTH));

		parent = createTabItemContent("Body");
		field = createTextField(parent, "<Body>标签HTML:", inputData, "bodyHtml", SWT.MULTI | SWT.BORDER | SWT.WRAP);
		field.setLayoutData(new GridData(GridData.FILL_BOTH));

		addPartNamePropertyChangeListener("name");

	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Site.class;
	}

}
