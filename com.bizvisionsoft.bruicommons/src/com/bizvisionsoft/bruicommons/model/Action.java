package com.bizvisionsoft.bruicommons.model;

import java.util.List;

public class Action extends ModelObject {

	public final static String TYPE_INSERT = "insert";

	public final static String TYPE_EDIT = "edit";

	public final static String TYPE_DELETE = "delete";

	public final static String TYPE_QUERY = "query";

	public final static String TYPE_CUSTOMIZED = "customized";

	public static final String TYPE_SWITCHCONTENT = "switch";

	public static final String TYPE_OPENPAGE = "openpage";

	public static final String TYPE_INSERT_SUBITEM = "insertsub";

	public static final String TYPE_EXPORT = "export";

	public static final String TYPE_SETTING = "setting";
	
	public static final String TYPE_SYSTEM_SETTING = "system_setting";

	public static final String TYPE_REPORT = "report";

	private String id;

	private String name;

	private String text;

	private String description;

	private String tooltips;

	private String image;

	private String imageDisabled;

	private String bundleId;

	private String className;

	private String switchContentToAssemblyId;

	private boolean openContent;

	private boolean propagate;

	private boolean forceText;

	private String style;

	private List<Action> children;

	private String editorAssemblyId;

	private boolean editorAssemblyEditable;

	private boolean objectBehavier;

	private boolean actionBehavier;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		this.description = description;

	}

	public String getImage() {
		return image;
	}

	public String getImageDisabled() {
		return imageDisabled;
	}

	public String getTooltips() {
		return tooltips;
	}

	public void setTooltips(String tooltips) {
		this.tooltips = tooltips;

	}

	public void setImage(String image) {
		this.image = image;

	}

	public void setImageDisabled(String imageDisabled) {
		this.imageDisabled = imageDisabled;

	}

	public List<Action> getChildren() {
		return children;
	}

	public void setChildren(List<Action> children) {
		this.children = children;

	}

	public String getBundleId() {
		return bundleId;
	}

	public String getClassName() {
		return className;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		Object old = this.text;
		this.text = text;
		firePropertyChange("text", old, this.text);

	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public void setClassName(String className) {
		this.className = className;

	}

	public String getSwitchContentToAssemblyId() {
		return switchContentToAssemblyId;
	}

	public void setSwitchContentToAssemblyId(String switchContentToAssemblyId) {
		this.switchContentToAssemblyId = switchContentToAssemblyId;

	}

	public void setPropagate(boolean propagate) {
		this.propagate = propagate;

	}

	public boolean isPropagate() {
		return propagate;
	}

	public void setForceText(boolean forceText) {
		this.forceText = forceText;

	}

	public boolean isForceText() {
		return forceText;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;

	}

	public String getEditorAssemblyId() {
		return editorAssemblyId;
	}

	public void setEditorAssemblyId(String editorAssemblyId) {
		this.editorAssemblyId = editorAssemblyId;

	}

	public boolean isEditorAssemblyEditable() {
		return editorAssemblyEditable;
	}

	public void setEditorAssemblyEditable(boolean editorAssemblyEditable) {
		this.editorAssemblyEditable = editorAssemblyEditable;

	}

	public boolean isObjectBehavier() {
		return objectBehavier;
	}

	public boolean isActionBehavier() {
		return actionBehavier;
	}

	public void setActionBehavier(boolean actionBehavier) {
		this.actionBehavier = actionBehavier;
	}

	public void setObjectBehavier(boolean objectBehavier) {
		this.objectBehavier = objectBehavier;

	}

	public boolean isOpenContent() {
		return openContent;
	}

	public void setOpenContent(boolean openContent) {
		this.openContent = openContent;

	}

	@Override
	public String toString() {
		return name;
	}

	private String type;

	public void setType(String type) {
		this.type = type;

	}

	public String getType() {
		return type;
	}

	private String createActionNewInstanceBundleId;

	private String createActionNewInstanceClassName;

	public String getCreateActionNewInstanceBundleId() {
		return createActionNewInstanceBundleId;
	}

	public String getCreateActionNewInstanceClassName() {
		return createActionNewInstanceClassName;
	}

	public void setCreateActionNewInstanceBundleId(String createActionNewInstanceBundleId) {
		this.createActionNewInstanceBundleId = createActionNewInstanceBundleId;

	}

	public void setCreateActionNewInstanceClassName(String createActionNewInstanceClassName) {
		this.createActionNewInstanceClassName = createActionNewInstanceClassName;

	}

	private String openPageName;

	public String getOpenPageName() {
		return openPageName;
	}

	public void setOpenPageName(String openPageName) {
		this.openPageName = openPageName;

	}

	private String role;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	private String excludeRole;

	public void setExcludeRole(String excludeRole) {
		this.excludeRole = excludeRole;
	}

	public String getExcludeRole() {
		return excludeRole;
	}

	private String budgetBundleId;

	private String budgetClassName;

	private String budgetServiceName;

	private int horizontalSpan;

	private int verticalSpan;

	public String getBudgetBundleId() {
		return budgetBundleId;
	}

	public void setBudgetBundleId(String budgetBundleId) {
		this.budgetBundleId = budgetBundleId;
	}

	public String getBudgetClassName() {
		return budgetClassName;
	}

	public void setBudgetClassName(String budgetClassName) {
		this.budgetClassName = budgetClassName;
	}

	public String getBudgetServiceName() {
		return budgetServiceName;
	}

	public void setBudgetServiceName(String budgetServiceName) {
		this.budgetServiceName = budgetServiceName;
	}

	public void setHorizontalSpan(int horizontalSpan) {
		this.horizontalSpan = horizontalSpan;
	}

	public void setVerticalSpan(int verticalSpan) {
		this.verticalSpan = verticalSpan;
	}

	public int getHorizontalSpan() {
		return horizontalSpan;
	}

	public int getVerticalSpan() {
		return verticalSpan;
	}

	private String reportJQ;

	private String reportOutputType;

	private String reportTemplate;

	private String reportFileName;

	public String getReportJQ() {
		return reportJQ;
	}

	public void setReportJQ(String reportJQ) {
		this.reportJQ = reportJQ;
	}

	public String getReportOutputType() {
		return reportOutputType;
	}

	public void setReportOutputType(String reportOutputType) {
		this.reportOutputType = reportOutputType;
	}

	public String getReportTemplate() {
		return reportTemplate;
	}

	public void setReportTemplate(String reportTemplate) {
		this.reportTemplate = reportTemplate;
	}

	public String getReportFileName() {
		return reportFileName;
	}

	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}

	private String parameters;
	
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	
	public String getParameters() {
		return parameters;
	}
	
	private String passParametersToAssembly;
	
	public String getPassParametersToAssembly() {
		return passParametersToAssembly;
	}
	
	public void setPassParametersToAssembly(String passParametersToAssembly) {
		this.passParametersToAssembly = passParametersToAssembly;
	}
	
	private String sysSettingParameter;
	
	public String getSysSettingParameter() {
		return sysSettingParameter;
	}
	
	public void setSysSettingParameter(String sysSettingParameter) {
		this.sysSettingParameter = sysSettingParameter;
	}
	
	private String shortcutKey;
	
	public void setShortcutKey(String shortcutKey) {
		this.shortcutKey = shortcutKey;
	}
	
	public String getShortcutKey() {
		return shortcutKey;
	}
}
