package com.bizvisionsoft.bruiengine.assembly;

public interface IExportable {

	void export();

	/**
	 * ���õ�����ť����
	 * 
	 * @param exportActionText
	 */
	void setExportActionText(String exportActionText);

	/**
	 * ��õ�����ť����
	 * 
	 * @return
	 */
	String getExportActionText();
}
