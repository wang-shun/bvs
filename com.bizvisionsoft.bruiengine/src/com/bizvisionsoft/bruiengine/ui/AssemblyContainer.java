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
			content = brui.injectModelParameters(parameter).init(newServices)
					.createUI(container = new Composite(parent, SWT.NONE)).getContainer();
		}

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ���Һ��ʵĲ���
		int decWidth = Display.getCurrent().getBounds().width;
		Layout layout = Optional.ofNullable(assembly.getLayout()).map(list -> list.stream().filter(l -> {
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
		String data = layout.getCss();
		if (data != null && !data.isEmpty())
			content.setHtmlAttribute("class", data);
		data = layout.getRwtCss();
		if (data != null && !data.isEmpty())
			content.setData(RWT.CUSTOM_VARIANT, data);

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
		layout.getAssemblys().forEach(al -> // ����
		new AssemblyContainer((Composite) content, context)// ����Ƕ��AssemblyContainer
				.setInput(input).setAssembly(ModelLoader.site.getAssembly(al.getId()))// �������
				.setContextName(al.getLayoutName())// ��������
				.setServices(services)// ���ݷ���
				.create()// ����
				.setContainerLayoutData(al)// ����

		);

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ���ù�������
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
