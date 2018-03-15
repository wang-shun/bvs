package com.bizvisionsoft.mongocodex.annotations;

public interface IAutoGenerator {

	Object generate(String name, String key, Class<?> type);

}
