package com.bizvisionsoft.service.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

	public static Date str_date(String str) {
		if (str == null)
			return null;
		String _str = str.replace("Z", " UTC");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
		try {
			return format.parse(_str);
		} catch (ParseException e) {
			return null;
		}
	}

}
