package com.bizvisionsoft.bruicommons.model;

import java.util.List;

public class FormField extends ModelObject {

	public final static String TYPE_TEXT = "单行文本框";

	public static final String TYPE_COMBO = "下拉选择框";

	public static final String TYPE_RADIO = "单选框";
	
	public static final String TYPE_CHECK = "复选框";
	
	public static final String TYPE_MULTI_CHECK = "多项勾选框";
	
	public static final String TYPE_DATETIME = "日期时间选择";
	
	public static final String TYPE_SELECTION = "对象选择框";

	public static final String TYPE_MULTI_SELECTION = "多个对象选择框";
	
	public static final String TYPE_FILE = "文件选择框";
	
	public static final String TYPE_MULTI_FILE = "多个文件选择框";
	
	public static final String TYPE_TEXT_RANGE = "数值范围输入";

	public static final String TYPE_TEXT_MULTILINE = "多行文本框";

	public final static String TYPE_INLINE = "行";

	public final static String TYPE_PAGE = "标签页";

	private String id;

	private String name;

	private String description;

	private String text;

	private String type;

	private List<FormField> formFields;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFieldText() {
		return (text == null || text.isEmpty()) ? name : text;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		Object old = this.name;
		this.name = name;
		firePropertyChange("name", old, this.name);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		Object old = this.description;
		this.description = description;
		firePropertyChange("description", old, this.description);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		Object old = this.text;
		this.text = text;
		firePropertyChange("text", old, this.text);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		Object old = this.type;
		this.type = type;
		firePropertyChange("type", old, this.type);
	}

	public List<FormField> getFormFields() {
		return formFields;
	}

	public void setFormFields(List<FormField> formFields) {
		this.formFields = formFields;
	}

	private boolean hasInfoLabel;

	public void setHasInfoLabel(boolean hasInfoLabel) {
		this.hasInfoLabel = hasInfoLabel;
	}

	public boolean isHasInfoLabel() {
		return hasInfoLabel;
	}

	///////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// 文本框特有的字段///////////////////////////////////////////

	// ---------------------------------------------------------------------------------
	// 文本框内部显示的信息
	private String textMessage;

	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

	// ---------------------------------------------------------------------------------
	// 文本框是否显示为密码
	private boolean textPasswordStyle;

	public boolean isTextPasswordStyle() {
		return textPasswordStyle;
	}

	public void setTextPasswordStyle(boolean textPasswordStyle) {
		this.textPasswordStyle = textPasswordStyle;
	}

	// ---------------------------------------------------------------------------------
	// 文本框是否显示为密码
	private boolean readOnly;

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	// ---------------------------------------------------------------------------------
	// 文本框是否必须填写
	private boolean required;

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	// ---------------------------------------------------------------------------------
	// 文本框字数限定
	private int textLimit;

	public int getTextLimit() {
		return textLimit;
	}

	public void setTextLimit(int textLimit) {
		this.textLimit = textLimit;
	}

	// ---------------------------------------------------------------------------------
	// 文本框内容限定
	private String textRestrict;

	public String getTextRestrict() {
		return textRestrict;
	}

	public void setTextRestrict(String textRestrict) {
		this.textRestrict = textRestrict;
	}

	// 自定义输入限制
	private String textCustomizedRestrict;

	public String getTextCustomizedRestrict() {
		return textCustomizedRestrict;
	}

	public void setTextCustomizedRestrict(String textCustomizedRestrict) {
		this.textCustomizedRestrict = textCustomizedRestrict;
	}

	public static final String TEXT_RESTRICT_INT = "整数";

	public static final String TEXT_RESTRICT_FLOAT = "浮点";

	public static final String TEXT_RESTRICT_NONE = "无限定";

	public static final String TEXT_RESTRICT_INVALID_CHAR = "输入中包含非法字符";

