package com.connor.hx3.plm.hxom011;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCSession;

public class Hxom011Handler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {

		try {
			AbstractAIFApplication app = AIFUtility.getCurrentApplication();
			TCSession session = (TCSession) app.getSession();
			Hxom011Command command = new Hxom011Command(app, session);
			command.executeModal();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
