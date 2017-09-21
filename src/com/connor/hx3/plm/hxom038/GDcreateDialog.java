package com.connor.hx3.plm.hxom038;

import java.util.ArrayList;

import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCPreferenceService;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.MessageBox;

public class GDcreateDialog extends AbstractAIFDialog {
	private AbstractAIFUIApplication app;
	private TCSession session;
	private String id;
	private InterfaceAIFComponent comment;
	private ArrayList<TCComponentFolder> folderList;
	private TCComponentFolder folder;
	private String folderUid;
	private TCComponent comp;
	private String lclxName;
	private String cxkey;

	public GDcreateDialog(String id, AbstractAIFUIApplication app,
			TCSession session) throws TCException {
		this.id = id;
		this.app = app;
		this.session = session;
		init();
	}

	public void init() throws TCException {
		app = AIFUtility.getCurrentApplication();
		comment = app.getTargetComponent();
		if (comment instanceof TCComponentFolder) {
			TCComponentFolder tarFolder = (TCComponentFolder) comment;

			TCProperty pro1 = tarFolder.getTCProperty("fnd0MyWorkflowTasks");// fnd0MyWorkflowTasks
			if (pro1 != null) {
				TCComponent[] com1 = pro1.getReferenceValueArray();
				System.out.println("com1->1");
				if (com1.length != 0) {
					for (int i = 0; i < com1.length; i++) {
						TCProperty pro2 = com1[i]
								.getTCProperty("project_task_attachments");// project_task_attachments
						if (pro2 != null) {
							TCComponent[] com2 = pro2.getReferenceValueArray();
							System.out.println("com2->2");
							if (com2.length != 0) {
								for (int j = 0; j < com2.length; j++) {
									TCProperty pro3 = com2[j]
											.getTCProperty("schedule_tag");// schedule_tag
									if (pro3 != null) {
										TCComponent com3 = pro3
												.getReferenceValue();
										System.out.println("com3->3");

										AIFComponentContext[] contexts = com3
												.whereReferenced();
										if (contexts == null
												|| contexts.length == 0) {
											MessageBox.post("该时间表没有归档在项目下",
													"提示", MessageBox.ERROR);
											this.dispose();
										}
										for (int k = 0; k < contexts.length; k++) {
											InterfaceAIFComponent context = contexts[k]
													.getComponent();
											if (context instanceof TCComponentFolder
													&& (context.getType())
															.equals("HX3_GDJGSJ")) {

												folderList = new ArrayList<TCComponentFolder>();
												folderList
														.add((TCComponentFolder) context);
												folder = folderList.get(0);
												folderUid = folder.getUid();// 得到文件夹的uid
												comp = session
														.stringToComponent(folderUid);// puid转换成对象
												TCProperty lclx = folder
														.getTCProperty("hx3_lclx");
												lclxName = lclx
														.getDisplayValue()
														.toString();

												System.out.println(folderUid);
												System.out.println(lclxName);
											}
										}
										TCPreferenceService service = ((TCSession) AIFUtility
												.getCurrentApplication()
												.getSession())
												.getPreferenceService();
										String[] ColVal = service
												.getStringArray(
														TCPreferenceService.TC_preference_site,
														"FindFolderinAPQPStructure");
										if (ColVal == null
												|| ColVal.length == 0) {
											MessageBox
													.post("首选项："
															+ "FindFolderinAPQPStructure"
															+ "未设置值或未定义，请联系系统管理员！",
															"提示",
															MessageBox.WARNING);
											this.disposeDialog();
										}
										for (int a = 0; a < ColVal.length; a++) {
											if (ColVal[a].contains("OTS样件评审|"
													+ lclxName)) {
												cxkey = ColVal[a]
														.substring(ColVal[a]
																.indexOf(":") + 1);// 得到首选项中"："后面的值
												System.out.println(":后面的值———>"
														+ cxkey);
											}
										}

									} else {
										MessageBox.post("请选择时间表任务下的交付文件夹",
												"ERROR", MessageBox.ERROR);
									}
								}
							} else {
								MessageBox.post("请选择时间表任务下的交付文件夹", "ERROR",
										MessageBox.ERROR);
							}
						} else {
							MessageBox.post("请选择时间表任务下的交付文件夹", "ERROR",
									MessageBox.ERROR);
						}
					}
				} else {
					MessageBox.post("请选择时间表任务下的交付文件夹", "ERROR",
							MessageBox.ERROR);
				}
			} else {
				MessageBox.post("请选择时间表任务下的交付文件夹", "ERROR", MessageBox.ERROR);
			}
		}

	}

}
