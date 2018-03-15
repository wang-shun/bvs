package com.bizvisionsoft.bruidesigner.model;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class ModelInput implements IEditorInput {
	
	private Object element;

	public ModelInput(Object element) {
		this.element = element;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return (T) element;
	}

	@Override
	public String getToolTipText() {
		return ModelToolkit.getToolTipText(element);
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getName() {
		return ModelToolkit.getText(element);
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ModelToolkit.getImageDescriptor(element);
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((element == null) ? 0 : element.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelInput other = (ModelInput) obj;
		if (element == null) {
			if (other.element != null)
				return false;
		} else if (!element.equals(other.element))
			return false;
		return true;
	}
	
	
}