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
		sheet = wb.createSheet(fileName);

		// �жϱ���Ƿ����ó��ı���ʾ
		isMarkupValue = Boolean.TRUE.equals(grid.getData(RWT.MARKUP_ENABLED));

		// ��ȡ��ͷ
		GridColumn[] columns = grid.getColumns();
		createTitle(columns, grid);

		// ����������
		ITreeContentProvider cp = (ITreeContentProvider) viewer.getContentProvider();
		Object[] elements = cp.getElements(input);
		createData(columns, cp, elements);

		// ����������
		// �ж��Ƿ���ڴ���������
		if (grid.getFooterVisible()) {
			createFooter(columns, grid);
		}

		// ��������Excelд�뵽�ļ���
		FileOutputStream fos = new FileOutputStream(filePath);
		wb.write(fos); // д�ļ�
		fos.close(); // �ر��ļ�
		wb.close();

		// �������ص�ַ����
		UserSession.bruiToolkit().downloadLocalFile(filePath);
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
	private void createFooter(GridColumn[] columns, Grid grid) {
		// ������
		XSSFRow row = createRow();
		for (int i = 0; i < columns.length; i++) {
			// ��ȡ�������ı�
			String text = columns[i].getFooterText();
			XSSFCell cell = row.createCell(i);
			setCellValue(cell, text, null, true);
			// TODO �ϲ���Ԫ��
		}
	}

	/**
	 * ����������,���ص�ǰ����
	 * 
	 * @param columns
	 *            ��Ҫ��������
	 * @param cp
	 *            ��������ṩ�ߣ�ͨ��cp��ȡTree�¼�����
	 * @param elements
	 *            ��Ҫ������������
	 * @return ��ǰ����
	 */
	private void createData(GridColumn[] columns, ITreeContentProvider cp, Object[] elements) {
		// ѭ�������ݣ�����Excel�С�
		for (Object element : elements) {
			// ������
			XSSFRow row = createRow();
			for (int i = 0; i < columns.length; i++) {
				// �ж��Ƿ�Ϊ�����У������в����д�����
				if (Boolean.TRUE.equals(columns[i].getData("fixedRight"))) {
					continue;
				}
				// ������
				final XSSFCell cell = row.createCell(i);
				// ����LabelProvider��ȡ��䵽��Ԫ���е�����
				GridPartColumnLabelProvider labelProvider = (GridPartColumnLabelProvider) viewer.getLabelProvider(i);
				// TODO
				// �������м̳���GridPartDefaultRender��Class�����е�GridRenderUpdateCellע���Ƿ�ʹ����ViewerCell,��ʹ����ViewerCell����Ҫ�������д���
				labelProvider.update(element, (txt, img) -> {
					// TODO �ϲ���Ԫ��

					// ����Ԫ������ֵ
					setCellValue(cell, txt, img, isMarkupValue);
				});

			}
			// �ж��Ƿ�����¼����ݣ�����ʱ��ͨ��cp��ȡ���¼����ݲ���������������
			if (cp.hasChildren(element)) {
				Object[] children = cp.getChildren(element);
				createData(columns, cp, children);
			}
		}
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
	private void createTitle(GridColumn[] columns, Grid grid) {
		// �������Group�򴴽�Group��ͷ
		XSSFRow groupRow = null;
		if (grid.getColumnGroupCount() > 0) {
			groupRow = createRow();
		}

		GridColumnGroup previousGroup = null;
		XSSFCell cell;

		// ����������
		XSSFRow row = createRow();
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
					boolean isColumnMarkupValue = Boolean.TRUE.equals(columnGroup.getData(RWT.MARKUP_ENABLED));
					// ��ȡ��ͷ�ı�
					String text = columnGroup.getText();
					// ����ͷ�ı��ŵ�cell
					setCellValue(cell, text, null, isColumnMarkupValue);
					// �ϲ���Ԫ��
					CellRangeAddress region = new CellRangeAddress(0, 0, i, i + columnGroup.getColumns().length - 1);
					sheet.addMergedRegion(region);

					previousGroup = columnGroup;
				}
			}

			// ���������
			cell = row.createCell(i);

			// �ж���ͷ�Ƿ��ı���ʾ
			boolean isColumnMarkupValue = Boolean.TRUE.equals(columns[i].getData(RWT.MARKUP_ENABLED));

			// ��ȡ��ͷ�ı�
			String text = columns[i].getText();

			// �Д��Ƿ���ڷֽM��������ڷֽM�����Ҹ��в��ڷ����У���ϲ��������С�
			if (groupRow != null && columns[i].getColumnGroup() == null) {
				CellRangeAddress region = new CellRangeAddress(0, 1, i, i);
				sheet.addMergedRegion(region);
				cell = groupRow.getCell(i);
			}

			// ����ͷ�ı��ŵ�cell
			setCellValue(cell, text, null, isColumnMarkupValue);

			// TODO ȱ����ͷ�ϲ���Ԫ������
		}
	}

	/**
	 * ������
	 * 
	 * @return
	 */
	private XSSFRow createRow() {
		XSSFRow newRow = sheet.createRow(currentRow);
		// �����к�
		currentRow++;
		return newRow;
	}

	/**
	 * ����ͷ�ı����뵽cell�С� TODO ȱ��Image
	 * 
	 * @param cell
	 * @param text
	 * @param img
	 * @param isColumnMarkupValue
	 * @throws Exception
	 */
	private void setCellValue(XSSFCell cell, String text, Object img, boolean isColumnMarkupValue) {
		// �ı�Ϊ�ջ���ַ�ʱ��ֱ�ӷ��أ����������á�
		if (text == null || text.trim().isEmpty())
			return;

		if (isColumnMarkupValue) {
			// ��ȡ�ı�����HTML��ʽ�����ݽ��н�����ֻ����HTML���ݡ�
			try {
				text = EngUtil.parserHtml2Text(text);
			} catch (Exception e) {
				// TODO ��ʾ��Ϣ
				text = "";
				logger.error(fileName + "����Excel ��" + (cell.getRowIndex() + 1) + "�У���" + (cell.getColumnIndex() + 1)
						+ "�У����ݴ���" + e.getMessage());
			}
		}
		// TODO text�����жϣ�ȡ�������ı���ʹ�ã�ʹ�ø��ı��ڲ����ñ���и߻��ÿ���ı�������ʽ������ºͲ�ʹ��ʱ��ʾЧ��һ����
		cell.setCellValue(text);
	}

}
