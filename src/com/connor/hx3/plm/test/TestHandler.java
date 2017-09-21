package com.connor.hx3.plm.test;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * @copyRight 杭州康勒科技有限公司
 * @author 作者 E-mail:hub@connor.net.cn
 * @date 创建时间：2016-10-24 下午11:13:55
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
