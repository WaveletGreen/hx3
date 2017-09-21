package com.connor.updaterev.handlers;

import java.util.ArrayList;
import java.util.List;

import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aif.common.actions.AbstractAIFAction;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentPseudoFolder;
import com.teamcenter.rac.util.MessageBox;



public class UpdateRevAction extends AbstractAIFAction {

	AbstractAIFApplication app;
	public UpdateRevAction(AbstractAIFApplication arg0, String arg1) {
		super(arg0, arg1);
		app=arg0;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		StringBuffer msgSB = new StringBuffer();
		// TODO Auto-generated method stub
		if(!(app.getTargetComponent() instanceof TCComponentPseudoFolder)){
			msgSB.append("请选择[工程更改通知单版本]的[更改前]为文件夹\n");
			MessageBox.post(msgSB.toString(),"",2);
			return;
		}
		try {
			TCComponentPseudoFolder folder=(TCComponentPseudoFolder) app.getTargetComponent();
			TCComponentItemRevision itemRev=(TCComponentItemRevision) folder.whereReferenced()[0].getComponent();
			if(!itemRev.getType().equals("HX3_GCGGTZDRevision")){
				msgSB.append("请选择[工程更改通知单版本]的[更改前]为文件夹\n");
				MessageBox.post(msgSB.toString(),"",2);
				
				return;
			}
			if(!folder.equals(itemRev.getChildren("HX3_ggq")[0].getComponent())){
				
				msgSB.append("请选择[工程更改通知单版本]的[更改前]为文件夹\n");
				MessageBox.post(msgSB.toString(),"",2);
				return;
			}
			AIFComponentContext children[]=folder.getChildren();
			TCComponentPseudoFolder folder2=(TCComponentPseudoFolder) itemRev.getChildren("HX3_ggh")[0].getComponent();
			AIFComponentContext children2[]=folder2.getChildren();
			List<String> id=new ArrayList<String>();
			for(int i=0;i<children2.length;i++){
				id.add(((TCComponentItemRevision)children2[i].getComponent()).getProperty("item_id"));
			}
			
			for(int i=0;i<children.length;i++){
				TCComponentItemRevision tempItem=(TCComponentItemRevision) children[i].getComponent();
				if(id.contains(tempItem.getProperty("item_id")))
				{
					msgSB.append("["+tempItem.getStringProperty("object_string")+"]已经存在[变更后]关系中，不需要升级版本\n");
					continue;
				}
				msgSB.append("["+tempItem.getStringProperty("object_string")+"]升级版本成功\n");
				itemRev.add("HX3_ggh", tempItem.saveAs(null));
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
