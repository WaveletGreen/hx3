package com.connor.hx3.plm.hxom018;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.MessageBox;

public class QTXHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent executionevent)
			throws ExecutionException {
		// TODO Auto-generated method stub
		AbstractAIFUIApplication app = AIFUtility.getCurrentApplication();
		TCSession session = (TCSession) app.getSession();
		try {
			new QTXJCDialog(app, session);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MessageBox.post("���ɽ��������Լ���ʧ��", "����", MessageBox.ERROR);

		}
		return null;
	}

}
