package com.bizvisionsoft.bruiengine.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.AssemblyLayouted;
import com.bizvisionsoft.bruicommons.model.Layout;
import com.bizvisionsoft.bruiengine.BruiAssemblyEngine;
import com.bizvisionsoft.bruiengine.onlinedesigner.AssemblySelector;
import com.bizvisionsoft.bruiengine.onlinedesigner.ClientSettingStore;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.util.BruiToolkit;
import com.bizvisionsoft.bruiengine.util.Controls;
import com.bizvisionsoft.service.tools.Check;

public class AssemblyContainer {

	private Assembly assembly;
	private IServiceWithId[] services;
	private Composite content;
	private Composite container;
	private Composite parent;
	private BruiAssemblyContext context;
	private Object input;
	private String parameter;
	private Control previous;
	private Layout layout;
	private Controls<Button> addLaneBtn;
	private Controls<Button> editLaneBtn;

	public AssemblyContainer(Composite parent, BruiAssemblyContext parentContext) {
		this.parent = parent;
		parentContext.add(context = UserSession.newAssemblyContext().setParent(parentContext).setContentPage(true));
	}

	public AssemblyContainer setAssembly(Assembly assembly) {
		this.assembly = assembly;
		context.setAssembly(assembly);
		return this;
	}

	public AssemblyContainer setInput(Object input) {
		this.input = input;
		context.setInput(input);
		return this;
	}

	public AssemblyContainer setServices(IServiceWithId... services) {
		this.services = services;
		return this;
	}

	private AssemblyContainer setContextName(String name) {
		context.setName(name);
		return this;
	}

