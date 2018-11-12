package com.bizvisionsoft.bruiengine.ui;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiFunction;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;

import com.bizvisionsoft.bruiengine.util.Controls;

public class ElementViewer<T> {

	private ScrolledComposite composite;
	private Composite content;
	private Point elementSize;
	private BiFunction<Object, Boolean, String> labelProvider;
	private List<T> input;
	private Collection<T> selected = new HashSet<>();
	private boolean edit;

	public ElementViewer(Composite parent, int style) {
		composite = new ScrolledComposite(parent, SWT.V_SCROLL | style);
		content = new Composite(composite, SWT.NONE);
		RowLayout layout = new RowLayout();
		layout.marginBottom = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.marginHeight = 8;
		layout.marginWidth = 8;
		layout.spacing = 8;
		content.setLayout(layout);
		composite.setContent(content);
		composite.addListener(SWT.Resize, e -> {
			Point size = content.computeSize(composite.getBounds().width, SWT.DEFAULT);
			content.setSize(size);
		});
	}

	public void setSize(Point elementSize) {
		this.elementSize = elementSize;
	}

	public void setLabelProvider(BiFunction<Object, Boolean, String> labelProvider) {
		this.labelProvider = labelProvider;
	}

	public void setInput(List<T> input) {
		this.input = input;
		Arrays.asList(content.getChildren()).forEach(c -> c.dispose());
		input.forEach(this::createElement);
	}

	private void createElement(T element) {
		Label label = Controls.label(content).html(labelProvider.apply(element, edit)).listen(SWT.MouseUp, this::select).get();
		label.setLayoutData(new RowData(elementSize));
		label.setData("data", element);
		label.setData("select", edit);
		if(edit)
			selected.add(element);
	}

	@SuppressWarnings("unchecked")
	private void select(Event e) {
		Label label = (Label) e.widget;
		boolean value = !Boolean.TRUE.equals(label.getData("select"));
		label.setData("select", value);
		Object element = label.getData("data");
		Controls.handle(label).html(labelProvider.apply(element, value));
		if (value)
			selected.add((T) element);
		else
			selected.remove(element);
	}

	public Control getControl() {
		return composite;
	}

	public void setSize(int width, int height) {
		elementSize = new Point(width, height);
	}

	public List<?> getInput() {
		return input;
	}
	
	public Collection<T> getSelected() {
		return selected;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

}
