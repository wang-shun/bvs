package com.bizvisionsoft.bruicommons.model;

import java.util.List;

public class FormField extends ModelObject {

	public final static String TYPE_TEXT = "�����ı���";

	public static final String TYPE_COMBO = "����ѡ���";

	public static final String TYPE_RADIO = "��ѡ��";
	
	public static final String TYPE_CHECK = "��ѡ��";
	
	public static final String TYPE_MULTI_CHECK = "���ѡ��";
	
	public static final String TYPE_DATETIME = "����ʱ��ѡ��";
	
	public static final String TYPE_SELECTION = "����ѡ���";

	public static final String TYPE_MULTI_SELECTION = "�������ѡ���";
	
	public static final String TYPE_FILE = "�ļ�ѡ���";
	
	public static final String TYPE_MULTI_FILE = "����ļ�ѡ���";
	
	public static final String TYPE_TEXT_RANGE = "��ֵ��Χ����";

	public static final String TYPE_TEXT_MULTILINE = "�����ı���";

	public final static String TYPE_INLINE = "��";

	public final static String TYPE_PAGE = "��ǩҳ";

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
	/////////////////////////// �ı������е��ֶ�///////////////////////////////////////////

	// ---------------------------------------------------------------------------------
	// �ı����ڲ���ʾ����Ϣ
	private String textMessage;

	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}

	// ---------------------------------------------------------------------------------
	// �ı����Ƿ���ʾΪ����
	private boolean textPasswordStyle;

	public boolean isTextPasswordStyle() {
		return textPasswordStyle;
	}

	public void setTextPasswordStyle(boolean textPasswordStyle) {
		this.textPasswordStyle = textPasswordStyle;
	}

	// ---------------------------------------------------------------------------------
	// �ı����Ƿ���ʾΪ����
	private boolean readOnly;

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	// ---------------------------------------------------------------------------------
	// �ı����Ƿ������д
	private boolean required;

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	// ---------------------------------------------------------------------------------
	// �ı��������޶�
	private int textLimit;

	public int getTextLimit() {
		return textLimit;
	}

	public void setTextLimit(int textLimit) {
		this.textLimit = textLimit;
	}

	// ---------------------------------------------------------------------------------
	// �ı��������޶�
	private String textRestrict;

	public String getTextRestrict() {
		return textRestrict;
	}

	public void setTextRestrict(String textRestrict) {
		this.textRestrict = textRestrict;
	}

	// �Զ�����������
	private String textCustomizedRestrict;

	public String getTextCustomizedRestrict() {
		return textCustomizedRestrict;
	}

	public void setTextCustomizedRestrict(String textCustomizedRestrict) {
		this.textCustomizedRestrict = textCustomizedRestrict;
	}

	public static final String TEXT_RESTRICT_INT = "����";

	public static final String TEXT_RESTRICT_FLOAT = "����";

	public static final String TEXT_RESTRICT_NONE = "���޶�";

	public static final String TEXT_RESTRICT_INVALID_CHAR = "�����а����Ƿ��ַ�";

	// ---------------------------------------------------------------------------------
	// ���ݸ�ʽ��
	private String format;

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}

	///////////////////////////////////////////////////////////////////////////////////

	///////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// Combo��radio���е��ֶ�///////////////////////////////////////////

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

	// Radio����ʽ
	private String radioStyle;

	public static final String RADIO_STYLE_CLASSIC = "��ͳ";

	public static final String RADIO_STYLE_SEGMENT = "����ֶΣ�Ĭ�ϣ�";

	public static final String RADIO_STYLE_VERTICAL = "����";

	public String getRadioStyle() {
		return radioStyle;
	}

	public void setRadioStyle(String radioStyle) {
		this.radioStyle = radioStyle;
	}
	
	//check��ʽ
	private String checkStyle;
	
	public static final String CHECK_STYLE_CLASSIC = "��ͳ";

	public static final String CHECK_STYLE_SWITCH = "���أ�Ĭ�ϣ�";
	
	public String getCheckStyle() {
		return checkStyle;
	}
	
	public void setCheckStyle(String checkStyle) {
		this.checkStyle = checkStyle;
	}

	///////////////////////////////////////////////////////////////////////////////////
	//����ʱ������
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
	
	//ѡ����
	private String selectorAssemblyId;

	
	public String getSelectorAssemblyId() {
		return selectorAssemblyId;
	}
	
	public void setSelectorAssemblyId(String selectorAssemblyId) {
		this.selectorAssemblyId = selectorAssemblyId;
	}

	//�ļ��ֶ�
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
	
	
	//��ֵ��Χ
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
	//���ڲ�ѯ���ֶ�
	public static final String TYPE_QUERY_TEXT = "�ı�����򣨲�ѯ��";
	
	public static final String TYPE_QUERY_COMBO = "����ѡ��򣨲�ѯ��";

	public static final String TYPE_QUERY_CHECK = "��ѡ�򣨲�ѯ��";

	public static final String TYPE_QUERY_MULTI_SELECTION = "�������ѡ��򣨲�ѯ��";

	public static final String TYPE_QUERY_TEXT_RANGE = "��ֵ��Χ���루��ѯ��";

	private String textQueryType;
	
	public static final String TEXT_QUERY_TYPE_NUMBER = "��ֵ";
	
	public static final String TEXT_QUERY_TYPE_STRING = "�ַ���";

	public static final String TYPE_QUERY_DATETIME = "����ʱ�䣨��ѯ��";

	public static final String TYPE_QUERY_DATETIME_RANGE = "����ʱ�䷶Χ����ѯ��";

	public static final String TYPE_QUERY_SELECTION = "����ѡ�񣨲�ѯ��";
	
	public String getTextQueryType() {
		return textQueryType;
	}
	
	public void setTextQueryType(String textQueryType) {
		this.textQueryType = textQueryType;
	}
	
	
}
