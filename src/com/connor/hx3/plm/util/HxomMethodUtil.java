package com.connor.hx3.plm.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.connor.hx3.plm.hxom012.BOM;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentBOMViewRevision;
import com.teamcenter.rac.kernel.TCComponentBOMWindow;
import com.teamcenter.rac.kernel.TCComponentBOMWindowType;
import com.teamcenter.rac.kernel.TCComponentContextList;
import com.teamcenter.rac.kernel.TCComponentDataset;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentQuery;
import com.teamcenter.rac.kernel.TCComponentQueryType;
import com.teamcenter.rac.kernel.TCComponentTcFile;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCPreferenceService;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCQueryClause;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.kernel.TCTypeService;
import com.teamcenter.rac.kernel.TCUserService;
import com.teamcenter.rac.util.FileUtility;
import com.teamcenter.rac.util.MessageBox;

public class HxomMethodUtil {

	public static TCPreferenceService service;
	public static TCSession session;
	public static TCUserService userservice;;

	static {
		if (session == null) {
			session = (TCSession) AIFUtility.getCurrentApplication().getSession();
		}
		if (service == null)
			service = session.getPreferenceService();
	}

	/**
	 * 获取多值得首选项
	 * 
	 * @param prefName
	 * @return
	 */
	public static String[] getPrefStrArray(String prefName) {
		String[] strs = service.getStringArray(
				TCPreferenceService.TC_preference_site, prefName);
		service.getStringValues(prefName);
		service.getStringValue(prefName);
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
			//TCTextService textService = session.getTextService();
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
			// String[] as = new String[keys.length];
			// for (int i = 0; i < keys.length; i++) {
			// as[i] = textService.getTextValue(keys[i]);
			// }

			// String[] as1 = new String[values.length];
			// for (int i = 0; i < values.length; i++) {
			// as1[i] = textService.getTextValue(values[i]);
			// }

			query.clearCache();
			// 通过查询得到查询的信息描述
			TCQueryClause[] clauses = query.describe();

			/** 遍历查询信息，检查查询条目是否正确*/
			for (int i = 0; i < clauses.length; i++) {
				// 得到查询属性名称
				System.out.println(clauses[i].getAttributeName());
				// 得到用户条目
				System.out.println(clauses[i].getUserEntryName());
				// 得到用户本地化条目
				System.out.println(clauses[i].getUserEntryNameDisplay());

			}

			//在查询构建器中会有多个条目，一个查询条目是一行
			// 参数1：用户查询条目 如：new String["零组件版本ID","ID"] 	参数2：查询条目的值  new String["0001*","*"]
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

	/***************** BOM相关 ***************/

	public static TCComponentBOMWindow getBomwindow(TCComponentItemRevision rev) {
		TCComponentBOMWindow window = null;
		try {
			TCTypeService service = session.getTypeService();
			// TCComponentBOMViewRevisionType bvr_type =
			// (TCComponentBOMViewRevisionType)
			// service.getTypeComponent("PSBOMViewRevision");

			TCComponentBOMWindowType winType = (TCComponentBOMWindowType) service
					.getTypeComponent("BOMWindow");
			window = winType.create(null);
			window.setWindowTopLine(rev.getItem(), rev, null, null);

		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return window;
	}

	/**
	 * 获取顶层的BOMLINE
	 * 
	 * @param rev
	 * @return
	 */
	public static TCComponentBOMLine getTopLineByRev(TCComponentItemRevision rev) {
		TCComponentBOMLine line = null;
		try {
			TCTypeService service = session.getTypeService();
			// TCComponentBOMViewRevisionType bvr_type =
			// (TCComponentBOMViewRevisionType)
			// service.getTypeComponent("PSBOMViewRevision");

			TCComponentBOMWindowType winType = (TCComponentBOMWindowType) service
					.getTypeComponent("BOMWindow");
			//如果不传入null，则需要传入版本规则，发送到结构管理器后作为顶层的BOMLine，后面的都是子line
			TCComponentBOMWindow window = winType.create(null);

			line = window.setWindowTopLine(rev.getItem(), rev, null, null);

		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return line;
	}
	
	
	public static void getAllBomItemByBomline(TCComponentBOMLine line,List<TCComponentItem> itemList) throws TCException{
		if(line == null){
			return;
		}
		itemList.add(line.getItem());
		AIFComponentContext[] contextS = line.getChildren();
		for(AIFComponentContext context : contextS ){
			TCComponentBOMLine child = (TCComponentBOMLine) context.getComponent();
			getAllBomItemByBomline(child,itemList);
		}	
	}
	/**
	 * 获取顶层的BOMLINE
	 * 
	 * @param rev
	 * @return
	 */
	public static List<TCComponentItem> getAllBomItemByTopRev(TCComponentItemRevision rev) {
		TCComponentBOMLine line = null;
		List<TCComponentItem> itemList = new ArrayList<>();
		try {
			TCTypeService service = session.getTypeService();
			

			TCComponentBOMWindowType winType = (TCComponentBOMWindowType) service
					.getTypeComponent("BOMWindow");
			TCComponentBOMWindow window = winType.create(null);

			line = window.setWindowTopLine(rev.getItem(), rev, null, null);
			getAllBomItemByBomline(line,itemList);
			
			window.close();
			
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return itemList;
	}


	/**
	 * 获取顶层的BOMLINE
	 * 
	 * @param rev
	 * @return
	 */
	public static BOM getTopLineByRev2(TCComponentItemRevision rev) {
		BOM bom = new BOM();
		TCComponentBOMLine line = null;
		try {
			TCTypeService service = session.getTypeService();
			// TCComponentBOMViewRevisionType bvr_type =
			// (TCComponentBOMViewRevisionType)
			// service.getTypeComponent("PSBOMViewRevision");

			TCComponentBOMWindowType winType = (TCComponentBOMWindowType) service
					.getTypeComponent("BOMWindow");
			TCComponentBOMWindow window = winType.create(null);

			line = window.setWindowTopLine(rev.getItem(), rev, null, null);
			bom.line = line;
			bom.window = window;
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bom;
	}

	/**
	 * if (window != null) { TCComponentBOMViewRevision bvr = window.askBvr();
	 * if (bvr != null) { TCComponent[] status = bvr
	 * .getReferenceListProperty("release_status_list"); if (status != null &&
	 * status.length != 0) { isReleased = true; } }
	 */
	public static TCComponentBOMLine getReleasedTopLineByRev(
			TCComponentItemRevision rev) {
		TCComponentBOMLine line = null;
		try {
			TCTypeService service = session.getTypeService();
			// TCComponentBOMViewRevisionType bvr_type =
			// (TCComponentBOMViewRevisionType)
			// service.getTypeComponent("PSBOMViewRevision");

			TCComponentBOMWindowType winType = (TCComponentBOMWindowType) service
					.getTypeComponent("BOMWindow");
			TCComponentBOMWindow window = winType.create(null);
			if (window != null) {
				TCComponentBOMViewRevision bvr = window.askBvr();
				if (bvr != null) {
					TCComponent[] status = bvr
							.getReferenceListProperty("release_status_list");
					if (status == null || status.length == 0) {
						return null;
					}
				}
			} else {
				return null;
			}
			line = window.setWindowTopLine(rev.getItem(), rev, null, null);

		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return line;
	}
	
	
	public void getAllBomlint(List<MyBomBean> bomLineList, TCComponentBOMLine line) throws TCException{
		//
		MyBomBean bean = new MyBomBean();
		bomLineList.add(bean);
		
		AIFComponentContext[] childrenContext = line.getChildren();
		if (childrenContext.length != 0) {
			for (AIFComponentContext child : childrenContext) {
			
				getAllBomlint(bomLineList, (TCComponentBOMLine) child.getComponent());
			}
		}
	}

	/**
	 * 搭建bom
	 * 
	 * @param selectIndex
	 * @throws TCException
	 */
	public static void createBom(TCComponentItemRevision parentRev,
			List<TCComponentItemRevision> childRevList,
			List<String> queryNoList, List<String> countNoList)
			throws TCException {
		if (childRevList == null || childRevList.size() == 0) {
			return;
		}
		if (parentRev == null)
			return;
		String[] setProps = { HxomStaticFinal.JF3_BOM_QUERY_NO,
				HxomStaticFinal.JF3_BOM_COUNT_NO ,HxomStaticFinal.HX_JBYL,HxomStaticFinal.HX_JCYL};
		//String[] setProps2 = { HxomStaticFinal.HX_JBYL,HxomStaticFinal.HX_JCYL};
		
		TCTypeService service = session.getTypeService();
		TCComponentBOMWindowType winType = (TCComponentBOMWindowType) service
				.getTypeComponent("BOMWindow");
		//不传入null则需要传入版本规则Latest Working
		
		
		TCComponentBOMWindow view = winType.create(null);
		//后面的两个参数是br和bvr，一般企业bom都是单视图，多视图成本高，后面两个参数传入null即可，API根据情况自动传入视图到bom中
		TCComponentBOMLine line = view.setWindowTopLine(parentRev.getItem(),
				parentRev, null, null);
		AIFComponentContext[] childrenContext = line.getChildren();

		view.lock();
		
		// 移出所有的子bomline
		if (childrenContext.length != 0) {
			for (AIFComponentContext child : childrenContext) {
				line.lock();
				line.remove("", (TCComponent) child.getComponent());
				line.save();
				line.unlock();
			}

			// return;
		}
		for (int i = 0; i < childRevList.size(); i++) {
			TCComponentItemRevision rev = childRevList.get(i);
			line.lock();
			//添加add();
			//获取子line:getchild(param);
			TCComponentBOMLine childBomLine = line.add(rev.getItem(), rev,
					null, false, "");
			line.save();
			line.unlock();
			childBomLine.lock();
			childBomLine.setProperties(setProps,
					new String[] { queryNoList.get(i), countNoList.get(i), countNoList.get(i) ,"1" });
			//if(countNoList.get(i)!=null && !countNoList.get(i).replace(" ", "").equals("")){
			//	childBomLine.setDoubleProperty(HxomStaticFinal.HX_JBYL, Double.parseDouble(countNoList.get(i)));
			//}
			//childBomLine.setDoubleProperty(HxomStaticFinal.HX_JCYL, Double.parseDouble("1"));
			
			childBomLine.save();
			childBomLine.unlock();

		}
		view.save();
		view.unlock();
		
		
		//创建出来用完关掉，否则会占用系统资源
		view.close();

	}

	/**
	 * 下载文件
	 * 
	 * @param comps
	 * @return
	 * @throws TCException
	 * @throws IOException
	 */
	public static String downLoadFile(TCComponent comp) {
		if (comp == null) {
			return "";
		}
		String value = "";
		String tempPath = System.getenv("TEMP");
		// MessageBox.post(" tempPath =
		// "+tempPath,"INFO",MessageBox.INFORMATION);
		if (tempPath == null) {
			tempPath = "";
		} else if (!tempPath.endsWith("\\")) {
			tempPath = tempPath + "\\";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(
				HxomStaticFinal.TIME_FORMAT2);
		// for(TCComponent comp : comps){
		try {
			if (comp instanceof TCComponentDataset) {
				TCComponentTcFile[] tcFiles = ((TCComponentDataset) comp)
						.getTcFiles();
				File file = null;
				if (tcFiles != null && tcFiles.length != 0) {
					file = tcFiles[0].getFmsFile();
					String fileName = file.getName();
					String fileDix = fileName.substring(
							fileName.lastIndexOf("."), fileName.length());
					fileName = tempPath + sdf.format(new Date()) + fileDix;
					File dirFile = new File(fileName);
					FileUtility.copyFile(file, dirFile);

					return fileName;
				}
			}
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// }
		return value;
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
	 * 是否有BOM
	 * 
	 * @param rev
	 * @return
	 */
	public static boolean isRevHadBom(TCComponentItemRevision rev) {
		boolean isHad = false;
		if (rev != null) {
			TCComponentBOMWindow window = null;
			try {
				TCTypeService service = session.getTypeService();
				TCComponentBOMWindowType winType = (TCComponentBOMWindowType) service
						.getTypeComponent("BOMWindow");
				window = winType.create(null);
				TCComponentBOMLine topLine = window.setWindowTopLine(
						rev.getItem(), rev, null, null);
				if (topLine != null) {
					if (topLine.getChildren().length > 0) {
						isHad = true;
					}
				}
				window.close();
			} catch (TCException e) {
				e.printStackTrace();
			}
		}
		return isHad;
	}

	/**
	 * 判断已经发布的对象是否有BOM
	 * 
	 * @param rev
	 * @return
	 */
	public static boolean isRevBomReleased(TCComponentItemRevision rev) {
		boolean isReleased = false;
		try {
			if (isCompReleased(rev)) {
				TCComponentBOMWindow window = getBomwindow(rev);
				// TCComponentBOMLine line = window.getTopBOMLine();
				// if(line.getChildren().length!=0){
				// isReleased = true;
				// }
				if (window != null) {
					TCComponentBOMViewRevision bvr = window.askBvr();
					if (bvr != null) {
						TCComponent[] status = bvr
								.getReferenceListProperty("release_status_list");
						if (status != null && status.length != 0) {
							isReleased = true;
						}
					}
					window.close();
				}
			}
		} catch (TCException e) {
			e.printStackTrace();
			isReleased = true;
		}
		return isReleased;

	}

	/**
	 * 获取所有的选择的对象的版本
	 * 
	 * @param comps
	 * @return
	 */
	public static List<TCComponentItemRevision> getAllRevComp(
			InterfaceAIFComponent comps[]) {
		List<TCComponentItemRevision> revList = new ArrayList<TCComponentItemRevision>();
		for (InterfaceAIFComponent comp : comps) {
			if (comp instanceof TCComponentItemRevision) {
				// revList.add((TCComponentItemRevision) comp);
				try {
					revList.add(((TCComponentItemRevision) comp).getItem()
							.getLatestItemRevision());
				} catch (TCException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (comp instanceof TCComponentItem) {
				try {
					revList.add(((TCComponentItem) comp)
							.getLatestItemRevision());
				} catch (TCException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				return null;
			}
		}
		return revList;
	}

	/**
	 * 获取所有的选择的对象的版本
	 * 
	 * @param comps
	 * @return
	 */
	public static List<TCComponentItemRevision> getAllRevComp2(
			InterfaceAIFComponent comps[]) {
		List<TCComponentItemRevision> revList = new ArrayList<TCComponentItemRevision>();
		for (InterfaceAIFComponent comp : comps) {
			if (comp instanceof TCComponentItemRevision) {
				// revList.add((TCComponentItemRevision) comp);
				try {
					// ((TCComponentItemRevision) comp).getItem().get
					revList.add(((TCComponentItemRevision) comp).getItem()
							.getLatestItemRevision());
				} catch (TCException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (comp instanceof TCComponentItem) {
				try {
					revList.add(((TCComponentItem) comp)
							.getLatestItemRevision());
				} catch (TCException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				return null;
			}
		}
		return revList;
	}

	/**
	 * 改变数据集的命名引用
	 * 
	 * @param tccomponentDataset
	 * @param quote
	 * @param type
	 * @param path
	 * @throws TCException
	 */
	public static void changeDataSet(TCComponentDataset tccomponentDataset,
			String quote, String type, String path) throws TCException {
		String myPath[] = { path };
		String myQuote[] = { quote };// 引用"excel"
		String myType[] = { type };// 类型"MSExcelX"
		String myPlain[] = { "Plain" };

		// 删除数据集的引用
		deleDateSetRef(tccomponentDataset);
		// 数据集的替换
		tccomponentDataset.setFiles(myPath, myType, myPlain, myQuote);

	}

	/**
	 * 删除数据集的命名引用
	 * 
	 * @param dataset
	 * @throws TCException
	 */
	private static void deleDateSetRef(TCComponentDataset dataset)
			throws TCException {

		TCComponentTcFile[] tcFiles = dataset.getTcFiles();
		for (int i = 0; i < tcFiles.length; i++) {
			// 得到数据集的引用类型
			String str_temp = getNamedRefType(dataset, tcFiles[i]);
			// 删除引用
			dataset.removeNamedReference(str_temp);
		}

	}

	/**
	 * 得到数据集的引用类型
	 * 
	 * @param datasetComponent
	 * @param tccomponent
	 * @return
	 * @throws TCException
	 */
	private static String getNamedRefType(TCComponentDataset datasetComponent,
			TCComponentTcFile tccomponent) throws TCException {
		String s;
		s = "";
		TCProperty tcproperty;
		TCProperty tcproperty1;
		TCComponent atccomponent[];
		String as[];
		int i;
		int j;
		int k;
		try {
			tcproperty = datasetComponent.getTCProperty("ref_list");
			tcproperty1 = datasetComponent.getTCProperty("ref_names");
			if (tcproperty == null || tcproperty1 == null)
				return s;
		} catch (TCException tcexception) {
			return s;
		}
		// ref_list:123.xlsx
		// datasetComponent.getRelatedComponents("ref_list")
		atccomponent = tcproperty.getReferenceValueArray();
		// 得到当前ref_names:excel
		as = tcproperty1.getStringValueArray();
		if (atccomponent == null || as == null)
			return s;
		i = atccomponent.length;
		if (i != as.length)
			return s;
		j = -1;
		k = 0;
		do {
			if (k >= i)
				break;
			if (tccomponent == atccomponent[k]) {
				j = k;
				break;
			}
			k++;
		} while (true);
		if (j != -1)
			s = as[j];
		return s;
	}

	/**
	 * 
	 * @param val
	 * @throws TCException
	 */
	public static void setByPass(boolean val) throws TCException {
		if (userservice == null) {
			userservice = session.getUserService();
		}
		Object[] obj = new Object[1];
		obj[0] = "origin";
		if (val) {
			userservice.call("ORIGIN_set_bypass", obj);
		} else {
			userservice.call("ORIGIN_close_bypass", obj);
		}
	}
}
