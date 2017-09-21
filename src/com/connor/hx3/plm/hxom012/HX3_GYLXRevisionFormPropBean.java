package com.connor.hx3.plm.hxom012;

import java.util.Date;

public class HX3_GYLXRevisionFormPropBean implements Comparable{

	public static final int COUNT = 16;
	public String hx3_gxhh = "";// "工序行号",
	public String hx3_bzgx = "";// "标准工序",
	public String hx3_gxsm = "";// "工序说明",
	public String hx3_gzzx = "";// "工作中心",
	public String hx3_gzzxmc = "";// "工作中心名称",// 5
	public String hx3_mjbh = "";// "模具编号",
	public boolean hx3_dcgx1 = false;// "倒冲工序",
	public boolean hx3_bgd1 = false;// "报告点",
	public boolean hx3_wwgx1 = false;// "委外工序",
	public boolean hx3_jfd1 = false;// "计费点",// 10
	public boolean hx3_jhwwgx1 = false;// "计划委外工序",
	public Date hx3_sxrq = new Date();// "生效日期",
	public Date hx3_shixrq = new Date();// "失效日期",
	public String hx3_gywjbh = "";// "工艺文件编号",
	public String hx3_gywjbb = "";// "工艺文件版本", // 15
	public String hx3_lywl = "";// "领用物料",
	// public String hx3_lywlid = "";// "领用物料ID" // 17
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if(o ==null){
			return  0;
		}
		HX3_GYLXRevisionFormPropBean bean = (HX3_GYLXRevisionFormPropBean) o;
		if(this.hx3_gxhh == null){
			if(bean.hx3_gxhh!=null){
				//111
				return "".compareTo(bean.hx3_gxhh);
			}else{
				return  0;
			}
		}else{
			if(bean.hx3_gxhh!=null){
				//111
				return this.hx3_gxhh.compareTo(bean.hx3_gxhh);
			}else{
				return  this.hx3_gxhh.compareTo("");
			}
		//return this.hx3_gxhh.compareTo(bean.hx3_gxhh);
		}
	}

}
