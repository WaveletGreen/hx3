package com.connor.hx3.plm.hxom042;

public class ClassReportSelfClassBean {
	public String NO;
	public int relName;
	public String disValue;
	public int lovID = -1;
	
	@Override
	public String toString() {
		return "[ NO="+NO+" relName="+relName +" disValue="+disValue + " lovID="+lovID+"]";
	}
}
