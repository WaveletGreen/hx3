package com.connor.hx3.plm.hxom028;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * @copyRight ���ݿ��տƼ����޹�˾
 * @author ���� E-mail:hub@connor.net.cn
 * @date ����ʱ�䣺2016-10-25 ����11:24:21
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
