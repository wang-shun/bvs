(function() {
	'use strict';

	rap.registerTypeHandler("bizvision.pagination", {

		factory : function(properties) {
			return new bizvision.pagination(properties);
		},

		destructor : "destroy",

		properties : [ "count", "limit", "layouts", "groups", "theme" ]

	});

	if (!window.bizvision) {
		window.bizvision = {};
	}

	bizvision.pagination = function(properties) {
		bindAll(this, [ "layout", "onReady", "onSend", "onRender", "destroy" ]);
		this.parent = rap.getObject(properties.parent);
		this.element = document.createElement("div");
		this.parent.append(this.element);
		// this.parent.addListener("Resize", this.layout);
		this.parent.addListener("Dispose", this.destroy);
		rap.on("render", this.onRender);
	};

	bizvision.pagination.prototype = {

		ready : false,

		onRender : function() {
			if (this.element.parentNode) {
				rap.off("render", this.onRender);
				var target = this.element;
				var cnt = this.count;
				var lmt = this.limit;
				var ly = this.layouts;
				var gps = this.groups;
				var the = this.theme;
				var remote = rap.getRemoteObject(this);
				layui.use('laypage', function() {
					var laypage = layui.laypage;

					laypage.render({
						elem : target,
						count : cnt,
						limit : lmt,
						layout : ly,
						groups : gps,
						theme : the,
						jump : function(obj, first) {
							if (!first) {
								remote.call("jump", obj);
							}
						}
					});

				});
				this.ready = true;
			}
		},

		onSend : function() {
			// if (this.editor.checkDirty()) {
			// rap.getRemoteObject(this).set("text", this.editor.getData());
			// this.editor.resetDirty();
			// }
		},

		setCount : function(count) {
			this.count = count;
			if (this.ready) {
				rap.on("render", this.onRender);
			}
		},

		setLimit : function(limit) {
			this.limit = limit;
			if (this.ready) {
				rap.on("render", this.onRender);
			}
		},

		setGroups : function(groups) {
			this.groups = groups;
			if (this.ready) {
				rap.on("render", this.onRender);
			}
		},

		setLayouts : function(layouts) {
			this.layouts = layouts;
			if (this.ready) {
				rap.on("render", this.onRender);
			}
		},

		setTheme : function(theme) {
			this.theme = theme;
			if (this.ready) {
				rap.on("render", this.onRender);
			}
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