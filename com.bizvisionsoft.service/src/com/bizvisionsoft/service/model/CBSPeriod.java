package com.bizvisionsoft.service.model;

import java.util.Calendar;
import java.util.Date;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;

@PersistenceCollection("cbsPeriod")
public class CBSPeriod {

	////////////////////////////////////////////////////////////////////////////////////////////////
	// 基本的一些字段

	/** 标识 Y **/
	@ReadValue
	@WriteValue
	private ObjectId _id;

	@ReadValue
	@WriteValue
	private Double cost;

	@ReadValue
	private Double budget;

	@ReadValue
	private String year;

	@ReadValue
	private String month;

	@ReadValue
	@WriteValue
	private ObjectId cbsItem_id;

	public CBSPeriod setCBSItem_id(ObjectId cbsItem_id) {
		this.cbsItem_id = cbsItem_id;
		return this;
	}

	@WriteValue("期间预算编辑器/year")
	public void setYear(Date year) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(year);
		this.year = "" + cal.get(Calendar.YEAR);
	}

	@WriteValue("期间预算编辑器/month")
	public void setMonth(Date month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(month);
		this.month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
	}
	
	@WriteValue("期间预算编辑器/budget")
	public void writeBudget(String _budget) {
		try {
			budget = Double.parseDouble(_budget);
		} catch (Exception e) {
			throw new RuntimeException("输入类型错误。");
		}
	}

}
