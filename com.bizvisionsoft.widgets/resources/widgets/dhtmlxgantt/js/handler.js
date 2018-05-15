(function() {
	'use strict';

	rap.registerTypeHandler("bizvision.dhtmlxgantt", {

		factory : function(properties) {
			return new bizvision.dhtmlxgantt(properties);
		},

		destructor : "destroy",

		properties : [ "config", "initFrom", "initTo", "inputData" ],

		methods : [ "addListener", "removeListener", "addTask", "addLink",
				"updateTask", "updateLink", "deleteTask", "deleteLink",
				"autoSchedule", "highlightCriticalPath", "setScaleType" ]

	});

	if (!window.bizvision) {
		window.bizvision = {};
	}

	bizvision.dhtmlxgantt = function(properties) {
		bindAll(this, [ "layout", "onReady", "onSend", "onRender", "destroy",
				"genericConfig", "configGridMenu", "configScale_0",
				"configScale_1", "configScale_2", "configScale_3",
				"configLayout", "configTaskStyle", "configHolidays",
				"configComparable", "acceptServerConfig", "onGridMenuClick",
				"onGridRowMenuClick", "_getHolidayStyle", "handleTaskModify" ]);
		this.parent = rap.getObject(properties.parent);
		this.element = document.createElement("div");
		this.element.style.width = "100%";
		this.element.style.height = "100%";
		this.parent.append(this.element);

		this.parent.addListener("Dispose", this.destroy);
		this.parent.addListener("Resize", this.layout);

		this.gantt = Gantt.getGanttInstance();

		rap.on("render", this.onRender);
	};

	bizvision.dhtmlxgantt.prototype = {

		onRender : function() {
			if (this.element.parentNode) {
				rap.off("render", this.onRender);

				// ////////////////////////////////////////////////////////////////////////////////
				// 初始配置
				this.genericConfig(this.config);

				// ////////////////////////////////////////////////////////////////////////////////
				// 表格菜单配置
				this.configGridMenu(this.config);

				// ////////////////////////////////////////////////////////////////////////////////
				// 配置刻度
				var type = this.config.brui_initScaletype;
				if (type == "year-month-week") {
					this.configScale_1(this.config);
				} else if (type == "year-month") {
					this.configScale_2(this.config);
				} else if (type == "month-week") {
					this.configScale_3(this.config);
				} else {
					// "month-week-date"
					this.configScale_0(this.config);
				}

				// ////////////////////////////////////////////////////////////////////////////////
				// 配置布局
				this.configLayout(this.config);

				// ////////////////////////////////////////////////////////////////////////////////
				// 配置任务样式
				this.configTaskStyle(this.config);

				// ////////////////////////////////////////////////////////////////////////////////
				// 配置周末
				this.configHolidays(this.config);

				// ////////////////////////////////////////////////////////////////////////////////
				// 配置对比甘特图
				this.configComparable(this.config);

				// ////////////////////////////////////////////////////////////////////////////////
				// 配置甘特图显示信息
				this.configSideContent(this.config);

				// ////////////////////////////////////////////////////////////////////////////////
				// 接受服务端配置
				this.acceptServerConfig(this.config);

				// ////////////////////////////////////////////////////////////////////////////////
				// 配置日期数据格式
				this.gantt.config.xml_date = "%Y-%m-%d %H:%i:%s";

				// ////////////////////////////////////////////////////////////////////////////////
				// 处理服务端与客户端的同步
				this.handleTaskModify();

				// ////////////////////////////////////////////////////////////////////////////////
				// 初始化并加载数据
				this.gantt.init(this.element);// , this.initFrom,
				// this.initTo);
				this.gantt.parse(this.inputData);

			}
		},

		genericConfig : function(config) {
			this.gantt.config.auto_scheduling = true;
			this.gantt.config.auto_scheduling_strict = true;
			this.gantt.config.auto_scheduling_move_projects = false;
			this.gantt.config.auto_scheduling_initial = false;

			this.gantt.config.work_time = true;
			this.gantt.config.correct_work_time = true;

			this.gantt.config.fit_tasks = true;
			this.gantt.config.autoscroll = true;
			this.gantt.config.autoscroll_speed = 50;

			this.gantt.config.order_branch = false;
			this.gantt.config.order_branch_free = false;// 禁止在整个项目中拖拽任务
			this.gantt.config.sort = true;

			this.gantt.config.touch = "force";
			this.gantt.config.open_tree_initially = true;
			this.gantt.config.grid_resize = true;
			this.gantt.config.keep_grid_width = false;
			this.gantt.config.start_on_monday = false;

			this.gantt.config.links.start_to_start = "SS";
			this.gantt.config.links.finish_to_start = "FS";
			this.gantt.config.links.start_to_finish = "SF";
			this.gantt.config.links.finish_to_finish = "FF";

			if (config.readonly) {
				this.gantt.config.drag_project = false;
				this.gantt.config.drag_links = false;
				this.gantt.config.drag_move = false;
				this.gantt.config.drag_progress = false;
				this.gantt.config.drag_resize = false;
			} else {
				this.gantt.config.drag_project = true;
				this.gantt.config.drag_links = true;
				this.gantt.config.drag_move = true;
				this.gantt.config.drag_progress = true;
				this.gantt.config.drag_resize = true;
			}
		},

		configGridMenu : function(config) {
			if (config.brui_HeadMenuEnable || config.brui_RowMenuEnable) {
				var remoteId = rap.getRemoteObject(this)._.id;
				var colHeader;
				var colContent;
				if (config.brui_HeadMenuEnable) {
					colHeader = "<div class='gantt_grid_head_cell gantt_grid_head_add' onclick='bizvision.dhtmlxgantt.prototype.onGridMenuClick(\""
							+ remoteId + "\")'></div>";
				} else {
					colHeader = "";
				}

				if (config.brui_RowMenuEnable) {
					colContent = function(task) {
						return ("<div class='gantt_row_btn_menu' onclick='bizvision.dhtmlxgantt.prototype.onGridRowMenuClick(\""
								+ remoteId + "\"," + JSON.stringify(task) + ")'></div>");
					};
				} else {
					colContent = function(task) {
						return "";
					};
				}

				config.columns.splice(0, 0, {
					name : "menu",
					width : 34,
					align : "center",
					resize : false,
					label : colHeader,
					template : colContent
				})

			}
		},

		// 月周日
		configScale_0 : function(config) {
			this.dateCell = true;
			var gantt = this.gantt;

			gantt.config.scale_unit = "month";
			gantt.config.step = 1;
			gantt.config.date_scale = "%Y年%n月";
			gantt.config.min_column_width = 40;
			gantt.config.scale_height = 90;
			var weekScaleTemplate = function(date) {
				var dateToStr = gantt.date.date_to_str("%n月%j日");
				var endDate = gantt.date.add(gantt.date.add(date, 1, "week"),
						-1, "day");
				return dateToStr(date) + " - " + dateToStr(endDate);
			};
			gantt.config.subscales = [ {
				unit : "week",
				step : 1,
				template : weekScaleTemplate
			}, {
				unit : "day",
				step : 1,
				date : "%j"
			} ];
		},

		configScale_1 : function(config) {
			this.dateCell = false;
			var gantt = this.gantt;

			gantt.config.scale_unit = "year";
			gantt.config.step = 1;
			gantt.config.date_scale = "%Y年";
			gantt.config.min_column_width = 40;
			gantt.config.scale_height = 90;
			gantt.config.subscales = [ {
				unit : "month",
				step : 1,
				date : "%n月"
			}, {
				unit : "week",
				step : 1,
				date : "%W周"
			} ];
		},

		configScale_2 : function(config) {
			this.dateCell = false;
			var gantt = this.gantt;

			gantt.config.scale_unit = "year";
			gantt.config.step = 1;
			gantt.config.date_scale = "%Y年";
			gantt.config.min_column_width = 40;
			gantt.config.scale_height = 90;
			gantt.config.subscales = [ {
				unit : "month",
				step : 1,
				date : "%n月"
			} ];
		},

		configScale_3 : function(config) {
			this.dateCell = false;
			var gantt = this.gantt;

			gantt.config.scale_unit = "month";
			gantt.config.step = 1;
			gantt.config.date_scale = "%Y年%n月";
			gantt.config.min_column_width = 40;
			gantt.config.scale_height = 90;
			var weekScaleTemplate = function(date) {
				var dateToStr = gantt.date.date_to_str("%n月%j日");
				var endDate = gantt.date.add(gantt.date.add(date, 1, "week"),
						-1, "day");
				return dateToStr(date) + " - " + dateToStr(endDate);
			};
			gantt.config.subscales = [ {
				unit : "week",
				step : 1,
				template : weekScaleTemplate
			} ];
		},

		configLayout : function(config) {
			if (config.grid_width != 0) {
				this.gantt.config.layout = {
					cols : [ {
						width : config.grid_width,
						min_width : 320,
						rows : [ {
							view : "grid",
							scrollX : "gridScroll",
							scrollable : true,
							scrollY : "scrollVer"
						}, {
							view : "scrollbar",
							id : "gridScroll",
							group : "horizontal"
						} ]
					}, {
						resizer : true,
						width : 1
					}, {
						rows : [ {
							view : "timeline",
							scrollX : "scrollHor",
							scrollY : "scrollVer"
						}, {
							view : "scrollbar",
							id : "scrollHor",
							group : "horizontal"
						} ]
					}, {
						view : "scrollbar",
						id : "scrollVer"
					} ]
				};
			} else {
				this.gantt.config.layout = {
					cols : [ {
						rows : [ {
							view : "timeline",
							scrollX : "scrollHor",
							scrollY : "scrollVer"
						}, {
							view : "scrollbar",
							id : "scrollHor",
							group : "horizontal"
						} ]
					}, {
						view : "scrollbar",
						id : "scrollVer"
					} ]
				};
			}
		},

		configTaskStyle : function(config) {
			this.gantt.config.task_height = 20;
			this.gantt.templates.task_class = function(start, end, task) {
				if (task.barstyle) {
					return task.barstyle;
				}
				return "";
			};
		},

		configHolidays : function(config) {
			this.gantt.templates.task_cell_class = this._getHolidayStyle;
			this.gantt.config.work_time = true;
		},

		_getHolidayStyle : function(task, date) {
			if (this.dateCell && !this.gantt.isWorkTime(date))
				return "week_end";
			return "";
		},

		configSideContent : function(config) {

			if (config.grid_width == 0) {
				var gantt = this.gantt;
				var formatFunc = gantt.date.date_to_str("%n/%j");

				gantt.templates.rightside_text = function(start, end, task) {
					var text = formatFunc(end);
					if (task.end_date1) {
						var overdue = Math.ceil(Math
								.abs((end.getTime() - task.end_date1.getTime())
										/ (24 * 60 * 60 * 1000)));
						if (end.getTime() > task.end_date1.getTime()) {
							text += " <b>+" + overdue + "d</b>";
						} else if (end.getTime() < task.end_date1.getTime()) {
							text += " <b>-" + overdue + "d</b>";
						}
					}
					return text;
				};

				gantt.templates.leftside_text = function(start, end, task) {
					return formatFunc(start);
				};
			}
		},

		configComparable : function(config) {
			if (!config.brui_enableGanttCompare) {
				return;
			}
			var gantt = this.gantt;
			gantt.config.row_height = 52;

			gantt.addTaskLayer(function draw_planned(task) {
				if (task.start_date1 && task.end_date1) {
					var sizes = gantt.getTaskPosition(task, task.start_date1,
							task.end_date1);
					var el = document.createElement('div');
					el.className = 'baseline';
					el.style.left = sizes.left + 'px';
					el.style.width = sizes.width + 'px';
					el.style.top = sizes.top + gantt.config.task_height + 13
							+ 'px';
					el.innerHTML = task.text;
					return el;
				}
				return false;
			});

			gantt.templates.task_class = function(start, end, task) {
				if (task.end_date1) {
					var classes = [ 'has-baseline' ];
					if (end.getTime() > task.end_date1.getTime()) {
						classes.push('overdue');
					}
					return classes.join(' ');
				}
			};

			gantt.attachEvent("onTaskLoading", function(task) {
				task.start_date1 = gantt.date.parseDate(task.start_date1,
						"xml_date");
				task.end_date1 = gantt.date.parseDate(task.end_date1,
						"xml_date");
				return true;
			});

			gantt.templates.rightside_text = function(start, end, task) {
				if (task.end_date1) {
					var overdue = Math.ceil(Math
							.abs((end.getTime() - task.end_date1.getTime())
									/ (24 * 60 * 60 * 1000)));
					if (end.getTime() > task.end_date1.getTime()) {
						var text = "<b>+" + overdue + "d</b>";
						return text;
					} else if (end.getTime() < task.end_date1.getTime()) {
						var text = "<b>-" + overdue + "d</b>";
						return text;
					}
				}
			};
		},

		acceptServerConfig : function(config) {
			if (config) {
				for ( var attr in config) {
					if (!attr.startsWith("brui_")) {
						this.gantt.config[attr] = this.config[attr];
					}
				}
			}
		},

		// 处理同步
		handleTaskModify : function() {

			var delTaskParent;

			var gantt = this.gantt;

			var ro = rap.getRemoteObject(this);

			gantt.attachEvent("onAfterAutoSchedule", function(id, tasks) {
				ro.call("onAfterAutoSchedule", {
					"taskId" : id,
					"updatedTasks" : tasks
				});
			});

			/*
			 * gantt.attachEvent("onAfterTaskAutoSchedule", function(task,
			 * start, link, predecessor) { ro.call("onAfterTaskAutoSchedule", {
			 * "task" : task, "start" : start, "link" : link, "predecessor" :
			 * predecessor }); });
			 */

			gantt.attachEvent("onAfterTaskAdd", function(id, task) {
				gantt.batchUpdate(function() {
					checkParents(id);
				});

				ro.call("onAfterTaskAdd", {
					"id" : id,
					"task" : task
				});
			});

			gantt.attachEvent("onAfterTaskUpdate", function(id, task) {
				ro.call("onAfterTaskUpdate", {
					"id" : id,
					"task" : task
				});

				gantt.eachParent(function(parent) {
					ro.call("onAfterTaskUpdate", {
						"id" : parent.id,
						"task" : parent
					});
				}, task);
			});

			gantt.attachEvent("onAfterTaskDrag", function(id, mode) {
				var modes = gantt.config.drag_mode;
				switch (mode) {
				case modes.move:
					break;
				case modes.resize:
					break;
				case modes.progress:
					ro.call("onAfterTaskProgress", {
						"id" : id,
						"task" : gantt.getTask(id)
					});
					break;
				}
			});

			gantt.attachEvent("onAfterTaskDelete", function(id, task) {
				if (delTaskParent != gantt.config.root_id) {
					gantt.batchUpdate(function() {
						checkParents(delTaskParent);
					});
				}

				ro.call("onAfterTaskDelete", {
					"id" : id,
					"task" : task
				});
			});

			gantt.attachEvent("onAfterLinkAdd", function(id, link) {
				ro.call("onAfterLinkAdd", {
					"id" : id,
					"link" : link
				});
			});

			gantt.attachEvent("onAfterLinkUpdate", function(id, link) {
				ro.call("onAfterLinkUpdate", {
					"id" : id,
					"link" : link
				});
			});

			gantt.attachEvent("onAfterLinkDelete", function(id, link) {
				ro.call("onAfterLinkDelete", {
					"id" : id,
					"link" : link
				});
			});

			var checkProject = this.checkProject;
			gantt.attachEvent("onBeforeLinkAdd", function(id, link) {
				if (checkProject && !(link.project)) {
					ro.call("onTaskLinkBefore", link);
					return false;
				} else {
					return true;
				}
			});

			function checkParents(id) {
				setTaskType(id);
				var parent = gantt.getParent(id);
				if (parent != gantt.config.root_id) {
					checkParents(parent);
				}
			}
			;

			function setTaskType(id) {
				id = id.id ? id.id : id;
				var task = gantt.getTask(id);
				if (gantt.hasChild(task.id)) {
					if (gantt.config.types.project != task.type) {
						task.type = gantt.config.types.project;
					}
					gantt.updateTask(id);
					gantt.open(id);
				} else {
					if (gantt.config.types.task != task.type
							&& gantt.config.types.milestone != task.type) {
						task.type = gantt.config.types.task;
					}
					gantt.updateTask(id);
				}
			}
			;

			gantt.attachEvent("onParse", function() {
				gantt.eachTask(function(task) {
					setTaskType(task);
				});
			});

			gantt.attachEvent("onBeforeTaskDelete",
					function onBeforeTaskDelete(id, task) {
						delTaskParent = gantt.getParent(id);
						return true;
					});

		},

		onGridMenuClick : function(id) {
			var cObj = rap.getObject(id);
			rap.getRemoteObject(cObj).call("onGridHeaderMenuClick", {});
		},

		onGridRowMenuClick : function(id, task) {
			var cObj = rap.getObject(id);
			rap.getRemoteObject(cObj).call("onGridRowMenuClick", {
				"id" : task.id,
				"task" : task
			});
		},

		setConfig : function(config) {
			this.config = config;
		},

		setInitFrom : function(initFrom) {
			this.initFrom = new Date(initFrom);
		},

		setInitTo : function(initTo) {
			this.initTo = new Date(initTo);
		},

		setInputData : function(inputData) {
			this.inputData = inputData;
		},

		onSend : function() {
		},

		destroy : function() {
			if (this.element.parentNode) {
				rap.off("send", this.onSend);
				this.gantt.destructor();
				this.element.parentNode.removeChild(this.element);
			}
		},

		layout : function() {
			var area = this.parent.getClientArea();
			this.element.style.left = area[0] + "px";
			this.element.style.top = area[1] + "px";
			this.element.style.width = area[2] + "px";
			this.element.style.height = area[3] + "px";
			this.gantt.setSizes();
		},

		addListener : function(event) {
			var eventCode = event.name;
			var ro = rap.getRemoteObject(this);
			var gantt = this.gantt;
			if (eventCode == "onAfterAutoSchedule"
					|| eventCode == "onAfterLinkAdd"
					|| eventCode == "onAfterLinkDelete"
					|| eventCode == "onAfterLinkUpdate"
					|| eventCode == "onAfterTaskAdd"
					|| eventCode == "onAfterTaskDelete"
					|| eventCode == "onAfterTaskUpdate"
					|| eventCode == "onAfterTaskAutoSchedule"
					|| eventCode == "onAfterTaskDrag") {
				// ////////////////////////////////////////////////////////////////////////
				// 统一处理同步,不能单独添加，否者可能在未更新模型前触发侦听程序，导致不一致的数据

			} else if (eventCode == "onAutoScheduleCircularLink") {
				gantt.attachEvent(eventCode, function(groups) {
					ro.call(eventCode, {
						"groups" : groups
					});
				});
			} else if (eventCode == "onCircularLinkError") {
				gantt.attachEvent(eventCode, function(link, group) {
					ro.call(eventCode, {
						"link" : link,
						"group" : group
					});
				});
			} else if (eventCode == "onEmptyClick"
					|| eventCode == "onMultiSelect") {
				gantt.attachEvent(eventCode, function(e) {
					ro.call(eventCode, {});
				});
			} else if (eventCode == "onError") {
				gantt.attachEvent(eventCode, function(errorMessage) {
					ro.call(eventCode, {
						"errorMessage" : errorMessage
					});
				});
			} else if (eventCode == "onLinkClick"
					|| eventCode == "onLinkDblClick"
					|| eventCode == "onTaskClick"
					|| eventCode == "onTaskDblClick"
					|| eventCode == "onTaskSelected"
					|| eventCode == "onTaskUnselected"
					|| eventCode == "onTaskRowClick") {
				gantt.attachEvent(eventCode, function(id, e) {
					ro.call(eventCode, {
						"id" : id
					});
					return true;
				});
			} else if (eventCode == "onLinkValidation") {
				gantt.attachEvent(eventCode, function(link) {
					ro.call(eventCode, {
						"link" : link
					});
				});
			} else if (eventCode == "onScaleClick") {
				gantt.attachEvent(eventCode, function(e, date) {
					ro.call(eventCode, {
						"date" : date
					});
				});
			} else if (eventCode == "onTaskMultiSelect") {
				gantt.attachEvent(eventCode, function(id, state, e) {
					ro.call(eventCode, {
						"id" : id,
						"state" : state
					});
				});
			} else if (eventCode == "onTaskLinkBefore") {
				// ////////////////////////////////////////////////////////////////////////
				// 自定义的事件，如果设置了本侦听器，那么系统认为添加link将被服务端接管。
				// 服务端接管情况下，要求必须检查link是否具有项目属性
				this.checkProject = true;
			} else if (eventCode == "onGridRowMenuClick") {// 自定义的事件
			} else if (eventCode == "onGridHeaderMenuClick") {// 自定义的事件
			} else if (eventCode == "onAfterTaskMove") {// 自定义的事件
			} else if (eventCode == "onAfterTaskResize") {// 自定义的事件
			} else if (eventCode == "onAfterTaskProgress") {// 自定义的事件
			} else {
				gantt.attachEvent(eventCode, function() {
					ro.call(eventCode, {});
				});
			}
		},

		addTask : function(parameter) {
			this.gantt.addTask(parameter.task, parameter.parentId,
					parameter.index);
		},

		addLink : function(link) {
			this.gantt.addLink(link);
		},

		updateTask : function(task) {
			var clientTask = this.gantt.getTask(task.id);
			for ( var attr in task) {
				if (task[attr].type == "Date") {
					var formatFunc = this.gantt.date
							.str_to_date(task[attr].format);
					clientTask[attr] = formatFunc(task[attr].value);
				} else {
					clientTask[attr] = task[attr];
				}
			}
			this.gantt.updateTask(task.id);
		},

		updateLink : function(link) {
			var clientLink = this.gantt.getLink(link.id);
			for ( var attr in link) {
				clientLink[attr] = link[attr];
			}
			this.gantt.updateLink(link.id);
		},

		deleteTask : function(param) {
			this.gantt.deleteTask(param.id);
		},

		deleteLink : function(param) {
			this.gantt.deleteLink(param.id);
		},

		autoSchedule : function() {
			this.gantt.autoSchedule();
		},

		highlightCriticalPath : function(param) {
			this.gantt.config.highlight_critical_path = param.display;
			this.gantt.render();
		},

		setScaleType : function(param) {
			var type = param.type;
			if (type == "month-week-date") {
				this.configScale_0(this.gantt.config);
			} else if (type == "year-month-week") {
				this.configScale_1(this.gantt.config);
			} else if (type == "year-month") {
				this.configScale_2(this.gantt.config);
			} else if (type == "month-week") {
				this.configScale_3(this.gantt.config);
			}
			this.gantt.render();
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