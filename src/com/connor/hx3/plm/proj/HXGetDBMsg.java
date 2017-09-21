package com.connor.hx3.plm.proj;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.kernel.TCUserService;
import com.teamcenter.rac.util.MessageBox;

public class HXGetDBMsg extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// TODO Auto-generated method stub

		TCSession session = (TCSession) AIFUtility.getCurrentApplication()
				.getSession();
		TCUserService service = session.getUserService();

		try {
			service.call("HX_GetProjMsg", new Object[] { "true" });
			MessageBox.post("项目看板更新完毕", "信息", MessageBox.INFORMATION);
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MessageBox.post("项目看板信息更新错误", "错误", MessageBox.ERROR);
		}

		return null;
	}

}
