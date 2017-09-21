package com.connor.hx3.plm.hxom002;
import com.connor.hx3.plm.hxom012.LOVBean;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.common.lov.view.ILOVDataView;
import com.teamcenter.rac.kernel.ListOfValuesInfo;
import com.teamcenter.rac.kernel.SoaUtil;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentItemRevisionType;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCComponentListOfValues;
import com.teamcenter.rac.kernel.TCComponentPseudoFolder;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCExceptionPartial;
import com.teamcenter.rac.kernel.TCPropertyDescriptor;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.ButtonLayout;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PropertyLayout;
import com.teamcenter.rac.util.combobox.iComboBox;
import com.teamcenter.services.rac.core.DataManagementService;

public class AuocodeDialog extends AbstractAIFDialog implements ActionListener {

	protected JTextField itemIdField;
	protected JTextField itemNameField;
	// protected JList<ItemType> typeList;
	protected JTree tree;
	protected JButton okBtn;
	protected JButton celBtn;
	protected final String DrawingType = "DrawingType";// 类型首选项
	protected final String  Autocodeitemrule= "Autocodeitemrule";// 编码规则首选项
	protected final String ClientNameCodeMapping = "ClientNameCodeMapping";// 公司编码首选项
	protected final String HX3_ZSK_UID_OP = "HX3_ZSK_UID_OP";//存放知识库PUID的首选项
	
	protected HashMap<String, String> clientNameMapping;
	protected HashMap<String, CreateInfoBean> createInfoBeanMapping;

	protected AbstractAIFApplication app;
	protected TCSession session;
	protected ItemType selectItemType;
	protected JPanel itemPropPanel;
	protected JPanel revPropPanel;
	protected JPanel revFormPropPanel;
	protected JPanel propPanel;

	private List<Object> itemFieldS;
	private List<Object> revFieldS;
	private List<Object> revFormFieldS;
	private InterfaceAIFComponent targetComp;
	//private ZSK_Bean zskBean;
	private HashMap<String, TCComponentFolder> zskMapping;

	public AuocodeDialog(AbstractAIFApplication app, TCSession session) {
		super(false);
		this.app = app;
		this.session = session;
		this.targetComp = this.app.getTargetComponent();
		getZSK();

	}
	
	public void getZSK(){
		try {
			zskMapping = new HashMap<>();
			String puid = HZMethodUtil.getPrefStr(HX3_ZSK_UID_OP);
			if(puid ==null || puid.trim().isEmpty()){
				return;
			}
			TCComponent comp = this.session.stringToComponent(puid);
			if(comp ==null || !(comp instanceof TCComponentFolder)){
				return;
			}
			
			TCComponentFolder zskF = (TCComponentFolder) comp;
			String nameP = zskF.getStringProperty("object_name");
			zskMapping.put(nameP, zskF);
			
			//获取第一层
			TCComponent[] contentsS = zskF.getReferenceListProperty("contents");
			for(int i = 0 ;i < contentsS.length;i++){
				if(!(contentsS[i] instanceof TCComponentFolder)){
					continue;
				}
				String nameF = contentsS[i].getStringProperty("object_name");
				zskMapping.put(nameP +"-" + nameF,  (TCComponentFolder) contentsS[i]);
				//获取第2层
				TCComponent[] contentsSS = contentsS[i].getReferenceListProperty("contents");
				for(int j = 0;j < contentsSS.length;j++){
					if(!(contentsSS[j] instanceof TCComponentFolder)){
						continue;
					}
					String nameS = contentsSS[j].getStringProperty("object_name");
					zskMapping.put(nameP +"-" + nameF+"-"+nameS,  (TCComponentFolder) contentsSS[j]);	
				}	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 创建对象
	 * 
	 * @throws TCException
	 */
	public void getItemID() throws TCException {
		TCComponentItemType itemType = (TCComponentItemType) session
				.getTypeComponent("Item");
		if (this.selectItemType == null || this.targetComp == null) {
			System.out.println("选择的对象为空");
			return;
		}
		CreateInfoBean bean = this.selectItemType.getBean();
		if (bean == null) {
			System.out.println("没有找到创建信息BEAN");
			return;
		}
		TCComponentType type = this.selectItemType.getType();
		if (type == null) {
			System.out.println("没有找到创建信息 的Type");
			return;
		}
		if (this.targetComp instanceof TCComponentItemRevision) {

		} else if (this.targetComp instanceof TCComponentPseudoFolder) {
			targetComp = ((TCComponentPseudoFolder) targetComp)
					.getReferenceProperty("owning_object");
		}

		if (bean.getSaveFlag().equals("另存")) {
			this.itemNameField.setText(((TCComponent) targetComp)
					.getStringProperty("object_name"));
			//return;
		}else{
			this.itemNameField.setText("");
		}

		this.itemIdField.setEditable(false);
		String newID = null;
		String sourceID = ((TCComponent) targetComp)
				.getStringProperty("item_id");

		String codeRule = bean.getCodeRule();
		String[] strS = codeRule.split("PID");
		if (strS == null || strS.length != 2) {
			return;
		}
		newID = codeRule.replace("PID", sourceID).replace("\"", "")
				.replace("+", "");// replace("公司代号", bean.getCompanyCode());

		if (newID.contains("公司代号")) {
			// newID = newID.replace("公司", bean.getCompanyCode());
			this.itemIdField.setText(newID);
			return;
		}
		char f = 0;
		char s = 0;
		String tempStr = null;
		if (newID.lastIndexOf("**") != -1) {
			tempStr = newID.replace("**", "");
			f = '0';
			s = '1';

		} else if (newID.lastIndexOf("#*") != -1) {
			tempStr = newID.replace("#*", "");
			f = 'A';
			s = '1';
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {

				if (itemType.find(tempStr + f + s) == null) {
					this.itemIdField.setText(tempStr + f + s);
					i = 10;
					break;
				}
				s++;
			}
			f++;
		}
	}

	/**
	 * 获取类型并加载到
	 * 
	 * @throws TCException
	 */
	public void getTypeOptionInfo() throws TCException {
		String[] values = HZMethodUtil.getPrefStrArray(DrawingType);
		if (values != null && values.length != 0) {
			DefaultMutableTreeNode top = new DefaultMutableTreeNode("类型");
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				String[] strS = value.split("\\/");
				if (strS != null && strS.length > 1) {
					String[] revDis = strS[0].split("=");
					if (revDis != null && revDis.length == 2) {
						TCComponentType pType = session
								.getTypeComponent(revDis[1]);
						if (pType != null) {
							CreateInfoBean bean1 = null;
							if (this.createInfoBeanMapping
									.containsKey(revDis[0])) {
								bean1 = this.createInfoBeanMapping
										.get(revDis[0]);
							}else{
								continue;
							}
							DefaultMutableTreeNode parentTypeNode = new DefaultMutableTreeNode(
									new ItemType(revDis[1], revDis[0], pType,
											bean1));
							String[] cStrS = strS[1].split("\\|");
							if (cStrS != null && cStrS.length > 0) {
								for (String cStr : cStrS) {
									String[] revDis2 = cStr.split("=");
									if (revDis2 != null && revDis2.length == 2) {
										TCComponentType cType = session
												.getTypeComponent(revDis2[1]);
										if (cType != null) {
											CreateInfoBean bean = null;
											if (this.createInfoBeanMapping
													.containsKey(revDis2[0])) {
												bean = this.createInfoBeanMapping
														.get(revDis2[0]);
											}
											DefaultMutableTreeNode cTypeNode = new DefaultMutableTreeNode(
													new ItemType(revDis2[1],
															revDis2[0], cType,
															bean));

											parentTypeNode.add(cTypeNode);
										}
									}

								}

							}
							top.add(parentTypeNode);

						}
					}
				} else {
					String[] revDis = value.split("=");
					if (revDis != null && revDis.length == 2) {
						TCComponentType type = session
								.getTypeComponent(revDis[1]);
						if (type != null) {
							CreateInfoBean bean = null;
							if (this.createInfoBeanMapping
									.containsKey(revDis[0])) {
								bean = this.createInfoBeanMapping
										.get(revDis[0]);
							}
							top.add(new DefaultMutableTreeNode(new ItemType(
									revDis[1], revDis[0], type, bean)));
						}
					}
				}

			}

			tree = new JTree(top);
			tree.setCellRenderer(new MyDefaultTreeCellRenderer());
			tree.addTreeSelectionListener(new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent e) {
					// TODO Auto-generated method stub
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
							.getLastSelectedPathComponent();// 返回最后选定的节点
					// String sel = selectedNode.toString(); // 获取选中的节点文字
					if (selectedNode == null)
						return;
					Object obj = selectedNode.getUserObject();
					if (obj instanceof ItemType) {
						System.out.println("选中替换");
						selectItemType = (ItemType) obj;

					} else {
						selectItemType = null;
					}
					// if (selectedNode.getChildCount() > 0) {
					// selectItemType = null;
					// }
					refreshUI2();
					// refreshUI();

				}
			});

		}
	}

