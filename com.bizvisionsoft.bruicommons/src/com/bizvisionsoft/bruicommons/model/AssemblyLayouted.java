package com.bizvisionsoft.bruicommons.model;

public class AssemblyLayouted extends ModelObject {

	private String id;

	private int horizontalAlignment;

	private int verticalAlignment;

	private boolean grabExcessHorizontalSpace;

	private boolean grabExcessVerticalSpace;

	private int verticalSpan;

	private int horizontalSpan;

	private int widthHint;

	private int heightHint;

	private String layoutName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		Object old = this.layoutName;
		this.layoutName = layoutName;
		firePropertyChange("layoutName", old, this.layoutName);
	}

	public int getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(int horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	public int getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(int verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}

	public boolean isGrabExcessHorizontalSpace() {
		return grabExcessHorizontalSpace;
	}

	public void setGrabExcessHorizontalSpace(boolean grabExcessHorizontalSpace) {
		this.grabExcessHorizontalSpace = grabExcessHorizontalSpace;
	}

	public boolean isGrabExcessVerticalSpace() {
		return grabExcessVerticalSpace;
	}

	public void setGrabExcessVerticalSpace(boolean grabExcessVerticalSpace) {
		this.grabExcessVerticalSpace = grabExcessVerticalSpace;
	}

	public int getVerticalSpan() {
		return verticalSpan;
	}

	public void setVerticalSpan(int verticalSpan) {
		this.verticalSpan = verticalSpan;
	}

	public int getHorizontalSpan() {
		return horizontalSpan;
	}

	public void setHorizontalSpan(int horizontalSpan) {
		this.horizontalSpan = horizontalSpan;
	}

	public int getWidthHint() {
		return widthHint;
	}

	public void setWidthHint(int widthHint) {
		this.widthHint = widthHint;
	}

	public int getHeightHint() {
		return heightHint;
	}

	public void setHeightHint(int heightHint) {
		this.heightHint = heightHint;
	}

}
