package com.bizvisionsoft.bruiengine.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.UrlLauncher;

import com.bizvisionsoft.bruiengine.assembly.GridPartColumnLabelProvider;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.util.EngUtil;

public class ExcelExp {

	private GridTreeViewer viewer;
	private Object input;
	private HSSFFont font;
	private boolean linesVisible;
	private boolean isMarkupValue;

	public ExcelExp setViewer(GridTreeViewer viewer) {
		this.viewer = viewer;
		return this;
	}

	public ExcelExp setInput(Object input) {
		this.input = input;
		return this;
	}

	public void export(String fileName) throws IOException {
		Grid grid = viewer.getGrid();
		// 构建文件名
		Pattern pattern = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");
		Matcher matcher = pattern.matcher(fileName);
		fileName = matcher.replaceAll("");

		// 创建导出文件
		File folder = EngUtil.createTempDirectory();
		String filePath = folder.getPath() + "/" + fileName + ".xls";

		// 创建Excel
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(fileName);

		// 创建单元格样式
		font = wb.createFont();
		font.setFontHeightInPoints((short) 10); // 字体高度
		font.setColor(HSSFFont.COLOR_NORMAL); // 字体颜色
		font.setFontName("微软雅黑"); // 字体

		// 超文本显示
		isMarkupValue = grid.getData(RWT.MARKUP_ENABLED) != null
				&& Boolean.TRUE.equals(grid.getData(RWT.MARKUP_ENABLED));

		linesVisible = grid.getLinesVisible();

		GridColumn[] columns = grid.getColumns();

		HSSFRow row;
		HSSFCell cell;
		String text;
		// 每一列最长字符数
		int[] wordCount = new int[columns.length];
		int[] warpCount = new int[0];

		// 创建表头
		HSSFRow groupRow = null;
		if (grid.getColumnGroupCount() > 0) {
			groupRow = sheet.createRow(warpCount.length);
			warpCount = Arrays.copyOf(warpCount, warpCount.length + 1);

		}

		GridColumnGroup previousGroup = null;
		row = sheet.createRow(warpCount.length);
		warpCount = Arrays.copyOf(warpCount, warpCount.length + 1);
		for (int i = 0; i < columns.length; i++) {
			if (groupRow != null) {
				cell = groupRow.createCell(i);
				GridColumnGroup columnGroup = columns[i].getColumnGroup();
				if (columnGroup != null && !columnGroup.equals(previousGroup)) {
					// 判断列头是否为超文本
					boolean isColumnMarkupValue = columnGroup.getData(RWT.MARKUP_ENABLED) != null
							&& Boolean.TRUE.equals(columnGroup.getData(RWT.MARKUP_ENABLED));
					text = columns[i].getText();

					String parseHtml = parseHtml(wb, cell, text, isColumnMarkupValue);
					// 合并单元格
					CellRangeAddress region = new CellRangeAddress(0, 0, i, i + columnGroup.getColumns().length - 1);
					sheet.addMergedRegion(region);

					countWordAndWarp(0, parseHtml, wordCount, warpCount, i);
					previousGroup = columnGroup;
				}
			}

			// 判断列头是否为超文本
			boolean isColumnMarkupValue = columns[i].getData(RWT.MARKUP_ENABLED) != null
					&& Boolean.TRUE.equals(columns[i].getData(RWT.MARKUP_ENABLED));
			// 判断是否为操作列，不是操作列时才进行创建。
			if (!Boolean.TRUE.equals(columns[i].getData("fixedRight"))) {
				cell = row.createCell(i);
				text = columns[i].getText();

				// 判斷是否存在分組，如果存在分組，并且该列不在分组中，则合并上下两行，如果不存在分组，则直接写入text。
				if (groupRow != null && columns[i].getColumnGroup() == null) {
					// 合并单元格
					CellRangeAddress region = new CellRangeAddress(0, 1, i, i);
					sheet.addMergedRegion(region);
					cell = groupRow.getCell(i);
					String parseHtml = parseHtml(wb, cell, text, isColumnMarkupValue);
					countWordAndWarp(0, parseHtml, wordCount, warpCount, i);
				} else {
					String parseHtml = parseHtml(wb, cell, text, isColumnMarkupValue);
					countWordAndWarp(warpCount.length - 1, parseHtml, wordCount, warpCount, i);
				}
			}
		}

		// 创建数据
		ITreeContentProvider cp = (ITreeContentProvider) viewer.getContentProvider();
		Object[] elements = cp.getElements(input);
		warpCount = createData(wb, sheet, columns, cp, elements, wordCount, warpCount);

		// 创建汇总行
		// 判断是否创建汇总行
		if (grid.getFooterVisible()) {
			row = sheet.createRow(warpCount.length);
			warpCount = Arrays.copyOf(warpCount, warpCount.length + 1);
			for (int i = 0; i < columns.length; i++) {
				text = columns[i].getFooterText();
				cell = row.createCell(i);
				String parseHtml = parseHtml(wb, cell, text, false);
				countWordAndWarp(warpCount.length - 1, parseHtml, wordCount, warpCount, i);
			}
		}

		// 整理列宽度
		for (int i = 0; i < columns.length; i++) {
			int j = wordCount[i];
			if (j > 255) {
				j = 254;
			}
			sheet.setColumnWidth(i, (j + 1) * 256);
		}

		// 整理行高度
		for (int i = 0; i < warpCount.length; i++) {
			int j = warpCount[i];
			if (j == 0) {
				continue;
			}
			HSSFRow row2 = sheet.getRow(i);
			if (row2 != null) {
				short fontHeightInPoints = font.getFontHeightInPoints();
				row2.setHeightInPoints(fontHeightInPoints * (j + j));
			}
		}

		// 将创建的Excel写入到文件中
		FileOutputStream fos;
		fos = new FileOutputStream(filePath);
		wb.write(fos); // 写文件
		fos.close(); // 关闭文件

		// 构建下载地址并打开
		String url = UserSession.bruiToolkit().createLocalFileDownloadURL(filePath);
		RWT.getClient().getService(UrlLauncher.class).openURL(url);
	}

