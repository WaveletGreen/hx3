package com.connor.hx3.plm.test;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * @copyRight ���ݿ��տƼ����޹�˾
 * @author ���� E-mail:hub@connor.net.cn
 * @date ����ʱ�䣺2016-10-24 ����11:13:55
 * @version v1.0
 * @parameter
 * @since
 * @return
 */

public class TestHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// TODO Auto-generated method stub

		TestDialog dialog = new TestDialog();
		new Thread(dialog).start();

		return null;
	}
}
