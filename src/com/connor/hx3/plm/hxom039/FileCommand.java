package com.connor.hx3.plm.hxom039;

import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aif.AbstractAIFCommand;

public class FileCommand extends AbstractAIFCommand {

	private AbstractAIFApplication app;
	
	public FileCommand(AbstractAIFApplication app){
		this.app = app;
	}
	@Override
	public void executeModal() throws Exception {
		// TODO Auto-generated method stub
		FileDialog dialog = new FileDialog(this.app);
		new Thread(dialog).start();
		super.executeModal();
	}

}
