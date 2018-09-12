package com.bizvisionsoft.annotations;

import com.mongodb.BasicDBObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.bson.Document;
import org.bson.types.ObjectId;

public class UniversalCommand {

	public static final String PARAM_TARGET_CLASS = "targetClass";

	private String targetClassName;

	private HashMap<String, Object> parameters = new HashMap<>();

	public void addParameter(String name, Object value) {
		parameters.put(name, value);
	}

	public <T> T getParameter(String name, Class<T> typeOfValue) {
		Object value = getParameter(parameters, name.split("\\."));
		if (value == null) {
			return null;
		}
		T tVal = getTypeValue(value, typeOfValue);
		return tVal;
	}

	@SuppressWarnings("unchecked")
	private <T> T getTypeValue(Object value, Class<T> typeOfValue) {
		if (typeOfValue.equals(BasicDBObject.class)) {
			if (value instanceof Map)
				return (T) getBasicDBObjectValue((Map<?, ?>) value);
		} else if (typeOfValue.equals(Document.class)) {
			if (value instanceof Map)
				return (T) getDocumentValue((Map<?, ?>) value);
		} else if (typeOfValue.equals(Integer.class)) {
			if (value instanceof Number)
				return (T) new Integer(((Number) value).intValue());
		} else if (typeOfValue.equals(Float.class)) {
			if (value instanceof Number)
				return (T) new Float(((Number) value).floatValue());
		} else if (typeOfValue.equals(Double.class)) {
			if (value instanceof Number)
				return (T) new Float(((Number) value).doubleValue());
		} else if (typeOfValue.equals(Long.class)) {
			if (value instanceof Number)
				return (T) new Long(((Number) value).longValue());
		} else if (typeOfValue.equals(Short.class)) {
			if (value instanceof Number)
				return (T) new Long(((Number) value).shortValue());
		} else if (typeOfValue.equals(Byte.class)) {
			if (value instanceof Number)
				return (T) new Byte(((Number) value).byteValue());
		}
		return (T) value;
	}

	private Object getDocumentValue(Map<?, ?> value) {
		// ´¦Àí_id
		Object v = handleSpecialKey(value);
		if (v != null) {
			return v;
		}

		Document result = new Document();
		value.entrySet().forEach(et -> {
			String key = (String) et.getKey();
			Object val = et.getValue();
			if (val instanceof Map) {
				val = getDocumentValue((Map<?, ?>) val);
			} else if (val instanceof List) {
				val = getListValue((List<?>) val, e -> getDocumentValue(e));
			}
			result.append(key, val);
		});
		return result;
	}

	private Object getBasicDBObjectValue(Map<?, ?> value) {
		Object v = handleSpecialKey(value);
		if (v != null) {
			return v;
		}

		BasicDBObject result = new BasicDBObject();
		value.entrySet().forEach(et -> {
			Object val = et.getValue();
			if (val instanceof Map) {
				val = getBasicDBObjectValue((Map<?, ?>) val);
			} else if (val instanceof List) {
				val = getListValue((List<?>) val, e -> getBasicDBObjectValue(e));
			}
			result.append((String) et.getKey(), val);
		});
		return result;
	}

	private List<?> getListValue(List<?> input, Function<Map<?, ?>, Object> fun) {
		ArrayList<Object> result = new ArrayList<>();
		input.forEach(val -> {
			if (val instanceof Map) {
				result.add(fun.apply((Map<?, ?>) val));
			} else if (val instanceof List<?>) {
				result.add(getListValue((List<?>) val, fun));
			} else {
				result.add(val);
			}
		});
		return result;
	}
	
	private Object handleSpecialKey(Map<?, ?> value) {
		String stid = (String) value.get("$oid");
		if (stid != null) {
			return new ObjectId(stid);
		}

		Double ldate = (Double) value.get("$date");
		if (ldate != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(ldate.longValue());
			return cal.getTime();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private Object getParameter(Map<String, Object> param, String[] names) {
		Object value = param.get(names[0]);
		if (names.length > 1) {
			String[] _names = new String[names.length - 1];
			System.arraycopy(names, 1, _names, 0, names.length - 1);
			return getParameter((Map<String, Object>) value, _names);
		} else {
			return value;
		}
	}

	public void setTargetClassName(String targetClassName) {
		this.targetClassName = targetClassName;
	}

	public String getTargetClassName() {
		return targetClassName;
	}

}
