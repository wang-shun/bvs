package com.bizivisionsoft.widgets.gantt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonObject;

public class Config {

	/**
	 * 返回缺省的配置
	 * 
	 * @return
	 */
	public static Config defaultConfig(boolean readonly) {
		return new Config().setReadonly(readonly);
	}

	/**
	 * defines the date format for the api_date template
	 * 
	 * Default value: "%d-%m-%Y"
	 * 
	 * @see <a href=
	 *      "https://docs.dhtmlx.com/gantt/desktop__date_format.html">https://docs.dhtmlx.com/gantt/desktop__date_format.html</a>
	 * 
	 **/
	public String api_date;

	/**
	 * enables auto scheduling
	 * 
	 * Default value: false
	 **/
	public Boolean auto_scheduling;

	/**
	 * allows or forbids creation of links from parent tasks (projects) to their
	 * children
	 * 
	 * Default value: false
	 **/
	public Boolean auto_scheduling_descendant_links;

	/**
	 * defines whether gantt will do autoscheduling on data loading
	 * 
	 * Default value: true
	 **/
	public Boolean auto_scheduling_initial;

	/**
	 * defines whether the whole project will be moved (see the details below)
	 * 
	 * Default value: true
	 **/
	public Boolean auto_scheduling_move_projects;

	/**
	 * enables the auto scheduling mode, in which tasks will always be rescheduled
	 * to the earliest possible date
	 * 
	 * Default value: false
	 **/
	public Boolean auto_scheduling_strict;

	/**
	 * enables automatic adjusting of the grid's columns to the grid's width
	 * 
	 * Default value: true
	 **/
	public Boolean autofit;

	/**
	 * enables autoscrolling while dragging a task or link out of the current
	 * browser screen
	 * 
	 * Default value: true
	 **/
	public Boolean autoscroll;

	/**
	 * defines the speed of autoscrolling (in ms) while dragging a task or link out
	 * of the current browser screen
	 * 
	 * Default value: 30
	 **/
	public Integer autoscroll_speed;

	/**
	 * forces the Gantt chart to automatically change its size to show all tasks
	 * without scrolling
	 * 
	 * Boolean| string "y" ( or true),"x", "xy"
	 * 
	 * Default value: false
	 **/
	public Object autosize;

	/**
	 * sets the minimum width (in pixels) that the Gantt chart can take in the
	 * horizontal 'autosize' mode
	 **/
	public Integer autosize_min_width;

	/**
	 * enables the dynamic loading in the Gantt chart
	 * 
	 * Default value: false
	 **/
	public Boolean branch_loading;

	/**
	 * specifies that the task has children that are not yet loaded from the backend
	 * 
	 * Default value: "$has_child"
	 **/
	// public String branch_loading_property;

	/**
	 * stores a collection of buttons resided in the left bottom corner of the
	 * lightbox
	 * 
	 * Default value: ["gantt_save_btn", "gantt_cancel_btn"]
	 **/
	public ArrayList<String> buttons_left;

	/**
	 * stores a collection of buttons resided in the right bottom corner of the
	 * lightbox
	 * 
	 * Default value: ["gantt_delete_btn"];
	 **/
	public List<String> buttons_right;

	/**
	 * changes the name of the property that affects binding of a calendar to a
	 * task/group of tasks
	 * 
	 * The default value of the option is "calendar_id".
	 **/
	// public String calendar_property;

	/**
	 * enables cascade deleting of nested tasks and links
	 * 
	 * Default value: true
	 **/
	public Boolean cascade_delete;

	/** configures the columns of the table **/
	public ArrayList<ColumnConfig> columns;
	/**
	 * enables adjusting the task's start and end dates to the work time (while
	 * dragging)
	 * 
	 * Default value: false
	 **/
	public Boolean correct_work_time;

	/**
	 * sets the format of dates in the "Start time" column of the table
	 * 
	 * Default value: "%Y-%m-%d"
	 **/
	public String date_grid;

	/**
	 * sets the format of the time scale (X-Axis)
	 * 
	 * Default value: "%d %M"
	 **/
	public String date_scale;

