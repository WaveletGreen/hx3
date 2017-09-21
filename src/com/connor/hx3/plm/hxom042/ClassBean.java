package com.connor.hx3.plm.hxom042;

import java.util.ArrayList;
import java.util.List;

public class ClassBean {
	public String className;
	public List<ClassReportSelfPropBean> selfPropBeanList;
	public List<ClassReportSelfClassBean> selfClassBeanList;
	public List<String> contentsIDList= new ArrayList<>();
	public List<String> contentsLovIDList= new ArrayList<>();
	public List<String> contentsValueList= new ArrayList<>();
	
	public List<String> notContentsIDList= new ArrayList<>();
	public List<String> notContentsLovIDList= new ArrayList<>();
	public List<String> notContentsValueList= new ArrayList<>();
	

}
