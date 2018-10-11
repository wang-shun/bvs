package org.apache.poi.bizvisionsoft.excel;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CellProp {
	// 输入一个单元格判断它是合并单元格？还是单个的单元格？如果是合并单元格，它是第一个吗？

	public boolean IS_LEFTTOP = false;
	public boolean IS_SINGAL = true;
	public boolean IS_MEGERED = false;
	public CellRangeAddress REGION_BELONGING;
	public int ROW_IDX = 0;
	public int COL_IDX = 0;
	public XSSFCell CELL = null;
	public XSSFRow ROW = null;

	public CellProp(XSSFSheet sheet, int rownumber, int colnumber) {
		ROW_IDX = rownumber;
		COL_IDX = colnumber;
		ROW = sheet.getRow(ROW_IDX);
		CELL = ROW.getCell(COL_IDX);
		REGION_BELONGING = new CellRangeAddress(ROW_IDX, ROW_IDX, COL_IDX, COL_IDX);

		int m = sheet.getNumMergedRegions();
		for (int i = 0; i < m; i++) {
			CellRangeAddress r = sheet.getMergedRegion(i);
			int startRow = r.getFirstRow();
			int endRow = r.getLastRow();
			int startCell = r.getFirstColumn();
			int endCell = r.getLastColumn();
			if (rownumber <= endRow && rownumber >= startRow && colnumber <= endCell && colnumber >= startCell) {
				this.IS_MEGERED = true;
				this.IS_SINGAL = false;
				if (rownumber == startRow && colnumber == startCell) {
					this.IS_LEFTTOP = true;
				} else {
					this.IS_LEFTTOP = false;
				}
				REGION_BELONGING = r;
				break;
			}
		}
	}

	public int setCellData(String celldata, XSSFWorkbook wb, int pageGridRows, int step, int initStartRow,
			int pageRowCount) {

		this.CELL.setCellValue(new XSSFRichTextString(celldata));
		return 0;

	}

}