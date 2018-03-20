package com.bizivisionsoft.widgets.datetime;

import java.util.Date;

import org.eclipse.swt.widgets.Event;

public class DateTimeEvent extends Event {

	private Date endDate;

	public DateTimeEvent(Date date, Date endDate) {
		this.data = date;
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Date getDate() {
		return (Date) data;
	}

}
