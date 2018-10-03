package com.bizvisionsoft.bruiengine.assembly;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.widgets.grid.GridItem;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.bruiengine.util.BruiColors;
import com.bizvisionsoft.bruiengine.util.BruiColors.BruiColor;
import com.bizvisionsoft.service.tools.Formatter;

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

		Boolean read = Boolean.TRUE.equals(AUtil.readValue(element, cName, "是否已读", false));
		// String url = (String) AUtil.readValue(element, cName, "链接", null);
		// String receiver = (String) AUtil.readValue(element, cName, "接收者", null);

		// 头像
		if (read) {
			sb.append("<div style='height:74px;display:block;cursor:pointer;'>");
		} else {
			sb.append("<div style='font-weight:bold;height:74px;display:block;cursor:pointer;'>");
		}
		String sender = (String) AUtil.readValue(element, cName, "发送者", null);
		String senderName = sender;// .substring(0, sender.indexOf("[")).trim();
		String headPicURL = (String) AUtil.readValue(element, cName, "头像", null);
		if (headPicURL != null) {
			sb.append("<img src=" + headPicURL
					+ " style='float:left;margin-top:4px;margin-left:4px;border-radius:28px;width:48px;height:48px;'/>");
		} else {
			try {
				String alpha = Formatter.getAlphaString(senderName);
				headPicURL = "/bvs/svg?text=" + URLEncoder.encode(alpha, "utf-8") + "&color=ffffff";
				sb.append(
						"<img src=" + headPicURL + " style='float:left;margin-top:4px;margin-left:4px;background-color:"
								+ BruiColors.getHTMLDarkColor(alpha) + ";border-radius:28px;width:48px;height:48px;'/>");
			} catch (UnsupportedEncodingException e) {
			}
		}

		sb.append("<div style='float:right;'><span>" + senderName + "</span>&nbsp;&nbsp;&nbsp;");
		Date sendDate = (Date) AUtil.readValue(element, cName, "发送日期", null);
		sb.append("<span>" + Formatter.getString(sendDate) + "</span></div>");

		// 内容区
		sb.append("<div style='margin-left:64px'>");

		String subject = (String) AUtil.readValue(element, cName, "标题", null);
		sb.append("<div class='label_subhead''>" + subject + "</div>");
		String content = (String) AUtil.readValue(element, cName, "内容", null);

		sb.append("<div style='height:38px;" // 2行文字高度
				+ "white-space:normal; word-break:break-all;" //
				+ "text-overflow: ellipsis;"//
				+ "text-overflow:-o-ellipsis-lastline;"//
				+ "overflow: hidden;"//
				+ "display: -webkit-box;"//
				+ "-webkit-box-orient:vertical;"//
				+ "-webkit-line-clamp:2;"// 谷歌上行显示省略号
				+ "'>" + content + "</div>");

		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}

}
