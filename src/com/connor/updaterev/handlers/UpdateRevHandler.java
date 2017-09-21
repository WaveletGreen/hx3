package com.connor.updaterev.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.teamcenter.rac.aifrcp.AIFUtility;

public class UpdateRevHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// TODO Auto-generated method stub
		new Thread(new UpdateRevAction(AIFUtility.getCurrentApplication(), null)).run();
		return null;
	}

}