package com.bizvisionsoft.bruiengine.assembly.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.rap.rwt.RWT;
import org.htmlparser.Htmlparser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizvisionsoft.bruiengine.assembly.GridPartColumnLabelProvider;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.service.tools.FileTools;

public class GridPartExcelExporter {

	private GridTreeViewer viewer;
	private Object input;
	private boolean isMarkupValue;

	public Logger logger = LoggerFactory.getLogger(GridPartExcelExporter.class);
	private String fileName;
	private int currentRow = 0;
	private XSSFSheet sheet;
	private XSSFWorkbook wb;

	public GridPartExcelExporter setViewer(GridTreeViewer viewer) {
		this.viewer = viewer;
		return this;
	}

	public GridPartExcelExporter setInput(Object input) {
		this.input = input;
		return this;
	}

	public GridPartExcelExporter setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public void export() throws Exception {
		Grid grid = viewer.getGrid();
		// 构建文件名
		Pattern pattern = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");
		Matcher matcher = pattern.matcher(fileName);
		fileName = matcher.replaceAll("");

		// 创建导出文件
		File folder = FileTools.createTempDirectory(RWT.getRequest().getSession().getId().toUpperCase());
		String filePath = folder.getPath() + "/" + fileName + ".xlsx";

		// 创建Excel工作薄
		wb = new XSSFWorkbook();
		// 在工作薄下创建工作表
		sheet = wb.createSheet(fileName);

		// 判断表格是否设置超文本显示
		isMarkupValue = Boolean.TRUE.equals(grid.getData(RWT.MARKUP_ENABLED));

		// 获取表格列，并清除掉操作列
		List<GridColumn> columns = new ArrayList<GridColumn>();
		GridColumn[] cols = grid.getColumns();
		for (GridColumn col : cols)
			// 判断是否为操作列，操作列不添加到需创建的表格列中。
			if (!Boolean.TRUE.equals(col.getData("fixedRight")))
				columns.add(col);

		// 获取列头
		createTitle(columns, grid);

		// 创建数据行
		ITreeContentProvider cp = (ITreeContentProvider) viewer.getContentProvider();
		Object[] elements = cp.getElements(input);
		createData(columns, cp, elements);

		// 创建汇总行
		// 判断是否存在创建汇总行
		if (grid.getFooterVisible()) {
			createFooter(columns, grid);
		}

		// 设置表格列宽
		setColumnWidth(columns);

		// 将创建的Excel写入到文件中
		FileOutputStream fos = new FileOutputStream(filePath);
		wb.write(fos); // 写文件
		fos.close(); // 关闭文件
		wb.close();

		// 构建下载地址并打开
		UserSession.bruiToolkit().downloadLocalFile(filePath);
	}

	/**
	 * 设置Excel表格列宽
	 * 
	 * @param columns
	 *            需要设置列宽的列
	 */
	private void setColumnWidth(List<GridColumn> columns) {
		// 循环设置表格列宽。因只能设置excel表格列的字符宽度，所以需要根据获取excel的单元格字符宽度和像素宽度计算出当前列的字符宽度进行设置
		for (int i = 0; i < columns.size(); i++) {
			// 获取单元格的字符宽度
			int columnWidth = sheet.getColumnWidth(i);
			// 当字符列宽为0时，表示该列隐藏，所以不需要设置列宽.只有字符宽度为0时，像素宽度才为0，因此只需判断当前单元格字符宽度不为0
			if (columnWidth == 0)
				continue;
			// 获取单元格的像素宽度
			Float columnWidthInPixels = sheet.getColumnWidthInPixels(i);
			// 获取表格列宽度
			int width = columns.get(i).getWidth();
			// 设置excel表格列宽度，
			sheet.setColumnWidth(i, columnWidth / columnWidthInPixels.intValue() * width);
		}
	}

	/**
	 * 创建汇总行,返回当前行数
	 * 
	 * @param columns
	 *            需要创建的列
	 * @param sheet
	 *            Excel工作表
	 * @param grid
	 *            表格
	 * @param currentRow
	 *            当前行数
	 * @return 当前行数
	 */
	private void createFooter(List<GridColumn> columns, Grid grid) {
		// 创建行
		XSSFRow row = createRow();
		for (int i = 0; i < columns.size(); i++) {
			// 获取汇总行文本
			String text = columns.get(i).getFooterText();
			XSSFCell cell = row.createCell(i);
			setCellValue(cell, text, null, true);
			// TODO 合并单元格
		}
	}

