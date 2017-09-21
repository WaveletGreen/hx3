package com.connor.hx3.plm.hxom004;

import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aif.AbstractAIFCommand;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCSession;

public class CreateProductionStructCommand extends AbstractAIFCommand {
	private String commandName;

	public CreateProductionStructCommand(String commandName) {
		this.commandName = commandName;
	}

	@Override
	public void executeModal() throws Exception {
		// TODO Auto-generated method stub
		AbstractAIFApplication app = AIFUtility.getCurrentApplication();
		TCSession session = (TCSession) app.getSession();
		APQPNewProductDialog dialog = new APQPNewProductDialog(commandName,
				app, session);
		// new Thread(dialog).start();
		super.executeModal();
	}

}
