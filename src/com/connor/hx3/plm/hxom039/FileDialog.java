package com.connor.hx3.plm.hxom039;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.kernel.TCSession;

public class FileDialog extends AbstractAIFDialog implements ActionListener{
	
	private AbstractAIFApplication app;
	private TCSession session;
	private InterfaceAIFComponent targetComp;

	private JLabel label;
	private JTextField pathfield;
	private JButton pathButton;
	private JButton okButton;
	private JButton celButton;
	
	private JFileChooser fileChooser;
	
	public FileDialog(AbstractAIFApplication app){
		super(false);
		this.app = app;
		this.session = (TCSession) app.getSession();
		this.targetComp = app.getTargetComponent();
	}
	public void initUI(){
		// �õ�ϵͳ��fileview
		FileSystemView fsv = FileSystemView.getFileSystemView();
		// ��ǰ�û�������·��
		String deskPath = fsv.getHomeDirectory().getPath();	
				
		this.setSize( new Dimension(600, 400));
		this.setTitle("��ʷ���ݹ鵵");
		this.label = new JLabel("�����ļ�:");
		this.pathfield = new JTextField(deskPath);
		this.pathButton = new JButton("...");
		this.fileChooser = new JFileChooser();
		this.fileChooser.setCurrentDirectory(new File(deskPath));// �ļ�ѡ�����ĳ�ʼĿ¼��Ϊ��ǰ�û�����
		this.fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		this.okButton = new JButton("ȷ��");
		this.celButton = new JButton("ȡ��");
		addActionEvent();
		
		JPanel centerPanel = new JPanel(new FlowLayout());
		centerPanel.add(label);
		centerPanel.add(pathfield);
		centerPanel.add(pathButton);
		
		JPanel rootJPanel = new JPanel(new FlowLayout());
		rootJPanel.add(okButton);
		rootJPanel.add(celButton);
		
		this.setLayout(new BorderLayout());
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(rootJPanel, BorderLayout.SOUTH);
		this.setVisible(true);
		this.centerToScreen();
		//this.pack();
		this.showDialog();
		
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//super.run();
		initUI();
	}
	
	/**
	 * �ļ�ѡ��ť�¼�
	 * 
	 * @param e
	 */
	public void selectFileButtonEvent() {
		
		int state = fileChooser.showOpenDialog(null);// �˾��Ǵ��ļ�ѡ��������Ĵ������
		if (state == 1) {
			return;
		} else {
			File f = fileChooser.getSelectedFile();// fΪѡ�񵽵�Ŀ¼
			pathfield.setText(f.getAbsolutePath());
		}
	}

	
	/**
	 * �Կؼ���� ActionListener �¼�����
	 */
	public void addActionEvent() {
		this.pathButton.addActionListener(this);
		this.okButton.addActionListener(this);
		this.celButton.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		Object sourceObj = arg0.getSource();
		if (sourceObj.equals(this.pathButton)) {
			selectFileButtonEvent();
		}else if (sourceObj.equals(this.okButton)) {
			okEvent();
	    }else if (sourceObj.equals(this.celButton)) {
	    	this.disposeDialog();
			this.dispose();
		}
	}
	
	/**
	 * ȷ����ť�¼�
	 */
	public void okEvent(){
		String filename = this.pathfield.getText();
		
		FileOperation operation = new FileOperation(filename , this.session, this.targetComp);
		this.session.queueOperation(operation);
		
	}

}
