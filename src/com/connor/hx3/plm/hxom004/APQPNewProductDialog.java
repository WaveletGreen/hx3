package com.connor.hx3.plm.hxom004;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.connor.hx3.plm.hxom016.MethodUtil;
import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.common.create.BOCreateDefinitionFactory;
import com.teamcenter.rac.common.create.IBOCreateDefinition;
import com.teamcenter.rac.common.create.SOAGenericCreateHelper;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentFolderType;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCComponentSchedule;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCPreferenceService;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.schedule.soainterface.CopyScheduleRunner;
import com.teamcenter.rac.util.MessageBox;

public class APQPNewProductDialog extends AbstractAIFDialog implements
		ActionListener {
	private JLabel nameLabel;
	private JTextField nameField;
	private JButton okBtn;
	private JButton celBtn;
	private TCSession session;
	private AbstractAIFApplication app;
	private TCComponentFolder parentFolder;
	private TCComponentFolder childFolder;
	private InterfaceAIFComponent comment;
	private TCComponentFolderType folderType;
	private TCComponentFolder namefolder;
	// private String itemType;
	private String itemName;
	private String prefName = "hxScheduleTemplateUID";
	private String puid;
	private TCComponentFolder compFolder = null;
	private TCComponentSchedule[] atccomponentschedule;
	private TCComponentSchedule tccomponent;
	private String commandName;
	private HashMap<String, String> mappping;

	public APQPNewProductDialog(String dialogName, AbstractAIFApplication app,
			TCSession session) throws Exception {
		super(false);
		this.app = app;
		this.session = session;
		this.commandName = dialogName;
		this.mappping = getAllApqpOptions();
		if (this.mappping == null || this.mappping.size() == 0) {

			MessageBox.post("��������ѡ�HXAPQPStandardmapping��", "��ʾ",
					MessageBox.ERROR);
			return;
		}
		comment = app.getTargetComponent();
		if (comment == null) {
			MessageBox.post("��ѡ��HX3_XXM����", "��ʾ", MessageBox.ERROR);
			return;
		}
		this.itemName = comment.getProperty("owning_object");
		System.out.println("");
		if ((comment instanceof TCComponentItem)) {
			String relType = comment.getType();
			if (relType.equals("HX3_XXM")) {
				// itemType = comment.getProperty("object_type");
				// if (itemType.equals("�½���Ŀ")) {
				init();
				// } else {
				// System.out.println("ѡ�ж�������Ͳ����½���Ŀ");
				// }
			} else {
				MessageBox.post("��ѡ��HX3_XXM����", "��ʾ", MessageBox.ERROR);
			}
		} else {
			MessageBox.post("��ѡ��" + this.itemName, "��ʾ", MessageBox.ERROR);
		}
	}

	/**
	 * ��ȡ������ϵͳ��APQP
	 * 
	 * @return
	 */
	public HashMap<String, String> getAllApqpOptions() {
		HashMap<String, String> mapping = MethodUtil.getPrefStrArray(
				"HXAPQPStandardmapping", ":");
		return mapping;
	}

	public void init() {
		this.setTitle("ͨ��APQPģ���½���Ʒ");
		this.setPreferredSize(new Dimension(300, 120));
		this.setResizable(false);
		this.nameLabel = new JLabel("��Ʒ����");
		this.nameField = new JTextField(18);
		this.okBtn = new JButton("ȷ��");
		this.okBtn.addActionListener(this);

		this.celBtn = new JButton("ȡ��");
		this.celBtn.addActionListener(this);

		JPanel rootPanel = new JPanel(new BorderLayout());
		JPanel topPanel = new JPanel(new FlowLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout());

		topPanel.add(nameLabel);
		topPanel.add(nameField);

		buttonPanel.add(okBtn);
		buttonPanel.add(new JLabel("   "));
		buttonPanel.add(celBtn);

		rootPanel.add(topPanel, BorderLayout.CENTER);
		rootPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.add(rootPanel, "Center");
		this.pack();
		this.centerToScreen();
		this.showDialog();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(this.okBtn)) {
			// getSource() ���صĵ�ǰ������ָ��Ķ��󣬰��������������Ϣ

			if (this.nameField.getText().trim().isEmpty()) {
				// ���textID���ı��ǲ���ȫ��Ϊ�ո���߸ɴ���ǿ��ַ���
				// trim()������ȥ���ַ�����ͷ��β���ϵ����пո��
				MessageBox.post("�������Ŀ���Ʋ���Ϊ�գ�", "����", MessageBox.ERROR);
				return;
			}
			try {
				app = AIFUtility.getCurrentApplication();
				session = (TCSession) app.getSession();
				folderType = (TCComponentFolderType) session
						.getTypeComponent("Folder");
				namefolder = createFolder("HX3_GDJGSJ", nameField.getText());// folderType.create(nameField.getText(),
																				// null,
				// "HX3_GDJGSJ");

				if (namefolder != null) {
					InterfaceAIFComponent comment = app.getTargetComponent();
					TCComponentItem tar = (TCComponentItem) comment;

					// ͨ��puid�ҵ�ʱ���ģ����ļ���
					MethodUtil method = new MethodUtil();
					puid = MethodUtil.getPrefStr(prefName);
					TCComponent comp = session.stringToComponent(puid);
					// puidת���ɶ���
					if (comp == null) {
						MessageBox.post("��ʱ���ģ��⡯�ļ��в�����", "ʧ��",
								MessageBox.ERROR);
					}
					if (comp instanceof TCComponentFolder) {
						// �ж�comp�ǲ����ļ���
						compFolder = (TCComponentFolder) comp;
						// ǿ��ת��comp

						// ��ʱ���ģ����ļ����еõ�ʱ����object_name
						TCComponent[] comps = compFolder
								.getReferenceListProperty("contents");
						if (comps != null) {
							for (int i = 0; i < comps.length; i++) {
								if (comps[i] instanceof TCComponentSchedule) {
									tccomponent = (TCComponentSchedule) comps[i];
									TCComponentSchedule schedule = null;
									if (tccomponent.getProperty("object_name")
											.equals(commandName)) {
										// TCComponentType scheduleType =
										// tccomponent.getTypeComponent();
										String scheduleType = tccomponent
												.getType();
										TCComponentItemType itemType = (TCComponentItemType) session
												.getTypeComponent("Item");
										if (itemType == null) {
											MessageBox.post("û���ҵ�Item",
													"Error", MessageBox.ERROR);
										}
										String id = itemType.getNewID();
										String name = nameField.getText();
										String description = null;
										schedule = create(tccomponent, id,
												name, description);
										if (schedule == null) {
											MessageBox.post("ʱ�����ʧ��", "����",
													MessageBox.ERROR);
											return;
										} else {
											namefolder
													.add("contents", schedule);
										}
										namefolder.refresh();
									}
								}
							}
						} else {
							System.out.println("comps = null");
						}
					}
					tar.add("HX3_kfcp", namefolder);// HX3_kfcp
					tar.refresh();
					// MessageBox.post("�����ļ�����.....���Ե�...","��ʾ",
					// MessageDialog.NONE);
				} else {
					MessageBox.post("����ʧ��", "����", MessageBox.ERROR);
					dispose();
					return;
				}

				TCPreferenceService service = ((TCSession) AIFUtility
						.getCurrentApplication().getSession())
						.getPreferenceService();
				if (!mappping.containsKey(this.commandName)) {
					MessageBox.post("HXAPQPStandardmapping ��ѡ����û�����á�"
							+ this.commandName + "������ѡ��", "����",
							MessageBox.ERROR);
					return;
				}
				String opName = mappping.get(this.commandName);

				String[] ColVal = service.getStringArray(
						TCPreferenceService.TC_preference_site, opName);
				if (ColVal == null || ColVal.length == 0) {
					MessageBox.post("��ѡ�" + "HXAPQPStandardAll"
							+ "δ����ֵ��δ���壬����ϵϵͳ����Ա��", "��ʾ", MessageBox.WARNING);
					System.out.println("all");
					return;
				}

				for (int k = 0; k < ColVal.length; k++) {
					Boolean isOK2 = ColVal[k].endsWith("*");
					if (isOK2) {
						System.out.println("Ϊ���ļ���");
						String str3 = ColVal[k];
						str3 = str3.replace("*", "");
						// parentFolder = folderType.create(str3, null,
						// "Folder");//HX3_GDJGSJ
						String typeStr = "HX3_GDJG";
						//String nameStr = str3;
						String[] typeAndName = str3.split(":");
						if(typeAndName.length ==2){
							typeStr = typeAndName[0];
							str3 = typeAndName[1];
						}
						
						parentFolder = createFolder(typeStr, str3);
						namefolder.setStringProperty("hx3_lclx", commandName);// ����hx3_lclx��д��
						namefolder.add("contents", parentFolder);
						namefolder.refresh();
					} else {
						System.out.println("Ϊ���ļ���");
						// childFolder = folderType.create(ColVal[k], null,
						// "Folder");//HX3_GDJG
						String str3 = ColVal[k];
						String typeStr = "HX3_GDJG";
						//String nameStr = str3;
						String[] typeAndName = str3.split(":");
						if(typeAndName.length ==2){
							typeStr = typeAndName[0];
							str3 = typeAndName[1];
						}
						
						childFolder = createFolder(typeStr, str3);
						parentFolder.add("contents", childFolder);
						parentFolder.refresh();
					}
				}
			} catch (TCException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.dispose();
		} else {
			this.dispose();
		}
	}

	/**
	 * ����ָ�����ͺ����Ƶ��ļ���
	 * 
	 * @param Type
	 *            ����
	 * @param folderName
	 *            ����
	 * @return
	 */
	public static TCComponentFolder createFolder(String Type, String folderName) {
		TCSession session = (TCSession) AIFUtility.getCurrentApplication()
				.getSession();
		TCComponentFolder folder = null;
		try {
			IBOCreateDefinition ibocreatedefinition = BOCreateDefinitionFactory
					.getInstance().getCreateDefinition(session, Type);
			com.teamcenter.rac.common.create.CreateInstanceInput cii = new com.teamcenter.rac.common.create.CreateInstanceInput(
					ibocreatedefinition);
			List list = new ArrayList();
			list.add(cii);
			ArrayList arraylist = new ArrayList(0);
			arraylist.addAll(list);
			List list1 = null;
			list1 = SOAGenericCreateHelper.create(session, ibocreatedefinition,
					arraylist);
			if (list1 != null && list1.size() > 0) {
				TCComponent tccomponent = (TCComponent) list1.get(0);
				folder = (TCComponentFolder) tccomponent;
				folder.setProperty("object_name", folderName);
				// session.getUser().getNewStuffFolder().add("contents",
				// folder);
			}
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return folder;
	}

	/**
	 * ����ʱ���
	 * 
	 * @param tccomponent
	 *            ģ��
	 * @param id
	 *            ʱ���ID
	 * @param name
	 *            ʱ�������
	 * @param description
	 *            ʱ�������
	 */
	public TCComponentSchedule create(TCComponentSchedule tccomponent,
			String id, String name, String description) {
		ArrayList arraylist = new ArrayList();
		TCComponentSchedule schedule = null;
		com.teamcenter.services.rac.projectmanagement._2011_06.ScheduleManagement.ScheduleCopyOptionsContainer schedulecopyoptionscontainer = new com.teamcenter.services.rac.projectmanagement._2011_06.ScheduleManagement.ScheduleCopyOptionsContainer();
		schedulecopyoptionscontainer.scheduleToCopy = tccomponent;
		schedulecopyoptionscontainer.id = id;
		schedulecopyoptionscontainer.name = name;
		schedulecopyoptionscontainer.description = description;
		com.teamcenter.services.rac.projectmanagement._2008_06.ScheduleManagement.StringValContainer stringvalcontainer1 = new com.teamcenter.services.rac.projectmanagement._2008_06.ScheduleManagement.StringValContainer();
		stringvalcontainer1.key = "is_template";
		stringvalcontainer1.value = Boolean.valueOf(false).toString();
		stringvalcontainer1.type = 5;
		arraylist.add(stringvalcontainer1);
		if (arraylist != null && arraylist.size() > 0) {
			com.teamcenter.services.rac.projectmanagement._2008_06.ScheduleManagement.StringValContainer astringvalcontainer1[] = new com.teamcenter.services.rac.projectmanagement._2008_06.ScheduleManagement.StringValContainer[arraylist
					.size()];
			astringvalcontainer1 = (com.teamcenter.services.rac.projectmanagement._2008_06.ScheduleManagement.StringValContainer[]) arraylist
					.toArray(astringvalcontainer1);
			schedulecopyoptionscontainer.stringValueContainer = astringvalcontainer1;
		}
		com.teamcenter.services.rac.projectmanagement._2011_06.ScheduleManagement.SchMgtOptions schmgtoptions = new com.teamcenter.services.rac.projectmanagement._2011_06.ScheduleManagement.SchMgtOptions();
		com.teamcenter.services.rac.projectmanagement._2011_06.ScheduleManagement.SchMgtLogicalOption schmgtlogicaloption = new com.teamcenter.services.rac.projectmanagement._2011_06.ScheduleManagement.SchMgtLogicalOption();
		schmgtlogicaloption.name = "resetWork";
		schmgtlogicaloption.value = true;
		com.teamcenter.services.rac.projectmanagement._2011_06.ScheduleManagement.SchMgtLogicalOption aschmgtlogicaloption[] = { schmgtlogicaloption };
		schmgtoptions.logicalOptions = aschmgtlogicaloption;
		schedulecopyoptionscontainer.options = schmgtoptions;
		CopyScheduleRunner copyschedulerunner = new CopyScheduleRunner(
				new com.teamcenter.services.rac.projectmanagement._2011_06.ScheduleManagement.ScheduleCopyOptionsContainer[] { schedulecopyoptionscontainer });
		int j1 = copyschedulerunner.execute();
		if (j1 == 0) {
			Object obj2 = copyschedulerunner.getResults();
			if (obj2 instanceof com.teamcenter.services.rac.projectmanagement._2008_06.ScheduleManagement.ScheduleCopyResponses) {
				com.teamcenter.services.rac.projectmanagement._2008_06.ScheduleManagement.ScheduleCopyResponses schedulecopyresponses = (com.teamcenter.services.rac.projectmanagement._2008_06.ScheduleManagement.ScheduleCopyResponses) obj2;
				if (schedulecopyresponses.scheduleResponse != null
						&& schedulecopyresponses.scheduleResponse.length == 1) {
					atccomponentschedule = new TCComponentSchedule[1];
					schedule = schedulecopyresponses.scheduleResponse[0].schedule;
				}
			}
		}
		return schedule;
	}
}
