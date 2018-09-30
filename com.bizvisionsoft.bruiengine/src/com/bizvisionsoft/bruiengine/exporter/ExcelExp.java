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
		// �����ļ���
		Pattern pattern = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");
		Matcher matcher = pattern.matcher(fileName);
		fileName = matcher.replaceAll("");

		// ���������ļ�
		File folder = EngUtil.createTempDirectory();
		String filePath = folder.getPath() + "/" + fileName + ".xlsx";

		// ����Excel������
		XSSFWorkbook wb = new XSSFWorkbook();
		// �ڹ������´���������
		XSSFSheet sheet = wb.createSheet(fileName);

		// �жϱ���Ƿ����ó��ı���ʾ
		isMarkupValue = grid.getData(RWT.MARKUP_ENABLED) != null
				&& Boolean.TRUE.equals(grid.getData(RWT.MARKUP_ENABLED));

		int currentRow = 0;
		// ��ȡ��ͷ
		GridColumn[] columns = grid.getColumns();
		currentRow = createTitle(columns, sheet, grid, currentRow);

		// ����������
		ITreeContentProvider cp = (ITreeContentProvider) viewer.getContentProvider();
		Object[] elements = cp.getElements(input);
		currentRow = createData(columns, sheet, cp, elements, currentRow);

		// ����������
		// �ж��Ƿ���ڴ���������
		if (grid.getFooterVisible()) {
			createFooter(columns, sheet, grid, currentRow);
		}

		// ��������Excelд�뵽�ļ���
		FileOutputStream fos = new FileOutputStream(filePath);
		wb.write(fos); // д�ļ�
		fos.close(); // �ر��ļ�
		wb.close();

		// �������ص�ַ����
		String url = UserSession.bruiToolkit().createLocalFileDownloadURL(filePath);
		RWT.getClient().getService(UrlLauncher.class).openURL(url);
	}

	/**
	 * ����������,���ص�ǰ����
	 * 
	 * @param columns
	 *            ��Ҫ��������
	 * @param sheet
	 *            Excel������
	 * @param grid
	 *            ���
	 * @param currentRow
	 *            ��ǰ����
	 * @return ��ǰ����
	 */
	private int createFooter(GridColumn[] columns, XSSFSheet sheet, Grid grid, int currentRow) {
		// ������
		XSSFRow row = sheet.createRow(currentRow);
		for (int i = 0; i < columns.length; i++) {
			// ��ȡ�������ı�
			String text = columns[i].getFooterText();
			XSSFCell cell = row.createCell(i);
			parseHtml(cell, text, true);
			// TODO �ϲ���Ԫ��
		}
		currentRow++;
		return currentRow;
	}

	/**
	 * ����������,���ص�ǰ����
	 * 
	 * @param columns
	 *            ��Ҫ��������
	 * @param sheet
	 *            Excel������
	 * @param cp
	 *            ��������ṩ�ߣ�ͨ��cp��ȡTree�¼�����
	 * @param elements
	 *            ��Ҫ������������
	 * @param currentRow
	 *            ��ǰ����
	 * @return ��ǰ����
	 */
	private int createData(GridColumn[] columns, XSSFSheet sheet, ITreeContentProvider cp, Object[] elements,
			int currentRow) {
		// ѭ�������ݣ�����Excel�С�
		for (Object element : elements) {
			// ������
			XSSFRow row = sheet.createRow(currentRow);
			for (int i = 0; i < columns.length; i++) {
				// �ж��Ƿ�Ϊ�����У������в����д�����
				if (Boolean.TRUE.equals(columns[i].getData("fixedRight"))) {
					continue;
				}
				// ������
				final XSSFCell cell = row.createCell(i);
				// ����LabelProvider��ȡ��䵽��Ԫ���е�����
				GridPartColumnLabelProvider labelProvider = (GridPartColumnLabelProvider) viewer.getLabelProvider(i);
				labelProvider.update(element, (txt, img) -> {
					parseHtml(cell, txt, isMarkupValue);
				});

				// TODO �ϲ���Ԫ��
			}
			// �����к�
			currentRow++;

			// �ж��Ƿ�����¼����ݣ�����ʱ��ͨ��cp��ȡ���¼����ݲ���������������
			if (cp.hasChildren(element)) {
				Object[] children = cp.getChildren(element);
				currentRow = createData(columns, sheet, cp, children, currentRow);
			}
		}
		return currentRow;
	}

	/**
	 * ������ͷ,���ص�ǰ����
	 * 
	 * @param columns
	 *            ��Ҫ��������
	 * @param sheet
	 *            Excel������
	 * @param grid
	 *            ���
	 * @param currentRow
	 *            ��ǰ����
	 * @return ��ǰ����
	 */
	private int createTitle(GridColumn[] columns, XSSFSheet sheet, Grid grid, int currentRow) {
		// �������Group�򴴽�Group��ͷ
		XSSFRow groupRow = null;
		if (grid.getColumnGroupCount() > 0) {
			groupRow = sheet.createRow(currentRow);
			currentRow++;
		}

		GridColumnGroup previousGroup = null;
		XSSFCell cell;

		// ����������
		XSSFRow row = sheet.createRow(currentRow);
		for (int i = 0; i < columns.length; i++) {
			// �ж��Ƿ�Ϊ�����У������в����д�����
			if (Boolean.TRUE.equals(columns[i].getData("fixedRight"))) {
				continue;
			}

			// �������Group��ͷ�����ȴ���Group��ͷ��
			if (groupRow != null) {
				cell = groupRow.createCell(i);
				GridColumnGroup columnGroup = columns[i].getColumnGroup();
				// �жϵ�ǰ���Ƿ������Group�У����жϵ�ǰ�����ڵ�Group���Ƿ��Ѿ����������統ǰ�д�����Group�У��������ڵ�Groupû�б���������ΪGroup��д���ͷ���ϲ���Ԫ��
				if (columnGroup != null && !columnGroup.equals(previousGroup)) {
					// �ж���ͷ�Ƿ��ı���ʾ
					boolean isColumnMarkupValue = columnGroup.getData(RWT.MARKUP_ENABLED) != null
							&& Boolean.TRUE.equals(columnGroup.getData(RWT.MARKUP_ENABLED));
					// ��ȡ��ͷ�ı�
					String text = columnGroup.getText();
					// ����ͷ�ı��ŵ�cell
					parseHtml(cell, text, isColumnMarkupValue);
					// �ϲ���Ԫ��
					CellRangeAddress region = new CellRangeAddress(0, 0, i, i + columnGroup.getColumns().length - 1);
					sheet.addMergedRegion(region);

					previousGroup = columnGroup;
				}
			}

			// ���������
			cell = row.createCell(i);

			// �ж���ͷ�Ƿ��ı���ʾ
			boolean isColumnMarkupValue = columns[i].getData(RWT.MARKUP_ENABLED) != null
					&& Boolean.TRUE.equals(columns[i].getData(RWT.MARKUP_ENABLED));

			// ��ȡ��ͷ�ı�
			String text = columns[i].getText();

			// ����ͷ�ı��ŵ�cell
			parseHtml(cell, text, isColumnMarkupValue);

			// TODO ȱ����ͷ�ϲ���Ԫ������
		}
		// �����к�
		currentRow++;
		return currentRow;
	}

	/**
	 * ����ͷ�ı����뵽cell�С� TODO ȱ��Image
	 * 
	 * @param cell
	 * @param text
	 * @param isColumnMarkupValue
	 * @throws Exception
	 */
	private void parseHtml(XSSFCell cell, String text, boolean isColumnMarkupValue) {
		// ��ȡ��ǰ��Ԫ����ʽ
		if (text != null && isColumnMarkupValue) {
			// ��ȡ�ı�����HTML��ʽ�����ݽ��н�����ֻ����HTML���ݡ�
			try {
				text = getPlainText3(text);
			} catch (Exception e) {
				// TODO ��ʾ��Ϣ
				logger.error(fileName + "����Excel ��" + (cell.getRowIndex() + 1) + "�У���" + (cell.getColumnIndex() + 1)
						+ "�У����ݴ���" + e.getMessage());
			}
		}
		// ����Ԫ��ֵ����ֵ�а������У���ʹ�ø��ı����и�ֵ
		if (text != null && text.contains("\n")) {
			cell.setCellValue(new XSSFRichTextString(text));
		} else {
			// TODO text�����ж�
			cell.setCellValue(text);
		}
	}

	/**
	 * HTML����,ʹ��HtmlParser����HTML
	 * 
	 * @param html
	 *            ��������ı�
	 * @return
	 * @throws IOException
	 */
	private String getPlainText3(String html) throws Exception {
		// ����HtmlParser�������������String������HTML��ǿ�ͷʱ��Parser��Ϊ�Ǵ��ļ����е��ļ��л�ȡ��
		Parser parser = new Parser("<div>" + html + "</div>");
		// ����Text������,TextExtractingVisitor������html�����еı�ǣ�����ȡhtml�����
		TextExtractingVisitor textVisitor = new TextExtractingVisitor();
		parser.visitAllNodesWith(textVisitor);
		return textVisitor.getExtractedText();
	}

}
