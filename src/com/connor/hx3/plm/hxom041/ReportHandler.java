package com.connor.hx3.plm.hxom041;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aifrcp.AIFUtility;

public class ReportHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		AbstractAIFApplication app = AIFUtility.getCurrentApplication();

		ReportAction action = new ReportAction(app, null);

		new Thread(action).start();

		return null;
	}

}
