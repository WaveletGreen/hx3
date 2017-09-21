package com.connor.hx3.plm.hxom041;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;

import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCComponentUser;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.DateButton;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PropertyLayout;

public class ReportDialog extends AbstractAIFDialog implements ActionListener{

	private AbstractAIFApplication app;
	private TCSession session;
	private InterfaceAIFComponent targetComp;
	
	private JLabel nameLabel;
	private JLabel startLabel;
	private JLabel endLabel;
	private JLabel fileLabel; 
	
	private JTextField textField;
	private DateButton startButton;
	private DateButton endButton;
	private JButton pathButton;
	private JButton okButton;
	private JButton celButton;
	
	// �ļ�ѡ����
	private JFileChooser jFileChooser;
	private List<ReportBean> beanList;
	public ReportDialog(AbstractAIFApplication app) {
		super(false);
		this.app = app;
		this.session = (TCSession) app.getSession();
		this.targetComp = app.getTargetComponent();

	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		initUI();
	}
	
	public void initUI(){
		
		// �õ�ϵͳ��fileview
		FileSystemView fsv = FileSystemView.getFileSystemView();
		// ��ǰ�û�������·��
		String deskPath = fsv.getHomeDirectory().getPath();	
		
		this.setSize( new Dimension(600, 400));
		this.setTitle("ͼֽ�·�ͳ��");
		
		this.nameLabel = new JLabel("����ʱ�䣺");
		this.startLabel = new JLabel("��");
		this.startButton = new DateButton();
		this.endLabel = new JLabel("ֹ");
		this.endButton = new DateButton();
		this.fileLabel = new JLabel("����λ�ã�");
		this.textField = new JTextField(deskPath);
		
		jFileChooser = new JFileChooser();
		this.jFileChooser.setCurrentDirectory(new File(deskPath));// �ļ�ѡ�����ĳ�ʼĿ¼��Ϊ��ǰ�û�����
		this.jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		this.pathButton = new JButton("...");
		this.okButton = new JButton("ȷ��");
		this.celButton = new JButton("ȡ��");
		
		addActionEvent();
		
		JPanel midJPanel = new JPanel(new PropertyLayout());
		//-------------�߿� TitleBorder--------------------------------------------
		midJPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.WHITE), "��ѡ������"));
		midJPanel.add("1.1.left.top", nameLabel);
		midJPanel.add("1.2.left.top", startLabel);
		midJPanel.add("1.3.left.top", startButton);
		midJPanel.add("1.4.left.top", endLabel);
		midJPanel.add("1.5.left.top", endButton);
		midJPanel.add("2.1.left.top", fileLabel);
		midJPanel.add("2.2.left.top", textField);
		midJPanel.add("2.3.left.top", pathButton);
		
		JPanel rootJPanel = new JPanel(new FlowLayout());
		rootJPanel.add(okButton);
		rootJPanel.add(celButton);
		
		JPanel centerJPanel = new JPanel(new BorderLayout());
		centerJPanel.add(midJPanel, BorderLayout.CENTER);
		centerJPanel.add(rootJPanel ,BorderLayout.SOUTH);
		
		this.setLayout(new BorderLayout());
		this.add(centerJPanel, BorderLayout.CENTER);
		this.setVisible(true);
	    this.centerToScreen();
	    this.pack();
	    this.showDialog();
		
	}
	
	/**
	 * �Կؼ���� ActionListener �¼�����
	 */
	public void addActionEvent() {
		this.pathButton.addActionListener(this);
		this.okButton.addActionListener(this);
		this.celButton.addActionListener(this);
	}
	
	/**
	 * �ļ���ѡ��ť�¼�
	 * 
	 * @param e
	 */
	public void selectFileButtonEvent() {
		jFileChooser.setFileSelectionMode(1);// �趨ֻ��ѡ���ļ���
		int state = jFileChooser.showOpenDialog(null);// �˾��Ǵ��ļ�ѡ��������Ĵ������
		if (state == 1) {
			return;
		} else {
			File f = jFileChooser.getSelectedFile();// fΪѡ�񵽵�Ŀ¼
			textField.setText(f.getAbsolutePath());
		}
	}
	
	/**
	 * ȷ����ť�¼�
	 */
	public void okEvent(){
		
		SimpleDateFormat adf = new SimpleDateFormat("yyyy-M-dd HH:mm");
		Date startdatevalue  = this.startButton.getDate();
		Date enddatevalue = this.endButton.getDate();
		
		InterfaceAIFComponent[] resultCompS = null;
		
		List<String> valueList = new ArrayList<String>();
		valueList.add("N");
		List<String> nameList = new ArrayList<String>();
		nameList.add("�Ƿ��ⷢͼֽ");
		if(enddatevalue != null){
			
			//valueList.add("N");
			valueList.add(adf.format(enddatevalue));
			nameList.add("������������");
			System.out.println("endvalue is" + adf.format(enddatevalue));
			//���ò�ѯ
			//resultCompS = JFomMethodUtil.searchComponentsCollection(session, "�·�ͼֽͳ��",
			//		new String[] { "�Ƿ��ⷢͼֽ","������������" }, valueList.toArray(new String[valueList.size()]));
		}
		if(startdatevalue != null){
			//List<String> valueList = new ArrayList<String>();
			//valueList.add("N");
			valueList.add(adf.format(startdatevalue));
			nameList.add("������������");
			//���ò�ѯ
			//resultCompS = JFomMethodUtil.searchComponentsCollection(session, "�·�ͼֽͳ��",
			//		new String[] { "�Ƿ��ⷢͼֽ","������������" }, valueList.toArray(new String[valueList.size()]));
		}
		
			
		//���ò�ѯ
		resultCompS = JFomMethodUtil.searchComponentsCollection(session, "�·�ͼֽͳ��",
					nameList.toArray(new String[nameList.size()]), valueList.toArray(new String[valueList.size()]));
		
		
		//�жϲ�ѯ����Ƿ�Ϊ��
		if (resultCompS == null || resultCompS.length == 0) {
			MessageBox.post("��ѯ���Ϊ��", "����", MessageBox.WARNING);
			return;
		}	
		
		List<TCComponentItemRevision> revList = new ArrayList<TCComponentItemRevision>();
		for (InterfaceAIFComponent comp : resultCompS) {
			revList.add((TCComponentItemRevision) comp);
		}
		try {
			getRevMessage(revList);
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		InputStream is = ReportDialog.class.getResourceAsStream("�·�ͼֽͳ��.xlsx");
		try{
			ExcelUtil07.writeExcel(textField.getText() + "\\" + "�·�ͼֽͳ��.xlsx", is ,beanList);
			MessageBox.post("��������ɹ�", "INFO", MessageBox.INFORMATION);
		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			MessageBox.post("��������ʧ��", "ERROR", MessageBox.ERROR);
		}

	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		Object sourceObj = arg0.getSource();
		if (sourceObj.equals(this.pathButton)) {
			selectFileButtonEvent();
		}else if (sourceObj.equals(this.okButton)) {
			//this.disposeDialog();
			//TransfProgressBarThread thread = new TransfProgressBarThread("Export Report", "Process .....");
			//thread.start();
			//okEvent();
			ReportOpreation operation = new ReportOpreation(this);
			this.session.queueOperation(operation);
			//thread.stopBar();
			//this.showDialog();
	    }else if (sourceObj.equals(this.celButton)) {
	    	this.disposeDialog();
			this.dispose();
		}
	}
	
	/**
	 * ��ȡ����
	 * 
	 * @param revList
	 * @throws TCException
	 */
	public void getRevMessage(List<TCComponentItemRevision> revList)
			throws TCException {
		if (revList == null) {
			System.out.println("revlistΪ��");
			return;
		}
		List<TCComponentItem> itemList = new ArrayList<TCComponentItem>();
		List<TCComponentItemRevision> relationList = new ArrayList<TCComponentItemRevision>();
		this.beanList = new ArrayList<ReportBean>();
		
		TCProperty[][] propssRev = TCComponentType.getTCPropertiesSet(
				revList, new String[] {  "owning_user", "date_released", "HX3_ffsj"});
		int number = 0;
		for(int i = 0;i <propssRev.length;i++ ){
			SimpleDateFormat adf = new SimpleDateFormat("yyyy-M-dd HH:mm");
			
			TCComponentUser user = (TCComponentUser) propssRev[i][0].getReferenceValue();
			String ownuser = user.getStringProperty("user_name");
			
			// ��ȡ�������Ե�ֵ
			Date date = propssRev[i][1].getDateValue();
			String data = adf.format(date);
			
			TCProperty iman = propssRev[i][2];
			TCComponent[] values = iman.getReferenceValueArray();
			if (values != null && values.length != 0) {
				for(int num = 0; num < values.length; num ++){
					ReportBean bean = new ReportBean();
					TCComponentItemRevision ffsj;
					if(values[num] instanceof TCComponentItemRevision ){
						ffsj = (TCComponentItemRevision) values[num];
					}else{
						ffsj = ((TCComponentItem)values[num]).getLatestItemRevision();
					}
					String Objtype = ffsj.getStringProperty("object_type");
					if(Objtype.equals("HX3_ZPTRevision") || Objtype.equals("HX3_LJTRevision")){
						bean.setIndex("" + (number + 1));
						number++;
					
						String ffsjRev = ffsj.getTCProperty("item_revision_id").getStringValue();
						String ffsjItemId = ffsj.getItem().getProperty("item_id");
						String ffsjName = ffsj.getItem().getProperty("object_name");
						bean.setItem_revision_id(ffsjRev);
						bean.setItem_id(ffsjItemId);
						bean.setObject_name(ffsjName);
						if (ownuser != null){
							bean.setOwning_user(ownuser);
						}else{
							bean.setOwning_user("");
						}
						if (data != null){
							bean.setDate_released(data);
						}else{
							bean.setOwning_user("");
						}
						beanList.add(bean);
					 }
					
				}
			}
		}
	}
}
