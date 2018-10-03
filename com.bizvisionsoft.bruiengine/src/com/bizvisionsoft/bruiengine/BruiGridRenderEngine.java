package com.bizvisionsoft.bruiengine;

import java.util.function.BiConsumer;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.osgi.framework.Bundle;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.grid.GridRenderColumnFooter;
import com.bizvisionsoft.annotations.ui.grid.GridRenderColumnHandler;
import com.bizvisionsoft.annotations.ui.grid.GridRenderColumnHeader;
import com.bizvisionsoft.annotations.ui.grid.GridRenderCompare;
import com.bizvisionsoft.annotations.ui.grid.GridRenderConfig;
import com.bizvisionsoft.annotations.ui.grid.GridRenderInput;
import com.bizvisionsoft.annotations.ui.grid.GridRenderUICreated;
import com.bizvisionsoft.annotations.ui.grid.GridRenderUpdateCell;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruiengine.assembly.GridPartDefaultRender;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.service.tools.Checker;

public class BruiGridRenderEngine extends BruiEngine {

	public static BruiGridRenderEngine create(Assembly grid, IServiceWithId... services) {
		BruiGridRenderEngine eng = (BruiGridRenderEngine) load(grid.getGridRenderBundleId(),
				grid.getGridRenderClassName())// load
						.newInstance();
		eng.setGridConfig(grid);
		return (BruiGridRenderEngine) eng.init(services);
	}

	private static BruiGridRenderEngine load(String bundleId, String className) {
		if (!Checker.isNotAssigned(bundleId) && !Checker.isNotAssigned(className)) {
			Bundle bundle = Platform.getBundle(bundleId);
			try {
				return new BruiGridRenderEngine(bundle.loadClass(className));
			} catch (Exception e) {
				throw new RuntimeException(e.getCause());
			}
		}
		return new BruiGridRenderEngine();
	}

	private GridPartDefaultRender defaultRender;
	private Assembly config;

	BruiGridRenderEngine(Class<?> clazz) {
		super(clazz);
	}

	BruiGridRenderEngine() {
		super();
		defaultRender = new GridPartDefaultRender();
	}

	public void setInput(Object input) {
		if (defaultRender != null)
			defaultRender.setInput(input);
		else
			setFieldValue(GridRenderInput.class, input);
	}

	private BruiGridRenderEngine setGridConfig(Assembly config) {
		this.config = config;
		if (defaultRender != null)
			defaultRender.setGridConfig(config);
		else
			setFieldValue(GridRenderConfig.class, config);
		return this;
	}

	public void renderCell(ViewerCell cell, Column column, Object inputElement, BiConsumer<String, Object> callBack) {
		Object element = cell == null ? inputElement : cell.getElement();
		Object value = getColumnValue(element, column);
		Object image = getColumnImageUrl(element, column);

		if (defaultRender != null) {
			defaultRender.renderCell(cell, column, value, image, callBack);
		} else {
			invokeMethodInjectParams(GridRenderUpdateCell.class, new Object[] { cell, column, value, image, callBack },
					new String[] { GridRenderUpdateCell.PARAM_CELL, GridRenderUpdateCell.PARAM_COLUMN,
							GridRenderUpdateCell.PARAM_VALUE, GridRenderUpdateCell.PARAM_IMAGE,
							GridRenderUpdateCell.PARAM_CALLBACK },
					element.toString());
		}
	}

	public void renderHeaderText(GridColumn col, Column c) {
		if (defaultRender != null) {
			defaultRender.renderColumnHeader(col, c);
		} else {
			invokeMethodInjectParams(GridRenderColumnHeader.class, new Object[] { col, c },
					new String[] { GridRenderColumnHeader.PARAM_COLUMN_WIDGET, GridRenderUpdateCell.PARAM_COLUMN },
					null);
		}
	}

	public void renderFoot(GridColumn col, Column c) {
		if (defaultRender != null) {
			defaultRender.renderColumnFooter(col, c);
		} else {
			invokeMethodInjectParams(GridRenderColumnFooter.class, new Object[] { col, c },
					new String[] { GridRenderColumnFooter.PARAM_COLUMN_WIDGET, GridRenderColumnFooter.PARAM_COLUMN },
					null);
		}
	}

	public Object getColumnValue(Object element, Column column) {
		return AUtil.readValue(element, config.getName(), column.getName(), column.isElement() ? element : null);
	}

	private Object getColumnImageUrl(Object element, Column column) {
		return AUtil.readImageUrl(element, config.getName(), column.getName(), null);
	}

	public int compare(Column column, Object e1, Object e2) {
		if (defaultRender != null) {
			return defaultRender.compare(column, e1, e2);
		} else {
			return (int) invokeMethodInjectParams(GridRenderCompare.class, new Object[] { column, e1, e2 },
					new String[] { GridRenderCompare.PARAM_COLUMN, GridRenderCompare.PARAM_ELEMENT1,
							GridRenderCompare.PARAM_ELEMENT2 },
					null);
		}
	}

	public void uiCreated() {
		if (defaultRender == null) {
			invokeMethod(GridRenderUICreated.class);
		}
	}

	public void handleColumn(GridViewerColumn vcol) {
		if (defaultRender != null) {
			defaultRender.handleColumn(vcol);
		} else {
			invokeMethodInjectParams(GridRenderColumnHandler.class, new Object[] { vcol }, new String[] { "" }, null);
		}
	}

}