	/**
	 * 创建数据行,返回当前行数
	 * 
	 * @param columns
	 *            需要创建的列
	 * @param cp
	 *            表格内容提供者，通过cp获取Tree下级数据
	 * @param elements
	 *            需要创建的行数据
	 * @return 当前行数
	 */
	private void createData(List<GridColumn> columns, ITreeContentProvider cp, Object[] elements) {
		// 循环行数据，创建Excel行。
		for (Object element : elements) {
			// 创建行
			XSSFRow row = createRow();
			for (int i = 0; i < columns.size(); i++) {
				// 创建列
				final XSSFCell cell = row.createCell(i);
				// 根据LabelProvider获取填充到单元格中的内容
				CellLabelProvider lp = viewer.getLabelProvider(i);
				if (lp instanceof GridPartColumnLabelProvider) {
					GridPartColumnLabelProvider labelProvider = (GridPartColumnLabelProvider) lp;
					// TODO
					// 需检查所有继承与GridPartDefaultRender的Class，其中的GridRenderUpdateCell注解是否使用了ViewerCell,如使用了ViewerCell则需要单独进行处理。
					labelProvider.update(element, (txt, img) -> {
						// TODO 合并单元格

						// 给单元格设置值
						setCellValue(cell, txt, img, isMarkupValue);
					});
				}

			}
			// 判断是否存在下级数据，存在时，通过cp获取到下级数据并创建到工作薄中。并创建分组
			if (cp.hasChildren(element)) {
				// 获取分组起始行行号.调用createRow方法后，currentRow默认为新行行号
				int startGroupRow = currentRow;

				Object[] children = cp.getChildren(element);
				createData(columns, cp, children);
				// 根据分组起始行和当前行创建分组。调用createRow方法后，currentRow默认为新行行号
				sheet.groupRow(startGroupRow, currentRow - 1);
			}
		}
	}

	/**
	 * 创建列头,返回当前行数
	 * 
	 * @param columns
	 *            需要创建的列
	 * @param sheet
	 *            Excel工作表
	 * @param grid
	 *            表格
	 * @param currentRow
	 *            当前行数
	 * @return 当前行数
	 */
	private void createTitle(List<GridColumn> columns, Grid grid) {
		int regionFirstRow = 0;
		// 创建表格title行和列
		XSSFRow titleRow = createRow();
		XSSFCell titleCell = titleRow.createCell(0);
		// 使用表格名称作为title
		titleCell.setCellValue(fileName);
		// TODO 设置title的样式

		CellRangeAddress region;
		// 合并title单元格
		if (columns.size() > 1) {
			region = new CellRangeAddress(regionFirstRow, regionFirstRow, 0, columns.size() - 1);
			sheet.addMergedRegion(region);
		}
		regionFirstRow++;

		// 如果存在Group则创建Group表头
		XSSFRow groupRow = null;
		if (grid.getColumnGroupCount() > 0) {
			groupRow = createRow();
		}

		GridColumnGroup previousGroup = null;
		XSSFCell cell;

		// 创建表格标题
		XSSFRow row = createRow();
		for (int i = 0; i < columns.size(); i++) {
			// 如果存在Group表头，则先创建Group表头列
			if (groupRow != null) {
				cell = groupRow.createCell(i);
				GridColumnGroup columnGroup = columns.get(i).getColumnGroup();
				// 判断当前列是否存在于Group中，并判断当前列所在的Group列是否已经被创建。如当前列存在于Group中，并且所在的Group没有被创建，则为Group列写入表头并合并单元格。
				if (columnGroup != null && !columnGroup.equals(previousGroup)) {
					// 判断列头是否超文本显示
					boolean isColumnMarkupValue = Boolean.TRUE.equals(columnGroup.getData(RWT.MARKUP_ENABLED));
					// 获取列头文本
					String text = columnGroup.getText();
					// 将列头文本放到cell
					setCellValue(cell, text, null, isColumnMarkupValue);
					// 合并单元格
					region = new CellRangeAddress(regionFirstRow, regionFirstRow, i,
							i + columnGroup.getColumns().length - 1);
					sheet.addMergedRegion(region);

					previousGroup = columnGroup;
				}
			}

			// 创建表格列
			cell = row.createCell(i);

			// 判断列头是否超文本显示
			boolean isColumnMarkupValue = Boolean.TRUE.equals(columns.get(i).getData(RWT.MARKUP_ENABLED));

			// 获取列头文本
			String text = columns.get(i).getText();

			// 判斷是否存在分組，如果存在分組，并且该列不在分组中，则合并上下两行。
			if (groupRow != null && columns.get(i).getColumnGroup() == null) {
				region = new CellRangeAddress(regionFirstRow, regionFirstRow + 1, i, i);
				sheet.addMergedRegion(region);
				cell = groupRow.getCell(i);
			}

			// 将列头文本放到cell
			setCellValue(cell, text, null, isColumnMarkupValue);

			// TODO 缺少列头合并单元格设置
		}
	}

	/**
	 * 创建行
	 * 
	 * @return
	 */
	private XSSFRow createRow() {
		XSSFRow newRow = sheet.createRow(currentRow);
		// 增加行号
		currentRow++;
		return newRow;
	}

	/**
	 * 将列头文本放入到cell中。 TODO 缺少Image
	 * 
	 * @param cell
	 * @param text
	 * @param img
	 * @param isColumnMarkupValue
	 * @throws Exception
	 */
	private void setCellValue(XSSFCell cell, String text, Object img, boolean isColumnMarkupValue) {
		// 文本为空或空字符时，直接返回，不进行设置。
		if (text == null || text.trim().isEmpty())
			return;

		if (isColumnMarkupValue) {
			// 获取文本，对HTML格式的内容进行解析，只保留HTML内容。
			try {
				text = Htmlparser.parser(text);
			} catch (Exception e) {
				// TODO 提示信息
				text = "";
				logger.error(fileName + "导出Excel 第" + (cell.getRowIndex() + 1) + "行，第" + (cell.getColumnIndex() + 1)
						+ "列，数据错误：" + e.getMessage());
			}
		}
		// TODO text类型判断，取消掉富文本的使用，使用富文本在不设置表格行高或对每个文本设置样式的情况下和不使用时显示效果一样。
		cell.setCellValue(text);
	}

}
