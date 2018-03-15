package com.bizvisionsoft.bruidesigner.editor;

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
import org.eclipse.swt.widgets.TreeItem;

import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruidesigner.Activator;
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

		addDisposeListener(
				e -> Optional.ofNullable(current).ifPresent(a -> a.removePropertyChangeListener("name", listener)));
	}

	public List<Action> getChildrenOfParentAction(Action action) {
		return Optional.ofNullable(getParentAction(action)).map(p -> p.getChildren()).orElse(actions);
	}

	public Action getParentAction(Action action) {
		return Optional.ofNullable((TreeItem) viewer.testFindItem(action)).map(itm -> itm.getParentItem())
				.map(pi -> (Action) pi.getData()).orElse(null);
	}

	private void createLeftPane() {
		Composite left = new Composite(this, SWT.NONE);
		left.setLayout(new GridLayout());

		Composite toolbar = new Composite(left, SWT.NONE);
		toolbar.setLayout(new GridLayout(6, true));

		Button add = new Button(toolbar, SWT.PUSH);
		add.setImage(Activator.getImageDescriptor("icons/add.png").createImage());
		add.addListener(SWT.Selection, e -> {
			actions.add(ModelToolkit.createAction());
			viewer.refresh();
		});

		Button remove = new Button(toolbar, SWT.PUSH);
		remove.setImage(Activator.getImageDescriptor("icons/delete.gif").createImage());
		remove.addListener(SWT.Selection, e -> {
			actions.remove(current);
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
				// ������ֵ�
				List<Action> acts = getChildrenOfParentAction(current);
				int idx = acts.indexOf(current);
				if (idx == 0) {
					return;
				}
				Action upBro = acts.get(idx - 1);
				// �Ƴ�current
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
		viewer.addPostSelectionChangedListener(
				e -> openAction((Action) e.getStructuredSelection().getFirstElement(), rightPane));

		createRightPane();
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

			editor.createTextField(parent, "Ψһ��ʶ��:", action, "id", SWT.READ_ONLY);

			editor.createTextField(parent, "��������:", action, "name", SWT.BORDER);

			editor.createTextField(parent, "�ı���ǩ�����Ǳ���ģ����û��ϵͳ��ʹ�ò������ƣ�:", action, "text", SWT.BORDER);

			editor.createTextField(parent, "����:", action, "description", SWT.BORDER);

			editor.createTextField(parent, "������ʾ:", action, "tooltips", SWT.BORDER);

			editor.createCheckboxField(parent, "ǿ��ʹ���ı�:", action, "forceText", SWT.CHECK);

			editor.createPathField(parent, "ͼ��URL:", action, "image", SWT.BORDER);

			editor.createComboField(parent, new String[] { "Ĭ��", "һ��", "��Ϣ", "����", "����" },
					new String[] { "", "normal", "info", "warning", "serious" }, "��ť��񣨽��Թ�������ť��Ч����", action, "style",
					SWT.READ_ONLY);

			editor.createPathField(parent, "ͼ��URL��ʧЧ״̬��:", action, "imageDisabled", SWT.BORDER);

			// editor.createCheckboxField(parent, "�Ƿ�������������²㴫���¼�:", action, "propagate",
			// SWT.CHECK);

			new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
					.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
			Label l = new Label(parent, SWT.NONE);
			l.setText("������Զ���������붨���������ݣ�");
			l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

			editor.createTextField(parent, "���Ψһ��ʶ����Bundle Id��:", action, "bundleId", SWT.BORDER);

			editor.createTextField(parent, "����������:", action, "className", SWT.BORDER);

			new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
					.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
			l = new Label(parent, SWT.NONE);
			l.setText("��������л����������붨���������ݣ�");
			l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
			editor.createAssemblyField(parent, "���������:", action, "switchContentToAssemblyId", true);

			new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
					.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
			l = new Label(parent, SWT.NONE);
			l.setText("������ڴ�ѡ�����ݣ��붨���������ݣ�");
			l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
			editor.createAssemblyField(parent, "�༭�����:", action, "editorAssemblyId", true);

			editor.createCheckboxField(parent, "ֻ����:", action, "editorAssemblyEditable", SWT.CHECK);

			action.addPropertyChangeListener("name", listener);
		}

		parent.layout();
	}

}
