package com.connor.hx3.plm.hxom011;

import java.util.Date;
import java.util.List;

import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentListOfValues;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCProperty;

public class Hxom011Bean {

	public Hxom011Bean() {

	}

	public Hxom011Bean(String DisplayName) {
		this.DisplayName = DisplayName;
	}

	// =============================================================
	private String DisplayName;
	private String QTY;
	private String QUERY;
	private String CustomerNo = "";
	private String WGYQ_EN = "";
	private String WGYQ_ZH_CN = "";
	private TCComponentItemRevision rev;
	private TCComponentItemRevision selectRev;
	private Hxom011Bean parentBean;
	private Hxom011Bean designBean;
	private List<Hxom011Bean> materialRevS;
	private List<Hxom011Bean> childBeanS;
	public Date creationDate;
	public String itemID;
	public String JCYL ="1";
	
	
	
	
	public String getJCYL() {
		return JCYL;
	}

	public void setJCYL(String jCYL) {
		JCYL = jCYL;
	}

	// =============================================================
	public String getCustomerNo() {
		return CustomerNo;
	}

	public void setCustomerNo(String customerNo) {
		CustomerNo = customerNo;
	}

	
	
	public String getWGYQ_EN() {
		return WGYQ_EN;
	}

	public void setWGYQ_EN(String wGYQ_EN) {
		WGYQ_EN = wGYQ_EN;
	}

	public String getWGYQ_ZH_CN() {
		return WGYQ_ZH_CN;
	}

	public void setWGYQ_ZH_CN(String wGYQ_ZH_CN) {
		WGYQ_ZH_CN = wGYQ_ZH_CN;
	}

	public String getQTY() {
		return QTY;
	}

	

	public Hxom011Bean getDesignBean() {
		return designBean;
	}

	public void setDesignBean(Hxom011Bean designBean) {
		this.designBean = designBean;
	}

	public String getQUERY() {
		return QUERY;
	}

	public void setQUERY(String qUERY) {
		QUERY = qUERY;
	}

	public void setQTY(String qTY) {
		QTY = qTY;
	}

	public List<Hxom011Bean> getMaterialRevS() {
		return materialRevS;
	}

	// 设置默认的选择的物料
	public void setMaterialRevS(List<Hxom011Bean> materialRevS) {
		this.materialRevS = materialRevS;
		if (materialRevS != null && materialRevS.size() > 0) {
			this.selectRev = materialRevS.get(0).getRev();
		}
	}

	public Hxom011Bean getParentBean() {
		return parentBean;
	}

	public void setParentBean(Hxom011Bean parentBean) {
		this.parentBean = parentBean;
	}

	public TCComponentItemRevision getSelectRev() {
		return selectRev;
	}

	public void setSelectRev(TCComponentItemRevision selectRev) {
		this.selectRev = selectRev;
	}

	public String getDisplayName() {
		return DisplayName;
	}

	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}

	public TCComponentItemRevision getRev() {
		return rev;
	}

	public void setRev(TCComponentItemRevision rev) {
		this.rev = rev;
		if (rev != null) {
			try {
				this.DisplayName = rev.getStringProperty("object_string");
				if(rev.getType().equals("HX3_WLRevision")){
					creationDate = rev.getDateProperty("creation_date");
					itemID = rev.getStringProperty("item_id");
					TCComponent formRev = rev.getReferenceListProperty("IMAN_master_form_rev")[0];
					TCProperty[] properties = formRev.getTCProperties(new String[]{"hx3_sskh","hx3_wgyq"});
					this.CustomerNo = properties[0]==null?"": properties[0].getStringValue();
					if(properties[1]!=null){
						this.WGYQ_ZH_CN = properties[1].getDisplayValue();
						this.WGYQ_EN = properties[1].getStringValue();
					}
					this.DisplayName  = this.DisplayName  + "客户["+this.CustomerNo+"]" + "外观要求["+this.WGYQ_EN+"/"+this.WGYQ_ZH_CN+"]" ;
				}
				
			} catch (TCException e) {
				e.printStackTrace();
			}
		}
	}

	public List<Hxom011Bean> getchildBeanS() {
		return childBeanS;
	}

	public void setchildBeanS(List<Hxom011Bean> childBeanS) {
		this.childBeanS = childBeanS;
	}

	@Override
	public String toString() {
		return DisplayName;
	}

}