	public AssemblyContainer create() {
		if (assembly == null) {
			throw new RuntimeException("ȱ��������塣");// ����û�д����κ����������
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ������Զ����������ȴ����������container
		if (!assembly.isEmptyContainer()) {
			int length = services == null ? 0 : services.length;
			IServiceWithId[] newServices = new IServiceWithId[length + 1];
			System.arraycopy(services, 0, newServices, 0, length);
			newServices[length] = context;

			BruiAssemblyEngine brui = BruiAssemblyEngine.newInstance(assembly);
			context.setEngine(brui);
			context.passParamters(parameter);
			content = brui.injectModelParameters(parameter).init(newServices).createUI(container = new Composite(parent, SWT.NONE))
					.getContainer();
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ���Һ��ʵĲ���
		int decWidth = Display.getCurrent().getBounds().width;
		layout = Optional.ofNullable(assembly.getLayout()).map(list -> list.stream().filter(l -> {
			int min = l.getMinimalDeviceWidth();
			int max = l.getMaximalDeviceWidth() == 0 ? 99999 : l.getMaximalDeviceWidth();
			return decWidth >= min && decWidth <= max;
		}).findFirst().orElse(null)).orElse(null);

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ���û�к��ʵĲ���
		if (!assembly.isEmptyContainer()) {
			if (layout == null || content == null) {// ���������ɣ�û������Ƕ�� ���� ������Ƕ�ף������δ�ṩ����,��������
				return this;
			}
		} else if (layout == null) {// ��û�������û������
			throw new RuntimeException("ȱ������������ʵĲ������á�");// ����û�д����κ����������
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ��ȡ�ͻ��˲���
		Layout cLayout = ClientSettingStore.getLayout(assembly, layout);
		if (cLayout != null)
			layout = cLayout;

		String type = layout.getLayoutType();
		if (Layout.TYPE_LANE.equals(type)) {
			createLaneContent(layout);
		} else {
			createGridContent(layout);
		}

		return this;
	}

	private void createLaneContent(Layout layout) {
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ����������, �Զ���������Դ���һ��Composite��ΪContent,Ȼ�󽻸������Ĳ���ʹ��
		boolean hLane = Layout.LANE_HORIZONTAL.equals(layout.getLaneDirection());
		if (container == null) {
			int style = hLane ? SWT.V_SCROLL : SWT.H_SCROLL;
			container = new ScrolledComposite(parent, style);
			content = new Composite(container, SWT.NONE);
			container.setBackgroundMode(SWT.INHERIT_DEFAULT);
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ������������ʽ
		String css = Check.option(layout.getCss()).orElse(ModelLoader.site.getDefaultPageCSS());
		Check.isAssigned(css, d -> container.setHtmlAttribute("class", d));

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// �����������в���
		FormLayout ly = new FormLayout();
		content.setLayout(ly);
		ly.spacing = hLane ? layout.getVerticalSpacing() : layout.getHorizontalSpacing();
		ly.marginHeight = layout.getMarginHeight();
		ly.marginWidth = layout.getMarginWidth();
		ly.marginTop = layout.getMarginTop();
		ly.marginBottom = layout.getMarginBottom();
		ly.marginLeft = layout.getMarginLeft();
		ly.marginRight = layout.getMarginRight();

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// �����Ӳ���
		previous = null;
		layout.getAssemblys().forEach(al -> previous = createLane(al));

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ���ں�ѡ���
		List<String> cands = assembly.getCandidates();
		if (Check.isAssigned(cands)) {

			addLaneBtn = Controls.button(content).rwt("pane").size(96, 96)
					.html("<img src='" + BruiToolkit.getResourceURL("/img/add_16_w.svg") + "' width=32px height=32px/>")
					.listen(SWT.Selection, e -> addLane(cands)).setData("control", true);

			editLaneBtn = Controls.button(content).rwt("pane").size(96, 96)
					.html("<img src='" + BruiToolkit.getResourceURL("/img/edit_w.svg") + "' width=32px height=32px/>")
					.listen(SWT.Selection, e -> editLane()).setData("control", true);

			layoutLaneButtons();
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ���ù�������
		((ScrolledComposite) container).setContent(content);
		((ScrolledComposite) container).addListener(SWT.Resize, e -> {
			Rectangle b = ((ScrolledComposite) container).getBounds();
			content.setSize(hLane ? content.computeSize(b.width, SWT.DEFAULT) : content.computeSize(SWT.DEFAULT, b.height));
		});
	}

	private void layoutLaneButtons() {
		boolean hLane = Layout.LANE_HORIZONTAL.equals(layout.getLaneDirection());

		if (hLane) {
			addLaneBtn.top(previous, 32).left();
		} else {
			addLaneBtn.top().left(previous, 32);
		}
		if (hLane) {
			editLaneBtn.top(previous, 32).left(addLaneBtn.get(), 32);
		} else {
			editLaneBtn.top(addLaneBtn.get(), 32).left(previous, 32);
		}
	}

	private Composite createLane(AssemblyLayouted al) {
		boolean hLane = Layout.LANE_HORIZONTAL.equals(layout.getLaneDirection());

		AssemblyContainer ac = new AssemblyContainer(content, context)// ����Ƕ��AssemblyContainer
				.setInput(input).setAssembly(ModelLoader.site.getAssembly(al.getId()))// �������
				.setContextName(al.getLayoutName())// ��������
				.setServices(services)// ���ݷ���
				.create();// ����

		if (hLane) {// ����Ӿ��
			Controls.handle(ac.container).height(layout.getLaneWidth()).left().right().top(previous);
		} else {
			Controls.handle(ac.container).width(layout.getLaneWidth()).left(previous).top().bottom();
		}
		return ac.container;
	}

	private void addLane(List<String> cands) {
		Collection<String> result = AssemblySelector.select(cands);
		if (result == null || result.isEmpty())
			return;

		List<AssemblyLayouted> alys = layout.getAssemblys();
		result.forEach(id -> {
			if (alys.stream().anyMatch(al -> al.getId().equals(id)))
				return;

			AssemblyLayouted al = new AssemblyLayouted();
			al.setId(id);
			previous = createLane(al);
			alys.add(al);
		});

		layoutLaneButtons();

		boolean hLane = Layout.LANE_HORIZONTAL.equals(layout.getLaneDirection());
		Rectangle b = ((ScrolledComposite) container).getBounds();
		content.setSize(hLane ? content.computeSize(b.width, SWT.DEFAULT) : content.computeSize(SWT.DEFAULT, b.height));

		content.layout(true);
		ClientSettingStore.saveLayout(assembly, layout);
	}

	private void editLane() {
		List<AssemblyLayouted> assemblys = layout.getAssemblys();
		ArrayList<String> ids = assemblys.stream().map(al -> al.getId()).collect(Collectors.toCollection(ArrayList::new));
		Collection<String> result = AssemblySelector.edit(ids);
		if (result == null)
			return;

		Arrays.asList(content.getChildren()).stream().filter(c -> !Boolean.TRUE.equals(c.getData("control"))).forEach(c -> c.dispose());
		previous = null;

		assemblys.clear();
		result.forEach(id -> {
			AssemblyLayouted al = new AssemblyLayouted();
			al.setId(id);
			previous = createLane(al);
			assemblys.add(al);
		});
		layoutLaneButtons();

		boolean hLane = Layout.LANE_HORIZONTAL.equals(layout.getLaneDirection());
		Rectangle b = ((ScrolledComposite) container).getBounds();
		content.setSize(hLane ? content.computeSize(b.width, SWT.DEFAULT) : content.computeSize(SWT.DEFAULT, b.height));

		content.layout(true);
		ClientSettingStore.saveLayout(assembly, layout);

	}

	private void createGridContent(Layout layout) {
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ����������, �Զ���������Դ���һ��Composite��ΪContent,Ȼ�󽻸������Ĳ���ʹ��
		if (container == null) {
			int style = layout.isExtendHorizontalSpace() ? SWT.H_SCROLL : SWT.NONE;
			style = layout.isExtendVerticalSpace() ? (style | SWT.V_SCROLL) : style;
			if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
				container = new Composite(parent, SWT.NONE);
				content = container;
			} else {
				container = new ScrolledComposite(parent, style);
				content = new Composite(container, SWT.NONE);
			}
			container.setBackgroundMode(SWT.INHERIT_DEFAULT);
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ������������ʽ
		String css = Check.option(layout.getCss()).orElse(ModelLoader.site.getDefaultPageCSS());
		Check.isAssigned(css, d -> container.setHtmlAttribute("class", d));

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// �����������в���
		GridLayout gl = new GridLayout();
		content.setLayout(gl);
		gl.horizontalSpacing = layout.getHorizontalSpacing();
		gl.verticalSpacing = layout.getVerticalSpacing();
		gl.numColumns = layout.getColumnCount();
		gl.makeColumnsEqualWidth = layout.isMakeColumnsEqualWidth();
		gl.marginHeight = layout.getMarginHeight();
		gl.marginWidth = layout.getMarginWidth();
		gl.marginTop = layout.getMarginTop();
		gl.marginBottom = layout.getMarginBottom();
		gl.marginLeft = layout.getMarginLeft();
		gl.marginRight = layout.getMarginRight();

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// �����Ӳ���
		layout.getAssemblys().forEach(al -> {// ����
			AssemblyContainer ac = new AssemblyContainer((Composite) content, context)// ����Ƕ��AssemblyContainer
					.setInput(input).setAssembly(ModelLoader.site.getAssembly(al.getId()))// �������
					.setContextName(al.getLayoutName())// ��������
					.setServices(services)// ���ݷ���
					.create();// ����

			GridData gd = new GridData();
			gd.grabExcessHorizontalSpace = al.isGrabExcessHorizontalSpace();
			gd.grabExcessVerticalSpace = al.isGrabExcessVerticalSpace();
			gd.heightHint = al.getHeightHint();
			gd.widthHint = al.getWidthHint();
			gd.horizontalAlignment = al.getHorizontalAlignment();
			gd.verticalAlignment = al.getVerticalAlignment();
			gd.horizontalSpan = al.getHorizontalSpan();
			gd.verticalSpan = al.getVerticalSpan();
			ac.container.setLayoutData(gd);
		}

		);

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ���ù�������
		if (container instanceof ScrolledComposite) {
			// ((ScrolledComposite)
			// container).setExpandVertical(layout.isExtendHorizontalSpace());
			// ((ScrolledComposite)
			// container).setExpandHorizontal(layout.isExtendVerticalSpace());
			((ScrolledComposite) container).setContent(content);
			((ScrolledComposite) container).addListener(SWT.Resize, e -> {
				Rectangle b = ((ScrolledComposite) container).getBounds();
				if (layout.isExtendHorizontalSpace()) {
					// ((ScrolledComposite) container).setMinWidth(b.width);
					content.setSize(content.computeSize(SWT.DEFAULT, b.height));
				}
				if (layout.isExtendVerticalSpace()) {
					// ((ScrolledComposite) container).setMinHeight(b.height);
					content.setSize(content.computeSize(b.width, SWT.DEFAULT));
				}
			});
		}
	}

	public void setCloseable(boolean closeable) {
		this.context.setCloseable(closeable);
	}

	public BruiAssemblyContext getContext() {
		return context;
	}

	public Composite getContainer() {
		return container;
	}

	public AssemblyContainer setParameter(String parameter) {
		this.parameter = parameter;
		return this;
	}

}
