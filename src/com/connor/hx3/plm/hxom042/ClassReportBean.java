package com.connor.hx3.plm.hxom042;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ClassReportBean {
	public String excelPUID ;
	public String sheetName;
	public String className;
	public List<ClassReportSelfPropBean> selfPropBeanList;
	public List<ClassReportSelfClassBean> selfClassBeanList;
	public Map<String, List<ClassReportChildClassBean>> childMap;
	public List<List<ReportBean>> reportBeanList ;
	public List<ClassBean> otherClassBeanList;
	
	public List<String> contentsIDList = new ArrayList<>();
	public List<String> contentsLovIDList= new ArrayList<>();
	public List<String> contentsValueList= new ArrayList<>();
	
	public List<String> notContentsIDList= new ArrayList<>();
	public List<String> notContentsLovIDList= new ArrayList<>();
	public List<String> notContentsValueList= new ArrayList<>();
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELF[excelPUID=").append(excelPUID);
		sb.append(" sheetName=").append(sheetName);
		sb.append(" className=").append(className).append("] \n");
		sb.append("[");
		if(selfPropBeanList!=null){
			for(ClassReportSelfPropBean bean : selfPropBeanList){
				sb.append(bean).append(" ");
			}
		}
		sb.append("] \n");
		sb.append("[");
		
		if(selfClassBeanList!=null){
			for(ClassReportSelfClassBean bean : selfClassBeanList){
				sb.append(bean).append(" ");
			}
		}
		sb.append("] \n");
		
		sb.append("[");
		
		if(childMap!=null){
			
			Set<Entry<String, List<ClassReportChildClassBean>>>  entrySet = childMap.entrySet();
			for(Entry<String, List<ClassReportChildClassBean>> entry : entrySet){
				
				sb.append("[ KEY=").append(entry.getKey()).append(" ");
				if(entry.getValue()!=null){
					for(ClassReportChildClassBean bean :entry.getValue() ){
						
						sb.append(bean).append("\n");
	
					}
				}
				sb.append("]\n");
				
			}
		}
		sb.append("] ");
		
		
		return sb.toString();
	}
}
