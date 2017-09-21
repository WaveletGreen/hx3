package com.connor.hx3.plm.hxom012;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeModel;

import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.util.ButtonLayout;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PropertyLayout;


public class HX3_SelectBzgxDialog extends AbstractAIFDialog implements ActionListener {
	private List<BZGXBean> bzgxBeanList;
	public static int index = -1;
	private JTable bzgxTable;
	private DefaultTableModel bzgxTableModel;
	private JButton okButton;
	private JButton celButton;
	
	private JTextField bzgxText;
	private JTextField gxsmText;
	private JButton queryButton;
	
	//private HX3_GYLXRevisionFormPropBean hx3Bean;
	//20170426
		private JTable partsTable;
		private int partsTableSelectIndex;
		private String gxhh;
		private String gxsm;
	//	private String gylxGxhh;
	
	private final Object[] titleS= new String[]{"��׼����","����˵��","��������","������������"
			,"���幤��","�����","ί�⹤��","�Ʒѵ�","�ƻ�ί�⹤��"};
	
	public HX3_SelectBzgxDialog(List<BZGXBean> bzgxBeanList,JTable partsTable){
		super(false);
		this.partsTable = partsTable;
		this.bzgxBeanList = bzgxBeanList;
		this.index = -1;
		initUI();
	}
	
	public Object[] transArray(BZGXBean bean){
		Object[] valueS = new Object[9];
		valueS[0] = bean.hx3_gxbz ; // ��׼����
		valueS[1] = bean.hx3_gxsm;// ����˵��
		valueS[2] = bean.hx3_gzzx ;// ��������,������������
		valueS[3] = bean.hx3_gzzxmc ;// ������������
		valueS[4] = bean. hx3_dcgx1 ;// ���幤��
		valueS[5] = bean.hx3_bgd1 ;// �����
		valueS[6] = bean. hx3_wwgx1 ;// ί�⹤��
		valueS[7] = bean. hx3_jfd1 ;// �Ʒѵ�
		valueS[8] = bean.hx3_jhwwgx1;
		return valueS;
	}
	
