package com.connor.hx3.plm.hxom041;


import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aif.AbstractAIFCommand;

public class ReportCommand extends AbstractAIFCommand {

	private AbstractAIFApplication app;
	
	public ReportCommand (AbstractAIFApplication app){
		this.app = app;
	}

	@Override
	public void executeModal() throws Exception {
		// TODO Auto-generated method stub
		ReportDialog dialog = new ReportDialog(this.app);
		new Thread(dialog).start();
		super.executeModal();
	}

}
