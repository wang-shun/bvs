(function() {
	'use strict';

	rap.registerTypeHandler("bizvision.dhtmlxscheduler", {

		factory : function(properties) {
			return new bizvision.dhtmlxscheduler(properties);
		},

		destructor : "destroy",

		properties : [ "inputData", "sectionData" ],

		methods : [ "addListener", "removeListener" ]

	});

	if (!window.bizvision) {
		window.bizvision = {};
	}

	bizvision.dhtmlxscheduler = function(properties) {
		bindAll(this, [ "layout", "onReady", "onSend", "onRender", "destroy",
				"renderScheduler" ]);
		this.parent = rap.getObject(properties.parent);
		this.type = properties.type;

		this.element = document.createElement("div");
		this.element.style.width = "100%";
		this.element.style.height = "100%";
		this.element.className = "dhx_cal_container";
		this.parent.append(this.element);

		var dhx_cal_navline = document.createElement("div");
		dhx_cal_navline.className = "dhx_cal_navline";

		var dhx_cal_prev_button = document.createElement("div");
		dhx_cal_prev_button.className = "dhx_cal_prev_button";
		dhx_cal_prev_button.innerHTML = "&nbsp;";
		dhx_cal_navline.append(dhx_cal_prev_button);

		var dhx_cal_next_button = document.createElement("div");
		dhx_cal_next_button.className = "dhx_cal_next_button";
		dhx_cal_next_button.innerHTML = "&nbsp;";
		dhx_cal_navline.append(dhx_cal_next_button);

		var dhx_cal_today_button = document.createElement("div");
		dhx_cal_today_button.className = "dhx_cal_today_button";
		dhx_cal_navline.append(dhx_cal_today_button);

		var dhx_cal_date = document.createElement("div");
		dhx_cal_date.className = "dhx_cal_date";
		dhx_cal_navline.append(dhx_cal_date);

		var day_tab = document.createElement("div");
		day_tab.className = "dhx_cal_tab";
		day_tab.setAttribute("name", "day_tab");
		day_tab.style.right = "204px";
		dhx_cal_navline.append(day_tab);

		var week_tab = document.createElement("div");
		week_tab.className = "dhx_cal_tab";
		week_tab.setAttribute("name", "week_tab");
		week_tab.style.right = "140px";
		dhx_cal_navline.append(week_tab);

		var month_tab = document.createElement("div");
		month_tab.className = "dhx_cal_tab";
		month_tab.setAttribute("name", "month_tab");
		month_tab.style.right = "76px";
		dhx_cal_navline.append(month_tab);
		
		if(this.type == "timeline"){
			var timeline_tab = document.createElement("div");
			timeline_tab.className = "dhx_cal_tab";
			timeline_tab.setAttribute("name", "timeline_tab");
			timeline_tab.style.right = "10px";
			dhx_cal_navline.append(timeline_tab);
		}

		this.element.append(dhx_cal_navline);

		var dhx_cal_header = document.createElement("div");
		dhx_cal_header.className = "dhx_cal_header";
		this.element.append(dhx_cal_header);

		var dhx_cal_data = document.createElement("div");
		dhx_cal_data.className = "dhx_cal_data";
		this.element.append(dhx_cal_data);

		this.scheduler = Scheduler.getSchedulerInstance()

		this.parent.addListener("Dispose", this.destroy);
		this.parent.addListener("Resize", this.layout);
		rap.on("render", this.onRender);
	};

	bizvision.dhtmlxscheduler.prototype = {

		onRender : function() {
			if (this.element.parentNode) {
				rap.off("render", this.onRender);
				if (this.type == "scheduler") {
					this.renderScheduler();
				} else if (this.type == "timeline") {
					this.renderTimeline();
				}
			}
		},

		renderTimeline : function() {
			var scheduler = this.scheduler;

			scheduler.locale.labels.timeline_tab = "表格";
			scheduler.config.readonly = true;
			scheduler.locale.labels.section_custom = "Section";
			scheduler.config.xml_date = "%Y-%m-%d %H:%i";
			scheduler.xy.nav_height = 48;

			scheduler.attachEvent("onTemplatesReady", function() {
				scheduler.xy.scale_height = 32;
			});

			var view_name = "timeline";
			scheduler.createTimelineView({
				name : view_name,
				x_unit : "day",
				x_date : "%D<br><b>%j</b>",
				x_step : 1,
				x_size : 31,
				section_autoheight : true,
				y_unit : this.sections,
				y_property : "section_id",
				render : "bar",
				round_position : true,
				dy : 40
			});

			scheduler.attachEvent("onBeforeViewChange", function(old_mode,
					old_date, mode, date) {
				var year = date.getFullYear();
				var month = (date.getMonth() + 1);
				var d = new Date(year, month, 0);
				scheduler.matrix[view_name].x_size = d.getDate();// number of
																	// days in
																	// month;
				return true;
			});

			scheduler.date['add_' + view_name] = function(date, step) {
				if (step > 0) {
					step = 1;
				} else if (step < 0) {
					step = -1;
				}
				return scheduler.date.add(date, step, "month")
			};
			scheduler.date[view_name + '_start'] = scheduler.date.month_start;

			scheduler.init(this.element, new Date(), "timeline");
			scheduler.parse(this.events, "json");
		},

		renderScheduler : function() {
			// ////////////////////////////////////////////////////////////////////////////////
			// 初始配置
			this.scheduler.config.readonly = true;
			this.scheduler.config.xml_date = "%Y-%m-%d %H:%i";
			this.scheduler.config.first_hour = 7;
			this.scheduler.config.last_hour = 19;
			this.scheduler.config.start_on_monday = false;
			this.scheduler.config.full_day = true;
			this.scheduler.config.multi_day = true;
			this.scheduler.xy.nav_height = 48;

			var ro = rap.getRemoteObject(this);
			this.scheduler.attachEvent("onClick", function(id, e) {
				ro.call("onClick", {
					"id" : id,
					"event" : e
				});
				return true;
			});
			// ////////////////////////////////////////////////////////////////////////////////
			// 初始化并加载数据
			this.scheduler.init(this.element, new Date(), "month");
			this.scheduler.parse(this.events, "json");
		},

		setInputData : function(inputData) {
			this.events = inputData.data;
		},

		setSectionData : function(sectionData) {
			this.sections = sectionData.data;
		},

		onSend : function() {
		},

		destroy : function() {
			if (this.element.parentNode) {
				rap.off("send", this.onSend);
				this.element.parentNode.removeChild(this.element);
			}
		},

		layout : function() {
			var area = this.parent.getClientArea();
			this.element.style.left = area[0] + "px";
			this.element.style.top = area[1] + "px";
		},

		addListener : function(event) {
		},

		removeListener : function(event) {
		}

	};

	var bind = function(context, method) {
		return function() {
			return method.apply(context, arguments);
		};
	};

	var bindAll = function(context, methodNames) {
		for (var i = 0; i < methodNames.length; i++) {
			var method = context[methodNames[i]];
			context[methodNames[i]] = bind(context, method);
		}
	};

	var async = function(context, func) {
		window.setTimeout(function() {
			func.apply(context);
		}, 0);
	};

}());