	/**
	 * 刷新属性也页面
	 * 
	 * @throws TCException
	 */
	public void refreshPropPanel() throws TCException {
		this.itemFieldS = new ArrayList<>();
		this.revFieldS = new ArrayList<>();
		this.revFormFieldS = new ArrayList<>();
		if(propPanel == null){
			propPanel = new JPanel(new ButtonLayout(ButtonLayout.VERTICAL));
		}else{
			propPanel.removeAll();
			propPanel.setLayout(new ButtonLayout(ButtonLayout.VERTICAL));
		}
		propPanel.setBorder(new TitledBorder(
				BorderFactory.createEtchedBorder(), "属性"));

		int j = 1;

		if (this.selectItemType != null) {
			if (this.selectItemType.getBean() != null) {
				TCComponentType type = this.selectItemType.getType();

				HashMap<String, String> mapping = this.selectItemType.getBean()
						.getItemPropMap();
				int i = 0;
				if (mapping != null) {
					itemPropPanel = new JPanel(new PropertyLayout());
					itemPropPanel.setBorder(new TitledBorder(BorderFactory
							.createEtchedBorder(), "对象属性"));

					Set<Entry<String, String>> entrySet = mapping.entrySet();
					for (Entry<String, String> entry : entrySet) {
						TCPropertyDescriptor propDesc = null;
						try {
							propDesc = type.getPropertyDescriptor(entry
									.getValue());
						} catch (TCException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (propDesc != null) {
							Component comp = null;
							
							TCComponentListOfValues lobList = propDesc.getLOV();
							if (lobList != null) {
								ListOfValuesInfo info = lobList
										.getListOfValues();
								String[] valuesList = info
										.getStringListOfValues();
								
								//List<String> lists = new ArrayList<>();
								LOVBean rootLovBean = new LOVBean("值", null);
								rootLovBean.relName = entry.getValue();
								DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootLovBean);
								for(String v :valuesList){
									LOVBean fLovBean = new LOVBean(info.getDisplayableValue(v),v);
									fLovBean.relName = entry.getValue();
									DefaultMutableTreeNode fNode = new DefaultMutableTreeNode(fLovBean);
									rootNode.add(fNode);
									TCComponentListOfValues cLov = lobList.getListOfFilterOfValue(v);
									if(cLov!=null){
										ListOfValuesInfo cInfo = cLov.getListOfValues();
										String[] cValueList = cInfo.getStringListOfValues();
										for(String cV: cValueList){
											LOVBean sLovBean = new LOVBean(cInfo.getDisplayableValue(cV),cV);
											sLovBean.relName = entry.getValue();
											DefaultMutableTreeNode sNode = new DefaultMutableTreeNode(sLovBean);
											fNode.add(sNode);
										}
									}
								}
								JTree tree = new JTree(new DefaultTreeModel(rootNode));
								JTreeComboBox combox = new JTreeComboBox(tree);
								//combox.setSize(16, 16);
								combox.setPreferredSize(new Dimension(150, 28));
								this.revFieldS.add(combox);
								comp = combox;
							} else {
								ITextField field = new ITextField(16);
								this.itemFieldS.add(field);
								field.setRelName(entry.getValue());
								field.setDisName(entry.getKey());
								comp = field;
							}
							i++;

							itemPropPanel.add(i
									+ ".1.left.top.preferred.preferred",
									new JLabel(entry.getKey()));
							itemPropPanel.add(i
									+ ".2.left.top.preferred.preferred", comp);
						}
					}
					if (mapping.size() > 0) {
//						propPanel.add(j + ".1.left.top.preferred.preferred",
//								itemPropPanel);
						propPanel.add(
								itemPropPanel);
						j++;
					}

				}
				mapping = this.selectItemType.getBean().getRevPropMap();
				i = 0;
				if (mapping != null) {
					revPropPanel = new JPanel(new PropertyLayout());
					revPropPanel.setBorder(new TitledBorder(BorderFactory
							.createEtchedBorder(), "版本属性"));

					Set<Entry<String, String>> entrySet = mapping.entrySet();
					for (Entry<String, String> entry : entrySet) {
						TCPropertyDescriptor propDesc = null;
						try {
							TCComponentType revType =this.session.getTypeComponent(this.selectItemType.getRelType()+"Revision");
							propDesc = revType.getPropertyDescriptor(entry
									.getValue());
						} catch (TCException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (propDesc != null) {
							Component comp = null;
							TCComponentListOfValues lobList = propDesc.getLOV();
							if (lobList != null) {
								ListOfValuesInfo info = lobList
										.getListOfValues();
								String[] valuesList = info
										.getStringListOfValues();
								
								//List<String> lists = new ArrayList<>();
								LOVBean rootLovBean = new LOVBean("值", null);
								rootLovBean.relName = entry.getValue();
								DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootLovBean);
								for(String v :valuesList){
									LOVBean fLovBean = new LOVBean(info.getDisplayableValue(v),v);
									fLovBean.relName = entry.getValue();
									DefaultMutableTreeNode fNode = new DefaultMutableTreeNode(fLovBean);
									rootNode.add(fNode);
									TCComponentListOfValues cLov = lobList.getListOfFilterOfValue(v);
									if(cLov!=null){
										ListOfValuesInfo cInfo = cLov.getListOfValues();
										String[] cValueList = cInfo.getStringListOfValues();
										for(String cV: cValueList){
											LOVBean sLovBean = new LOVBean(cInfo.getDisplayableValue(cV),cV);
											sLovBean.relName = entry.getValue();
											DefaultMutableTreeNode sNode = new DefaultMutableTreeNode(sLovBean);
											fNode.add(sNode);
										}
									}
								}
								JTree tree = new JTree(new DefaultTreeModel(rootNode));
								JTreeComboBox combox = new JTreeComboBox(tree);
								//combox.setSize(16, 16);
								combox.setPreferredSize(new Dimension(150, 28));
								this.revFieldS.add(combox);
								comp = combox;
							} else {
								ITextField field = new ITextField(16);
								this.revFieldS.add(field);
								field.setRelName(entry.getValue());
								field.setDisName(entry.getKey());
								comp = field;
							}
							i++;

							revPropPanel.add(i
									+ ".1.left.top.preferred.preferred",
									new JLabel(entry.getKey()));
							revPropPanel.add(i
									+ ".2.left.top.preferred.preferred", comp);
						}
					}
					if (mapping.size() > 0) {
						propPanel.add(//j + ".1.left.top.preferred.preferred",
								revPropPanel);
						j++;
					}
				}

				mapping = this.selectItemType.getBean().getRevFormPropMap();
				i = 0;
				if (mapping != null) {
					revFormPropPanel = new JPanel(new PropertyLayout());
					revFormPropPanel.setBorder(new TitledBorder(BorderFactory
							.createEtchedBorder(), "版本表单属性"));

					Set<Entry<String, String>> entrySet = mapping.entrySet();
					for (Entry<String, String> entry : entrySet) {
						TCPropertyDescriptor propDesc = null;
						try {
							TCComponentType revFormType =this.session.getTypeComponent(this.selectItemType.getRelType()+"RevisionMaster");
							
							propDesc = revFormType.getPropertyDescriptor(entry
									.getValue());
						} catch (TCException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (propDesc != null) {
							Component comp = null;
							TCComponentListOfValues lobList = propDesc.getLOV();
							if (lobList != null) {
								ListOfValuesInfo info = lobList
										.getListOfValues();
								String[] valuesList = info
										.getStringListOfValues();
								LOVBean rootLovBean = new LOVBean("值", null);
								rootLovBean.relName = entry.getValue();
								DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootLovBean);
								for(String v :valuesList){
									LOVBean fLovBean = new LOVBean(info.getDisplayableValue(v),v);
									fLovBean.relName = entry.getValue();
									DefaultMutableTreeNode fNode = new DefaultMutableTreeNode(fLovBean);
									rootNode.add(fNode);
									TCComponentListOfValues cLov = lobList.getListOfFilterOfValue(v);
									if(cLov!=null){
										ListOfValuesInfo cInfo = cLov.getListOfValues();
										String[] cValueList = cInfo.getStringListOfValues();
										for(String cV: cValueList){
											LOVBean sLovBean = new LOVBean(cInfo.getDisplayableValue(cV),cV);
											sLovBean.relName = entry.getValue();
											DefaultMutableTreeNode sNode = new DefaultMutableTreeNode(sLovBean);
											fNode.add(sNode);
										}
									}
								}
								JTree tree = new JTree(new DefaultTreeModel(rootNode));
								JTreeComboBox combox = new JTreeComboBox(tree);
								//combox.setSize(16, 16);
								combox.setPreferredSize(new Dimension(150, 28));
								this.revFieldS.add(combox);
								comp = combox;
							} else {
								ITextField field = new ITextField(16);
								this.revFormFieldS.add(field);
								field.setRelName(entry.getValue());
								field.setDisName(entry.getKey());
								comp = field;
							}
							i++;

							revFormPropPanel.add(i
									+ ".1.left.top.preferred.preferred",
									new JLabel(entry.getKey()));
							revFormPropPanel.add(i
									+ ".2.left.top.preferred.preferred", comp);
						}
					}
					if (mapping.size() > 0) {
						propPanel.add(//j + ".1.left.top.preferred.preferred",
								revFormPropPanel);
						j++;
					}
				}
			}
		}
		propPanel.setVisible(false);
		propPanel.setVisible(true);
	}

