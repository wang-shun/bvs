package com.bizvisionsoft.bruidesigner;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.bizvisionsoft.bruidesigner.view.AssyLibView;
import com.bizvisionsoft.bruidesigner.view.SiteView;

/**
 * Configures the perspective layout. This class is contributed through the
 * plugin.xml.
 */
public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setFixed(true);

		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);

		IFolderLayout folder = layout.createFolder("left", IPageLayout.LEFT, 0.4f, editorArea);

		folder.addView(SiteView.ID);
		folder.addView(AssyLibView.ID);
//		folder.addView(DataSourcesView.ID);

	}
}
