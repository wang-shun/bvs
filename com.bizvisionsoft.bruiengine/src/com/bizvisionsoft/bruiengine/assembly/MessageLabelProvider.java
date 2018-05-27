package com.bizvisionsoft.bruiengine.assembly;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.widgets.grid.GridItem;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.bruiengine.util.BruiColors;
import com.bizvisionsoft.bruiengine.util.BruiColors.BruiColor;
import com.bizvisionsoft.bruiengine.util.Util;

public class MessageLabelProvider extends ColumnLabelProvider {

	private String cName;

	public MessageLabelProvider(String cName) {
		this.cName = cName;
	}

	@Override
	public void update(ViewerCell cell) {
		GridItem gridItem = (GridItem) cell.getViewerRow().getItem();

		Object element = cell.getElement();
		if (element instanceof String) {
			cell.setText((String) element);
			cell.setBackground(BruiColors.getColor(BruiColor.Grey_50));
			// cell.setForeground(getForeground(element));
			// cell.setFont(getFont(element));
			gridItem.setHeight(38);
		} else {
			cell.setText(getMessageText(element));
			gridItem.setHeight(82);
		}
	}

	private String getMessageText(Object element) {
		StringBuffer sb = new StringBuffer();

		// Boolean read = Boolean.TRUE.equals(AUtil.readValue(element, cName, "是否已读",
		// true));
		// String url = (String) AUtil.readValue(element, cName, "链接", null);
		// String receiver = (String) AUtil.readValue(element, cName, "接收者", null);

		// TODO 是否已读

		// 头像
		sb.append("<div style='height:74px;display:inline-flex;'>");
		String sender = (String) AUtil.readValue(element, cName, "发送者", null);
		String senderName = sender.substring(0, sender.indexOf("[")).trim();
		String headPicURL = (String) AUtil.readValue(element, cName, "头像", null);
		if (headPicURL != null) {
			sb.append("<img src=" + headPicURL
					+ " style='margin-top:4px;margin-left:4px;border-radius:28px;width:48px;height:48px;'/>");
		} else {
			try {
				String alpha = Util.getAlphaString(senderName);
				headPicURL = "/bvs/svg?text=" + URLEncoder.encode(alpha, "utf-8") + "&color=ffffff";
				sb.append("<img src=" + headPicURL + " style='margin-top:4px;margin-left:4px;background-color:"
						+ Util.getRandomHTMLDarkColor() + ";border-radius:28px;width:48px;height:48px;'/>");
			} catch (UnsupportedEncodingException e) {
			}
		}

		// 内容区
		sb.append("<div style='margin-left:16px'>");

		String subject = (String) AUtil.readValue(element, cName, "标题", null);
		sb.append("<div class='label_title'>" + subject + "</div>");
		String content = (String) AUtil.readValue(element, cName, "内容", null);
		sb.append("<div style='white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>" + content + "</div>");
		sb.append("<div><span>" + senderName +  "</span>&nbsp;&nbsp;&nbsp;");
		Date sendDate = (Date) AUtil.readValue(element, cName, "发送日期", null);
		sb.append("<span style='font-weight:bolder'>"+new SimpleDateFormat("yyyy/MM/dd").format(sendDate) +"</span></div>");

		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}

}
