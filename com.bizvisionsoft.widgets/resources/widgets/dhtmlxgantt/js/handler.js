(function() {
	'use strict';

	rap.registerTypeHandler("bizvision.dhtmlxgantt", {

		factory : function(properties) {
			return new bizvision.dhtmlxgantt(properties);
		},

		destructor : "destroy",

		properties : [ "config", "initFrom", "initTo", "inputData" ],

		methods : [ "addListener", "removeListener", "addTask", "addLink",
				"updateTask", "updateLink" ]

	});

	if (!window.bizvision) {
		window.bizvision = {};
	}

	bizvision.dhtmlxgantt = function(properties) {
		bindAll(this, [ "layout", "onReady", "onSend", "onRender", "destroy",
				"onGridMenuClick" ]);
		this.parent = rap.getObject(properties.parent);
		this.element = document.createElement("div");
		this.element.style.width = "100%";
		this.element.style.height = "100%";
		this.parent.append(this.element);

		this.parent.addListener("Dispose", this.destroy);
		this.parent.addListener("Resize", this.layout);

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
				this.configScale(this.config);

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
				// 自动控制任务类型
				this.handleTaskType();

				// ////////////////////////////////////////////////////////////////////////////////
				// 接受服务端配置
				this.acceptServerConfig(this.config);

				// ////////////////////////////////////////////////////////////////////////////////
				// 配置日期数据格式
				gantt.config.xml_date = "%Y-%m-%d %H:%i:%s";

				// ////////////////////////////////////////////////////////////////////////////////
				// 处理服务端与客户端的同步
				this.handleTaskModify();

				// ////////////////////////////////////////////////////////////////////////////////
				// 初始化并加载数据
				gantt.init(this.element, this.initFrom, this.initTo);
				gantt.parse(this.inputData);

			}
		},

		genericConfig : function(config) {
			gantt.config.auto_scheduling = true;
			gantt.config.auto_scheduling_strict = true;
			gantt.config.work_time = true;

			// gantt.config.order_branch = true;
			// gantt.config.order_branch_free = true;

			gantt.config.touch = "force";
			gantt.config.grid_resize = true;
			gantt.config.keep_grid_width = false;
			gantt.config.start_on_monday = false;

			gantt.config.links.start_to_start = "SS";
			gantt.config.links.finish_to_start = "FS";
			gantt.config.links.start_to_finish = "SF";
			gantt.config.links.finish_to_finish = "FF";
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

		configScale : function(config) {
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

		configLayout : function(config) {
			gantt.config.layout = {
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
		},

		configTaskStyle : function(config) {
			gantt.config.task_height = 20;
			gantt.templates.task_class = function(start, end, task) {
				if (task.barstyle) {
					return task.barstyle;
				}
				return "";
			};
		},

		configHolidays : function(config) {
			gantt.templates.task_cell_class = function(task, date) {
				if (!gantt.isWorkTime(date))
					return "week_end";
				return "";
			};
			gantt.config.work_time = true;
		},

		acceptServerConfig : function(config) {
			if (config) {
				for ( var attr in config) {
					if (!attr.startsWith("brui_")) {
						gantt.config[attr] = this.config[attr];
					}
				}
			}
		},

		// 处理同步
		handleTaskModify : function() {
			var ro = rap.getRemoteObject(this);

			gantt.attachEvent("onAfterTaskAdd", function(id, task) {
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
			});

			gantt.attachEvent("onAfterTaskDelete", function(id, task) {
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

		},

		handleTaskType : function() {
			var delTaskParent;

			function checkParents(id) {
				setTaskType(id);
				var parent = gantt.getParent(id);
				if (parent != gantt.config.root_id) {
					checkParents(parent);
				}
			}

			function setTaskType(id) {
				id = id.id ? id.id : id;
				var task = gantt.getTask(id);
				if (gantt.hasChild(task.id)) {
					if (gantt.config.types.project != task.type) {
						task.type = gantt.config.types.project;
						gantt.updateTask(id);
						gantt.open(id);
					}
				} else {
					if (gantt.config.types.task != task.type
							&& gantt.config.types.milestone != task.type) {
						task.type = gantt.config.types.task;
						gantt.updateTask(id);
					}
				}
			}

			gantt.attachEvent("onParse", function() {
				gantt.eachTask(function(task) {
					setTaskType(task);
				});
			});

			gantt.attachEvent("onAfterTaskAdd", function onAfterTaskAdd(id) {
				gantt.batchUpdate(function() {
					checkParents(id)
				});
			});

			gantt.attachEvent("onBeforeTaskDelete",
					function onBeforeTaskDelete(id, task) {
						delTaskParent = gantt.getParent(id);
						return true;
					});

			gantt.attachEvent("onAfterTaskDelete", function onAfterTaskDelete(
					id, task) {
				if (delTaskParent != gantt.config.root_id) {
					gantt.batchUpdate(function() {
						checkParents(delTaskParent)
					});
				}
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
				this.element.parentNode.removeChild(this.element);
			}
		},

		layout : function() {
			var area = this.parent.getClientArea();
			this.element.style.left = area[0] + "px";
			this.element.style.top = area[1] + "px";
			this.element.style.width = area[2] + "px";
			this.element.style.height = area[3] + "px";
			gantt.setSizes();
		},

		addListener : function(event) {
			var eventCode = event.name;
			var ro = rap.getRemoteObject(this);
			if (eventCode == "onAfterAutoSchedule") {
				gantt.attachEvent(eventCode, function(taskId, updatedTasks) {
					ro.call(eventCode, {
						"taskId" : taskId,
						"updatedTasks" : updatedTasks
					});
				});
			} else if (eventCode == "onAfterLinkAdd"
					|| eventCode == "onAfterLinkDelete"
					|| eventCode == "onAfterLinkUpdate"
					|| eventCode == "onAfterTaskAdd"
					|| eventCode == "onAfterTaskDelete"
					|| eventCode == "onAfterTaskUpdate") {
				// 统一处理同步
			} else if (eventCode == "onAfterTaskAutoSchedule") {
				gantt.attachEvent(eventCode, function(task, start, link,
						predecessor) {
					ro.call(eventCode, {
						"task" : task,
						"start" : start,
						"link" : link,
						"predecessor" : predecessor
					});
				});
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
			} else if (eventCode == "onTaskLinkBefore") {// 自定义的事件
				this.checkProject = true;
			} else if (eventCode == "onGridRowMenuClick") {// 自定义的事件

			} else if (eventCode == "onGridHeaderMenuClick") {// 自定义的事件

			} else {
				gantt.attachEvent(eventCode, function() {
					ro.call(eventCode, {});
				});
			}
		},

		addTask : function(parameter) {
			gantt.addTask(parameter.task, parameter.parentId, parameter.index);
		},

		addLink : function(link) {
			gantt.addLink(link);
		},

		updateTask : function(task) {
			var clientTask = gantt.getTask(task.id);
			for ( var attr in task) {
				if (task[attr].type == "Date") {
					var formatFunc = gantt.date.str_to_date(task[attr].format);
					clientTask[attr] = formatFunc(task[attr].value);
				} else {
					clientTask[attr] = task[attr];
				}
			}
			gantt.updateTask(task.id);
		},

		updateLink : function(link) {
			var clientLink = gantt.getLink(link.id);
			for ( var attr in link) {
				clientLink[attr] = link[attr];
			}
			gantt.updateLink(link.id);
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