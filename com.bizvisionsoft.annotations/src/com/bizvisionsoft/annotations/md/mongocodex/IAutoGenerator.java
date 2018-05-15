package com.bizvisionsoft.annotations.md.mongocodex;

public interface IAutoGenerator<T> {

	Object generate(T model, String name, String key, Class<?> t);

}
