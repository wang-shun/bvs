package com.bizvisionsoft.demo.rsclient;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.bizvisionsoft.bruiengine.ui.BruiToolkit;
import com.bizvisionsoft.bruiengine.util.Util;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class DemoAssemblyInvokeRS {

	@Inject
	private IBruiService bruiService;

	@CreateUI
	private void createUI(Composite parent) {
		parent.setLayout(new RowLayout(SWT.VERTICAL));
		BruiToolkit bruiToolkit = UserSession.bruiToolkit();
		Button button ;

		button = bruiToolkit.newStyledControl(Button.class, parent, SWT.PUSH, BruiToolkit.CSS_NORMAL);
		button.setText("����һ���û�,��������");
		button.addListener(SWT.Selection, e -> {
			BasicDBObject filterAndUpdate = new BasicDBObject().append("filter", new BasicDBObject("userId", "zh"))
					.append("update", new BasicDBObject("$set", new BasicDBObject("activated", false)));
			long cnt = Services.get(UserService.class).update(filterAndUpdate);
			System.out.println(cnt);
		});

		button = bruiToolkit.newStyledControl(Button.class, parent, SWT.PUSH, BruiToolkit.CSS_NORMAL);
		button.setText("����һ���û��������û�Id");
		button.addListener(SWT.Selection, e -> {
			// BasicDBObject update = new BasicDBObject("$set", new BasicDBObject("name",
			// "ABCD"));
			// long cnt = ServicesLoader.get(UserService.class).updateByUserId("1", update);
			// System.out.println(cnt);
		});

//		button = bruiToolkit.newStyledControl(Button.class, parent, SWT.PUSH, BruiToolkit.CSS_NORMAL);
//		button.setText("��������֯");
//		button.addListener(SWT.Selection, e -> {
//			Organization orgInfo = new Organization().setFullName("�人��������������޹�˾").setName("��������").setNumber("BIZV")
//					.setType("��˾");
//			orgInfo = Services.get(OrganizationService.class).insert(orgInfo);
//			System.out.println(orgInfo);
//		});

//		button = bruiToolkit.newStyledControl(Button.class, parent, SWT.PUSH, BruiToolkit.CSS_NORMAL);
//		button.setText("�����¼���֯");
//		button.addListener(SWT.Selection, e -> {
//			Organization orgInfo = new Organization().setFullName("����������Բ�").setName("���Բ�").setNumber("RTD")
//					.setType("����").setParentId(new ObjectId("5a98b5de89db0071a0f77269"));
//			orgInfo = Services.get(OrganizationService.class).insert(orgInfo);
//			System.out.println(orgInfo);
//		});

		button = bruiToolkit.newStyledControl(Button.class, parent, SWT.PUSH, BruiToolkit.CSS_NORMAL);
		button.setText("����һ�������û�");
		button.addListener(SWT.Selection, e -> {
			for (int i = 0; i < 300; i++) {
				String id = Util._10_to_N(i, 24);
				User user = new User();//
				Services.get(UserService.class).insert(user);
			}
			System.out.println("ok");
		});

//		button = bruiToolkit.newStyledControl(Button.class, parent, SWT.PUSH, BruiToolkit.CSS_NORMAL);
//		button.setText("���Բ�ѯ�û�");
//		button.addListener(SWT.Selection, e -> {
//			BasicDBObject condition = new BasicDBObject().append("skip", 0).append("limit", 1).append("filter",
//					new BasicDBObject("userId", "zh"));
//			List<UserInfo> result = Services.get(UserService.class).createDataSet(condition);
//			System.out.println("ok" + result);
//		});
	}
}
