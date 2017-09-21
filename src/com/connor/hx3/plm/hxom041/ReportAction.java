package com.connor.hx3.plm.hxom041;


import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aif.common.actions.AbstractAIFAction;

public class ReportAction extends AbstractAIFAction {

	private AbstractAIFApplication app;
	public ReportAction(AbstractAIFApplication arg0, String arg1) {
		super(arg0, arg1);
		this.app = arg0;
	}

	@Override
	public void run() {
		try {
			ReportCommand command = new ReportCommand(this.app);
			command.executeModal();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
