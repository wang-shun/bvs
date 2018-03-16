package com.bizvisionsoft.bruiengine.assembly.field;

import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.scripting.ClientListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruiengine.util.Util;

public class TextField extends EditorField {

	private Text control;

	public TextField() {
	}

	@Override
	protected Control createControl(Composite parent) {

		int style = SWT.BORDER;
		style = fieldConfig.isTextPasswordStyle() ? (style | SWT.PASSWORD) : style;

		control = new Text(parent, style);

		//////////////////////////////////////////////////////////////////////////////////////
		// 读取配置进行设置
		// 设置文本信息
		Optional.ofNullable(fieldConfig.getTextMessage()).ifPresent(o -> control.setMessage(o));

		// 设置文本是否只读
		control.setEditable(!isReadOnly());

		// 设置文本是否显示为密码
		// ^ style

		// 设置字数限定
		if (fieldConfig.getTextLimit() > 0)
			control.setTextLimit(fieldConfig.getTextLimit());

		// 设置为必填

		// 设置输入检验
		Optional.ofNullable(getVerfifyListener()).ifPresent(listener -> control.addListener(SWT.Modify, listener));

		// 设置修改
		control.addListener(SWT.FocusOut, e -> {
			try {
				writeToInput(false);
			} catch (Exception e1) {
				MessageDialog.openError(control.getShell(), "错误", e1.getMessage());
			}
		});
		return control;
	}

	@Override
	public void setValue(Object value) {
		String text = "";
		if (value != null) {
			String format = fieldConfig.getFormat();
			if (format != null && !format.isEmpty()) {
				text = Util.getFormatText(value, format, locale);
			} else {
				text = value.toString();
			}
		}

		control.setText(text);
	}

	@Override
	public Object getValue() {
		return control.getText();
	}

	private ClientListener getVerfifyListener() {
		String restrict = fieldConfig.getTextRestrict();
		String reg = null;
		if (FormField.TEXT_RESTRICT_FLOAT.equals(restrict)) {
			reg = "/^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$/";
			String js = "var handleEvent = function(event) {"
					+ "	var reg1 = /^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$/;"
					+ "	var reg2 = /^-?[1-9]\\d*$/;"
					+ "	if (reg1.test(event.widget.getText())||reg2.test(event.widget.getText())) {"
					+ "		event.widget.setBackground(null);" + "		event.widget.setToolTipText(null);"
					+ "	} else {" + "		event.widget.setBackground([ 255, 152, 0  ]);"
					+ "		event.widget.setToolTipText(\"必须输入整数或小数\");" + "	}" + "};";
			return new ClientListener(js);
		} else if (FormField.TEXT_RESTRICT_INT.equals(restrict)) {
			reg = "/^-?[1-9]\\d*$/";
			String js = "var handleEvent = function(event) {" + "	var reg = " + reg + ";"
					+ "	if (reg.test(event.widget.getText())) {" + "		event.widget.setBackground(null);"
					+ "		event.widget.setToolTipText(null);" + "	} else {"
					+ "		event.widget.setBackground([ 255, 152, 0  ]);"
					+ "		event.widget.setToolTipText(\"必须输入整数\");" + "	}" + "};";
			return new ClientListener(js);
		} else if (FormField.TEXT_RESTRICT_INVALID_CHAR.equals(restrict)) {
			reg = "/[`~!@#$%^&*()_+<>?:\"{},.\\/;'[\\]]/im";
			String js = "var handleEvent = function(event) {" + "	var reg = " + reg + ";"
					+ "	if (!reg.test(event.widget.getText())) {" + "		event.widget.setBackground(null);"
					+ "		event.widget.setToolTipText(null);" + "	} else {"
					+ "		event.widget.setBackground([ 255, 152, 0 ]);"
					+ "		event.widget.setToolTipText(\"输入中包含非法字符\");" + "	}" + "};";
			return new ClientListener(js);
		} else {
			String js = fieldConfig.getTextCustomizedRestrict();
			if (js != null && !js.isEmpty())
				return new ClientListener(js);
			return null;
		}
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
		// 必填检查
		String text = control.getText().trim();
		if (fieldConfig.isRequired() && text.isEmpty()) {
			throw new Exception(fieldConfig.getFieldText() + "必填。");
		}
		// 类型检查
		String restrict = fieldConfig.getTextRestrict();
		if (FormField.TEXT_RESTRICT_FLOAT.equals(restrict)) {
			// 浮点数
			try {
				Float.parseFloat(text);
			} catch (Exception e) {
				throw new Exception(fieldConfig.getFieldText() + "要求输入浮点数。");
			}
		} else if (FormField.TEXT_RESTRICT_INT.equals(restrict)) {
			try {
				Integer.parseInt(text);
			} catch (Exception e) {
				throw new Exception(fieldConfig.getFieldText() + "要求输入整数。");
			}
		} else if (FormField.TEXT_RESTRICT_INVALID_CHAR.equals(restrict)) {
			String a = "`~!@#$%^&*()_+<>?:\"{},.\\/;'[\\]";
			for (int i = 0; i < a.length(); i++) {
				if (text.contains("" + a.charAt(i))) {
					throw new Exception(fieldConfig.getFieldText() + "包含了非法的字符。");
				}
			}
		}

	}


}