	/**
	 * 'says' to open the lightbox while creating new events by clicking on the '+'
	 * button
	 * 
	 * Default value: false
	 **/
	public Boolean details_on_create;

	/**
	 * 'says' to open the lightbox after double clicking on a task
	 * 
	 * Default value: true
	 **/
	public Boolean details_on_dblclick;

	/**
	 * enables the possibility to drag the lightbox by the header
	 * 
	 * Default value: true
	 **/
	public Boolean drag_lightbox;

	/**
	 * enables creating dependency links by drag-and-drop
	 * 
	 * Default value: true
	 **/
	public Boolean drag_links;

//	/**
//	 * stores the types of available drag-and-drop modes
//	 * 
//	 * The property is readonly.
//	 * 
//	 * "resize" - the mode when the user drags a task bar to change its duration.
//	 * 
//	 * "progress" - the mode when the user drags the progress knob of a task bar.
//	 * 
//	 * "move" - the mode when the user drags a task bar to replace it.
//	 * 
//	 * "ignore" - the service mode which restricts the drag-and-drop action.
//	 * 
//	 * Default value: { "resize":"resize", "progress":"progress", "move":"move",
//	 * "ignore":"ignore" }
//	 * 
//	 **/
//	public final Object drag_mode = null;

	/**
	 * enables the possibility to move tasks by drag-and-drop
	 * 
	 * Default value: true
	 **/
	public Boolean drag_move;

	/**
	 * enables the possibility to change the task progress by dragging the progress
	 * knob
	 * 
	 * Default value: true
	 **/
	public Boolean drag_progress;

	/**
	 * enables drag and drop of items of project type
	 * 
	 * Default value: false
	 **/
	public Boolean drag_project;

	/**
	 * enables the possibility to resize tasks by drag-and-drop
	 * 
	 * Default value: true
	 **/
	public Boolean drag_resize;

	/**
	 * sets the number of 'gantt.config.duration_unit' units that will correspond to
	 * one unit of the 'duration' data property.
	 * 
	 * Default value: 1
	 **/
	public Integer duration_step;

	/**
	 * sets the duration unit
	 * 
	 * "minute", "hour", "day", "week", "month", "year"
	 * 
	 * Default value: "day"
	 **/
	public String duration_unit;

//	/**
//	 * changes the name of a property that affects the editing ability of
//	 * tasks/links in the read-only Gantt chart
//	 * 
//	 * The default value of the option is "editable".
//	 **/
//	public String editable_property;

	/**
	 * sets the end value of the time scale
	 * 
	 **/
	public Date end_date;

	/**
	 * 'says' the Gantt chart to re-render the scale each time a task doesn't fit
	 * Integero the existing scale Integererval
	 * 
	 * Default value: false
	 **/
	public Boolean fit_tasks;

	/**
	 * makes the grid resizable by dragging the right grid's border
	 * 
	 * Default value: false
	 **/
	public Boolean grid_resize;

//	/**
//	 * sets the name of the attribute of the grid resizer's DOM element
//	 * 
//	 * Default value: "grid_resizer"
//	 **/
//	public String grid_resizer_attribute;
//
//	/**
//	 * sets the name of the attribute of the column resizer's DOM element. The
//	 * attribute presents the column's index
//	 * 
//	 * Default value: "column_index"
//	 **/
//	public String grid_resizer_column_attribute;

	/**
	 * sets the maximum width of the grid
	 * 
	 * Default value: 350
	 **/
	public Integer grid_width;

	/**
	 * shows the critical path in the chart
	 * 
	 * Default value: false
	 **/
	public Boolean highlight_critical_path;

	/**
	 * specifies whether sub-scales shall use the scale_cell_class template by
	 * default
	 * 
	 * Default value: false
	 **/
	public Boolean inherit_scale_class;

	/**
	 * sets whether the timeline area will be initially scrolled to display the
	 * earliest task
	 * 
	 * Default value: true
	 **/
	public Boolean initial_scroll;
	/**
	 * 'says' to preserve the initial grid's width during resizing the columns
	 * within
	 * 
	 * Default value: false
	 **/
	public Boolean keep_grid_width;

