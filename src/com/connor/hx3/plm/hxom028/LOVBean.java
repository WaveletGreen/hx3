package com.connor.hx3.plm.hxom028;

/**
 * @copyRight 杭州康勒科技有限公司
 * @author 作者 E-mail:hub@connor.net.cn
 * @date 创建时间：2016-10-25 下午11:40:51
 * @version v1.0
 * @parameter
 * @since
 * @return
 */

public class LOVBean {
	private String displayValue;
	private String relValue;

	public LOVBean() {
	}

	public LOVBean(String displayValue, String relValue) {
		this.displayValue = displayValue;
		this.relValue = relValue;

	}

	@Override
	public String toString() {
		return displayValue;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getRelValue() {
		return relValue;
	}

	public void setRelValue(String relValue) {
		this.relValue = relValue;
	}

	// ==========

}
