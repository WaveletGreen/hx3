package com.connor.hx3.plm.hxom015;

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

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil07 {
	public static List<String> sortIndex(Map<String, ModelBean> modelTypeMap) {

		List<String> keyList = new ArrayList<>();

		Set<Entry<String, ModelBean>> entrySet = modelTypeMap.entrySet();

		List<Integer> indexList = new ArrayList<>();

		for (Entry<String, ModelBean> entry : entrySet) {
			ModelBean bean = entry.getValue();

			indexList.add(bean.getRowNumber());

		}

		Integer[] integS = indexList.toArray(new Integer[indexList.size()]);
		Arrays.sort(integS);

		for (Integer integ : integS) {
			entrySet = modelTypeMap.entrySet();
			for (Entry<String, ModelBean> entry : entrySet) {
				ModelBean bean = entry.getValue();

				if (bean.getRowNumber() == integ) {

					keyList.add(entry.getKey());
				}

			}

		}
		return keyList;
	}

	public static void main(String[] args) {
		try {
			InputStream is = new FileInputStream(new File(
					"F:\\Connor（项目管理）\\宏协\\李琪\\OTS送样文件交付清单1.xlsx"));
			Map<String, ModelBean> modelBeanMap = new HashMap<>();
			readModel(is, modelBeanMap);
			Set<Entry<String, ModelBean>> setEntry = modelBeanMap.entrySet();
			for (Entry<String, ModelBean> entry : setEntry) {
				System.out.println("key =" + entry.getKey() + " | "
						+ entry.getValue());

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 读取模板
	 * 
	 * @param is
	 * @param modelBeanMap
	 * @throws IOException
	 */
	public static void readModel(InputStream is,
			Map<String, ModelBean> modelBeanMap) throws IOException {
		if (modelBeanMap == null) {
			modelBeanMap = new HashMap<>();
		}
		XSSFWorkbook workBook = new XSSFWorkbook(is);
		XSSFFormulaEvaluator evaluator = new XSSFFormulaEvaluator(workBook);
		XSSFSheet sheet = workBook.getSheetAt(0);
		XSSFCell startCell = getNamedCellValue1(workBook, "开始行");

		int row_count = sheet.getLastRowNum();// 获取最后一排数
		ModelBean bean = new ModelBean();
		List<String> modelTypeMap = new ArrayList<>();
		String jfjd = "0";// 交付节点
		String wjlx = "0";// 文件类型
		String jfjdT = "";//
		Integer indexT = startCell.getRowIndex() + 1;// 得到"开始行"的行数
		for (int j = startCell.getRowIndex(); j < row_count + 1; j++) {

			XSSFRow row = sheet.getRow(j);

			if (row == null) {
				bean.setRowNumber(indexT);// set行数
				bean.setModelTypeMap(modelTypeMap);//
				modelBeanMap.put(jfjdT, bean);//
				return;

			}
			jfjd = getStringCellValue(evaluator, row.getCell((short) 1));
			wjlx = getStringCellValue(evaluator, row.getCell((short) 2));

			if (!jfjd.trim().isEmpty()) {// "交付节点"去除左右空格不为空
				if (j != startCell.getRowIndex()) {// j不等于"开始行"的行数
					// jfjdIndex = 0;
					bean.setRowNumber(indexT);// set行数
					bean.setModelTypeMap(modelTypeMap);//
					modelBeanMap.put(jfjdT, bean);//

					bean = new ModelBean();
					modelTypeMap = new ArrayList<>();
				}
				jfjdT = jfjd;
				indexT = j;
			}
			modelTypeMap.add(wjlx);
			if (jfjd.trim().isEmpty() && wjlx.trim().isEmpty()) {
				bean.setRowNumber(indexT);
				bean.setModelTypeMap(modelTypeMap);
				modelBeanMap.put(jfjdT, bean);
				return;
			}
			if (j == row_count) {
				bean.setRowNumber(indexT);
				bean.setModelTypeMap(modelTypeMap);
				modelBeanMap.put(jfjdT, bean);
			}
		}
	}

	/**
	 * 写入数据到excel
	 * 
	 * @param jfjdNameMap
	 * @param is
	 * @param outPutFile
	 * @param modelBeanMap
	 * @throws IOException
	 */
	public static void writeExcel(
			Map<String, Map<String, List<PsdBean>>> jfjdNameMap,
			InputStream is, String outPutFile,
			Map<String, ModelBean> modelBeanMap, String projectNameString)
			throws IOException {

		FileOutputStream output = new FileOutputStream(new File(outPutFile)); //
		XSSFWorkbook wb = new XSSFWorkbook(is);//
		XSSFFormulaEvaluator evaluator = new XSSFFormulaEvaluator(wb);
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(HSSFColor.RED.index);
		// XSSFCellStyle cellStyle2 = wb.createCellStyle();
		// cellStyle2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// cellStyle2.setFillForegroundColor(HSSFColor.t.index);
		XSSFCell xmmcCell = getNamedCellValue1(wb, "项目名称");
		if (xmmcCell != null) {
			xmmcCell.setCellValue(projectNameString);

		}

		XSSFCell rqCell = getNamedCellValue1(wb, "日期");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (rqCell != null) {
			rqCell.setCellValue(sdf.format(new Date()));
		}
		XSSFCell startCell = getNamedCellValue1(wb, "开始行");
		int addRowNum = 0;
		Set<Entry<String, ModelBean>> entrySet = modelBeanMap.entrySet();
		List<String> tList = sortIndex(modelBeanMap);// sort排序
		for (String key : tList) {// 循环map的keylist

			ModelBean bean = modelBeanMap.get(key);
			System.out.println(" JFJD =" + key + " Type = " + bean.toString());

			// 交付节点判断
			if (jfjdNameMap.containsKey(key)) {
				System.out.println(" JFJD =" + key + " 存在");

				// 获取TC候总交付节点的内容
				Map<String, List<PsdBean>> listMap = jfjdNameMap.get(key);

				// 交付节点所在的行数
				Integer index = bean.getRowNumber();

				// 获取类型的匹配
				List<String> typeList = bean.getModelTypeMap();
				for (String type : typeList) {
					Integer typeIndex = typeList.indexOf(type);
					if (listMap.containsKey(type)) {
						List<PsdBean> psdBeanList = listMap.get(type);

						System.out.println("psdBeanList--->>>"
								+ psdBeanList.size());

						if (startCell.getRowIndex() == (index + addRowNum)) {
							continue;

						}
						XSSFRow row2 = sheet.getRow(index + addRowNum);
						XSSFCell SFTJCell = row2.getCell(10);
						XSSFCell emptyCell = row2.getCell(2);
						if (getStringCellValue(evaluator, row2.getCell(0))
								.equals("序号")) {
							continue;

						}

						String emptyStr = emptyCell.getStringCellValue();
						if (tList.size() != 0 && !emptyStr.trim().isEmpty()) {
							if (psdBeanList.size() != 0) {
								SFTJCell.setCellStyle(emptyCell.getCellStyle());
								SFTJCell.setCellValue("是");
								System.out.println(index + addRowNum + ">>>>"
										+ "是");
							} else {
								SFTJCell.setCellStyle(cellStyle);
								SFTJCell.setCellValue("否");
								System.out.println(index + addRowNum + ">>>>"
										+ "否");
							}
						} else if (!emptyStr.trim().isEmpty()) {
							SFTJCell.setCellStyle(cellStyle);
							SFTJCell.setCellValue("否");
							System.out
									.println(index + addRowNum + ">>>>" + "否");
						}

						for (PsdBean psdBean : psdBeanList) {
							insertRows(sheet, index + addRowNum + typeIndex, 1);

							XSSFRow row = sheet.getRow(index + addRowNum
									+ typeIndex + 1);

							XSSFCell objectNameCell = row.getCell(4);
							objectNameCell.setCellValue(psdBean
									.getObject_name());
							XSSFCell itemIDCell = row.getCell(5);
							itemIDCell.setCellValue(psdBean.getItem_id());
							XSSFCell owningUserCell = row.getCell(7);
							owningUserCell.setCellValue(psdBean
									.getOwning_user());

							if (psdBean.getDate_released() != null
									&& !psdBean.getDate_released().equals(
											"没有发布")) {
								XSSFCell dateReleasedCell = row.getCell(8);
								dateReleasedCell.setCellStyle(emptyCell
										.getCellStyle());
								dateReleasedCell.setCellValue(psdBean
										.getDate_released());

							} else {
								XSSFCell dateReleasedCell = row.getCell(8);
								dateReleasedCell.setCellStyle(cellStyle);
								dateReleasedCell.setCellValue(psdBean
										.getDate_released());

							}

							XSSFCell revision_idCell = row.getCell(9);
							revision_idCell.setCellValue(psdBean
									.getRevision_id());

							addRowNum++;
						}
					} else {

						XSSFRow row2 = sheet.getRow(index + addRowNum
								+ typeIndex);
						XSSFCell SFTJCell = row2.getCell(10);
						XSSFCell emptyCell = row2.getCell(2);
						String emptyStr = emptyCell.getStringCellValue();
						if (!emptyStr.trim().isEmpty()) {
							SFTJCell.setCellStyle(cellStyle);
							SFTJCell.setCellValue("否");
						}

					}
				}
			} else {
				// 不包含的时候
				Integer index = bean.getRowNumber();
				List<String> typeList = bean.getModelTypeMap();
				for (String type : typeList) {

					Integer typeIndex = typeList.indexOf(type);
					XSSFRow row2 = sheet.getRow(index + addRowNum + typeIndex);
					if (getStringCellValue(evaluator, row2.getCell(0)).equals(
							"序号")) {
						continue;

					}
					XSSFCell SFTJCell = row2.getCell(10);
					XSSFCell emptyCell = row2.getCell(2);
					String emptyStr = emptyCell.getStringCellValue();
					if (!emptyStr.trim().isEmpty()) {
						SFTJCell.setCellStyle(cellStyle);
						SFTJCell.setCellValue("否");
					}
				}

			}
		}
		wb.write(output);
		output.close();
		is.close();
	}

	/**
	 * 获取单个的命名单元格的内容
	 * 
	 * @param wb
	 * @param cellName
	 * @return
	 * @throws IOException
	 */
	public static XSSFCell getNamedCellValue1(XSSFWorkbook wb, String cellName)
			throws IOException {
		XSSFCell value = null;
		XSSFFormulaEvaluator evaluator = new XSSFFormulaEvaluator(wb);
		XSSFName name = wb.getName(cellName);
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
						XSSFSheet sheet = wb.getSheet(sheetName);
						XSSFRow row = sheet.getRow(rowIndex);
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

}
