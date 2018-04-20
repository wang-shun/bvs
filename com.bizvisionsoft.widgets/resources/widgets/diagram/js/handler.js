(function() {
	'use strict';

	rap.registerTypeHandler("bizvision.diagram", {

		factory : function(properties) {
			return new bizvision.diagram(properties);
		},

		destructor : "destroy",

		properties : ["inputData" ],

		methods : [  ]

	});

	if (!window.bizvision) {
		window.bizvision = {};
	}

	bizvision.diagram = function(properties) {
		bindAll(this, [ "layout", "onReady", "onSend", "onRender", "destroy" ]);
		this.parent = rap.getObject(properties.parent);
		this.element = document.createElement("div");
		this.element.style.width = "100%";
		this.element.style.height = "100%";
		this.parent.append(this.element);

		this.parent.addListener("Dispose", this.destroy);
		this.parent.addListener("Resize", this.layout);

		rap.on("render", this.onRender);
	};

	bizvision.diagram.prototype = {

		onRender : function() {
			if (this.element.parentNode) {
				rap.off("render", this.onRender);

				// ////////////////////////////////////////////////////////////////////////////////
				// 初始化并加载数据
				this.diagram = new dhx.Diagram(this.element, { type: "org" ,defaultShapeType: "img-card"});
				this.diagram.data.parse(this.inputData);
			}
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
			this.diagram.paint();
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