package com.connor.hx3.plm.hxom029;

public class TZWFBean {
	public static final String HX3_029_EXPORT_EXCEL = "外发图纸统计.xlsx";
	private String XUHAO;
	private String drawingsIndex;
	private String drawingsName;
	private String drawingsRev;
	private String owning_user;
	private String date_released;
	private String WFKH;

	public String getXUHAO() {
		return XUHAO;
	}

	public void setXUHAO(String xUHAO) {
		XUHAO = xUHAO;
	}

	public String getDrawingsIndex() {
		return drawingsIndex;
	}

	public void setDrawingsIndex(String drawingsIndex) {
		this.drawingsIndex = drawingsIndex;
	}

	public String getDrawingsName() {
		return drawingsName;
	}

	public void setDrawingsName(String drawingsName) {
		this.drawingsName = drawingsName;
	}

	public String getDrawingsRev() {
		return drawingsRev;
	}

	public void setDrawingsRev(String drawingsRev) {
		this.drawingsRev = drawingsRev;
	}

	public String getOwning_user() {
		return owning_user;
	}

	public void setOwning_user(String owning_user) {
		this.owning_user = owning_user;
	}

	public String getDate_released() {
		return date_released;
	}

	public void setDate_released(String date_released) {
		this.date_released = date_released;
	}

	public String getWFKH() {
		return WFKH;
	}

	public void setWFKH(String wFKH) {
		WFKH = wFKH;
	}

}
