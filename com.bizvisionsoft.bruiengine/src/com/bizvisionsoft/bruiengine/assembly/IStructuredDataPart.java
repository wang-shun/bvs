package com.bizvisionsoft.bruiengine.assembly;

import com.mongodb.BasicDBObject;

public interface IStructuredDataPart {

	void add(Object parent, Object child);

	void doModify(Object element, Object newElement, BasicDBObject r);

	void replaceItem(Object element, Object newElement);

	void doDelete(Object element);

	void refresh(Object parent);
	
	void refreshAll();

	Object doGetEditInput(Object element);

}
