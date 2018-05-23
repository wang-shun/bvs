package com.bizvisionsoft.mongocodex.codec;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bson.BsonBinarySubType;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWriter;
import org.bson.BsonReader;
import org.bson.BsonReaderMark;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.Binary;
import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.EncodingType;
import com.bizvisionsoft.annotations.md.mongocodex.Exclude;
import com.bizvisionsoft.annotations.md.mongocodex.Generator;
import com.bizvisionsoft.annotations.md.mongocodex.GetValue;
import com.bizvisionsoft.annotations.md.mongocodex.IAutoGenerator;
import com.bizvisionsoft.annotations.md.mongocodex.Persistence;
import com.bizvisionsoft.annotations.md.mongocodex.PostDecoding;
import com.bizvisionsoft.annotations.md.mongocodex.PreEncoding;
import com.bizvisionsoft.annotations.md.mongocodex.SetValue;
import com.bizvisionsoft.annotations.md.mongocodex.Strict;
import com.bizvisionsoft.mongocodex.Activator;
import com.mongodb.MongoClient;

public class Codex<T> implements CollectibleCodec<T> {

	private static final BsonTypeClassMap DEFAULT_BSON_TYPE_CLASS_MAP = new BsonTypeClassMap();

	private static final String DTYPE = "DTYPE";

	private CodecRegistry registry;

	private static final String ID_FIELD_NAME = "_id";

	private final Class<T> clazz;

	private Map<String, Field> setFields;

	private Map<String, Field> getFields;

	private Map<String, Method> setMethods;

	private Map<String, Method> getMethods;

	/**
	 * 是否保存类型
	 */
	private boolean encodingType;

	private boolean autoCoding;

	public Codex(Class<T> clazz, CodecRegistry registry) {
		this.clazz = clazz;
		this.registry = registry;

		init();
	}

	private void init() {
		setFields = new HashMap<String, Field>();
		getFields = new HashMap<String, Field>();
		setMethods = new HashMap<String, Method>();
		getMethods = new HashMap<String, Method>();

		autoCoding = clazz.getAnnotation(Strict.class) == null;
		// autoCoding = true;

		Arrays.asList(clazz.getDeclaredFields()).forEach(m -> {

			if (Optional.ofNullable(m.getAnnotation(Exclude.class)).map(a -> a.value()).orElse(false)) {
				return;
			}

			boolean hasSetValue = Optional.ofNullable(m.getAnnotation(SetValue.class)).map(a -> a.value()).map(n -> {
				if (Persistence.DEFAULT.equals(n)) {
					n = m.getName();
				}
				setFields.put(n, m);
				m.setAccessible(true);
				return true;
			}).orElse(false);

			boolean hasGetValue = Optional.ofNullable(m.getAnnotation(GetValue.class)).map(a -> a.value()).map(n -> {
				if (Persistence.DEFAULT.equals(n)) {
					n = m.getName();
				}
				getFields.put(n, m);
				m.setAccessible(true);
				return true;
			}).orElse(false);

			boolean hasPersis = Optional.ofNullable(m.getAnnotation(Persistence.class)).map(a -> a.value()).map(n -> {
				if (Persistence.DEFAULT.equals(n)) {
					n = m.getName();
				}
				getFields.put(n, m);
				setFields.put(n, m);
				m.setAccessible(true);
				return true;
			}).orElse(false);

			if (autoCoding && !hasSetValue && !hasGetValue && !hasPersis) {
				String n = m.getName();
				getFields.put(n, m);
				setFields.put(n, m);
				m.setAccessible(true);
			}
		});

		Arrays.asList(clazz.getDeclaredMethods()).forEach(m -> {
			Optional.ofNullable(m.getAnnotation(SetValue.class)).map(a -> a.value()).ifPresent(n -> {
				setMethods.put(n, m);
				m.setAccessible(true);
			});
			Optional.ofNullable(m.getAnnotation(GetValue.class)).map(a -> a.value()).ifPresent(n -> {
				getMethods.put(n, m);
				m.setAccessible(true);
			});
		});

		encodingType = clazz.getAnnotation(EncodingType.class) != null;
	}

	@Override
	public Class<T> getEncoderClass() {
		return clazz;
	}

