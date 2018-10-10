package org.apache.poi.bizvisionsoft.excel;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PagedExcelExporter {

	private static Logger logger = LoggerFactory.getLogger(PagedExcelExporter.class);

	private XSSFDrawing patriarch;
	private XSSFWorkbook wb;
	private XSSFSheet sheet;
	private ArrayList<XSSFRow> templateRows;
	private ArrayList<CellRangeAddress> templateMergedRegion;
	private Map<CellRangeAddress, String[][]> bodyMap;

	private int maxPageCount = 0;
	private int startRow = 0;
	private List<CellRangeAddress> mergeCells = new ArrayList<>();
	private List<Object[]> customizedStyleList = new ArrayList<>();

	public PagedExcelExporter() {

	}

	public void doExport(String existExcelPath, Map<String, String> headData, Map<String, String[][]> bodyData,
			String sheetName) throws Exception {
		doExport(existExcelPath, existExcelPath, headData, bodyData, sheetName);
	}

	public void doExport(String templatePath, String outputPath, Map<String, String> headData,
			Map<String, String[][]> bodyData, String sheetName) throws Exception {
		FileOutputStream fos = new FileOutputStream(outputPath);
		doExport(templatePath, headData, bodyData, sheetName, fos);
	}

	private void doExport(String templatePath, Map<String, String> headData, Map<String, String[][]> bodyData,
			String sheetName, FileOutputStream fos) throws Exception {
		wb = null;
		sheet = null;
		templateRows = new ArrayList<>();
		templateMergedRegion = new ArrayList<>();
		bodyMap = new HashMap<>();

		wb = new XSSFWorkbook(templatePath);
		sheet = wb.getSheet("template"); //$NON-NLS-1$
		int sheetIdx = wb.getSheetIndex("template"); //$NON-NLS-1$
		wb.setSheetHidden(sheetIdx, false);
		wb.setSheetName(sheetIdx, sheetName);

		XSSFSheet template = wb.cloneSheet(sheetIdx);
		int newTemplateIndex = wb.getSheetIndex(template);
		wb.setSheetName(newTemplateIndex, "template"); //$NON-NLS-1$
		wb.setSheetHidden(newTemplateIndex, true);

		logger.debug("SheetWriter Starting..."); //$NON-NLS-1$
		patriarch = sheet.createDrawingPatriarch(); // 准备插图片用。

		fillData(headData, bodyData);
		wb.write(fos); // 写文件
		fos.close(); // 关闭文件
	}

	private void fillData(Map<String, String> headData, Map<String, String[][]> bodyData) throws Exception {
		// get region of template
		List<CellAddress> a1 = getLableIndex("PAGESTART");
		List<CellAddress> a2 = getLableIndex("PAGEEND");
		CellAddress n1 = a1.get(0);
		CellAddress n2 = a2.get(0);
		CellRangeAddress modelRegion = getArea(n1.getRow(), n1.getColumn(), n2.getRow(), n2.getColumn());
		int startRow = modelRegion.getFirstRow();
		int startCell = modelRegion.getFirstColumn();
		int endRow = modelRegion.getLastRow();
		int endCell = modelRegion.getLastColumn();
		this.startRow = startRow;

		// clear Start label cells
		sheet.getRow(startRow).getCell(startCell).setCellValue(new XSSFRichTextString(""));
		sheet.getRow(endRow).getCell(endCell).setCellValue(new XSSFRichTextString(""));

		// save Template
		for (int i = startRow; i <= endRow; i++) {
			XSSFRow tempRow = sheet.getRow(i);
			templateRows.add(tempRow);
		}

		// Save Merged Cells
		int nrgn = sheet.getNumMergedRegions();
		for (int i = 0; i < nrgn; i++) {
			templateMergedRegion.add(sheet.getMergedRegion(i));
		}

		//

		// create full template，preparing for gird and image
		templatePrepare(bodyData, endRow + 1);

		// fill bodyData
		bodyMap.forEach(this::addGrid);

		addHead(headData);

		// 处理特殊的单元格样式
		applyCustomizedStyle();

		// 处理合并单元格标签
		mergeCell();
		// clean unusedlabel
		cleanSheet();
		logger.debug("SheetWriter Finished...");

	}

	private void applyCustomizedStyle() {
		if (customizedStyleList == null)
			return;
		customizedStyleList.forEach(this::applyCellCustomizedStyle);
	}

	private void mergeCell() {
		if (mergeCells == null)
			return;
		mergeCells.forEach(sheet::addMergedRegion);
	}

	private void addHead(Map<String, String> headData) {
		headData.forEach((t, v) -> {
			List<CellAddress> idx = getLableIndex(t);
			if (t.startsWith("IMGR:")) {
				addImgInRegion(idx, v);
			} else if (t.startsWith("IMGS:")) {
				addImgInCell(idx, v);
			} else {
				addTxtHead(idx, v);
			}
		});
	}

	private void addImgInCell(List<CellAddress> lableList, String cellValue) {
		lableList.forEach(ca -> {
			int row = ca.getRow();
			int column = ca.getColumn();
			CellRangeAddress r = new CellRangeAddress(row, row, column, column);
			XSSFCellStyle csty = sheet.getRow(row).getCell(column).getCellStyle();
			try {
				addImg(csty, r, ImageIO.read(new File(cellValue)), 4f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private void addImg(XSSFCellStyle csty, CellRangeAddress r, BufferedImage bufferImg, float margin) {
		// 创建图片坐标测量器
		CellMeasure ccm = new CellMeasure(sheet);

		if (csty.getAlignment() == HorizontalAlignment.CENTER)// 居中
		{
			ccm.setAlignCenter();
		} else if (csty.getAlignment() == HorizontalAlignment.LEFT) {
			ccm.setAlignLeft();
		} else if (csty.getAlignment() == HorizontalAlignment.RIGHT) {
			ccm.setAlignRight();
		} else {
			ccm.setAlignLeft();
		}
		ccm.setMargin(margin);

		// 取图片
		try {

			Cordset cds = ccm.cordTransfer(r, bufferImg.getWidth(), bufferImg.getHeight());

			XSSFClientAnchor anchor = new XSSFClientAnchor(cds.dx1, cds.dy1, cds.dx2, cds.dy2, cds.colFrom, cds.rowFrom,
					cds.colTo, cds.rowTo);

			// TODO 考虑图片的不同设置方式
			anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);

			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			ImageIO.write(bufferImg, "jpg", byteArrayOut);
			patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void addTxtHead(List<CellAddress> lableList, String cellValue) {
		lableList.forEach(ca -> {
			XSSFCell cell = sheet.getRow(ca.getRow()).getCell(ca.getColumn());
			cell.setCellValue(new XSSFRichTextString(cellValue));
		});
	}

	private void addImgInRegion(List<CellAddress> lableList, String cellValue) {
		// 由于拷贝了模版，所以lableList的长度除2正好是若干对IMG标签。
		for (int i = 0; i < lableList.size();) {
			// 取第一对
			CellAddress c1 = lableList.get(i++);
			CellAddress c2 = lableList.get(i++);

			// 取图片显示Region
			CellRangeAddress r = new CellRangeAddress(c1.getRow(), c2.getRow(), c1.getColumn(), c2.getColumn());

			XSSFCellStyle csty = sheet.getRow(c1.getRow()).getCell(c1.getColumn()).getCellStyle();
			try {
				addImg(csty, r, ImageIO.read(new File(cellValue)), 4f);
			} catch (IOException e) {
				logger.error("写入图片失败：" + cellValue, e);
			}
		}
	}

	private void cleanSheet() {
		Iterator<Row> rowIter = sheet.rowIterator();
		while (rowIter.hasNext()) {
			Iterator<Cell> cellIter = rowIter.next().cellIterator();
			while (cellIter.hasNext()) {
				Cell cell = cellIter.next();
				if (cell.getCellType() == CellType.STRING) {
					String cellText = cell.getRichStringCellValue().getString().trim();
					if (cellText.startsWith("<") && cellText.endsWith(">")) {
						cell.setCellValue(new XSSFRichTextString());
					}
				}
			}
		}
	}

	private void addGrid(CellRangeAddress ca, String[][] matrix) {
		int initStartRow = ca.getFirstRow();
		int initStartCell = ca.getFirstColumn();
		int initEndRow = ca.getLastRow();
		int initEndCell = ca.getLastColumn();

		int columnCount = initEndCell - initStartCell + 1;
		int pageIdx = 0;
		int pageRowCount = templateRows.size();
		int pageGridRows = initEndRow - initStartRow + 1;
		int step = pageRowCount - (initEndRow - initStartRow) - 1;

		int rowNum = 0;
		for (int x = 0; x < matrix.length; x++) {// row
			int rowCord = initStartRow + pageIdx * step;
			int dataColumn = 0;
			int appendRowNum = 0;
			boolean b = false;

			for (int y = 0; y < columnCount; y++) {// cell
				CellProp cp = new CellProp(sheet, rowCord + rowNum, initStartCell + y);
				if (cp.IS_LEFTTOP || cp.IS_SINGAL) {
					String[] row = matrix[x];
					if (dataColumn < row.length) {
						int n = setCell(cp, matrix[x][dataColumn], pageGridRows, step, initStartRow, pageRowCount);

						if (n == -1) {// 需换sheet
							b = true;
							appendRowNum = 0;
							break;
						}

						if (n > appendRowNum)
							appendRowNum = n;
						dataColumn++;
					}
				}
			}
			if (b) {// 需换sheet
					// 清空当前数据
				int temp = pageGridRows - rowNum % pageGridRows;
				for (int i = 0; i < temp; i++) {
					for (int y = 0; y < columnCount; y++) {// cell
						sheet.getRow(rowCord + rowNum + i).getCell(initStartCell + y)
								.setCellValue(new XSSFRichTextString(""));
					}
				}
				x--;

				// 换sheet
				rowNum += temp - 1;
			}

			if (((rowNum + 1 + appendRowNum) / pageGridRows > 0) && ((rowNum + 1 + appendRowNum) % pageGridRows == 0)
					|| ((rowNum + 1 + appendRowNum) / pageGridRows) > ((rowNum + 1) / pageGridRows)) {
				pageIdx++;
				if (maxPageCount == 0)
					maxPageCount++;
				if (pageIdx == maxPageCount) {
					addPageFrom(pageRowCount * maxPageCount + this.startRow);
					// 清空Grid
					clearGrid(pageRowCount * maxPageCount + this.startRow);

					maxPageCount++;
				}
			}
			rowNum += appendRowNum;

			rowNum++;
		}

	}

	private void clearGrid(int rowIndex) {
		bodyMap.keySet().forEach(ca -> {
			int srow = rowIndex + ca.getFirstRow();
			int erow = rowIndex + ca.getLastRow();
			int scell = ca.getFirstColumn();
			int ecell = ca.getLastColumn();
			// clear
			for (int k = srow; k <= erow; k++) {
				for (int h = scell; h < ecell; h++) {
					this.sheet.getRow(k).getCell(h).setCellValue(new XSSFRichTextString(""));
				}
			}
		});
	}

	/**
	 * 返回插入的行数 若返回-1则表示此表格行数不够，需更换表格
	 * 
	 * @throws Exception
	 */
	private int setCell(CellProp cp, String celldata, int pageGridRows, int step, int initStartRow, int pageRowCount) {
		if (celldata == null || celldata.equals("")) {
			return 0;
		}
		XSSFCell cell = cp.CELL;

		if (celldata.contains("<M:")) {// 表示要合并单元格
			if (celldata.toUpperCase().contains("<M:")) {// 合并
				int startIdx = celldata.indexOf("<M:");
				int endIdx = celldata.indexOf(">", startIdx);
				String[] regn = celldata.substring(startIdx + 3, endIdx).split(",");
				int hCnt = Integer.parseInt(regn[0]);
				int vCnt = Integer.parseInt(regn[1]);
				CellRangeAddress region = new CellRangeAddress(cp.ROW_IDX, cp.ROW_IDX + vCnt, cp.COL_IDX,
						cp.COL_IDX + hCnt);
				mergeCells.add(region);
				celldata = celldata.substring(0, startIdx) + celldata.substring(endIdx + 1);
			}
		}

		if (celldata.contains("<S:")) {// 表示要设置特殊单元格属性
			if (celldata.toUpperCase().contains("<S:")) {// 合并
				int startIdx = celldata.indexOf("<S:");
				int endIdx = celldata.indexOf(">", startIdx);
				String[] styleCodes = celldata.substring(startIdx + 3, endIdx).split(";");
				for (int i = 0; i < styleCodes.length; i++) {
					customizedStyleList.add(new Object[] { cell, styleCodes[i] });
				}
				celldata = celldata.substring(0, startIdx) + celldata.substring(endIdx + 1);
			}
		}

		// has a blank label, write a blank
		try {
			String cellLabel = cell.getRichStringCellValue().getString();
			if (cellLabel.equals("<BLANK>")) {
				cell.setCellValue(new XSSFRichTextString());
				return 0;
			}
		} catch (Exception e) {
		}
		// symbol picture
		if (celldata.startsWith("[") && celldata.endsWith("]")) {// 有特殊符号
			// String symbolName = celldata.substring(1, celldata.indexOf(":"));
			// String symbolParm = celldata.substring(celldata.indexOf(":") + 1,
			// celldata.length() - 1);
			//
			// try {
			//
			// if (symbolName.equals("形位")) {
			// addImg(cell.getCellStyle(), cp.REGION_BELONGING,
			// DrawSymbol.getGeoTolerance(symbolParm, 1f, 1f), 3f);
			// }
			// if (symbolName.equals("偏差")) {
			// addImg(cell.getCellStyle(), cp.REGION_BELONGING,
			// DrawSymbol.getOffsetLable(symbolParm, "宋体", 48, 1f,
			// 1f), 4f);
			// }
			// if (symbolName.equals("符号")) {
			// addImg(cell.getCellStyle(), cp.REGION_BELONGING,
			// DrawSymbol.getCommonSymbol(symbolParm), 4f);
			// }
			// if (symbolName.equals("粗糙")) {
			// addImg(cell.getCellStyle(), cp.REGION_BELONGING,
			// DrawSymbol.getRoughnessSymbol(symbolParm), 1f);
			// }
			//
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			return 0;
		} else {
			return cp.setCellData(celldata, wb, pageGridRows, step, initStartRow, pageRowCount);
		}
	}

	private void applyCellCustomizedStyle(Object[] value) {
		XSSFCell cell = (XSSFCell) value[0];
		String styleCode = (String) value[1];

		XSSFCellStyle newStyle = wb.createCellStyle();

		XSSFCellStyle old = cell.getCellStyle();

		newStyle.setAlignment(old.getAlignment());
		newStyle.setBorderBottom(old.getBorderBottom());
		newStyle.setBorderLeft(old.getBorderLeft());
		newStyle.setBorderRight(old.getBorderRight());
		newStyle.setBorderTop(old.getBorderTop());
		newStyle.setBottomBorderColor(old.getBottomBorderColor());
		newStyle.setDataFormat(old.getDataFormat());
		newStyle.setFillBackgroundColor(old.getFillBackgroundColor());
		newStyle.setFillForegroundColor(old.getFillForegroundColor());
		newStyle.setFillPattern(old.getFillPattern());
		newStyle.setFont(old.getFont());
		newStyle.setHidden(old.getHidden());
		newStyle.setIndention(old.getIndention());
		newStyle.setLeftBorderColor(old.getLeftBorderColor());
		newStyle.setLocked(old.getLocked());
		newStyle.setRightBorderColor(old.getRightBorderColor());
		newStyle.setRotation(old.getRotation());
		newStyle.setTopBorderColor(old.getTopBorderColor());
		newStyle.setVerticalAlignment(old.getVerticalAlignment());
		newStyle.setWrapText(old.getWrapText());

		String styleType = styleCode.split("=")[0];
		String styleValue = styleCode.split("=")[1];
		if (styleType.equals("alignment")) {// 设置对齐
			newStyle.setAlignment(HorizontalAlignment.valueOf(styleValue));
		} else if (styleType.equals("bottom border")) {// 底部框线
			newStyle.setBorderBottom(BorderStyle.valueOf(styleValue));
		} else if (styleType.equals("left border")) {// 左部框线
			newStyle.setBorderLeft(BorderStyle.valueOf(styleValue));
		} else if (styleType.equals("right border")) {// 右部框线
			newStyle.setBorderRight(BorderStyle.valueOf(styleValue));
		} else if (styleType.equals("top border")) {// 顶部框线
			newStyle.setBorderTop(BorderStyle.valueOf(styleValue));
		} else if (styleType.equals("bottom border color")) {// 底部框线颜色
			newStyle.setBottomBorderColor(Short.parseShort(styleValue));
		} else if (styleType.equals("data format")) {// 数据格式
			newStyle.setDataFormat(Short.parseShort(styleValue));
		} else if (styleType.equals("background color")) {// 背景颜色
			newStyle.setFillBackgroundColor(Short.parseShort(styleValue));
		} else if (styleType.equals("foreground color")) {// 前景框线
			newStyle.setFillForegroundColor(Short.parseShort(styleValue));
		} else if (styleType.equals("fill pattern")) {// 填充
			newStyle.setFillPattern(FillPatternType.valueOf(styleValue));
		} else if (styleType.equals("hiden")) {// 隐藏
			newStyle.setHidden(styleValue.equals("Y"));
		} else if (styleType.equals("indent")) {// 缩进
			newStyle.setIndention(Short.parseShort(styleValue));
		} else if (styleType.equals("left border color")) {// 左框线颜色
			newStyle.setLeftBorderColor(Short.parseShort(styleValue));
		} else if (styleType.equals("locked")) {// 锁定
			newStyle.setLocked(styleValue.equals("Y"));
		} else if (styleType.equals("right border color")) {// 右框线颜色
			newStyle.setRightBorderColor(Short.parseShort(styleValue));
		} else if (styleType.equals("rotation")) {// 文字旋转
			newStyle.setRotation(Short.parseShort(styleValue));
		} else if (styleType.equals("top border color")) {// 顶框线颜色
			newStyle.setTopBorderColor(Short.parseShort(styleValue));
		} else if (styleType.equals("vertical alignment")) {// 纵向对其方式
			newStyle.setVerticalAlignment(VerticalAlignment.valueOf(styleValue));
		} else if (styleType.equals("wrap")) {// 文字自动换行
			newStyle.setWrapText(styleValue.equals("Y"));
		}

		cell.setCellStyle(newStyle);
	}

	private Map<CellRangeAddress, String[][]> templatePrepare(Map<String, String[][]> bodyData, int pageStart) {

		// get how many page needed.
		// int maxPageCount = 0;

		bodyData.forEach((l, v) -> {
			int rowCount = v.length;
			List<CellAddress> labelidx = getLableIndex(l);
			CellAddress na = labelidx.get(0);
			CellAddress nb = labelidx.get(1);
			CellRangeAddress dataRegion = getArea(na.getRow(), na.getColumn(), nb.getRow(), nb.getColumn());

			int startRowIdx = dataRegion.getFirstRow();
			int endRowIdx = dataRegion.getLastRow();

			int everyPageSpendRow = endRowIdx - startRowIdx + 1;
			int pageCount = getPageCount(rowCount, everyPageSpendRow);
			if (maxPageCount < pageCount) {
				maxPageCount = pageCount;
			}

			bodyMap.put(dataRegion, v);
		});

		logger.debug("MaxPageCount:" + maxPageCount);
		// add those pages
		int nextPageStart = pageStart;
		for (int i = 1; i < maxPageCount; i++) {
			nextPageStart = addPageFrom(nextPageStart);
		}
		return bodyMap;
	}

	private int getPageCount(int rowCount, int everyPageSpendRow) {
		int i1 = rowCount / everyPageSpendRow;
		int i2 = rowCount % everyPageSpendRow;
		int pageCount;
		if (i1 > 0 && i2 > 0) {
			pageCount = i1 + 1;
		} else {
			pageCount = i1;
		}
		return pageCount;

	}

	private int addPageFrom(int pageStart) {
		int tc = templateRows.size();
		int j = 0;
		for (int i = pageStart; i < pageStart + tc; i++) {
			XSSFRow trow = templateRows.get(j++);

			XSSFRow nrow = sheet.createRow(i);

			nrow.setHeight(trow.getHeight());

			for (int k = 0; k <= trow.getLastCellNum(); k++) {
				XSSFCell tcell = trow.getCell(k);
				XSSFCell ncell = nrow.createCell(k);
				if (tcell != null) {
					ncell.setCellStyle(tcell.getCellStyle());

					try {
						XSSFRichTextString v = tcell.getRichStringCellValue();
						if (!v.getString().equals("")) {
							ncell.setCellValue(v);
						}
					} catch (Exception e) {
						try {
							ncell.setCellValue(tcell.getDateCellValue());
						} catch (Exception e1) {
							try {
								ncell.setCellValue(tcell.getBooleanCellValue());
							} catch (Exception e2) {
								ncell.setCellValue(tcell.getNumericCellValue());
							}
						}
					}

					try {
						ncell.setCellComment(tcell.getCellComment());
					} catch (Exception e) {
					}

					try {
						ncell.setCellFormula(tcell.getCellFormula());
					} catch (Exception e) {
					}

				}
			}
		}
		for (int i = 0; i < templateMergedRegion.size(); i++) {
			CellRangeAddress tr = templateMergedRegion.get(i);
			CellRangeAddress nr = new CellRangeAddress(tr.getFirstRow() + pageStart, tr.getLastRow() + pageStart,
					tr.getFirstColumn(), tr.getLastColumn());
			sheet.addMergedRegion(nr);
		}

		return pageStart + tc;
	}

	// private Map<String, Integer> getArea(int row1, int cell1, int row2, int
	// cell2) {
	// HashMap<String, Integer> hs = new HashMap<String, Integer>();
	// if (row1 == row2) {
	// if (cell1 < cell2) {
	// hs.put("startCell", cell1);
	// hs.put("startRow", row1);
	// hs.put("endCell", cell2);
	// hs.put("endRow", row2);
	// } else {
	// hs.put("startCell", cell2);
	// hs.put("startRow", row2);
	// hs.put("endCell", cell1);
	// hs.put("endRow", row1);
	// }
	// } else {
	// if (row1 < row2) {
	// hs.put("startCell", cell1);
	// hs.put("startRow", row1);
	// hs.put("endCell", cell2);
	// hs.put("endRow", row2);
	// } else {
	// hs.put("startCell", cell2);
	// hs.put("startRow", row2);
	// hs.put("endCell", cell1);
	// hs.put("endRow", row1);
	// }
	// }
	// return hs;
	// }
	//
	private CellRangeAddress getArea(int row1, int cell1, int row2, int cell2) {
		if (row1 == row2) {
			if (cell1 < cell2) {
				return new CellRangeAddress(row1, row2, cell1, cell2);
			} else {
				return new CellRangeAddress(row2, row1, cell2, cell1);
			}
		} else {
			if (row1 < row2) {
				return new CellRangeAddress(row1, row2, cell1, cell2);
			} else {
				return new CellRangeAddress(row2, row1, cell2, cell1);
			}
		}
	}

	private List<CellAddress> getLableIndex(String lableText) {
		String aLable = "<" + lableText.trim() + ">";

		ArrayList<CellAddress> cordlist = new ArrayList<>();

		Iterator<Row> rowIter = sheet.rowIterator();
		while (rowIter.hasNext()) {
			Iterator<Cell> cellIter = rowIter.next().cellIterator();
			while (cellIter.hasNext()) {
				Cell cell = cellIter.next();
				if (cell.getCellType() == CellType.STRING) {
					String cellText = cell.getRichStringCellValue().getString().trim();
					if (cellText.equals(aLable)) {
						cordlist.add(cell.getAddress());
					}
				}
			}
		}
		return cordlist;
	}

}