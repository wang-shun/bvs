package com.bizvisionsoft.service.tools;

public class Checker {

	public static void checkNull(Object obj) {
		if (obj == null)
			throw new IllegalArgumentException("������Ϊ��ֵ(null)");
	}

	public static void checkNullorEmpty(String obj) {
		if (obj == null || obj.isEmpty())
			throw new IllegalArgumentException("������Ϊ��ֵ(null)����ַ���");
	}

}
