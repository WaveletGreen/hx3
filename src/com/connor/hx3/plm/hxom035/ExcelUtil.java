package com.connor.hx3.plm.hxom035;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel ǩ��
 * 
 * @author hub 2015-09-06
 */
public class ExcelUtil {
	// private static Registry reg = Registry.getRegistry(this);
	// private static XSSFSheet sheet;
	// private static final int MY_ROWS = 46;

	/**
	 * ��ȡEXCEL��
	 * 
	 * @param File
	 *            excel_file
	 * @return List<List<String>> ���ص���Ϣ
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static List<List<String>> readExcel(File excel_file)
			throws FileNotFoundException, IOException {
		if (excel_file == null || !excel_file.exists()) {
			return null;
		}
		// �������ص����е��е���Ϣ
		List<List<String>> lines_msg = new ArrayList<List<String>>();
		XSSFWorkbook workBook = new XSSFWorkbook(
				new FileInputStream(excel_file));
		XSSFFormulaEvaluator evaluator = new XSSFFormulaEvaluator(workBook);
		XSSFSheet sheet = workBook.getSheetAt(0);
		XSSFRow title_row = sheet.getRow(0);
		// �õ��е���Ŀ
		int col_count = title_row.getPhysicalNumberOfCells();
		System.out.println(" SHEET COL COUNT " + col_count);
		// �õ��е���Ŀ
		int row_count = sheet.getLastRowNum();

		System.out.println(" SHEET ROW COUNT " + row_count);
		for (int j = 0; j < row_count + 1; j++) {
			// �������ص��е���Ϣ
			XSSFRow row = sheet.getRow(j);
			List<String> line_msg = new ArrayList<String>();
			for (int i = 0; i < col_count; i++) {
				String value = getStringCellValue(evaluator,
						row.getCell((short) i));
				line_msg.add(value);
			}
			lines_msg.add(line_msg);
		}
		// insertRow(sheet,4,1);
		return lines_msg;
	}

	public static void writeEmptyPasteMSG(String intExcelFilePath)
			throws Exception {
		FileInputStream inPut = new FileInputStream(new File(intExcelFilePath));
		XSSFWorkbook wb = new XSSFWorkbook(inPut);
		XSSFSheet sheet = wb.getSheetAt(0);

		for (int i = 2; i <= sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);
			for (int j = 0; j < 8; j++) {
				XSSFCell cell0 = row.getCell(j);
				if (cell0 == null) {
					cell0 = row.createCell(j);
					cell0.setCellType(Cell.CELL_TYPE_STRING);
				}
				cell0.setCellValue("");
			}

		}
		inPut.close();

	}

	public static void writePasteMSG(String intExcelFilePath,
			String outExcelFilePath, List<PasteBean> beanList) throws Exception {
		// ����ļ���·��s
		FileOutputStream outPut = new FileOutputStream(new File(
				outExcelFilePath));
		// �����ļ���·��
		FileInputStream inPut = new FileInputStream(new File(intExcelFilePath));
		XSSFWorkbook wb = new XSSFWorkbook(inPut);
		XSSFSheet sheet = wb.getSheetAt(0);
		int index = sheet.getLastRowNum();
		if (index == 1) {
			XSSFRow row = sheet.createRow(2);
			for (int i = 0; i < 8; i++) {
				XSSFCell cell = row.createCell(i);
				cell.setCellType(Cell.CELL_TYPE_STRING);
			}
			// sheet.getRow(2);
		}
		for (int i = (index - 1); i < beanList.size(); i++) {
			insertRows(sheet, 1, 1);
		}
		for (int i = 0; i < beanList.size(); i++) {
			PasteBean bean = beanList.get(i);
			XSSFRow row = sheet.getRow(i + 2);
			XSSFCell cell0 = row.getCell(0);
			cell0.setCellValue("" + (1 + i));
			XSSFCell cell1 = row.getCell(1);
			cell1.setCellValue(bean.getDrawingsIndex());
			XSSFCell cell2 = row.getCell(2);
			cell2.setCellValue(bean.getDrawingsName());
			XSSFCell cell3 = row.getCell(3);
			cell3.setCellValue(bean.getDrawingsRev());
			XSSFCell cell4 = row.getCell(4);
			cell4.setCellValue(bean.getOwning_user());
			XSSFCell cell5 = row.getCell(5);
			cell5.setCellValue(bean.getDate_released());
			XSSFCell cell6 = row.getCell(6);
			cell6.setCellValue(bean.getToStrng());
			XSSFCell cell7 = row.getCell(7);
			cell7.setCellValue(bean.getWFKH());

		}

		wb.write(outPut);
		inPut.close();
		outPut.close();

	}

	public void writeSignName(String intExcelFilePath, String outExcelFilePath,
			String signName, String msgValue, int sheetIndex, int rowIndex,
			int cellIndex) throws IOException {
		// ����ļ���·��
		FileOutputStream outPut = new FileOutputStream(new File(
				outExcelFilePath));
		// �����ļ���·��
		FileInputStream inPut = new FileInputStream(new File(intExcelFilePath));
		XSSFWorkbook wb = new XSSFWorkbook(inPut);
		wb.getName("");
		AreaReference[] areaR2 = AreaReference.generateContiguous(wb
				.getName("").getRefersToFormula());
		AreaReference areaR = null;
		CellReference[] cellR = areaR.getAllReferencedCells();
		cellR[0].getSheetName();// s
		cellR[0].getCol();//
		cellR[0].getRow();//
		XSSFFormulaEvaluator evaluator = new XSSFFormulaEvaluator(wb);
		XSSFSheet sheet = wb.getSheetAt(sheetIndex);
		XSSFRow row = sheet.getRow(rowIndex);

		XSSFCell cell = row.getCell(cellIndex);
		String value = getStringCellValue(evaluator, cell);
		if (value.startsWith(signName)) {
			value = value.substring(0, signName.length());
		}

		cell.setCellValue(value + msgValue);
		wb.write(outPut);
		inPut.close();
		outPut.close();
	}

	/***
	 * д�뵽EXCEL���� ��д�뵥�����ݣ�
	 * 
	 * @param xls_write_Address
	 * @param beanListAll
	 * @param sheetnames
	 * @throws IOException
	 */
	public void writeExcel(String xls_write_Address, String path,
			List<PasteBean> beanListAll, int index) throws IOException {
		FileOutputStream output = new FileOutputStream(new File(path)); // ��ȡ���ļ�·��
		FileInputStream input = new FileInputStream(new File(xls_write_Address));
		XSSFWorkbook wb = new XSSFWorkbook(input);// (new
		XSSFSheet sheet = wb.getSheetAt(0);
		for (int i = 0; i < beanListAll.size(); i++) {
			PasteBean bean = beanListAll.get(i);
			XSSFRow row = null;
			Boolean isCreat = false;
			if ((i + 3) > sheet.getLastRowNum()) {
				row = sheet.createRow(i + 3);
				isCreat = true;
			} else {
				row = sheet.getRow(i + 3);
				isCreat = false;
			}

			for (int cols = 0; cols < 7; cols++) {
				XSSFCell cell = null;
				if (isCreat) {
					cell = row.createCell(cols);
				} else {
					cell = row.getCell(cols);
				}
				cell.setCellType(Cell.CELL_TYPE_STRING);// �ı���ʽ
				switch (cols) {

				case 0:
					cell.setCellValue(bean.getXUHAO());// д������
					break;
				case 1:
					cell.setCellValue(bean.getDrawingsIndex());// д������
					break;
				case 2:
					cell.setCellValue(bean.getDrawingsName());// д������
					break;
				case 3:
					cell.setCellValue(bean.getDrawingsRev());// д������
					break;
				case 4:
					cell.setCellValue(bean.getOwning_user());// д������
					break;
				case 5:
					cell.setCellValue(bean.getDate_released());// д������
					break;
				case 6:
					cell.setCellValue(bean.getWFKH());// д������
					break;

				}
			}
		}
		wb.write(output);
		output.close();
		input.close();
		System.out.println("-------WRITE EXCEL OVER-------");
	}

