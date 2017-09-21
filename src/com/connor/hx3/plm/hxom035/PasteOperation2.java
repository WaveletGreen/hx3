package com.connor.hx3.plm.hxom035;
//package com.connor.hx3.plm.hxom035;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import com.teamcenter.rac.aif.AbstractAIFOperation;
//import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
//import com.teamcenter.rac.kernel.TCComponent;
//import com.teamcenter.rac.kernel.TCComponentDataset;
//import com.teamcenter.rac.kernel.TCComponentItem;
//import com.teamcenter.rac.kernel.TCComponentItemRevision;
//import com.teamcenter.rac.kernel.TCComponentTcFile;
//import com.teamcenter.rac.kernel.TCComponentType;
//import com.teamcenter.rac.kernel.TCException;
//import com.teamcenter.rac.kernel.TCProperty;
//import com.teamcenter.rac.util.FileUtility;
//
//public class PasteOperation extends AbstractAIFOperation
//{
//
//	private TCComponentDataset excelDataset;
//	private TCComponent parentComp;
//	private InterfaceAIFComponent[] childrenComps;
//	private String contextString;
//	@Override
//	public void executeOperation() throws Exception {
//		// TODO Auto-generated method stub
//		checkChildren(parentComp, childrenComps, contextString);
//	}
//	/**
//	 *  下载文件
//	 * @param comps
//	 * @return
//	 * @throws TCException
//	 * @throws IOException
//	 */
//	public String downLoadFile(TCComponent[] comps,String compType) throws TCException, IOException{
//		if(comps == null){
//			return "";
//		}
//		String value = "";
//		String tempPath = System.getenv("TEMP");
//		//MessageBox.post(" tempPath = "+tempPath,"INFO",MessageBox.INFORMATION);
//		if(tempPath == null){
//			tempPath = "";
//		}else if(!tempPath.endsWith("\\")){
//			tempPath = tempPath+"\\";
//		}
//		SimpleDateFormat sdf = new SimpleDateFormat(JFomUtil.TIME_FORMAT2);
//		for(TCComponent comp : comps){
//			if(comp instanceof TCComponentDataset){
//				if(!comp.getType().equals(compType)){
//					continue;
//				}
//				TCComponentTcFile[] tcFiles = ((TCComponentDataset)comp).getTcFiles();
//				File file = null;
//				if (tcFiles != null) {
//					file = tcFiles[0].getFmsFile();
//					String fileName = file.getName();
//					String fileDix = fileName.substring(fileName.lastIndexOf("."),fileName.length());
//					fileName = tempPath + sdf.format(new Date()) + fileDix;
//					File dirFile = new File(fileName);
//					FileUtility.copyFile(file, dirFile);
//					if(compType.equals(JFomUtil.JF3_DATASET_TYPE)){
//						this.excelDataset = (TCComponentDataset) comp;
//					}
//					return fileName;
//				} 
//			}
//		}
//		return value;
//	}
//	/**
//	 * 获取属性
//	 * @param prop
//	 * @param isEmpty
//	 * @return
//	 */
//	public String getTcproptertyToStr(TCProperty prop,String compType) {
//		if (prop == null) {
//			return "";
//		}
//		String tempPath = System.getenv("TEMP");
//		if(tempPath == null){
//			tempPath = "";
//		}else if(!tempPath.endsWith("\\")){
//			tempPath = tempPath+"\\";
//		}
//		String propValue = new String();
//		int propType = prop.getPropertyType();
//		switch (propType) {
//		case TCProperty.PROP_untyped_relation:
//		    TCComponent[] comps = prop.getReferenceValueArray();
//		    try {
//		    	propValue = downLoadFile(comps,compType);
//			} catch (TCException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			break;
//		case TCProperty.PROP_int:
//			propValue = ""+prop.getIntValue();
//			break;
//		case TCProperty.PROP_string:
//			propValue = prop.getStringValue();
//			break;
//		case TCProperty.PROP_date:
//		
//			SimpleDateFormat sdf = new SimpleDateFormat(JFomUtil.TIME_FORMAT);
//			Date date = prop.getDateValue();
//			if(date!=null)
//				propValue = sdf.format(date);
//			else
//				propValue = "";
//			break;
//
//		}
//
//		return propValue;
//	}
//	/**
//	 * 添加对粘贴操作的报表功能
//	 * 
//	 * @param parentComp
//	 * @param childrenComps
//	 * @param grmStr
//	 */
//	public void checkChildren(TCComponent parentComp, InterfaceAIFComponent[] childrenComps, String grmStr) {
//		if (parentComp != null && childrenComps != null) {
//			if (parentComp.getType().equals(JFomUtil.JF3_PASTE_PARENT_TYPE) && grmStr.equals(JFomUtil.JF3_PASTE_GRM)) {
//				List<PasteBean> beanListBef = null;
//				String excelPath =null;
//				try {
//					TCProperty parentProp = parentComp.getTCProperty(JFomUtil.JF3_PASTE_EXCEL_GRM);
//					excelPath = getTcproptertyToStr(parentProp,JFomUtil.JF3_DATASET_TYPE);
//					beanListBef = getProductInfo(excelPath);
//				} catch (TCException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				List<TCComponentItemRevision> revList = new ArrayList<TCComponentItemRevision>();
//				for (int i = 0; i < childrenComps.length; i++) {
//					if (childrenComps[i] instanceof TCComponentItemRevision) {
//						revList.add((TCComponentItemRevision) childrenComps[i]);
//					}
//				}
//				if (revList.size() != 0) {
//					try {
//						List<PasteBean> beanListAft = new ArrayList<PasteBean>();
//						TCProperty[][] propValues = TCComponentType.getTCPropertiesSet(revList,
//								JFomUtil.JF3_PASTE_PROPS);
//						for (int i = 0; i < propValues.length; i++) {
//							// TODO 添加逻辑
//							//JFomPasteBean bean = 
//							String picPath = getTcproptertyToStr(propValues[i][0],JFomUtil.JF3_DATASET_TYPE2);
//							String nameStr = getTcproptertyToStr(propValues[i][1],"");
//							String itemIDStr = getTcproptertyToStr(propValues[i][2],"");
//							String revIDStr = getTcproptertyToStr(propValues[i][3],"");
//							String dateStr = getTcproptertyToStr(propValues[i][4],"");
//							PasteBean bean = new PasteBean("", itemIDStr,nameStr, picPath, itemIDStr+"/"+nameStr, revIDStr, dateStr);
//							beanListAft.add(bean);
//						}
//						List<PasteBean> beanListAll = addNewBean(beanListBef,beanListAft);
//						writeAllBeanExcel(beanListAll,excelPath);
//					} catch (TCException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}else if(parentComp.getType().equals(JFomUtil.JF3_PASTE_PARENT_TYPE2) && grmStr.equals(JFomUtil.JF3_PASTE_GRM2)){
//				for(int u = 0 ; u <childrenComps.length;u++){
//					if(childrenComps[u].getType().equals(JFomUtil.JF3_PASTE_PARENT_TYPE3)){
//						//TODO
//						try {
//							TCComponent[] comps1 = parentComp.getReferenceListProperty(JFomUtil.JF3_PASTE_GRM3);
//							TCComponentItemRevision rev =((TCComponentItem)childrenComps[u]).getLatestItemRevision();
//							if(comps1!=null && comps1.length!=0){
//								rev.add(JFomUtil.JF3_BGQduixiang, comps1);
//							}
//						} catch (TCException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//		}
//	}
//	/**
//	 * 将新添加的对象加入到粘贴前的缓存
//	 * 
//	 * @param beanListBef
//	 * @param beanListAft
//	 * @return
//	 */
//	public List<PasteBean> addNewBean(List<PasteBean> beanListBef, List<PasteBean> beanListAft) {
//		//MessageBox.post(beanListBef.size()+" | "+beanListAft.size(),"INFO",MessageBox.INFORMATION);
//		List<String> befRecord = new ArrayList<String>();
//		List<String> aftRecord = new ArrayList<String>();
//		for(int i = 0;i < beanListBef.size();i++){
//			String record = beanListBef.get(i).getItemID()+"/"+beanListBef.get(i).getItemRevisionId();
//			befRecord.add(record);
//		}
//		for(int i = 0;i < beanListAft.size();i++){
//			String record = beanListAft.get(i).getItemID()+"/"+beanListAft.get(i).getItemRevisionId();
//			aftRecord.add(record);
//		}
//		for(int i = 0 ;i <aftRecord.size();i++ ){
//			if(befRecord.contains(aftRecord.get(i))){
//				System.out.println("该物料["+aftRecord.get(i)+"]已经存在，不需要再添加");
//			}else{
//				PasteBean bean = beanListAft.get(i);
//				int index = beanListBef.size()+1;
//				bean.setIndexStr(index+"");
//				beanListBef.add(bean);
//			}
//		}
//		return beanListBef;
//	}
//	/**
//	 * 获取原有的EXCEL中的信息
//	 * 
//	 * @param excelPath
//	 * @return
//	 */
//	public List<PasteBean> getProductInfo(String excelPath) {
//		ExcelUtil excelUtil03 = new ExcelUtil();
//		List<PasteBean> pasteBefMsgBeans = new ArrayList<PasteBean>();
//		try {
//			List<List<String>> msgStrList = excelUtil03.readExcel(excelPath);
//			if (msgStrList == null) {
//				return pasteBefMsgBeans;
//			}
//			for (int i = JFomUtil.JF3_PASTE_EXCEL_READ_INDEX; i < msgStrList.size(); i++) {
//				List<String> msgList = msgStrList.get(i);
//				if (msgList.get(1).trim().isEmpty())
//					continue;
//				String itemID= "";
//				if(msgList.get(3).contains("/"))
//					itemID = msgList.get(3).split("/")[0];
//				else
//					itemID = msgList.get(3);
//				PasteBean bean = new PasteBean(msgList.get(0),itemID, msgList.get(1), msgList.get(2), msgList.get(3),
//						msgList.get(4), msgList.get(6));
//				pasteBefMsgBeans.add(bean);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return pasteBefMsgBeans;
//	}
//
//	/**
//	 * 将粘贴后的所有的对象写入到excel中
//	 * @param beanListAll
//	 * @param excelPath
//	 */
//	public void writeAllBeanExcel(List<PasteBean> beanListAll,String excelPath){
//		//excelDataset
//		
//		String outExcelPath = null;
//		if(excelPath.endsWith(".xls")){
//			outExcelPath = excelPath.replace(".xls", "_1.xls");
//			ExcelUtil util = new ExcelUtil();
//			try {
//				util.writeExcel(excelPath, outExcelPath, beanListAll, 3);
//				changeDataSet(excelDataset,"excel","MSExcel",outExcelPath);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (TCException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}	
//	}
//	/**
//	 * 改变数据集的命名引用
//	 * 
//	 * @param tccomponentDataset
//	 * @param quote
//	 * @param type
//	 * @param path
//	 * @throws TCException
//	 */
//	public static void changeDataSet(TCComponentDataset tccomponentDataset,
//			String quote, String type, String path) throws TCException {
//		String myPath[] = { path };
//		String myQuote[] = { quote };// 引用"excel"
//		String myType[] = { type };// 类型"MSExcelX"
//		String myPlain[] = { "Plain" };
//
//		// 删除数据集的引用
//		deleDateSetRef(tccomponentDataset);
//		// 数据集的替换
//		tccomponentDataset.setFiles(myPath, myType, myPlain, myQuote);
//
//	}
//	/**
//	 * 删除数据集的命名引用
//	 * 
//	 * @param dataset
//	 * @throws TCException
//	 */
//	private static void deleDateSetRef(TCComponentDataset dataset)
//			throws TCException {
//
//		TCComponentTcFile[] tcFiles = dataset.getTcFiles();
//		for (int i = 0; i < tcFiles.length; i++) {
//			// 得到数据集的引用类型
//			String str_temp = getNamedRefType(dataset, tcFiles[i]);
//			// 删除引用
//			dataset.removeNamedReference(str_temp);
//		}
//
//	}
//	/**
//	 * 得到数据集的引用类型
//	 * 
//	 * @param datasetComponent
//	 * @param tccomponent
//	 * @return
//	 * @throws TCException
//	 */
//	private static String getNamedRefType(TCComponentDataset datasetComponent,
//			TCComponentTcFile tccomponent) throws TCException {
//		String s;
//		s = "";
//		TCProperty tcproperty;
//		TCProperty tcproperty1;
//		TCComponent atccomponent[];
//		String as[];
//		int i;
//		int j;
//		int k;
//		try {
//			tcproperty = datasetComponent.getTCProperty("ref_list");
//			tcproperty1 = datasetComponent.getTCProperty("ref_names");
//			if (tcproperty == null || tcproperty1 == null)
//				return s;
//		} catch (TCException tcexception) {
//			return s;
//		}
//		atccomponent = tcproperty.getReferenceValueArray();
//		as = tcproperty1.getStringValueArray();
//		if (atccomponent == null || as == null)
//			return s;
//		i = atccomponent.length;
//		if (i != as.length)
//			return s;
//		j = -1;
//		k = 0;
//		do {
//			if (k >= i)
//				break;
//			if (tccomponent == atccomponent[k]) {
//				j = k;
//				break;
//			}
//			k++;
//		} while (true);
//		if (j != -1)
//			s = as[j];
//		return s;
//	}
//
// }
