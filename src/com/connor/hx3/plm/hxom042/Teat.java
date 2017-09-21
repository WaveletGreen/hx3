package com.connor.hx3.plm.hxom042;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teamcenter.rac.kernel.TCPreferenceService;

public class Teat {
	
	public static void main(String[] args) {
		String s = "1231 [T]";
		s =s.trim();
		s = s.trim();
		s = s.replace(" ", "");
		//if(s.contains("."))
			System.out.println(s.split("\\[")[0] +"|");
	}
	public static void main3(String[] args) {
		String className = "condongpan<330=1]331≠2]333∈aa,bb>";
		if(className.contains("<")){
			System.out.println(className.substring(0,className.indexOf("<")));
			String contents = className.substring(className.indexOf("<")+1,className.length()-1);
			List<String> contentsList = new ArrayList<>();
			List<String> notContentsList = new ArrayList<>();
			//classBean.contentsList = contentsList;
			//classBean.notContentsList = notContentsList;
			String[] subContent =contents.split("]");
			for(int j = 0;j < subContent.length;j++){
				if(subContent[j].contains("=")){
					String[] eStr = subContent[j].split("=");
					contentsList.add(eStr[0]+eStr[1]);
				}else if(subContent[j].contains("≠")){
					String[] eStr = subContent[j].split("≠");
					notContentsList.add(eStr[0]+eStr[1]);
				}else if(subContent[j].contains("∈")){
					String[] eStr = subContent[j].split("∈");
					String[] tStr = eStr[1].split(",");
					for(String ts :tStr){
						contentsList.add(eStr[0]+ts);
					}
				}	
			}
			System.out.println("1111111");
		}
	}
	public static void main1(String[] args) {
		//String str = "W5ShTVFDKDrcaB&数据源(congdong(panzongcheng#A=item_id:B=30550,C=30551,D=30552/从动盘类:E=30503,F=30504*摩擦片类:G=30353)";
		
		//System.out.println(str.substring(0,str.indexOf("(")));
	//System.out.println(str.substring(str.indexOf("(")+1, str.indexOf(")")));
		
		Integer.parseInt("30531");
		
		String str = "1231]";
		System.out.println(str.split("\\[").length  + "|" +str.split("\\[")[0] );
	}

	public static void main2(String[] args) {

		 List<ClassReportBean> beanList = new ArrayList<>();
		//TCPreferenceService service = this.session.getPreferenceService();
		String[] opValueS = {"W5ShTVFDKDrcaB&数据源(congdongpanzongcheng#A=item_id:B=30550,C=30551,D=30552/从动盘类:E=30503,F=30504*摩擦片类:G=30353)",
				"mJbhTVFDKDrcaB&从动盘(congdongpan#C=item_id:E=30503,F=30504,G=30505/)|面片合并后新数据(mocapian#B=item_id:F=30353,G=30354,H=30355/)"};
		for(String opValue : opValueS){
			String[] valueS0 = opValue.split("&");
			if(valueS0.length!=2){
				continue;
			}
			//valueS0[0];
			String[] valueS1 = valueS0[1].split("\\|");
			for(String value1 : valueS1){
				ClassReportBean reportBean = new ClassReportBean();
				
				List<ClassReportSelfPropBean> selfPropBeanList = new ArrayList<>();
				List<ClassReportSelfClassBean> selfClassBeanList= new ArrayList<>();
				Map<String, List<ClassReportChildClassBean>> childMap= new HashMap<String, List<ClassReportChildClassBean>>();
				reportBean.selfPropBeanList = selfPropBeanList;
				reportBean.selfClassBeanList = selfClassBeanList;
				reportBean.childMap = childMap;
				 
				beanList.add(reportBean);
				reportBean.excelPUID = valueS0[0];
				reportBean.sheetName = value1.substring(0, value1.indexOf("("));
				String propMsg = value1.substring(value1.indexOf("(")+1, value1.indexOf(")"));
				String[] valueS2 = propMsg.split("\\/");
				reportBean.className = valueS2[0].substring(0, valueS2[0].indexOf("#"));
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
						selfClassBeanList.add(selfClassBean);
					}
				}
				
				if(valueS2.length ==2){

					String childS[] = valueS2[1].split("\\*");
					for(String child : childS){
						System.out.println(child);
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
							childBeanList.add(childBean);
						}
					}
				}	
			}
		}
		
		for(ClassReportBean bean : beanList){
			
			System.out.println(bean.toString());
		}
	
	}
}
