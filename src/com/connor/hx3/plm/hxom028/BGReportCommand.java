package com.connor.hx3.plm.hxom028;

import com.teamcenter.rac.aif.AbstractAIFCommand;

/**
 * @copyRight 杭州康勒科技有限公司
 * @author 作者 E-mail:hub@connor.net.cn
 * @date 创建时间：2016-10-25 下午11:24:40
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
