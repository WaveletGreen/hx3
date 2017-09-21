package com.connor.hx3.plm.hxom002;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentPseudoFolder;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.MessageBox;

public class AutocodeHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// TODO Auto-generated method stub
		System.out.println("");
		AbstractAIFApplication app = AIFUtility.getCurrentApplication();
		TCSession session = (TCSession) app.getSession();
		InterfaceAIFComponent targetComp = app.getTargetComponent();
		if (targetComp instanceof TCComponentItemRevision) {

		} else if (targetComp instanceof TCComponentPseudoFolder) {
			try {
				targetComp = ((TCComponentPseudoFolder) targetComp)
						.getReferenceProperty("owning_object");
			} catch (TCException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!(targetComp instanceof TCComponentItemRevision)) {
			MessageBox.post("请选择版本或者版本下的伪文件夹操作", "失败", MessageBox.ERROR);
			return null;
		}
		String type = targetComp.getType();
		if (!(type.equals("HX3_ZPTRevision") || type.equals("HX3_LJTRevision") || type
				.equals("HX3_GYTRevision"))) {
			MessageBox.post("请选择【零组件版本、工艺图版本】或者其下的伪文件夹操作", "失败",
					MessageBox.ERROR);
			return null;
		}
		AuocodeDialog dialog = new AuocodeDialog(app, session);

		new Thread(dialog).start();
		return null;
	}

}
