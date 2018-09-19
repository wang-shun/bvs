package com.bizvisionsoft.dpsconnector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.bizvisionsoft.service.dps.EmailSender;
import com.bizvpm.dps.client.IProcessorManager;
import com.bizvpm.dps.client.Task;

public class EmailSenderImpl implements EmailSender{

	public static final String EMAIL = "com.bizvpm.dps.processor.email:email.send";
	
	public void send(String appName, String to, String subject, String emailBody, String from)
			throws Exception {
		send(appName, null, true, Arrays.asList(to), null, subject, emailBody, from, null);
	}

	public void send(String appName, String emailType, String to, String subject, String emailBody, String from)
			throws Exception {
		send(appName, emailType, true, Arrays.asList(to), null, subject, emailBody, from, null);
	}

	public void send(String appName, String emailType, boolean useServerAddress, List<String> to,
			List<String> cc, String subject, String emailBody, String from, List<Map<String, Object>> attachment)
			throws Exception {
		String _name = "Send Email Notice" + (appName == null ? "" : (" " + appName));
		String _emailType = emailType == null ? EMAIL_TYPE_IMAGE_HTML : emailType;
		if (to == null || to.isEmpty()) {
			throw new IllegalArgumentException("parameter: to");
		}
		String _subject = subject == null ? "" : subject;
		String _emailBody = emailBody == null ? "" : emailBody;

		IProcessorManager manager = DPSConnector.getProcessManager();

		Task task = new Task();
		task.setName(_name);
		task.setValue("emailType", _emailType);
		task.setValue("useServerAddress", useServerAddress);
		task.setValue("to", to);
		if (cc != null && cc.size() > 0) {
			task.setValue("cc", cc);
		}

		task.setValue("title", _subject);
		task.setValue("message", _emailBody);

		if (from != null) {
			task.setValue("from", from);
		}

		if (attachment != null && attachment.size() > 0) {
			task.setValue("attachment", attachment);
		}else {
			task.setValue("attachment", new ArrayList<>());
		}

		// Result result = manager.runTask(task, EMAIL);
		// Object r = result.getValue("result");
		// if (!Utils.isNullOrEmptyString(r)) {
		// Commons.loginfo(r.toString());
		// }
		manager.runTaskAsync(task, EMAIL);
	}

}
