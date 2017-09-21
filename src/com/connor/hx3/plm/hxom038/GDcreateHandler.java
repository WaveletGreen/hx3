package com.connor.hx3.plm.hxom038;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.menus.CommandContributionItem;

import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class GDcreateHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// TODO Auto-generated method stub
		AbstractAIFUIApplication app = AIFUtility.getCurrentApplication();
		TCSession session = (TCSession) app.getSession();

		try {
			org.eclipse.swt.widgets.Event event = (org.eclipse.swt.widgets.Event) arg0
					.getTrigger();
			org.eclipse.ui.menus.CommandContributionItem item = (CommandContributionItem) event.widget
					.getData();
			org.eclipse.jface.action.MenuManager menumanager = (MenuManager) item
					.getParent();
			String menuId = menumanager.getId();

			System.out.println("0000=>" + menumanager.getId());
			new GDcreateDialog(arg0.getCommand().getId(), app, session);
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
