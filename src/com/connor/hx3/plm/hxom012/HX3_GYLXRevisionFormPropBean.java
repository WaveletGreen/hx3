package com.connor.hx3.plm.hxom012;

import java.util.Date;

public class HX3_GYLXRevisionFormPropBean implements Comparable{

	public static final int COUNT = 16;
	public String hx3_gxhh = "";// "�����к�",
	public String hx3_bzgx = "";// "��׼����",
	public String hx3_gxsm = "";// "����˵��",
	public String hx3_gzzx = "";// "��������",
	public String hx3_gzzxmc = "";// "������������",// 5
	public String hx3_mjbh = "";// "ģ�߱��",
	public boolean hx3_dcgx1 = false;// "���幤��",
	public boolean hx3_bgd1 = false;// "�����",
	public boolean hx3_wwgx1 = false;// "ί�⹤��",
	public boolean hx3_jfd1 = false;// "�Ʒѵ�",// 10
	public boolean hx3_jhwwgx1 = false;// "�ƻ�ί�⹤��",
	public Date hx3_sxrq = new Date();// "��Ч����",
	public Date hx3_shixrq = new Date();// "ʧЧ����",
	public String hx3_gywjbh = "";// "�����ļ����",
	public String hx3_gywjbb = "";// "�����ļ��汾", // 15
	public String hx3_lywl = "";// "��������",
	// public String hx3_lywlid = "";// "��������ID" // 17
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
