package com.connor.hx3.plm.hxom016;

public class propBean {
	public static final String HX3_EXPORT_EXCEL = "问题点统计报表.xlsx";
	public static final String HX3_DATE = "yyyy-MM-dd";

	private String current_date;// 统计时间

	public String getCurrent_date() {
		return current_date;
	}

	public void setCurrent_date(String current_date) {
		this.current_date = current_date;
	}

	private String hx3_lclx;

	// 序号
	private String XUHAO;

	// 发起人
	private String owning_user;

	// 问题点编码
	private String item_id;

	// 问题点名称
	private String object_name;

	// 问题描述
	private String hx3_wtms;

	// 重要度
	private String hx3_zyd;

	// 原因分析
	private String hx3_yyfx;

	// 解决措施
	private String hx3_jjcs;

	// 责任部门
	private String hx3_zrbm;

	// 处理状态
	private String hx3_clzt;

	// 是否发布
	private String release_status;

	// 发布时间
	private String data_released;

	// 验证结果
	private String hx3_yzjg;

	// 产品型号
	// private String hx3_cpxh;

	// 问题类别
	// private String hx3_wtlb;

	// 项目阶段
	// private String hx3_xmjd;

	// -----------------------------------------------------

	public String gethx3_lclx() {
		return hx3_lclx;
	}

	public void sethx3_lclx(String hx3_lclx) {
		this.hx3_lclx = hx3_lclx;
	}

	public String gethx3_yzjg() {
		return hx3_yzjg;
	}

	public void sethx3_yzjg(String hx3_yzjg) {
		this.hx3_yzjg = hx3_yzjg;
	}

	public String getXUHAO() {
		return XUHAO;
	}

	public void setXUHAO(String XUHAO) {
		this.XUHAO = XUHAO;
	}

	public String getrelease_status() {
		return release_status;
	}

	public void setrelease_status(String release_status) {
		this.release_status = release_status;
	}

	public String getdata_released() {
		return data_released;
	}

	public void setdata_released(String data_released) {
		this.data_released = data_released;
	}

	public String getowning_user() {
		return owning_user;
	}

	public void setowning_user(String owning_user) {
		this.owning_user = owning_user;
	}

	public String getobject_name() {
		return object_name;
	}

	public void setobject_name(String object_name) {
		this.object_name = object_name;
	}

	public String getitem_id() {
		return item_id;
	}

	public void setitem_id(String item_id) {
		this.item_id = item_id;
	}

	public String gethx3_wtms() {
		return hx3_wtms;
	}

	public void sethx3_wtms(String hx3_wtms) {
		this.hx3_wtms = hx3_wtms;
	}

	public String gethx3_zyd() {
		return hx3_zyd;
	}

	public void sethx3_zyd(String hx3_zyd) {
		this.hx3_zyd = hx3_zyd;
	}

	public String gethx3_yyfx() {
		return hx3_yyfx;
	}

	public void sethx3_yyfx(String hx3_yyfx) {
		this.hx3_yyfx = hx3_yyfx;
	}

	public String gethx3_jjcs() {
		return hx3_jjcs;
	}

	public void sethx3_jjcs(String hx3_jjcs) {
		this.hx3_jjcs = hx3_jjcs;
	}

	public String gethx3_zrbm() {
		return hx3_zrbm;
	}

	public void sethx3_zrbm(String hx3_zrbm) {
		this.hx3_zrbm = hx3_zrbm;
	}

	public String gethx3_clzt() {
		return hx3_clzt;
	}

	public void sethx3_clzt(String hx3_clzt) {
		this.hx3_clzt = hx3_clzt;
	}

	// public String gethx3_cpxh() {
	// return hx3_cpxh;
	// }
	//
	// public void sethx3_cpxh(String hx3_cpxh) {
	// hx3_cpxh = hx3_cpxh;
	// }

	// public String gethx3_wtlb() {
	// return hx3_wtlb;
	// }
	//
	// public void sethx3_wtlb(String hx3_wtlb) {
	// hx3_wtlb = hx3_wtlb;
	// }

	// public String gethx3_xmjd() {
	// return hx3_xmjd;
	// }
	//
	// public void sethx3_xmjd(String hx3_xmjd) {
	// hx3_xmjd = hx3_xmjd;
	// }

}
