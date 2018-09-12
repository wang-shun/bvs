package com.bizvisionsoft.sqldb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.List;
import java.util.function.Consumer;

import org.bson.Document;

public class SqlQuery {

	private String datasource;

	private String sql;

	private boolean lower;

	public SqlQuery(String datasource) {
		this.datasource = datasource;
	}

	public SqlQuery sql(String sql) {
		this.sql = sql;
		return this;
	}

	public List<Document> into(List<Document> arr) {
		forEach(d -> arr.add(d));
		return arr;
	}

	public Document first() {
		try {
			Connection conn = SqlDB.s.getConnection(datasource);
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			Document doc = null;
			if (rs.next()) {
				doc = new Document();
				for (int i = 1; i < meta.getColumnCount() + 1; i++) {
					String name = meta.getColumnName(i);
					Object value = rs.getObject(name);
					doc.put(getKey(name), value);
				}
			}
			rs.close();
			stat.close();
			SqlDB.s.freeConnection(datasource, conn);
			return doc;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String getKey(String name) {
		if (lower)
			return name.toLowerCase();
		else
			return name.toUpperCase();

	}

	public void forEach(Consumer<Document> each) {
		try {
			Connection conn = SqlDB.s.getConnection(datasource);
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			while (rs.next()) {
				Document doc = new Document();
				for (int i = 1; i < meta.getColumnCount() + 1; i++) {
					String name = meta.getColumnName(i);
					Object value = rs.getObject(name);
					doc.put(getKey(name), value);
				}
				each.accept(doc);
			}
			rs.close();
			stat.close();
			SqlDB.s.freeConnection(datasource, conn);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public SqlQuery changeKeyCase(boolean lower) {
		this.lower = lower;
		return this;
	}

}