	private List<String> compNameList;

	/**
	 * 公司匹配加载代码 =>名称
	 */
	public void getCreateInfo() {
		compNameList = new ArrayList<>();
		clientNameMapping = new HashMap<>();
		String[] values = HZMethodUtil
				.getPrefStrArray(this.ClientNameCodeMapping);
		if (values != null && values.length != 0) {
			for (String value : values) {
				String[] strS = value.split(":");
				if (strS != null && strS.length == 2) {
					clientNameMapping.put(strS[1]+":"+strS[0], strS[1]);
					compNameList.add(strS[1]+":"+strS[0]);
				}
			}
		}
	}

	/**
	 * 编码信息加载
	 */
	public void getClientMapping() {
		createInfoBeanMapping = new HashMap<>();
		String[] values = HZMethodUtil.getPrefStrArray(this.Autocodeitemrule);

		if (values == null) {
			return;
		}
		for (String value : values) {
			String[] strS = value.split("\\/");
			if (strS == null || strS.length != 5) {
				continue;
			}
			CreateInfoBean bean = new CreateInfoBean();
			HashMap<String, String> itemPropMap = new HashMap<>();
			HashMap<String, String> revPropMap = new HashMap<>();
			HashMap<String, String> revFormPropMap = new HashMap<>();
			bean.setNodeName(strS[0]);
			bean.setCodeRule(strS[1]);
			bean.setCompany(strS[3]);
			if (clientNameMapping.containsKey(strS[3])) {
				bean.setCompanyCode(clientNameMapping.get(strS[3]));
			}
			bean.setLocation(strS[4]);
			String[] cStrS = strS[2].split("\\|");
			if (cStrS != null && cStrS.length == 3) {
				bean.setSaveFlag(cStrS[0]);
				bean.setTypeName(cStrS[1]);
				String[] ccStrS = cStrS[2].split("&");
				if (ccStrS != null) {
					for (String ccStr : ccStrS) {
						String[] cccStrS = ccStr.split(":");
						if (cccStrS != null && cccStrS.length == 3) {
							if (cccStrS[0].toUpperCase().equals("ITEM")) {
								itemPropMap.put(cccStrS[1], cccStrS[2]);
							} else if (cccStrS[0].toUpperCase().equals("REV")) {
								revPropMap.put(cccStrS[1], cccStrS[2]);
							} else if (cccStrS[0].toUpperCase().equals(
									"REVFORM")) {
								revFormPropMap.put(cccStrS[1], cccStrS[2]);
							}
						}
					}
				}
			}

			bean.setItemPropMap(itemPropMap);
			bean.setRevFormPropMap(revFormPropMap);
			bean.setRevPropMap(revPropMap);
			this.createInfoBeanMapping.put(strS[0], bean);

		}

	}

