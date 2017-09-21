package com.connor.hx3.plm.hxom012;

import java.util.List;

import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCException;

public class BomStructBean {

	private TCComponentBOMLine bomLine;

	private String gxh;
	private String itemID;

	private List<BomStructBean> chlidLineS;

	// ============================================

	public List<BomStructBean> getChlidLineS() {
		return chlidLineS;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getGxh() {
		return gxh;
	}

	public void setGxh(String gxh) {
		this.gxh = gxh;
	}

	public void setChlidLineS(List<BomStructBean> chlidLineS) {
		this.chlidLineS = chlidLineS;
	}

	public TCComponentBOMLine getBomLine() {
		return bomLine;
	}

	public void setBomLine(TCComponentBOMLine bomLine) {
		this.bomLine = bomLine;
		try {
			this.gxh = bomLine.getStringProperty("HX3_gxhh");
			this.itemID = bomLine.getItem().getStringProperty("item_id");
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		String objectString = "NULL";
		if (bomLine != null) {
			try {
				objectString = bomLine.getItemRevision().getStringProperty(
						"object_string")
						+ " [" + this.gxh + "]";

			} catch (TCException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return objectString;
	}

}
