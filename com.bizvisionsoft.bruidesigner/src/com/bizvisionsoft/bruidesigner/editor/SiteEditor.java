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
		
		createTextField(parent, "����ҳ�ײ��������:", inputData, "footLeftText", SWT.BORDER);
		
		createPathField(parent, "����ͼƬ:", inputData, "pageBackgroundImage", SWT.BORDER);
		
		createTextField(parent, "��¼��ӭ��:", inputData, "welcome", SWT.BORDER);

		createTextField(parent, "�����û����루��¼��Ϊsu��:", inputData, "password", SWT.BORDER);

		parent = createTabItemContent("Head");
		Text field = createTextField(parent, "<Head>��ǩHTML:", inputData, "headHtml", SWT.MULTI | SWT.BORDER | SWT.WRAP);
		field.setLayoutData(new GridData(GridData.FILL_BOTH));

		parent = createTabItemContent("Body");
		field = createTextField(parent, "<Body>��ǩHTML:", inputData, "bodyHtml", SWT.MULTI | SWT.BORDER | SWT.WRAP);
		field.setLayoutData(new GridData(GridData.FILL_BOTH));

		addPartNamePropertyChangeListener("name");

	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Site.class;
	}

}