	private JPanel centerPanel;
	private  JComboBox<String>  nameCombox;
	private JTextField txtInput;
	private String olderId = null;
	public void refreshUI2() {
		centerPanel.removeAll();
		centerPanel.setLayout(new PropertyLayout());
		centerPanel.add("1.1.left.top.preferred.preferred", new JLabel("    "));
		centerPanel.add("2.1.left.top.preferred.preferred", new JLabel("ID:"));
		centerPanel.add("2.2.left.top.preferred.preferred", itemIdField);
		centerPanel.add("3.1.left.top.preferred.preferred", new JLabel("名称:"));
		centerPanel.add("3.2.left.top.preferred.preferred", itemNameField);
		try {
			refreshPropPanel();
			getItemID();
			if (itemIdField.getText().trim().contains("公司代号")) {
				centerPanel.add("4.1.left.top.preferred.preferred", new JLabel(
						"公司:"));
				
//				 nameCombox = new JComboBox<String>(
//						this.compNameList.toArray(new String[compNameList
//								.size()]));
//				DefaultComboBoxModel model = new DefaultComboBoxModel();  
//				 nameCombox  = new JComboBox(model) {  
//			             public Dimension getPreferredSize() {  
//			                 return new Dimension(super.getPreferredSize().width, 0);  
//			             }  
//			         }; 
				// nameCombox.setEditable(true);
				 
				 
				txtInput = new JTextField();  
		         setupAutoComplete(txtInput, 
							(ArrayList<String>) this.compNameList);  
		         txtInput.setColumns(30);  
		       // System.out.print(" >>>>>>"+);
		        olderId = itemIdField.getText();
		         nameCombox.addItemListener(new ItemListener() {
					
					@Override
					public void itemStateChanged(ItemEvent arg0) {
						// TODO Auto-generated method stub
						String idStr =(String) arg0.getItem();
						String[] idS = idStr.split(":");
						itemIdField.setEditable(true);
						itemIdField.setText(olderId.replace("公司代号", idS[0]));
						itemIdField.setEditable(false);
						System.out.println("<<<<"+idS[0]);
						 System.out.print(" >>>>>>"+olderId);
						
					}
				});
				 
				
				centerPanel.add("4.2.left.top.preferred.preferred", txtInput);
			}else{
				nameCombox = null;
				txtInput = null;
			}
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// this.dispose();
			// return;
		}
		centerPanel.setVisible(false);
		centerPanel.setVisible(true);
	}
	 private  boolean isAdjusting(JComboBox cbInput) {  
         if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {  
             return (Boolean) cbInput.getClientProperty("is_adjusting");  
         }  
         return false;  
     }  

     private  void setAdjusting(JComboBox cbInput, boolean adjusting) {  
         cbInput.putClientProperty("is_adjusting", adjusting);  
     }  

