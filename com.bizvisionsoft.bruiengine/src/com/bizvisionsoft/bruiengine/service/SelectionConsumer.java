package com.bizvisionsoft.bruiengine.service;

import org.eclipse.jface.viewers.ISelectionProvider;

@FunctionalInterface
public interface SelectionConsumer {
	
	void run(ISelectionProvider selectionProvider, Object firstElement);

}
