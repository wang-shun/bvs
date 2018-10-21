package com.bizvisionsoft.bruiengine.assembly;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

import com.bizivisionsoft.widgets.diagram.Diagram;
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
import com.mongodb.BasicDBObject;

public class TreePart implements IStructuredDataPart, IPostSelectionProvider, IDataSetEngineProvider {

	@Inject
	private IBruiService bruiService;

	@Inject
	private BruiAssemblyContext context;

	private Assembly config;

	@GetContent("tree")
	private Diagram tree;

	private ListenerList<ISelectionChangedListener> postSelectionChangedListeners = new ListenerList<>();

	private BruiDataSetEngine dataSetEngine;

	private Object selectedItem;

	@Init
	private void init() {
		config = context.getAssembly();
		dataSetEngine = BruiDataSetEngine.create(config, bruiService, context);
		Assert.isNotNull(dataSetEngine, config.getName() + "组件缺少数据集定义");
	}

	private Composite createSticker(Composite parent) {
		StickerPart sticker = new StickerPart(config);

		Action zoomIn = new Action();
		zoomIn.setType(Action.TYPE_CUSTOMIZED);
		zoomIn.setImage("/img/zoomin_w.svg");
		zoomIn.setStyle("info");
		sticker.addAction(zoomIn, e -> {
			tree.zoomIn();
		});

		Action zoomIOut = new Action();
		zoomIOut.setType(Action.TYPE_CUSTOMIZED);
		zoomIOut.setImage("/img/zoomOut_w.svg");
		zoomIOut.setStyle("info");
		sticker.addAction(zoomIOut, e -> {
			tree.zoomOut();
		});

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
		tree = new Diagram(panel).setContainer(config.getName());

		// 查询数据
		List<?> input = (List<?>) dataSetEngine.query(null, null, null, context);

		// 设置为gantt输入
		tree.setInputData(input);

		context.setSelectionProvider(this);

		List<Action> rowActions = config.getRowActions();
		if (!Check.isNotAssigned(rowActions)) {
			tree.addListener(SWT.Selection, e -> selected(e));
		}
	}

	private void selected(Event e) {
		this.selectedItem = e.data;
		new ActionMenu(bruiService).setAssembly(config).setContext(context).setInput(e.data)
				.setActions(config.getRowActions()).setEvent(e).open();
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

	@Override
	public void doModify(Object element, Object newElement, BasicDBObject newData) {
		if (dataSetEngine != null) {
			try {
				dataSetEngine.replace(element, newData, context);
				replaceItem(element, newElement);
			} catch (Exception e) {
				MessageDialog.openError(bruiService.getCurrentShell(), "更新", e.getMessage());
			}
		}
	}

	@Override
	public void doDelete(Object element) {
		if (dataSetEngine != null) {
			try {
				dataSetEngine.delete(element, null, context);
				tree.deleteItem(element);
			} catch (Exception e) {
				MessageDialog.openError(bruiService.getCurrentShell(), "删除", e.getMessage());
			}
		}
	}

	@Override
	public void replaceItem(Object element, Object newElement) {
		tree.updateItem(element, newElement);
	}

	@Override
	public void add(Object parent, Object child) {
		tree.addItem(child);
	}

	public void addAll(Object parent, List<?> childs) {
		tree.addItems(childs);
	}

	@Override
	public void refresh(Object parent) {
		// TODO 刷新树
	}

	@Override
	public Object doGetEditInput(Object element) {
		return dataSetEngine.query(element, context);
	}

	@Override
	public void refreshAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public BruiDataSetEngine getDataSetEngine() {
		return dataSetEngine;
	}

}