	private void countWordAndWarp(int currentRow, String text, int[] wordCount, int[] warpCount, int columnIndex) {
		String[] split = text.split("\n");
		String[] copyStr = new String[split.length];
		System.arraycopy(split, 0, copyStr, 0, copyStr.length);
		quickSort(copyStr, 0, copyStr.length - 1);
		String maxLStr = copyStr[copyStr.length - 1];
		wordCount[columnIndex] = wordCount[columnIndex] < maxLStr.getBytes().length ? maxLStr.getBytes().length
				: wordCount[columnIndex];
		int WarpNum = split.length;
		if (text.isEmpty()) {
			// 说明当前当前单元格的数据为空
			// 判断前一列的单元格是不是没有数据，等于0表示没有数据
			if (warpCount[currentRow] == 0) {
				warpCount[currentRow] = 0;
			}
		} else {
			warpCount[currentRow] = warpCount[currentRow] < WarpNum ? WarpNum : warpCount[currentRow];
		}
	}

	public void quickSort(String a[], int low, int height) {
		if (low < height) {
			int result = partition(a, low, height);
			quickSort(a, low, result - 1);
			quickSort(a, result + 1, height);
		}
	}

	public int partition(String a[], int low, int height) {
		String key = a[low];
		while (low < height) {
			while (low < height && a[height].length() >= key.length())
				height--;
			a[low] = a[height];
			while (low < height && a[low].length() <= key.length())
				low++;
			a[height] = a[low];
		}
		a[low] = key;
		return low;
	}

	private int[] createData(HSSFWorkbook wb, HSSFSheet sheet, GridColumn[] columns, ITreeContentProvider cp,
			Object[] elements, int[] wordCount, int[] warpCount) throws IOException {
		for (Object element : elements) {
			HSSFRow row;
			HSSFCell cell;
			row = sheet.createRow(warpCount.length);
			warpCount = Arrays.copyOf(warpCount, warpCount.length + 1);
			for (int i = 0; i < columns.length; i++) {
				// 判断是否为操作列，不是操作列时才进行创建。
				if (!Boolean.TRUE.equals(columns[i].getData("fixedRight"))) {
					cell = row.createCell(i);
					// CellLabelProvider获取值
					GridPartColumnLabelProvider labelProvider = (GridPartColumnLabelProvider) viewer
							.getLabelProvider(i);
					String text = labelProvider.getColumnValue(element);
					String parseHtml = parseHtml(wb, cell, text, isMarkupValue);
					countWordAndWarp(warpCount.length - 1, parseHtml, wordCount, warpCount, i);
				}
			}
			if (cp.hasChildren(element)) {
				Object[] child = cp.getChildren(element);
				warpCount = createData(wb, sheet, columns, cp, child, wordCount, warpCount);
			}
		}
		return warpCount;
	}

