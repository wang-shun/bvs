(function() {
	'use strict';

	rap.registerTypeHandler("bizvision.echarts", {

		factory : function(properties) {
			return new bizvision.echarts(properties);
		},

		destructor : "destroy",

		properties : [ "option" ]

	});

	if (!window.bizvision) {
		window.bizvision = {};
	}

	bizvision.echarts = function(properties) {
		bindAll(this, [ "layout", "onRender", "onReady" ]);
		this.parent = rap.getObject(properties.parent);
		this.element = document.createElement("div");
		this.parent.append(this.element);
		this.parent.addListener("Resize", this.layout);
		rap.on("render", this.onRender);
	};

	bizvision.echarts.prototype = {

		setOption : function(option) {
			this.option = option;
			this.onRender();
		},

		onRender : function() {
			if (this.element.parentNode) {
				rap.off("render", this.onRender);
				this.layout();
				this.chart = echarts.init(this.element,"light");
				this.chart.setOption(this.option,true);
			}
		},

		destroy : function() {
			if(this.chart && !this.chart.isDisposed()){
				this.chart.dispose();
			}
			if (this.element && this.element.parentNode) {
				this.element.parentNode.removeChild(this.element);
			}
		},

		layout : function() {
			var area = this.parent.getClientArea();
			this.element.style.width = area[2] + "px";
			this.element.style.height = area[3] + "px";
			if (this.chart) {
				this.chart.resize(area[2],area[3]);
			}
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