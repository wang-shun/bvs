package com.bizvisionsoft.bruidesigner.command;

import org.eclipse.jface.viewers.TreeViewer;

public interface ITreePart {

	default void expandTree() {
		getTree().expandAll();
	}

	default void collapseTree() {
		getTree().collapseAll();
	}
	
	TreeViewer getTree();

}
