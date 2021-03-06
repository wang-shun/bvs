package org.apache.poi.bizvisionsoft.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class Demo {

	public static void main(String[] args) {

		new Demo().test();

		/**
		 * 注意：
		 * 
		 * 1. 模板工作表的名称必须是"template" 程序在第一次写入数据后，模板将被clone并隐藏 2.
		 * 必须指定写入的工作表的名称，工作表名称在该工作表存在时，将会报错（Excel 不能支持同名的Excel Sheet存在）
		 * 
		 */
	}
	

	public void test() {
		// 初始化转换对象
		PagedExcelExporter oe2 = new PagedExcelExporter();

		// 构造页头的数据
		Map<String, String> headData = getHeadData();

		// 构造表身数据
		Map<String, String[][]> bodyData = getBodyData();

		/**
		 * 第一次写入
		 */
		// 模板目录
		String templatePath = "d:/template.xlsx"; //$NON-NLS-1$
		// 输出文件目录
		String output = "d:/output.xlsx"; //$NON-NLS-1$
		// 写入到工作表的名称
		String sheetName = "test"; //$NON-NLS-1$
		try {
			oe2.doExport(templatePath, output, headData, bodyData, sheetName);
		} catch (Exception e) {
			e.printStackTrace();
		}

//		/**
//		 * 第二次写入,templatePath 必须等于 output 以后每次写入都采用相同的方式
//		 */
//		// 写入到工作表的名称
//		sheetName = "test01"; //$NON-NLS-1$
//		try {
//			oe2.doExport(output, headData, bodyData, sheetName);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	private static Map<String, String[][]> getBodyData() {
		Map<String, String[][]> bodyData = new HashMap<>();
		// 表身可以支持放入多个表身，标签以GRD:开头的数字表示，例如GRD:1,对应到EXCEL模板
		int rowNumber = 84;
		int colNumber = 7;// 必须要和模板对应，模板中如果有空的列，也必须技术在内，请参考模板
		String[][] griddata = new String[rowNumber][colNumber];// 加入一个7列20行的数据

		int i = 0;
		int j = 0;

		griddata[i][j] = "1";
		j++;
		griddata[i][j] = "2620900008";
		j++;
		griddata[i][j] = "FYL0812.01";
		j++;
		griddata[i][j] = "底座";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "2";
		j++;
		griddata[i][j] = "2620900021";
		j++;
		griddata[i][j] = "FYL0812.01-01";
		j++;
		griddata[i][j] = "围板";
		j++;
		griddata[i][j] = "FYL0812.01";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "3";
		j++;
		griddata[i][j] = "2620900022";
		j++;
		griddata[i][j] = "FYL0812.01-02";
		j++;
		griddata[i][j] = "支腿";
		j++;
		griddata[i][j] = "FYL0812.01";
		j++;
		griddata[i][j] = "4.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "4";
		j++;
		griddata[i][j] = "2620900023";
		j++;
		griddata[i][j] = "FYL0812.01-03";
		j++;
		griddata[i][j] = "圆板";
		j++;
		griddata[i][j] = "FYL0812.01";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "5";
		j++;
		griddata[i][j] = "2620900024";
		j++;
		griddata[i][j] = "FYL0812.01-04";
		j++;
		griddata[i][j] = "加强筋";
		j++;
		griddata[i][j] = "FYL0812.01";
		j++;
		griddata[i][j] = "6.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "6";
		j++;
		griddata[i][j] = "2620900025";
		j++;
		griddata[i][j] = "FYL0812.01-05";
		j++;
		griddata[i][j] = "固定座";
		j++;
		griddata[i][j] = "FYL0812.01";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "7";
		j++;
		griddata[i][j] = "2620900009";
		j++;
		griddata[i][j] = "FYL0812.02";
		j++;
		griddata[i][j] = "下转盘";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "8";
		j++;
		griddata[i][j] = "2620900026";
		j++;
		griddata[i][j] = "FYL0812.02-01";
		j++;
		griddata[i][j] = "圆板";
		j++;
		griddata[i][j] = "FYL0812.02";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "9";
		j++;
		griddata[i][j] = "2620900027";
		j++;
		griddata[i][j] = "FYL0812.02-02";
		j++;
		griddata[i][j] = "加强筋";
		j++;
		griddata[i][j] = "FYL0812.02";
		j++;
		griddata[i][j] = "8.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "10";
		j++;
		griddata[i][j] = "2620900028";
		j++;
		griddata[i][j] = "FYL0812.02-03";
		j++;
		griddata[i][j] = "固定座";
		j++;
		griddata[i][j] = "FYL0812.02";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "11";
		j++;
		griddata[i][j] = "3750201004";
		j++;
		griddata[i][j] = "GB/T41-2000";
		j++;
		griddata[i][j] = "六角螺母M8";
		j++;
		griddata[i][j] = "FYL0812.02";
		j++;
		griddata[i][j] = "8.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "12";
		j++;
		griddata[i][j] = "2620900001";
		j++;
		griddata[i][j] = "FYL0812-01";
		j++;
		griddata[i][j] = "轴承座";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "13";
		j++;
		griddata[i][j] = "2620900002";
		j++;
		griddata[i][j] = "FYL0812-02";
		j++;
		griddata[i][j] = "端盖";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "14";
		j++;
		griddata[i][j] = "2620900003";
		j++;
		griddata[i][j] = "FYL0812-03";
		j++;
		griddata[i][j] = "压盖";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "15";
		j++;
		griddata[i][j] = "3750104017";
		j++;
		griddata[i][j] = "GB/T70.1-2000";
		j++;
		griddata[i][j] = "内六圆柱头螺钉M8×16";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "2.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "16";
		j++;
		griddata[i][j] = "3750605003";
		j++;
		griddata[i][j] = "GB/T297-1994";
		j++;
		griddata[i][j] = "圆锥滚子轴承30212";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "2.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "17";
		j++;
		griddata[i][j] = "3750101052";
		j++;
		griddata[i][j] = "GB/T5783-2000";
		j++;
		griddata[i][j] = "六角头螺栓M12×40";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "5.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "18";
		j++;
		griddata[i][j] = "3750303004";
		j++;
		griddata[i][j] = "GB/T93-1987";
		j++;
		griddata[i][j] = "弹性垫圈12";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "18.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "19";
		j++;
		griddata[i][j] = "3750301004";
		j++;
		griddata[i][j] = "GB/T95-2002";
		j++;
		griddata[i][j] = "平垫圈12";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "18.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "20";
		j++;
		griddata[i][j] = "2620900007";
		j++;
		griddata[i][j] = "FYL0812-07";
		j++;
		griddata[i][j] = "密封圈";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "21";
		j++;
		griddata[i][j] = "2620900057";
		j++;
		griddata[i][j] = "";
		j++;
		griddata[i][j] = "留样桶";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "8.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "22";
		j++;
		griddata[i][j] = "2620900010";
		j++;
		griddata[i][j] = "FYL0812.03";
		j++;
		griddata[i][j] = "顶盖";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "23";
		j++;
		griddata[i][j] = "2620900021";
		j++;
		griddata[i][j] = "FYL0812.01-01";
		j++;
		griddata[i][j] = "围板";
		j++;
		griddata[i][j] = "FYL0812.03";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "24";
		j++;
		griddata[i][j] = "2620900029";
		j++;
		griddata[i][j] = "FYL0812.03-01";
		j++;
		griddata[i][j] = "圆板";
		j++;
		griddata[i][j] = "FYL0812.03";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "25";
		j++;
		griddata[i][j] = "2620900030";
		j++;
		griddata[i][j] = "FYL0812.03-02";
		j++;
		griddata[i][j] = "加强筋";
		j++;
		griddata[i][j] = "FYL0812.03";
		j++;
		griddata[i][j] = "6.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "26";
		j++;
		griddata[i][j] = "2620900031";
		j++;
		griddata[i][j] = "FYL0812.03-03";
		j++;
		griddata[i][j] = "固定座";
		j++;
		griddata[i][j] = "FYL0812.03";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "27";
		j++;
		griddata[i][j] = "2620900011";
		j++;
		griddata[i][j] = "FYL0812.04";
		j++;
		griddata[i][j] = "上转盘";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "28";
		j++;
		griddata[i][j] = "2620900032";
		j++;
		griddata[i][j] = "FYL0812.04-01";
		j++;
		griddata[i][j] = "圆板";
		j++;
		griddata[i][j] = "FYL0812.04";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "29";
		j++;
		griddata[i][j] = "2620900033";
		j++;
		griddata[i][j] = "FYL0812.04-02";
		j++;
		griddata[i][j] = "圆管";
		j++;
		griddata[i][j] = "FYL0812.04";
		j++;
		griddata[i][j] = "8.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "30";
		j++;
		griddata[i][j] = "2620900027";
		j++;
		griddata[i][j] = "FYL0812.02-02";
		j++;
		griddata[i][j] = "加强筋";
		j++;
		griddata[i][j] = "FYL0812.04";
		j++;
		griddata[i][j] = "8.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "31";
		j++;
		griddata[i][j] = "2620900034";
		j++;
		griddata[i][j] = "FYL0812.04-04";
		j++;
		griddata[i][j] = "固定座";
		j++;
		griddata[i][j] = "FYL0812.04";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "32";
		j++;
		griddata[i][j] = "2620900017";
		j++;
		griddata[i][j] = "FYL0812.10";
		j++;
		griddata[i][j] = "限位盘";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "8.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "33";
		j++;
		griddata[i][j] = "2620900050";
		j++;
		griddata[i][j] = "FYL0812.10-01";
		j++;
		griddata[i][j] = "上圈";
		j++;
		griddata[i][j] = "FYL0812.10";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "34";
		j++;
		griddata[i][j] = "2620900051";
		j++;
		griddata[i][j] = "FYL0812.10-02";
		j++;
		griddata[i][j] = "下圈";
		j++;
		griddata[i][j] = "FYL0812.10";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "35";
		j++;
		griddata[i][j] = "3750101040";
		j++;
		griddata[i][j] = "GB/T5783-2000";
		j++;
		griddata[i][j] = "六角头螺栓M8×16";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "16.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "36";
		j++;
		griddata[i][j] = "2620900012";
		j++;
		griddata[i][j] = "FYL0812.05";
		j++;
		griddata[i][j] = "短支撑杆";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "4.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "37";
		j++;
		griddata[i][j] = "2620900035";
		j++;
		griddata[i][j] = "FYL0812.05-01";
		j++;
		griddata[i][j] = "底板";
		j++;
		griddata[i][j] = "FYL0812.05";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "38";
		j++;
		griddata[i][j] = "2620900036";
		j++;
		griddata[i][j] = "FYL0812.05-02";
		j++;
		griddata[i][j] = "方管";
		j++;
		griddata[i][j] = "FYL0812.05";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "39";
		j++;
		griddata[i][j] = "2620900037";
		j++;
		griddata[i][j] = "FYL0812.05-03";
		j++;
		griddata[i][j] = "底板";
		j++;
		griddata[i][j] = "FYL0812.05";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "40";
		j++;
		griddata[i][j] = "2620900013";
		j++;
		griddata[i][j] = "FYL0812.06";
		j++;
		griddata[i][j] = "轴";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "41";
		j++;
		griddata[i][j] = "2620900038";
		j++;
		griddata[i][j] = "FYL0812.06-01";
		j++;
		griddata[i][j] = "上法兰";
		j++;
		griddata[i][j] = "FYL0812.06";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "42";
		j++;
		griddata[i][j] = "2620900039";
		j++;
		griddata[i][j] = "FYL0812.06-02";
		j++;
		griddata[i][j] = "空心轴";
		j++;
		griddata[i][j] = "FYL0812.06";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "43";
		j++;
		griddata[i][j] = "2620900040";
		j++;
		griddata[i][j] = "FYL0812.06-03";
		j++;
		griddata[i][j] = "下法兰";
		j++;
		griddata[i][j] = "FYL0812.06";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "44";
		j++;
		griddata[i][j] = "2620900041";
		j++;
		griddata[i][j] = "FYL0812.06-04";
		j++;
		griddata[i][j] = "轴头1";
		j++;
		griddata[i][j] = "FYL0812.06";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "45";
		j++;
		griddata[i][j] = "2620900042";
		j++;
		griddata[i][j] = "FYL0812.06-05";
		j++;
		griddata[i][j] = "轴头2";
		j++;
		griddata[i][j] = "FYL0812.06";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "46";
		j++;
		griddata[i][j] = "3750605007";
		j++;
		griddata[i][j] = "GB/T297-1995";
		j++;
		griddata[i][j] = "圆锥滚子轴承30216";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "3.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "47";
		j++;
		griddata[i][j] = "3750504013";
		j++;
		griddata[i][j] = "GB/T1096-2003";
		j++;
		griddata[i][j] = "平键A型14×9×160";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "48";
		j++;
		griddata[i][j] = "3750303003";
		j++;
		griddata[i][j] = "GB/T93-1987";
		j++;
		griddata[i][j] = "弹性垫圈10";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "56.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "49";
		j++;
		griddata[i][j] = "3750301003";
		j++;
		griddata[i][j] = "GB/T95-2002";
		j++;
		griddata[i][j] = "平垫圈10";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "56.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "50";
		j++;
		griddata[i][j] = "3750101045";
		j++;
		griddata[i][j] = "GB/T5783-2000";
		j++;
		griddata[i][j] = "六角头螺栓M10×25";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "56.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "51";
		j++;
		griddata[i][j] = "3750119003";
		j++;
		griddata[i][j] = "GB/T825-1988";
		j++;
		griddata[i][j] = "吊环螺钉M16";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "2.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "52";
		j++;
		griddata[i][j] = "2620900014";
		j++;
		griddata[i][j] = "FYL0812.07";
		j++;
		griddata[i][j] = "长支撑杆";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "6.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "53";
		j++;
		griddata[i][j] = "2620900043";
		j++;
		griddata[i][j] = "FYL0812.07-01";
		j++;
		griddata[i][j] = "顶板";
		j++;
		griddata[i][j] = "FYL0812.07";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "54";
		j++;
		griddata[i][j] = "2620900044";
		j++;
		griddata[i][j] = "FYL0812.07-02";
		j++;
		griddata[i][j] = "方管";
		j++;
		griddata[i][j] = "FYL0812.07";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "55";
		j++;
		griddata[i][j] = "2620900037";
		j++;
		griddata[i][j] = "FYL0812.05-03";
		j++;
		griddata[i][j] = "底板";
		j++;
		griddata[i][j] = "FYL0812.07";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "56";
		j++;
		griddata[i][j] = "2620900016";
		j++;
		griddata[i][j] = "FYL0812.09";
		j++;
		griddata[i][j] = "门";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "2.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "57";
		j++;
		griddata[i][j] = "2620900048";
		j++;
		griddata[i][j] = "FYL0812.09-01";
		j++;
		griddata[i][j] = "外板";
		j++;
		griddata[i][j] = "FYL0812.09";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "58";
		j++;
		griddata[i][j] = "2620900049";
		j++;
		griddata[i][j] = "FYL0812.09-02";
		j++;
		griddata[i][j] = "加强筋";
		j++;
		griddata[i][j] = "FYL0812.09";
		j++;
		griddata[i][j] = "2.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "59";
		j++;
		griddata[i][j] = "3750903001";
		j++;
		griddata[i][j] = "MS305Z3A050001";
		j++;
		griddata[i][j] = "门锁（带锁芯）";
		j++;
		griddata[i][j] = "FYL0812.09";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "60";
		j++;
		griddata[i][j] = "2620900047";
		j++;
		griddata[i][j] = "FYL0812.08-03";
		j++;
		griddata[i][j] = "垫板";
		j++;
		griddata[i][j] = "FYL0812.09";
		j++;
		griddata[i][j] = "4.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "61";
		j++;
		griddata[i][j] = "3750901003";
		j++;
		griddata[i][j] = "CL226Z1A015";
		j++;
		griddata[i][j] = "铰链";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "4.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "62";
		j++;
		griddata[i][j] = "3750108015";
		j++;
		griddata[i][j] = "GB/T819.2-1997";
		j++;
		griddata[i][j] = "十字槽沉头螺钉M8×16";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "24.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "63";
		j++;
		griddata[i][j] = "3750303001";
		j++;
		griddata[i][j] = "GB/T93-1987";
		j++;
		griddata[i][j] = "弹性垫圈6";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "16.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "64";
		j++;
		griddata[i][j] = "2620900015";
		j++;
		griddata[i][j] = "FYL0812.08";
		j++;
		griddata[i][j] = "围板";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "2.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "65";
		j++;
		griddata[i][j] = "2620900045";
		j++;
		griddata[i][j] = "FYL0812.08-01";
		j++;
		griddata[i][j] = "外板";
		j++;
		griddata[i][j] = "FYL0812.08";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "66";
		j++;
		griddata[i][j] = "2620900046";
		j++;
		griddata[i][j] = "FYL0812.08-02";
		j++;
		griddata[i][j] = "加强筋";
		j++;
		griddata[i][j] = "FYL0812.08";
		j++;
		griddata[i][j] = "2.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "67";
		j++;
		griddata[i][j] = "2620900047";
		j++;
		griddata[i][j] = "FYL0812.08-03";
		j++;
		griddata[i][j] = "垫板";
		j++;
		griddata[i][j] = "FYL0812.08";
		j++;
		griddata[i][j] = "4.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "68";
		j++;
		griddata[i][j] = "3750301001";
		j++;
		griddata[i][j] = "GB/T95-2002";
		j++;
		griddata[i][j] = "平垫圈6";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "16.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "69";
		j++;
		griddata[i][j] = "3750101037";
		j++;
		griddata[i][j] = "GB/T5783-2000";
		j++;
		griddata[i][j] = "六角头螺栓M6×16";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "16.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "70";
		j++;
		griddata[i][j] = "2620900005";
		j++;
		griddata[i][j] = "FYL0812-05";
		j++;
		griddata[i][j] = "接近开关支架";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "71";
		j++;
		griddata[i][j] = "3710501101";
		j++;
		griddata[i][j] = "";
		j++;
		griddata[i][j] = "接近开关";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "72";
		j++;
		griddata[i][j] = "2620900018";
		j++;
		griddata[i][j] = "FYL0812.11";
		j++;
		griddata[i][j] = "零位感应块";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "73";
		j++;
		griddata[i][j] = "2620900052";
		j++;
		griddata[i][j] = "FYL0812.11-01";
		j++;
		griddata[i][j] = "感应板";
		j++;
		griddata[i][j] = "FYL0812.11";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "74";
		j++;
		griddata[i][j] = "2620900053";
		j++;
		griddata[i][j] = "FYL0812.11-02";
		j++;
		griddata[i][j] = "固定板";
		j++;
		griddata[i][j] = "FYL0812.11";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "75";
		j++;
		griddata[i][j] = "2620900019";
		j++;
		griddata[i][j] = "FYL0812.12";
		j++;
		griddata[i][j] = "感应块";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "7.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "76";
		j++;
		griddata[i][j] = "2620900054";
		j++;
		griddata[i][j] = "FYL0812.12-01";
		j++;
		griddata[i][j] = "感应板";
		j++;
		griddata[i][j] = "FYL0812.12";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "77";
		j++;
		griddata[i][j] = "2620900053";
		j++;
		griddata[i][j] = "FYL0812.11-02";
		j++;
		griddata[i][j] = "固定板";
		j++;
		griddata[i][j] = "FYL0812.12";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "78";
		j++;
		griddata[i][j] = "2620900020";
		j++;
		griddata[i][j] = "FYL0812.13";
		j++;
		griddata[i][j] = "弹簧卡箍";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "8.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "79";
		j++;
		griddata[i][j] = "2620900055";
		j++;
		griddata[i][j] = "FYL0812.13-01";
		j++;
		griddata[i][j] = "弹簧片";
		j++;
		griddata[i][j] = "FYL0812.13";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "80";
		j++;
		griddata[i][j] = "2620900056";
		j++;
		griddata[i][j] = "FYL0812.13-02";
		j++;
		griddata[i][j] = "固定座";
		j++;
		griddata[i][j] = "FYL0812.13";
		j++;
		griddata[i][j] = "2.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "81";
		j++;
		griddata[i][j] = "3751202001";
		j++;
		griddata[i][j] = "GB/T12618-1990";
		j++;
		griddata[i][j] = "开口型扁圆头抽芯铆钉 5×9";
		j++;
		griddata[i][j] = "FYL0812.13";
		j++;
		griddata[i][j] = "4.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "82";
		j++;
		griddata[i][j] = "2620900004";
		j++;
		griddata[i][j] = "FYL0812-04";
		j++;
		griddata[i][j] = "密封圈";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "83";
		j++;
		griddata[i][j] = "3750101056";
		j++;
		griddata[i][j] = "GB/T5783-2000";
		j++;
		griddata[i][j] = "六角头螺栓M16×35";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "3.0";
		j++;
		griddata[i][j] = "";
		j++;
		i++;
		j = 0;
		griddata[i][j] = "84";
		j++;
		griddata[i][j] = "2620900006";
		j++;
		griddata[i][j] = "FYL0812-06";
		j++;
		griddata[i][j] = "管接头";
		j++;
		griddata[i][j] = "FYL0812.00";
		j++;
		griddata[i][j] = "1.0";
		j++;
		griddata[i][j] = "";
		j++;

		bodyData.put("GRD:1", griddata);
		return bodyData;

	}

	@SuppressWarnings({ "unchecked", "unused" })
	private static ArrayList getBodyData1() {
		ArrayList bodyData = new ArrayList();
		// 表身可以支持放入多个表身，标签以GRD:开头的数字表示，例如GRD:1,对应到EXCEL模板
		int rowNumber = 37;
		int colNumber = 7;// 必须要和模板对应，模板中如果有空的列，也必须技术在内，请参考模板
		String[][] griddata = new String[rowNumber][colNumber];// 加入一个7列20行的数据
		for (int i = 0; i < rowNumber; i++) {
			for (int j = 0; j < colNumber; j++) {
				if (j == 3) {
					griddata[i][j] = "这是一个需要换行的数据"; //$NON-NLS-1$
				} else {
					griddata[i][j] = "I" + i + "J" + j; //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}

		Object[] grid = new Object[] { "GRD:1", griddata }; //$NON-NLS-1$
		bodyData.add(grid);
		return bodyData;
	}

	private static Map<String, String> getHeadData() {
		Map<String, String> headData = new HashMap<>();
		// 每个标签对应一个字符串，该字符串将替代在excel模板对应位子的表签
		headData.put("ItemNo", "1234ItemNo");
		headData.put("ItemName", "产品ItemName");
		headData.put("DrawingNo", "345DrawingNo");
		return headData;
	}

}