	/**
	 * ��ȡ������������Ԫ�������
	 * 
	 * @param wb
	 * @param cellName
	 * @return
	 * @throws IOException
	 */
	public static String getNamedCellValue(XSSFWorkbook wb, String cellName)
			throws IOException {
		String value = null;
		XSSFFormulaEvaluator evaluator = new XSSFFormulaEvaluator(wb);
		XSSFName name = wb.getName(cellName);
		AreaReference[] areaRs = AreaReference.generateContiguous(name
				.getRefersToFormula());
		if (areaRs != null)
			for (AreaReference areaR : areaRs) {
				CellReference[] cellRs = areaR.getAllReferencedCells();
				if (cellRs != null) {
					for (CellReference cellR : cellRs) {
						String sheetName = cellR.getSheetName();
						short colIndex = cellR.getCol();
						int rowIndex = cellR.getRow();
						XSSFSheet sheet = wb.getSheet(sheetName);
						XSSFRow row = sheet.getRow(rowIndex);
						XSSFCell cell = row.getCell(colIndex);
						value = getStringCellValue(evaluator, cell);
					}
				}
			}
		return value;
	}

	/***
	 * д�뵽EXCEL���� ��д�뵥�����ݣ�
	 * 
	 * @param xls_write_Address
	 * @param ls
	 * @param sheetnames
	 * @throws IOException
	 */
	public void writeExcel(String xls_write_Address, ArrayList<ArrayList> ls,
			String[] sheetnames) throws IOException {
		FileOutputStream output = new FileOutputStream(new File(
				xls_write_Address)); // ��ȡ���ļ�·��
		XSSFWorkbook wb = new XSSFWorkbook();// (new
												// BufferedInputStream(output));
		for (int sn = 0; sn < ls.size(); sn++) {
			XSSFSheet sheet = wb.createSheet(String.valueOf(sn));
			wb.setSheetName(sn, sheetnames[sn]);
			ArrayList<String[]> ls2 = ls.get(sn);
			for (int i = 0; i < ls2.size(); i++) {
				XSSFRow row = sheet.createRow(i);
				String[] s = ls2.get(i);
				for (int cols = 0; cols < s.length; cols++) {
					XSSFCell cell = row.createCell(cols);
					cell.setCellType(Cell.CELL_TYPE_STRING);// �ı���ʽ
					cell.setCellValue(s[cols]);// д������
				}
			}
		}
		wb.write(output);
		output.close();
		System.out.println("-------WRITE EXCEL OVER-------");
	}