	/**
	 * enables keyboard navigation in gantt
	 * 
	 * Default value: true
	 **/
	public Boolean keyboard_navigation;

	/**
	 * enables keyboard navigation by cells
	 * 
	 * Default value: true
	 **/
	public Boolean keyboard_navigation_cells;

	/**
	 * sets the name of the attribute of the task layer's DOM element
	 * 
	 * Default value: "data-layer"
	 **/
	public String layer_attribute;

	/**
	 * specifies the layout object <code>
	 * <dd>gantt.config.layout = {</dd>
	 *	<dd>css: "gantt_container",</dd>
	 *		<dd>rows:[{</dd>
	 *    	<dd>cols: [</dd>
	 *     	<dd>{view: "grid", id: "grid", scrollX:"scrollHor", scrollY:"scrollVer"},</dd>
	 *    	<dd>{resizer: true, width: 1},</dd>
	 *   	<dd>{view: "timeline", id: "timeline", scrollX:"scrollHor", scrollY:"scrollVer"},</dd>
	 *  	<dd>{view: "scrollbar", scroll: "y", id:"scrollVer"}</dd>
	 *	<dd>]},</dd>
	 *<dd>{view: "scrollbar", scroll: "x", id:"scrollHor", height:20}</dd>
	 *<dd> ]</dd>
	 *<dd>};</code>
	 * 
	 **/
	public JsonObject layout;

	/** specifies the lightbox object **/
	public LightBoxConfig lightbox;

	/**
	 * increases the height of the lightbox
	 * 
	 * Default value: 75
	 **/
	public Integer lightbox_additional_height;

	/**
	 * sets the size of the link arrow
	 * 
	 * Default value: 6
	 **/
	public Integer link_arrow_size;

//	/**
//	 * sets the name of the attribute that will specify the id of the link's HTML
//	 * element
//	 * 
//	 * Default value: "link_id"
//	 **/
//	public String link_attribute;

	/**
	 * sets the width of dependency links in the timeline area
	 * 
	 * Default value: 2
	 **/
	public Integer link_line_width;

	/**
	 * sets the width of the area (over the link) sensitive to clicks
	 * 
	 * Default value: 20
	 **/
	public Integer link_wrapper_width;

	/**
	 * stores the types of links dependencies
	 * 
	 * Default value: { "finish_to_start":"0", "start_to_start":"1",
	 * "finish_to_finish":"2", "start_to_finish":"3" }
	 **/
	public JsonObject links;

	/**
	 * sets the minimum width for a column in the timeline area
	 * 
	 * Default value: 70
	 **/
	public Integer min_column_width;

	/**
	 * Sets the minimum duration (in milliseconds) that can be set for a task during
	 * resizing.
	 * 
	 * Default value: 60*60*1000
	 **/
	public Integer min_duration;

	/**
	 * sets the minumum width for the grid (in pixels) while being resized
	 * 
	 * Default value: 70
	 **/
	public Integer min_grid_column_width;

	/**
	 * enables/disables multi-task selection in the Gantt chart
	 * 
	 * Default value: true
	 **/
	public Boolean multiselect;

	/**
	 * specifies whether multi-task selection will be available within one or any
	 * level
	 * 
	 * Default value: false
	 **/
	public Boolean multiselect_one_level;

	/**
	 * openes all branches initially
	 * 
	 * Default value: false
	 **/
	public Boolean open_tree_initially;

	/**
	 * activates the 'branch' mode that allows dragging tasks only within the parent
	 * branch
	 **/
	public Boolean order_branch;

	/**
	 * activates the 'branch' mode that allows dragging tasks within the whole gantt
	 * 
	 * Default value: false
	 **/
	public Boolean order_branch_free;

	/**
	 * preserves the current position of the vertical and horizontal scrolls while
	 * re-drawing the gantt chart
	 * 
	 * Default value: true
	 **/
	public Boolean preserve_scroll;

