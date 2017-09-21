package com.connor.hx3.plm.hxom012;

public class LOVBean {
	public String disValue;
	public String value;
	public String relName;
	
	
	public LOVBean(String disValue,String value){
		this.disValue = disValue;
		this.value = value;
	}
	
	
	@Override
	public String toString() {
		return  disValue==null?"":disValue;
	}
	
}