	public Object[][] transArrays(List<BZGXBean> bzgxBeanList){
		Object[][] valueS = new Object[bzgxBeanList.size()][9];
		for(int i = 0;i <bzgxBeanList.size();i++ ){
			BZGXBean bean = bzgxBeanList.get(i);
			valueS[i][0] = bean.hx3_gxbz ; // ��׼����
			valueS[i][1] = bean.hx3_gxsm;// ����˵��
			valueS[i][2] = bean.hx3_gzzx ;// ��������,������������
			valueS[i][3] = bean.hx3_gzzxmc ;// ������������
			valueS[i][4] = bean. hx3_dcgx1 ;// ���幤��
			valueS[i][5] = bean.hx3_bgd1 ;// �����
			valueS[i][6] = bean. hx3_wwgx1 ;// ί�⹤��
			valueS[i][7] = bean. hx3_jfd1 ;// �Ʒѵ�
			valueS[i][8] = bean.hx3_jhwwgx1;
		}
		
		return valueS;
		
	}
	
	
	public void initUI(){
		this.setVisible(false);
		this.setTitle("��׼����ѡ��");
		this.setLayout(new BorderLayout());
		//JPanel
		Object[][] valueS = transArrays(this.bzgxBeanList);
		bzgxTableModel = new DefaultTableModel(valueS, titleS);
		bzgxTable = new JTable(bzgxTableModel);
		bzgxTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		okButton = new JButton("ȷ��");
		okButton.addActionListener(this);
		celButton =new JButton("ȡ��");
		celButton.addActionListener(this);
		
		JPanel centerJPanel = new JPanel(new BorderLayout());
		centerJPanel.add(new JScrollPane(bzgxTable),BorderLayout.CENTER);
		
		JPanel bottomJPanel = new JPanel(new ButtonLayout(ButtonLayout.HORIZONTAL));
		bottomJPanel.add(okButton);
		bottomJPanel.add(celButton);
		
		JPanel nothJPanel  = new JPanel(new PropertyLayout());
		nothJPanel.setBorder(new TitledBorder(BorderFactory
				.createEtchedBorder(), "��ѯ��׼����"));
		
		this.bzgxText = new JTextField(8);
		this.bzgxText.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == 10) {
					queryButton.doClick();
				}
			}
		});
		this.gxsmText = new JTextField(16);
		this.gxsmText.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == 10) {
					queryButton.doClick();
				}
			}
		});
		this.queryButton = new JButton("��ѯ");
		this.queryButton.addActionListener(this);
		nothJPanel.add("1.1.left.top",new JLabel("��׼����"));
		nothJPanel.add("1.2.left.top",this.bzgxText);
		nothJPanel.add("2.1.left.top",new JLabel("����˵��"));
		nothJPanel.add("2.2.left.top",this.gxsmText);
		nothJPanel.add("2.3.left.top",this.queryButton);
		
		this.add(nothJPanel,BorderLayout.NORTH);
		this.add(centerJPanel,BorderLayout.CENTER);
		this.add(bottomJPanel,BorderLayout.SOUTH);
		
		this.centerToScreen();
		this.pack();
		//this.showDialog();
		this.disposeDialog();
	}

	@Override
	public void run() {
		initUI();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object sourceObject = arg0.getSource();
		
		if(sourceObject.equals(this.okButton)){
			//BZGXBean selectBean = null;
			int selectIndex  = -1;
			if(this.bzgxTableModel.getRowCount() > 1){
				selectIndex = this.bzgxTable.getSelectedRow();
				if(selectIndex ==-1){
					MessageBox.post("��ѡ���׼����","����",MessageBox.ERROR);
					return;
				}
			}else if(this.bzgxTableModel.getRowCount() ==1){
				selectIndex = 0;
			}
			if(selectIndex!=-1){
				((DefaultTableModel) partsTable.getModel())
				.setValueAt(this.bzgxTable.getValueAt(selectIndex, 0)//selectTableBean.hx3_lywl,
						,partsTableSelectIndex, 1);
				((DefaultTableModel) partsTable.getModel())
				.setValueAt(this.bzgxTable.getValueAt(selectIndex, 1)//selectTableBean.hx3_lywl,
						,partsTableSelectIndex, 2);
				((DefaultTableModel) partsTable.getModel())
				.setValueAt(this.bzgxTable.getValueAt(selectIndex, 2)//selectTableBean.hx3_lywl,
						,partsTableSelectIndex, 3);
				((DefaultTableModel) partsTable.getModel())
				.setValueAt(this.bzgxTable.getValueAt(selectIndex, 3)//selectTableBean.hx3_lywl,
						,partsTableSelectIndex, 4);
				((DefaultTableModel) partsTable.getModel())
				.setValueAt(this.bzgxTable.getValueAt(selectIndex, 4)//selectTableBean.hx3_lywl,
						,partsTableSelectIndex, 6);
				((DefaultTableModel) partsTable.getModel())
				.setValueAt(this.bzgxTable.getValueAt(selectIndex, 5)//selectTableBean.hx3_lywl,
						,partsTableSelectIndex, 7);
				((DefaultTableModel) partsTable.getModel())
				.setValueAt(this.bzgxTable.getValueAt(selectIndex, 6)//selectTableBean.hx3_lywl,
						,partsTableSelectIndex, 8);
				((DefaultTableModel) partsTable.getModel())
				.setValueAt(this.bzgxTable.getValueAt(selectIndex, 7)//selectTableBean.hx3_lywl,
						,partsTableSelectIndex, 9);
				((DefaultTableModel) partsTable.getModel())
				.setValueAt(this.bzgxTable.getValueAt(selectIndex, 8)//selectTableBean.hx3_lywl,
						,partsTableSelectIndex, 10);
				
			}
			this.dispose();
		}else if(sourceObject.equals(this.celButton)){
			this.dispose();
		}else if(sourceObject.equals(this.queryButton)){
			queryBzgx();
			
		}
	}
	
	
	/**
	 * ��ʾ����
	 * @param bean
	 */
	public void showDialog(String gxhh,String gxsm,int partsTableSelectIndex) {

		this.partsTableSelectIndex = partsTableSelectIndex;
		this.gxhh = gxhh;
		this.gxsm = gxsm;
		
		this.bzgxText.setText(gxhh);
		this.gxsmText.setText(gxsm);
		queryBzgx();
		//getSelectedItem(this.bean);
		//this.listJPanel.validate();
		this.validate();
		super.showDialog();
	}
	
	/**
	 * ��ѯ��׼����
	 */
	public void queryBzgx(){
		
		String gxsmRegEx = this.gxsmText.getText();
		//String regEx = this.currentProjectIDText.getText();

		gxsmRegEx = "^" + gxsmRegEx;
		gxsmRegEx = gxsmRegEx.replace("?", ".");
		gxsmRegEx = gxsmRegEx.replace("*", ".*");
		String bzgxRegEx = this.bzgxText.getText();
		//String regEx = this.currentProjectIDText.getText();

		bzgxRegEx = "^" + bzgxRegEx;
		bzgxRegEx = bzgxRegEx.replace("?", ".");
		bzgxRegEx = bzgxRegEx.replace("*", ".*");
		
		for(int i =0 ;i < this.bzgxTableModel.getRowCount();){
			this.bzgxTableModel.removeRow(i);
		}
		System.out.println("bzgxRegEx = "+bzgxRegEx + " | "+gxsmRegEx);
		//List<BZGXBean> bzgxListT = new ArrayList<>();
		for(BZGXBean bean : this.bzgxBeanList){
			
			boolean bzgx =  Pattern.compile(bzgxRegEx).matcher(bean.hx3_gxbz).find();
			boolean gxsm =  Pattern.compile(gxsmRegEx).matcher(bean.hx3_gxsm).find();
			System.out.println("bzgx = " +bean.hx3_gxbz + "=>" + bzgx);
			System.out.println("gxsm = " +bean.hx3_gxsm + "=>" + gxsm);
			if(bzgx && gxsm){
				//bzgxListT.add(bean);
				this.bzgxTableModel.addRow(transArray(bean));
			}
		}
	}
	
}
