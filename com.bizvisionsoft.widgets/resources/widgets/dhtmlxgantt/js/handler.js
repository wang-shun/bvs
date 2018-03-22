(function() {
	'use strict';

	rap.registerTypeHandler("bizvision.dhtmlxgantt", {

		factory : function(properties) {
			return new bizvision.dhtmlxgantt(properties);
		},

		destructor : "destroy",

		properties : [ "config", "initFrom", "initTo", "inputData" ]

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
				// 表格列和菜单配置
				this.configGrid(this.config);

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
				// 初始化并加载数据
				gantt.init(this.element, this.initFrom, this.initTo);
				gantt.parse(this.inputData);
			}
		},

		genericConfig : function(config) {
			gantt.config.touch = "force";
			gantt.config.grid_resize = true;
			gantt.config.keep_grid_width = false;
			gantt.config.start_on_monday = false;
			gantt.config.details_on_create = true;
			gantt.config.links.start_to_start = "SS";
			gantt.config.links.finish_to_start = "FS";
			gantt.config.links.start_to_finish = "SF";
			gantt.config.links.finish_to_finish = "FF";
		},

		configGrid : function(config) {
			var remoteId = rap.getRemoteObject(this)._.id;
			var colHeader = "<div class='gantt_grid_head_cell gantt_grid_head_add' onclick='bizvision.dhtmlxgantt.prototype.onGridMenuClick(\""
					+ remoteId + "\")'></div>";
			var colContent = function(task) {
				return ("<div class='gantt_row_btn_menu' onclick='bizvision.dhtmlxgantt.prototype.onGridRowMenuClick(\""
						+ remoteId + "\"," + JSON.stringify(task) + ")'></div>");
			};
			gantt.config.columns = [ {
				name : "menu",
				label : colHeader,
				width : 34,
				align : "center",
				resize : false,
				template : colContent
			}, {
				name : "text",
				label : "工作",
				tree : true,
				width : 320,
				resize : true
			}, {
				name : "start_date",
				label : "开始",
				align : "center",
				width : 96,
				resize : true
			}, {
				name : "end_date",
				label : "完成",
				align : "center",
				width : 96,
				hide : true,
				resize : true
			}, {
				name : "duration",
				label : "工期",
				align : "right",
				width : 40,
				resize : true
			} ];
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
					width : 490,
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
					gantt.config[attr] = this.config[attr];
				}
			}
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
				var type = gantt.hasChild(task.id) ? gantt.config.types.project
						: gantt.config.types.task;
				if (type != task.type) {
					task.type = type;
					gantt.updateTask(id);
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
			rap.getRemoteObject(cObj).call("gridMenuClicked", "");
		},

		onGridRowMenuClick : function(id, task) {
			var cObj = rap.getObject(id);
			rap.getRemoteObject(cObj).call("gridRowMenuClicked", task);
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