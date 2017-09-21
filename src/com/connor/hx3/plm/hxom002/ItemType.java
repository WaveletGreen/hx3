package com.connor.hx3.plm.hxom002;

import com.teamcenter.rac.kernel.TCComponentType;

public class ItemType {
	private String relType;
	private String displayType;
	private TCComponentType type;
	private CreateInfoBean bean;

	public CreateInfoBean getBean() {
		return bean;
	}

	public void setBean(CreateInfoBean bean) {
		this.bean = bean;
	}

	public TCComponentType getType() {
		return type;
	}

	public void setType(TCComponentType type) {
		this.type = type;
	}

	public ItemType() {

	}

	public ItemType(String relType, String displayType, TCComponentType type,
			CreateInfoBean bean) {

		this.relType = relType;
		this.displayType = displayType;
		this.type = type;
		this.bean = bean;
	}

	public ItemType(String displayType) {

		this.relType = "";
		this.displayType = displayType;
	}

	@Override
	public String toString() {
		return displayType;
	}

	public String print() {
		return "ItemType [relType=" + relType + ", displayType=" + displayType
				+ "]";
	}

	public String getRelType() {
		return relType;
	}

	public void setRelType(String relType) {
		this.relType = relType;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

}
