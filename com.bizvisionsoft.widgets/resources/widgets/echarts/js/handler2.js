(function() {
	'use strict';

	rap.registerTypeHandler("bizvision.carousel", {

		factory : function(properties) {
			return new bizvision.carousel(properties);
		},

		destructor : "destroy",

		properties : [ "renderSetting" ]

	});

	if (!window.bizvision) {
		window.bizvision = {};
	}

	bizvision.carousel = function(properties) {
		bindAll(this, [ "layout", "onReady", "onSend", "onRender", "destroy" ]);
		this.parent = rap.getObject(properties.parent);
		this.element = document.createElement("div");
		this.element.className = "layui-carousel";
		this.element.style.width="100%";
		this.element.style.height="100%";
		this.parent.append(this.element);

		this.item = document.createElement("div");
		this.item.setAttribute("carousel-item","");  

		this.element.appendChild(this.item);

		// this.parent.addListener("Resize", this.layout);
		this.renderSetting = rap.getObject(properties.renderSetting);
		this.parent.addListener("Dispose", this.destroy);
		rap.on("render", this.onRender);
	};

	bizvision.carousel.prototype = {

		ready : false,

		onRender : function() {
			if (this.element.parentNode) {
				rap.off("render", this.onRender);
				var pages = new Array();
				var caro = this.parent.$el.get()[0];
				caro.childNodes.forEach(function(elem) {
					if (elem.className != "layui-carousel") {
						pages.push(elem);
					}
				});
				var folder = this.item;

				pages.forEach(function(elem) {
					elem.style.width="100%";
					elem.style.height="100%";
					$(elem).appendTo(folder);
				});

				var input = this.renderSetting;
				input.elem = this.element;
				input.width = "100%";
				input.height = "100%";

				layui.use('carousel', function() {
					var carousel = layui.carousel;
					carousel.render(input);
				});
				this.ready = true;
			}
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