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
		// �����ļ���
		Pattern pattern = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");
		Matcher matcher = pattern.matcher(fileName);
		fileName = matcher.replaceAll("");

		// ���������ļ�
		File folder = EngUtil.createTempDirectory();
		String filePath = folder.getPath() + "/" + fileName + ".xls";

		// ����Excel
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(fileName);

		// ������Ԫ����ʽ
		font = wb.createFont();
		font.setFontHeightInPoints((short) 10); // ����߶�
		font.setColor(HSSFFont.COLOR_NORMAL); // ������ɫ
		font.setFontName("΢���ź�"); // ����

		// ���ı���ʾ
		isMarkupValue = grid.getData(RWT.MARKUP_ENABLED) != null
				&& Boolean.TRUE.equals(grid.getData(RWT.MARKUP_ENABLED));

		linesVisible = grid.getLinesVisible();

		GridColumn[] columns = grid.getColumns();

		HSSFRow row;
		HSSFCell cell;
		String text;
		// ÿһ����ַ���
		int[] wordCount = new int[columns.length];
		int[] warpCount = new int[0];

		// ������ͷ
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
					// �ж���ͷ�Ƿ�Ϊ���ı�
					boolean isColumnMarkupValue = columnGroup.getData(RWT.MARKUP_ENABLED) != null
							&& Boolean.TRUE.equals(columnGroup.getData(RWT.MARKUP_ENABLED));
					text = columns[i].getText();

					String parseHtml = parseHtml(wb, cell, text, isColumnMarkupValue);
					// �ϲ���Ԫ��
					CellRangeAddress region = new CellRangeAddress(0, 0, i, i + columnGroup.getColumns().length - 1);
					sheet.addMergedRegion(region);

					countWordAndWarp(0, parseHtml, wordCount, warpCount, i);
					previousGroup = columnGroup;
				}
			}

			// �ж���ͷ�Ƿ�Ϊ���ı�
			boolean isColumnMarkupValue = columns[i].getData(RWT.MARKUP_ENABLED) != null
					&& Boolean.TRUE.equals(columns[i].getData(RWT.MARKUP_ENABLED));
			// �ж��Ƿ�Ϊ�����У����ǲ�����ʱ�Ž��д�����
			if (!Boolean.TRUE.equals(columns[i].getData("fixedRight"))) {
				cell = row.createCell(i);
				text = columns[i].getText();

				// �Д��Ƿ���ڷֽM��������ڷֽM�����Ҹ��в��ڷ����У���ϲ��������У���������ڷ��飬��ֱ��д��text��
				if (groupRow != null && columns[i].getColumnGroup() == null) {
					// �ϲ���Ԫ��
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

		// ��������
		ITreeContentProvider cp = (ITreeContentProvider) viewer.getContentProvider();
		Object[] elements = cp.getElements(input);
		warpCount = createData(wb, sheet, columns, cp, elements, wordCount, warpCount);

		// ����������
		// �ж��Ƿ񴴽�������
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

		// �����п��
		for (int i = 0; i < columns.length; i++) {
			int j = wordCount[i];
			if (j > 255) {
				j = 254;
			}
			sheet.setColumnWidth(i, (j + 1) * 256);
		}

		// �����и߶�
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

		// ��������Excelд�뵽�ļ���
		FileOutputStream fos;
		fos = new FileOutputStream(filePath);
		wb.write(fos); // д�ļ�
		fos.close(); // �ر��ļ�

		// �������ص�ַ����
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
			// ˵����ǰ��ǰ��Ԫ�������Ϊ��
			// �ж�ǰһ�еĵ�Ԫ���ǲ���û�����ݣ�����0��ʾû������
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
				// �ж��Ƿ�Ϊ�����У����ǲ�����ʱ�Ž��д�����
				if (!Boolean.TRUE.equals(columns[i].getData("fixedRight"))) {
					cell = row.createCell(i);
					// CellLabelProvider��ȡֵ
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
		// ��ȡ��ǰ��Ԫ����ʽ
		HSSFCellStyle cellStyle = cell.getCellStyle();
		if (cellStyle == null) {
			cellStyle = wb.createCellStyle();
		}
		// ��ӱ߿�
		if (linesVisible) {
			cellStyle.setBorderBottom(BorderStyle.THIN); // �±߿�
			cellStyle.setBorderLeft(BorderStyle.THIN);// ��߿�
			cellStyle.setBorderTop(BorderStyle.THIN);// �ϱ߿�
			cellStyle.setBorderRight(BorderStyle.THIN);// �ұ߿�
		}
		// ���õ�Ԫ����ʽ
		cellStyle.setFont(font);
		if (text != null && isColumnMarkupValue) {
			// ��ȡ�ı�����HTML��ʽ�����ݽ��н�����ֻ����HTML���ݡ�
			text = getPlainText3(text);
		}
		// ����Ԫ��ֵ����ֵ�а������У���ʹ�ø��ı����и�ֵ
		if (text != null && text.contains("\n")) {
			cellStyle.setWrapText(true);// ����Ϊ�Զ�����
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
		// �ж��Ƿ�ΪDecimal
		if (isDecimal(text)) {
			try {
				double d = Double.parseDouble(text);
				cell.setCellValue(d);
				return;
			} catch (Exception e) {
			}
		}
		// �ж��Ƿ�Ϊ��ʽ����Number
		if (isFormatNumber(text)) {
			try {
				text = text.replaceAll(",", "");
				double d = Double.parseDouble(text);
				cell.setCellValue(d);
				return;
			} catch (Exception e) {
			}
		}
		// �ж��Ƿ�Ϊ����
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
