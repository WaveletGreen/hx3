package com.connor.hx3.plm.hxom041;

import com.teamcenter.rac.aif.AbstractAIFOperation;

public class ReportOpreation  extends AbstractAIFOperation{
	
	private ReportDialog dialog;
	public ReportOpreation(ReportDialog dialog ){
		this.dialog = dialog;
	}

	@Override
	public void executeOperation() throws Exception {
		// TODO Auto-generated method stub
		dialog.okEvent();
	}
	

}
