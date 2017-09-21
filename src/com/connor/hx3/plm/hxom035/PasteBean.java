package com.connor.hx3.plm.hxom035;

public class PasteBean {
	private String XUHAO;
	private String drawingsIndex;
	private String drawingsName;
	private String drawingsRev;
	private String owning_user;
	private String date_released;
	private String WFKH;
	private String toStrng;

	public PasteBean() {

	}

	public PasteBean(String XUHAO, String drawingsIndex, String drawingsRev,
			String drawingsName, String owning_user, String date_released,
			String WFKH, String toStrng) {
		this.XUHAO = XUHAO;
		this.drawingsIndex = drawingsIndex;
		this.drawingsName = drawingsName;
		this.drawingsRev = drawingsRev;
		this.owning_user = owning_user;
		this.date_released = date_released;
		this.WFKH = WFKH;
		this.toStrng = toStrng;
	}

	public String getToStrng() {
		return toStrng;
	}

	public void setToStrng(String toStrng) {
		this.toStrng = toStrng;
	}

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
