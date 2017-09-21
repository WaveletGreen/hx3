package com.connor.hx3.plm.hxom028;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;

import com.connor.hx3.plm.util.HxomMethodUtil;
import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.ListOfValuesInfo;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentListOfValues;
import com.teamcenter.rac.kernel.TCComponentListOfValuesType;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.DateButton;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PropertyLayout;

//import com.teamcenter.rac.common.CalendarPanel.DateButton;

/**
 * @copyRight ���ݿ��տƼ����޹�˾
 * @author ���� E-mail:hub@connor.net.cn
 * @date ����ʱ�䣺2016-10-25 ����11:25:04
 * @version v1.0
 * @parameter
 * @since
 * @return
 */

public class BGReportDialog extends AbstractAIFDialog implements ActionListener {

	private AbstractAIFApplication app;
	private TCSession session;

	private JPanel entryPanel;
	private JPanel rootPanel;

	private JButton okButton;
	private JButton celButton;
	private JButton fileButton;

	private JTextField filePathField;// �ļ�·��

	private JTextField sqdbhField;// ���뵥���
	private JComboBox<LOVBean> ggfqfBox;// ���ķ���
	private JTextField ggcpxhField;// ���Ĳ�Ʒ�ͺ�
	private JTextField ggdbhField;// ���ĵ����
	private JComboBox<LOVBean> gglbBox;// �������
	private JTextField ggyyField;// ����ԭ��
	private JTextField lbjthField;// �㲿��ͼ��
	private JTextField lbjmcField;// �㲿������
	private DateButton ggsjDateButton;// ����ʱ��
	private DateButton ggsjDateButton2;// ����ʱ��

	private HashMap<String, String> SQDMapping;
	private HashMap<String, String> TZDMapping;

	private List<TCComponentItemRevision> SQDSearchResultList;
	private HashMap<String, TCComponentItemRevision> TZDSearchResultList;
	private JFileChooser jfc;
	private SimpleDateFormat sdf;
	private int OPTYPE = 0;

	public BGReportDialog() {
		super(false);
		this.app = AIFUtility.getCurrentApplication();
		this.session = (TCSession) app.getSession();
		this.sdf = new SimpleDateFormat("yyyy-M-dd HH:mm");
		this.SQDMapping = new HashMap<>();
		this.TZDMapping = new HashMap<>();
		this.SQDSearchResultList = new ArrayList<>();
		this.TZDSearchResultList = new HashMap<>();

	}

	// ��ʼ������
	public void initUI() {
		this.setTitle("���ͳ�Ʊ���");
		this.setSize(new Dimension(500, 500));
		FileSystemView fsv = FileSystemView.getFileSystemView();
		String deskTop = fsv.getHomeDirectory().getPath();
		jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File(deskTop));// �ļ�ѡ�����ĳ�ʼĿ¼��Ϊd��s

		okButton = new JButton("ȷ��");
		okButton.addActionListener(this);
		celButton = new JButton("�˳�");
		celButton.addActionListener(this);
		fileButton = new JButton("���");
		fileButton.addActionListener(this);

		filePathField = new JTextField(deskTop);// ����·��
		filePathField.setEditable(false);
		sqdbhField = new JTextField(16);// ���뵥���
		// ��ȡ���ķ�LOV
		List<LOVBean> ggfList = getLov("HX3_fqflov");
		ggfqfBox = new JComboBox<LOVBean>(ggfList.toArray(new LOVBean[ggfList
				.size()]));// ���ķ���
		ggcpxhField = new JTextField(16);// ���Ĳ�Ʒ�ͺ�
		ggdbhField = new JTextField(16);// ���ĵ����
		// ��ȡ������LOV
		List<LOVBean> gglbList = getLov("HX3_bgfllov");
		gglbBox = new JComboBox<LOVBean>(gglbList.toArray(new LOVBean[gglbList
				.size()]));// �������

		ggyyField = new JTextField(16);// ����ԭ��

		lbjthField = new JTextField(16);// �㲿��ͼ��

		lbjmcField = new JTextField(16);// �㲿������
		ggsjDateButton = new DateButton(this.sdf);// ����ʱ��s
		ggsjDateButton2 = new DateButton(this.sdf);// ����ʱ��s

		entryPanel = new JPanel(new PropertyLayout());
		entryPanel.setBorder(new TitledBorder(BorderFactory
				.createEtchedBorder(), "��ѯ��Ϣ"));
		entryPanel.add("1.1.LEFT.TOP", new JLabel("����·����"));
		entryPanel.add("1.2.LEFT.TOP", filePathField);
		entryPanel.add("1.3.LEFT.TOP", fileButton);

		//
		entryPanel.add("2.1.LEFT.TOP", new JLabel("���뵥��ţ�"));
		entryPanel.add("2.2.LEFT.TOP", sqdbhField);

		entryPanel.add("3.1.LEFT.TOP", new JLabel("���ķ��𷽣�"));
		entryPanel.add("3.2.LEFT.TOP", ggfqfBox);

