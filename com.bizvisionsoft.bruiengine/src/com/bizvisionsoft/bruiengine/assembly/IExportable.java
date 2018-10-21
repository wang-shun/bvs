package com.bizvisionsoft.bruiengine.assembly;

public interface IExportable {

	void export();

	/**
	 * 设置导出按钮名称
	 * 
	 * @param exportActionText
	 */
	void setExportActionText(String exportActionText);

	/**
	 * 获得导出按钮名称
	 * 
	 * @return
	 */
	String getExportActionText();
}
