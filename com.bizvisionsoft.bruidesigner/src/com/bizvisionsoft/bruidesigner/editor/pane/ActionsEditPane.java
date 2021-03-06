package com.bizvisionsoft.bruidesigner.editor.pane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TreeItem;

import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruidesigner.Activator;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;
import com.bizvisionsoft.bruidesigner.model.ModelToolkit;

public class ActionsEditPane extends SashForm {

	class ActionContentProvider implements ITreeContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object[] getElements(Object inputElement) {
			return ((List<Action>) inputElement).toArray();
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			List<Action> children = ((Action) parentElement).getChildren();
			if (children != null)
				return children.toArray(new Action[0]);
			return new Action[0];
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

	private List<Action> actions;
	private ModelEditor editor;
	private PropertyChangeListener listener;
	private TreeViewer viewer;
	private Composite rightPane;
	private Action current;
	private boolean hierarchyActions;

	public ActionsEditPane(Composite parent, List<Action> actions, boolean hierarchyActions, ModelEditor editor) {
		super(parent, SWT.HORIZONTAL);
		this.actions = actions;
		this.hierarchyActions = hierarchyActions;
		this.editor = editor;
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		setLayoutData(layoutData);

		createLeftPane();

		listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				viewer.update(e.getSource(), null);
			}
		};

		setWeights(new int[] { 1, 2 });

