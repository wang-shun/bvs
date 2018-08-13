package com.bizvisionsoft.bruiengine.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.types.ObjectId;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.bizivisionsoft.widgets.datetime.DateTimeSetting;
import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.ui.DateTimeInputDialog;
import com.bizvisionsoft.bruiengine.ui.Part;
import com.bizvisionsoft.bruiengine.ui.View;
import com.bizvisionsoft.bruiengine.util.Util;
import com.bizvisionsoft.service.SystemService;
import com.bizvisionsoft.service.model.Command;
import com.bizvisionsoft.service.model.OperationInfo;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.serviceconsumer.Services;

public class BruiService implements IBruiService {

	final private Part part;

	public BruiService(Part part) {
		this.part = part;
	}

	@Override
	public boolean closeCurrentPart() {
		return part.close();
	}

	@Override
	public User getCurrentUserInfo() {
		return Brui.sessionManager.getUser();
	}

	public User getCurrentConsignerInfo() {
		return Brui.sessionManager.getConsigner();
	}

	public OperationInfo operationInfo() {
		OperationInfo info = new OperationInfo();
		User user = getCurrentUserInfo();
		info.userId = user.getUserId();
		info.userName = user.getName();
		User consigner = getCurrentConsignerInfo();
		info.consignerId = consigner.getUserId();
		info.consignerName = consigner.getName();
		info.date = new Date();
		return info;
	}

	@Override
	public void loginUser(User user) {
		if (!user.isAdmin() && !user.isBuzAdmin() && !user.isSU() && ModelLoader.site.getShutDown() != null) {
			throw new RuntimeException("ϵͳά���У���ֹ�û���¼ϵͳ��<br>������ѯ������ϵϵͳ����Ա��");
		}
		Brui.sessionManager.setSessionUserInfo(user);
	}

	public void loginUser() {
		User user = Brui.sessionManager.getUser();
		if (!user.isAdmin() && !user.isBuzAdmin() && !user.isSU() && ModelLoader.site.getShutDown() != null) {
			throw new RuntimeException("ϵͳά���У���ֹ�û���¼ϵͳ��<br>������ѯ������ϵϵͳ����Ա��");
		}
		Brui.sessionManager.updateSessionUserInfo();
	}

	public void consign(User user) {
		Brui.sessionManager.consign(user);
	}

	public void logout() {
		Brui.sessionManager.logout();

	}

	@Override
	public String getResourceURL(String resPath) {
		if (!resPath.startsWith("/")) {
			resPath = "/" + resPath;
		}
		String aliasOfResFolder = ModelLoader.site.getAliasOfResFolder();
		return "rwt-resources/" + aliasOfResFolder + resPath;
	}

	@Override
	public Shell getCurrentShell() {
		return part.getShell();
	}

	@Override
	public void switchContent(Assembly assembly, Object input) {
		if (part instanceof View) {
			((View) part).switchAssemblyInContentArea(assembly, input);
		}
	}

	@Override
	public void openContent(Assembly assembly, Object input) {
		if (part instanceof View) {
			((View) part).openAssemblyInContentArea(assembly, input);
		}
	}

	@Override
	public void closeCurrentContent() {
		if (part instanceof View) {
			((View) part).closeCurrentContent();
		}
	}

	@Override
	public void switchContent(String assemblyName, Object input) {
		switchContent(getAssembly(assemblyName), input);
	}

	@Override
	public void switchPage(String pageName, String inputUid) {
		Page page = ModelLoader.site.getPageByName(pageName);
		if (page == null) {
			throw new RuntimeException("ȱ��ҳ�涨�塣" + pageName);
		}
		switchPage(page, inputUid);
	}

	@Override
	public void switchPage(Page page, String inputUid) {
		UserSession.current().getEntryPoint().switchPage(page, inputUid, true);
	}

	@Override
	public String getCurrentUserId() {
		return getCurrentUserInfo().getUserId();
	}

	public String getCurrentConsignerId() {
		return getCurrentConsignerInfo().getUserId();
	}

	@Override
	public boolean confirm(String title, String message) {
		return MessageDialog.openConfirm(getCurrentShell(), title, message);
	}

	@Override
	public Command command(ObjectId target_id, Date date, String name) {
		return Command.newInstance(name, getCurrentUserInfo(), getCurrentConsignerInfo(), date, target_id);
	}

	@Override
	public boolean switchMnt(boolean b) {
		if (b) {
			DateTimeInputDialog dt = new DateTimeInputDialog(getCurrentShell(), "����ϵͳά��",
					"��ѡ������ϵͳά����ʱ�䡣\n��ʱ�䵽��ʱ���ѵ�½���û�����ǿ�Ƶǳ���ֱ���ر�ϵͳά����", null,
					d -> (d == null || d.before(new Date())) ? "����ѡ������ʱ�䣨���ڵ�ǰʱ�䣩" : null)
							.setDateSetting(DateTimeSetting.dateTime().setRange(false));
			if (dt.open() != DateTimeInputDialog.OK) {
				return false;
			}

			Date date = dt.getValue();
			if (confirm("����ϵͳά��", "����ϵͳά����<br>" + "�û�����ֹ��¼ϵͳ��<br>" + "�ѵ�¼�û�������"
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) + "��ǿ�Ƶǳ���")) {
				Brui.sessionManager.getUserSessions().forEach(u -> u.logout(date));
				ModelLoader.site.setShutDown(date);
				try {
					ModelLoader.saveSite();
					return true;
				} catch (IOException e) {
					Layer.message(e.getMessage(), Layer.ICON_CANCEL);
				}
			}
			return false;
		} else {
			if (confirm("���ϵͳά��", "��ȷ��ϵͳά���Ѿ���ɣ��������û���¼��")) {
				ModelLoader.site.setShutDown(null);
				try {
					ModelLoader.saveSite();
					return false;
				} catch (IOException e) {
					Layer.message(e.getMessage(), Layer.ICON_CANCEL);
				}
			}
			return true;
		}

	}

	@Override
	public void backup() {
		if (ModelLoader.site.getShutDown() == null) {
			Layer.message("����������ϵͳά����", Layer.ICON_CANCEL);
			return;
		}

		InputDialog id = new InputDialog(getCurrentShell(), "ϵͳ����", "����д����˵������ȷ����ʼ����ϵͳ����", "", null)
				.setTextMultiline(true);
		if (id.open() != InputDialog.OK) {
			return;
		}

		String path = Services.get(SystemService.class).mongodbDump(id.getValue());
		MessageDialog.openInformation(getCurrentShell(), "ϵͳ����", "ϵͳ������ɡ�<br>" + path);
	}

	@Override
	public void updateSidebarActionBudget(String actionName) {
		Util.ifInstanceThen(part, View.class, p->p.updateSidebarActionBudget(actionName));
	}
}