	// ---------------------------------------------------------------------------------
	// 数据格式化
	private String format;

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}

	///////////////////////////////////////////////////////////////////////////////////

	///////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// Combo，radio特有的字段///////////////////////////////////////////

	private String optionValue;

	private String optionText;

	public String getOptionText() {
		return optionText;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	// Radio的样式
	private String radioStyle;

	public static final String RADIO_STYLE_CLASSIC = "传统";

	public static final String RADIO_STYLE_SEGMENT = "横向分段（默认）";

	public static final String RADIO_STYLE_VERTICAL = "纵向";

	public String getRadioStyle() {
		return radioStyle;
	}

	public void setRadioStyle(String radioStyle) {
		this.radioStyle = radioStyle;
	}
	
	//check样式
	private String checkStyle;
	
	public static final String CHECK_STYLE_CLASSIC = "传统";

	public static final String CHECK_STYLE_SWITCH = "开关（默认）";
	
	public String getCheckStyle() {
		return checkStyle;
	}
	
	public void setCheckStyle(String checkStyle) {
		this.checkStyle = checkStyle;
	}

	///////////////////////////////////////////////////////////////////////////////////
	//日期时间类型
	private String dateType;
	
	public static final String DATE_TYPE_YEAR = "year";

	public static final String DATE_TYPE_MONTH = "month";

	public static final String DATE_TYPE_DATE = "date";

	public static final String DATE_TYPE_TIME = "time";

	public static final String DATE_TYPE_DATETIME = "datetime";
	
	public String getDateType() {
		return dateType;
	}
	
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	
	//选择器
	private String selectorAssemblyId;

	
	public String getSelectorAssemblyId() {
		return selectorAssemblyId;
	}
	
	public void setSelectorAssemblyId(String selectorAssemblyId) {
		this.selectorAssemblyId = selectorAssemblyId;
	}

	//文件字段
	private String fileNamespace;

	public String getFileNamespace() {
		return fileNamespace;
	}
	
	public void setFileNamespace(String fileNamespace) {
		this.fileNamespace = fileNamespace;
	}
	
	private int maxFileSize;
	
	public int getMaxFileSize() {
		return maxFileSize;
	}
	
	public void setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
	
	private int timeLimit;
	
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	public int getTimeLimit() {
		return timeLimit;
	}
	
	private String fileFilerExts;
	
	public String getFileFilerExts() {
		return fileFilerExts;
	}
	
	public void setFileFilerExts(String fileFilerExts) {
		this.fileFilerExts = fileFilerExts;
	}
	
	
	//数值范围
	public Integer textRangeMinValue;
	
	public Integer textRangeMaxValue;
	
	public Boolean textRangeLimitMinValue;
	
	public Boolean textRangeLimitMaxValue;
	
	public String textMessage2;
	
	public String getTextMessage2() {
		return textMessage2;
	}
	
	public void setTextMessage2(String textMessage2) {
		this.textMessage2 = textMessage2;
	}
	
	public Boolean getTextRangeLimitMaxValue() {
		return textRangeLimitMaxValue;
	}
	
	public Boolean getTextRangeLimitMinValue() {
		return textRangeLimitMinValue;
	}
	
	public Integer getTextRangeMaxValue() {
		return textRangeMaxValue;
	}
	
	public Integer getTextRangeMinValue() {
		return textRangeMinValue;
	}
	
	public void setTextRangeLimitMaxValue(Boolean textRangeLimitMaxValue) {
		this.textRangeLimitMaxValue = textRangeLimitMaxValue;
	}
	
	public void setTextRangeLimitMinValue(Boolean textRangeLimitMinValue) {
		this.textRangeLimitMinValue = textRangeLimitMinValue;
	}
	
	public void setTextRangeMaxValue(Integer textRangeMaxValue) {
		this.textRangeMaxValue = textRangeMaxValue;
	}
	
	public void setTextRangeMinValue(Integer textRangeMinValue) {
		this.textRangeMinValue = textRangeMinValue;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//用于查询的字段
	public static final String TYPE_QUERY_TEXT = "文本输入框（查询）";
	
	public static final String TYPE_QUERY_COMBO = "下拉选择框（查询）";

	public static final String TYPE_QUERY_CHECK = "多选框（查询）";

	public static final String TYPE_QUERY_MULTI_SELECTION = "多个对象选择框（查询）";

	public static final String TYPE_QUERY_TEXT_RANGE = "数值范围输入（查询）";

	private String textQueryType;
	
	public static final String TEXT_QUERY_TYPE_NUMBER = "数值";
	
	public static final String TEXT_QUERY_TYPE_STRING = "字符串";

	public static final String TYPE_QUERY_DATETIME = "日期时间（查询）";

	public static final String TYPE_QUERY_DATETIME_RANGE = "日期时间范围（查询）";

	public static final String TYPE_QUERY_SELECTION = "对象选择（查询）";
	
	public String getTextQueryType() {
		return textQueryType;
	}
	
	public void setTextQueryType(String textQueryType) {
		this.textQueryType = textQueryType;
	}
	
	
}
