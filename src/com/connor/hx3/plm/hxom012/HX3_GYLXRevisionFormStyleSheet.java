package com.connor.hx3.plm.hxom012;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.connor.hx3.plm.util.HxomMethodUtil;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentForm;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.stylesheet.AbstractRendering;
import com.teamcenter.rac.util.ButtonLayout;
import com.teamcenter.rac.util.MessageBox;

public class HX3_GYLXRevisionFormStyleSheet extends AbstractRendering implements
		ActionListener {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private final String HX_BZGX_Option_Name = "HX_BZGX_Option_Name";
	private String titleName ;

	private JPanel rootPanel;
	private JPanel topPanel;
	private JPanel buttomPanel;
	private JScrollPane scrollPanel;
	private JLabel emptyLable;
	private JButton addButton;
	private JButton insertButton;
	private JButton delButton;
	private JCheckBox sureButton;
	

	private JTable partsTable;
	public boolean isRefresh;
	public boolean over;
	private List<HX3_GYLXRevisionFormPropBean> valueLists;
	private HashMap<String, List<BZGXBean>> bzgxMap;//��׼����
	private DefaultTableModel dtm;
	private TCComponent formComp;

	private TCProperty[] propertiesArray;
	private BomStructTree bomJTree;
	private BomStructBean firstBean;
	private HX3_GYLXRevisionFormPropBean selectTableBean;
	private BOM bomMsg;
	private HX3_SelectBzgxDialog bzgxDialog;
	private boolean isSure = false;
	private final String hx3_qrgg = "hx3_qrgg";

	private String[] BZGX_LIST = { //
	"hx3_gzbz", // ��׼����
			"hx3_gxsm",// ����˵��
			"hx3_gzzx",// ��������,������������
			"hx3_dcgx1",// ���幤�� bool
			"hx3_bgd1",// ����� bool
			"hx3_wwgx1",// ί�⹤�� bool
			"hx3_jfd1",// �Ʒѵ� bool
			"hx3_jhwwgx1"// �ƻ�ί�⹤�� bool
	};
	private Object[] titleNames = { //
	"�����к�", "��׼����", "����˵��", "��������", "������������",// 5
			"ģ�߱��", "���幤��", "�����", "ί�⹤��", "�Ʒѵ�",// 10
			"�ƻ�ί�⹤��", "��Ч����", "ʧЧ����", "�����ļ����", "�����ļ��汾", // 15
			"��������" // 17
	};
	public static final String[] TM2_PROCESS_PROP_NAMES = {//
	"hx3_gxhh", "hx3_gxbz", "hx3_gxsm", "hx3_gzzx",
			"hx3_gzzxmc", // 5
			"hx3_mjbh", "hx3_dcgx1", "hx3_bgd1", "hx3_wwgx1",
			"hx3_jfd1", // 10
			"hx3_jhwwgx1", "hx3_sxrq", "hx3_shixrq", "hx3_gywjbh",
			"hx3_gywjbb",// 15
			"hx3_lywl",// 16
			"hx3_qrgg"//17
	};

	// ===========================

	public HX3_GYLXRevisionFormStyleSheet(TCComponent arg0) throws Exception {
		super(arg0);
		this.formComp = arg0;
		getTitle2();
		this.isRefresh = false;
		this.over = false;
		loadRendering();
		if (firstBean == null) {
			getFormMsg();
			bomJTree = new BomStructTree(firstBean, this.partsTable);
		}
//		new Thread() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				// super.run();
//				while (!over) {
//					try {
//						new Thread().sleep(1000);
//						if (selectTableBean != null) {
//
//							int index = valueLists.indexOf(selectTableBean);
//
//							if(index !=-1){
//							((DefaultTableModel) partsTable.getModel())
//									.setValueAt(selectTableBean.hx3_lywl,
//											index, 15);
//							}
//							
//						}
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}catch(Exception e){
//						e.printStackTrace();
//					}
//				}
//			}
//
//		}.start();
	}

	public void getTitle2(){
		try {
			TCComponentItemRevision tempRev = (TCComponentItemRevision) formComp
					.getReferenceProperty("item_revision");
			if(tempRev == null){
				return;
			}
			TCComponentItem item = tempRev.getItem();
			if(item == null){
				return;
			}
			AIFComponentContext[] contextS = item.whereReferencedByTypeRelation(new String[]{"HX3_WLRevision"},new String[]{ "HX3_GYLXRelation"});
			System.out.println("count = "+contextS.length);
			if(contextS!=null && contextS.length>0){
				titleName =((TCComponentItemRevision) contextS[0].getComponent()).getStringProperty("object_string")+"����·��ͼ";
			}
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void getBZGXList() {
		try {
			List<BZGXBean> bzgxBeanListT = new ArrayList<>();
			bzgxMap = new HashMap<>();
			String puid = HxomMethodUtil.getPrefStr(HX_BZGX_Option_Name);
			TCSession session = (TCSession) AIFUtility.getCurrentApplication()
					.getSession();

			TCComponentForm form = (TCComponentForm) session
					.stringToComponent(puid);
			TCProperty[] propS = form.getTCProperties(BZGX_LIST);
			if (propS != null) {
				if (propS[0] != null && propS[1] != null && propS[2] != null
						&& propS[3] != null && propS[4] != null
						&& propS[5] != null && propS[6] != null
						&& propS[7] != null) {
					if (propS[0].getStringArrayValue().length == propS[1]
							.getStringArrayValue().length
							&& propS[0].getStringArrayValue().length == propS[2]
									.getStringArrayValue().length
							&& propS[0].getStringArrayValue().length == propS[3]
									.getLogicalValueArray().length
							&& propS[0].getStringArrayValue().length == propS[4]
									.getLogicalValueArray().length
							&& propS[0].getStringArrayValue().length == propS[5]
									.getLogicalValueArray().length
							&& propS[0].getStringArrayValue().length == propS[6]
									.getLogicalValueArray().length
							&& propS[0].getStringArrayValue().length == propS[7]
									.getLogicalValueArray().length) {
						for (int i = 0; i < propS[0].getStringArrayValue().length; i++) {
							BZGXBean bean = new BZGXBean();
							bzgxBeanListT.add(bean);
							bean.hx3_gxbz = propS[0].getStringArrayValue()[i]; // ��׼����
							bean.hx3_gxsm = propS[1].getStringArrayValue()[i];// ����˵��
							bean.hx3_gzzx = propS[2].getStringArrayValue()[i];// ��������,������������
							bean.hx3_gzzxmc = propS[2].getDisplayableValues()
									.get(i);// ������������
							bean.hx3_dcgx1 = propS[3].getLogicalValueArray()[i];// ���幤��
							bean.hx3_bgd1 = propS[4].getLogicalValueArray()[i];// �����
							bean.hx3_wwgx1 = propS[5].getLogicalValueArray()[i];// ί�⹤��
							bean.hx3_jfd1 = propS[6].getLogicalValueArray()[i];// �Ʒѵ�
							bean.hx3_jhwwgx1 = propS[7].getLogicalValueArray()[i];// �ƻ�ί�⹤��
							if (!bzgxMap.containsKey(bean.hx3_gxsm)) {
								System.out.println("��׼���� =>" + bean.hx3_gxsm);
								List<BZGXBean> bzgxBeanList = new ArrayList<>();
								bzgxBeanList.add(bean);
								bzgxMap.put(bean.hx3_gxsm, bzgxBeanList);
							}else{
								List<BZGXBean> bzgxBeanList = bzgxMap.get(bean.hx3_gxsm);
								bzgxBeanList.add(bean);
							}
						}

					} else {
						System.out.println("��׼����ĳ��Ȳ���ͬ");
					}

				} else {
					System.out.println("��ȡ��׼������ִ���");
				}

			}
			this.bzgxDialog = new HX3_SelectBzgxDialog(bzgxBeanListT,this.partsTable);

		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * ͨ��Form�����Ϲ�����Ϣ
	 * 
	 * @throws TCException
	 */
	public void getFormMsg() throws TCException {
		getBZGXList();
		TCComponentItemRevision rev = (TCComponentItemRevision) formComp
				.getReferenceProperty("item_revision");// ��ȡ�����ڵİ汾
		if (rev == null) {
			System.out.println("û���ҵ��汾����");
			return;
		}
		TCComponentItem item = rev.getItem();
		if (item == null) {
			System.out.println("û���ҵ�����");
			return;
		}
		AIFComponentContext[] aifContext = item.whereReferencedByTypeRelation(
				new String[] { "HX3_WLRevision" },
				new String[] { "HX3_GYLXRelation" });
		if (aifContext == null || aifContext.length == 0
				|| aifContext.length > 1) {
			System.out.println("��ǰ����·�߹����˶�����ϰ汾������");
		}
		firstBean = new BomStructBean();
		TCComponentBOMLine topLine = null;
		if(aifContext != null && aifContext.length>0){
			TCComponentItemRevision wlRev = ((TCComponentItemRevision) aifContext[0]
					.getComponent());
			bomMsg = HxomMethodUtil.getTopLineByRev2(wlRev);
			 topLine = bomMsg.line;
		}
		if (topLine == null) {
			System.out.println("��ǰ����û��BOM��Ϣ������");
			return;

		}
		exchangeLineToBean(firstBean, topLine);

	}

	/**
	 * ����FORM��ʱ���ȡ����
	 */
	public void getFormVlue() {

		this.valueLists = new ArrayList<>();
		try {

			// ��ȡ��ֵ����
			propertiesArray = formComp.getTCProperties(TM2_PROCESS_PROP_NAMES);
			// д���ֵ���Ե�����
			if (propertiesArray != null) {
				String[] valueStrs0 = propertiesArray[0] == null ? new String[0]
						: propertiesArray[0].getStringArrayValue();
				String[] valueStrs1 = propertiesArray[1] == null ? new String[0]
						: propertiesArray[1].getStringArrayValue();
				String[] valueStrs2 = propertiesArray[2] == null ? new String[0]
						: propertiesArray[2].getStringArrayValue();
				String[] valueStrs3 = propertiesArray[3] == null ? new String[0]
						: propertiesArray[3].getStringArrayValue();
				String[] valueStrs4 = propertiesArray[4] == null ? new String[0]
						: propertiesArray[4].getStringArrayValue();
				String[] valueStrs5 = propertiesArray[5] == null ? new String[0]
						: propertiesArray[5].getStringArrayValue();
				boolean[] valueStrs6 = propertiesArray[6] == null ? new boolean[0]
						: propertiesArray[6].getBoolArrayValue();
				boolean[] valueStrs7 = propertiesArray[7] == null ? new boolean[0]
						: propertiesArray[7].getBoolArrayValue();
				boolean[] valueStrs8 = propertiesArray[8] == null ? new boolean[0]
						: propertiesArray[8].getBoolArrayValue();
				boolean[] valueStrs9 = propertiesArray[9] == null ? new boolean[0]
						: propertiesArray[9].getBoolArrayValue();
				boolean[] valueStrs10 = propertiesArray[10] == null ? new boolean[0]
						: propertiesArray[10].getBoolArrayValue();
				Date[] valueStrs11 = propertiesArray[11] == null ? new Date[0]
						: propertiesArray[11].getDateValueArray();// Date
				Date[] valueStrs12 = propertiesArray[12] == null ? new Date[0]
						: propertiesArray[12].getDateValueArray();// Date
				String[] valueStrs13 = propertiesArray[13] == null ? new String[0]
						: propertiesArray[13].getStringArrayValue();
				String[] valueStrs14 = propertiesArray[14] == null ? new String[0]
						: propertiesArray[14].getStringArrayValue();
				String[] valueStrs15 = propertiesArray[15] == null ? new String[0]
						: propertiesArray[15].getStringArrayValue();// TypedReference
				String qrgg = propertiesArray[16] == null?"":propertiesArray[16] .getStringValue();
				if(qrgg.endsWith("(ȷ�ϸ���)")){
					this.isSure = true;
				}else{
					this.isSure = false;
				}
				if (valueStrs0.length == valueStrs1.length
						&& valueStrs0.length == valueStrs2.length
						&& valueStrs0.length == valueStrs3.length
						&& valueStrs0.length == valueStrs4.length
						&& valueStrs0.length == valueStrs5.length
						&& valueStrs0.length == valueStrs6.length
						&& valueStrs0.length == valueStrs7.length
						&& valueStrs0.length == valueStrs8.length
						&& valueStrs0.length == valueStrs9.length
						&& valueStrs0.length == valueStrs10.length
						&& valueStrs0.length == valueStrs11.length
						&& valueStrs0.length == valueStrs12.length
						&& valueStrs0.length == valueStrs13.length
						&& valueStrs0.length == valueStrs14.length
						&& valueStrs0.length == valueStrs15.length

						&& valueStrs0.length != 0) {
					for (int i = 0; i < valueStrs0.length; i++) {
						HX3_GYLXRevisionFormPropBean bean = new HX3_GYLXRevisionFormPropBean();
						bean.hx3_gxhh = valueStrs0[i];// "�����к�",

						bean.hx3_bzgx = valueStrs1[i];// "��׼����",
						bean.hx3_gxsm = valueStrs2[i];// "����˵��",
						bean.hx3_gzzx = valueStrs3[i];// "��������",
						bean.hx3_gzzxmc = valueStrs4[i];// "������������",// 5
						bean.hx3_mjbh = valueStrs5[i];// "ģ�߱��",
						bean.hx3_dcgx1 = valueStrs6[i];// "���幤��",
						bean.hx3_bgd1 = valueStrs7[i];// "�����",
						bean.hx3_wwgx1 = valueStrs8[i];// "ί�⹤��",
						bean.hx3_jfd1 = valueStrs9[i];// "�Ʒѵ�",// 10
						bean.hx3_jhwwgx1 = valueStrs10[i];// "�ƻ�ί�⹤��",

						bean.hx3_sxrq = valueStrs11[i];// "��Ч����",
						bean.hx3_shixrq = valueStrs12[i];// "ʧЧ����",
						bean.hx3_gywjbh = valueStrs13[i];// "�����ļ����",
						bean.hx3_gywjbb = valueStrs14[i];// "�����ļ��汾", // 15
						bean.hx3_lywl = valueStrs15[i];// "��������",

						this.valueLists.add(bean);
						
						
					}
				}
			}
			Collections.sort(this.valueLists);
		} catch (TCException e) {
			e.printStackTrace();
		}

	}

	
	
	/**
	 * ��������
	 */
	public boolean saveWLProps() {
//		int rowCount = this.partsTable.getRowCount();
//		for(int i = 0 ;i < rowCount;i++){
//			HX3_GYLXRevisionFormPropBean bean = valueLists.get(i);
//			
//		}
		
		
		
		if (valueLists == null) {
			System.out.println("�����б�Ϊ��");
			return true;
		}
		Collections.sort(this.valueLists);
		// List<String> gxbbList = new ArrayList<>();
		// for (int i = 0; i < valueLists.size(); i++) {
		// HX3_GYLXRevisionFormPropBean bean = valueLists.get(i);
		//
		// }
		try {
			HxomMethodUtil.setByPass(true);
		} catch (TCException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String[] valueStrs0 = new String[valueLists.size()];
		String[] valueStrs1 = new String[valueLists.size()];
		String[] valueStrs2 = new String[valueLists.size()];
		String[] valueStrs3 = new String[valueLists.size()];
		String[] valueStrs4 = new String[valueLists.size()];
		String[] valueStrs5 = new String[valueLists.size()];

		boolean[] valueStrs6 = new boolean[valueLists.size()];
		boolean[] valueStrs7 = new boolean[valueLists.size()];
		boolean[] valueStrs8 = new boolean[valueLists.size()];
		boolean[] valueStrs9 = new boolean[valueLists.size()];
		boolean[] valueStrs10 = new boolean[valueLists.size()];
		Date[] valueStrs11 = new Date[valueLists.size()];
		Date[] valueStrs12 = new Date[valueLists.size()];
		String[] valueStrs13 = new String[valueLists.size()];
		String[] valueStrs14 = new String[valueLists.size()];
		String[] valueStrs15 = new String[valueLists.size()];

//		for (int i = 0; i < valueLists.size(); i++) {
//			HX3_GYLXRevisionFormPropBean bean = valueLists.get(i);
//			valueStrs0[i] = bean.hx3_gxhh == null ? "" : bean.hx3_gxhh;
//			valueStrs1[i] = bean.hx3_bzgx == null ? "" : bean.hx3_bzgx;
//			valueStrs2[i] = bean.hx3_gxsm == null ? "" : bean.hx3_gxsm;
//			valueStrs3[i] = bean.hx3_gzzx == null ? "" : bean.hx3_gzzx;
//			valueStrs4[i] = bean.hx3_gzzxmc == null ? "" : bean.hx3_gzzxmc;
//			valueStrs5[i] = bean.hx3_mjbh == null ? "" : bean.hx3_mjbh;
//			valueStrs6[i] = bean.hx3_dcgx1;
//			valueStrs7[i] = bean.hx3_bgd1;
//			valueStrs8[i] = bean.hx3_wwgx1;
//			valueStrs9[i] = bean.hx3_jfd1;
//			valueStrs10[i] = bean.hx3_jhwwgx1;
//			valueStrs11[i] = bean.hx3_sxrq;
//			valueStrs12[i] = bean.hx3_shixrq;
//			valueStrs13[i] = bean.hx3_gywjbh == null ? "" : bean.hx3_gywjbh;
//			valueStrs14[i] = bean.hx3_gywjbb == null ? "" : bean.hx3_gywjbb;
//			valueStrs15[i] = bean.hx3_lywl == null ? "" : bean.hx3_lywl;
//		}
		//20170426
		for (int i = 0; i < valueLists.size(); i++) {
		HX3_GYLXRevisionFormPropBean bean = valueLists.get(i);
		//this.partsTable.getValueAt(i, 0)
		valueStrs0[i] =(String) this.partsTable.getValueAt(i, 0) == null ? "" : (String) this.partsTable.getValueAt(i, 0);
		valueStrs1[i] = (String) this.partsTable.getValueAt(i, 1)==null ? "" : (String) this.partsTable.getValueAt(i, 1);
		valueStrs2[i] = (String) this.partsTable.getValueAt(i, 2) == null ? "" : (String) this.partsTable.getValueAt(i, 2);
		valueStrs3[i] = (String) this.partsTable.getValueAt(i, 3) == null ? "" : (String) this.partsTable.getValueAt(i, 3);
		valueStrs4[i] = (String) this.partsTable.getValueAt(i, 4) == null ? "" : (String) this.partsTable.getValueAt(i, 4);
		valueStrs5[i] = (String) this.partsTable.getValueAt(i, 5) == null ? "" : (String) this.partsTable.getValueAt(i, 5);
		valueStrs6[i] = (Boolean)this.partsTable.getValueAt(i, 6) ;
		valueStrs7[i] = (Boolean)this.partsTable.getValueAt(i, 7) ;
		valueStrs8[i] = (Boolean)this.partsTable.getValueAt(i, 8) ;
		valueStrs9[i] = (Boolean)this.partsTable.getValueAt(i, 9) ;
		valueStrs10[i] = (Boolean)this.partsTable.getValueAt(i, 10) ;
		valueStrs11[i] = bean.hx3_sxrq;
		valueStrs12[i] = bean.hx3_shixrq;
		valueStrs13[i] = (String)this.partsTable.getValueAt(i, 13)  == null ? "" : (String)this.partsTable.getValueAt(i, 13);
		valueStrs14[i] = (String)this.partsTable.getValueAt(i, 14) == null ? "" : (String)this.partsTable.getValueAt(i, 14);
		//if(valueStrs13[i].equals("") || valueStrs14[i] .equals("")){
		//	return false;
		//}
		valueStrs15[i] = (String)this.partsTable.getValueAt(i, 15) == null ? "" : (String)this.partsTable.getValueAt(i, 15);
		}
		try {
			if (propertiesArray[0] != null)
				propertiesArray[0].setStringValueArray(valueStrs0);
			if (propertiesArray[1] != null)
				propertiesArray[1].setStringValueArray(valueStrs1);
			if (propertiesArray[2] != null)
				propertiesArray[2].setStringValueArray(valueStrs2);
			if (propertiesArray[3] != null)
				propertiesArray[3].setStringValueArray(valueStrs3);
			if (propertiesArray[4] != null)
				propertiesArray[4].setStringValueArray(valueStrs4);
			if (propertiesArray[5] != null)
				propertiesArray[5].setStringValueArray(valueStrs5);
			if (propertiesArray[6] != null)
				propertiesArray[6].setLogicalValueArray(valueStrs6);
			if (propertiesArray[7] != null)
				propertiesArray[7].setLogicalValueArray(valueStrs7);
			if (propertiesArray[8] != null)
				propertiesArray[8].setLogicalValueArray(valueStrs8);
			if (propertiesArray[9] != null)
				propertiesArray[9].setLogicalValueArray(valueStrs9);
			if (propertiesArray[10] != null)
				propertiesArray[10].setLogicalValueArray(valueStrs10);
			if (propertiesArray[11] != null)
				propertiesArray[11].setDateValueArray(valueStrs11);
			if (propertiesArray[12] != null)
				propertiesArray[12].setDateValueArray(valueStrs12);
			if (propertiesArray[13] != null)
				propertiesArray[13].setStringValueArray(valueStrs13);
			if (propertiesArray[14] != null)
				propertiesArray[14].setStringValueArray(valueStrs14);
			if (propertiesArray[15] != null)
				propertiesArray[15].setStringValueArray(valueStrs15);

			formComp.setTCProperties(propertiesArray);
			if(this.sureButton.isSelected()){
				formComp.setStringProperty(hx3_qrgg, "(ȷ�ϸ���)");
			}else{
				formComp.setStringProperty(hx3_qrgg, "()");
			}
			List<TCComponent> lineList = new ArrayList<>();
			List<TCProperty> propList = new ArrayList<>();

			if (this.firstBean != null) {

				if (this.firstBean.getChlidLineS() != null) {

					for (BomStructBean structBean : this.firstBean
							.getChlidLineS()) {
						setBomlineProp(structBean, lineList, propList);
					}
				}

				TCProperty[][] props = new TCProperty[propList.size()][1];

				for (int j = 0; j < propList.size(); j++) {
					props[j][0] = propList.get(j);
				}

				if (bomMsg.window != null) {
					bomMsg.window.lock();
					TCComponentType.setPropertiesSet(
							lineList.toArray(new TCComponent[lineList.size()]),
							props);
					bomMsg.window.save();
					bomMsg.window.unlock();
					bomMsg.window.close();

				}

			}

		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			HxomMethodUtil.setByPass(false);
		} catch (TCException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return true;
	}

	/**
	 * 
	 * @param bean
	 * @throws TCException
	 */
	public void setBomlineProp(BomStructBean bean, List<TCComponent> lineList,
			List<TCProperty> propList) throws TCException {

		if (bean == null) {
			return;
		}
		lineList.add(bean.getBomLine());
		TCProperty prop = bean.getBomLine().getTCProperty("HX3_gxhh");
		prop.setStringValue(bean.getGxh());
		// bean.getBomLine().lock();
		// bean.getBomLine().setTCProperty(prop);
		// bean.getBomLine().save();
		// bean.getBomLine().unlock();
		propList.add(prop);

		if (bean.getChlidLineS() != null && bean.getChlidLineS().size() > 0) {
			for (BomStructBean child : bean.getChlidLineS()) {
				setBomlineProp(child, lineList, propList);
			}
		}

	}

	/**
	 * ǩ�벢����
	 */
	@Override
	public boolean checkForSave(Object obj) {
		// TODO Auto-generated method stub
		over = true;
		if(!saveWLProps()){
			MessageBox.post("����[�����ļ����]����[�����ļ��汾]����Ϊ��","����",MessageBox.WARNING);
			isSavable(false);
			
			return false;
		}
		
		System.out.println("ǩ�벢����");
		return super.checkForSave(obj);
		//return false;
	}
	

	/**
	 * ����
	 */
	@Override
	public void save() {
		// TODO Auto-generated method stub
		over = true;
		if(!saveWLProps()){
			MessageBox.post("����[�����ļ����]����[�����ļ��汾]����Ϊ��","����",MessageBox.WARNING);
			return ;
		}

		System.out.println("����");
		super.save();
	}

	public void init() {
		System.out.println("====>>>>  ��ʼ������  <1>");
		// clorMap = new HashMap<Integer, String>();
		// fontMap = new HashMap<Integer, Integer>();
		valueLists = new ArrayList<HX3_GYLXRevisionFormPropBean>();
		getFormVlue();
		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE);
		rootPanel = new JPanel(new BorderLayout());
		addButton = new JButton("���");

		addButton.setEnabled(true);
		insertButton = new JButton("����");
		insertButton.setEnabled(true);
		delButton = new JButton("ɾ��");
		delButton.setEnabled(true);
		sureButton = new JCheckBox("ȷ�ϸ���",isSure);
		
		sureButton.setEnabled(true);
		
		
	
		
		emptyLable = new JLabel("    ");

		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addRow(e);
			}
		});

		insertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				insertRow(e);
			}
		});

		delButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				delRow(e);
			}
		});
		// TODO ���Table�����
		this.partsTable = getjTable(partsTable, this.dtm, this.titleNames,
				getValues(valueLists));
		final MouseInputListener mouseInputListener = getMouseInputListener(partsTable);// �������Ҽ�ѡ����

		partsTable.addMouseListener(mouseInputListener);

		this.topPanel = new JPanel(new BorderLayout());
		scrollPanel = new JScrollPane();
		scrollPanel.getViewport().add(partsTable, null);
		scrollPanel.validate();

		this.topPanel.add(BorderLayout.CENTER, scrollPanel);

		this.buttomPanel = new JPanel(new ButtonLayout(ButtonLayout.VERTICAL));
		this.buttomPanel.add(addButton);
		this.buttomPanel.add(new JLabel("    "));
		this.buttomPanel.add(insertButton);
		this.buttomPanel.add(emptyLable);
		this.buttomPanel.add(delButton);
		this.buttomPanel.add(new JLabel("    "));
		this.buttomPanel.add(sureButton);

		this.rootPanel = new JPanel(new BorderLayout());
		this.rootPanel.add(BorderLayout.CENTER, topPanel);
		// this.rootPanel.add(BorderLayout.SOUTH, buttomPanel);

		JTabbedPane tabPane = new JTabbedPane();

		
		String title = "����·����Ϣ";
		if(titleName!=null)
		{
			title = titleName;
		}
		tabPane.add(title, rootPanel);

		this.setLayout(new BorderLayout());
		this.add(buttomPanel, BorderLayout.EAST);
		this.add(tabPane, BorderLayout.CENTER);
	}

	private void addRow(ActionEvent e) {

		HX3_GYLXRevisionFormPropBean bean = new HX3_GYLXRevisionFormPropBean();
		try {
			bean.hx3_sxrq = sdf.parse("2000-01-01");
			bean.hx3_shixrq = sdf.parse("2099-12-31");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Object[] objs = new Object[] { bean.hx3_gxhh, bean.hx3_bzgx,
				bean.hx3_gxsm, bean.hx3_gzzx, bean.hx3_gzzxmc, bean.hx3_mjbh,
				bean.hx3_dcgx1, bean.hx3_bgd1, bean.hx3_wwgx1, bean.hx3_jfd1,
				bean.hx3_jhwwgx1, sdf.format(bean.hx3_sxrq),
				sdf.format(bean.hx3_shixrq), bean.hx3_gywjbh, bean.hx3_gywjbb,
				bean.hx3_lywl };

		valueLists.add(bean);
		((DefaultTableModel) this.partsTable.getModel()).addRow(objs);

		((DefaultTableModel) this.partsTable.getModel())
				.fireTableStructureChanged();
		((DefaultTableModel) this.partsTable.getModel()).fireTableDataChanged();
		scrollPanel.validate();

	}

	private void insertRow(ActionEvent e) {
		int index = partsTable.getSelectedRow();
		if (partsTable.getRowCount() == 0) {
			index = 0;
		}
		// if (index == -1) {
		// MessageBox.post("��ѡ�������ִ��ɾ��������", "Warning", MessageBox.WARNING);
		// return;
		// }
		HX3_GYLXRevisionFormPropBean bean = new HX3_GYLXRevisionFormPropBean();
		try {
			bean.hx3_sxrq = sdf.parse("2000-01-01");
			bean.hx3_shixrq = sdf.parse("2099-12-31");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Object[] objs = new Object[] { bean.hx3_gxhh, bean.hx3_bzgx,
				bean.hx3_gxsm, bean.hx3_gzzx, bean.hx3_gzzxmc, bean.hx3_mjbh,
				bean.hx3_dcgx1, bean.hx3_bgd1, bean.hx3_wwgx1, bean.hx3_jfd1,
				bean.hx3_jhwwgx1, sdf.format(bean.hx3_sxrq),
				sdf.format(bean.hx3_shixrq), bean.hx3_gywjbh, bean.hx3_gywjbb,
				bean.hx3_lywl };
		if (index != -1) {
			valueLists.add(index, bean);
			((DefaultTableModel) this.partsTable.getModel()).insertRow(index,
					objs);
		} else {
			valueLists.add(bean);
			((DefaultTableModel) this.partsTable.getModel()).addRow(objs);
		}

		((DefaultTableModel) this.partsTable.getModel())
				.fireTableStructureChanged();
		((DefaultTableModel) this.partsTable.getModel()).fireTableDataChanged();
		scrollPanel.validate();
	}

	private void delRow(ActionEvent e) {
		int index = partsTable.getSelectedRow();
		if (index == -1) {
			MessageBox.post("��ѡ�������ִ��ɾ��������", "����", MessageBox.WARNING);
			return;
		}
		if(valueLists.get(index).hx3_lywl!=null){
			if(!valueLists.get(index).hx3_lywl.trim().isEmpty()){
				MessageBox.post("���Ƴ����������Ϻ���ִ��ɾ��������", "����", MessageBox.WARNING);
				return;
			}
		}
		valueLists.remove(index);
		((DefaultTableModel) this.partsTable.getModel()).removeRow(index);
		((DefaultTableModel) this.partsTable.getModel())
				.fireTableStructureChanged();
		((DefaultTableModel) this.partsTable.getModel()).fireTableDataChanged();
		scrollPanel.validate();
	}

	/***************************************************/

	/**
	 * JTABLEͨ�÷���
	 * 
	 * @param partsTable
	 * @param titleNames
	 * @return
	 */
	public JTable getjTable(JTable partsTable, DefaultTableModel dtm,
			Object[] titleNames, Object[][] values) {
		int simpleLen = 72;
		int totleLen = 900;
		if (partsTable == null) {
			//partsTable.isCellEditable(arg0, arg1)
		
			partsTable = new JTable(getTableModel(dtm, titleNames, values)) {
				@Override
				public boolean isCellEditable(int row, int column) {

					if (column == 15|| column == 1|| (column >1 && column < 5)
							|| (column > 5 && column < 13)){
						return false;
					}else if(column == 0){
						if(valueLists.get(row).hx3_lywl ==null || valueLists.get(row).hx3_lywl.trim().isEmpty()){
							return true;
						}
						return false;
					}
					else{
						
						
						
						return true;
					}
				}

			};

			partsTable.setDefaultRenderer(Object.class,
					new TableCellTextAreaRenderer());

			partsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			if (simpleLen * titleNames.length >= totleLen) {
				for (int i = 0; i < titleNames.length; i++) {
					partsTable.getColumnModel().getColumn(i)
							.setPreferredWidth(72);
				}
				partsTable.setAutoResizeMode(0);

			} else {
				// System.out.println("auto size");
				partsTable.setAutoResizeMode(1);
			}

		}
		return partsTable;
	}

	public DefaultTableModel getTableModel(DefaultTableModel dtm,
			Object[] columNameObjects, Object[][] objects) {
		// Object[] columNameObjects = this.titleNames; //// ,
		// Object[][] objects = getValues(this.valueLists);
		if (dtm == null) {
			dtm = new DefaultTableModel(objects, columNameObjects);
		}
		return dtm;
	}

	public Object[][] getValues(List<HX3_GYLXRevisionFormPropBean> beanList) {
		Object[][] objects = new Object[beanList.size()][HX3_GYLXRevisionFormPropBean.COUNT];

		for (int i = 0; i < beanList.size(); i++) {
			// objects[i][0] =beanList.get(i).getIndex();
			objects[i][0] = beanList.get(i).hx3_gxhh;// "�����к�",

			objects[i][1] = beanList.get(i).hx3_bzgx;// "��׼����",
			objects[i][2] = beanList.get(i).hx3_gxsm;// "����˵��",
			objects[i][3] = beanList.get(i).hx3_gzzx;// "��������",
			objects[i][4] = beanList.get(i).hx3_gzzxmc;// "������������",// 5
			objects[i][5] = beanList.get(i).hx3_mjbh;// "ģ�߱��",
			objects[i][6] = beanList.get(i).hx3_dcgx1;// "���幤��",
			objects[i][7] = beanList.get(i).hx3_bgd1;// "�����",
			objects[i][8] = beanList.get(i).hx3_wwgx1;// "ί�⹤��",
			objects[i][9] = beanList.get(i).hx3_jfd1;// "�Ʒѵ�",// 10
			objects[i][10] = beanList.get(i).hx3_jhwwgx1;// "�ƻ�ί�⹤��",
			objects[i][11] = sdf.format(beanList.get(i).hx3_sxrq);// "��Ч����",
			objects[i][12] = sdf.format(beanList.get(i).hx3_shixrq);// "ʧЧ����",
			objects[i][13] = beanList.get(i).hx3_gywjbh;// "�����ļ����",
			objects[i][14] = beanList.get(i).hx3_gywjbb;// "�����ļ��汾", // 15
			objects[i][15] = beanList.get(i).hx3_lywl;// "��������",
			// objects[i][16] = beanList.get(i).hx3_lywlid;
		}
		return objects;
	}

	public void setClipboard(String str) {
		StringSelection ss = new StringSelection(str);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
	}

	/**************************************************/
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadRendering() throws TCException {
		// TODO Auto-generated method stub
		init();
	}

	@Override
	public void saveRendering() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {

	}

	/**
	 * ����BOM
	 * 
	 * @param bean
	 * @param line
	 * @throws TCException
	 */
	public void exchangeLineToBean(BomStructBean bean, TCComponentBOMLine line)
			throws TCException {
		if (line == null && bean != null) {
			return;
		}
		bean.setBomLine(line);
		List<BomStructBean> childBeanList = new ArrayList<>();

		AIFComponentContext[] contextS = line.getChildren();
		if (contextS != null) {
			for (AIFComponentContext context : contextS) {
				BomStructBean childBean = new BomStructBean();
				TCComponentBOMLine childLine = (TCComponentBOMLine) context
						.getComponent();
				// childBean.setBomLine(childLine);
				childBeanList.add(childBean);
				exchangeLineToBean(childBean, childLine);
			}
		}
		bean.setChlidLineS(childBeanList);

	}

	/**
	 * 
	 * �������Ҽ�����ѡ�����������ʾ�Ҽ��˵�
	 */
	private MouseInputListener getMouseInputListener(final JComponent jTable) {
		return new MouseInputListener() {
			public void mouseClicked(MouseEvent e) {
				processEvent(e);
				System.out.println("  =>>>> mouseClicked");
			}

			public void mousePressed(MouseEvent e) {
				processEvent(e);
				System.out.println("  =>>>> mousePressed");
			}

			public void mouseReleased(MouseEvent e) {
				processEvent(e);
				System.out.println(" =>>> mouseReleased");
				System.out
						.println(" jTable.isEnabled() =" + jTable.isEnabled());
				if (!jTable.isEnabled()) {
					System.out.println("�ؼ�����ʹ��");
					return;
				}
				if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0
						&& !e.isControlDown() && !e.isShiftDown()) {
					// popupMenu.show(tableLyz, e.getX(), e.getY());//�Ҽ��˵���ʾ
					JMenu mainMenu = new JMenu("�༭");
					JMenuItem wlMenu = new JMenuItem("��������");
					wlMenu.addMouseListener(new MouseListener() {
						@Override
						public void mouseReleased(MouseEvent arg0) {

						}

						@Override
						public void mousePressed(MouseEvent arg0) {
							System.out.println("++++++++++++++");
							// new Thread(bomJTree).start();
//							selectTableBean = valueLists.get(((JTable) jTable)
//									.getSelectedRow());
//							if (selectTableBean.hx3_gxhh == null
//									|| selectTableBean.hx3_gxhh.trim()
//											.isEmpty()) {
//								MessageBox.post("�����к�Ϊ�գ�����д�����к�֮���������", "����",
//										MessageBox.WARNING);
							int selectIndex = ((JTable) jTable).getSelectedRow();
							String selectGxhh = (String)((JTable) jTable).getValueAt(selectIndex, 0);
							if(selectGxhh ==null || selectGxhh.trim().isEmpty()){
								MessageBox.post("�����к�Ϊ�գ�����д�����к�֮���������", "����",
										MessageBox.WARNING);
							} else {
								bomJTree.showDialog(selectIndex,selectGxhh);
							}

						}

						@Override
						public void mouseExited(MouseEvent arg0) {
							// TODO Auto-generated method stub
							// System.out.println("3333333333");
						}

						@Override
						public void mouseEntered(MouseEvent arg0) {
							// TODO Auto-generated method stub
							// System.out.println("2222222222");
						}

						@Override
						public void mouseClicked(MouseEvent arg0) {
							// TODO Auto-generated method stub
							// System.out.println("1111111111");
						}
					});
					//JMenuItem d = new JMenuItem("�Ƴ���������");
					JMenuItem gylxMenu = new JMenuItem("������׼����");
					gylxMenu.addMouseListener(new MouseListener(){

						@Override
						public void mouseClicked(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mouseEntered(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mouseExited(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mousePressed(MouseEvent arg0) {
							// TODO Auto-generated method stub
							//selectTableBean = valueLists.get();
							int selectIndex = ((JTable) jTable)
									.getSelectedRow();
							
							String gylxGxhh = (String)((JTable) jTable).getValueAt(selectIndex, 1);
							String gylxGxsm =(String)((JTable) jTable).getValueAt(selectIndex, 2);
							
							bzgxDialog.showDialog(gylxGxhh,gylxGxsm,selectIndex);
							
						}

						@Override
						public void mouseReleased(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}
						
						
					});
					mainMenu.add(wlMenu);
					mainMenu.add(gylxMenu);
					//a.add(d);
					JPopupMenu propMenu = new JPopupMenu();
					propMenu = mainMenu.getPopupMenu();
					propMenu.show(jTable, e.getX(), e.getY());

				}

			}

			public void mouseEntered(MouseEvent e) {

				processEvent(e);

			}

			public void mouseExited(MouseEvent e) {

				processEvent(e);

			}

			public void mouseDragged(MouseEvent e) {

				processEvent(e);

			}

			public void mouseMoved(MouseEvent e) {

				processEvent(e);

			}

			private void processEvent(MouseEvent e) {

				if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {

					int modifiers = e.getModifiers();

					modifiers -= MouseEvent.BUTTON3_MASK;

					modifiers |= MouseEvent.BUTTON1_MASK;

					MouseEvent ne = new MouseEvent(e.getComponent(), e.getID(),

					e.getWhen(), modifiers, e.getX(), e.getY(), e

					.getClickCount(), false);

					jTable.dispatchEvent(ne);
				}
			}
		};
	}

	class TableCellTextAreaRenderer extends JTextArea implements
			TableCellRenderer {
		private BZGXBean bean = null;

		public TableCellTextAreaRenderer() {
			setLineWrap(true);
			setWrapStyleWord(true);
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			// ���㵱���е���Ѹ߶�
			int maxPreferredHeight = 0;
			for (int i = 0; i < table.getColumnCount(); i++) {
				setText("" + table.getValueAt(row, i));
				setSize(table.getColumnModel().getColumn(column).getWidth(), 0);
				maxPreferredHeight = Math.max(maxPreferredHeight,
						getPreferredSize().height);
			}

			if (table.getRowHeight(row) != maxPreferredHeight) // ��������������Ϲæ
				table.setRowHeight(row, maxPreferredHeight);

			setText(value == null ? "" : value.toString());
			// table.setSelectionBackground(Color.yellow);
			Font fp = new Font("Menu.font", Font.PLAIN, 14);
			Font fb = new Font("Menu.font", Font.BOLD, 14);
			if (isSelected) {
				this.setFont(fb);

			} else {
				this.setFont(fp);
			}

			if (isSelected
					&& hasFocus
					&& row == table.getSelectedRow()
					&& column == table.getSelectedColumn()
					&& !((column ==1)||(column > 2 && column < 5) || (column > 5 && column < 11))) {

				bean = null;
				System.out.println(" => " + table.getValueAt(row, 2));
				if (bzgxMap.containsKey(table.getValueAt(row, 2))) {
					List<BZGXBean> bzgxBL = bzgxMap.get(table.getValueAt(row, 2)) ;
					
					if(bzgxBL.size()==1){
						bean = bzgxBL.get(0);
					}else if(bzgxBL.size()>1){
						//11
						//HX3_SelectBzgxDialog dialog =new  HX3_SelectBzgxDialog(bzgxBL);
						//new Thread(dialog).start();
//						while(dialog.index ==-1){
//							try {
//								new Thread().sleep(1000);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
						bean = bzgxBL.get(0);
//						dialog.index = -1;
					}
					
					
					System.out.println("contains bzgxBL ="+bzgxBL.size());
				} else {
					System.out.println("not contains ");
				}
				//�޸�����20170426
//				switch (column) {
//				case 0:
//					valueLists.get(row).hx3_gxhh = value == null ? "" : value
//							.toString();
//					break;
//				case 2:
//					if (bean == null) {
//
//						valueLists.get(row).hx3_bzgx = "";
//						table.setValueAt("", row, 1);
//						valueLists.get(row).hx3_gzzx = "";
//						table.setValueAt("", row, 3);
//						valueLists.get(row).hx3_gzzxmc = "";
//						table.setValueAt("", row, 4);
//						valueLists.get(row).hx3_dcgx1 = false;
//						table.setValueAt(false, row, 6);
//						valueLists.get(row).hx3_bgd1 = false;
//						table.setValueAt(false, row, 7);
//						valueLists.get(row).hx3_wwgx1 = false;
//						table.setValueAt(false, row, 8);
//						valueLists.get(row).hx3_jfd1 = false;
//						table.setValueAt(false, row, 9);
//						valueLists.get(row).hx3_jhwwgx1 = false;
//						table.setValueAt(false, row, 10);
//						valueLists.get(row).hx3_gxsm = value == null ? ""
//								: value.toString();
//					} else {
//						valueLists.get(row).hx3_bzgx = bean.hx3_gxbz;
//						table.setValueAt(bean.hx3_gxbz, row, 1);
//						valueLists.get(row).hx3_gzzx = bean.hx3_gzzx;
//						table.setValueAt(bean.hx3_gzzx, row, 3);
//						valueLists.get(row).hx3_gzzxmc = bean.hx3_gzzxmc;
//						table.setValueAt(bean.hx3_gzzxmc, row, 4);
//						valueLists.get(row).hx3_dcgx1 = bean.hx3_dcgx1;
//						table.setValueAt(bean.hx3_dcgx1, row, 6);
//						valueLists.get(row).hx3_bgd1 = bean.hx3_bgd1;
//						table.setValueAt(bean.hx3_bgd1, row, 7);
//						valueLists.get(row).hx3_wwgx1 = bean.hx3_wwgx1;
//						table.setValueAt(bean.hx3_wwgx1, row, 8);
//						valueLists.get(row).hx3_jfd1 = bean.hx3_jfd1;
//						table.setValueAt(bean.hx3_jfd1, row, 9);
//						valueLists.get(row).hx3_jhwwgx1 = bean.hx3_jhwwgx1;
//						table.setValueAt(bean.hx3_jhwwgx1, row, 10);
//						valueLists.get(row).hx3_gxsm = (String) value;
//					}
//
//					break;
//				
//				case 3:
//					// if (bean != null) {
//					// value = bean.hx3_gzzx;
//					// } else {
//					// value = "";
//					// }
//					//
//					// valueLists.get(row).hx3_gzzx = value == null ? "" : value
//					// .toString();
//					break;
//				case 4:
//					// if (bean != null) {
//					// value = bean.hx3_gzzxmc;
//					// } else {
//					// value = "";
//					// }
//					// valueLists.get(row).hx3_gzzxmc = value == null ? "" :
//					// value
//					// .toString();
//					break;
//				case 5:
//
//					valueLists.get(row).hx3_mjbh = value == null ? "" : value
//							.toString();
//					break;
//				case 6:
//					// if (bean != null) {
//					// value = bean.hx3_dcgx1;
//					// } else {
//					// value = false;
//					// }
//					// valueLists.get(row).hx3_dcgx1 = (boolean) value;
//					break;
//				case 7:
//					// if (bean != null) {
//					// value = bean.hx3_bgd1;
//					// } else {
//					// value = false;
//					// }
//					// valueLists.get(row).hx3_bgd1 = (boolean) value;
//
//					break;
//				case 8:
//					// if (bean != null) {
//					// value = bean.hx3_wwgx1;
//					// } else {
//					// value = false;
//					// }
//					// valueLists.get(row).hx3_wwgx1 = (boolean) value;
//
//					break;
//				case 9:
//					// if (bean != null) {
//					// value = bean.hx3_jfd1;
//					// } else {
//					// value = false;
//					// }
//					// valueLists.get(row).hx3_jfd1 = (boolean) value;
//
//					break;
//				case 10:
//					// if (bean != null) {
//					// value = bean.hx3_jhwwgx1;
//					// } else {
//					// value = false;
//					// }
//					// valueLists.get(row).hx3_jhwwgx1 = (boolean) value;
//
//					break;
//
//				case 13:
//					valueLists.get(row).hx3_gywjbh = value == null ? "" : value
//							.toString();
//					break;
//				case 14:
//					valueLists.get(row).hx3_gywjbb = value == null ? "" : value
//							.toString();
//					break;
//
//				default:
//					break;
//
//				}

				// 2.���õ�ǰCell����ɫ
				// Component c = super.getTableCellRendererComponent(table,
				// value,
				// isSelected, hasFocus, row, column);
				this.setBackground(Color.yellow);// ���ñ���ɫ
				this.setForeground(Color.BLACK);// ����ǰ��ɫ
				return this;
			} else {

				// 3.���õ����У�ż���е���ɫ
				if (row % 2 == 0) {// ż����ʱ����ɫ
					setBackground(Color.white);
				} else if (row % 2 == 1) {// ���õ����е���ɫ
					setBackground(Color.white);
				}
				if (isSelected) {
					this.setBackground(Color.LIGHT_GRAY);// ���ñ���ɫ
				}
				return this;
			}
			//
			
			// return this;
		}
	}

}
