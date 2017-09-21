package com.connor.hx3.plm.hxom012;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.stylesheet.AbstractRendering;
import com.teamcenter.rac.util.MessageBox;

public class WL_Form extends AbstractRendering {

	private TCComponent form;
	
	public WL_Form(TCComponent arg0) throws Exception {
		super(arg0);
		//	
		this.form = arg0;// = form;
		initUI();
	}
	
	public void initUI(){
		JLabel nameLabel = new JLabel("NAME:");
		JTextField nameField = new JTextField(16);
		JButton printButton = new JButton("Print");
		
		this.setLayout(new FlowLayout());
		this.add(nameLabel);
		this.add(nameField);
		this.add(printButton);
		
	}

	
	
	
	
	

	@Override
	public void loadRendering() throws TCException {
		// TODO Auto-generated method stub
		//MessageBox.post("load","11",MessageBox.ERROR);
		
		

	}

	
	
	@Override
	public void initializeRenderingDisplay() {
		// TODO Auto-generated method stub
		
		initUI();
		super.initializeRenderingDisplay();
	}

	@Override
	public void saveRendering() {
		// TODO Auto-generated method stub
		MessageBox.post("save()","11",MessageBox.ERROR);
		
	}
	

	

}
