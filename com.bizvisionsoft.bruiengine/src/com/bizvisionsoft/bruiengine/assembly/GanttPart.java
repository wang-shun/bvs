package com.bizvisionsoft.bruiengine.assembly;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizivisionsoft.widgets.gantt.ColumnConfig;
import com.bizivisionsoft.widgets.gantt.Config;
import com.bizivisionsoft.widgets.gantt.Gantt;
import com.bizivisionsoft.widgets.gantt.GanttEvent;
import com.bizivisionsoft.widgets.gantt.GanttEventCode;
import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.GetContent;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruiengine.BruiDataSetEngine;
import com.bizvisionsoft.bruiengine.BruiEventEngine;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.ActionMenu;
import com.bizvisionsoft.service.tools.Check;
import com.mongodb.BasicDBObject;

public class GanttPart implements IPostSelectionProvider ,IDataSetEngineProvider{
	
	public Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	private IBruiService bruiService;

	@Inject
	private BruiAssemblyContext context;

	private Assembly config;

	@GetContent("gantt")
	private Gantt gantt;

	private Config ganttConfig;

	private BruiDataSetEngine dataSetEngine;

	private List<?> tasks;

	private List<?> links;

	private BruiEventEngine eventEngine;

	private boolean highlightCriticalPath;

	private ListenerList<ISelectionChangedListener> postSelectionChangedListeners = new ListenerList<ISelectionChangedListener>();

	private ISelection selection;

	@Init
	private void init() {
		this.config = context.getAssembly();

		dataSetEngine = BruiDataSetEngine.create(config, bruiService, context);
		Assert.isNotNull(dataSetEngine, config.getName() + "组件缺少数据集定义");

		eventEngine = BruiEventEngine.create(config, bruiService, context);

		ganttConfig = Config.defaultConfig(config.isReadonly());

		// 配置操作列
		ganttConfig.brui_RowMenuEnable = !Check.isNotAssigned(config.getRowActions());
		ganttConfig.brui_HeadMenuEnable = !Check.isNotAssigned(config.getHeadActions());

		ganttConfig.columns = new ArrayList<>();
		// 配置列和表格宽度
		int gridWidth = 8;
		for (int i = 0; i < config.getColumns().size(); i++) {
			Column c = config.getColumns().get(i);

			ColumnConfig colConf = new ColumnConfig();
			ganttConfig.columns.add(colConf);

			colConf.label = c.getText();
			colConf.name = c.getName();
			colConf.resize = c.isResizeable();
			colConf.width = c.getWidth();
			colConf.tree = i == 0;
			switch (c.getAlignment()) {
			case SWT.CENTER:
				colConf.align = "center";
				break;
			case SWT.RIGHT:
				colConf.align = "right";
				break;
			default:
				colConf.align = "left";
				break;
			}
			if (c.isHide()) {
				colConf.hide = c.isHide();
			} else {
				gridWidth += c.getWidth();
			}
		}

		if (config.isGanttGridWidthCalculate()) {
			ganttConfig.grid_width = gridWidth - 8;//
			if (ganttConfig.brui_RowMenuEnable || ganttConfig.brui_HeadMenuEnable) {
				ganttConfig.grid_width += 34;
			}
		} else {
			ganttConfig.grid_width = config.getGanttGridWidth();
		}

		// 设置默认的时间刻度
		ganttConfig.brui_initScaletype = config.getGanttTimeScaleType();

		// 设置是否为对比甘特图
		ganttConfig.brui_enableGanttCompare = config.isEnableGanttCompare();

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
		gantt = new Gantt(panel, ganttConfig).setContainer(config.getName());

//		Date[] dateRange = dataSetEngine.getGanttInitDateRange();
//		if (dateRange != null && dateRange.length == 2) {
//			gantt.setInitDateRange(dateRange[0], dateRange[1]);
//		}
//		Calendar cal = Calendar.getInstance();
//		Date from = cal.getTime();
//		cal.add(Calendar.MONTH, 6);
//		Date to = cal.getTime();
//		gantt.setInitDateRange(from, to);

		// 查询数据
		try {
			tasks = (List<?>)dataSetEngine.getGanntInputData(new BasicDBObject(), context);
			links = (List<?>)dataSetEngine.getGanntInputLink(new BasicDBObject(), context);
			// 设置为gantt输入
			gantt.setInputData(tasks, links);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Layer.message(e.getMessage(), Layer.ICON_CANCEL);
		}

		// 设置必须的事件侦听
		addGanttEventListener(GanttEventCode.onGridHeaderMenuClick.name(), e -> showHeadMenu(e));
		addGanttEventListener(GanttEventCode.onGridRowMenuClick.name(), e -> showRowMenu(e));
		// addGanttEventListener(GanttEventCode.onTaskLinkBefore.name(), e ->
		// showLinkCreateEditor(e));

		dataSetEngine.attachListener((eventCode, m) -> {
			addGanttEventListener(eventCode, e1 -> {
				try {
					m.invoke(dataSetEngine.getTarget(), e1);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
					logger.error(e2.getMessage(), e2);
				}
			});
		});

		// 处理客户端事件侦听
		if (eventEngine != null) {
			eventEngine.attachListener((eventCode, m) -> {
				addGanttEventListener(eventCode, e1 -> {
					try {
						m.invoke(eventEngine.getTarget(), e1);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
						logger.error(e2.getMessage(), e2);
					}
				});
			});
		}

		//
		// addGanttEventListener(GanttEventCode.onAfterTaskDelete.name(), e1 ->
		// testEvent(e1));
		//
		// addGanttEventListener(GanttEventCode.onAfterTaskUpdate.name(), e1 ->
		// testEvent(e1));
		//
		// addGanttEventListener(GanttEventCode.onError.name(), e1 -> testEvent(e1));
		context.setSelectionProvider(this);

	}

