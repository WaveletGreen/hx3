package com.connor.hx3.plm.hxom042;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.connor.hx3.plm.util.HxomMethodUtil;
import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.kernel.TCClassificationService;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentDataset;
import com.teamcenter.rac.kernel.TCComponentICO;
import com.teamcenter.rac.kernel.TCComponentICOType;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentTcFile;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCPreferenceService;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.kernel.ics.ICSAdminAttribute;
import com.teamcenter.rac.kernel.ics.ICSAdminClass;
import com.teamcenter.rac.kernel.ics.ICSAdminClassAttribute;
import com.teamcenter.rac.kernel.ics.ICSApplicationObject;
import com.teamcenter.rac.kernel.ics.ICSKeyLov;
import com.teamcenter.rac.kernel.ics.ICSProperty;
import com.teamcenter.rac.kernel.ics.ICSPropertyDescription;
import com.teamcenter.rac.kernel.ics.ICSSearchResult;
import com.teamcenter.rac.kernel.ics.ICSView;
import com.teamcenter.rac.util.FileUtility;
import com.teamcenter.rac.util.MessageBox;

public class ClassReportOperation extends AbstractAIFOperation {
	private TCSession session ;
	private AbstractAIFApplication app;
	private List<ClassReportBean> beanList;
	private final String OP_NAME = "HXDataForm";
	
	public ClassReportOperation(TCSession session,AbstractAIFApplication app){
		this.session = session;
		this.app = app;
			
	}
	
	
	
