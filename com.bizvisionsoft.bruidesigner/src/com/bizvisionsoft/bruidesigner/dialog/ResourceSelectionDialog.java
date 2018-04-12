package com.bizvisionsoft.bruidesigner.dialog;

import java.io.File;
import java.io.FileFilter;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;

import com.bizvisionsoft.bruidesigner.model.ModelToolkit;

public class ResourceSelectionDialog extends SelectionDialog {

	class FolderContentProvider implements ITreeContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof File[]) {
				return (File[]) inputElement;
			}
			return new File[0];
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			return ((File) parentElement).listFiles();
		}

		@Override
		public Object getParent(Object element) {
			return ((File) element).getParentFile();
		}

		@Override
		public boolean hasChildren(Object element) {
			String[] list = ((File) element).list();
			return list != null && list.length > 0;
		}

	}

	private TreeViewer viewer;

	public ResourceSelectionDialog(Shell parentShell) {
		super(parentShell);
		setTitle("请选择资源文件");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(OK).setEnabled(false);
	}
	

	@Override
	protected Control createDialogArea(Composite container) {
		Composite parent = new Composite(container,SWT.NONE);
		parent.setLayout(new GridLayout(2,true));
		
		viewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		IContentProvider provider = new FolderContentProvider();
		viewer.setContentProvider(provider);
		viewer.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {
				return ((File) element).getName();
			}

			@Override
			public Image getImage(Object element) {
				if (((File) element).isDirectory())
					return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
			}
		});

		viewer.setInput(ModelToolkit.getSiteResourceFolder().listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		}));
		viewer.addDoubleClickListener(e -> {
			okPressed();
		});

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 400;
		gd.widthHint = 400;
		viewer.getTree().setLayoutData(gd);

		
		Label label = new Label(parent,SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 400;
		gd.widthHint = 400;
		label.setLayoutData(gd);

		
		viewer.addSelectionChangedListener(e->{
			File file = (File) e.getStructuredSelection().getFirstElement();
			getButton(OK).setEnabled(file.isFile());
			if(file.isFile()) {
//				try {
//					FileInputStream stream = new FileInputStream(file);
//					Image image = new Image(Display.getCurrent(), stream);
//					label.setImage(image);
//					stream.close();
//				} catch (Exception e1) {
//				}
			}
		});
		
		return parent;
	}

	protected void okPressed() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		File file = (File) selection.getFirstElement();
		if (file.isFile()) {
			setResult(selection.toList());
			super.okPressed();
		}
	}

}