     public  void setupAutoComplete(final JTextField txtInput, final ArrayList<String> items) {  
         final DefaultComboBoxModel model = new DefaultComboBoxModel();  
         final JComboBox cbInput = new JComboBox(model) {  
             public Dimension getPreferredSize() {  
                 return new Dimension(super.getPreferredSize().width, 0);  
             }  
         };  
         nameCombox = cbInput;
         setAdjusting(cbInput, false);  
         for (String item : items) {  
             model.addElement(item);  
         }  
         cbInput.setSelectedItem(null);  
         cbInput.addActionListener(new ActionListener() {  
             @Override  
             public void actionPerformed(ActionEvent e) {  
                 if (!isAdjusting(cbInput)) {  
                     if (cbInput.getSelectedItem() != null) {  
                         txtInput.setText(cbInput.getSelectedItem().toString());  
                     }  
                 }  
             }  
         });  

         txtInput.addKeyListener(new KeyAdapter() {  

             @Override  
             public void keyPressed(KeyEvent e) {  
                 setAdjusting(cbInput, true);  
                 if (e.getKeyCode() == KeyEvent.VK_SPACE) {  
                     if (cbInput.isPopupVisible()) {  
                         e.setKeyCode(KeyEvent.VK_ENTER);  
                     }  
                 }  
                 if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {  
                     e.setSource(cbInput);  
                     cbInput.dispatchEvent(e);  
                     if (e.getKeyCode() == KeyEvent.VK_ENTER) {  
                         txtInput.setText(cbInput.getSelectedItem().toString());  
                         cbInput.setPopupVisible(false);  
                     }  
                 }  
                 if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {  
                     cbInput.setPopupVisible(false);  
                 }  
                 setAdjusting(cbInput, false);  
             }  
         });  
         txtInput.getDocument().addDocumentListener(new DocumentListener() {  
             public void insertUpdate(DocumentEvent e) {  
                 updateList();  
             }  

             public void removeUpdate(DocumentEvent e) {  
                 updateList();  
             }  

             public void changedUpdate(DocumentEvent e) {  
                 updateList();  
             }  

             private void updateList() {  
                 setAdjusting(cbInput, true);  
                 model.removeAllElements();  
                 String input = txtInput.getText();  
                 if (!input.isEmpty()) {  
                     for (String item : items) {  
                         if (item.toLowerCase().startsWith(input.toLowerCase())) {  
                             model.addElement(item);  
                         }  
                     }  
                 }  
                 cbInput.setPopupVisible(model.getSize() > 0);  
                 setAdjusting(cbInput, false);  
             }  
         });  
         txtInput.setLayout(new BorderLayout());  
         txtInput.add(cbInput, BorderLayout.SOUTH);  
     }  
     
	/**
	 * 刷新
	 */
	public void refreshUI() {

		this.getContentPane().removeAll();
		this.setTitle("创建其他图纸");
		this.setSize(new Dimension(700, 500));

		itemIdField = new JTextField(32);
		itemNameField = new JTextField(32);
		centerPanel = new JPanel(new PropertyLayout());
		centerPanel.add("1.1.left.top.preferred.preferred", new JLabel("    "));
		centerPanel.add("2.1.left.top.preferred.preferred", new JLabel("ID:"));
		centerPanel.add("2.2.left.top.preferred.preferred", itemIdField);
		centerPanel.add("3.1.left.top.preferred.preferred", new JLabel("名称:"));
		centerPanel.add("3.2.left.top.preferred.preferred", itemNameField);

		okBtn = new JButton("新建");
		okBtn.addActionListener(this);

		celBtn = new JButton("关闭");
		celBtn.addActionListener(this);

		JPanel southPanel = new JPanel(new FlowLayout());
		southPanel.add(celBtn);
		southPanel.add(new JLabel("     "));
		southPanel.add(okBtn);

		// ------------加载属性---

		try {
			refreshPropPanel();
			getItemID();
			if (itemIdField.getText().trim().contains("公司代号")) {
				centerPanel.add("4.1.left.top.preferred.preferred", new JLabel(
						"公司:"));
				JComboBox<String> nameCombox = new JComboBox<String>(
						this.compNameList.toArray(new String[compNameList
								.size()]));
				nameCombox.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						// TODO Auto-generated method stub

					}
				});
				centerPanel.add("4.2.left.top.preferred.preferred", nameCombox);
			}
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// this.dispose();
			// return;
		}

		// ----------------------
		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.setBorder(new TitledBorder(
				BorderFactory.createEtchedBorder(), "新建信息"));
		rootPanel.add(centerPanel, BorderLayout.NORTH);
		rootPanel.add(new JScrollPane(propPanel), BorderLayout.CENTER);
		rootPanel.add(southPanel, BorderLayout.SOUTH);

		JPanel westPanel = new JPanel(new BorderLayout());
		westPanel.setBorder(new TitledBorder(
				BorderFactory.createEtchedBorder(), "类型信息"));

		// ecTreeTest(tree);
		westPanel.add(new JScrollPane(tree), BorderLayout.CENTER);

		JPanel root = new JPanel(new BorderLayout());
		root.add(westPanel, BorderLayout.WEST);
		root.add(rootPanel, BorderLayout.CENTER);
		

		this.add(root);
		this.centerToScreen();
		// this.pack();

		this.showDialog();
	}