	/**
	 * specifies whether the gantt container should block the mousewheel event, or
	 * should it be propagated up to the window element
	 * 
	 * Default value: true
	 **/
	public Boolean prevent_default_scroll;

	/**
	 * defines whether the task form will appear from the left/right side of the
	 * screen or near the selected task
	 * 
	 * Default value: true (the event form will appear near the selected event)
	 **/
	public Boolean quick_info_detached;

	/**
	 * stores a collection of buttons resided in the pop-up task's details form
	 * 
	 * Default value: ["icon_delete","icon_edit"]
	 **/
	public ArrayList<String> quickinfo_buttons;

	/**
	 * activates the read-only mode for the Gantt chart
	 * 
	 * Default value: false
	 **/
	public Boolean readonly;

//	/**
//	 * changes the name of a property that affects the read-only behaviour of
//	 * tasks/links
//	 **/
//	public String readonly_property;

	/**
	 * enables the Redo functionality for the gantt
	 * 
	 * Default value: true
	 **/
	public Boolean redo;

	/**
	 * defines a set of working calendars that can be assigned to a specific
	 * resource, e.g. a user
	 * 
	 * @see <a href=
	 *      "https://docs.dhtmlx.com/gantt/api__gantt_resource_calendars_config.html">https://docs.dhtmlx.com/gantt/api__gantt_resource_calendars_config.html</a>
	 **/
	public JsonObject resource_calendars;

	/**
	 * specifies a property of the task object which stores a resource id assiciated
	 * with resourceGrid/resourceTimeline
	 * 
	 * Default value: "owner_id"
	 **/
	public String resource_property;
	/**
	 * 
	 * specifies a name of the dataStore connected to the
	 * resourceGrid/resourceTimeline
	 * 
	 * Default value: "resource"
	 **/
	public String resource_store;

	/**
	 * sets the id of the virtual root element String Number
	 * 
	 * Default value: "0"
	 **/
	public Object root_id;
	/**
	 * enables rounding the task's start and end dates to the nearest scale marks
	 * 
	 * Default value: true
	 **/
	public Boolean round_dnd_dates;

	/**
	 * sets the default height for rows of the table Default value: 35
	 **/
	public Integer row_height;

	/** switches gantt to the right-to-left mode **/
	public Boolean rtl;

	/** sets the height of the time scale and the header of the grid **/
	public Integer scale_height;

	/**
	 * sets the minimal scale unit (in case multiple scales are used) as the
	 * Integererval of leading/closing empty space
	 **/
	public Boolean scale_offset_minimal;

	/** sets the unit of the time scale (X-Axis) **/
	public String scale_unit;
	/**
	 * specifies whether the timeline area shall be scrolled while selecting to
	 * display the selected task
	 **/
	public Boolean scroll_on_click;
	/** set the sizes of the vertical (width) and horizontal (height) scrolls **/
	public Integer scroll_size;
	/** enables selection of tasks in the Gantt chart **/
	public Boolean select_task;
	/**
	 * enables converting server-side dates from UTC to a local time zone (and
	 * backward) while sending data to the server
	 **/
	public Boolean server_utc;

	/** shows the chart (timeline) area of the Gantt chart **/
	public Boolean show_chart;

	/** enables showing error alerts in case of unexpected behavior **/
	public Boolean show_errors;

	/** shows the grid area of the Gantt chart **/
	public Boolean show_grid;

	/** enables/disables displaying links in the Gantt chart **/
	public Boolean show_links;

	/** shows/hides markers on the page **/
	public Boolean show_markers;

	/** enables displaying of the progress inside the task bars **/
	public Boolean show_progress;

	/**
	 * activates/disables the 'quick_info' extension (pop-up task's details form)
	 **/
	public Boolean show_quick_info;

	/** enables/disables displaying column borders in the chart area **/
	public Boolean show_task_cells;

	/** enables showing unscheduled tasks **/
	public Boolean show_unscheduled;

	/** hides non-working time from the time scale **/
	public Boolean skip_off_time;

	/** enables the smart rendering mode for gantt's tasks and links rendering **/
	public Boolean smart_rendering;
	/**
	 * specifies that only visible part of the time scale is rendered on the screen
	 **/
	public Boolean smart_scales;

