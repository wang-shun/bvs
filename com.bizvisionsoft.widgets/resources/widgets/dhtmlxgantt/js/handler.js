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
		bindAll(this, [ "layout", "onReady", "onSend", "onRender", "destroy","onGridMenuClick" ]);
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
				var remoteId = rap.getRemoteObject(this)._.id;

				// ////////////////////////////////////////////////////////////////////////////////
				// 初始配置
				gantt.config.touch = "force";
				gantt.config.grid_resize = true;
				gantt.config.keep_grid_width = false;
				gantt.config.start_on_monday = false;
				gantt.config.details_on_create = true;

				// ////////////////////////////////////////////////////////////////////////////////
				// 表格列配置
//				gantt.config.columns = [ 
//					{ name : "add", label : "",width : 24,resize: false },
//					{ name : "text",label : "工作", tree : true,width : 320,resize : true },
//					{ name : "start_date", label : "开始", align : "center", width : 96, resize : true }, 
//					{ name : "end_date", label : "完成", align : "center", width : 96, hide:true, resize : true }, 
//					{ name : "duration", label : "工期", align : "right", width : 40, resize : true }
//					];
				
				// ////////////////////////////////////////////////////////////////////////////////
				// 表格列和菜单配置
				var colHeader = "<div class='gantt_grid_head_cell gantt_grid_head_add' onclick='bizvision.dhtmlxgantt.prototype.onGridMenuClick(\""+remoteId+"\")'></div>";
				var colContent = function (task) {
					return ("<div class='gantt_row_btn_menu' onclick='bizvision.dhtmlxgantt.prototype.onGridRowMenuClick(\""+remoteId+"\","+JSON.stringify(task)+")'></div>");
				};
				gantt.config.columns = [ 
					{ name : "menu", label : colHeader ,width : 34,align : "center",resize: false,template: colContent },
					{ name : "text",label : "工作", tree : true,width : 320,resize : true },
					{ name : "start_date", label : "开始", align : "center", width : 96, resize : true }, 
					{ name : "end_date", label : "完成", align : "center", width : 96, hide:true, resize : true }, 
					{ name : "duration", label : "工期", align : "right", width : 40, resize : true }
					];
				
				// ////////////////////////////////////////////////////////////////////////////////
				// 配置刻度
				gantt.config.scale_unit = "month";
				gantt.config.step = 1;
				gantt.config.date_scale = "%Y年%n月";
				gantt.config.min_column_width = 40;
				gantt.config.scale_height = 90;
				var weekScaleTemplate = function(date) {
					var dateToStr = gantt.date.date_to_str("%n月%j日");
					var endDate = gantt.date.add(gantt.date
							.add(date, 1, "week"), -1, "day");
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
				
				// ////////////////////////////////////////////////////////////////////////////////
				// 配置布局
				gantt.config.layout = {
					  cols: [
					    {
					      width:480,
					      min_width: 320,
					      rows:[
					        {view: "grid", scrollX: "gridScroll", scrollable: true, scrollY: "scrollVer"}, 
					        {view: "scrollbar", id: "gridScroll", group:"horizontal"}       ]
					    },
					    {resizer: true, width: 1},
					    {
					      rows:[
					        {view: "timeline", scrollX: "scrollHor", scrollY: "scrollVer"},
					        {view: "scrollbar", id: "scrollHor", group:"horizontal"}        ]
					    },
					    {view: "scrollbar", id: "scrollVer"}
					  ]
				};
				
				// ////////////////////////////////////////////////////////////////////////////////
				// 配置任务样式
				gantt.config.task_height = 20;

				// ////////////////////////////////////////////////////////////////////////////////
				// 配置任务类型
				gantt.config.types.level1 = "level1";
				gantt.locale.labels.type_level1 = "一级计划";

				gantt.config.types.level2 = "level2";
				gantt.locale.labels.type_level2 = "二级计划";

				gantt.config.types.level3 = "level3";
				gantt.locale.labels.type_level3 = "三级计划";

				gantt.templates.task_class = function(start, end, task) {
					if (task.type == gantt.config.types.level1) {
						return "level1_task";
					} else if (task.type == gantt.config.types.level2) {
						return "level2_task";
					} else if (task.type == gantt.config.types.level3) {
						return "";
					}
					return "";
				};

				// ////////////////////////////////////////////////////////////////////////////////
				// 配置周末
				gantt.templates.task_cell_class = function(task, date) {
					if (!gantt.isWorkTime(date))
						return "week_end";
					return "";
				};
				gantt.config.work_time = true;

				// ////////////////////////////////////////////////////////////////////////////////
				// 配置日期数据格式
				gantt.config.xml_date = "%Y-%m-%d %H:%i:%s";

				
				// ////////////////////////////////////////////////////////////////////////////////
				// 接受服务端配置
				if (this.config) {
					for ( var attr in this.config) {
						gantt.config[attr] = this.config[attr];
					}
				}
				
				// ////////////////////////////////////////////////////////////////////////////////
				// 初始化并加载数据
				gantt.init(this.element, this.initFrom, this.initTo);
				gantt.parse(this.inputData);

				// ////////////////////////////////////////////////////////////////////////////////
				// 
				
			}
		},
		
		onGridMenuClick: function(id){
			var cObj = rap.getObject(id);
			rap.getRemoteObject(cObj).call("gridMenuClicked", "");
		},
		
		onGridRowMenuClick: function(id, task){
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