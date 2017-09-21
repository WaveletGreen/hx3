package com.connor.hx3.plm.hxom041;

import com.teamcenter.rac.util.Registry;


public class TransfProgressBarThread extends Thread {
	private CUSTProgressBar bar;
	private String title;
	private Registry reg;

	public TransfProgressBarThread(String title, String message) {
		this.title = title;
		this.bar = new CUSTProgressBar(message);
	}

	

	public void run() {
		this.bar.setTitle(this.title);
		this.bar.initUI();
	}

	public void stopBar() {
		this.bar.setBool(true);
	}

	public void setBarMsg(String str) {
		this.bar.setTitle(str);
	}
}