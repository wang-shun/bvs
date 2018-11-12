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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.AssemblyLayouted;
import com.bizvisionsoft.bruicommons.model.Layout;
import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruidesigner.Activator;
import com.bizvisionsoft.bruidesigner.dialog.AssemblySelectionDialog;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;
import com.bizvisionsoft.bruidesigner.model.ModelToolkit;

public class LayoutEditPane extends Composite {

	class LayoutContentProvider implements ITreeContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object[] getElements(Object inputElement) {
			return ((List<Layout>) inputElement).toArray();
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Layout) {
				List<AssemblyLayouted> children = ((Layout) parentElement).getAssemblys();
				if (children != null)
					return children.toArray(new AssemblyLayouted[0]);
			}
			return new Object[0];
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

	private List<Layout> layouts;
	private ModelEditor editor;
	private PropertyChangeListener listener;
	private TreeViewer viewer;
	private Composite rightPane;
	private ModelObject current;

	public LayoutEditPane(Composite parent, List<Layout> layouts, ModelEditor editor) {
		super(parent, SWT.HORIZONTAL);
		this.layouts = layouts;
		this.editor = editor;
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		setLayoutData(layoutData);

		setLayout(new GridLayout(2, false));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, true);
		gd.widthHint = 400;
		createLeftPane().setLayoutData(gd);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		createRightPane().setLayoutData(gd);

		listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				viewer.update(e.getSource(), null);
			}
		};

		addDisposeListener(e -> Optional.ofNullable(current).ifPresent(a -> a.removePropertyChangeListener("name", listener)));
	}

	private Composite createLeftPane() {
		Composite left = new Composite(this, SWT.NONE);
		left.setLayout(new GridLayout());

		Composite toolbar = new Composite(left, SWT.NONE);
		toolbar.setLayout(new GridLayout(5, false));

		Button addlayout = new Button(toolbar, SWT.PUSH);
		addlayout.setText("��Ӳ���");
		addlayout.addListener(SWT.Selection, e -> {
			layouts.add(ModelToolkit.createLayout());
			viewer.refresh();
		});

		Button addAssembly = new Button(toolbar, SWT.PUSH);
		addAssembly.setText("��������");
		addAssembly.addListener(SWT.Selection, e -> {
			Object element = viewer.getStructuredSelection().getFirstElement();
			if (element instanceof Layout) {
				AssemblySelectionDialog dialog = new AssemblySelectionDialog(getShell(), true);
				if (AssemblySelectionDialog.OK == dialog.open()) {
					Object[] result = dialog.getResult();
					if (result != null && result.length > 0) {
						ModelToolkit.createAssemblyLayouted((Layout) element, ((Assembly) result[0]).getId());
						viewer.refresh(element);
						viewer.expandAll();
					}
				}
			}
		});

		Button moveUp = new Button(toolbar, SWT.PUSH);
		moveUp.setImage(Activator.getImageDescriptor("icons/up.png").createImage());
		moveUp.addListener(SWT.Selection, e -> {
			if (!(current instanceof AssemblyLayouted))
				return;
			List<AssemblyLayouted> acts = getNeighborAssemblyLayouted((AssemblyLayouted) current);
			int idx = acts.indexOf(current);
			if (idx == 0) {
				return;
			}
			acts.remove(idx);
			acts.add(idx - 1, (AssemblyLayouted) current);

			viewer.refresh();
		});

		Button moveDown = new Button(toolbar, SWT.PUSH);
		moveDown.setImage(Activator.getImageDescriptor("icons/down.png").createImage());
		moveDown.addListener(SWT.Selection, e -> {
			if (!(current instanceof AssemblyLayouted))
				return;
			List<AssemblyLayouted> acts = getNeighborAssemblyLayouted((AssemblyLayouted) current);
			int idx = acts.indexOf(current);
			if (idx == acts.size() - 1) {
				return;
			}
			acts.remove(idx);
			acts.add(idx + 1, (AssemblyLayouted) current);
			viewer.refresh();
		});

		Button remove = new Button(toolbar, SWT.PUSH);
		remove.setImage(Activator.getImageDescriptor("icons/delete.gif").createImage());
		remove.addListener(SWT.Selection, e -> {
			Object element = viewer.getStructuredSelection().getFirstElement();
			if (element instanceof Layout) {
				layouts.remove(current);
				viewer.refresh();
			} else if (element instanceof AssemblyLayouted) {
				Layout layout = (Layout) ((TreeItem) viewer.testFindItem(element)).getParentItem().getData();
				ModelToolkit.removeAssembly(layout, (AssemblyLayouted) element);
				viewer.refresh(layout);
			}
		});

		viewer = new TreeViewer(left, SWT.FULL_SELECTION | SWT.BORDER);
		viewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		viewer.setContentProvider(new LayoutContentProvider());
		viewer.setLabelProvider(ModelToolkit.createLabelProvider());
		viewer.setInput(layouts);
		viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.addPostSelectionChangedListener(e -> {
			Object element = e.getStructuredSelection().getFirstElement();
			if (element instanceof Layout) {
				openLayout((Layout) element, rightPane);
			} else if (element instanceof AssemblyLayouted) {
				openAssemblyLayouted((AssemblyLayouted) element, rightPane);
			}
		});
		return left;
	}

	private List<AssemblyLayouted> getNeighborAssemblyLayouted(AssemblyLayouted al) {
		return Optional.ofNullable(getParentLayout(al)).map(p -> p.getAssemblys()).orElse(new ArrayList<>());
	}

	public Layout getParentLayout(AssemblyLayouted al) {
		return Optional.ofNullable((TreeItem) viewer.testFindItem(al)).map(itm -> itm.getParentItem()).map(pi -> (Layout) pi.getData())
				.orElse(null);
	}

	private void openAssemblyLayouted(AssemblyLayouted element, Composite parent) {
		Optional.ofNullable(current).ifPresent(a -> a.removePropertyChangeListener("name", listener));
		current = element;
		Arrays.asList(parent.getChildren()).stream().filter(c -> !c.isDisposed()).forEach(ctl -> ctl.dispose());

		if (element != null) {

			editor.createComboField(parent, new String[] { "���", "����", "����", "����" },
					new Object[] { SWT.FILL, SWT.LEFT, SWT.CENTER, SWT.RIGHT }, "������뷽ʽ:", element, "horizontalAlignment",
					SWT.READ_ONLY | SWT.BORDER);

			editor.createComboField(parent, new String[] { "���", "����", "����", "����" },
					new Object[] { SWT.FILL, SWT.LEFT, SWT.CENTER, SWT.RIGHT }, "������뷽ʽ:", element, "verticalAlignment",
					SWT.READ_ONLY | SWT.BORDER);

			editor.createIntegerField(parent, "�����Խ��λ:", element, "horizontalSpan", SWT.BORDER, 0, 9999);

			editor.createIntegerField(parent, "�����Խ��λ:", element, "verticalSpan", SWT.BORDER, 0, 9999);

			editor.createCheckboxField(parent, "�������", element, "grabExcessHorizontalSpace", SWT.CHECK);

			editor.createCheckboxField(parent, "�������", element, "grabExcessVerticalSpace", SWT.CHECK);

			editor.createIntegerField(parent, "�����ȣ����أ�-1��ʾ����:", element, "widthHint", SWT.BORDER, -1, 9999);

			editor.createIntegerField(parent, "����߶ȣ����أ�-1��ʾ����:", element, "heightHint", SWT.BORDER, -1, 9999);

			editor.createAssemblyField(parent, "���:", element, "id", true);

			editor.createTextField(parent, "��������������ֵ������������������з��ʣ���", element, "layoutName", SWT.BORDER);
		}
		parent.layout();
	}

	private Composite createRightPane() {
		rightPane = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.marginTop = 16;
		layout.marginBottom = 16;
		layout.marginLeft = 16;
		layout.marginRight = 16;
		layout.horizontalSpacing = 16;
		layout.verticalSpacing = 16;
		rightPane.setLayout(layout);
		return rightPane;
	}

	private void openLayout(Layout element, Composite parent) {
		Optional.ofNullable(current).ifPresent(a -> a.removePropertyChangeListener("name", listener));
		current = element;

		Arrays.asList(parent.getChildren()).stream().filter(c -> !c.isDisposed()).forEach(ctl -> ctl.dispose());

		if (element != null) {
			// ���ֵı�׼����
			editor.createComboField(parent, //
					new String[] { //
							"����Ĭ�ϣ�"//
							, "Ӿ��"//
					}, //
					new Object[] { //
							"grid"//
							, "lane", //
					}, //
					"�������ͣ�", element, "layoutType", SWT.READ_ONLY | SWT.BORDER);

			editor.createTextField(parent, "Ψһ��ʶ��:", element, "id", SWT.READ_ONLY);

			editor.createTextField(parent, "��������:", element, "name", SWT.BORDER);

			editor.createTextField(parent, "����:", element, "description", SWT.BORDER);

			editor.createIntegerField(parent, "�����豸����С��ȣ����أ�:", element, "minimalDeviceWidth", SWT.BORDER, 0, 9999);

			editor.createIntegerField(parent, "�����豸������ȣ����أ�:", element, "maximalDeviceWidth", SWT.BORDER, 0, 9999);

			editor.createComboField(parent, //
					new String[] { "��", "Login", //
							"Grey Cloud", //
							"Spiky Naga", //
							"Deep Relief", //
							"Dirty Beauty", //
							"Saint Petersburg", //
							"Sharpeye Eagle", //
							"Blessing", //
							"Plum Plate", //
							"New York", //
							"Fly High", //
							"Soft Grass", //
							"Kind Steel", //
							"Great Whale" }, //
					new Object[] { "", "brui_login_bg", //
							"brui_grey_bg", //
							"brui_bg_spiky_naga", //
							"brui_bg_deep_relief", //
							"brui_bg_dirty_beauty", //
							"brui_bg_saint_petersburg", //
							"brui_bg_sharpeye_eagle", //
							"brui_bg_blessing", //
							"brui_bg_plum_plate", //
							"brui_bg_new_york", //
							"brui_bg_fly_high", //
							"brui_bg_soft_grass", //
							"brui_bg_kind_steel", //
							"brui_bg_great_whale" }, //
					"����CSS������", element, "css", SWT.READ_ONLY | SWT.BORDER);

			String type = element.getLayoutType();
			if (type == null || type.trim().isEmpty() || type.equals("grid")) {
				createGridLayout(parent, element);
			} else if ("lane".equals(type)) {
				createLaneLayout(parent, element);
			}

			element.addPropertyChangeListener("name", listener);
		}

		parent.layout();

	}

	private void createLaneLayout(Composite parent, Layout element) {

		editor.createComboField(parent, //
				new String[] { "����", "����" }, //
				new Object[] { Layout.LANE_HORIZONTAL, Layout.LANE_VERTICAL }, //
				"����", element, "laneDirection", SWT.READ_ONLY | SWT.BORDER);

		editor.createIntegerField(parent, "Ӿ�����", element, "laneWidth", SWT.BORDER, 100, 1200);

		editor.createIntegerField(parent, "�����ࣨ���أ�:", element, "horizontalSpacing", SWT.BORDER, 0, 9999);

		editor.createIntegerField(parent, "�����ࣨ���أ�:", element, "verticalSpacing", SWT.BORDER, 0, 9999);

		editor.createIntegerField(parent, "���±߾ࣨ���أ�:", element, "marginHeight", SWT.BORDER, 0, 9999);

		editor.createIntegerField(parent, "���߾ࣨ���أ�:", element, "marginTop", SWT.BORDER, 0, 9999);

		editor.createIntegerField(parent, "�ױ߾ࣨ���أ�:", element, "marginBottom", SWT.BORDER, 0, 9999);

		editor.createIntegerField(parent, "���ұ߾ࣨ���أ�:", element, "marginWidth", SWT.BORDER, 0, 9999);

		editor.createIntegerField(parent, "��߾ࣨ���أ�:", element, "marginLeft", SWT.BORDER, 0, 9999);

		editor.createIntegerField(parent, "�ұ߾ࣨ���أ�:", element, "marginRight", SWT.BORDER, 0, 9999);
	}

	private void createGridLayout(Composite parent, Layout element) {
		editor.createIntegerField(parent, "����:", element, "columnCount", SWT.BORDER, 0, 9999);

		editor.createCheckboxField(parent, "����չ��", element, "extendHorizontalSpace", SWT.CHECK);

		editor.createCheckboxField(parent, "����չ��", element, "extendVerticalSpace", SWT.CHECK);

		editor.createCheckboxField(parent, "�����Ƿ�ȿ�", element, "makeColumnsEqualWidth", SWT.CHECK);

		editor.createIntegerField(parent, "�����ࣨ���أ�:", element, "horizontalSpacing", SWT.BORDER, 0, 9999);

		editor.createIntegerField(parent, "�����ࣨ���أ�:", element, "verticalSpacing", SWT.BORDER, 0, 9999);

		editor.createIntegerField(parent, "���±߾ࣨ���أ�:", element, "marginHeight", SWT.BORDER, 0, 9999);

		editor.createIntegerField(parent, "���߾ࣨ���أ�:", element, "marginTop", SWT.BORDER, 0, 9999);

		editor.createIntegerField(parent, "�ױ߾ࣨ���أ�:", element, "marginBottom", SWT.BORDER, 0, 9999);

		editor.createIntegerField(parent, "���ұ߾ࣨ���أ�:", element, "marginWidth", SWT.BORDER, 0, 9999);

		editor.createIntegerField(parent, "��߾ࣨ���أ�:", element, "marginLeft", SWT.BORDER, 0, 9999);

		editor.createIntegerField(parent, "�ұ߾ࣨ���أ�:", element, "marginRight", SWT.BORDER, 0, 9999);

	}

}
