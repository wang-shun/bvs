package com.bizvisionsoft.mongocodex.codec;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.MongoClient;

@SuppressWarnings("rawtypes")
public class CodexProvider implements CodecProvider {

	public CodexProvider() {

	}

	public static CodecRegistry getRegistry() {
		CodexProvider provider = new CodexProvider();
		CodecRegistry modelCodeRegistry = CodecRegistries.fromProviders(provider);
		CodecRegistry defaultCodecRegistry = MongoClient.getDefaultCodecRegistry();
		return CodecRegistries.fromRegistries(modelCodeRegistry, defaultCodecRegistry);
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
		try {
			return MongoClient.getDefaultCodecRegistry().get(clazz);
		} catch (Exception e) {
			return new Codex(clazz, registry);
		}
	}

}
