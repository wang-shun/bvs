Scheduler.plugin(function(e) {
	e.config.day_date="%M%d日%D";
	e.config.default_date="%Y年%M%j日";
	e.config.month_date="%Y年%M";
	e.locale = {
		date : {
			month_full : [ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月",
					"九月", "十月", "十一月", "十二月" ],
			month_short : [ "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月",
					"9月", "10月", "11月", "12月" ],
			day_full : [ "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" ],
			day_short : [ "周日", "周一", "周二", "周三", "周四", "周五", "周六" ]
		},
		labels : {
			dhx_cal_today_button : "今日",
			day_tab : "日",
			week_tab : "周",
			month_tab : "月",
			new_event : "新日程",
			icon_save : "保存",
			icon_cancel : "取消",
			icon_details : "详细",
			icon_edit : "编辑",
			icon_delete : "删除",
			confirm_closing : "确认关闭",
			confirm_deleting : "确认删除?",
			section_description : "描述",
			section_time : "期间",
			full_day : "全天",
			confirm_recurring : "是否将日程设置为重复模式？",
			section_recurring : "重复周期",
			button_recurring : "禁用",
			button_recurring_open : "启用",
			button_edit_series : "编辑系列",
			button_edit_occurrence : "编辑实例",
			agenda_tab : "议程",
			date : "日期",
			description : "描述",
			year_tab : "年",
			week_agenda_tab : "周议程",
			grid_tab : "表格",
			drag_to_create : "Drag to create",
			drag_to_move : "Drag to move",
			message_ok : "OK",
			message_cancel : "Cancel",
			next : "Next",
			prev : "Previous",
			year : "Year",
			month : "Month",
			day : "Day",
			hour : "Hour",
			minute : "Minute"
		}
	}
});
// # sourceMappingURL=../sources/locale/locale_en.js.map
