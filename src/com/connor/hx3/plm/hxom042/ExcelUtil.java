package com.connor.hx3.plm.hxom042;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFName;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;

public class ExcelUtil {
	

	

	
		//HSSFCell startCell = getNamedCellValue1(workBook, "开始行");
	
	public static void writeClassMsgExcel(String inPath,String outPath,ClassReportBean bean){
		try {
			InputStream in = new FileInputStream(new File(inPath));
			HSSFWorkbook workBook = new HSSFWorkbook(in);
			HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(workBook);
			HSSFSheet sheet = workBook.getSheet(bean.sheetName);
			int rowNum = sheet.getLastRowNum();
			HSSFCell startCell = getNamedCellValue1(workBook, "开始行"+bean.sheetName);
			System.out.println(bean.sheetName);
			int index = startCell.getRowIndex();
			System.out.println("-----开始行="+index);
			for(int i = index;i <= rowNum ;i ++){
				HSSFRow row = sheet.getRow(i);
				if(row == null)
					continue;
				int cellNum = row.getLastCellNum();
				for(int j = 0 ;j <= cellNum;j++){
					if(row.getCell(j) == null)
						continue;
					row.getCell(j).setCellValue("");
				}
			}
			
			
			
			List<List<ReportBean>> reportBeanList = bean.reportBeanList;
			
			int i = 1;
			for(List<ReportBean> beanList : reportBeanList){
				HSSFRow row = sheet.getRow(index);
				if(row == null){
					row = sheet.createRow(index);
					//row.createCell(0);
				}
				if(row.getCell(0) == null){
					row.createCell(0);
				}
				row.getCell(0).setCellValue(i+"");
				System.out.println(beanList.size());
				for(ReportBean rb : beanList){
					HSSFCell valueCell = row.getCell(getColumIndex(rb.NO));
					if(valueCell == null){
						valueCell = row.createCell(getColumIndex(rb.NO));
					}
					System.out.println(rb.NO +"|"+rb.value );
					valueCell.setCellValue(rb.value);
				}
				
				i++;
				index ++;
			}
			
			
			
			
			HSSFRow row = sheet.getRow(index);
			if(row == null){
				row = sheet.createRow(index);
				//row.createCell(0);
			}
			if(row.getCell(0) == null){
				row.createCell(0);
			}
			row.getCell(0).setCellValue(i+"");
			
			
			FileOutputStream os = new FileOutputStream(new File(outPath));
			workBook.write(os);
			
			os.close();
			in.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

		
	
	public static int getColumIndex(String columName){
		int index = 0;
		if(columName.length()==1){
			index = columName.charAt(0) - 'A';
			
		}else if(columName.length()==2){
			index = (columName.charAt(0) - 'A'+1)*26 + (columName.charAt(1) - 'A');
			
		}
		
		return index;
	}
	
	/**
	 * 获取单个的命名单元格的内容
	 * 
	 * @param wb
	 * @param cellName
	 * @return
	 * @throws IOException
	 */
	public static HSSFCell getNamedCellValue1(HSSFWorkbook wb, String cellName)
			throws IOException {
		HSSFCell value = null;
		HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(wb);
		HSSFName name = wb.getName(cellName);
		if (name == null) {
			return null;
		}
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
						value = row.getCell(colIndex);
						// value = getStringCellValue(evaluator, cell);
					}
				}
			}
		return value;
	}

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	private static String getStringCellValue(HSSFFormulaEvaluator evaluator,
			HSSFCell cell) {
		if (cell == null) {
			return "";
		}
		String strCell = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			// strCell = cell.getStringCellValue();
			if (DateUtil.isCellDateFormatted(cell)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				Date date = cell.getDateCellValue();
				if (date != null)
					strCell = sdf.format(date);
			} else {

				strCell = "" + cell.getNumericCellValue();
				if (strCell.endsWith(".0")) {
					strCell = strCell.substring(0, strCell.indexOf(".0"));
				}
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
		System.out.println(" === Type =" + cell.getCellType() + " Value ="
				+ strCell);
		return strCell;
	}

	/***
	 * 向sheet中插入一行
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

}
