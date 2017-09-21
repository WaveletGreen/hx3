package com.connor.hx3.plm.hxom039;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aifrcp.AIFUtility;

public class FileHandler extends AbstractHandler {

	
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// TODO Auto-generated method stub
		AbstractAIFApplication app = AIFUtility.getCurrentApplication();

		FileAction action = new FileAction(app, null);

		new Thread(action).start();
		return null;
	}

}
