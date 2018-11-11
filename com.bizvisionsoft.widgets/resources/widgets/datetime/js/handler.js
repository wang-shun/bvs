(function() {
	'use strict';

	rap.registerTypeHandler("bizvision.datetime", {

		factory : function(properties) {
			return new bizvision.datetime(properties);
		},

		destructor : "destroy",

		properties : [ "renderSetting" ]

	});

	if (!window.bizvision) {
		window.bizvision = {};
	}

	bizvision.datetime = function(properties) {
		bindAll(this, [ "layout", "onReady", "onSend", "onRender", "destroy",
				"change", "done" ]);
		this.renderSetting = rap.getObject(properties.renderSetting);
		this.parent = rap.getObject(properties.parent);

		if (properties.selector) {
			this.element = document.createElement("div");
		} else {
			this.element = document.createElement("input");
			this.element.className = "layui-input";
		}
		this.parent.append(this.element);
		this.parent.addListener("Dispose", this.destroy);
		rap.on("render", this.onRender);
	};

	bizvision.datetime.prototype = {

		ready : false,

		onRender : function() {
			if (this.element.parentNode) {
				rap.off("render", this.onRender);
				var input = this.renderSetting;
				input.elem = this.element;
				input.btns = [ 'clear', 'now', 'confirm' ];
				input.trigger = "click";
				input.change = this.change;
				input.done = this.done;

				layui.laydate.render(input);
				this.ready = true;
			}
		},

		change : function(value, date, endDate) {
			rap.getRemoteObject(this).call("change", {
				'value' : value,
				'date' : date,
				'endDate' : endDate
			});
		},

		done : function(value, date, endDate) {
			rap.getRemoteObject(this).call("done", {
				'value' : value,
				'date' : date,
				'endDate' : endDate
			});
		},

		setRenderSetting : function(renderSetting) {
			this.renderSetting = renderSetting;
			if (this.ready) {
				rap.on("render", this.onRender);
			}
		},

		onSend : function() {
			// if (this.editor.checkDirty()) {
			// rap.getRemoteObject(this).set("text", this.editor.getData());
			// this.editor.resetDirty();
			// }
		},

		destroy : function() {
			if (this.element.parentNode) {
				rap.off("send", this.onSend);
				// this.editor.destroy();
				this.element.parentNode.removeChild(this.element);
			}
		},

		layout : function() {
			// if (this.ready) {
			// var area = this.parent.getClientArea();
			// this.element.style.left = area[0] + "px";
			// this.element.style.top = area[1] + "px";
			// // this.editor.resize(area[2], area[3]);
			// }
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