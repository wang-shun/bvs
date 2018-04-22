package com.bizvisionsoft.pms.cbs.assembly;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.bizvisionsoft.service.model.ICBSScope;

public class BudgetCBS extends GridPart {

	@Inject
	private BruiAssemblyContext context;

	@Inject
	private IBruiService bruiService;

	private ICBSScope input;

	@Init
	public void init() {
		setContext(context);
		setConfig(context.getAssembly());
		setBruiService(bruiService);
		input = (ICBSScope) context.getRootInput();
		super.init();
	}

	@CreateUI
	public void createUI(Composite parent) {
		super.createUI(parent);
	}
	
	@Override
	protected GridTreeViewer createGridViewer(Composite parent) {
		GridTreeViewer viewer = new GridTreeViewer(parent, SWT.H_SCROLL|SWT.V_SCROLL);
		viewer.setAutoExpandLevel(3);
		viewer.setUseHashlookup(false);

		Grid grid = viewer.getGrid();
		grid.setHeaderVisible(true);
		grid.setFooterVisible(false);
		grid.setLinesVisible(true);
		UserSession.bruiToolkit().enableMarkup(grid);
		grid.setData(RWT.FIXED_COLUMNS, 3);
		
		return viewer;
	}

	@Override
	protected void createColumns(Grid grid) {

		/////////////////////////////////////////////////////////////////////////////////////
		// 创建列
		Column c = new Column();
		c.setName("id");
		c.setText("编号");
		c.setWidth(140);
		c.setAlignment(SWT.LEFT);
		c.setMoveable(false);
		c.setResizeable(true);
		createColumn(grid, c);

		c = new Column();
		c.setName("name");
		c.setText("名称");
		c.setWidth(120);
		c.setAlignment(SWT.LEFT);
		c.setMoveable(false);
		c.setResizeable(true);
		createColumn(grid, c);

		c = new Column();
		c.setName("summary");
		c.setText("合计");
		c.setWidth(120);
		c.setAlignment(SWT.RIGHT);
		c.setMoveable(false);
		c.setResizeable(true);
		createColumn(grid, c);

		Date[] range = input.getCBSRange();
		Calendar start = Calendar.getInstance();
		start.setTime(range[0]);
		start.set(Calendar.DATE, 1);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);

		Calendar end = Calendar.getInstance();
		end.setTime(range[1]);

		String year = null;
		GridColumnGroup grp = null;
		while (start.before(end)) {
			String nYear = "" + start.get(Calendar.YEAR);
			if (!nYear.equals(year)) {
				// 创建合计列
				if (grp != null) {
					c = new Column();
					c.setName("summary_" + (start.get(Calendar.YEAR) - 1));
					c.setText("合计");
					c.setWidth(100);
					c.setAlignment(SWT.RIGHT);
					c.setMoveable(false);
					c.setResizeable(true);
					c.setDetail(true);
					c.setSummary(true);
					createColumn(grp, c);
				}

				// 创建gruop
				grp = new GridColumnGroup(grid, SWT.TOGGLE);
				grp.setText(nYear);
				grp.setExpanded(true);
				year = nYear;
			}
			int i = start.get(Calendar.MONTH) + 1;
			String month = String.format("%02d", i);
			c = new Column();
			c.setName(year + month);
			c.setText(i + "月");
			c.setWidth(80);
			c.setAlignment(SWT.RIGHT);
			c.setMoveable(false);
			c.setResizeable(true);
			c.setDetail(true);
			c.setSummary(false);
			createColumn(grp, c);
			start.add(Calendar.MONTH, 1);
		}
	}


}
