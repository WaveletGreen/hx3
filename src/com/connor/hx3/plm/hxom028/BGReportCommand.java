package com.connor.hx3.plm.hxom028;

import com.teamcenter.rac.aif.AbstractAIFCommand;

/**
 * @copyRight ���ݿ��տƼ����޹�˾
 * @author ���� E-mail:hub@connor.net.cn
 * @date ����ʱ�䣺2016-10-25 ����11:24:40
 * @version v1.0
 * @parameter
 * @since
 * @return
 */

public class BGReportCommand extends AbstractAIFCommand {

	public BGReportCommand() {

	}

	@Override
	public void executeModal() throws Exception {
		// TODO Auto-generated method stub
		BGReportDialog dialog = new BGReportDialog();
		new Thread(dialog).start();

	}

}
