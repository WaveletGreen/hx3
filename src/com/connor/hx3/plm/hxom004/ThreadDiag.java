package com.connor.hx3.plm.hxom004;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ThreadDiag extends JDialog {
	private Thread currentThread = null;// ʵ�ʵ���ʱ����TestThread�������߳�
	private String messages = "";// ��ʾ�����ʾ��Ϣ
	private JDialog parentDialog = null;// ��ʾ��ĸ�����
	private JDialog clueDiag = null;// ���߳��������С���ʾ��
	private Dimension dimensions = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = dimensions.width / 4, height = 60;
	private int left = 0, top = 0;

	public ThreadDiag(JDialog parentDialog, Thread currentThread,
			String messages) {
		this.parentDialog = parentDialog;
		this.currentThread = currentThread;
		this.messages = messages;
		initDiag();
	}

	public void initDiag() {
		clueDiag = new JDialog(parentDialog, "Pro...", true);
		clueDiag.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		JPanel testPanel = new JPanel();
		JLabel testLabel = new JLabel(messages);
		clueDiag.getContentPane().add(testPanel);
		testPanel.add(testLabel);
		(new DisposeDiag()).start();// �����ر���ʾ���߳�
	}

	public void run() {
		int left = (dimensions.width - width) / 2;
		int top = (dimensions.height - height) / 2;
		clueDiag.setSize(new Dimension(width, height));
		clueDiag.setLocation(left, top);
		clueDiag.show();
	}

	public class DisposeDiag extends Thread {
		@Override
		public void run() {
			try {
				currentThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// �ȴ��������߳̽���
			clueDiag.dispose();// �ر���ʾ��
		}

	}

}
