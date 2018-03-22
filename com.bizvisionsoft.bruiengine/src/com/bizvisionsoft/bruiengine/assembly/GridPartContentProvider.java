package com.bizvisionsoft.bruiengine.assembly;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiEngine;

public class GridPartContentProvider implements ITreeContentProvider {

	private Assembly gridConfig;

	public GridPartContentProvider(Assembly gridConfig) {
		this.gridConfig = gridConfig;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object value) {
		if (value instanceof List) {
			return ((List<?>) value).toArray();
		} else if (value instanceof Object[]) {
			return (Object[]) value;
		}
		return new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		Object value = BruiEngine.getStructureValue(parentElement, gridConfig.getName(), "list", new Object[0]);
		if (value instanceof List) {
			return ((List<?>) value).toArray();
		} else if (value instanceof Object[]) {
			return (Object[]) value;
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		Object value = BruiEngine.getStructureValue(element, gridConfig.getName(), "count", 0);
		return value instanceof Number && ((Number) value).longValue() > 0;
	}

}
