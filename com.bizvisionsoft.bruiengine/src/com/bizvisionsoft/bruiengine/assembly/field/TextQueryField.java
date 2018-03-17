package com.bizvisionsoft.bruiengine.assembly.field;

import java.util.Optional;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.scripting.ClientListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.mongodb.BasicDBObject;

public class TextQueryField extends EditorField {

	private static final String GT = "大于";

	private static final String GTE = "大于等于";

	private static final String EQ = "等于";

	private static final String LTE = "小于等于";

	private static final String LT = "小于";

	private static final String NUL = "空";

	private static final String C = "包含";

	private Text control;

	private Combo operator;

	public TextQueryField() {
	}

	@Override
	protected Control createControl(Composite parent) {

		Composite pane = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 8;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		pane.setLayout(layout);

		operator = new Combo(pane, SWT.READ_ONLY | SWT.BORDER);
		if (FormField.TEXT_QUERY_TYPE_NUMBER.endsWith(fieldConfig.getTextQueryType())) {
			operator.add(GT);
			operator.add(GTE);
			operator.add(EQ);
			operator.add(LTE);
			operator.add(LT);
			operator.select(2);
		} else {
			operator.add(C);
			operator.add(EQ);
			operator.select(0);
		}
		operator.add(NUL);

		control = new Text(pane, SWT.BORDER);
		control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

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
		return pane;
	}

	@Override
	public void setValue(Object value) {
	}

	@Override
	public Object getValue() {
		String op = operator.getText();
		String value = control.getText().trim();
		if (value.isEmpty()) {
			return null;
		}

		if (FormField.TEXT_QUERY_TYPE_NUMBER.endsWith(fieldConfig.getTextQueryType())) {
			float v = Float.parseFloat(value);
			if (EQ.equals(op)) {
				return new BasicDBObject("$eq", v);
			} else if (GT.equals(op)) {
				return new BasicDBObject("$gt", v);
			} else if (GTE.equals(op)) {
				return new BasicDBObject("$gte", v);
			} else if (LT.equals(op)) {
				return new BasicDBObject("$lt", v);
			} else if (LTE.equals(op)) {
				return new BasicDBObject("$lte", v);
			}
		} else {
			if (C.equals(op)) {
				return Pattern.compile(value + "$", Pattern.CASE_INSENSITIVE);
			} else if (EQ.equals(op)) {
				return new BasicDBObject("$eq", value);
			}
		}
		return null;
	}

	private ClientListener getVerfifyListener() {
		if (FormField.TEXT_QUERY_TYPE_NUMBER.endsWith(fieldConfig.getTextQueryType())) {
			String js = "var handleEvent = function(event) {"
					+ "	var reg1 = /^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$/;"
					+ "	var reg2 = /^-?[1-9]\\d*$/;"
					+ "	if (reg1.test(event.widget.getText())||reg2.test(event.widget.getText())) {"
					+ "		event.widget.setBackground(null);" + "		event.widget.setToolTipText(null);"
					+ "	} else {" + "		event.widget.setBackground([ 255, 152, 0  ]);"
					+ "		event.widget.setToolTipText(\"必须输入整数或小数\");" + "	}" + "};";
			return new ClientListener(js);
		} else {
			String js = "var handleEvent = function(event) {" + "	var reg = "
					+ "/[`~!@#$%^&*()_+<>?:\"{},.\\/;'[\\]]/im" + ";" + "	if (!reg.test(event.widget.getText())) {"
					+ "		event.widget.setBackground(null);" + "		event.widget.setToolTipText(null);"
					+ "	} else {" + "		event.widget.setBackground([ 255, 152, 0 ]);"
					+ "		event.widget.setToolTipText(\"输入中包含非法字符\");" + "	}" + "};";
			return new ClientListener(js);
		}
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
		String text = control.getText().trim();
		if (text.isEmpty()) {
			return;
		}
		if (FormField.TEXT_QUERY_TYPE_NUMBER.endsWith(fieldConfig.getTextQueryType())) {
			try {
				Float.parseFloat(text);
			} catch (Exception e) {
				throw new Exception(fieldConfig.getFieldText() + "要求输入数值。");
			}
		} else {
			String a = "`~!@#$%^&*()_+<>?:\"{},.\\/;'[\\]";
			for (int i = 0; i < a.length(); i++) {
				if (text.contains("" + a.charAt(i))) {
					throw new Exception(fieldConfig.getFieldText() + "包含了非法的字符。");
				}
			}
		}
	}

}
