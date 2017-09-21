package com.connor.hx3.plm.hxomAPQP;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.teamcenter.rac.aif.AIFDesktop;
import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.commands.newitem.NewItemCommand;
import com.teamcenter.rac.kernel.TCComponent;

public class APQPHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// TODO Auto-generated method stub
		String commandID = arg0.getCommand().getId();

		try {
			AbstractAIFUIApplication app = AIFUtility.getCurrentApplication();
			InterfaceAIFComponent comp = app.getTargetComponent();
			TCComponent comp2 = (TCComponent) comp;
			String grm = comp2.getDefaultPasteRelation();
			NewItemCommand commond = new NewItemCommand(
					AIFDesktop.getActiveDesktop(), app, commandID, grm, false);
			commond.executeModal();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
