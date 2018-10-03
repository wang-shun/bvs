package com.bizvisionsoft.mongocodex.tools;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;

import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;
import org.bson.json.JsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizvisionsoft.mongocodex.codec.CodexProvider;
import com.mongodb.BasicDBObject;

public class BsonTools {

	public static Logger logger = LoggerFactory.getLogger(BsonTools.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static BasicDBObject getBson(Object input, boolean ignoreNull, String[] containFields,
			String[] ignoreFields) {
		Codec codec = CodexProvider.getRegistry().get(input.getClass());
		StringWriter sw = new StringWriter();
		codec.encode(new JsonWriter(sw), input, EncoderContext.builder().build());
		String json = sw.toString();
		BasicDBObject result = BasicDBObject.parse(json);

		BasicDBObject _result = new BasicDBObject();
		Iterator<String> iter = result.keySet().iterator();
		while (iter.hasNext()) {
			String k = iter.next();
			if (ignoreFields != null && Arrays.asList(ignoreFields).contains(k)) {
				continue;
			}
			Object v = result.get(k);
			if (v == null && ignoreNull && (containFields == null
					|| (containFields != null && !Arrays.asList(containFields).contains(k)))) {
				continue;
			}
			_result.append(k, v);
		}
		return _result;
	}

	public static BasicDBObject getBson(Object input, String... ignoreFields) {
		return getBson(input, true, null, ignoreFields);
	}

}