	/**
	 * 获取配置信息
	 */
	public void getOperationInfo(){
		beanList = new ArrayList<>();
		TCPreferenceService service = this.session.getPreferenceService();
		String[] opValueS = service.getStringValues(this.OP_NAME);
		for(String opValue : opValueS){
			String[] valueS0 = opValue.split("&");
			if(valueS0.length!=2){
				continue;
			}
			//valueS0[0];
			String[] valueS1 = valueS0[1].split("\\|");
			for(String value10 : valueS1){
				String[] value10T = value10.split("@@");
				String value1 = value10T[0];
				List<ClassBean> otherClassBeanList = new ArrayList<>();
				for(int i = 1;i < value10T.length;i++){
					String classValue = value10T[i];
					classValue =classValue. substring(classValue.indexOf("(")+1, classValue.indexOf(")"));
					
					ClassBean classBean = new ClassBean();
					otherClassBeanList.add(classBean);
					
					List<ClassReportSelfPropBean> selfPropBeanList = new ArrayList<>();
					List<ClassReportSelfClassBean> selfClassBeanList = new ArrayList<>();
					classBean.selfPropBeanList = selfPropBeanList;
					classBean.selfClassBeanList = selfClassBeanList;
					if(classValue.contains("#")){
						//class name
						String className = classValue.substring(0, classValue.indexOf("#"));
						classBean.className = className;
						if(className.contains("<")){
							classBean.className = className.substring(0,className.indexOf("<"));
							String contents = className.substring(className.indexOf("<")+1,className.length()-1);
							List<String> contentsIDList = new ArrayList<>();
							List<String> contentsLovIDList = new ArrayList<>();
							List<String> contentsValueList = new ArrayList<>();
							List<String> notContentsIDList = new ArrayList<>();
							List<String> notContentsLovIDList = new ArrayList<>();
							List<String> notContentsValueList = new ArrayList<>();
							classBean.contentsIDList = contentsIDList;
							classBean.contentsLovIDList = contentsLovIDList;
							classBean.contentsValueList = contentsValueList;
							
							classBean.notContentsIDList = notContentsIDList;
							classBean.notContentsLovIDList = notContentsLovIDList;
							classBean.notContentsValueList = notContentsValueList;
							String[] subContent =contents.split("]");
							for(int j = 0;j < subContent.length;j++){
								if(subContent[j].contains("=")){
									String[] eStr = subContent[j].split("=");
									String[] ids = eStr[0].split("\\.");
									contentsIDList.add(ids[0]);
									if(ids.length == 2){
										contentsLovIDList.add(ids[1]);
									}else{
										contentsLovIDList.add("-1");
									}
									contentsValueList.add(eStr[1]);
									
								}else if(subContent[j].contains("≠")){
									String[] eStr = subContent[j].split("≠");
									String[] ids = eStr[0].split("\\.");
									
									notContentsIDList.add(ids[0]);
									if(ids.length == 2){
										notContentsLovIDList.add(ids[1]);
									}else{
										notContentsLovIDList.add("-1");
									}
									notContentsValueList.add(eStr[1]);
									
								}else if(subContent[j].contains("∈")){
									String[] eStr = subContent[j].split("∈");
									String[] tStr = eStr[1].split(",");
									String[] ids = eStr[0].split("\\.");
									for(String ts :tStr){
										contentsIDList.add(ids[0]);
										if(ids.length == 2){
											contentsLovIDList.add(ids[1]);
										}else{
											contentsLovIDList.add("-1");
										}
										contentsValueList.add(ts);
									}
								}	
							}
						}
						String propString = classValue.substring(classValue.indexOf("#")+1, classValue.length());
						
						if(propString.contains(":")){
							//prop self
							String selfPropStr = propString.substring(0, propString.indexOf(":"));
							String selfProp[] = selfPropStr.split("\\,");
							for(String selfSignleProp : selfProp){
								String columName[] = selfSignleProp.split("\\=");
								ClassReportSelfPropBean bean = new ClassReportSelfPropBean();
								bean.NO = columName[0];
								bean.relName =  columName[1];
								selfPropBeanList.add(bean);
							}
							//prop class
							String selClassPropStr = propString.substring( propString.indexOf(":")+1, propString.length());
							String selClass[] = selClassPropStr.split("\\,");
							for(String selSignleClass : selClass){
								String columName[] = selSignleClass.split("\\=");
								ClassReportSelfClassBean bean = new ClassReportSelfClassBean();
								bean.NO = columName[0];
								bean.relName = Integer.parseInt( columName[1]);
								if(columName.length ==3){
									bean.lovID = Integer.parseInt( columName[2]);
								}
								selfClassBeanList.add(bean);
							}
						}
					}
				}
				
				ClassReportBean reportBean = new ClassReportBean();
				List<ClassReportSelfPropBean> selfPropBeanList = new ArrayList<>();
				List<ClassReportSelfClassBean> selfClassBeanList= new ArrayList<>();
				
				Map<String, List<ClassReportChildClassBean>> childMap= new HashMap<String, List<ClassReportChildClassBean>>();
				reportBean.selfPropBeanList = selfPropBeanList;
				reportBean.selfClassBeanList = selfClassBeanList;
				reportBean.childMap = childMap;
				reportBean.otherClassBeanList = otherClassBeanList;
				
				
				beanList.add(reportBean);
				reportBean.excelPUID = valueS0[0];
				reportBean.sheetName = value1.substring(0, value1.indexOf("("));
				String propMsg = value1.substring(value1.indexOf("(")+1, value1.indexOf(")"));
				String[] valueS2 = propMsg.split("\\/");
				reportBean.className = valueS2[0].substring(0, valueS2[0].indexOf("#"));
				String className = reportBean.className;
				if(className.contains("<")){
					reportBean.className = className.substring(0,className.indexOf("<"));
					String contents = className.substring(className.indexOf("<")+1,className.length()-1);
					List<String> contentsIDList = new ArrayList<>();
					List<String> contentsLovIDList = new ArrayList<>();
					List<String> contentsValueList = new ArrayList<>();
					List<String> notContentsIDList = new ArrayList<>();
					List<String> notContentsLovIDList = new ArrayList<>();
					List<String> notContentsValueList = new ArrayList<>();
					reportBean.contentsIDList = contentsIDList;
					reportBean.contentsLovIDList = contentsLovIDList;
					reportBean.contentsValueList = contentsValueList;
					
					reportBean.notContentsIDList = notContentsIDList;
					reportBean.notContentsLovIDList = notContentsLovIDList;
					reportBean.notContentsValueList = notContentsValueList;
					String[] subContent =contents.split("]");
					for(int j = 0;j < subContent.length;j++){
						if(subContent[j].contains("=")){
							String[] eStr = subContent[j].split("=");
							String[] ids = eStr[0].split("\\.");
							contentsIDList.add(ids[0]);
							if(ids.length == 2){
								contentsLovIDList.add(ids[1]);
							}else {
								contentsLovIDList.add("-1");
							}
							contentsValueList.add(eStr[1]);
							
						}else if(subContent[j].contains("≠")){
							String[] eStr = subContent[j].split("≠");
							String[] ids = eStr[0].split("\\.");
							
							notContentsIDList.add(ids[0]);
							if(ids.length == 2){
								notContentsLovIDList.add(ids[1]);
							}else {
								notContentsLovIDList.add("-1");
							}
							notContentsValueList.add(eStr[1]);
							
						}else if(subContent[j].contains("∈")){
							String[] eStr = subContent[j].split("∈");
							String[] tStr = eStr[1].split(",");
							String[] ids = eStr[0].split("\\.");
							for(String ts :tStr){
								contentsIDList.add(ids[0]);
								if(ids.length == 2){
									contentsLovIDList.add(ids[1]);
								}else {
									contentsLovIDList.add("-1");
								}
								contentsValueList.add(ts);
							}
						}	
					}
				}
				
				
				String selfMsg = valueS2[0].substring(valueS2[0].indexOf("#")+1 , valueS2[0].length());
				String[] selS = selfMsg.split("\\:");
				String columNameS[] = selS[0].split("\\,");
				for(String colum:columNameS){
					String[] nameS = colum.split("\\=");
					ClassReportSelfPropBean selfPropBean = new ClassReportSelfPropBean();
					selfPropBean.NO = nameS[0];
					selfPropBean.relName = nameS[1];
					selfPropBeanList.add(selfPropBean);
				}
				
				if(selS.length ==2){
					String columNameS1[] = selS[1].split("\\,");
					for(String colum:columNameS1){
						String[] nameS = colum.split("\\=");
						ClassReportSelfClassBean selfClassBean = new ClassReportSelfClassBean();
						selfClassBean.NO = nameS[0];
						selfClassBean.relName = Integer.parseInt(nameS[1].trim());
						if(nameS.length == 3){
							selfClassBean.lovID=  Integer.parseInt(nameS[2].trim());
						}
						
						selfClassBeanList.add(selfClassBean);
					}
				}
				if(valueS2.length ==2){
					String childS[] = valueS2[1].split("\\*");
					for(String child : childS){
						String key = child.substring(0,child.indexOf(":"));
						List<ClassReportChildClassBean> childBeanList = new ArrayList<>();
						childMap.put(key, childBeanList);
						String columName =child.substring(child.indexOf(":")+1,child.length());
						String columNameS2[] = columName.split("\\,");
						for(String colum:columNameS2){
							String[] nameS = colum.split("\\=");
							ClassReportChildClassBean childBean = new ClassReportChildClassBean();
							childBean.NO = nameS[0];
							childBean.relName = Integer.parseInt(nameS[1].trim());
							if(nameS.length ==3 ){
								childBean.lovID = Integer.parseInt(nameS[2].trim());
							}
							childBeanList.add(childBean);
						}
					}
				}
			}
		}
	}
	
	
	public void createICS(String classId) throws TCException {

		TCClassificationService classificationService = this.session.getClassificationService(); 
		//	TCComponentItemRevision rev = this.bomLine.getItemRevision();
		ICSAdminClass c = classificationService.newICSAdminClass();
		c.load(classId);
		
		ICSSearchResult[] results = classificationService.searchICOs(classId,null , 0);
		if(results == null ||results.length == 0){
			System.out.println(" 搜索得到的结果为0");
			return;
		}
		for(int i = 0;i<results.length;i++){
			String icoID = results[i].getIcoId();
			String icoUid = results[i].getIcoUid();
			String wsoUid = results[i].getWsoUid();
			String classIDT = results[i].getClassId();
			InterfaceAIFComponent comp = this.session.stringToComponent(wsoUid);
			TCComponentItemRevision lastRev = null;
			if(comp instanceof TCComponentItemRevision){
				lastRev = (TCComponentItemRevision) comp;
			}else if(comp instanceof TCComponentItem){
				lastRev = ((TCComponentItem) comp).getLatestItemRevision();
			}
			
			InterfaceAIFComponent comp2 = this.session.stringToComponent(icoUid);
			ICSProperty[] icsProps = ((TCComponentICO)comp2).getICSProperties(true);
			for(int w = 0 ;w <icsProps.length;w++){
				icsProps[w].getValue();
			}
			
		}
	}
	/**
	 * 获取单个对象的属性
	 * @throws TCException 
	 */
	public void getSingleObjMsg(TCComponent comp ,List<ClassReportSelfPropBean> selfPropBeanList,List<ReportBean> reportBeanList) throws TCException{
		if(comp == null){
			return;
		}
		for(ClassReportSelfPropBean bean: selfPropBeanList){
			String disValue = comp.getTCProperty(bean.relName).getDisplayValue();
			ReportBean rb = new ReportBean();
			rb.NO = bean.NO;
			rb.value = disValue;
			reportBeanList.add(rb);
		}
		
	}
	
