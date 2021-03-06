package com.bizvisionsoft.bruiengine.assembly;

import java.util.List;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

import com.bizivisionsoft.widgets.schedule.Schedulers;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.GetContent;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiDataSetEngine;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.ActionMenu;
import com.bizvisionsoft.service.tools.Check;

public class SchedulerPart implements IPostSelectionProvider {

	@Inject
	private IBruiService bruiService;

	@Inject
	private BruiAssemblyContext context;

	@GetContent("schedulers")
	private Schedulers schedulers;

	private ListenerList<ISelectionChangedListener> postSelectionChangedListeners = new ListenerList<>();

	private BruiDataSetEngine dataSetEngine;

	private Object selectedItem;

	private Assembly config;

	@Init
	private void init() {
		this.config = context.getAssembly();
		dataSetEngine = BruiDataSetEngine.create(config, bruiService, context);
	}

	private Composite createSticker(Composite parent) {
		StickerPart sticker = new StickerPart(config);

		sticker.context = context;
		sticker.service = bruiService;
		sticker.createUI(parent);
		return sticker.content;
	}

	@CreateUI
	public void createUI(Composite parent) {

		Composite panel;
		if (config.isHasTitlebar()) {
			panel = createSticker(parent);
		} else {
			panel = parent;
		}

		panel.setLayout(new FillLayout());
		schedulers = (Schedulers) new Schedulers(panel, config.getSchedulerType()).setContainer(config.getName());

		if (dataSetEngine != null) {
			loadData();
		}

		context.setSelectionProvider(this);

		schedulers.addListener(SWT.Selection, e -> selected(e));
	}

	private void loadData() {
		// ��ѯ����
		List<?> input = (List<?>) dataSetEngine.query(null, context, "list");
		schedulers.setInput(input);
		if ("timeline".equals(config.getSchedulerType())) {
			try {
				List<?> sections = (List<?>) dataSetEngine.query(null, context, "section");
				schedulers.setSection(sections);
			} catch (Exception e2) {
			}
		}
	}

	private void selected(Event e) {
		this.selectedItem = e.data;
		fireSelectionChanged(e);
		List<Action> rowActions = config.getRowActions();
		if (!Check.isNotAssigned(rowActions)) {
			new ActionMenu(bruiService).setAssembly(config).setContext(context).setInput(e.data).setActions(rowActions)
					.setEvent(e).open();
		}
	}

	private void fireSelectionChanged(Event e) {
		SelectionChangedEvent event = new SelectionChangedEvent(this, new StructuredSelection(e.data));
		postSelectionChangedListeners.forEach(i -> i.selectionChanged(event));
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		postSelectionChangedListeners.add(listener);
	}

	@Override
	public ISelection getSelection() {
		if (selectedItem == null) {
			return StructuredSelection.EMPTY;
		}
		return new StructuredSelection(selectedItem);
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		postSelectionChangedListeners.remove(listener);
	}

	@Override
	public void setSelection(ISelection selection) {
	}

	@Override
	public void addPostSelectionChangedListener(ISelectionChangedListener listener) {
		postSelectionChangedListeners.add(listener);
	}

	public void removePostSelectionChangedListener(ISelectionChangedListener listener) {
		postSelectionChangedListeners.remove(listener);
	}

}
