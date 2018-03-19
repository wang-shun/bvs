(function() {
	'use strict';

	rap.registerTypeHandler("bizvision.timeline", {

		factory : function(properties) {
			return new bizvision.timeline(properties);
		},

		destructor : "destroy",

		properties : [ "renderSetting" ]

	});

	if (!window.bizvision) {
		window.bizvision = {};
	}

	bizvision.timeline = function(properties) {
		bindAll(this, [ "layout", "onReady", "onSend", "onRender", "destroy",
				"createItem" ]);
		this.parent = rap.getObject(properties.parent);
		this.element = document.createElement("ul");
		this.element.className = "layui-timeline";
		this.element.style.setProperty('width', '100%');
		this.element.style.setProperty('height', 'auto');

		this.parent.append(this.element);

		this.renderSetting = rap.getObject(properties.renderSetting);
		this.parent.addListener("Dispose", this.destroy);
		rap.on("render", this.onRender);
	};

	bizvision.timeline.prototype = {

		onRender : function() {
			if (this.element.parentNode) {
				rap.off("render", this.onRender);

				while (this.element.hasChildNodes()) {
					this.element.removeChild(this.element.firstChild);
				}
				this.renderSetting.forEach(this.createItem);
				var height = this.element.offsetHeight;
				var width = this.element.offsetWidth;
				
				var sc = $("[name='timeline']").get(0);
				sc.style.setProperty('border', 'none');
				
				rap.getRemoteObject(this).call("rendered", {
					'width' : width,
					'height' : height
				});
			}
		},

		createItem : function(elem) {
			var li = document.createElement("li");
			li.className = "layui-timeline-item";

			var i = document.createElement("i");
			i.className = "layui-icon layui-timeline-axis";
			i.innerHTML = "&#xe63f;";
			li.appendChild(i);

			var c = document.createElement("div");
			c.className = "layui-timeline-content layui-text";
			var h = document.createElement("h3");
			h.className = "layui-timeline-title";
			h.innerHTML = elem.title;
			c.appendChild(h);
			var p = document.createElement("p");
			p.innerHTML = elem.content;
			c.appendChild(p);

			li.appendChild(c);

			this.element.appendChild(li);
		},

		setRenderSetting : function(renderSetting) {
			this.renderSetting = renderSetting;
			rap.on("render", this.onRender);
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