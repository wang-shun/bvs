package com.bizvisionsoft.bruidesigner.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.part.ViewPart;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.AssemblyLink;
import com.bizvisionsoft.bruicommons.model.ContentArea;
import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruicommons.model.Site;
import com.bizvisionsoft.bruidesigner.model.ModelToolkit;
import com.bizvisionsoft.bruidesigner.model.SiteLoader;

public class SiteView extends ViewPart implements PropertyChangeListener {
	public static final String ID = "com.bizvisionsoft.bruidesigner.view.SiteView";
	private TreeViewer viewer;

	class ViewContentProvider implements ITreeContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (parent instanceof Site[]) {
				return (Object[]) parent;
			}
			return new Object[0];
		}

		public Object getParent(Object child) {
			return null;
		}

		public Object[] getChildren(Object parent) {
			if (parent instanceof ModelObject) {
				if (!((ModelObject) parent).hasListeners("name", SiteView.this)) {
					((ModelObject) parent).addPropertyChangeListener("name", SiteView.this);
				}
				if (!((ModelObject) parent).hasListeners("enabled", SiteView.this)) {
					((ModelObject) parent).addPropertyChangeListener("enabled", SiteView.this);
				}
				if (!((ModelObject) parent).hasListeners("defaultAssembly", SiteView.this)) {
					((ModelObject) parent).addPropertyChangeListener("defaultAssembly", SiteView.this);
				}
			}

			if (parent instanceof Site) {
				return ((Site) parent).getPages().toArray(new Page[0]);
			} else if (parent instanceof Page) {
				return new Object[] { ((Page) parent).getSidebar(), ((Page) parent).getHeadbar(),
						((Page) parent).getFootbar(), ((Page) parent).getContentArea() };
			} else if (parent instanceof ContentArea) {
				return ((ContentArea) parent).getAssemblyLinks().toArray(new AssemblyLink[0]);
			}

			return new Object[0];
		}

		public boolean hasChildren(Object parent) {
			Object[] children = getChildren(parent);
			return children.length > 0;
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL| SWT.FULL_SELECTION);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(ModelToolkit.createLabelProvider());
		viewer.setAutoExpandLevel(2);
		viewer.setInput(new Site[] { SiteLoader.site });
		viewer.addDoubleClickListener(
				e -> ModelToolkit.openEditor(((IStructuredSelection) e.getSelection()).getFirstElement(), null));
		getSite().setSelectionProvider(viewer);
		setMenu();
	}

	private void setMenu() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IMenuService mSvc = (IMenuService) window.getService(IMenuService.class);
		MenuManager menuManager = new MenuManager();
		mSvc.populateContributionManager(menuManager, "popup:bruidesigner.site");
		Menu createContextMenu = menuManager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(createContextMenu);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void addPage() {
		Page page = ModelToolkit.createPage(SiteLoader.site);
		viewer.refresh(SiteLoader.site);
		viewer.setExpandedElements(new Object[] { SiteLoader.site });
		viewer.expandToLevel(page, TreeViewer.ALL_LEVELS);
		viewer.setSelection(new StructuredSelection(page), true);
	}

	public void removePage(Page page) {
		ModelToolkit.removePage(page);
		viewer.refresh(SiteLoader.site);
	}

	public void addAssemblies(ContentArea ca, List<String> assysIds) {
		ModelToolkit.addAssemblies(ca, assysIds);
		viewer.refresh(ca);
		viewer.expandToLevel(ca, TreeViewer.ALL_LEVELS);
	}

	public void removeAssembly(AssemblyLink link) {
		Assembly assembly = ModelToolkit.getAssembly(link.getId());
		TreeItem item = (TreeItem) viewer.testFindItem(link);
		ContentArea ca = (ContentArea) item.getParentItem().getData();
		ModelToolkit.removeAssembly(ca, assembly);
		viewer.refresh(ca);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		viewer.update(event.getSource(), null);
	}
}
