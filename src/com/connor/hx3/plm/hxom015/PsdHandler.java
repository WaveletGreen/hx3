package com.connor.hx3.plm.hxom015;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.menus.CommandContributionItem;

import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCSession;

public class PsdHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {

		org.eclipse.swt.widgets.Event event = (Event) arg0.getTrigger();

		org.eclipse.ui.menus.CommandContributionItem item = (CommandContributionItem) event.widget
				.getData();

		org.eclipse.jface.action.MenuManager menumanager = (MenuManager) item
				.getParent();

		System.out.println(menumanager.getMenuText());

		// TODO Auto-generated method stub
		AbstractAIFUIApplication app = AIFUtility.getCurrentApplication();
		TCSession session = (TCSession) app.getSession();

		try {
			new PsdDialog(menumanager.getMenuText(), app, session);
		} catch (NotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
