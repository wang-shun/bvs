package com.bizvisionsoft.mongocodex.tools;


import org.bson.Document;

import com.bizvisionsoft.annotations.md.mongocodex.IAutoGenerator;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;

public abstract class SNGen implements IAutoGenerator {

	@Override
	public Object generate(String name, String key, Class<?> type) {
		Number typedValue = new Long(1);
		if (Integer.class.isAssignableFrom(type)) {
			typedValue = typedValue.intValue();
		} else if (Short.class.isAssignableFrom(type)) {
			typedValue = typedValue.shortValue();
		}

		MongoCollection<Document> c = getSeqCollection(name);
		Document doc = c.findOneAndUpdate(Filters.eq("_id", key), Updates.inc("value", typedValue),
				new FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.AFTER));

		return doc.get("value");
	}

	protected abstract MongoCollection<Document> getSeqCollection(String name);

}
