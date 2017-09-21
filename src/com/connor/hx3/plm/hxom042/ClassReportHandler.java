package com.connor.hx3.plm.hxom042;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCSession;

public class ClassReportHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// TODO Auto-generated method stub
		
		 AbstractAIFApplication app = AIFUtility.getCurrentApplication();
		 TCSession session =(TCSession) app.getSession();
		 
		 ClassReportOperation operation = new ClassReportOperation(session, app);
		 
		 session.queueOperation(operation);
		
		
		return null;
	}

}
