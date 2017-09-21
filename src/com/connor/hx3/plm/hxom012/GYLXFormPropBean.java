package com.connor.hx3.plm.hxom012;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import com.teamcenter.rac.kernel.TCProperty;

public class GYLXFormPropBean {
	private TCProperty prop;
	private JComponent comp;
	private JButton addBtn;
	private JButton celBtn;
	private JComboBox<String> selectBox;
	private List<String> innerValue;

	public JButton getAddBtn() {
		return addBtn;
	}

	public void setAddBtn(JButton addBtn) {
		this.addBtn = addBtn;
	}

	public JButton getCelBtn() {
		return celBtn;
	}

	public void setCelBtn(JButton celBtn) {
		this.celBtn = celBtn;
	}

	public JComboBox<String> getSelectBox() {
		return selectBox;
	}

	public void setSelectBox(JComboBox<String> selectBox) {
		this.selectBox = selectBox;
	}

	public List<String> getInnerValue() {
		return innerValue;
	}

	public void setInnerValue(List<String> innerValue) {
		this.innerValue = innerValue;
	}

	public TCProperty getProp() {
		return prop;
	}

	public void setProp(TCProperty prop) {
		this.prop = prop;
	}

	public JComponent getComp() {
		return comp;
	}

	public void setComp(JComponent comp) {
		this.comp = comp;
	}

}
