package com.bizivisionsoft.widgets.gantt;

public class ColumnConfig {

	public String align; // ('left', 'center', 'right') the horizontal title alignment

	public Boolean hide;// (Boolean) hides/shows a column
	
	public String label;// (string) the title of the column
	
	public String name ;//(function) the column's id. The name 'add' allows you
	// to add a column with the '+' sign
	
	public Boolean resize;// (Boolean) enables a possibility to resize a column by dragging the column's
							// border
	
	// private Object template; // (function) the data template
	
	public Boolean tree;// (Boolean) indicates that the related column should display the tree
	
	public Object width;// (number) the width of the column

}
