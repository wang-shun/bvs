(function() {
	'use strict';

	rap.registerTypeHandler("bizvision.container", {

		factory : function(properties) {
			return new bizvision.container(properties);
		},

		destructor : "destroy",

		properties : []

	});

	if (!window.bizvision) {
		window.bizvision = {};
	}

	bizvision.container = function(properties) {
		bindAll(this, [ "layout", "onRender" ]);
		this.name = properties.name;
		this.target = properties.target;
		rap.on("render", this.onRender);
	};

	bizvision.container.prototype = {

		setTarget : function(target) {
			this.target = target;
			if (this.targetElement) {
				this.onRender();
			}
		},

		onRender : function() {
			rap.off("render", this.onRender);
			if (this.target) {
				$("[name='" + this.target+"']").append($("[name='" + this.name+"']"));
			}
		},

		destroy : function() {
		},

		layout : function() {

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