package com.bizvisionsoft.mongocodex.codec;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StringWriter;

import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.json.JsonReader;
import org.bson.json.JsonWriter;

import com.mongodb.BasicDBObject;


@SuppressWarnings({ "rawtypes", "unchecked" })
public interface JsonExternalizable extends Externalizable {

	public default void writeExternal(ObjectOutput out) throws IOException {
		String json = encodeJson();
		out.writeUTF(json);
	}

	public default String encodeJson() {
		Codec codec = CodexProvider.getRegistry().get(getClass());
		StringWriter sw = new StringWriter();
		codec.encode(new JsonWriter(sw), this, EncoderContext.builder().build());
		return sw.toString();
	}

	public default BasicDBObject encodeBson() {
		Codec codec = CodexProvider.getRegistry().get(getClass());
		StringWriter sw = new StringWriter();
		codec.encode(new JsonWriter(sw), this, EncoderContext.builder().build());
		String json = sw.toString();
		return BasicDBObject.parse(json);
	}

	public default void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		String json = in.readUTF();
		decodeJson(json);
	}

	public default JsonExternalizable decodeJson(String json) {
		Codec codec = CodexProvider.getRegistry().get(getClass());
		if (codec instanceof Codex) {
			Codex codex = (Codex) codec;
			codex.decode(this, new JsonReader(json), DecoderContext.builder().build());
		}
		return this;
	}

	public default JsonExternalizable decodeBson(BasicDBObject dbo) {
		Codec codec = CodexProvider.getRegistry().get(getClass());
		if (codec instanceof Codex) {
			Codex codex = (Codex) codec;
			codex.decode(this, new JsonReader(dbo.toJson()), DecoderContext.builder().build());
		}
		return this;
	}

}