		entryPanel.add("4.1.LEFT.TOP", new JLabel("���Ĳ�Ʒ�ͺţ�"));
		entryPanel.add("4.2.LEFT.TOP", ggcpxhField);
		//

		entryPanel.add("5.1.LEFT.TOP", new JLabel("���ĵ���ţ�"));
		entryPanel.add("5.2.LEFT.TOP", ggdbhField);

		entryPanel.add("6.1.LEFT.TOP", new JLabel("�������"));
		entryPanel.add("6.2.LEFT.TOP", gglbBox);

		entryPanel.add("7.1.LEFT.TOP", new JLabel("����ԭ��"));
		entryPanel.add("7.2.LEFT.TOP", ggyyField);

		entryPanel.add("8.1.LEFT.TOP", new JLabel("�㲿��ͼ�ţ�"));
		entryPanel.add("8.2.LEFT.TOP", lbjthField);

		entryPanel.add("9.1.LEFT.TOP", new JLabel("�㲿�����ƣ�"));
		entryPanel.add("9.2.LEFT.TOP", lbjmcField);

		entryPanel.add("10.1.LEFT.TOP", new JLabel("����ʱ�䣺"));
		entryPanel.add("10.2.LEFT.TOP", ggsjDateButton);
		entryPanel.add("10.3.LEFT.TOP", new JLabel("����"));
		entryPanel.add("10.4.LEFT.TOP", ggsjDateButton2);

		rootPanel = new JPanel(new FlowLayout());
		rootPanel.add(okButton);
		rootPanel.add(new JLabel("   "));
		rootPanel.add(celButton);

		this.setLayout(new BorderLayout());
		this.add(entryPanel, BorderLayout.CENTER);
		this.add(rootPanel, BorderLayout.SOUTH);

