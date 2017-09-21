package com.connor.hx3.plm.hxom015;

public class PsdBean {
	// public static final String HX3_EXPORT_EXCEL = "OTS送样文件交付清单.xlsx";
	public static final String HX3_PSD_DATE = "yyyy-MM-dd";

	private String XMMC;// 项目名称

	private String JCRQ;// 检查日期

	private String XUHAO;// 序号

	private String JFJD;// 交付节点

	private String object_type;// item文件类型

	private String object_name;// 版本的obje_name

	private String item_id;// 版本的item_id

	public String getDate_released() {
		return date_released;
	}

	public void setDate_released(String date_released) {
		this.date_released = date_released;
	}

	private String owning_user;// 提交人

	public String getOwning_user() {
		return owning_user;
	}

	public void setOwning_user(String owning_user) {
		this.owning_user = owning_user;
	}

	private String date_released = "";// 发布日期

	private String revision_id;// 最新版本

	private String SFTJ;// 是否提交

	public String getXMMC() {
		return XMMC;
	}

	public void setXMMC(String xMMC) {
		XMMC = xMMC;
	}

	public String getJCRQ() {
		return JCRQ;
	}

	public void setJCRQ(String jCRQ) {
		JCRQ = jCRQ;
	}

	public String getXUHAO() {
		return XUHAO;
	}

	public void setXUHAO(String xUHAO) {
		XUHAO = xUHAO;
	}

	public String getJFJD() {
		return JFJD;
	}

	public void setJFJD(String jFJD) {
		JFJD = jFJD;
	}

	public String getObject_type() {
		return object_type;
	}

	public void setObject_type(String object_type) {
		this.object_type = object_type;
	}

	public String getObject_name() {
		return object_name;
	}

	public void setObject_name(String object_name) {
		this.object_name = object_name;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public String getRevision_id() {
		return revision_id;
	}

	public void setRevision_id(String revision_id) {
		this.revision_id = revision_id;
	}

	public String getSFTJ() {
		return SFTJ;
	}

	public void setSFTJ(String sFTJ) {
		SFTJ = sFTJ;
	}

	@Override
	public String toString() {
		return "PsdBean [XMMC=" + XMMC + ", JCRQ=" + JCRQ + ", XUHAO=" + XUHAO
				+ ", JFJD=" + JFJD + ", object_type=" + object_type
				+ ", object_name=" + object_name + ", item_id=" + item_id
				+ ", owning_user=" + owning_user + ", date_released="
				+ date_released + ", revision_id=" + revision_id + ", SFTJ="
				+ SFTJ + "]";
	}

}
