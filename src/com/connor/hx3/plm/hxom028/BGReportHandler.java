package com.connor.hx3.plm.hxom028;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * @copyRight 杭州康勒科技有限公司
 * @author 作者 E-mail:hub@connor.net.cn
 * @date 创建时间：2016-10-25 下午11:24:21
 * @version v1.0
 * @parameter
 * @since
 * @return
 */

public class BGReportHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		try {
			BGReportCommand command = new BGReportCommand();
			command.executeModal();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