	@Override
	public void encode(BsonWriter writer, final T model, EncoderContext encoderContext) {
		preEncoding(model);
		writer.writeStartDocument();
		beforeFields(writer, model, encoderContext);
		getGetNames(encodingType).forEach(name -> {
			if (!skipField(encoderContext, name)) {
				Object value = valueForRead(model, name);
				try {
					if (encoderContext.isEncodingCollectibleDocument()) {
						if (value == null || value.equals("")) {// TODO 需要考虑空字符串的问题
							value = generateValue(model, name);
						}
					}
				} catch (Exception e) {
				}

				if (value == null || (value instanceof List && ((List<?>) value).isEmpty())
						|| (value instanceof Map && ((Map<?, ?>) value).isEmpty())) {
					if (!encoderContext.isEncodingCollectibleDocument()) {
						writer.writeName(name);
						writeValue(writer, encoderContext, null);
					}
				} else {
					writer.writeName(name);
					// boolean encodingSubtype = encodingSubtype(name);
					writeValue(writer, encoderContext, value);
				}
			}
		});

		writer.writeEndDocument();
	}

	@SuppressWarnings("unused")
	private boolean encodingSubtype(String name) {
		Field f = getFields.get(name);
		if (f != null && f.getAnnotation(EncodingType.class) != null) {
			return true;
		}
		Method m = getMethods.get(name);
		if (m != null && m.getAnnotation(EncodingType.class) != null) {
			return true;
		}

		if (autoCoding) {
			Class<?> type = getTypeOf(name);
			if (type == Object.class) {
				return true;
			}
			// 取出最终的泛型
			type = (Class<?>) getFinalTypeParameter(type);
			if (type == null || type == Object.class) {
				return true;
			} else {
				try {
					// 默认类型不保存DTYPE
					MongoClient.getDefaultCodecRegistry().get(type);
					return false;
				} catch (Exception e) {
					// final类型不保存
					if (Modifier.isFinal(type.getModifiers())) {
						return false;
					}
					// 抽象类型保存
					if (Modifier.isAbstract(type.getModifiers())) {
						return true;
					}
					// 接口保存
					if (Modifier.isInterface(type.getModifiers())) {
						return true;
					}
					return false;
				}
			}
		}

		return false;
	}

	private Type getFinalTypeParameter(Type fieldType) {
		if (fieldType instanceof ParameterizedType) {
			ParameterizedType gType = (ParameterizedType) fieldType;
			Type rawType = gType.getRawType();
			int index = 0;
			if (Iterable.class.isAssignableFrom((Class<?>) rawType)) {
				index = 0;
			} else if (Map.class.isAssignableFrom((Class<?>) rawType)) {
				index = 1;
			}
			Type[] arg = gType.getActualTypeArguments();
			if (arg.length > index) {
				return getFinalTypeParameter(arg[index]);
			} else {
				return null;
			}
		} else {
			return fieldType;
		}
	}

	@Override
	public T decode(BsonReader reader, DecoderContext decoderContext) {
		T model;
		try {
			model = clazz.newInstance();

			return decode(model, reader, decoderContext);
		} catch (InstantiationException | IllegalAccessException e) {
			// GongoActivator.logError(e.getMessage()+" class:"+clazz, e);
			throw new IllegalArgumentException(e.getMessage() + " class:" + clazz);
		}
	}

	public T decode(T model, BsonReader reader, DecoderContext decoderContext) {
		HashSet<String> names = getSetNames();
		reader.readStartDocument();
		while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
			String fieldName = reader.readName();
			if (names.contains(fieldName)) {
				Object value = readValue(reader, fieldName, false, decoderContext, registry);
				valueToWrite(model, fieldName, value);
			} else {
				reader.skipValue();
			}
		}

		reader.readEndDocument();
		postDecoding(model);

