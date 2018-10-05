(function() {
	'use strict';

	rap.registerTypeHandler("bizvision.widgethandler", {

		factory : function(properties) {
			return new bizvision.widgethandler(properties);
		},

		destructor : "destroy",

		properties : [ "name", "className", "disabledClassName", "htmlContent",
				"targetState" ],

		methods : [ "addEventListener", "removeEventListener",
				"addGestureAction", "removeGestureAction", "setTargetState",
				"animate", "sliding", "download"]

	});

	if (!window.bizvision) {
		window.bizvision = {};
	}

	bizvision.widgethandler = function(properties) {
		bindAll(this, [ "onRender", "addEventListener", "removeEventListener",
				"addGestureAction", "removeGestureAction", "gestureAction",
				"changeTargetState", "setTargetState", "animate", "sliding",
				"_sliding" ]);
		rap.on("render", this.onRender);
	};

	bizvision.widgethandler.prototype = {

		ready : false,

		onReady : function() {
			this.ready = true;
			if (this._className) {
				this.setClassName(this._className);
				delete this._className;
			}
		},

		destroy : function() {
		},

		setName : function(name) {
			this.name = name;
		},

		setTargetState : function(state) {
			if (this.targetState != state) {
				if (this.ready) {
					this.changeTargetState(state);
				} else {
					this.targetState = state;
				}
			}
		},

		setClassName : function(className) {
			this.className = className;
			if (this.ready) {
				this.onRender();
			} else {
				this._className = className;
			}
		},

		setDisabledClassName : function(disabledClassName) {
			$("[name='" + this.name + "']").removeClass(disabledClassName);
		},

		setHtmlContent : function(htmlContent) {
			this.htmlContent = htmlContent;
			if (this.ready) {
				this.onRender();
			} else {
				this._htmlContent = htmlContent;
			}
		},

		addEventListener : function(eo) {
			var ro = rap.getRemoteObject(this);
			$(window).bind(eo.event, function(event_, data_) {
				ro.call("gestureEvent", {
					"event" : eo.event,
					"data" : data_.description
				});
			});
		},

		removeEventListener : function(eo) {
			var ro = rap.getRemoteObject(this);
			$(window).unbind(eo.event);
		},

		addGestureAction : function(action) {
			this.gActAdd = action;
			delete this.gActRemove;
			if (this.ready) {
				this.onRender();
			}
		},

		removeGestureAction : function(action) {
			this.gActRemove = action;
			delete this.gActAdd;
			if (this.ready) {
				this.onRender();
			}
		},

		onRender : function() {
			rap.off("render", this.onRender);
			var el = $("[name='" + this.name + "']");
			if (this.className) {
				el.attr({
					"class" : this.className
				});
			}
			if (this.htmlContent) {
				el.html(this.htmlContent);
				el.css({
					"overflow-x" : "visible",
					"overflow-y" : "visible",
					"height" : "auto"
				});
				el.css({
					"overflow-x" : "hidden",
					"overflow-y" : "hidden",
					"height" : el.height() + "px"
				});
				rap.getRemoteObject(this).call("renderHtml", {
					"height" : el.height()
				});
			}
			if (!this.targetState) {
				this.targetState = "show";
			}
			if (this.gActAdd) {
				el.bind(this.gActAdd.event, this.gestureAction);
				el.bind("tapone", this.gestureAction);
			}
			if (this.gActRemove) {
				el.unbind(this.gActRemove.event);
				el.unbind("tapone");
			}
			if (this.slidingSetting) {
				this._sliding(this.slidingSetting);
			}

			this.ready = true;
		},

		gestureAction : function(event_, data_) {
			var _a = data_.description.split(":");

			if (this.gActAdd.type == "collapse") {
				if (data_.delta[0] && data_.delta[0].startX
						&& data_.delta[0].startY) {
					var x = Math.abs(data_.delta[0].startX);
					var y = Math.abs(data_.delta[0].startY);
					if (y > x) {
						return;
					}
				}
				if (_a[0] == "swipe" && _a[1] == 1) {
					if (_a[2] == "left" && this.targetState == "show") {

						this.changeTargetState("hide");
					} else if (_a[2] == "right" && this.targetState == "hide") {
						this.changeTargetState("show");
					}
				} else if (_a[0] == "tapone") {
					this.changeTargetState("hide");
				}

			}
		},

		changeTargetState : function(state) {
			if (this.gActAdd) {
				this.targetState = state;
				$("[name='" + this.gActAdd.target + "']").animate({
					width : this.targetState
				}, 300);
			}
		},

		animate : function(animation) {
			$("[name='" + this.name + "']").animate(animation);
		},

		sliding : function(slidingSetting) {
			if (this.ready) {
				this._sliding(slidingSetting);
			} else {
				this.slidingSetting = slidingSetting;
			}
		},

		_sliding : function(slidingSetting) {
			var ro = rap.getRemoteObject(this);
			var el1 = $("[name='" + slidingSetting.previous + "']");
			var el2 = $("[name='" + this.name + "']");
			el1.animate({
				"left" : slidingSetting.width
			}, 300);
			el2.animate({
				"left" : slidingSetting.width
			}, 300, function() {
				ro.call("sliding", {
					"event" : "sliding",
					"data" : slidingSetting.width
				});
			});
			delete this.slidingSetting;
		},
		
		download: function (parameter) {
		    var config = $.extend(true, { method: 'post' }, parameter);
		    var $form = $('<form target="_blank" style="display:none;" method="' + config.method + '" />');
		    $form.attr('action', config.url);
		    for (var key in config.data) {
		    	$form.append($('<input>',{type:'hidden',name:key,val:config.data[key] }));
		    }
		    $(document.body).append($form);
		    $form[0].submit();
		    $form.remove();
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