package com.bizvisionsoft.bruidesigner.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.part.ViewPart;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Folder;
import com.bizvisionsoft.bruidesigner.command.ITreePart;
import com.bizvisionsoft.bruidesigner.model.ModelToolkit;
import com.bizvisionsoft.bruidesigner.model.SiteLoader;

public class FolderView extends ViewPart implements PropertyChangeListener, ITreePart {

	public class FolderContentProvider implements ITreeContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Folder[]) {
				return (Folder[]) inputElement;
			}
			return getChildren(inputElement);
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Folder) {
				ArrayList<Object> result = new ArrayList<Object>();
				result.addAll(((Folder) parentElement).getChildren());
				result.addAll(SiteLoader.site.getAssysByFolder(((Folder) parentElement).getId()));
				return result.toArray();
			} else {
				return new Object[0];
			}
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}

	}

	public static final String ID = "com.bizvisionsoft.bruidesigner.view.FolderView";
	private TreeViewer viewer;
	private Text searcher;
	private FolderContentProvider provider;

	public FolderView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());

		searcher = new Text(parent, SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		searcher.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				if (e.detail == SWT.ICON_SEARCH) {
					search(searcher.getText());
				} else if (e.detail == SWT.ICON_CANCEL) {
					search("");
				}
			}
		});
		searcher.setMessage("组件完整的名称,id或者标题");
		searcher.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		viewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.setAutoExpandLevel(2);
		provider = new FolderContentProvider();
		viewer.setContentProvider(provider);
		viewer.setLabelProvider(ModelToolkit.createLabelProvider());
		Folder folder = SiteLoader.site.getRootFolder();
		if (folder == null) {
			folder = ModelToolkit.createFolder(null);
			SiteLoader.site.setRootFolder(folder);
		}
		viewer.setInput(new Folder[] { folder });
		viewer.addDoubleClickListener(e -> {
			Object ele = ((IStructuredSelection) e.getSelection()).getFirstElement();
			if (ele instanceof Folder) {
				InputDialog id = new InputDialog(parent.getShell(), "输入目录名称", "", "", null);
				if (InputDialog.OK == id.open()) {
					((Folder) ele).setName(id.getValue());
					viewer.update(ele, null);
				}
			} else {
				ModelToolkit.openEditor(ele, null);
			}
		});
		getSite().setSelectionProvider(viewer);

		folder.addPropertyChangeListener("name", FolderView.this);
		setMenu();

		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	private void search(String text) {
		String searchText = text.trim();
		if (searchText.isEmpty()) {
			viewer.resetFilters();
			viewer.expandAll();
		} else {
			ViewerFilter filter = new ViewerFilter() {
				@Override
				public boolean select(Viewer viewer, Object parentElement, Object element) {
					return match(element, searchText);
				}
			};
			viewer.setFilters(new ViewerFilter[] { filter });
			viewer.expandAll();
		}
	}

	private boolean match(Object element, String searchText) {
		if (element instanceof Assembly) {
			return searchText.equals(((Assembly) element).getId()) || searchText.equals(((Assembly) element).getName())
					|| searchText.equals(((Assembly) element).getStickerTitle());
		}
		return Arrays.asList(provider.getChildren(element)).stream().anyMatch(o -> match(o, searchText));
	}

	private void setMenu() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IMenuService mSvc = (IMenuService) window.getService(IMenuService.class);
		MenuManager menuManager = new MenuManager();
		mSvc.populateContributionManager(menuManager, "popup:bruidesigner.folder");
		Menu createContextMenu = menuManager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(createContextMenu);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		viewer.update(event.getSource(), null);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void createFolder(Folder parentFolder) {
		ModelToolkit.createFolder(parentFolder);
		viewer.refresh(parentFolder);
		viewer.expandToLevel(parentFolder, 1);
	}

	public void deleteFolder(Folder folder) {
		if (folder.getChildren() != null && folder.getChildren().size() > 0) {
			MessageDialog.openError(getSite().getShell(), "删除目录", "目录不为空时不可删除。");
			return;
		}

		List<Assembly> ass = SiteLoader.site.getAssysByFolder(folder.getId());
		if (ass != null && ass.size() > 0) {
			MessageDialog.openError(getSite().getShell(), "删除目录", "目录不为空时不可删除。");
			return;
		}

		if (folder.id.equals(SiteLoader.site.getRootFolder().getId())) {
			MessageDialog.openError(getSite().getShell(), "删除目录", "根目录不可删除。");
			return;
		}

		TreeItem item = (TreeItem) viewer.testFindItem(folder);
		Optional.ofNullable(item.getParentItem()).ifPresent(i -> {
			Folder parentFolder = (Folder) i.getData();
			parentFolder.getChildren().remove(folder);
			viewer.refresh(parentFolder);
		});
	}

	public void createAssembly(String type, Folder folder) {
		Assembly assy = ModelToolkit.createAssembly(type, folder.getId());
		assy.addPropertyChangeListener("name", this);
		viewer.refresh(folder);
		viewer.expandToLevel(folder, 1);
	}

	public void createJsonAssembly(String json, Folder folder) {
		try {
			Assembly assy;
			assy = ModelToolkit.createJsonAssembly(json, folder.getId());
			assy.addPropertyChangeListener("name", this);
			viewer.refresh(folder);
			viewer.expandToLevel(folder, 1);
		} catch (Exception e) {
			MessageDialog.openError(getViewSite().getShell(), "添加组件失败", e.getMessage());
		}
	}

	public void removeAssembly(Assembly assy) {
		assy.removePropertyChangeListener("name", this);
		ModelToolkit.deleteAssembly(assy);
		TreeItem item = (TreeItem) viewer.testFindItem(assy);
		Optional.ofNullable(item.getParentItem()).ifPresent(i -> {
			Folder parentFolder = (Folder) i.getData();
			viewer.refresh(parentFolder);
		});
	}

	public void AddAssembliesToFolder(Object[] result, Folder folder) {
		Arrays.asList(result).forEach(a -> {
			Assembly assembly = (Assembly) a;
			assembly.setFolderId(folder.getId());
		});
		viewer.refresh();
	}

	public void duplicateAssembly(Assembly assy) {
		TreeItem item = (TreeItem) viewer.testFindItem(assy);
		Folder folder = (Folder) item.getParentItem().getData();
		Assembly newAssy = ModelToolkit.duplicateAssembly(assy);
		newAssy.addPropertyChangeListener("name", this);
		viewer.refresh(folder);
	}

	@Override
	public TreeViewer getTree() {
		return viewer;
	}

}
