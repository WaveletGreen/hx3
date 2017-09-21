package com.connor.hx3.plm.hxom011;

import java.util.ArrayList;
import java.util.List;

import com.connor.hx3.plm.util.HxomMethodUtil;
import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.util.MessageBox;

public class Hxom011Operation extends AbstractAIFOperation {

	private Hxom011Bean firstBean;
	private Hxom011Dialog dialog;

	public Hxom011Operation(Hxom011Bean firstBean, Hxom011Dialog dialog) {
		this.firstBean = firstBean;
		this.dialog = dialog;

	}

	@Override
	public void executeOperation() throws Exception {
		try {
			HxomMethodUtil.setByPass(true);
		} catch (TCException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			createMaterialBom(this.firstBean);
			MessageBox.post("����BOMת���ɹ�", "�ɹ�", MessageBox.INFORMATION);
			dialog.disposeDialog();
			dialog.dispose();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MessageBox.post("����BOMת��ʧ��:\n" + e.getLocalizedMessage(), "ʧ��",
					MessageBox.ERROR);

			dialog.showDialog();

		}
		try {
			HxomMethodUtil.setByPass(false);
		} catch (TCException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	public void createMaterialBom(Hxom011Bean bean) throws TCException {
		if (bean == null) {
			return;
		}
		// ��ȡѡ�е�����
		TCComponentItemRevision materialRev = bean.getSelectRev();
		// �����Ѿ����������ϵ�bom
		if (HxomMethodUtil.isRevBomReleased(materialRev)) {
			return;
		}
		//
		List<Hxom011Bean> childBeanS = bean.getchildBeanS();
		if (childBeanS == null) {
			return;
		}
		List<TCComponentItemRevision> childrenList = new ArrayList<>();
		List<String> queryNoList = new ArrayList<>();
		List<String> countNoList = new ArrayList<>();
		// �BOM
		for (int i = 0; i < childBeanS.size(); i++) {
			Hxom011Bean childBean = childBeanS.get(i);
			// ��ȡ�ӵ�ѡ�������
			TCComponentItemRevision childSelectMaterialRev = childBean
					.getSelectRev();
			childrenList.add(childSelectMaterialRev);
			queryNoList.add(childBean.getQUERY());
			countNoList.add(childBean.getQTY());
			createMaterialBom(childBean);
		}

		HxomMethodUtil.createBom(materialRev, childrenList, queryNoList,
				countNoList);

		// for (int i = 0; i < childBeanS.size(); i++) {
		// }

	}

}
