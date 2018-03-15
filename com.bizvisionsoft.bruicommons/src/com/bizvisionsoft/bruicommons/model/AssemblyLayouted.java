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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(int horizontalAlignment) {
		Object old = this.horizontalAlignment;
		this.horizontalAlignment = horizontalAlignment;
		firePropertyChange("horizontalAlignment", old, this.horizontalAlignment);
	}

	public int getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(int verticalAlignment) {
		Object old = this.verticalAlignment;
		this.verticalAlignment = verticalAlignment;
		firePropertyChange("verticalAlignment", old, this.verticalAlignment);
	}

	public boolean isGrabExcessHorizontalSpace() {
		return grabExcessHorizontalSpace;
	}

	public void setGrabExcessHorizontalSpace(boolean grabExcessHorizontalSpace) {
		Object old = this.grabExcessHorizontalSpace;
		this.grabExcessHorizontalSpace = grabExcessHorizontalSpace;
		firePropertyChange("grabExcessHorizontalSpace", old, this.grabExcessHorizontalSpace);
	}

	public boolean isGrabExcessVerticalSpace() {
		return grabExcessVerticalSpace;
	}

	public void setGrabExcessVerticalSpace(boolean grabExcessVerticalSpace) {
		Object old = this.grabExcessVerticalSpace;
		this.grabExcessVerticalSpace = grabExcessVerticalSpace;
		firePropertyChange("grabExcessVerticalSpace", old, this.grabExcessVerticalSpace);
	}

	public int getVerticalSpan() {
		return verticalSpan;
	}

	public void setVerticalSpan(int verticalSpan) {
		Object old = this.verticalSpan;
		this.verticalSpan = verticalSpan;
		firePropertyChange("verticalSpan", old, this.verticalSpan);
	}

	public int getHorizontalSpan() {
		return horizontalSpan;
	}

	public void setHorizontalSpan(int horizontalSpan) {
		Object old = this.horizontalSpan;
		this.horizontalSpan = horizontalSpan;
		firePropertyChange("horizontalSpan", old, this.horizontalSpan);
	}

	public int getWidthHint() {
		return widthHint;
	}

	public void setWidthHint(int widthHint) {
		Object old = this.widthHint;
		this.widthHint = widthHint;
		firePropertyChange("widthHint", old, this.widthHint);
	}

	public int getHeightHint() {
		return heightHint;
	}

	public void setHeightHint(int heightHint) {
		Object old = this.heightHint;
		this.heightHint = heightHint;
		firePropertyChange("heightHint", old, this.heightHint);
	}

}
