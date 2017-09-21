package com.connor.hx3.plm.hxom039;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFName;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

/**
 * Excel ǩ��
 * 
 * @author hub 2015-09-06
 */
public class ExcelUtil {
	// private static Registry reg = Registry.getRegistry(this);
	// private static HSSFSheet sheet;
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
		HSSFWorkbook workBook = new HSSFWorkbook(
				new FileInputStream(excel_file));
		HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(workBook);
		HSSFSheet sheet = workBook.getSheetAt(0);
		HSSFRow title_row = sheet.getRow(0);
		// �õ��е���Ŀ
		int col_count = title_row.getPhysicalNumberOfCells();
		System.out.println(" SHEET COL COUNT " + col_count);
		// �õ��е���Ŀ
		int row_count = sheet.getLastRowNum();

		System.out.println(" SHEET ROW COUNT " + row_count);
		for (int j = 1; j < row_count + 1; j++) {
			// �������ص��е���Ϣ
			HSSFRow row = sheet.getRow(j);
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

	public void writeSignName(String intExcelFilePath, String outExcelFilePath,
			String signName, String msgValue, int sheetIndex, int rowIndex,
			int cellIndex) throws IOException {
		// ����ļ���·��
		FileOutputStream outPut = new FileOutputStream(new File(
				outExcelFilePath));
		// �����ļ���·��
		FileInputStream inPut = new FileInputStream(new File(intExcelFilePath));
		HSSFWorkbook wb = new HSSFWorkbook(inPut);
		wb.getName("");
		AreaReference[] areaR2 = AreaReference.generateContiguous(wb
				.getName("").getRefersToFormula());
		AreaReference areaR = null;
		CellReference[] cellR = areaR.getAllReferencedCells();
		cellR[0].getSheetName();// s
		cellR[0].getCol();//
		cellR[0].getRow();//
		HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(wb);
		HSSFSheet sheet = wb.getSheetAt(sheetIndex);
		HSSFRow row = sheet.getRow(rowIndex);

		HSSFCell cell = row.getCell(cellIndex);
		String value = getStringCellValue(evaluator, cell);
		if (value.startsWith(signName)) {
			value = value.substring(0, signName.length());
		}

		cell.setCellValue(value + msgValue);
		wb.write(outPut);
		inPut.close();
		outPut.close();
	}


	/**
	 * ��ȡ������������Ԫ�������
	 * 
	 * @param wb
	 * @param cellName
	 * @return
	 * @throws IOException
	 */
	public static String getNamedCellValue(HSSFWorkbook wb, String cellName)
			throws IOException {
		String value = null;
		HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(wb);
		HSSFName name = wb.getName(cellName);
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
						HSSFSheet sheet = wb.getSheet(sheetName);
						HSSFRow row = sheet.getRow(rowIndex);
						HSSFCell cell = row.getCell(colIndex);
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
		HSSFWorkbook wb = new HSSFWorkbook();// (new
												// BufferedInputStream(output));
		for (int sn = 0; sn < ls.size(); sn++) {
			HSSFSheet sheet = wb.createSheet(String.valueOf(sn));
			wb.setSheetName(sn, sheetnames[sn]);
			ArrayList<String[]> ls2 = ls.get(sn);
			for (int i = 0; i < ls2.size(); i++) {
				HSSFRow row = sheet.createRow(i);
				String[] s = ls2.get(i);
				for (int cols = 0; cols < s.length; cols++) {
					HSSFCell cell = row.createCell(cols);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);// �ı���ʽ
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
			HSSFWorkbook wb = new HSSFWorkbook(is);
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

	
	public void insertPicture(HSSFWorkbook wb, HSSFSheet sheet1,
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
			// HSSFWorkbook wb = new HSSFWorkbook();
			// HSSFSheet sheet1 = wb.createSheet("test picture");
			// ��ͼ�Ķ�����������һ��sheetֻ�ܻ�ȡһ����һ��Ҫע����㣩
			HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
			// anchor��Ҫ��������ͼƬ������
			HSSFClientAnchor anchor = new HSSFClientAnchor(13, 13, 0, 0,
					(short) colIndex, rowIndex, (short) (colIndex + 1),
					rowIndex + 1);
			anchor.setAnchorType(3);
			// ����ͼƬ
			patriarch
					.createPicture(anchor, wb.addPicture(
							byteArrayOut.toByteArray(),
							HSSFWorkbook.PICTURE_TYPE_JPEG));

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
	public static boolean removeMergen(HSSFSheet sheet, int startRow, int endRow) {
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
	private static String getStringCellValue(HSSFFormulaEvaluator evaluator,
			HSSFCell cell) {
		if (cell == null) {
			return "";
		}
		String strCell = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			strCell = "" + cell.getNumericCellValue();
			if (strCell.endsWith(".0")) {
				strCell = strCell.substring(0, strCell.indexOf(".0"));
			}
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			strCell = String.valueOf(cell.getErrorCellValue());
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
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
	
	/**
     * ��ȡExcel�����ݣ���һά����洢����һ���и��е�ֵ����ά����洢���Ƕ��ٸ���
     * @param file ��ȡ���ݵ�ԴExcel
     * @param ignoreRows ��ȡ���ݺ��Ե�������������ͷ����Ҫ���� ���Ե�����Ϊ1
     * @return ������Excel�����ݵ�����
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String[][] getData(File file, int ignoreRows)
           throws FileNotFoundException, IOException {
       List<String[]> result = new ArrayList<String[]>();
       int rowSize = 0;
       BufferedInputStream in = new BufferedInputStream(new FileInputStream(
              file));
       // ��HSSFWorkbook
       POIFSFileSystem fs = new POIFSFileSystem(in);
       HSSFWorkbook wb = new HSSFWorkbook(fs);
       HSSFCell cell = null;
       for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
           HSSFSheet st = wb.getSheetAt(sheetIndex);
           // ��һ��Ϊ���⣬��ȡ
           for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
              HSSFRow row = st.getRow(rowIndex);
              if (row == null) {
                  continue;
              }
              int tempRowSize = row.getLastCellNum() + 1;
              if (tempRowSize > rowSize) {
                  rowSize = tempRowSize;
              }
              String[] values = new String[rowSize];
              Arrays.fill(values, "");
              boolean hasValue = false;
              for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
                  String value = "";
                  cell = row.getCell(columnIndex);
                  if (cell != null) {
                     
                     switch (cell.getCellType()) {
                     case HSSFCell.CELL_TYPE_STRING:
                         value = cell.getStringCellValue();
                         break;
                     case HSSFCell.CELL_TYPE_NUMERIC:
                         if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            Date date = cell.getDateCellValue();
                            if (date != null) {
                                value = new SimpleDateFormat("yyyy-MM-dd")
                                       .format(date);
                            } else {
                                value = "";
                            }
                         } else {
                            value = new DecimalFormat("0").format(cell
                                   .getNumericCellValue());
                         }
                         break;
                     case HSSFCell.CELL_TYPE_FORMULA:
                         // ����ʱ���Ϊ��ʽ���ɵ���������ֵ
                         if (!cell.getStringCellValue().equals("")) {
                            value = cell.getStringCellValue();
                         } else {
                            value = cell.getNumericCellValue() + "";
                         }
                         break;
                     case HSSFCell.CELL_TYPE_BLANK:
                         break;
                     case HSSFCell.CELL_TYPE_ERROR:
                         value = "";
                         break;
                     case HSSFCell.CELL_TYPE_BOOLEAN:
                         value = (cell.getBooleanCellValue() == true ? "Y"
                                : "N");
                         break;
                     default:
                         value = "";
                     }
                  }
                  if (columnIndex == 0 && value.trim().equals("")) {
                     break;
                  }
                  values[columnIndex] = rightTrim(value);
                  hasValue = true;
              }

              if (hasValue) {
                  result.add(values);
              }
           }
       }
       in.close();
       String[][] returnArray = new String[result.size()][rowSize];
       for (int i = 0; i < returnArray.length; i++) {
           returnArray[i] = (String[]) result.get(i);
       }
       return returnArray;
    }

    /**
     * ȥ���ַ����ұߵĿո�
     * @param str Ҫ������ַ���
     * @return �������ַ���
     */
     public static String rightTrim(String str) {
       if (str == null) {
           return "";
       }
       int length = str.length();
       for (int i = length - 1; i >= 0; i--) {
           if (str.charAt(i) != 0x20) {
              break;
           }
           length--;
       }
       return str.substring(0, length);
    }

	// public static void insertRow(HSSFSheet sheet, int starRow, int rows) {
	// HSSFRow sourceRow = sheet.getRow(starRow);
	// sheet.shiftRows(starRow + 1, sheet.getLastRowNum(), rows, true, false);
	// // starRow = starRow -1;
	// for (int i = 0; i < rows; i++) {
	// // sheet.shiftRows(starRow + i, sheet.getLastRowNum(), 1, true,
	// // false);
	// HSSFRow targetRow = null;
	// HSSFCell sourceCell = null;
	// HSSFCell targetCell = null;
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
	public static void insertRow(HSSFSheet sheet, int starRow, int rows) {
		HSSFRow sourceRow = sheet.getRow(starRow);
		sheet.shiftRows(starRow + 1, sheet.getLastRowNum(), rows, true, false);
		for (int i = 0; i < rows; i++) {
			HSSFRow targetRow = null;
			HSSFCell sourceCell = null;
			HSSFCell targetCell = null;
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
	public static HSSFSheet insertRows(HSSFSheet sheet, int startRow, int rows) {
		HSSFRow sourceRow = sheet.getRow(startRow);
		sheet.shiftRows(startRow + 1, sheet.getLastRowNum(), rows, true, false);
		for (int i = 0; i < rows; i++) {
			HSSFRow targetRow = null;
			HSSFCell sourceCell = null;
			HSSFCell targetCell = null;
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
		if (value.equals("")) {
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