		return model;
	}

	@Override
	public T generateIdIfAbsentFromDocument(T document) {
		if (!documentHasId(document)) {
			generateId(document);
		}
		return document;
	}

	private Object generateValue(T model, String name) throws InstantiationException, IllegalAccessException {
		Generator a = null;
		Class<?> t = null;

		Field f = getFields.get(name);
		if (f != null) {
			a = f.getAnnotation(Generator.class);
			if (a != null) {
				t = f.getType();
			}
		}

		if (a == null) {
			Method m = getMethods.get(name);
			if (m != null) {
				a = m.getAnnotation(Generator.class);
				if (a != null) {
					t = m.getReturnType();
				}
			}
		}

		if (a == null) {
			return null;
		}

		@SuppressWarnings("unchecked")
		IAutoGenerator<T> generator = (IAutoGenerator<T>) a.generator().newInstance();
		String key = a.key();
		if (key.equals(Generator.DEFAULT_KEY)) {
			key = clazz.getSimpleName();
		}
		final Object value = generator.generate(model, a.name(), key, t);

		if (f != null) {
			f.set(model, value);
		}

		final String callback = a.callback();
		if (!Generator.NONE_CALLBACK.equals(callback)) {
			Arrays.asList(clazz.getDeclaredMethods()).parallelStream().filter(m -> m.getName().equals(callback))
					.findFirst().ifPresent(m -> {
						try {
							m.setAccessible(true);
							m.invoke(model, value);
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
		}
		return value;

	}

	@Override
	public boolean documentHasId(T document) {
		return getId(document) != null;
	}

	@Override
	public BsonValue getDocumentId(T document) {
		if (!documentHasId(document)) {
			IllegalStateException e = new IllegalStateException("The document does not contain an _id");
			Activator.logError(e.getMessage());
			throw e;
		}

		BsonDocument idHoldingDocument = new BsonDocument();
		BsonWriter writer = new BsonDocumentWriter(idHoldingDocument);
		writer.writeStartDocument();
		writer.writeName(ID_FIELD_NAME);
		Object id = getId(document);
		if (id instanceof ObjectId) {
			Codec<ObjectId> codec = registry.get(ObjectId.class);
			EncoderContext.builder().build().encodeWithChildContext(codec, writer, (ObjectId) id);
		} else if (id instanceof String) {
			Codec<String> codec = registry.get(String.class);
			EncoderContext.builder().build().encodeWithChildContext(codec, writer, (String) id);
		} else if (id instanceof Integer) {
			Codec<Integer> codec = registry.get(Integer.class);
			EncoderContext.builder().build().encodeWithChildContext(codec, writer, (Integer) id);
		} else if (id instanceof Long) {
			Codec<Long> codec = registry.get(Long.class);
			EncoderContext.builder().build().encodeWithChildContext(codec, writer, (Long) id);
		} else if (id instanceof Double) {
			Codec<Double> codec = registry.get(Double.class);
			EncoderContext.builder().build().encodeWithChildContext(codec, writer, (Double) id);
		} else if (id instanceof Float) {
			Codec<Float> codec = registry.get(Float.class);
			EncoderContext.builder().build().encodeWithChildContext(codec, writer, (Float) id);
		}
		writer.writeEndDocument();
		return idHoldingDocument.get(ID_FIELD_NAME);
	}

	private boolean preEncoding(Object model) {
		return Arrays.asList(clazz.getMethods()).parallelStream()
				.filter(m -> m.getAnnotation(PreEncoding.class) != null).findFirst().map(c -> {
					try {
						c.invoke(model);
						return true;
					} catch (Exception e) {
						return false;
					}
				}).orElse(false);
	}

	private boolean postDecoding(Object model) {
		return Arrays.asList(clazz.getMethods()).parallelStream()
				.filter(m -> m.getAnnotation(PostDecoding.class) != null).findFirst().map(c -> {
					try {
						c.invoke(model);
						return true;
					} catch (Exception e) {
						return false;
					}
				}).orElse(false);
	}

	private void beforeFields(final BsonWriter bsonWriter, T model, final EncoderContext encoderContext) {
		if (encoderContext.isEncodingCollectibleDocument()) {
			Object _id = getId(model);
			if (_id != null) {
				bsonWriter.writeName("_id");
				writeValue(bsonWriter, encoderContext, _id);
			}
		}
	}

	private Object getId(T model) {
		return valueForRead(model, "_id");
	}

	private void set_Id(T model, Object _id) {
		valueToWrite(model, "_id", _id);
	}

	private void generateId(T model) {
		try {
			Object id = generateValue(model, "_id");
			if (id == null) {
				id = new ObjectId();
			}
			set_Id(model, id);
		} catch (InstantiationException | IllegalAccessException e) {
			Activator.logError("_id generated failure: ", e);
		}
	}

	private HashSet<String> getGetNames(boolean encodingType) {
		HashSet<String> set = new HashSet<String>();
		set.addAll(getFields.keySet());
		set.addAll(getMethods.keySet());
		if (this.encodingType || encodingType) {
			set.add(DTYPE);
		}
		return set;
	}

	private HashSet<String> getSetNames() {
		HashSet<String> set = new HashSet<String>();
		set.addAll(setFields.keySet());
		set.addAll(setMethods.keySet());
		return set;
	}

	private Class<?> getTypeOf(String name) {
		Method method = setMethods.get(name);
		if (method != null) {
			if (method.getParameterCount() == 1) {
				return method.getParameterTypes()[0];
			}
		}

		Field field = setFields.get(name);
		if (field != null) {
			return field.getType();
		}

		return null;
	}

	private Class<?> getParameterTypeOf(String name) {
		Method method = setMethods.get(name);
		if (method != null) {
			if (method.getParameterCount() == 1) {
				Type[] gTypes = method.getGenericParameterTypes();
				if (gTypes.length == 1 && gTypes[0] instanceof ParameterizedType) {
					Type[] types = ((ParameterizedType) gTypes[0]).getActualTypeArguments();
					if (types != null && types.length > 0) {
						return ((Class<?>) types[0]);
					}
				}
			}
		}

		Field field = setFields.get(name);
		if (field != null) {
			Type fieldType = field.getGenericType();
			if (fieldType instanceof ParameterizedType) {
				ParameterizedType gType = (ParameterizedType) fieldType;
				return ((Class<?>) gType.getActualTypeArguments()[0]);
			}
		}

		return null;
	}

	private boolean skipField(final EncoderContext encoderContext, final String key) {
		return encoderContext.isEncodingCollectibleDocument() && key.equals("_id");
	}

	private Object valueForRead(T model, String name) {

		if (name.equals(DTYPE)) {
			return clazz.getName();
		}

		boolean hasError = false;

		Method method = getMethods.get(name);
		if (method != null) {
			try {
				Object value = method.invoke(model);
				return enumValueForRead(value);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				hasError = true;
				Activator.logWarning("  Encode by Method failure, class:" + getClass() + " name:" + name + " Method:"
						+ method + " Reason:" + e.getClass() + " Message:" + e.getMessage());
			}
		}

		Field field = getFields.get(name);
		if (field != null) {
			try {
				Object value = field.get(model);
				return enumValueForRead(value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				hasError = true;
				Activator.logWarning("  Encode by Field failure, class:" + model.getClass() + " name:" + name
						+ " Field:" + field + " Reason:" + e.getClass() + " Message:" + e.getMessage());
			}
		}
		if (hasError) {
			Activator.logError("Encode failure, return null. class:" + model.getClass() + " name:" + name + " Method:"
					+ method + " Field:" + field);
		}

		return null;
	}

	/**
	 * 设置字段值
	 * 
	 * @param name
	 *            对应标注字段的名称
	 * @param value
	 *            值
	 * @return
	 */
	private void valueToWrite(T model, String name, Object value) {
		Method method = setMethods.get(name);
		if (method != null) {
			try {
				method.invoke(model, value);
				return;
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				Activator.logError("   Method invoke failure. name:" + name + " value:" + value + ", Reason:"
						+ e.getClass() + ", Message:" + e.getMessage());
			}
		}

		Field field = setFields.get(name);
		if (field != null) {
			try {
				field.set(model, value);
				return;
			} catch (IllegalAccessException | IllegalArgumentException e) {
				Activator.logWarning("   Field set failure. name:" + name + " value:" + value + ", Reason:"
						+ e.getClass() + ", Message" + e.getMessage());
			}
		}
		if (field != null || method != null) {
			Activator.logError("Decode failure, class:" + model.getClass() + " name:" + name + " value:" + value);
		}
	}

	private Object enumValueForRead(Object value) {
		if (value != null && value.getClass().isEnum()) {
			return ((Enum<?>) value).name();
		}
		return value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void writeValue(final BsonWriter writer, EncoderContext encoderContext, Object value) {
		if (value == null) {
			writer.writeNull();
		} else if (value instanceof Iterable) {
			writeIterable(writer, (Iterable<Object>) value, encoderContext.getChildContext());
		} else if (value instanceof Map) {
			writeMap(writer, (Map<String, Object>) value, encoderContext.getChildContext());
		} else {
			Codec codec = registry.get(value.getClass());
			if (codec instanceof Codex) {
				((Codex) codec).encode(writer, value, encoderContext.getChildContext());
			} else {
				encoderContext.encodeWithChildContext(codec, writer, value);
			}
		}
	}

	private void writeIterable(final BsonWriter writer, final Iterable<Object> list,
			final EncoderContext encoderContext) {
		writer.writeStartArray();
		for (final Object value : list) {
			writeValue(writer, encoderContext, value);
		}
		writer.writeEndArray();
	}

	private void writeMap(final BsonWriter writer, final Map<String, Object> map, final EncoderContext encoderContext) {
		writer.writeStartDocument();

		for (final Map.Entry<String, Object> entry : map.entrySet()) {
			if (skipField(encoderContext, entry.getKey())) {
				continue;
			}
			writer.writeName(entry.getKey());
			writeValue(writer, encoderContext, entry.getValue());
		}
		writer.writeEndDocument();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object readValue(final BsonReader reader, String fieldName, boolean isListItem,
			final DecoderContext decoderContext, CodecRegistry registry) {
		BsonType bsonType = reader.getCurrentBsonType();
		if (bsonType == BsonType.NULL) {
			reader.readNull();
			return null;
		} else if (bsonType == BsonType.ARRAY) {
			return readList(reader, fieldName, decoderContext);
		} else if (bsonType == BsonType.DOCUMENT) {
			return readDocument(reader, fieldName, isListItem, decoderContext);
		} else if (bsonType == BsonType.BINARY) {
			return readBinary(reader, fieldName, decoderContext);
		}
		Class<?> clazz = DEFAULT_BSON_TYPE_CLASS_MAP.get(bsonType);
		Codec<?> codec = registry.get(clazz);
		Object value = codec.decode(reader, decoderContext);

		Class<?> targetClass = getTypeOf(fieldName);
		if (targetClass != null) {
			// 处理枚举
			if (value instanceof String && targetClass.isEnum()) {
				return Enum.valueOf((Class) targetClass, (String) value);
			}
			// 处理Short
			if (targetClass == Short.class) {
				if (value instanceof Number) {
					return ((Number) value).shortValue();
				} else if (value != null) {
					Activator.logError("Cannot decode nonnumberic value to Short, value is " + value);
					return null;
				}
			}
		}
		return value;

	}

	private List<Object> readList(final BsonReader reader, String fieldName, final DecoderContext decoderContext) {
		reader.readStartArray();
		List<Object> list = new ArrayList<Object>();
		while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
			list.add(readValue(reader, fieldName, true, decoderContext, registry));
		}
		reader.readEndArray();
		return list;
	}

	private Object readDocument(BsonReader reader, String fieldName, boolean isListItem,
			DecoderContext decoderContext) {

		/*
		 * 处理DTYPE规定类型的情况
		 */
		Codec<Document> codec = registry.get(Document.class);
		BsonReaderMark mark = reader.getMark();
		Document document = codec.decode(reader, decoderContext);

		String dtype = document.getString(DTYPE);
		Class<?> targetClass = null;
		if (dtype != null) {
			try {
				targetClass = clazz.getClassLoader().loadClass(dtype);
			} catch (ClassNotFoundException e) {
			}
		}

		/*
		 * DTYPE没有时获取字段类型
		 */
		if (targetClass == null) {
			targetClass = isListItem ? getParameterTypeOf(fieldName) : getTypeOf(fieldName);
		}

		/*
		 * 类型不可能为空
		 */
		if (targetClass == null) {
			mark.reset();
			reader.skipValue();
			Activator.logError("Cannot get Codec for name:" + fieldName);
			return null;
		}

		/*
		 * 如果类型是Document或者Document超类，直接返回
		 */
		if (targetClass.isAssignableFrom(Document.class)) {
			mark.reset();
			reader.skipValue();
			return document;
		}

		/*
		 * 获取解码器
		 */
		Codec<?> targetCodec = registry.get(targetClass);
		if (targetCodec != null) {
			mark.reset();
			return targetCodec.decode(reader, decoderContext);
		} else {
			reader.reset();
			reader.skipValue();
			Activator.logError("Cannot get Codec for name:" + fieldName);
			return null;
		}
	}

	private Object readBinary(BsonReader reader, String fieldName, DecoderContext decoderContext) {
		byte bsonSubType = reader.peekBinarySubType();
		if (bsonSubType == BsonBinarySubType.UUID_STANDARD.getValue()
				|| bsonSubType == BsonBinarySubType.UUID_LEGACY.getValue()) {
			return registry.get(UUID.class).decode(reader, decoderContext);
		} else {
			return registry.get(Binary.class).decode(reader, decoderContext).getData();
		}
	}

}
