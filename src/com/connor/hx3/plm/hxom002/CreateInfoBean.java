package com.connor.hx3.plm.hxom002;

import java.util.HashMap;

public class CreateInfoBean {
	private String nodeName;
	private String codeRule;
	private String saveFlag;
	private String typeName;
	private HashMap<String, String> itemPropMap;
	private HashMap<String, String> revPropMap;
	private HashMap<String, String> revFormPropMap;

	public HashMap<String, String> getItemPropMap() {
		return itemPropMap;
	}

	public void setItemPropMap(HashMap<String, String> itemPropMap) {
		this.itemPropMap = itemPropMap;
	}

	public HashMap<String, String> getRevPropMap() {
		return revPropMap;
	}

	public void setRevPropMap(HashMap<String, String> revPropMap) {
		this.revPropMap = revPropMap;
	}

	public HashMap<String, String> getRevFormPropMap() {
		return revFormPropMap;
	}

	public void setRevFormPropMap(HashMap<String, String> revFormPropMap) {
		this.revFormPropMap = revFormPropMap;
	}

	private String company;
	private String companyCode;

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	private String location;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getCodeRule() {
		return codeRule;
	}

	public void setCodeRule(String codeRule) {
		this.codeRule = codeRule;
	}

	public String getSaveFlag() {
		return saveFlag;
	}

	public void setSaveFlag(String saveFlag) {
		this.saveFlag = saveFlag;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