		addDisposeListener(e -> Optional.ofNullable(current).ifPresent(a -> a.removePropertyChangeListener("name", listener)));
	}

	public List<Action> getChildrenOfParentAction(Action action) {
		return Optional.ofNullable(getParentAction(action)).map(p -> p.getChildren()).orElse(actions);
	}

	public Action getParentAction(Action action) {
		return Optional.ofNullable((TreeItem) viewer.testFindItem(action)).map(itm -> itm.getParentItem()).map(pi -> (Action) pi.getData())
				.orElse(null);
	}

	private void createLeftPane() {
		Composite left = new Composite(this, SWT.NONE);
		left.setLayout(new GridLayout());

		Composite toolbar = new Composite(left, SWT.NONE);
		toolbar.setLayout(new GridLayout(6, true));

		Button add = new Button(toolbar, SWT.PUSH);
		add.setImage(Activator.getImageDescriptor("icons/add.png").createImage());
		add.addListener(SWT.Selection, e -> showActionAddMenu(add));

		Button remove = new Button(toolbar, SWT.PUSH);
		remove.setImage(Activator.getImageDescriptor("icons/delete.gif").createImage());
		remove.addListener(SWT.Selection, e -> {
			Action parent = getParentAction(current);
			if (parent == null) {
				actions.remove(current);
			} else {
				parent.getChildren().remove(current);
			}
			viewer.refresh();
		});

		Button moveUp = new Button(toolbar, SWT.PUSH);
		moveUp.setImage(Activator.getImageDescriptor("icons/up.png").createImage());
		moveUp.addListener(SWT.Selection, e -> {
			if (current == null)
				return;
			List<Action> acts = getChildrenOfParentAction(current);
			int idx = acts.indexOf(current);
			if (idx == 0) {
				return;
			}
			acts.remove(idx);
			acts.add(idx - 1, current);

			viewer.refresh();
		});
		if (hierarchyActions) {
			Button moveRight = new Button(toolbar, SWT.PUSH);
			moveRight.setImage(Activator.getImageDescriptor("icons/right.png").createImage());
			moveRight.addListener(SWT.Selection, e -> {
				if (current == null)
					return;
				// 获得上兄弟
				List<Action> acts = getChildrenOfParentAction(current);
				int idx = acts.indexOf(current);
				if (idx == 0) {
					return;
				}
				Action upBro = acts.get(idx - 1);
				// 移除current
				acts.remove(idx);
				List<Action> children = upBro.getChildren();
				if (children == null) {
					children = new ArrayList<Action>();
					upBro.setChildren(children);
				}
				children.add(current);

				viewer.refresh();
			});
		}

		Button moveDown = new Button(toolbar, SWT.PUSH);
		moveDown.setImage(Activator.getImageDescriptor("icons/down.png").createImage());
		moveDown.addListener(SWT.Selection, e -> {
			if (current == null)
				return;
			List<Action> acts = getChildrenOfParentAction(current);
			int idx = acts.indexOf(current);
			if (idx == acts.size() - 1) {
				return;
			}
			acts.remove(idx);
			acts.add(idx + 1, current);
			viewer.refresh();
		});

		if (hierarchyActions) {

			Button moveLeft = new Button(toolbar, SWT.PUSH);
			moveLeft.setImage(Activator.getImageDescriptor("icons/left.png").createImage());
			moveLeft.addListener(SWT.Selection, e -> {
				if (current == null)
					return;
				Action parentAction = getParentAction(current);
				if (parentAction == null) {
					return;
				}
				List<Action> acts = getChildrenOfParentAction(current);
				acts.remove(current);

				acts = getChildrenOfParentAction(parentAction);
				int idx = acts.indexOf(parentAction);
				acts.add(idx + 1, current);

				viewer.refresh();
			});
		}
		viewer = new TreeViewer(left, SWT.FULL_SELECTION | SWT.BORDER);
		viewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		viewer.setContentProvider(new ActionContentProvider());
		viewer.setLabelProvider(ModelToolkit.createLabelProvider());
		viewer.setInput(actions);
		viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.addPostSelectionChangedListener(e -> openAction((Action) e.getStructuredSelection().getFirstElement(), rightPane));

		createRightPane();
	}

	/*
	 * 
	 * 
	 */
	private void showActionAddMenu(Button parent) {
		Menu menu = new Menu(parent);
		MenuItem item;

		item = new MenuItem(menu, SWT.PUSH);
		item.setText("切换内容区");
		item.addListener(SWT.Selection, e -> {
			actions.add(ModelToolkit.createAction(Action.TYPE_SWITCHCONTENT));
			viewer.refresh();
		});

		item = new MenuItem(menu, SWT.PUSH);
		item.setText("打开新页面");
		item.addListener(SWT.Selection, e -> {
			actions.add(ModelToolkit.createAction(Action.TYPE_OPENPAGE));
			viewer.refresh();
		});

		item = new MenuItem(menu, SWT.PUSH);
		item.setText("创建新对象（适用于表格组件）");
		item.addListener(SWT.Selection, e -> {
			actions.add(ModelToolkit.createAction(Action.TYPE_INSERT));
			viewer.refresh();
		});

		item = new MenuItem(menu, SWT.PUSH);
		item.setText("创建选中对象的下级对象（适用于表格组件）");
		item.addListener(SWT.Selection, e -> {
			actions.add(ModelToolkit.createAction(Action.TYPE_INSERT_SUBITEM));
			viewer.refresh();
		});

		item = new MenuItem(menu, SWT.PUSH);
		item.setText("编辑或打开选择中对象（适用于表格组件）");
		item.addListener(SWT.Selection, e -> {
			actions.add(ModelToolkit.createAction(Action.TYPE_EDIT));
			viewer.refresh();
		});

		item = new MenuItem(menu, SWT.PUSH);
		item.setText("删除选择中对象（适用于表格组件）");
		item.addListener(SWT.Selection, e -> {
			actions.add(ModelToolkit.createAction(Action.TYPE_DELETE));
			viewer.refresh();
		});

		item = new MenuItem(menu, SWT.PUSH);
		item.setText("根据查询字段查询（适用于表格组件）");
		item.addListener(SWT.Selection, e -> {
			actions.add(ModelToolkit.createAction(Action.TYPE_QUERY));
			viewer.refresh();
		});

		item = new MenuItem(menu, SWT.PUSH);
		item.setText("导出");
		item.addListener(SWT.Selection, e -> {
			actions.add(ModelToolkit.createAction(Action.TYPE_EXPORT));
			viewer.refresh();
		});

		item = new MenuItem(menu, SWT.PUSH);
		item.setText("报表");
		item.addListener(SWT.Selection, e -> {
			actions.add(ModelToolkit.createAction(Action.TYPE_REPORT));
			viewer.refresh();
		});

		item = new MenuItem(menu, SWT.PUSH);
		item.setText("组件设置");
		item.addListener(SWT.Selection, e -> {
			actions.add(ModelToolkit.createAction(Action.TYPE_SETTING));
			viewer.refresh();
		});

		item = new MenuItem(menu, SWT.PUSH);
		item.setText("系统设置");
		item.addListener(SWT.Selection, e -> {
			actions.add(ModelToolkit.createAction(Action.TYPE_SYSTEM_SETTING));
			viewer.refresh();
		});

		item = new MenuItem(menu, SWT.PUSH);
		item.setText("自定义操作");
		item.addListener(SWT.Selection, e -> {
			actions.add(ModelToolkit.createAction(Action.TYPE_CUSTOMIZED));
			viewer.refresh();
		});

		parent.setMenu(menu);
		parent.addListener(SWT.Selection, e -> {
			menu.setLocation(parent.toDisplay(0, 32));
			menu.setVisible(true);
		});
	}

	private void createRightPane() {
		rightPane = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.marginTop = 16;
		layout.marginBottom = 16;
		layout.marginLeft = 16;
		layout.marginRight = 16;
		layout.horizontalSpacing = 16;
		layout.verticalSpacing = 16;
		rightPane.setLayout(layout);
	}

	private void openAction(Action action, Composite parent) {
		Optional.ofNullable(current).ifPresent(a -> a.removePropertyChangeListener("name", listener));
		current = action;

		Arrays.asList(parent.getChildren()).stream().filter(c -> !c.isDisposed()).forEach(ctl -> ctl.dispose());

		if (action != null) {

			editor.createComboField(parent,
					new String[] { "切换或打开内容区", "打开新页面", "创建新对象", "创建选中对象的子对象", "删除选中对象", "编辑或打开选中对象", "根据查询字段查询", "导出", "报表", "设置", "系统设置",
							"自定义操作" },
					new String[] { Action.TYPE_SWITCHCONTENT, Action.TYPE_OPENPAGE, Action.TYPE_INSERT, Action.TYPE_INSERT_SUBITEM,
							Action.TYPE_DELETE, Action.TYPE_EDIT, Action.TYPE_QUERY, Action.TYPE_EXPORT, Action.TYPE_REPORT,
							Action.TYPE_SETTING, Action.TYPE_SYSTEM_SETTING, Action.TYPE_CUSTOMIZED },
					"操作类型：", action, "type", SWT.READ_ONLY);

			editor.createTextField(parent, "唯一标识符:", action, "id", SWT.READ_ONLY);

			editor.createTextField(parent, "操作名称:", action, "name", SWT.BORDER);

			editor.createTextField(parent, "文本标签（不是必须的，如果没有系统将使用操作名称）:", action, "text", SWT.BORDER);

			editor.createTextField(parent, "描述:", action, "description", SWT.BORDER);

			editor.createTextField(parent, "工具提示:", action, "tooltips", SWT.BORDER);
			
			editor.createTextField(parent, "快捷键:", action, "shortcutKey", SWT.BORDER);

			editor.createCheckboxField(parent, "强制使用文本:", action, "forceText", SWT.CHECK);

			editor.createPathField(parent, "图标URL:", action, "image", SWT.BORDER);

			editor.createComboField(parent, new String[] { "默认", "一般", "信息", "警告", "严重" },
					new String[] { "", "normal", "info", "warning", "serious" }, "按钮风格（仅对工具栏按钮有效）：", action, "style", SWT.READ_ONLY);

			editor.createCheckboxField(parent, "由对象行为控制有效性:", action, "objectBehavier", SWT.CHECK);

			editor.createCheckboxField(parent, "由操作行为控制有效性:", action, "actionBehavier", SWT.CHECK);

			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (Action.TYPE_CUSTOMIZED.equals(action.getType())) {
				editor.createTextField(parent, "插件唯一标识符（Bundle Id）:", action, "bundleId", SWT.BORDER);
				editor.createTextField(parent, "完整的类名:", action, "className", SWT.BORDER);
			}

			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (Action.TYPE_SWITCHCONTENT.equals(action.getType())) {
				editor.createAssemblyField(parent, "内容区组件:", action, "switchContentToAssemblyId", true);
				editor.createCheckboxField(parent, "打开新内容（原有的内容区不关闭）:", action, "openContent", SWT.CHECK);
				editor.createTextField(parent, "传递参数到组件:", action, "passParametersToAssembly", SWT.BORDER | SWT.MULTI);
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (Action.TYPE_OPENPAGE.equals(action.getType())) {
				editor.createTextField(parent, "页面名称:", action, "openPageName", SWT.BORDER);
				// editor.createTextField(parent, "传递参数到页面:", action,
				// "passParametersToAssembly", SWT.BORDER | SWT.MULTI);
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (Action.TYPE_INSERT.equals(action.getType()) || Action.TYPE_INSERT_SUBITEM.equals(action.getType())) {
				editor.createAssemblyField(parent, "编辑器组件:", action, "editorAssemblyId", true);
				editor.createTextField(parent, "新对象的插件唯一标识符（Bundle Id）:", action, "createActionNewInstanceBundleId", SWT.BORDER);
				editor.createTextField(parent, "新对象的完整类名:", action, "createActionNewInstanceClassName", SWT.BORDER);
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (Action.TYPE_EDIT.equals(action.getType())) {
				editor.createAssemblyField(parent, "编辑器组件:", action, "editorAssemblyId", true);
				Label l = new Label(parent, SWT.NONE);
				l.setText("不提供编辑器组件时，系统将使用对象的@ReadEditorConfig注解。");
				l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
				editor.createCheckboxField(parent, "允许编辑:", action, "editorAssemblyEditable", SWT.CHECK);
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (Action.TYPE_DELETE.equals(action.getType())) {
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (Action.TYPE_QUERY.equals(action.getType())) {
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (Action.TYPE_REPORT.equals(action.getType())) {
				editor.createTextField(parent, "报表查询JQ", action, "reportJQ", SWT.BORDER);
				editor.createComboField(parent, new String[] { "pdf", "html", "docx", "excel", "pptx" },
						new String[] { "pdf", "html", "docx", "excel", "pptx" }, "报表输出类型", action, "reportOutputType",
						SWT.BORDER | SWT.READ_ONLY);
				editor.createTextField(parent, "报表模板", action, "reportTemplate", SWT.BORDER);
				editor.createTextField(parent, "报表输出文件", action, "reportFileName", SWT.BORDER);
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (Action.TYPE_SYSTEM_SETTING.equals(action.getType())) {
				editor.createAssemblyField(parent, "编辑器组件（默认为action名称）:", action, "editorAssemblyId", true);
				editor.createTextField(parent, "系统设置参数（默认为action名称）：", action, "sysSettingParameter", SWT.BORDER);
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

			editor.createTextField(parent, "角色（多个#分割）", action, "role", SWT.BORDER);

			editor.createTextField(parent, "排除角色（多个#分割）", action, "excludeRole", SWT.BORDER);

			new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
			editor.createTextField(parent, "Budget取数插件唯一标识符（Bundle Id）:", action, "budgetBundleId", SWT.BORDER);
			editor.createTextField(parent, "Budget取数完整的类名:", action, "budgetClassName", SWT.BORDER);
			editor.createTextField(parent, "Budget取数服务:", action, "budgetServiceName", SWT.BORDER);

			editor.createTextField(parent, "参数:", action, "parameters", SWT.BORDER | SWT.MULTI);

			action.addPropertyChangeListener("name", listener);
		}

		parent.layout();
	}

}
