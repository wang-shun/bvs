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
				"autoSchedule", "highlightCriticalPath", "setScaleType",
				"save", "setDirty", "redo", "undo" ]

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

		dirty : false,

		onRender : function() {
			if (this.element.parentNode) {
				rap.off("render", this.onRender);

				// ////////////////////////////////////////////////////////////////////////////////
				// 初始配置
				this.genericConfig(this.config);

				// ////////////////////////////////////////////////////////////////////////////////
				// 任务图标配置
				this.configTaskIcon(this.config);

				// ////////////////////////////////////////////////////////////////////////////////
				// 表格菜单配置
				this.configGridMenu(this.config);

				// ////////////////////////////////////////////////////////////////////////////////
				// 表格行拖拽配置
				this.configGridRowDrag(this.config);

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
				// 配置任务提示
				this.configTaskTips(this.config);

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

				// rap.on( "send", this.onSend );
			}
		},

		genericConfig : function(config) {
			this.gantt.config.auto_scheduling = true;
			this.gantt.config.auto_scheduling_strict = true;
			this.gantt.config.auto_scheduling_move_projects = false;
			this.gantt.config.auto_scheduling_initial = false;
			this.gantt.config.auto_types = true;

			this.gantt.config.fit_tasks = true;
			this.gantt.config.autoscroll = true;
			this.gantt.config.autoscroll_speed = 50;

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
				this.gantt.config.order_branch = false;
				this.gantt.config.order_branch_free = false;// 禁止在整个项目中拖拽任务
				this.gantt.config.drag_project = false;
				this.gantt.config.drag_links = false;
				this.gantt.config.drag_move = false;
				this.gantt.config.drag_progress = false;
				this.gantt.config.drag_resize = false;
			} else {
				this.gantt.config.order_branch = true;
				this.gantt.config.order_branch_free = false;// 禁止在整个项目中拖拽任务
				this.gantt.config.drag_project = true;
				this.gantt.config.drag_links = true;
				this.gantt.config.drag_move = true;
				this.gantt.config.drag_progress = false;
				this.gantt.config.drag_resize = true;
			}

			this.gantt.config.show_progress = true;
			this.gantt.config.smart_rendering = true;
			this.gantt.config.static_background = true;
		},

		configTaskIcon : function(config) {
			this.gantt.templates.grid_file = function(item) {
				if (item.type == gantt.config.types.milestone) {
					if (item.actualFinish) {
						return "<div class='gantt_tree_icon gantt_milestone_finished'></div>";
					} else if (item.actualStart) {
						return "<div class='gantt_tree_icon gantt_milestone_finished'></div>";
					} else {
						return "<div class='gantt_tree_icon gantt_milestone'></div>";
					}
				} else {
					if (item.actualFinish) {
						return "<div class='gantt_tree_icon gantt_file_finished'></div>";
					} else if (item.actualStart) {
						return "<div class='gantt_tree_icon gantt_file_started'></div>";
					} else {
						return "<div class='gantt_tree_icon gantt_file'></div>";
					}
				}
			};
		},

		configGridRowDrag : function(config) {
			if (config.readonly) {
				return;
			}

			// ////////////////////////////////////////////////////////////////
			// 禁止放入到不同的parent下
			gantt.attachEvent("onBeforeTaskMove", function(id, parent, tindex) {
				var task = gantt.getTask(id);
				if (task.parent != parent)
					return false;
				return true;
			});
		},

		configGridMenu : function(config) {
			var gantt = this.gantt;
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
								+ remoteId + "\"," + JSON.stringify(task.id) + ")'></div>");
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

			// 处理标准模板
			config.columns.forEach(function(col) {
				if (col.name == "wbs") {
					col.template = gantt.getWBSCode;
				}
			});
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
				var dateToStr = gantt.date.date_to_str("%n/%j");
				return dateToStr(date);
			};
			gantt.config.subscales = [ {
				unit : "week",
				step : 1,
				template : weekScaleTemplate
			} ];
		},

		configTaskTips : function(config) {
			if (config.readonly && !config.grid_width) {
				var gantt = this.gantt;
				var fFunc = gantt.date.date_to_str("%Y-%m-%d %H:%i");
				gantt
						.attachEvent(
								"onTaskClick",
								function(id, e) {//
									var task = gantt.getTask(id);//
									layer
											.tips(
													"开始："
															+ fFunc(task.start_date)//
															+ "<br>完成："
															+ fFunc(task.end_date)//
															+ "<br>工期："
															+ task.duration
															+ "天"//
															+ (task.chargerInfo ? ("<br>负责：" + task.chargerInfo)
																	: "")//
															+ (task.actualFinish ? " 已完成"
																	: (task.actualStart ? "已开始"
																			: ""))//
													,
													$("div[task_id$='"
															+ task.id + "'")[0],//
													{
														tips : [ 1, '#3595CC' ],
														time : 4000
													});
									return true;
								});
			}
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
				if (task.type == gantt.config.types.milestone) {
					return "milestone_task";
				}
				if (task.barstyle) {
					return task.barstyle;
				}
				return "";
			};

		},

		configHolidays : function(config) {
			this.gantt.templates.task_cell_class = this._getHolidayStyle;
		},

		_getHolidayStyle : function(task, date) {
			if (this.dateCell && !this.gantt.isWorkTime(date))
				return "week_end";
			return "";
		},

		configSideContent : function(config) {

			var gantt = this.gantt;
			var formatFunc = gantt.date.date_to_str("%n/%j");
			if (config.grid_width == 0) {

				gantt.templates.rightside_text = function(start, end, task) {
					var text = "";
					if (task.type == gantt.config.types.milestone) {
						text = task.text + "&nbsp;";
					}
					text += formatFunc(end) + "&nbsp;";
					if (task.end_date1) {
						var overdue = Math.ceil(Math
								.abs((end.getTime() - task.end_date1.getTime())
										/ (24 * 60 * 60 * 1000)));
						if (end.getTime() > task.end_date1.getTime()) {
							text += "<span class='layui-badge' style='margin-left:8px;'>+"
									+ overdue + "d</span>";
						} else if (end.getTime() < task.end_date1.getTime()) {
							text += "<span class='layui-badge layui-bg-blue' style='margin-left:8px;'>-"
									+ overdue + "d</span>";
						}
					}
					if (task.chargerInfo) {
						text += "&nbsp;" + task.chargerInfo;
					}
					return text;
				};

				gantt.templates.leftside_text = function(start, end, task) {
					return formatFunc(start);
				};
			} else {
				gantt.templates.rightside_text = function(start, end, task) {
					if (task.type == gantt.config.types.milestone) {
						return task.text + "&nbsp;" + formatFunc(start);
					}
					return task.chargerInfo ? task.chargerInfo : "";
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
						var text = "<span class='layui-badge' style='margin-left:8px;'>+"
								+ overdue + "d</span>";
						return text;
					} else if (end.getTime() < task.end_date1.getTime()) {
						var text = "<span class='layui-badge layui-bg-blue' style='margin-left:8px;'>-"
								+ overdue + "d</span>";
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

			var gantt = this.gantt;

			var pt = this;

			var ro = rap.getRemoteObject(this);

			gantt.attachEvent("onAfterTaskAdd", function(id, task) {
				changeDirty(true);
			});

			gantt.attachEvent("onAfterTaskUpdate", function(id, item) {
				changeDirty(true);
			});

			gantt.attachEvent("onAfterTaskDelete", function(id, task) {
				changeDirty(true);
			});

			var checkProject = this.checkProject;
			gantt.attachEvent("onBeforeLinkAdd", function(id, link) {
				if (checkProject && !(link.project)) {
					ro.call("onTaskLinkBefore", link);
					return false;
				} else {
					changeDirty(true);
					return true;
				}
			});

			function changeDirty(dirty) {
				if (dirty != pt.dirty) {
					pt.dirty = dirty;
					ro.call("dirtyChanged", {
						"dirty" : pt.dirty
					});
				}
			}
			;

		},

		onGridMenuClick : function(id) {
			var cObj = rap.getObject(id);
			rap.getRemoteObject(cObj).call("onGridHeaderMenuClick", {});
		},

		onGridRowMenuClick : function(id, taskId) {
			var cObj = rap.getObject(id);
			rap.getRemoteObject(cObj).call("onGridRowMenuClick", {
				"id" : taskId
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
			rap.getRemoteObject(this).set("dirty", this.dirty);
		},

		destroy : function() {
			if (this.element.parentNode) {
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
			} else if (eventCode == "onCircularLinkError") {
			} else if (eventCode == "onEmptyClick"
					|| eventCode == "onMultiSelect") {
				gantt.attachEvent(eventCode, function(e) {
					ro.call(eventCode, {});
				});
			} else if (eventCode == "onError") {
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
				// gantt.attachEvent(eventCode, function() {
				// ro.call(eventCode, {});
				// });
			}
		},

		addTask : function(parameter) {
			this.gantt.addTask(parameter.task, parameter.parentId);
		},

		addLink : function(link) {
			this.gantt.addLink(link);
		},

		save : function() {
			this.updateClient("save");
		},
		
		updateClient: function(eventCode){
			var tasks = [];
			var gantt = this.gantt;
			this.gantt.eachTask(function(task) {
				task.wbsCode = gantt.getWBSCode(task);
				tasks.push(task);
			})
			var links = this.gantt.getLinks();
			rap.getRemoteObject(this).call(eventCode, {
				"tasks" : tasks,
				"links" : links
			});
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

		setDirty : function(param) {
			this.dirty = param.dirty;
		},

		autoSchedule : function() {
			this.gantt.autoSchedule();
		},

		highlightCriticalPath : function(param) {
			this.gantt.config.highlight_critical_path = param.display;
			this.gantt.render();
		},

		undo : function() {
			this.gantt.undo();
			this.updateClient("update");
		},

		redo : function() {
			this.gantt.redo();
			this.updateClient("update");
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