	/** enables sorting in the table **/
	public Boolean sort;

	/** sets the start value of the time scale **/
	public Date start_date;

	/** sets the start day of weeks **/
	public Boolean start_on_monday;

	/**
	 * generates a background image for the timeline area instead of rendering
	 * actual columns' and rows' lines
	 **/
	public Boolean static_background;

	/** sets the step of the time scale (X-Axis) **/
	public Integer step;

	/** specifies the second time scale(s) **/
	public ArrayList<SubscalesConfig> subscales;

	/**
	 * sets the name of the attribute that will specify the id of the task's HTML
	 * element
	 **/
	public String task_attribute;

	/**
	 * sets the format of the date label in the 'Time period' section of the
	 * lightbox
	 **/
	public String task_date;

	/** sets the height of task bars in the timeline area **/
	public Integer task_height;

	/**
	 * sets the offset (in pixels) of the nearest task from the left border in the
	 * timeline
	 **/
	public Integer task_scroll_offset;

	/** sets the format of the time drop-down selector in the lightbox **/
	public String time_picker;

	/** sets the minimum step (in minutes) for the task's time values **/
	public Integer time_step;

	/** sets the length of time, in milliseconds, before the tooltip hides **/
	public Integer tooltip_hide_timeout;

	/** sets the the right (if positive) offset of the tooltip's position **/
	public Integer tooltip_offset_x;

	/** sets the the top (if positive) offset of the tooltip's position **/
	public Integer tooltip_offset_y;

	/**
	 * sets the timeout in milliseconds before the tooltip is displayed for a task
	 **/
	public Integer tooltip_timeout;

	/**
	 * enables/disables the touch support for the Gantt chart As a string, the
	 * parameter can take the only value - 'force'.
	 **/
	public Object touch;

	/**
	 * defines the time period in milliseconds that is used to differ the long touch
	 * gesture from the scroll gesture
	 * 
	 * number| Boolean
	 **/
	public Object touch_drag;

	/** returns vibration feedback before/after drag and drop on touch devices **/
	public Boolean touch_feedback;

	/**
	 * defines the duration of vibration feedback before/after drag and drop on
	 * touch devices (in milliseconds)
	 **/
	public Integer touch_feedback_duration;

	/** redefines functions responsible for displaying different types of tasks **/
	// public Object type_renderers;//TODO

	/**
	 * stores the names of lightbox's structures (used for different types of tasks)
	 * types : {'task':'task','project':'project','milestone':'milestone'}
	 * 
	 * @see <a href=
	 *      "https://docs.dhtmlx.com/gantt/api__gantt_types_config.html">https://docs.dhtmlx.com/gantt/api__gantt_types_config.html</a>
	 **/
	public Object types;

	/**
	 * enables the Undo functionality for the gantt
	 **/
	public Boolean undo;

	/**
	 * sets the actions that the Undo operation will revert
	 **/
	public JsonObject undo_actions;

	/**
	 * sets the number of steps that should be reverted by the undo method
	 **/
	public Integer undo_steps;

	/**
	 * sets the types of entities for which the Undo operation will be applied
	 **/
	public JsonObject undo_types;

//	/**
//	 * enables WAI-ARIA support to make the component recognizable for screen
//	 * readers
//	 * 
//	 * Default value: true
//	 **/
//	public Boolean wai_aria_attributes;
	/**
	 * enables calculating the duration of tasks in working time instead of calendar
	 * time
	 * 
	 * Default value: false
	 **/
	public Boolean work_time;
	/**
	 * defines date formats that are used to parse data from a data set and to send
	 * data to a server
	 * 
	 * Default value: "%d-%m-%Y %H:%i"
	 **/
	public String xml_date;

	/**
	 * brui 扩展配置，是否显示操作菜单
	 */
	public boolean brui_HeadMenuEnable;

	public boolean brui_RowMenuEnable;
	
	public String brui_initScaletype;

	public Config setReadonly(boolean b) {
		readonly = b;
		return this;
	}

}
