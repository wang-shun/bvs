package com.bizvisionsoft.service.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.Exclude;
import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.service.tools.Util;

public class WorkTime {

	@Behavior({ "��������/ɾ��", "��������/�༭" })
	private boolean behavior = true;
	
	@ReadValue(ReadValue.TYPE)
	@Exclude
	private String typeName = "����ʱ��";

	@ReadValue
	@WriteValue
	public ObjectId _id;

	@ReadValue
	@WriteValue
	@Label
	public String name;

	@ReadValue
	@WriteValue
	public List<String> day;

	@ReadValue
	@WriteValue
	public Date date;

	@ReadValue
	@WriteValue
	public boolean workingDay;

	@ReadValue
	@WriteValue
	public Date from1;

	@ReadValue
	@WriteValue
	public Date to1;

	@ReadValue
	@WriteValue
	public Date from2;

	@ReadValue
	@WriteValue
	public Date to2;

	@ReadValue
	@WriteValue
	public Date from3;

	@ReadValue
	@WriteValue
	public Date to3;

	@Override
	@ReadValue("��������/id")
	public String toString() {
		// ����������
		if (date != null && !workingDay) {
			return "�ǹ�����";
		}

		if (day != null && !day.isEmpty()) {
			if (from1 != null || to1 != null || from2 != null || to2 != null || from3 != null || to3 != null)
				return "����ʱ��";
		}
		// String text = name;
		// if (date != null) {
		// text += " " + new SimpleDateFormat(Util.DATE_FORMAT_DATE).format(date) +
		// (workingDay ? "������" : "�ǹ�����");
		// }
		return "����������";
	}

	@ReadValue("��������/description")
	public String getDescription() {
		String text = "";
		if (date != null)
			text += " " + new SimpleDateFormat(Util.DATE_FORMAT_DATE).format(date) + (workingDay ? "������" : "�ǹ�����");

		if (day != null && !day.isEmpty())
			text += " ÿ�ܹ���: " + day;

		if (from1 != null)
			text += " �����ϰ�: " + new SimpleDateFormat(Util.DATE_FORMAT_TIME).format(from1);
		if (to1 != null)
			text += " �����°�: " + new SimpleDateFormat(Util.DATE_FORMAT_TIME).format(to1);
		if (from2 != null)
			text += " �����ϰ�: " + new SimpleDateFormat(Util.DATE_FORMAT_TIME).format(from2);
		if (to2 != null)
			text += " �����°�: " + new SimpleDateFormat(Util.DATE_FORMAT_TIME).format(to2);
		if (from3 != null)
			text += " ����ϰ�: " + new SimpleDateFormat(Util.DATE_FORMAT_TIME).format(from3);
		if (to3 != null)
			text += " ����°�" + new SimpleDateFormat(Util.DATE_FORMAT_TIME).format(to3);

		return text.trim();
	}

	public WorkTime set_id(ObjectId _id) {
		this._id = _id;
		return this;
	}

	public ObjectId get_id() {
		return _id;
	}
}
