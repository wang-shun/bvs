/*
@license
dhtmlxScheduler v.5.0.0 Professional

This software can be used only as part of dhtmlx.com site.
You are not allowed to use it on any other site

(c) Dinamenta, UAB.


*/
Scheduler.plugin(function(e){e.date.add_agenda=function(t){return e.date.add(t,1,"year")},e.templates.agenda_time=function(t,a,i){return i._timed?this.day_date(i.start_date,i.end_date,i)+" "+this.event_date(t):e.templates.day_date(t)+" &ndash; "+e.templates.day_date(a)},e.templates.agenda_text=function(e,t,a){return a.text},e.templates.agenda_date=function(){return""},e.date.agenda_start=function(){return e.date.date_part(e._currentDate())},e.attachEvent("onTemplatesReady",function(){function t(t){
if(t){var a=e.locale.labels,i=e._waiAria.agendaHeadAttrString(),n=e._waiAria.agendaHeadDateString(a.date),r=e._waiAria.agendaHeadDescriptionString(a.description);e._els.dhx_cal_header[0].innerHTML="<div "+i+" class='dhx_agenda_line'><div "+n+">"+a.date+"</div><span style='padding-left:25px' "+r+">"+a.description+"</span></div>",e._table_view=!0,e.set_sizes()}}function a(){var t=(e._date,e.get_visible_events());t.sort(function(e,t){return e.start_date>t.start_date?1:-1});for(var a,i=e._waiAria.agendaDataAttrString(),n="<div class='dhx_agenda_area' "+i+">",r=0;r<t.length;r++){
var l=t[r],o=l.color?"background:"+l.color+";":"",d=l.textColor?"color:"+l.textColor+";":"",s=e.templates.event_class(l.start_date,l.end_date,l);a=e._waiAria.agendaEventAttrString(l);var _=e._waiAria.agendaDetailsBtnString();n+="<div "+a+" class='dhx_agenda_line"+(s?" "+s:"")+"' event_id='"+l.id+"' style='"+d+o+(l._text_style||"")+"'><div class='dhx_agenda_event_time'>"+e.templates.agenda_time(l.start_date,l.end_date,l)+"</div>",n+="<div "+_+" class='dhx_event_icon icon_details'>&nbsp;</div>",n+="<span>"+e.templates.agenda_text(l.start_date,l.end_date,l)+"</span></div>";
}n+="<div class='dhx_v_border'></div></div>",e._els.dhx_cal_data[0].innerHTML=n,e._els.dhx_cal_data[0].childNodes[0].scrollTop=e._agendaScrollTop||0;var c=e._els.dhx_cal_data[0].childNodes[0],u=c.childNodes[c.childNodes.length-1];u.style.height=c.offsetHeight<e._els.dhx_cal_data[0].offsetHeight?"100%":c.offsetHeight+"px";var h=e._els.dhx_cal_data[0].firstChild.childNodes;e._els.dhx_cal_date[0].innerHTML=e.templates.agenda_date(e._min_date,e._max_date,e._mode),e._rendered=[];for(var r=0;r<h.length-1;r++)e._rendered[r]=h[r];
}var i=e.dblclick_dhx_cal_data;e.dblclick_dhx_cal_data=function(){if("agenda"==this._mode)!this.config.readonly&&this.config.dblclick_create&&this.addEventNow();else if(i)return i.apply(this,arguments)},e.attachEvent("onSchedulerResize",function(){return"agenda"==this._mode?(this.agenda_view(!0),!1):!0});var n=e.render_data;e.render_data=function(e){return"agenda"!=this._mode?n.apply(this,arguments):void a()};var r=e.render_view_data;e.render_view_data=function(){return"agenda"==this._mode&&(e._agendaScrollTop=e._els.dhx_cal_data[0].childNodes[0].scrollTop,
e._els.dhx_cal_data[0].childNodes[0].scrollTop=0),r.apply(this,arguments)},e.agenda_view=function(i){e._min_date=e.config.agenda_start||e.date.agenda_start(e._date),e._max_date=e.config.agenda_end||e.date.add_agenda(e._min_date,1),t(i),i?(e._cols=null,e._colsS=null,e._table_view=!0,a()):e._table_view=!1}})});
//# sourceMappingURL=../sources/ext/dhtmlxscheduler_agenda_view.js.map