	// private void testEvent(Event e1) {
	// }

	private void showRowMenu(Event e) {
		setSelection(new StructuredSelection(((GanttEvent) e).task));
		new ActionMenu(bruiService).setAssembly(config).setContext(context).setInput(((GanttEvent) e).task)
				.setActions(config.getRowActions()).setEvent(e).open();
	}

	private void showHeadMenu(Event e) {
		new ActionMenu(bruiService).setAssembly(config).setContext(context).setActions(config.getHeadActions())
				.setEvent(e).open();
	}

	public void addGanttEventListener(String eventCode, Listener listener) {
		gantt.addGanttEventListener(eventCode, listener);
	}

	public void removeGanttListener(String eventCode, Listener listener) {
		gantt.removeGanttListener(eventCode, listener);
	}

	public void addTask(Object task) {
		gantt.addTask(task);
	}

	public void deleteTask(String taskId) {
		gantt.deleteTask(taskId);
	}

	public void addLink(Object link) {
		gantt.addLink(link);
	}

	public void deleteLink(String linkId) {
		gantt.deleteLink(linkId);
	}

	public void updateLink(Object link) {
		gantt.updateLink(link);
	}

	public void updateTask(Object task) {
		gantt.updateTask(task);
	}

	public void reschedule() {
		gantt.autoSchedule();
	}
	
	public void callSave() {
		gantt.save();
	}

	public void highlightCriticalPath(boolean highlightCriticalPath) {
		this.highlightCriticalPath = highlightCriticalPath;
		gantt.highlightCriticalPath(highlightCriticalPath);
	}

	public void switchCriticalPathHighLight() {
		highlightCriticalPath(!this.highlightCriticalPath);
	}

	public void setScaleType(String type) {
		gantt.setScaleType(type);
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		postSelectionChangedListeners.add(listener);
	}

	@Override
	public ISelection getSelection() {
		return selection;
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		postSelectionChangedListeners.remove(listener);
	}

	@Override
	public void setSelection(ISelection selection) {
		this.selection = selection;
	}

	public void addPostSelectionChangedListener(ISelectionChangedListener listener) {
		postSelectionChangedListeners.add(listener);
	}

	@Override
	public void removePostSelectionChangedListener(ISelectionChangedListener listener) {
		postSelectionChangedListeners.remove(listener);
	}

	protected void firePostSelectionChanged(final SelectionChangedEvent event) {
		Object[] listeners = postSelectionChangedListeners.getListeners();
		for (int i = 0; i < listeners.length; ++i) {
			final ISelectionChangedListener l = (ISelectionChangedListener) listeners[i];
			SafeRunnable.run(new SafeRunnable() {
				public void run() {
					l.selectionChanged(event);
				}
			});
		}
	}
	
	public boolean isDirty() {
		return gantt.isDirty();
	}

	@Override
	public BruiDataSetEngine getDataSetEngine() {
		return dataSetEngine;
	}

}
