package com.bizvisionsoft.bruiengine.assembly.field;

import java.util.Optional;

import com.mongodb.BasicDBObject;

public class NumberRangeQueryField extends NumberRangeField {

	public NumberRangeQueryField() {
	}

	@Override
	public void setValue(Object value) {
	}

	@Override
	public Object getValue() {
		String fromText = from.getText().trim();
		String toText = to.getText().trim();

		BasicDBObject filter = null;
		if (!fromText.isEmpty())
			try {
				filter = new BasicDBObject("$gte", Double.parseDouble(fromText));
			} catch (Exception e) {
			}
		if (!toText.isEmpty())
			try {
				filter = Optional.ofNullable(filter).orElse(new BasicDBObject()).append("$lte",
						Double.parseDouble(toText));
			} catch (Exception e) {
			}
		return filter;
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
		String fromText = from.getText().trim();
		String toText = to.getText().trim();
		// 类型检查
		// 浮点数
		Float _from = null;
		Float _to = null;
		try {
			if (!fromText.isEmpty())
				_from = Float.parseFloat(fromText);
			if (!toText.isEmpty())
				_to = Float.parseFloat(toText);
		} catch (Exception e) {
			throw new Exception(fieldConfig.getFieldText() + "要求输入数值。");
		}

		if (_from != null && _to != null && _from > _to)
			throw new Exception(fieldConfig.getFieldText() + "最小值必须小于最大值。");

	}

}
