package com.bizvisionsoft.bruiengine.assembly.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.rap.rwt.RWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizvisionsoft.bruiengine.assembly.GridPartColumnLabelProvider;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.util.EngUtil;

public class GridPartExcelExporter {

	private GridTreeViewer viewer;
	private Object input;
	private boolean isMarkupValue;

	public Logger logger = LoggerFactory.getLogger(GridPartExcelExporter.class);
	private String fileName;
	private int currentRow = 0;
	private XSSFSheet sheet;

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
		File folder = EngUtil.createTempDirectory();
		String filePath = folder.getPath() + "/" + fileName + ".xlsx";

		// 创建Excel工作薄
		XSSFWorkbook wb = new XSSFWorkbook();
		// 在工作薄下创建工作表
		sheet = wb.createSheet(fileName);

		// 判断表格是否设置超文本显示
		isMarkupValue = Boolean.TRUE.equals(grid.getData(RWT.MARKUP_ENABLED));

		// 获取列头
		GridColumn[] columns = grid.getColumns();
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

		// 将创建的Excel写入到文件中
		FileOutputStream fos = new FileOutputStream(filePath);
		wb.write(fos); // 写文件
		fos.close(); // 关闭文件
		wb.close();

		// 构建下载地址并打开
		UserSession.bruiToolkit().downloadLocalFile(filePath);
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
	private void createFooter(GridColumn[] columns, Grid grid) {
		// 创建行
		XSSFRow row = createRow();
		for (int i = 0; i < columns.length; i++) {
			// 获取汇总行文本
			String text = columns[i].getFooterText();
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
	private void createData(GridColumn[] columns, ITreeContentProvider cp, Object[] elements) {
		// 循环行数据，创建Excel行。
		for (Object element : elements) {
			// 创建行
			XSSFRow row = createRow();
			for (int i = 0; i < columns.length; i++) {
				// 判断是否为操作列，操作列不进行创建。
				if (Boolean.TRUE.equals(columns[i].getData("fixedRight"))) {
					continue;
				}
				// 创建列
				final XSSFCell cell = row.createCell(i);
				// 根据LabelProvider获取填充到单元格中的内容
				GridPartColumnLabelProvider labelProvider = (GridPartColumnLabelProvider) viewer.getLabelProvider(i);
				// TODO
				// 需检查所有继承与GridPartDefaultRender的Class，其中的GridRenderUpdateCell注解是否使用了ViewerCell,如使用了ViewerCell则需要单独进行处理。
				labelProvider.update(element, (txt, img) -> {
					// TODO 合并单元格

					// 给单元格设置值
					setCellValue(cell, txt, img, isMarkupValue);
				});

			}
			// 判断是否存在下级数据，存在时，通过cp获取到下级数据并创建到工作薄中
			if (cp.hasChildren(element)) {
				Object[] children = cp.getChildren(element);
				createData(columns, cp, children);
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
	private void createTitle(GridColumn[] columns, Grid grid) {
		// 如果存在Group则创建Group表头
		XSSFRow groupRow = null;
		if (grid.getColumnGroupCount() > 0) {
			groupRow = createRow();
		}

		GridColumnGroup previousGroup = null;
		XSSFCell cell;

		// 创建表格标题
		XSSFRow row = createRow();
		for (int i = 0; i < columns.length; i++) {
			// 判断是否为操作列，操作列不进行创建。
			if (Boolean.TRUE.equals(columns[i].getData("fixedRight"))) {
				continue;
			}

			// 如果存在Group表头，则先创建Group表头列
			if (groupRow != null) {
				cell = groupRow.createCell(i);
				GridColumnGroup columnGroup = columns[i].getColumnGroup();
				// 判断当前列是否存在于Group中，并判断当前列所在的Group列是否已经被创建。如当前列存在于Group中，并且所在的Group没有被创建，则为Group列写入表头并合并单元格。
				if (columnGroup != null && !columnGroup.equals(previousGroup)) {
					// 判断列头是否超文本显示
					boolean isColumnMarkupValue = Boolean.TRUE.equals(columnGroup.getData(RWT.MARKUP_ENABLED));
					// 获取列头文本
					String text = columnGroup.getText();
					// 将列头文本放到cell
					setCellValue(cell, text, null, isColumnMarkupValue);
					// 合并单元格
					CellRangeAddress region = new CellRangeAddress(0, 0, i, i + columnGroup.getColumns().length - 1);
					sheet.addMergedRegion(region);

					previousGroup = columnGroup;
				}
			}

			// 创建表格列
			cell = row.createCell(i);

			// 判断列头是否超文本显示
			boolean isColumnMarkupValue = Boolean.TRUE.equals(columns[i].getData(RWT.MARKUP_ENABLED));

			// 获取列头文本
			String text = columns[i].getText();

			// 判斷是否存在分組，如果存在分組，并且该列不在分组中，则合并上下两行。
			if (groupRow != null && columns[i].getColumnGroup() == null) {
				CellRangeAddress region = new CellRangeAddress(0, 1, i, i);
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
				text = EngUtil.parserHtml2Text(text);
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
