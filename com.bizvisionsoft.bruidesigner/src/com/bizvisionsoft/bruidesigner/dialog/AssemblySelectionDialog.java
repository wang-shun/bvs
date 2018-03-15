package com.bizvisionsoft.bruidesigner.dialog;

import java.util.Comparator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruidesigner.Activator;
import com.bizvisionsoft.bruidesigner.model.ModelToolkit;
import com.bizvisionsoft.bruidesigner.model.SiteLoader;

public class AssemblySelectionDialog extends FilteredItemsSelectionDialog {

	public AssemblySelectionDialog(Shell shell, boolean multi) {
		super(shell, multi);
		LabelProvider labelProvider = ModelToolkit.createLabelProvider();
		setListLabelProvider(labelProvider);
		setDetailsLabelProvider(labelProvider);
		setTitle("选择添加的组件");
		setInitialPattern("?"); //$NON-NLS-1$
	}

	@Override
	protected Control createExtendedContentArea(Composite parent) {
		return null;
	}

	@Override
	protected IDialogSettings getDialogSettings() {
		IDialogSettings settings = Activator.getDefault().getDialogSettings()
				.getSection("AssemblySelectionDialogDialogSettings");
		if (settings == null) {
			settings = Activator.getDefault().getDialogSettings()
					.addNewSection("AssemblySelectionDialogDialogSettings");
		}
		return settings;
	}
	

	@Override
	protected IStatus validateItem(Object item) {
		return Status.OK_STATUS;
	}

	@Override
	protected ItemsFilter createFilter() {
		return new ItemsFilter() {

			@Override
			public boolean matchItem(Object item) {
				return matches(((Assembly) item).getName()) || matches(((Assembly) item).getId());
			}

			@Override
			public boolean isConsistentItem(Object item) {
				return true;
			}

		};
	}

	@Override
	protected Comparator<Assembly> getItemsComparator() {
		return new Comparator<Assembly>() {

			@Override
			public int compare(Assembly a0, Assembly a1) {
				return a0.getName().compareTo(a1.getName());
			}

		};
	}

	@Override
	protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter,
			IProgressMonitor progressMonitor) throws CoreException {
		SiteLoader.site.getAssyLib().getAssys().forEach(assy -> {
			contentProvider.add(assy, itemsFilter);
		});
	}

	@Override
	public String getElementName(Object item) {
		return ModelToolkit.getText(item);
	}
	

}
