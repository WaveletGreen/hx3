package com.connor.hx3.plm.hxom011;

import java.util.ArrayList;
import java.util.List;

import com.connor.hx3.plm.util.HxomMethodUtil;
import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aif.AbstractAIFCommand;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.MessageBox;

public class Hxom011Command extends AbstractAIFCommand {

	private AbstractAIFApplication app;
	private StringBuffer errorMsgSb;
	public static final String JF3_BOM_QUERY_NO = "bl_sequence_no";
	public static final String JF3_BOM_COUNT_NO = "bl_quantity";
	private Hxom011Bean bean;
	private Boolean isOk = true;
	private TCSession session;

	public Hxom011Command(AbstractAIFApplication app, TCSession session) {
		this.app = app;
		this.errorMsgSb = new StringBuffer();
		this.bean = new Hxom011Bean();
		this.session = session;
	}

	@Override
	public void executeModal() throws Exception {
		execCommond();
	}
	

	public void execCommond() {

		InterfaceAIFComponent comp = this.app.getTargetComponent();
		if (comp instanceof TCComponentItemRevision) {
			checkWL((TCComponentItemRevision) comp);
			if (!isOk) {
				MessageBox.post(this.errorMsgSb.toString(), "错误",
						MessageBox.ERROR);
			} else {

				Hxom011Dialog dialog = new Hxom011Dialog(this.bean, this.app,
						this.session);
				new Thread(dialog).start();
				// setRunnable(dialog);
			}
		} else {

			MessageBox.post("请选择组件版本后重新操作", "错误", MessageBox.ERROR);
		}
	}

	public void checkWL(TCComponentItemRevision rev) {
		if (rev == null) {
			return;
		}
		TCComponentBOMLine line = HxomMethodUtil.getTopLineByRev(rev);
		// Hxom011Bean bean = new Hxom011Bean();
		bean.setParentBean(null);
		checkBomLine(line, bean);
	}

	public void checkBomLine(TCComponentBOMLine line, Hxom011Bean bean) {
		try {
			if (line == null) {
				return;
			}

			List<Hxom011Bean> materialList = new ArrayList<>();

			List<Hxom011Bean> childBeanS = new ArrayList<>();
			bean.setchildBeanS(childBeanS);

			bean.setRev(line.getItemRevision());
			bean.setQTY(line.getProperty(JF3_BOM_COUNT_NO));
			bean.setQUERY(line.getProperty(JF3_BOM_QUERY_NO));

			AIFComponentContext[] aifContext = line.getItemRevision()
					.whereReferencedByTypeRelation(
							new String[] { "HX3_WLRevision" },
							new String[] { "TC_Is_Represented_By" });
			if (aifContext == null || aifContext.length == 0) {
				this.isOk = false;
				this.errorMsgSb.append("[" + bean.getDisplayName()
						+ "]没有关联物料\n");
			} else {
				GOTO_1 :
				for (AIFComponentContext context : aifContext) {
					Hxom011Bean beanT = new Hxom011Bean();
					beanT.setRev((TCComponentItemRevision) context
							.getComponent());
					beanT.setDesignBean(bean);
					for(int i = 0;i < materialList.size();){
						//if(materialList.get(i).getRev().equals(beanT.toString())){
						//	continue GOTO_1;
						//}
						if(materialList.get(i).itemID.endsWith(beanT.itemID)){
							if(materialList.get(i).creationDate.before(beanT.creationDate)){
								materialList.remove(i);
								materialList.add(i, beanT);
							}
							continue GOTO_1;
						}
						i++;
					}
					materialList.add(beanT);
				}

			}
			bean.setMaterialRevS(materialList);

			AIFComponentContext[] contexts = line.getChildren();
			for (AIFComponentContext context : contexts) {
				TCComponentBOMLine line2 = (TCComponentBOMLine) context
						.getComponent();
				Hxom011Bean childBean = new Hxom011Bean();
				childBean.setParentBean(bean);
				childBeanS.add(childBean);
				checkBomLine(line2, childBean);
			}
		} catch (TCException e) {
			e.printStackTrace();
		}
	}

}
