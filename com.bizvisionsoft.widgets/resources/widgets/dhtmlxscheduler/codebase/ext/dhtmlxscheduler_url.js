/*
@license
dhtmlxScheduler v.5.0.0 Professional

This software can be used only as part of dhtmlx.com site.
You are not allowed to use it on any other site

(c) Dinamenta, UAB.


*/
Scheduler.plugin(function(e){e._get_url_nav=function(){for(var e={},t=(document.location.hash||"").replace("#","").split(","),a=0;a<t.length;a++){var n=t[a].split("=");2==n.length&&(e[n[0]]=n[1])}return e},e.attachEvent("onTemplatesReady",function(){function t(t){r=t,e.getEvent(t)&&e.showEvent(t)}var a=!0,n=e.date.str_to_date("%Y-%m-%d"),i=e.date.date_to_str("%Y-%m-%d"),r=e._get_url_nav().event||null;e.attachEvent("onAfterEventDisplay",function(e){return r=null,!0}),e.attachEvent("onBeforeViewChange",function(o,d,l,s){
if(a){a=!1;var _=e._get_url_nav();if(_.event)try{if(e.getEvent(_.event))return t(_.event),!1;var c=e.attachEvent("onXLE",function(){t(_.event),e.detachEvent(c)})}catch(u){}if(_.date||_.mode){try{this.setCurrentView(_.date?n(_.date):null,_.mode||null)}catch(u){this.setCurrentView(_.date?n(_.date):null,l)}return!1}}var h=["date="+i(s||d),"mode="+(l||o)];r&&h.push("event="+r);var p="#"+h.join(",");return document.location.hash=p,!0})})});
//# sourceMappingURL=../sources/ext/dhtmlxscheduler_url.js.map