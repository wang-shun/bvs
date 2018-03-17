package com.bizvisionsoft.bruiengine.assembly.field;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;

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
		List<Bson> filters = new ArrayList<Bson>();
		if (!fromText.isEmpty())
			try {
				filters.add(new BasicDBObject("$gte", Double.parseDouble(fromText)));
			} catch (Exception e) {
			}
		if (!toText.isEmpty())
			try {
				filters.add(new BasicDBObject("$lte", Double.parseDouble(toText)));
			} catch (Exception e) {
			}
		if (filters.isEmpty()) {
			return null;
		} else if (filters.size() == 1) {
			return filters.get(0);
		} else {
			return filters;
		}
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
		String fromText = from.getText().trim();
		String toText = to.getText().trim();
		// ���ͼ��
		// ������
		Float _from = null;
		Float _to = null;
		try {
			if (!fromText.isEmpty())
				_from = Float.parseFloat(fromText);
			if (!toText.isEmpty())
				_to = Float.parseFloat(toText);
		} catch (Exception e) {
			throw new Exception(fieldConfig.getFieldText() + "Ҫ��������ֵ��");
		}

		if (_from != null && _to != null && _from > _to)
			throw new Exception(fieldConfig.getFieldText() + "��Сֵ����С�����ֵ��");

	}

}
