package com.connor.hx3.plm.hxom039;

import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aif.common.actions.AbstractAIFAction;

public class FileAction extends AbstractAIFAction {


	private AbstractAIFApplication app;
	
	public FileAction(AbstractAIFApplication arg0, String arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
		this.app = arg0;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			FileCommand command = new FileCommand(this.app);
			command.executeModal();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
