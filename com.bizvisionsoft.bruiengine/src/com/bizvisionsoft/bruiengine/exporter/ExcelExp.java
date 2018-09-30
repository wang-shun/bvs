package com.bizvisionsoft.bruiengine.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.UrlLauncher;
import org.htmlparser.Parser;
import org.htmlparser.visitors.TextExtractingVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizvisionsoft.bruiengine.assembly.GridPartColumnLabelProvider;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.util.EngUtil;

public class ExcelExp {

	private GridTreeViewer viewer;
	private Object input;
	private boolean isMarkupValue;

	public Logger logger = LoggerFactory.getLogger(ExcelExp.class);
	private String fileName;

	public ExcelExp setViewer(GridTreeViewer viewer) {
		this.viewer = viewer;
		return this;
	}

	public ExcelExp setInput(Object input) {
		this.input = input;
		return this;
	}

	public ExcelExp setFileName(String fileName) {
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
		XSSFSheet sheet = wb.createSheet(fileName);

		// 判断表格是否设置超文本显示
		isMarkupValue = grid.getData(RWT.MARKUP_ENABLED) != null
				&& Boolean.TRUE.equals(grid.getData(RWT.MARKUP_ENABLED));

		int currentRow = 0;
		// 获取列头
		GridColumn[] columns = grid.getColumns();
		currentRow = createTitle(columns, sheet, grid, currentRow);

		// 创建数据行
		ITreeContentProvider cp = (ITreeContentProvider) viewer.getContentProvider();
		Object[] elements = cp.getElements(input);
		currentRow = createData(columns, sheet, cp, elements, currentRow);

		// 创建汇总行
		// 判断是否存在创建汇总行
		if (grid.getFooterVisible()) {
			createFooter(columns, sheet, grid, currentRow);
		}

		// 将创建的Excel写入到文件中
		FileOutputStream fos = new FileOutputStream(filePath);
		wb.write(fos); // 写文件
		fos.close(); // 关闭文件
		wb.close();

		// 构建下载地址并打开
		String url = UserSession.bruiToolkit().createLocalFileDownloadURL(filePath);
		RWT.getClient().getService(UrlLauncher.class).openURL(url);
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
	private int createFooter(GridColumn[] columns, XSSFSheet sheet, Grid grid, int currentRow) {
		// 创建行
		XSSFRow row = sheet.createRow(currentRow);
		for (int i = 0; i < columns.length; i++) {
			// 获取汇总行文本
			String text = columns[i].getFooterText();
			XSSFCell cell = row.createCell(i);
			parseHtml(cell, text, true);
			// TODO 合并单元格
		}
		currentRow++;
		return currentRow;
	}

	/**
	 * 创建数据行,返回当前行数
	 * 
	 * @param columns
	 *            需要创建的列
	 * @param sheet
	 *            Excel工作表
	 * @param cp
	 *            表格内容提供者，通过cp获取Tree下级数据
	 * @param elements
	 *            需要创建的行数据
	 * @param currentRow
	 *            当前行数
	 * @return 当前行数
	 */
	private int createData(GridColumn[] columns, XSSFSheet sheet, ITreeContentProvider cp, Object[] elements,
			int currentRow) {
		// 循环行数据，创建Excel行。
		for (Object element : elements) {
			// 创建行
			XSSFRow row = sheet.createRow(currentRow);
			for (int i = 0; i < columns.length; i++) {
				// 判断是否为操作列，操作列不进行创建。
				if (Boolean.TRUE.equals(columns[i].getData("fixedRight"))) {
					continue;
				}
				// 创建列
				final XSSFCell cell = row.createCell(i);
				// 根据LabelProvider获取填充到单元格中的内容
				GridPartColumnLabelProvider labelProvider = (GridPartColumnLabelProvider) viewer.getLabelProvider(i);
				labelProvider.update(element, (txt, img) -> {
					parseHtml(cell, txt, isMarkupValue);
				});

				// TODO 合并单元格
			}
			// 增加行号
			currentRow++;

			// 判断是否存在下级数据，存在时，通过cp获取到下级数据并创建到工作薄中
			if (cp.hasChildren(element)) {
				Object[] children = cp.getChildren(element);
				currentRow = createData(columns, sheet, cp, children, currentRow);
			}
		}
		return currentRow;
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
	private int createTitle(GridColumn[] columns, XSSFSheet sheet, Grid grid, int currentRow) {
		// 如果存在Group则创建Group表头
		XSSFRow groupRow = null;
		if (grid.getColumnGroupCount() > 0) {
			groupRow = sheet.createRow(currentRow);
			currentRow++;
		}

		GridColumnGroup previousGroup = null;
		XSSFCell cell;

		// 创建表格标题
		XSSFRow row = sheet.createRow(currentRow);
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
					boolean isColumnMarkupValue = columnGroup.getData(RWT.MARKUP_ENABLED) != null
							&& Boolean.TRUE.equals(columnGroup.getData(RWT.MARKUP_ENABLED));
					// 获取列头文本
					String text = columnGroup.getText();
					// 将列头文本放到cell
					parseHtml(cell, text, isColumnMarkupValue);
					// 合并单元格
					CellRangeAddress region = new CellRangeAddress(0, 0, i, i + columnGroup.getColumns().length - 1);
					sheet.addMergedRegion(region);

					previousGroup = columnGroup;
				}
			}

			// 创建表格列
			cell = row.createCell(i);

			// 判断列头是否超文本显示
			boolean isColumnMarkupValue = columns[i].getData(RWT.MARKUP_ENABLED) != null
					&& Boolean.TRUE.equals(columns[i].getData(RWT.MARKUP_ENABLED));

			// 获取列头文本
			String text = columns[i].getText();

			// 将列头文本放到cell
			parseHtml(cell, text, isColumnMarkupValue);

			// TODO 缺少列头合并单元格设置
		}
		// 增加行号
		currentRow++;
		return currentRow;
	}

	/**
	 * 将列头文本放入到cell中。 TODO 缺少Image
	 * 
	 * @param cell
	 * @param text
	 * @param isColumnMarkupValue
	 * @throws Exception
	 */
	private void parseHtml(XSSFCell cell, String text, boolean isColumnMarkupValue) {
		// 获取当前单元格样式
		if (text != null && isColumnMarkupValue) {
			// 获取文本，对HTML格式的内容进行解析，只保留HTML内容。
			try {
				text = getPlainText3(text);
			} catch (Exception e) {
				// TODO 提示信息
				logger.error(fileName + "导出Excel 第" + (cell.getRowIndex() + 1) + "行，第" + (cell.getColumnIndex() + 1)
						+ "列，数据错误：" + e.getMessage());
			}
		}
		// 给单元格赋值，如值中包含换行，则使用富文本进行赋值
		if (text != null && text.contains("\n")) {
			cell.setCellValue(new XSSFRichTextString(text));
		} else {
			// TODO text类型判断
			cell.setCellValue(text);
		}
	}

	/**
	 * HTML解析,使用HtmlParser解析HTML
	 * 
	 * @param html
	 *            需解析的文本
	 * @return
	 * @throws IOException
	 */
	private String getPlainText3(String html) throws Exception {
		// 构建HtmlParser解析器，传入的String不是以HTML标记开头时，Parser认为是从文件夹中的文件中获取。
		Parser parser = new Parser("<div>" + html + "</div>");
		// 构建Text遍历器,TextExtractingVisitor将遍历html中所有的标记，并获取html标记中
		TextExtractingVisitor textVisitor = new TextExtractingVisitor();
		parser.visitAllNodesWith(textVisitor);
		return textVisitor.getExtractedText();
	}

}
