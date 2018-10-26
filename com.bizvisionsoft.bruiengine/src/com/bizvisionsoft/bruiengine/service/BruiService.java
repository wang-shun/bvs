package com.bizvisionsoft.bruiengine.service;

import java.io.IOException;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

import org.bson.types.ObjectId;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizivisionsoft.widgets.datetime.DateTimeSetting;
import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.ui.DateTimeInputDialog;
import com.bizvisionsoft.bruiengine.ui.Part;
import com.bizvisionsoft.bruiengine.ui.View;
import com.bizvisionsoft.service.SystemService;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.model.Command;
import com.bizvisionsoft.service.model.OperationInfo;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.service.tools.Check;
import com.bizvisionsoft.service.tools.Formatter;
import com.bizvisionsoft.serviceconsumer.Services;
import com.google.gson.GsonBuilder;

public class BruiService implements IBruiService {

	private static Logger logger = LoggerFactory.getLogger(BruiService.class);

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
	public void switchContent(Assembly assembly, Object input, String parameter) {
		if (part instanceof View) {
			((View) part).switchAssemblyInContentArea(assembly, input, parameter);
		}
	}

	@Override
	public void openContent(Assembly assembly, Object input, String parameter, Consumer<BruiAssemblyContext> callback) {
		if (part instanceof View) {
			((View) part).openAssemblyInContentArea(assembly, input, parameter, callback);
		}
	}

	@Override
	public void closeCurrentContent() {
		if (part instanceof View) {
			((View) part).closeCurrentContent();
		}
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
	public void error(String title, String message) {
		MessageDialog.openError(getCurrentShell(), title, message);
	}

	@Override
	public Command command(ObjectId target_id, Date date, String name) {
		return Command.newInstance(name, getCurrentUserInfo(), getCurrentConsignerInfo(), date, target_id);
	}

	@Override
	public boolean switchMnt(boolean b) {
		if (b) {
			DateTimeInputDialog dt = new DateTimeInputDialog(getCurrentShell(), "����ϵͳά��", "��ѡ������ϵͳά����ʱ�䡣\n��ʱ�䵽��ʱ���ѵ�½���û�����ǿ�Ƶǳ���ֱ���ر�ϵͳά����", null,
					d -> (d == null || d.before(new Date())) ? "����ѡ������ʱ�䣨���ڵ�ǰʱ�䣩" : null).setDateSetting(DateTimeSetting.dateTime().setRange(false));
			if (dt.open() != DateTimeInputDialog.OK) {
				return false;
			}

			Date date = dt.getValue();
			if (confirm("����ϵͳά��", "����ϵͳά����<br>" + "�û�����ֹ��¼ϵͳ��<br>" + "�ѵ�¼�û�������" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) + "��ǿ�Ƶǳ���")) {
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

		InputDialog id = new InputDialog(getCurrentShell(), "ϵͳ����", "����д����˵������ȷ����ʼ����ϵͳ����", "", null).setTextMultiline(true);
		if (id.open() != InputDialog.OK) {
			return;
		}

		String path = Services.get(SystemService.class).mongodbDump(id.getValue());
		MessageDialog.openInformation(getCurrentShell(), "ϵͳ����", "ϵͳ������ɡ�<br>" + path);
	}

	@Override
	public void updateSidebarActionBudget(String actionName) {
		Check.instanceThen(part, View.class, p -> p.updateSidebarActionBudget(actionName));
	}

	@Override
	public void sendMessage(User sender, User receiver, String subject, String content) {
		Brui.sessionManager.getUserSessions(receiver).forEach(us -> us.sendMessage(sender.getName(), subject, content));
	}

	@Override
	public void displaySiteModel(ModelObject config) {
		String json = new GsonBuilder().setPrettyPrinting().create().toJson(config);
		logger.debug("��ʾģ������:\n" + json);
		json = Formatter.toHtml(json);
		Layer.open("Brui ����״̬", json, 800, 600);
	}

	@Override
	public void checkLogin(String userName, String password) throws Exception {
		if (userName.isEmpty()) {
			throw new Exception("��������ȷ���û�����");
		}

		User user = null;
		if ("su".equals(userName) && password.equals(ModelLoader.site.getPassword())) {
			user = User.SU();
		} else {
			try {
				user = Services.get(UserService.class).check(userName, password);
			} catch (Exception e) {
				if(e.getCause() instanceof ConnectException) {
					throw new Exception("�޷�������֤�����������Ժ����ԡ�");
				}
				throw e;
			}
		}

		if (user == null) {
			throw new Exception("�޷�ͨ���˻���֤����������ȷ���û��������롣");
		}

		try {
			loginUser(user);
		} catch (Exception e) {
			throw e;
		}		
	}

	@Override
	public void saveClientLogin(String usr, String psw) throws Exception {
		Brui.sessionManager.saveClientLogin(usr, psw);
	}

	@Override
	public void cleanClientLogin() {
		Brui.sessionManager.cleanClientLogin();		
	}

	@Override
	public String[] loadClientLogin() {
		return Brui.sessionManager.loadClientLogin();
	}

}
