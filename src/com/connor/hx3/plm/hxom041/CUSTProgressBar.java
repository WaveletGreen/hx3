package com.connor.hx3.plm.hxom041;

import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.util.PropertyLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

public class CUSTProgressBar extends AbstractAIFDialog implements
		ActionListener {
	private static final long serialVersionUID = 1L;
	private JProgressBar progressbar;
	private JLabel label;
	private Timer timer;
	private boolean bool = false;
	private String showLable = null;

	public CUSTProgressBar(String showlable,boolean isTrue) {
		super(isTrue);
		this.showLable = showlable;
	}
	
	public CUSTProgressBar(String showlable) {
		super(true);
		this.showLable = showlable;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}

	public void initUI() {
		Container container = getContentPane();
		JPanel mainPanel = new JPanel(new PropertyLayout());
		this.label = new JLabel(this.showLable, 0);
		this.progressbar = new JProgressBar();
		this.progressbar.setOrientation(0);
		this.progressbar.setMinimum(0);
		this.progressbar.setMaximum(100);
		this.progressbar.setValue(0);
		this.progressbar.setPreferredSize(new Dimension(200, 15));// 设置进度条大小
		this.progressbar.setBorderPainted(true);
		this.timer = new Timer(50, this);
		this.timer.setRepeats(false);
		mainPanel.add("1.1.center", new JLabel(" "));
		mainPanel.add("2.1.center", this.label);
		mainPanel.add("3.1.center", this.progressbar);
		mainPanel.add("4.1.center", new JLabel(" "));

		// mainPanel.add("1.1.left.center", new JLabel(" "));
		// mainPanel.add("1.2.left.center", new JTextField());
		// mainPanel.add("2.1.left.center", new JLabel(" "));
		// mainPanel.add("2.2.left.center", new JTextField());

		container.add(mainPanel);
		pack();
		setLocation(500, 200);
		TaskThread thread = new TaskThread(this);
		thread.start();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				CUSTProgressBar.this.bool = true;
			}
		});
		setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0) {
	}

	class TaskThread extends Thread {
		private CUSTProgressBar bar;

		public TaskThread(CUSTProgressBar bar) {
			this.bar = bar;
		}

		public void run() {
			for (int i = 0; i < i + 1; i++) {
				CUSTProgressBar.this.timer.start();
				int value = CUSTProgressBar.this.progressbar.getValue();
				if (value < 100) {
					value += 5;
					CUSTProgressBar.this.progressbar.setValue(value);
				} else {
					CUSTProgressBar.this.timer.stop();
					CUSTProgressBar.this.progressbar.setValue(0);
				}
				try {
					sleep(100L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (CUSTProgressBar.this.bool) {
					this.bar.setVisible(false);
					this.bar.dispose();
					return;
				}
			}
		}
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}

}