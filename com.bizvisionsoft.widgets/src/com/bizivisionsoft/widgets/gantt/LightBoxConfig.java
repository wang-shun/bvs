package com.bizivisionsoft.widgets.gantt;

import java.util.ArrayList;

public class LightBoxConfig {

	/**
	 * (string) the section's name (according to this name, dhtmlxGantt will take
	 * the section's label from locale.labels collection). For example, for the
	 * 'time' section, dhtmlxGantt will take the label stored as
	 * gantt.locale.labels.section_time.
	 */
	public String name;

	/**
	 * (number) the section's height
	 * 
	 */
	public Integer height;

	/**
	 * (string) the name of a data property that will be mapped to the section
	 */
	public String map_to;

	/**
	 * (duration,select,template,textarea,time) the type of the section's control
	 * (editor)
	 */
	public String type;

	/**
	 * (string) sets the order of date-time selectors in the "duration" or "time"
	 * section
	 */
	public String time_format;

	/**
	 * (Boolean) if set to true, the section will take the focus on opening the
	 * lightbox
	 */
	public Boolean focus;

	/**
	 * (any) the default value of the section's control
	 */
	public Object default_value;

	// // (function) specifies the 'onChange' event handler function for the
	// section's control (for the select control only)
	// public Object onchange;

	/**
	 * (array of objects) defines select options of the control. (for the select
	 * control only) Each object in the array specifies a single option and takes
	 * these properties:
	 * 
	 * key - (string) the option's id. This attribute is compared with the task's
	 * data property to assign select options to tasks label - (string) the option's
	 * label
	 */

	public ArrayList<ArrayList<String>> options;

	/**
	 * (Boolean) if you set the "true" value, the section will be read-only (for the
	 * time, duration controls only)
	 */
	public Boolean readonly;

	// (array,number) sets the range for the year selector
	// Can be set in 2 ways:
	//
	// year_range: [2005, 2025] - a period from 2005 till 2025 year
	// year_range: 10 - a period [current year - 10 years; current year + 10 years]
	//
	public Object year_range;

	// (Boolean) if you set the "true" value, just the 'start Date' selector will be
	// presented in the section. Edited tasks will be specified only by the start
	// date and have the zero duration. Makes sense only for milestones
	// (for the time, duration controls only)
	public Boolean single_date;

	// (Boolean) if set to "true", the options list will contain an additional
	// option that will allow users to set for tasks the root level as the parent.
	// Used in pair with the 'root_label' property (for the parent control only)
	public Boolean allow_root;

	// (string) sets the label for the root-level parent. Used in pair with the
	// 'allow_root' property (for the parent control only)
	public String root_label;

	// (function) sets the filtering function for the select options
	// (for the parent control only)
	// public Object filter;

	// (function) sets the sorting function for the select options
	// (for the parent control only)
	// public Object sort;

	// (function) sets the template for select options (for the parent control only)
	// public Object template;

}