		this.centerToScreen();
		this.pack();
		this.showDialog();

	}

	/**
	 * ��ȡ��ѯ�Ľ�����ŵ�����
	 */
	public void searchBG() {
		System.out.println("ִ�в�ѯ");
		this.SQDSearchResultList.clear();
		this.TZDSearchResultList.clear();
		this.SQDMapping.clear();
		this.TZDMapping.clear();

		// ���뵥��ѯ
		if (!sqdbhField.getText().trim().isEmpty()) {
			SQDMapping.put("ID", sqdbhField.getText());
		}
		if (!((LOVBean) ggfqfBox.getSelectedItem()).getDisplayValue().trim()
				.isEmpty()) {
			SQDMapping.put("����",
					((LOVBean) ggfqfBox.getSelectedItem()).getDisplayValue());
		}
		if (!ggcpxhField.getText().trim().isEmpty()) {
			SQDMapping.put("��Ʒ�ͺ�", ggcpxhField.getText());

		}
		// ֪ͨ����ѯ
		if (!ggdbhField.getText().trim().isEmpty()) {
			TZDMapping.put("ID", ggdbhField.getText());
		}
		if (!((LOVBean) gglbBox.getSelectedItem()).getDisplayValue().trim()
				.isEmpty()) {
			TZDMapping.put("hx3_bgfl",
					((LOVBean) gglbBox.getSelectedItem()).getDisplayValue());
		}
		if (!ggyyField.getText().trim().isEmpty()) {
			TZDMapping.put("���ԭ��", ggyyField.getText());

		}

		if (!lbjthField.getText().trim().isEmpty()) {
			TZDMapping.put("hx3_lbjth1", lbjthField.getText());

		}
		if (!lbjmcField.getText().trim().isEmpty()) {
			TZDMapping.put("hx3_lbjmc", lbjmcField.getText());

		}

		if (ggsjDateButton.getDate() != null) {
			TZDMapping.put("date_released����",
					sdf.format(ggsjDateButton.getDate()));

		}
		if (ggsjDateButton2.getDate() != null) {
			TZDMapping.put("date_released����",
					sdf.format(ggsjDateButton2.getDate()));

		}
		// ggsjDateButton
		// ggsjDateButton2
		List<String> entryNameS = new ArrayList<>();
		List<String> entryValueS = new ArrayList<>();
		Set<Entry<String, String>> entrySet = null;
		InterfaceAIFComponent[] compS = null;
		int opType = 0;
		System.out.println("SQDMapping.size()  TZDMapping.size() "
				+ SQDMapping.size() + " " + TZDMapping.size());
		if (SQDMapping.size() != 0 && TZDMapping.size() != 0) {
			// ���ܲ�ѯSQD��ѯ����Ϣ
			entryNameS.clear();
			entryValueS.clear();
			entrySet = SQDMapping.entrySet();
			for (Entry<String, String> entry : entrySet) {
				entryNameS.add(entry.getKey());
				entryValueS.add(entry.getValue());
			}
			// ִ�в�ѯ
			compS = HxomMethodUtil.searchComponentsCollection(session, "SQD",
					entryNameS.toArray(new String[entryNameS.size()]),
					entryValueS.toArray(new String[entryValueS.size()]));
			for (InterfaceAIFComponent comp : compS) {
				SQDSearchResultList.add((TCComponentItemRevision) comp);
			}

			// ���ܲ�ѯTZD��ѯ����Ϣ
			entryNameS.clear();
			entryValueS.clear();
			entrySet = TZDMapping.entrySet();
			for (Entry<String, String> entry : entrySet) {
				entryNameS.add(entry.getKey());
				entryValueS.add(entry.getValue());
			}
			// ִ�в�ѯ
			compS = HxomMethodUtil.searchComponentsCollection(session, "TZD",
					entryNameS.toArray(new String[entryNameS.size()]),
					entryValueS.toArray(new String[entryValueS.size()]));
			for (InterfaceAIFComponent comp : compS) {
				TZDSearchResultList.put(
						((TCComponentItemRevision) comp).getUid(),
						(TCComponentItemRevision) comp);
			}
			// msgBeanList = getMsg(1);
			opType = 1;
		} else if (SQDMapping.size() != 0 && TZDMapping.size() == 0) {
			// ���ܲ�ѯSQD��ѯ����Ϣ
			entryNameS.clear();
			entryValueS.clear();
			entrySet = SQDMapping.entrySet();
			for (Entry<String, String> entry : entrySet) {
				entryNameS.add(entry.getKey());
				entryValueS.add(entry.getValue());
			}
			// ִ�в�ѯ
			compS = HxomMethodUtil.searchComponentsCollection(session, "SQD",
					entryNameS.toArray(new String[entryNameS.size()]),
					entryValueS.toArray(new String[entryValueS.size()]));
			for (InterfaceAIFComponent comp : compS) {
				SQDSearchResultList.add((TCComponentItemRevision) comp);
			}
			// msgBeanList = getMsg(2);
			opType = 2;
		} else if (SQDMapping.size() == 0 && TZDMapping.size() != 0) {
			// ���ܲ�ѯTZD��ѯ����Ϣ
			entryNameS.clear();
			entryValueS.clear();
			entrySet = TZDMapping.entrySet();
			for (Entry<String, String> entry : entrySet) {
				entryNameS.add(entry.getKey());
				entryValueS.add(entry.getValue());
			}
			// ִ�в�ѯ
			compS = HxomMethodUtil.searchComponentsCollection(session, "TZD",
					entryNameS.toArray(new String[entryNameS.size()]),
					entryValueS.toArray(new String[entryValueS.size()]));
			for (InterfaceAIFComponent comp : compS) {
				TZDSearchResultList.put(
						((TCComponentItemRevision) comp).getUid(),
						(TCComponentItemRevision) comp);
			}
			// msgBeanList = getMsg(3);
			opType = 3;
		} else {
			MessageBox.post("����������һ����ѯ����", "����", MessageBox.ERROR);
			return;
		}

		System.out.println("SQDSearchResultList.size ="
				+ SQDSearchResultList.size() + "  TZDSearchResultList.size()="
				+ TZDSearchResultList.size());
		BGReportOperation operation = new BGReportOperation(
				SQDSearchResultList, TZDSearchResultList, opType,
				this.filePathField.getText());
		this.session.queueOperation(operation);

	}

	/***
	 * ��ȡLOV����ʾֵ����ʵֵ
	 * 
	 * @param lovPropName
	 * @return
	 */
	public List<LOVBean> getLov(String lovPropName) {
		List<LOVBean> beanList = new ArrayList<>();
		TCComponentListOfValues lobList = TCComponentListOfValuesType
				.findLOVByName(lovPropName);
		try {
			if (lobList != null) {

				ListOfValuesInfo info = lobList.getListOfValues();
				String[] valuesList = info.getStringListOfValues();
				LOVBean bean = new LOVBean("", "");
				beanList.add(bean);
				for (int w = 0; w < valuesList.length; w++) {
					String disValue = info.getDisplayableValue(valuesList[w]);
					LOVBean bean1 = new LOVBean(disValue, valuesList[w]);
					beanList.add(bean1);
				}
			}
		} catch (Exception e) {

		}
		return beanList;

	}

	@Override
	public void run() {
		initUI();
	}

	public void selectFileButtonEvent(ActionEvent e) {
		jfc.setFileSelectionMode(1);// �趨ֻ��ѡ���ļ���
		int state = jfc.showOpenDialog(null);// �˾��Ǵ��ļ�ѡ��������Ĵ������
		if (state == 1) {
			return;
		} else {
			File f = jfc.getSelectedFile();// fΪѡ�񵽵�Ŀ¼
			filePathField.setText(f.getAbsolutePath());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object sorceObj = e.getSource();
		if (sorceObj.equals(okButton)) {

			searchBG();
		} else if (sorceObj.equals(celButton)) {
			this.disposeDialog();
			this.dispose();
		} else if (sorceObj.equals(fileButton)) {
			selectFileButtonEvent(e);
		}
		// ========================================

	}

}
