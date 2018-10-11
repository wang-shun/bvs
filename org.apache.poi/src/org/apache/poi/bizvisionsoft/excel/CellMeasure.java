package org.apache.poi.bizvisionsoft.excel;

import java.util.HashMap;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class CellMeasure {

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private XSSFSheet sheet;
	private float margin = 3f;
	private float shapeHeight = 15f;
	private String ALIGN = "LEFT";// 0左对齐，1居中，2右对齐

	public CellMeasure(XSSFSheet sheet) {
		this.sheet = sheet;
	}

	public CellMeasure(XSSFSheet sheet, float margin) {
		this.sheet = sheet;
		this.margin = margin;
	}

	public void setMargin(float margin) {
		this.margin = margin;
	}

	public void setAlignLeft() {
		this.ALIGN = "LEFT";
	}

	public void setAlignCenter() {
		this.ALIGN = "CENTER";
	}

	public void setAlignRight() {
		this.ALIGN = "CENTER";
	}

	public Cordset cordTransfer(CellRangeAddress r, int picWidth, int picHeight) {
		float rHeight = getRegionHeight(r);
		float rWidth = getRegionWidth(r);
		float newPicHeight = rHeight - margin * 2;
		float ratio = newPicHeight / picHeight;
		float newPicWidth = picWidth * ratio;

		if (newPicWidth > (rWidth - margin)) {
			newPicWidth = rWidth - margin - 1;
			ratio = newPicWidth / picWidth;
			newPicHeight = picHeight * ratio;
		}

		float startRowPoint = (rHeight - newPicHeight) / 2;
		float startColPoint;
		if (this.ALIGN.equals("LEFT")) {
			startColPoint = margin;
		} else {
			startColPoint = (rWidth - newPicWidth) / 2;
		}

		HashMap<String, Number> hsR = getPointLocRow(r, startRowPoint);
		int rowFrom = ((Number) hsR.get("ROW")).intValue();
		int pdy1 = ((Number) hsR.get("LOCATION")).intValue();
		HashMap<String, Number> hsC = getPointLocCol(r, startColPoint);
		short colFrom = ((Number) hsC.get("COLUMN")).shortValue();
		int pdx1 = ((Number) hsC.get("LOCATION")).intValue();

		float r1x = 1024f / (sheet.getColumnWidth(colFrom) / 32);
		float r1y = 256f / (sheet.getRow(rowFrom).getHeight() / shapeHeight);
		int dx1 = (int) (r1x * pdx1);
		int dy1 = (int) (r1y * pdy1);

		float endRowPoint = (rHeight - newPicHeight) / 2 + newPicHeight;
		float endColPoint;
		if (this.ALIGN.equals("LEFT")) {
			endColPoint = newPicWidth + margin;
			;
		} else {
			endColPoint = (rWidth - newPicWidth) / 2 + newPicWidth;
		}
		HashMap<String, Number> hsR1 = getPointLocRow(r, endRowPoint);
		int rowTo = ((Number) hsR1.get("ROW")).intValue();
		int pdy2 = ((Number) hsR1.get("LOCATION")).intValue();
		HashMap<String, Number> hsC1 = getPointLocCol(r, endColPoint);
		short colTo = ((Number) hsC1.get("COLUMN")).shortValue();
		int pdx2 = ((Number) hsC1.get("LOCATION")).intValue();
		float r2x = 1024f / (sheet.getColumnWidth(colTo) / 32);
		float r2y = 256f / (sheet.getRow(rowTo).getHeight() / shapeHeight);
		int dx2 = (int) (r2x * pdx2);
		int dy2 = (int) (r2y * pdy2);
		Cordset cds = new Cordset();
		cds.setDx1(dx1);
		cds.setDx2(dx2);
		cds.setDy1(dy1);
		cds.setDy2(dy2);
		cds.setrowFrom(rowFrom);
		cds.setrowTo(rowTo);
		cds.setcolFrom(colFrom);
		cds.setcolTo(colTo);

		return cds;

	}

	private HashMap<String, Number> getPointLocCol(CellRangeAddress r, float startP) {
		int rx1 = r.getFirstColumn();
		int rx2 = r.getLastColumn();
		float width = 0f;// Width是Cell相加
		HashMap<String, Number> loc = new HashMap<String, Number>();
		for (int i = rx1; i <= rx2; i++) {// 遍历Cell
			width = width + sheet.getColumnWidth(i) / 32;
			if (width >= startP) {
				loc.put("COLUMN", (Number) (i));
				float lastWidth = width - sheet.getColumnWidth(i) / 32;
				loc.put("LOCATION", (Number) (startP - lastWidth));
				break;
			}
		}
		return loc;
	}

	private HashMap<String, Number> getPointLocRow(CellRangeAddress r, float startP) {
		int ry1 = r.getFirstRow();
		int ry2 = r.getLastRow();
		float height = 0;// Height是Row相加
		HashMap<String, Number> loc = new HashMap<String, Number>();
		for (int i = ry1; i <= ry2; i++) {// 遍历row
			height = height + sheet.getRow(i).getHeight() / shapeHeight;
			if (height >= startP) {
				loc.put("ROW", (Number) i);
				loc.put("LOCATION", (Number) (startP - height + sheet.getRow(i).getHeight() / shapeHeight));
				break;
			}

		}
		return loc;
	}

	private float getRegionWidth(CellRangeAddress r) {
		int rx1 = r.getFirstColumn();
		int rx2 = r.getLastColumn();
		float width = 0;// Width是Cell相加
		for (int i = rx1; i <= rx2; i++) {// 遍历Cell
			width = width + sheet.getColumnWidth(i) / 32;
		}
		return width;
	}

	private float getRegionHeight(CellRangeAddress r) {
		int ry1 = r.getFirstRow();
		int ry2 = r.getLastRow();
		float height = 0;// Height是Row相加
		for (int i = ry1; i <= ry2; i++) {// 遍历row
			height = height + sheet.getRow(i).getHeight() / shapeHeight;
		}
		return height;
	}

}

class Cordset {
	public int dx1;
	public int dy1;
	public int dx2;
	public int dy2;
	public short colFrom;
	public int rowFrom;
	public short colTo;
	public int rowTo;

	public Cordset() {

	}

	public void setDx1(int dx1) {
		this.dx1 = dx1;
	}

	public void setDx2(int dx2) {
		this.dx2 = dx2;
	}

	public void setDy1(int dy1) {
		this.dy1 = dy1;
	}

	public void setDy2(int dy2) {
		this.dy2 = dy2;
	}

	public void setcolFrom(short colFrom) {
		this.colFrom = colFrom;
	}

	public void setrowFrom(int rowFrom) {
		this.rowFrom = rowFrom;
	}

	public void setcolTo(short colTo) {
		this.colTo = colTo;
	}

	public void setrowTo(int rowTo) {
		this.rowTo = rowTo;
	}

}