	/**
	 * ���ݵ�Ԫ����������ҵ���Ԫ�������
	 * 
	 * @param inputFilePath
	 * @param cellName
	 * @return
	 */
	public static List<String> getExcelNamedCellValue(String inputFilePath,
			String[] cellNames) {
		List<String> valueList = new ArrayList<String>();
		try {
			FileInputStream is = new FileInputStream(new File(inputFilePath));
			XSSFWorkbook wb = new XSSFWorkbook(is);
			for (String name : cellNames)
				valueList.add(getNamedCellValue(wb, name));
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valueList;
	}

	public void insertPicture(XSSFWorkbook wb, XSSFSheet sheet1,
			String picPath, short colIndex, int rowIndex) {

		// FileOutputStream fileOut = null;
		BufferedImage bufferImg = null;
		// �ȰѶ�������ͼƬ�ŵ�һ��ByteArrayOutputStream�У��Ա����ByteArray
		try {
			if (!new File(picPath).exists()) {
				return;
			}
			String dexStr = picPath.substring(picPath.lastIndexOf(".") + 1,
					picPath.length());

			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			System.out.println(picPath);
			bufferImg = ImageIO.read(new File(picPath));
			ImageIO.write(bufferImg, dexStr, byteArrayOut);
			// XSSFWorkbook wb = new XSSFWorkbook();
			// XSSFSheet sheet1 = wb.createSheet("test picture");
			// ��ͼ�Ķ�����������һ��sheetֻ�ܻ�ȡһ����һ��Ҫע����㣩
			XSSFDrawing patriarch = sheet1.createDrawingPatriarch();
			// anchor��Ҫ��������ͼƬ������
			XSSFClientAnchor anchor = new XSSFClientAnchor(13, 13, 0, 0,
					colIndex, rowIndex, (short) (colIndex + 1), rowIndex + 1);
			anchor.setAnchorType(3);
			// ����ͼƬ
			patriarch.createPicture(anchor, wb.addPicture(
					byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_JPEG));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * �Ƴ��ϲ��ĵ�Ԫ��
	 * 
	 * @param startRow
	 * @param endRow
	 * @return
	 */
	public static boolean removeMergen(XSSFSheet sheet, int startRow, int endRow) {
		boolean isOK = false;
		int count = sheet.getNumMergedRegions();
		for (int i = 0; i < count; i++) {
			CellRangeAddress address = sheet.getMergedRegion(i);
			System.out.println(address.getFirstRow() + "|"
					+ address.getLastRow());
			if (address.getFirstRow() == startRow
					&& address.getLastRow() == endRow) {
				sheet.removeMergedRegion(i);
				isOK = true;
				break;
			}
		}
		return isOK;
	}

	/**
	 * ��ȡ�ַ���
	 * 
	 * @param inStr
	 * @return
	 */
	public static String changeStringToInt(String inStr) {
		int index = inStr.indexOf(".");
		System.out.println(index);
		String outStr = null;
		if (index != -1) {

			outStr = inStr.substring(0, index);
		} else {
			outStr = inStr;

		}
		// System.out.println(outStr);
		return outStr;
	}

	/**
	 * ��ȡһ�����ȵ��ַ���
	 * 
	 * @param inStr
	 * @param inLen
	 * @return
	 */
	public static String changeStringToInt(String inStr, int inLen) {
		int index = inStr.indexOf(".");
		inLen++;
		System.out.println(index);
		String outStr = null;
		if (index != -1 && (inStr.length() - index) >= inLen) {

			outStr = inStr.substring(0, index + inLen);
		} else {
			outStr = inStr;

		}
		// System.out.println(outStr);
		return outStr;
	}

	/**
	 * ��floatת���ɱ�����λ���ַ���
	 * 
	 * @param f
	 *            ��Ҫת����float��
	 * @return
	 */
	public static String changeFloatToStr(float f) {
		DecimalFormat decimalFormat = new DecimalFormat(".0");// ���췽�����ַ���ʽ�������С������2λ,����0����.
		String str = decimalFormat.format(f);// format ���ص����ַ���
		return str;
	}

	/**
	 * ���ַ���ת����int
	 * 
	 * @param str
	 * @return
	 */
	public static float changeStrToFloat(String str) {
		float temp_int = 0;
		try {
			temp_int = Float.parseFloat(str.trim());
			return temp_int;
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
			return -1;
		}
	}

	/**
	 * ����formual��ʱ�������滻ԭ�еĵ�Ԫ��ŵ��µĵ�Ԫ���
	 * 
	 * @param originStr
	 * @param subStr
	 * @param index
	 * @param index2
	 * @return
	 */
	public static String replaceSubString(String originStr, String subStr,
			int index, int index2) {
		StringBuffer sbO = new StringBuffer();
		sbO.append(subStr);
		sbO.append(index);
		StringBuffer sbR = new StringBuffer();
		sbR.append(subStr);
		sbR.append(index2);
		String resultStr = originStr.replace(sbO.toString(), sbR.toString());
		return resultStr;
	}

	/**
	 * ��ȡEXCEL��Ϣ
	 * 
	 * @param String
	 *            excel_file
	 * @return List<List<String>> ������Ϣ
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static List<List<String>> readExcel(String excel_file)
			throws FileNotFoundException, IOException {
		if (excel_file == null) {
			return null;
		}
		File file = new File(excel_file);
		return readExcel(file);
	}

	/**
	 * ��ȡ��Ԫ����������Ϊ�ַ������͵�����
	 * 
	 * @param cell
	 *            Excel��Ԫ��
	 * @return String ��Ԫ����������
	 */
	private static String getStringCellValue(XSSFFormulaEvaluator evaluator,
			XSSFCell cell) {
		if (cell == null) {
			return "";
		}
		String strCell = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			strCell = "" + cell.getNumericCellValue();
			if (strCell.endsWith(".0")) {
				strCell = strCell.substring(0, strCell.indexOf(".0"));
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		case Cell.CELL_TYPE_ERROR:
			strCell = String.valueOf(cell.getErrorCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			CellValue value = evaluator.evaluate(cell);
			try {
				strCell = value.getStringValue();
				if (strCell == null) {
					strCell = "" + value.getNumberValue();
					if (strCell.endsWith(".0")) {
						strCell = strCell.substring(0, strCell.indexOf(".0"));
					}
				}
			} catch (Exception e) {

				strCell = "" + value.getNumberValue();
				if (strCell.endsWith(".0")) {
					strCell = strCell.substring(0, strCell.indexOf(".0"));
				}
			}
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		return strCell;
	}

	// public static void insertRow(XSSFSheet sheet, int starRow, int rows) {
	// XSSFRow sourceRow = sheet.getRow(starRow);
	// sheet.shiftRows(starRow + 1, sheet.getLastRowNum(), rows, true, false);
	// // starRow = starRow -1;
	// for (int i = 0; i < rows; i++) {
	// // sheet.shiftRows(starRow + i, sheet.getLastRowNum(), 1, true,
	// // false);
	// XSSFRow targetRow = null;
	// XSSFCell sourceCell = null;
	// XSSFCell targetCell = null;
	// short m;
	// targetRow = sheet.createRow(starRow + 1);
	// targetRow.setHeight(sourceRow.getHeight());
	// System.out.println(sourceRow.getLastCellNum());
	// for (m = sourceRow.getFirstCellNum(); m < sourceRow
	// .getLastCellNum(); m++) {
	// System.out.println(m);
	// sourceCell = sourceRow.getCell(m);
	// targetCell = targetRow.createCell(m);
	// // System.out.println(" ===== " + sourceCell + "  " +
	// // targetCell);
	// // targetCell.setEncoding(sourceCell.get);
	// targetCell.setCellStyle(sourceCell.getCellStyle());
	// targetCell.setCellType(sourceCell.getCellType());
	// // targetCell.setCellValue("11111");
	// }
	// }
	// }

	/**
	 * ��������
	 * 
	 * @param starRow
	 * @param rows
	 */
	public static void insertRow(XSSFSheet sheet, int starRow, int rows) {
		XSSFRow sourceRow = sheet.getRow(starRow);
		sheet.shiftRows(starRow + 1, sheet.getLastRowNum(), rows, true, false);
		for (int i = 0; i < rows; i++) {
			XSSFRow targetRow = null;
			XSSFCell sourceCell = null;
			XSSFCell targetCell = null;
			short m;
			targetRow = sheet.createRow(starRow + 1);
			targetRow.setHeight(sourceRow.getHeight());
			System.out.println(sourceRow.getLastCellNum());
			for (m = sourceRow.getFirstCellNum(); m < sourceRow
					.getLastCellNum(); m++) {
				System.out.println(m);
				sourceCell = sourceRow.getCell(m);
				targetCell = targetRow.createCell(m);
				targetCell.setCellStyle(sourceCell.getCellStyle());
				targetCell.setCellType(sourceCell.getCellType());
			}
		}
	}

	/***
	 * ��sheet�в���һ��
	 * 
	 * @param sheet
	 * @param startRow
	 * @param rows
	 * @return
	 */
	public static XSSFSheet insertRows(XSSFSheet sheet, int startRow, int rows) {
		XSSFRow sourceRow = sheet.getRow(startRow);
		sheet.shiftRows(startRow + 1, sheet.getLastRowNum(), rows, true, false);
		for (int i = 0; i < rows; i++) {
			XSSFRow targetRow = null;
			XSSFCell sourceCell = null;
			XSSFCell targetCell = null;
			short m;
			targetRow = sheet.createRow(startRow + 1);
			targetRow.setHeight(sourceRow.getHeight());
			System.out.println(sourceRow.getLastCellNum());
			for (m = sourceRow.getFirstCellNum(); m < sourceRow
					.getLastCellNum(); m++) {
				System.out.println(m);
				sourceCell = sourceRow.getCell(m);
				targetCell = targetRow.createCell(m);
				targetCell.setCellStyle(sourceCell.getCellStyle());
				targetCell.setCellType(sourceCell.getCellType());
			}
		}
		return sheet;
	}

	/**
	 * ��鵥Ԫ�������Ƿ�������
	 * 
	 * @param value
	 * @return
	 */
	public static boolean CheckCellValueIsNumber(String value) {
		boolean is_ok = false;
		if (value.equals("")) /
			return false;
		}
		try {
			Float.parseFloat(value);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.out.println("===>" + value);
			is_ok = true;
		}
		return is_ok;
	}
}