//	public void ecTreeTest(JTree tree) {
//
//		TreeNode root = (TreeNode) tree.getModel().getRoot();
//
//		expandTree(tree, new TreePath(root));
//
//	}
//
//	private void expandTree(JTree tree, TreePath parent) {
//
//		TreeNode node = (TreeNode) parent.getLastPathComponent();
//
//		if (node.getChildCount() >= 0) {
//
//			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
//
//				TreeNode n = (TreeNode) e.nextElement();
//
//				TreePath path = parent.pathByAddingChild(n);
//
//				expandTree(tree, path);
//
//			}
//
//		}
//
//		tree.expandPath(parent);
//
//	}

	/**
	 * 初始化界面
	 */
	public void init() {
		try {
			getCreateInfo();
			getClientMapping();
			getTypeOptionInfo();
			System.out.println(this.clientNameMapping.size());
			System.out.println(this.createInfoBeanMapping.size());
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MessageBox.post("请检查【" + this.DrawingType + "】首选项", "错误",
					MessageBox.ERROR);
			this.dispose();
			return;
		}
		if (tree == null) {
			MessageBox.post("请检查【" + this.DrawingType + "】首选项", "错误",
					MessageBox.ERROR);
			this.dispose();
			return;
		}

		refreshUI();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// super.run();
		this.init();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object sourceObj = e.getSource();
		if (sourceObj.equals(this.okBtn)) {
			if (selectItemType != null) {
				if (selectItemType.getBean() != null) {
					if(this.selectItemType.getBean().getSaveFlag().equals("另存"))
						saveAsItemFromRev(this.itemIdField.getText(),"A");
					else{
					HashMap<String, Object> itemPropMap = new HashMap<>();
					HashMap<String, Object> revPropMap = new HashMap<>();
					HashMap<String, Object> revFormPropMap = new HashMap<>();
					for(Object obj : itemFieldS){
						if(obj instanceof ITextField){
							ITextField field = (ITextField) obj;
							itemPropMap.put(field.getRelName(), field.getText());
						}else if(obj instanceof IJCombox){
							IJCombox combox = (IJCombox) obj;
							itemPropMap.put(combox.getRelName(), combox.getValues()[combox.getSelectedIndex()]);
						}else if(obj instanceof JTreeComboBox){
							JTreeComboBox jtreeCombox = (JTreeComboBox) obj;
							//LOVBean bean = (LOVBean) jtreeCombox.getSelectedItem();
							try {
								LOVBean bean = (LOVBean)((DefaultMutableTreeNode)((TreePath)jtreeCombox.getSelectedItem()).getLastPathComponent()).getUserObject();
								
								itemPropMap.put(bean.relName, bean.value);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
						}
					}
					for(Object obj : revFieldS){
						if(obj instanceof ITextField){
							ITextField field = (ITextField) obj;
							revPropMap.put(field.getRelName(), field.getText());
						}else if(obj instanceof IJCombox){
							IJCombox combox = (IJCombox) obj;
							revPropMap.put(combox.getRelName(), combox.getValues()[combox.getSelectedIndex()]);
						}	else if(obj instanceof JTreeComboBox){
							JTreeComboBox jtreeCombox = (JTreeComboBox) obj;
							try {
								LOVBean bean = (LOVBean)((DefaultMutableTreeNode)((TreePath)jtreeCombox.getSelectedItem()).getLastPathComponent()).getUserObject();
								revPropMap.put(bean.relName, bean.value);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
						}			
					}
					for(Object obj : revFormFieldS){
						if(obj instanceof ITextField){
							ITextField field = (ITextField) obj;
							revFormPropMap.put(field.getRelName(), field.getText());
						}else if(obj instanceof IJCombox){
							IJCombox combox = (IJCombox) obj;
							revFormPropMap.put(combox.getRelName(), combox.getValues()[combox.getSelectedIndex()]);
						}else if(obj instanceof JTreeComboBox){
							JTreeComboBox jtreeCombox = (JTreeComboBox) obj;
							//LOVBean bean = (LOVBean) jtreeCombox.getSelectedItem();
							try {
								LOVBean bean = (LOVBean)((DefaultMutableTreeNode)((TreePath)jtreeCombox.getSelectedItem()).getLastPathComponent()).getUserObject();
								
								revFormPropMap.put(bean.relName, bean.value);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
						}	
					}
					
					
					createNewItemSoa(this.itemIdField.getText(), "A",
							selectItemType.getRelType(),
							this.itemNameField.getText(), "",
							this.selectItemType.getBean().getLocation(),
							itemPropMap,
							revPropMap,
							revFormPropMap);
					}
				} else {
					MessageBox.post("配置创建规则，请查看首选项【" + this.Autocodeitemrule
							+ "】", "错误", MessageBox.ERROR);
				}
			} else {
				MessageBox.post("请选择要创建的类型", "错误", MessageBox.ERROR);
			}
		} else {
			this.disposeDialog();
		}
	}
	
	
	/**
	 * 存放对象到配置的路径中
	 * @param item
	 * @param pathP
	 */
	 public void sendToPlace( String itemID,TCComponentItem item,String pathP){
		 if(item == null ||  pathP == null){	 
			 return;
		 }
		 try {
			String[] pathS = pathP.split("&");
			 for(int i = 0 ;i < pathS.length;i++){
				 //Newstuff
				 if(pathS[i].toUpperCase().trim().equals("NEWSTUFF")){
					 session.getUser().getNewStuffFolder()
						.add("contents", item);
				 }
				 //伪文件夹
				 else if(pathS[i].startsWith("伪:")){
					 
					 ((TCComponentItemRevision)this.targetComp).add(pathS[i].replace("伪:", ""), item);
					 
				 }
				 //知识库
				 else if(pathS[i].startsWith("知:")&& zskMapping!=null){
					 pathS[i] = pathS[i].replace("知:", "");
					 //String[] pathS3 = pathS[i].split("-");
					 //|公司
					 TCComponentFolder folder = null;
					 if(pathS[i].endsWith("|公司")){
						if( zskMapping.containsKey(pathS[i].replace("|公司", ""))){
							
							folder = zskMapping.get(pathS[i].replace("|公司", ""));
							TCComponent[] compS =	folder.getReferenceListProperty("contents");
							boolean findCompany = false;
							String[] enCNs =((String) nameCombox.getSelectedItem()).split("\\:");
							String folderName = "";
							if(enCNs.length == 2 ){
								if(enCNs[0].equals(enCNs[1])){
									folderName = enCNs[0];
								}else{
									folderName = enCNs[0]+"#"+enCNs[1];
								}
							}
							boolean isSaveOK = false;
							for(TCComponent comp : compS){
								
								if(comp instanceof TCComponentFolder && comp.getStringProperty("object_name").equals(folderName))
								{
									comp.add("contents", item);
									isSaveOK = true;
								}
							}
							if(!isSaveOK){
								MessageBox.post("["+pathP+"]中不存在公司["+nameCombox.getSelectedItem()+"]文件夹，请联系管理员进行配置","错误",MessageBox.ERROR);
							}
						}
					 }else{
						 if( zskMapping.containsKey(pathS[i])){
							 folder = zskMapping.get(pathS[i]);
							 folder.add("contents", item);		
						 }
					 }
					 
					 if(folder == null){
						 session.getUser().getNewStuffFolder()
							.add("contents", item);
					 }
				 }
				 
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MessageBox.post("对象"+itemID+"存放到路径["+pathP+"]失败，","错误",MessageBox.ERROR);
		}
	 }
	
	 private void saveAsItemFromRev(String itemId,String revID){
		 try {
			TCComponentItemRevisionType revTypeComp =  (TCComponentItemRevisionType) ((TCComponentItemRevision)this.targetComp).getTypeComponent();
			if(revTypeComp.getRevisionNamingRule()!=null)
				revID = revTypeComp.getRevisionNamingRule().getStartingRevision();
	
			TCComponentItem newComp = ((TCComponentItemRevision)this.targetComp).saveAsItem(itemId, revID);
			if (newComp != null ) {
				//arraylist.addAll(Arrays.asList(atccomponent));
				try {
					getItemID();
				} catch (TCException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sendToPlace(itemId,newComp,this.selectItemType.getBean().getLocation());
				MessageBox.post("创建成功", "成功", MessageBox.INFORMATION);
			}else{
				MessageBox.post("创建失败", "失败", MessageBox.ERROR);
			}
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MessageBox.post("创建失败", "失败", MessageBox.ERROR);
		}
		 
	 }
	 
	 /**
	  * 通过SOA创建对象
	  * @param itemID
	  * @param itemRev
	  * @param itemType
	  * @param itemName
	  * @param itemDesc
	  * @param path
	  */
	 private void createNewItemSoa(String itemID, String itemRev,
				String itemType, String itemName, String itemDesc, String path,
				HashMap<String, Object> itemPropMap,
				HashMap<String, Object> revPropMap,
				HashMap<String, Object> revFormPropMap) {
		 if(this.nameCombox!=null && this.txtInput !=null){
			 	if(!this.compNameList.contains(this.txtInput.getText())){
			 		MessageBox.post("选择的公司代码不存在,请联系管理员!","错误",MessageBox.ERROR);
					return;
			 	}
				itemID = itemID.replace("公司代号",this.clientNameMapping.get( (String)nameCombox.getSelectedItem()) );
			}
			try {
				TCComponentItemType type = (TCComponentItemType) this.session.getTypeComponent("Item");
				if(type.findItems(itemID)!=null && type.findItems(itemID).length!=0){
					MessageBox.post("该零组件编码["+itemID+"]已经存在","错误",MessageBox.ERROR);
					return;
				}
			} catch (TCException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//TODO 新添加逻辑

			System.out.println("新的方法创建ITEM对象");

			try {
				Set<Entry<String, Object>> setEntry = null;
				Map<String, String> stringMap = null;
				Map<String, Integer> intMap = null;
				Map<String, Boolean> boolMap = null;
				Map<String, Date> dateMap = null;
				
				com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput itemInput = new com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput();
				itemInput.boName = itemType;
				//Map<String, String> itemStringMap = new HashMap();
				
				//itemInput.stringProps = itemStringMap;

				Map<String, CreateInput[]> revFormMap = new HashMap();
				CreateInput[] revFormInputS = new CreateInput[1];
				revFormInputS[0] = new CreateInput();
				revFormInputS[0].boName = itemType + "RevisionMaster";
				stringMap = new HashMap();
				intMap = new HashMap();
				boolMap = new HashMap();
				dateMap = new HashMap();
				setEntry = revFormPropMap.entrySet();
				for(Entry<String,Object> entry : setEntry){
					String propName = entry.getKey();
					Object value = entry.getValue();
					System.out.println(" 版本表单：key ="+ propName+"| value ="+ value);
					if(value instanceof String){
						stringMap.put(propName, (String) value);
					}else if(value instanceof Integer){
						intMap.put(propName,  (Integer) value);
					}else if(value instanceof Boolean){
						boolMap.put(propName,  (Boolean) value);
					}else if(value instanceof Date){
						dateMap.put(propName,  (Date) value);
					}
				}
			
				revFormInputS[0].stringProps = stringMap;
				revFormInputS[0].intProps = intMap;
				revFormInputS[0].boolProps = boolMap;
				revFormInputS[0].dateProps = dateMap;
				revFormMap.put("IMAN_master_form_rev", revFormInputS);
				
				Map<String, CreateInput[]> revMap = new HashMap();
				CreateInput[] revInputS = new CreateInput[1];
				revInputS[0] = new CreateInput();
				revInputS[0].boName = itemType + "Revision";
				try {
					TCComponentItemRevisionType revTypeComp = (TCComponentItemRevisionType) this.session.getTypeComponent(itemType + "Revision");
					if(revTypeComp.getRevisionNamingRule()!=null)
						itemRev = revTypeComp.getRevisionNamingRule().getStartingRevision();
				} catch (TCException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//设置REV的属性
				stringMap = new HashMap();
				intMap = new HashMap();
				boolMap = new HashMap();
				dateMap = new HashMap();	
				stringMap.put("item_revision_id", itemRev);
				setEntry = revPropMap.entrySet();
				for(Entry<String,Object> entry : setEntry){
					String propName = entry.getKey();
					Object value = entry.getValue();
					System.out.println(" 版本：key ="+ propName+"| value ="+ value);
					if(value instanceof String){
						stringMap.put(propName, (String) value);
					}else if(value instanceof Integer){
						intMap.put(propName,  (Integer) value);
					}else if(value instanceof Boolean){
						boolMap.put(propName,  (Boolean) value);
					}else if(value instanceof Date){
						dateMap.put(propName,  (Date) value);
					}
				}
			
				revInputS[0].stringProps = stringMap;
				revInputS[0].intProps = intMap;
				revInputS[0].boolProps = boolMap;
				revInputS[0].dateProps = dateMap;
				revInputS[0].compoundCreateInput = revFormMap;

				revMap.put("revision", revInputS);
				
				itemInput.compoundCreateInput = revMap;
				
				//设置ITEM的属性
				stringMap = new HashMap();
				intMap = new HashMap();
				boolMap = new HashMap();
				dateMap = new HashMap();	
				setEntry = itemPropMap.entrySet();
				for(Entry<String,Object> entry : setEntry){
					String propName = entry.getKey();
					Object value = entry.getValue();
					System.out.println(" 对象：key ="+ propName+"| value ="+ value);
					if(value instanceof String){
						stringMap.put(propName, (String) value);
					}else if(value instanceof Integer){
						intMap.put(propName,  (Integer) value);
					}else if(value instanceof Boolean){
						boolMap.put(propName,  (Boolean) value);
					}else if(value instanceof Date){
						dateMap.put(propName,  (Date) value);
					}
				}
				stringMap.put("object_name", itemName);
				stringMap.put("item_id", itemID);
				stringMap.put("object_desc", itemDesc);
				itemInput.stringProps= stringMap;
				itemInput.intProps = intMap;
				itemInput.boolProps = boolMap;
				itemInput.dateProps = dateMap;
				
				DataManagementService datamanagementservice = DataManagementService
						.getService(session);
				com.teamcenter.services.rac.core._2008_06.DataManagement.CreateIn createin = new com.teamcenter.services.rac.core._2008_06.DataManagement.CreateIn();
				createin.clientId = "Connor";
				createin.data = itemInput;
				com.teamcenter.services.rac.core._2008_06.DataManagement.CreateResponse createresponse = datamanagementservice
						.createObjects(new com.teamcenter.services.rac.core._2008_06.DataManagement.CreateIn[] { createin });
				SoaUtil.handlePartialErrors(createresponse.serviceData, null, null,
						true);
				com.teamcenter.services.rac.core._2008_06.DataManagement.CreateOut acreateout[] = createresponse.output;
				if (acreateout != null && acreateout.length > 0) {
					//ArrayList arraylist = new ArrayList(0);
					com.teamcenter.services.rac.core._2008_06.DataManagement.CreateOut acreateout1[];
					int j = (acreateout1 = acreateout).length;
					for (int i = 0; i < j; i++) {
						com.teamcenter.services.rac.core._2008_06.DataManagement.CreateOut createout = acreateout1[i];
						TCComponent atccomponent[] = createout.objects;
						if (atccomponent != null && atccomponent.length > 0) {
							//arraylist.addAll(Arrays.asList(atccomponent));
							try {
								getItemID();
							} catch (TCException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							TCComponentItem newComp = (TCComponentItem) atccomponent[0];
							sendToPlace(itemID,newComp,this.selectItemType.getBean().getLocation());
							MessageBox.post("创建成功", "成功", MessageBox.INFORMATION);
						}else{
							MessageBox.post("创建失败", "失败", MessageBox.ERROR);
						}
					}
				}
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MessageBox.post("创建失败", "失败", MessageBox.ERROR);
			} catch (TCExceptionPartial e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MessageBox.post("创建失败", "失败", MessageBox.ERROR);
			}	 
	 }
	 
	 /**
	 * 通过SOA创建对象
	 * 
	 * @param itemID
	 * @param itemRev
	 * @param itemType
	 * @param itemName
	 * @param itemDesc
	 */
	private void createNewItemSoa2(String itemID, String itemRev,
			String itemType, String itemName, String itemDesc, String path) {
		
		if(this.nameCombox!=null){
			itemID = itemID.replace("公司代号",this.clientNameMapping.get( (String)nameCombox.getSelectedItem()) );
		}
		try {
			TCComponentItemType type = (TCComponentItemType) this.session.getTypeComponent("Item");
			if(type.findItems(itemID)!=null && type.findItems(itemID).length!=0){
				MessageBox.post("该零组件编码["+itemID+"]已经存在","错误",MessageBox.ERROR);
				return;
			}
		} catch (TCException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Map propMap = new HashMap<String, String>();

		for (Object field : this.revFormFieldS) {
			if (field instanceof ITextField) {
				ITextField it = (ITextField) field;
				propMap.put(it.getRelName(), it.getText());
			}
		}

		DataManagementService datamanagementservice = DataManagementService
				.getService(session);
		com.teamcenter.services.rac.core._2006_03.DataManagement.CreateItemsResponse createitemsresponse = null;
		com.teamcenter.services.rac.core._2006_03.DataManagement.ItemProperties aitemproperties[] = new com.teamcenter.services.rac.core._2006_03.DataManagement.ItemProperties[1];
		aitemproperties[0] = new com.teamcenter.services.rac.core._2006_03.DataManagement.ItemProperties();
		aitemproperties[0].clientId = Integer.toString(1);
		aitemproperties[0].description = itemDesc;
		
		aitemproperties[0].itemId = itemID;
		aitemproperties[0].name = itemName;
		aitemproperties[0].revId = itemRev;
		aitemproperties[0].type = itemType;
		aitemproperties[0].uom = "";// == null ? "" : uomComp.toString();

		com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes extendedattributes[] = new com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes[1];
		com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes extendedattributes3 = new com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes();
		extendedattributes3 = new com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes();
		extendedattributes3.objectType = itemType + "Revision";
		extendedattributes3.attributes = propMap;
		extendedattributes[0] = extendedattributes3;

		aitemproperties[0].extendedAttributes = extendedattributes;
		TCComponent tccomponent1 = null;
		String s1 = "";
		createitemsresponse = datamanagementservice.createItems(
				aitemproperties, tccomponent1, s1);
		if (createitemsresponse.serviceData.sizeOfPartialErrors() == 0) {
			TCComponentItem newComp = createitemsresponse.output[0].item;
			if (newComp != null) {
				try {
					getItemID();
//					session.getUser().getNewStuffFolder()
//							.add("contents", newComp);
					sendToPlace(itemID,newComp,this.selectItemType.getBean().getLocation());
				} catch (TCException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MessageBox.post("创建成功", "成功", MessageBox.INFORMATION);
			} else
				MessageBox.post("创建失败", "成功", MessageBox.INFORMATION);
		}
	}

	private class IJCombox extends JComboBox {

		private String[] values;
		private String relName;
		private String disName;

		public String getRelName() {
			return relName;
		}

		public void setRelName(String relName) {
			this.relName = relName;
		}

		public String getDisName() {
			return disName;
		}

		public void setDisName(String disName) {
			this.disName = disName;
		}

		public String[] getValues() {
			return values;
		}

		public void setValues(String[] values) {
			this.values = values;
		}

	}

	private class ITextField extends JTextField {

		public ITextField() {
			super();
			// TODO Auto-generated constructor stub
		}

		public ITextField(Document doc, String text, int columns) {
			super(doc, text, columns);
			// TODO Auto-generated constructor stub
		}

		public ITextField(int columns) {
			super(columns);
			// TODO Auto-generated constructor stub
		}

		public ITextField(String text, int columns) {
			super(text, columns);
			// TODO Auto-generated constructor stub
		}

		public ITextField(String text) {
			super(text);
			// TODO Auto-generated constructor stub
		}

		private String relName;
		private String disName;

		public String getDisName() {
			return disName;
		}

		public void setDisName(String disName) {
			this.disName = disName;
		}

		public String getRelName() {
			return relName;
		}

		public void setRelName(String relName) {
			this.relName = relName;
		}

	}

}
