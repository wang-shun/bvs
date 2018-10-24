package com.bizvisionsoft.bruiengine.ui;

import java.util.Optional;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.AssemblyLayouted;
import com.bizvisionsoft.bruicommons.model.Layout;
import com.bizvisionsoft.bruiengine.BruiAssemblyEngine;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.bruiengine.service.UserSession;

public class AssemblyContainer {

	private Assembly assembly;
	private IServiceWithId[] services;
	private Composite content;
	private Composite container;
	private Composite parent;
	private BruiAssemblyContext context;
	private Object input;
	private String parameter;

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
			throw new RuntimeException("缺少组件定义。");// 否则没有创建任何组件，出错。
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 如果是自定义的组件，先创建，并获得container
		if (!assembly.isEmptyContainer()) {
			int length = services == null ? 0 : services.length;
			IServiceWithId[] newServices = new IServiceWithId[length + 1];
			System.arraycopy(services, 0, newServices, 0, length);
			newServices[length] = context;

			BruiAssemblyEngine brui = BruiAssemblyEngine.newInstance(assembly);
			context.setEngine(brui);
			content = brui.injectModelParameters(parameter).init(newServices)
					.createUI(container = new Composite(parent, SWT.NONE)).getContainer();
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 查找合适的布局
		int decWidth = Display.getCurrent().getBounds().width;
		Layout layout = Optional.ofNullable(assembly.getLayout()).map(list -> list.stream().filter(l -> {
			int min = l.getMinimalDeviceWidth();
			int max = l.getMaximalDeviceWidth() == 0 ? 99999 : l.getMaximalDeviceWidth();
			return decWidth >= min && decWidth <= max;
		}).findFirst().orElse(null)).orElse(null);

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 如果没有合适的布局
		if (!assembly.isEmptyContainer()) {
			if (layout == null || content == null) {// 组件创建完成，没有配置嵌套 或者 配置了嵌套，但组件未提供容器,忽略配置
				return this;
			}
		} else if (layout == null) {// 既没有组件又没有配置
			throw new RuntimeException("缺少组件定义或合适的布局配置。");// 否则没有创建任何组件，出错。
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 创建内容区, 自定义组件可以创建一个Composite作为Content,然后交给后续的布局使用
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
		// 设置内容区样式
		String data = layout.getCss();
		if (data != null && !data.isEmpty())
			content.setHtmlAttribute("class", data);
		data = layout.getRwtCss();
		if (data != null && !data.isEmpty())
			content.setData(RWT.CUSTOM_VARIANT, data);

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 对内容区进行布局
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
		// 创建子部件
		layout.getAssemblys().forEach(al -> // 迭代
		new AssemblyContainer((Composite) content, context)// 创建嵌套AssemblyContainer
				.setInput(input).setAssembly(ModelLoader.site.getAssembly(al.getId()))// 设置组件
				.setContextName(al.getLayoutName())// 设置命名
				.setServices(services)// 传递服务
				.create()// 创建
				.setContainerLayoutData(al)// 布局

		);

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 设置滚动容器
		if (container instanceof ScrolledComposite) {
			((ScrolledComposite) container).setExpandVertical(layout.isExtendHorizontalSpace());
			((ScrolledComposite) container).setExpandHorizontal(layout.isExtendVerticalSpace());
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

		return this;
	}

	private void setContainerLayoutData(AssemblyLayouted al) {
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = al.isGrabExcessHorizontalSpace();
		gd.grabExcessVerticalSpace = al.isGrabExcessVerticalSpace();
		gd.heightHint = al.getHeightHint();
		gd.widthHint = al.getWidthHint();
		gd.horizontalAlignment = al.getHorizontalAlignment();
		gd.verticalAlignment = al.getVerticalAlignment();
		gd.horizontalSpan = al.getHorizontalSpan();
		gd.verticalSpan = al.getVerticalSpan();
		container.setLayoutData(gd);
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
