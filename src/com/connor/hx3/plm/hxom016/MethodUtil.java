package com.connor.hx3.plm.hxom016;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentContextList;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentQuery;
import com.teamcenter.rac.kernel.TCComponentQueryType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCPreferenceService;
import com.teamcenter.rac.kernel.TCQueryClause;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.kernel.TCTextService;
import com.teamcenter.rac.util.MessageBox;

public class MethodUtil {

	private static TCPreferenceService service;
	private static TCSession session;

	static {
		if (session == null) {
			session = (TCSession) (AIFUtility.getCurrentApplication()
					.getSession());
		}
		if (service == null)
			service = session.getPreferenceService();
	}

	// public void getOption(){
	// TCPreferenceService service = ((TCSession)AIFUtility
	// .getCurrentApplication().getSession()).getPreferenceService();
	// String [] ColVal = service.getStringArray
	// (TCPreferenceService.TC_preference_site, "HXAPQPmapping");
	// }

	/**
	 * 获取多值得首选项
	 * 
	 * @param prefName
	 * @return
	 */
	public static String[] getPrefStrArray(String prefName) {
		String[] strs = service.getStringArray(
				TCPreferenceService.TC_preference_site, prefName);
		if (strs == null) {
			strs = new String[] { "" };
		}
		return strs;
	}

	public static TCComponentBOMLine getTopline(TCComponentItemRevision rev) {
		TCComponentBOMLine topLine = null;

		return topLine;
	}

	public static List<Integer> getPrefIntArray(String prefName) {
		List<Integer> intList = new ArrayList<>();
		String[] strs = service.getStringArray(
				TCPreferenceService.TC_preference_site, prefName);
		if (strs != null) {
			for (String str : strs) {
				try {
					int index = Integer.parseInt(str.trim());
					intList.add(index);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
		}

		return intList;
	}

	public static HashMap<String, String> getPrefStrArrayReturn(
			String prefName, String split) {
		HashMap<String, String> map = new HashMap<String, String>();
		// map.put("", " ");
		String[] strs = service.getStringArray(
				TCPreferenceService.TC_preference_site, prefName);
		if (strs != null) {
			for (String str : strs) {
				String temp[] = str.split(split);
				map.put(temp[1], temp[0]);
			}
		}
		return map;
	}

	public static HashMap<String, String> getPrefStrArray(String prefName,
			String split) {
		HashMap<String, String> map = new HashMap<String, String>();

		// map.put("", " ");

		String[] strs = service.getStringArray(
				TCPreferenceService.TC_preference_site, prefName);
		if (strs != null) {
			for (String str : strs) {
				String temp[] = str.split(split);
				map.put(temp[0], temp[1]);
			}
		}
		return map;
	}

	/**
	 * 获取单值得首选项
	 * 
	 * @param prefName
	 * @return
	 */
	public static String getPrefStr(String prefName) {
		String str = service.getString(TCPreferenceService.TC_preference_site,
				prefName);
		if (str == null) {
			str = new String("");
		}
		return str;
	}

	/**
	 * 通过查询,查找符合条件的版本
	 * 
	 * @param session
	 * @param searchName
	 * @param keys
	 * @param values
	 * @return
	 */
	public static InterfaceAIFComponent[] searchComponentsCollection(
			TCSession session, String searchName, String[] keys, String[] values) {
		// 信息输出
		InterfaceAIFComponent[] result = new InterfaceAIFComponent[0];

		try {
			// 得到查询的服务
			TCTextService textService = session.getTextService();
			// 获取querytype
			// 参数固定写法ImanQuery
			TCComponentQueryType querytype = (TCComponentQueryType) session
					.getTypeComponent("ImanQuery");
			// 通过querytype找到searchName的查询
			TCComponentQuery query = (TCComponentQuery) querytype
					.find(searchName);
			if (query == null) {
				MessageBox.post("通过查询构建器" + searchName + "不存在", "错误", 1);
				return null;
			}
			querytype.clearCache();
			String[] as = new String[keys.length];
			for (int i = 0; i < keys.length; i++) {
				as[i] = textService.getTextValue(keys[i]);
			}

			String[] as1 = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				as1[i] = textService.getTextValue(values[i]);
			}

			query.clearCache();
			// 通过查询得到查询的信息描述
			TCQueryClause[] clauses = query.describe();

			// 遍历查询信息，检查查询条目是否正确
			for (int i = 0; i < clauses.length; i++) {
				// 得到查询属性名称
				clauses[i].getAttributeName();
				// 得到用户条目
				clauses[i].getUserEntryName();
				// 得到用户本地化条目
				clauses[i].getUserEntryNameDisplay();

				clauses[i].getAttributeType();

			}
			TCComponentContextList list = query.getExecuteResultsList(keys,
					values);
			if (list != null) {
				int count = list.getListCount();
				result = new InterfaceAIFComponent[count];

				for (int i = 0; i < count; i++) {
					result[i] = list.get(i).getComponent();
				}
			}
		} catch (TCException e) {
			e.printStackTrace();
			MessageBox.post("通过查询构建器" + searchName + "查询发生错误.", "错误", 1);
		}

		return result;
	}

	/**
	 * 检查对象是否已经发布
	 * 
	 * @param comp
	 * @return
	 * @throws TCException
	 */
	public static boolean isCompReleased(TCComponent comp) throws TCException {
		TCComponent[] comps = comp.getRelatedComponents("release_status_list");
		if (comps != null && comps.length > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 获取所有的选择的对象的版本
	 * 
	 * @param comps
	 * @return
	 * @throws TCException
	 */
	public static List<TCComponentItemRevision> getAllRevComp(
			InterfaceAIFComponent comps[]) throws TCException {
		List<TCComponentItemRevision> revList = new ArrayList<TCComponentItemRevision>();
		for (InterfaceAIFComponent comp : comps) {
			if (comp instanceof TCComponentItemRevision) {
				// revList.add((TCComponentItemRevision) comp);
				revList.add(((TCComponentItemRevision) comp).getItem()
						.getLatestItemRevision());
			} else {
				return null;
			}
		}
		return revList;
	}

	/**
	 * 查找最新版本
	 * 
	 * @param revList
	 */
	private boolean checkRevIsLast(List<TCComponentItemRevision> revList) {
		boolean isOk = true;
		for (TCComponentItemRevision rev : revList) {
			try {
				TCComponentItemRevision rev2 = rev.getItem()
						.getLatestItemRevision();
				if (!rev2.getUid().equals(rev.getUid())) {
					return false;
				}
			} catch (TCException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return isOk;
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
}