	/**
	 * 获取单个分类的属性
	 */
	public void getSingleClassMsg(String classId){
		
		
	}
	/**
	 * 获取熟悉关联的keylov的值
	 * @param id
	 * @param key
	 * @return
	 */
	public String getICSLOV(int id,String key){
		String value = key;
		ICSKeyLov lov =ICSKeyLov.getICSKeyLov(id);
		if(lov !=null){
			value = lov.getValueOfKey(key);
		}
		return value;
	}
	
	/**
	 * 获取BOM结构
	 * @throws TCException
	 */
	public void getStructMsg() throws TCException{
		TCClassificationService classificationService = this.session.getClassificationService(); 
			 
		ICSAdminClass c = classificationService.newICSAdminClass();
		//ICSPropertyDescription des = null;
		//ICSAdminAttribute attr = null;
		//ICSAdminClassAttribute attr2 = c.getAttribute(123);
		
		
		
		for(ClassReportBean reportBean : beanList){
			List<List<ReportBean>>  reportBeanList  = new ArrayList<>();
			reportBean.reportBeanList = reportBeanList;
			List<ClassReportSelfPropBean> selfPropBeanList = reportBean.selfPropBeanList;
			List<ClassReportSelfClassBean> selfClassBeanList= reportBean.selfClassBeanList;
			Map<String, List<ClassReportChildClassBean>> childMap= reportBean.childMap;
			c.load(reportBean.className);
			ICSSearchResult[] results = classificationService.searchICOs(reportBean.className,null , 0);
			if(results == null ||results.length == 0){
				System.out.println(" 搜索["+reportBean.className+"]得到的结果为0");
				continue;
			}
			LOOP_1 :
			for(int i = 0;i<results.length;i++){
				//String icoID = results[i].getIcoId();
				String icoUid = results[i].getIcoUid();
				String wsoUid = results[i].getWsoUid();
				//String classIDT = results[i].getClassId();
				InterfaceAIFComponent comp = this.session.stringToComponent(wsoUid);
				
				TCComponentItemRevision lastRev = null;
				if(comp instanceof TCComponentItemRevision){
					
					lastRev = (TCComponentItemRevision) comp;
				}else if(comp instanceof TCComponentItem){
					lastRev = ((TCComponentItem) comp).getLatestItemRevision();
				}
				List<ReportBean> rBeanList = new ArrayList<>();
				reportBeanList.add(rBeanList);
				//TODO 获取单个对象属性
				getSingleObjMsg((TCComponent)comp,selfPropBeanList,rBeanList);
				
				InterfaceAIFComponent comp2 = this.session.stringToComponent(icoUid);
				ICSProperty[] icsProps = ((TCComponentICO)comp2).getICSProperties(true);
				Map<Integer, String> idValueMap = new HashMap<>();
				
				for(int w = 0 ;w <icsProps.length;w++){
					System.out.println("zzzzz="+icsProps[w].getValue(icsProps[w].getId()));
					System.out.println("xxxxx="+icsProps[w].getValue());
					boolean content = false;
					boolean eques = false;
					for(int j = 0 ;j <reportBean.contentsIDList.size();j++ ){
						if(reportBean.contentsIDList.get(j).equals(""+icsProps[w].getId())){
							content = true;
							if(reportBean.contentsValueList.get(j).
									equals(
											this.getICSLOV( Integer.parseInt(reportBean.contentsLovIDList.get(j)), icsProps[w].getValue()) )){
								eques = true;
							}
						}
					}
					if(content && !eques){
						reportBeanList.remove(rBeanList);
						continue LOOP_1;
					}
					content = false;
					eques = false;
					for(int j = 0 ;j <reportBean.notContentsIDList.size();j++ ){
						if(reportBean.notContentsIDList.get(j).equals(""+icsProps[w].getId())){
							content = true;
							if(! reportBean.notContentsValueList.get(j).equals(this.getICSLOV(Integer.parseInt(reportBean.notContentsLovIDList.get(j)), icsProps[w].getValue()))){
								eques = true;
							}
						}
					}
					if(content && !eques){
						reportBeanList.remove(rBeanList);
						continue LOOP_1;
					}
					
					idValueMap.put(icsProps[w].getId(), icsProps[w].getValue());
				}
				//TODO 获取父的分类属性信息
				for(ClassReportSelfClassBean bean :selfClassBeanList){
					
					if(idValueMap.containsKey(bean.relName)){
						ReportBean rb = new ReportBean();
						rb.NO = bean.NO;
						rb.value = idValueMap.get(bean.relName);
						rb.value = this.getICSLOV(bean.lovID, rb.value);
						rBeanList.add(rb);
					}
				}
				
				List<TCComponentItem> childItemList = HxomMethodUtil.getAllBomItemByTopRev(lastRev);
				List<String> valueList = new ArrayList<>();
				TCProperty[][] propListS = TCComponentType.getTCPropertiesSet(childItemList, new String[]{"cd9_codeStyle"});
				for(int j = 0 ;j < propListS.length;j++){
					String value = propListS[j][0].getStringValue().trim();
					value = value.replace(" ", "");
					value = value.split("\\[")[0];
					//修改逻辑
					TCComponentItem childItem = childItemList.get(j);
					TCComponentICO[] icoS =	childItem.getClassificationObjects();
					if(icoS == null || icoS.length==0){
						icoS =	childItem.getLatestItemRevision().getClassificationObjects();
						if(icoS == null || icoS.length==0)
							continue;
					}
					for(TCComponentICO cIco : icoS){
						
					String object_type_id = cIco.getStringProperty("object_type_id");
					System.out.println(">>> "+value+"^"+object_type_id);
						
					
					if(childMap.containsKey(value + "^"+object_type_id)&& ! valueList.contains(value + "^"+object_type_id)){
						
						Map<Integer, String> classAttrS = new HashMap<>();
						
						ICSProperty[] cPropS = cIco.getICSProperties(true);
						for(ICSProperty prop : cPropS){
							classAttrS.put(prop.getId(), prop.getValue());
						}
						
						valueList.add(value + "^"+object_type_id);
						List<ClassReportChildClassBean> childBeanList =childMap.get(value + "^"+object_type_id);
						for(ClassReportChildClassBean ccb : childBeanList){
							if(classAttrS.containsKey(ccb.relName)){
								
								ReportBean rb = new ReportBean();
								rb.NO = ccb.NO;
								rb.value = classAttrS.get(ccb.relName);
								rb.value = this.getICSLOV(ccb.lovID, rb.value);
								rBeanList.add(rb);
							}
						}
						
					}
					}
				}
			}
			//扩展对象
			List<ClassBean> otherClassBeanList = reportBean.otherClassBeanList;
			LOOP_2 :
			for(ClassBean cBean : otherClassBeanList){
				
				selfPropBeanList = cBean.selfPropBeanList;
				selfClassBeanList = cBean.selfClassBeanList;
				c.load(cBean.className);
				
				results = classificationService.searchICOs(cBean.className,null , 0);
				if(results == null ||results.length == 0){
					System.out.println(" 搜索["+cBean.className+"]得到的结果为0");
					continue;
				}
				for(int i = 0;i<results.length;i++){
					//String icoID = results[i].getIcoId();
					String icoUid = results[i].getIcoUid();
					String wsoUid = results[i].getWsoUid();
					//String classIDT = results[i].getClassId();
					InterfaceAIFComponent comp = this.session.stringToComponent(wsoUid);
					TCComponentItemRevision lastRev = null;
					if(comp instanceof TCComponentItemRevision){
						lastRev = (TCComponentItemRevision) comp;
					}else if(comp instanceof TCComponentItem){
						lastRev = ((TCComponentItem) comp).getLatestItemRevision();
					}
					List<ReportBean> rBeanList = new ArrayList<>();
					reportBeanList.add(rBeanList);
					//TODO 获取单个对象属性
					getSingleObjMsg((TCComponent)comp,selfPropBeanList,rBeanList);
					
					InterfaceAIFComponent comp2 = this.session.stringToComponent(icoUid);
					ICSProperty[] icsProps = ((TCComponentICO)comp2).getICSProperties(true);
					
					Map<Integer, String> idValueMap = new HashMap<>();
					for(int w = 0 ;w <icsProps.length;w++){
						
						boolean content = false;
						boolean eques = false;
						for(int j = 0 ;j <cBean.contentsIDList.size();j++ ){
							if(cBean.contentsIDList.get(j).equals(""+icsProps[w].getId())){
								content = true;
								if(cBean.contentsValueList.get(j).
										equals(
												this.getICSLOV( Integer.parseInt(cBean.contentsLovIDList.get(j)), icsProps[w].getValue()) )){
									eques = true;
								}
							}
						}
						if(content && !eques){
							reportBeanList.remove(rBeanList);
							continue LOOP_2;
						}
						content = false;
						eques = false;
						for(int j = 0 ;j <cBean.notContentsIDList.size();j++ ){
							if(cBean.notContentsIDList.get(j).equals(""+icsProps[w].getId())){
								content = true;
								if(! cBean.notContentsValueList.get(j).equals(this.getICSLOV(Integer.parseInt(cBean.notContentsLovIDList.get(j)), icsProps[w].getValue()))){
									eques = true;
								}
							}
						}
						if(content && !eques){
							reportBeanList.remove(rBeanList);
							continue LOOP_2;
						}
						
						
						idValueMap.put(icsProps[w].getId(), icsProps[w].getValue());
					}
					//TODO 获取父的分类属性信息
					for(ClassReportSelfClassBean bean :selfClassBeanList){
						if(idValueMap.containsKey(bean.relName)){
							ReportBean rb = new ReportBean();
							rb.NO = bean.NO;
							rb.value = idValueMap.get(bean.relName);
							rb.value = this.getICSLOV(bean.lovID, rb.value);
							rBeanList.add(rb);
							
						}
					}
				}
			}
		}
	}

