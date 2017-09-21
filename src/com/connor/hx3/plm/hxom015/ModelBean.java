package com.connor.hx3.plm.hxom015;

import java.util.List;

public class ModelBean {

	private List<String> modelTypeMap;//

	private Integer rowNumber = 0;// 行数

	private Integer index;// 序号

	private String object_type;// 类型

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(" RowNO = ").append(rowNumber).append("{");
		if (modelTypeMap != null) {
			for (String type : modelTypeMap) {
				sb.append(" Type = ").append(type);
			}
		}
		sb.append("}");
		return sb.toString();

	}

	public List<String> getModelTypeMap() {
		return modelTypeMap;
	}

	public void setModelTypeMap(List<String> modelTypeMap) {
		this.modelTypeMap = modelTypeMap;
	}

	public Integer getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getObject_type() {
		return object_type;
	}

	public void setObject_type(String object_type) {
		this.object_type = object_type;
	}

}