	private String parseHtml(HSSFWorkbook wb, HSSFCell cell, String text, boolean isColumnMarkupValue)
			throws IOException {
		// 获取当前单元格样式
		HSSFCellStyle cellStyle = cell.getCellStyle();
		if (cellStyle == null) {
			cellStyle = wb.createCellStyle();
		}
		// 添加边框
		if (linesVisible) {
			cellStyle.setBorderBottom(BorderStyle.THIN); // 下边框
			cellStyle.setBorderLeft(BorderStyle.THIN);// 左边框
			cellStyle.setBorderTop(BorderStyle.THIN);// 上边框
			cellStyle.setBorderRight(BorderStyle.THIN);// 右边框
		}
		// 设置单元格样式
		cellStyle.setFont(font);
		if (text != null && isColumnMarkupValue) {
			// 获取文本，对HTML格式的内容进行解析，只保留HTML内容。
			text = getPlainText3(text);
		}
		// 给单元格赋值，如值中包含换行，则使用富文本进行赋值
		if (text != null && text.contains("\n")) {
			cellStyle.setWrapText(true);// 设置为自动换行
			cell.setCellStyle(cellStyle);
			setCellValue(cell, new HSSFRichTextString(text));
		} else {
			cell.setCellStyle(cellStyle);
			setCellValue(cell, text);
		}
		return text;
	}

	private void setCellValue(HSSFCell cell, HSSFRichTextString richText) {
		cell.setCellValue(richText);
	}

	private void setCellValue(HSSFCell cell, String text) {
		// 判断是否为Decimal
		if (isDecimal(text)) {
			try {
				double d = Double.parseDouble(text);
				cell.setCellValue(d);
				return;
			} catch (Exception e) {
			}
		}
		// 判断是否为格式化的Number
		if (isFormatNumber(text)) {
			try {
				text = text.replaceAll(",", "");
				double d = Double.parseDouble(text);
				cell.setCellValue(d);
				return;
			} catch (Exception e) {
			}
		}
		// 判断是否为整型
		if (isWholeNumber(text)) {
			try {
				int i = Integer.parseInt(text);
				cell.setCellValue(i);
				return;
			} catch (Exception e) {
			}
		}

		cell.setCellValue(text);
	}

	public boolean isPositiveInteger(String orginal) {
		return isMatch("^\\+{0,1}[1-9]\\d*", orginal);
	}

	public boolean isNegativeInteger(String orginal) {
		return isMatch("^-[1-9]\\d*", orginal);
	}

	public boolean isWholeNumber(String orginal) {
		return isMatch("[+-]{0,1}0", orginal) || isPositiveInteger(orginal) || isNegativeInteger(orginal);
	}

	public boolean isDecimal(String orginal) {
		return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);
	}

	public boolean isFormatNumber(String orginal) {
		return isMatch("[-+]{0,1}\\d+(,\\d{3})*\\.\\d*|[-+]{0,1}\\d*(,\\d{3})*\\.\\d+", orginal);
	}

	private boolean isMatch(String regex, String orginal) {
		if (orginal == null || orginal.trim().equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(orginal);
		return isNum.matches();
	}

	public String getPlainText3(String htmlString) throws IOException {
		Html2Text html2Text = new Html2Text();
		html2Text.parse(htmlString);
		return html2Text.getText();
	}

	class Html2Text extends HTMLEditorKit.ParserCallback {
		StringBuffer s;

		public Html2Text() {
		}

		public void parse(String HtmlString) throws IOException {
			s = new StringBuffer();
			ParserDelegator delegator = new ParserDelegator();
			delegator.parse(new StringReader(HtmlString), this, Boolean.TRUE);
		}

		public void handleText(char[] text, int pos) {
			s.append(text);
		}

		public String getText() {
			return s.toString();
		}
	}
}
