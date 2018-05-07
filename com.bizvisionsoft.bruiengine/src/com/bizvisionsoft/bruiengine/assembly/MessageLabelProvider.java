package com.bizvisionsoft.bruiengine.assembly;

import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.widgets.grid.GridItem;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.bruiengine.util.BruiColors;
import com.bizvisionsoft.bruiengine.util.BruiColors.BruiColor;

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
			 cell.setBackground(BruiColors.getColor(BruiColor.Grey_200));
			// cell.setForeground(getForeground(element));
			// cell.setFont(getFont(element));
			gridItem.setHeight(38);
		} else {
			cell.setText(getMessageText(element));
			gridItem.setHeight(72);
		}
	}

	private String getMessageText(Object element) {
		String desc = (String) AUtil.readValue(element, cName, "简短说明", null);
		if(desc!=null) {
			return desc;
		}
		String sender = (String) AUtil.readValue(element, cName, "发送者", null);
		String content = (String) AUtil.readValue(element, cName, "内容", null);
		Date sendDate = (Date) AUtil.readValue(element, cName, "发送日期", null);
		Boolean read = Boolean.TRUE.equals(AUtil.readValue(element, cName, "是否已读", true));
		String subject = (String) AUtil.readValue(element, cName, "标题", null);
		String url = (String) AUtil.readValue(element, cName, "链接", null);
		String receiver = (String) AUtil.readValue(element, cName, "接收者", null);
		
		
		return subject;
	}

}