	@Override
	public void executeOperation() throws Exception {
		getOperationInfo();
		getStructMsg();
		int index = 1;
		
		for(ClassReportBean reportBean : beanList){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSSsss");
			String name = sdf.format(new Date());
			name = System.getenv("TEMP")+"\\"+name;
			TCComponent comp = this.session.stringToComponent(reportBean.excelPUID);
			if(comp == null  || !(comp instanceof TCComponentDataset)){
				MessageBox.post("["+reportBean.excelPUID+"]该PUID不是数据集对象，请通知管理员更改首选项["+this.OP_NAME+"]","错误",MessageBox.ERROR);
				return;
			}
			String path = HxomMethodUtil.downLoadFile(comp);
			if(!path.endsWith(".xls")){
				MessageBox.post("["+reportBean.excelPUID+"]的模板格式不正确，要使用[.xls]的模板，请通知管理员更改模板","错误",MessageBox.ERROR);
				return;
			}
			
			String inPath = name +"_"+(index++) +".xls";
			FileUtility.copyFile(new File(path), new File(inPath));
			String outPath  =  name +"_" + index  + ".xls";
		
			ExcelUtil.writeClassMsgExcel(inPath , outPath, reportBean);
			
			changeDataSet((TCComponentDataset)comp,"excel", "MSExcelX", outPath);
		}
		MessageBox.post("更新成功！","错误",MessageBox.INFORMATION);
			
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
		atccomponent = tcproperty.getReferenceValueArray();
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

}
