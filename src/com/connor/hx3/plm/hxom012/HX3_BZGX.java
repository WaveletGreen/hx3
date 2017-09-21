package com.connor.hx3.plm.hxom012;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.teamcenter.rac.common.IEditableSwingComp;
import com.teamcenter.rac.kernel.ListOfValuesInfo;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentForm;
import com.teamcenter.rac.kernel.TCComponentListOfValues;
import com.teamcenter.rac.kernel.TCComponentListOfValuesType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.stylesheet.AbstractRendering;
import com.teamcenter.rac.util.ButtonLayout;
import com.teamcenter.rac.util.MessageBox;

public class HX3_BZGX extends AbstractRendering implements ActionListener {
	private final String[] propNameS ={"hx3_gzbz","hx3_gxsm","hx3_gzzx",//string
			"hx3_bgd1","hx3_dcgx1","hx3_jfd1","hx3_jhwwgx1","hx3_wwgx1"}; //boolean
	private final Object[] tfStr = {true,false};
	private TCComponentForm form ;
	private List<LOVBean> lovList;
	private HashMap<String, String> lovMap;
	private HashMap<String, List<BZGXBean>> bzgxBeanMap;
	private List<JTable> tableList;
	private JTabbedPane tabbedPane;
	private JButton addButton;
	private JButton addUpButton;
	private JButton delButton;
	
	/**
	 * 清理缓存
	 */
	public void cleanAll(){
		if(lovList!=null){
			lovList.clear();
		}
		if(lovMap!=null){
			lovMap.clear();
		}
		if(bzgxBeanMap!=null){
			bzgxBeanMap.clear();
		}
	}
	
	public void setPropS() throws TCException{
		boolean isSaveOk = false;
		StringBuffer errorBuffer = new StringBuffer();
		List<String> hx3_gzbzValueList = new ArrayList<>(); 
		List<String> hx3_gxsmValueList = new ArrayList<>(); 
		List<String> hx3_gzzxValueList = new ArrayList<>(); 
		List<Boolean> hx3_bgd1ValueList = new ArrayList<>(); 
		List<Boolean> hx3_dcgx1ValueList = new ArrayList<>(); 
		List<Boolean> hx3_jfd1ValueList = new ArrayList<>(); 
		List<Boolean> hx3_jhwwgx1ValueList = new ArrayList<>(); 
		List<Boolean> hx3_wwgx1ValueList = new ArrayList<>();
		TCProperty[] propSS= form.getTCProperties(propNameS);
		
		for(JTable table : tableList){
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			int rowCount = model.getRowCount();
			for(int i = 0 ;i <rowCount;i++){
				hx3_gzbzValueList.add((String) model.getValueAt(i, 0));
				
				if( model.getValueAt(i, 0) == null
						||model.getValueAt(i, 1) == null
						||((String)model.getValueAt(i, 0)).trim().isEmpty()
						||((String)model.getValueAt(i, 1)).trim().isEmpty()
						){
					errorBuffer.append("["+(String) model.getValueAt(i, 2)+"]中第["+(i+1)+"]行数据不能为空\n");
					isSaveOk = true;
					continue;
				}
				
				hx3_gxsmValueList.add((String) model.getValueAt(i, 1));
				hx3_gzzxValueList.add(lovMap.get((String) model.getValueAt(i, 2)));
				hx3_bgd1ValueList.add((boolean) model.getValueAt(i, 3));
				hx3_dcgx1ValueList.add((boolean) model.getValueAt(i, 4));
				hx3_jfd1ValueList.add((boolean) model.getValueAt(i, 5));
				hx3_jhwwgx1ValueList.add((boolean) model.getValueAt(i, 6));
				hx3_wwgx1ValueList.add((boolean) model.getValueAt(i, 7));
				
			}	
		}
		if(isSaveOk){
			MessageBox.post(errorBuffer.toString(),"错误",MessageBox.ERROR);
			return;
		}
		propSS[0].setStringValueArray(hx3_gzbzValueList.toArray(new String[hx3_gzbzValueList.size()]));
		propSS[1].setStringValueArray(hx3_gxsmValueList.toArray(new String[hx3_gxsmValueList.size()]));
		propSS[2].setStringValueArray(hx3_gzzxValueList.toArray(new String[hx3_gzbzValueList.size()]));
		boolean[] values = new boolean[hx3_bgd1ValueList.size()];
		for(int i =0;i<values.length;i++){
			values[i] = hx3_bgd1ValueList.get(i);
		}
		propSS[3].setLogicalValueArray(values);
		for(int i =0;i<values.length;i++){
			values[i] = hx3_dcgx1ValueList.get(i);
		}
		propSS[4].setLogicalValueArray(values);
		for(int i =0;i<values.length;i++){
			values[i] = hx3_jfd1ValueList.get(i);
		}
		propSS[5].setLogicalValueArray(values);
		for(int i =0;i<values.length;i++){
			values[i] = hx3_jhwwgx1ValueList.get(i);
		}
		propSS[6].setLogicalValueArray(values);
		for(int i =0;i<values.length;i++){
			values[i] = hx3_wwgx1ValueList.get(i);
		}
		propSS[7].setLogicalValueArray(values);
		
		form.lock();
		form.setTCProperties(propSS);
		form.save();
		form.unlock();
		form.refresh();
	
	}
	/**
	 * 获取属性
	 * @throws TCException
	 */
	public void getPropS() throws TCException{
		cleanAll();
		this.bzgxBeanMap = new HashMap<>();
		this.lovList = getLov("HX3_GZZX");
		if(this.lovList!=null){
			for(LOVBean bean : this.lovList){
				List<BZGXBean> bzgxBeanList = new ArrayList<>();
				
				this.bzgxBeanMap.put(bean.disValue, bzgxBeanList);
			}
		}
		TCProperty[] propSS= form.getTCProperties(propNameS);
		boolean isOk = true;
		for(int i = 0 ;i < 8;i++){
			if( propSS[i]==null){
				isOk = false;
			}
		}
		if(!isOk){
			return;
		}
		if(propSS[0].getStringArrayValue().length ==propSS[1].getStringArrayValue().length
				&&propSS[0].getStringArrayValue().length ==propSS[2].getStringArrayValue().length
				&&propSS[0].getStringArrayValue().length ==propSS[3].getBoolArrayValue().length
				&&propSS[0].getStringArrayValue().length ==propSS[4].getBoolArrayValue().length
				&&propSS[0].getStringArrayValue().length ==propSS[5].getBoolArrayValue().length
				&&propSS[0].getStringArrayValue().length ==propSS[6].getBoolArrayValue().length
				&&propSS[0].getStringArrayValue().length ==propSS[7].getBoolArrayValue().length){
			System.out.println("Array length is ok !");
		}else{
			System.out.println("Array length is not ok !");
			return;
		}
		
		for(int i = 0;i<propSS[2].getStringArrayValue().length;i++ ){
			String hx3_gzzx = propSS[2].getDisplayableValues().get(i);
			System.out.println("hx3_gzzx = "+hx3_gzzx);
			if(bzgxBeanMap.containsKey(hx3_gzzx)){
				List<BZGXBean> beanList= bzgxBeanMap.get(hx3_gzzx);
				BZGXBean bean = new BZGXBean();
				bean.hx3_gxbz = propSS[0].getStringArrayValue()[i]; // 标准工序
				bean.hx3_gxsm = propSS[1].getStringArrayValue()[i];// 工序说明
				bean.hx3_gzzx = propSS[2].getStringArrayValue()[i];// 工作中心,工作中心名称
				bean.hx3_gzzxmc = propSS[2].getDisplayableValues()
						.get(i);// 工作中心名称
				bean.hx3_dcgx1 = propSS[4].getLogicalValueArray()[i];// 倒冲工序
				bean.hx3_bgd1 = propSS[3].getLogicalValueArray()[i];// 报告点
				bean.hx3_wwgx1 = propSS[7].getLogicalValueArray()[i];// 委外工序
				bean.hx3_jfd1 = propSS[5].getLogicalValueArray()[i];// 计费点
				bean.hx3_jhwwgx1 = propSS[6].getLogicalValueArray()[i];// 计划委外工序
				beanList.add(bean);
			}
		}
	}
	
	/**
	 * 界面
	 */
	public void initUI(){
		try {
			tableList = new ArrayList<>();
			getPropS();
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setLayout(new BorderLayout());
		tabbedPane = new JTabbedPane();
		Set<Entry<String, List<BZGXBean>>> entrySet = this.bzgxBeanMap.entrySet();
		for(Entry<String, List<BZGXBean>> entry : entrySet){
			String tabName = entry.getKey();
			List<BZGXBean> beanList = entry.getValue();
			Object[][] valueSS = new Object[beanList.size()][8] ;
			for(int i = 0;i<valueSS.length;i++){
				valueSS[i][0] = beanList.get(i).hx3_gxbz ; // 标准工序
				valueSS[i][1] = beanList.get(i).hx3_gxsm ;// 工序说明
				valueSS[i][2] = beanList.get(i).hx3_gzzxmc ;// 工作中心名称
				valueSS[i][3] =  beanList.get(i).hx3_bgd1 ;// 报告点
				valueSS[i][4] =  beanList.get(i).hx3_dcgx1 ;// 倒冲工序
				valueSS[i][5] =  beanList.get(i). hx3_jfd1;// 计费点
				valueSS[i][6] =  beanList.get(i). hx3_jhwwgx1;// 计划委外工序
				valueSS[i][7] =  beanList.get(i). hx3_wwgx1;// 委外工序
			}
			
			JTable table = getjTable(null,null,new Object[]{"标准工序","工序说明","工作中心名称","报告点","倒冲工序","计费点","计划委外工序","委外工序"},valueSS);
			tableList.add(table);
			
			for(int i = 3;i < 8;i++){
				TableColumn col = table.getColumnModel().getColumn(i);
				col.setCellEditor(new MyComboBoxEditor(tfStr));
				col.setCellRenderer(new MyComboBoxRenderer(tfStr));
			}
			
			tabbedPane.addTab(tabName, new JScrollPane(table));
			
			
		}
		this.addButton = new JButton("插入");
		this.addUpButton = new JButton("向上添加");
		this.delButton = new JButton("删除");
		JPanel eastPanel = new JPanel(new ButtonLayout(ButtonLayout.VERTICAL));
		eastPanel.add(addButton);
		eastPanel.add(addUpButton);
		eastPanel.add(delButton);
		addEvent();
		this.add(tabbedPane,BorderLayout.CENTER);
		this.add(eastPanel,BorderLayout.EAST);
		
	}
	
	/**
	 * 添加事件
	 */
	public void addEvent(){
		addButton.addActionListener(this);
		addUpButton.addActionListener(this);
		delButton.addActionListener(this);
	}
	/***************************************************/

	
	
	/**
	 * JTABLE通用方法
	 * 
	 * @param partsTable
	 * @param titleNames
	 * @return
	 */
	public JTable getjTable(JTable partsTable, DefaultTableModel dtm,
			Object[] titleNames, Object[][] values) {
		int simpleLen = 105;
		int totleLen = 900;
		if (partsTable == null) {
			partsTable = new JTable(getTableModel(dtm, titleNames, values)) {
				@Override
				public boolean isCellEditable(int row, int column) {

					if (column == 2 )
						return false;
					else
						return true;
				}

			};

			//partsTable.setDefaultRenderer(Object.class,
			//		new TableCellTextAreaRenderer());

			partsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			if (simpleLen * titleNames.length >= totleLen) {
				for (int i = 0; i < titleNames.length; i++) {
					partsTable.getColumnModel().getColumn(i)
							.setPreferredWidth(105);
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
	/***
	 * 获取LOV的显示值和真实值
	 * 
	 * @param lovPropName
	 * @return
	 */
	public List<LOVBean> getLov(String lovPropName) {
		List<LOVBean> beanList = new ArrayList<>();
		this.lovMap = new HashMap<>();
		TCComponentListOfValues lobList = TCComponentListOfValuesType
				.findLOVByName(lovPropName);
		try {
			if (lobList != null) {
				ListOfValuesInfo info = lobList.getListOfValues();
				String[] valuesList = info.getStringListOfValues();
				//LOVBean bean = new LOVBean("", "");
				//beanList.add(bean);
				for (int w = 0; w < valuesList.length; w++) {
					String disValue = info.getDisplayableValue(valuesList[w]);
					lovMap.put(disValue,valuesList[w]);
					System.out.println("Lov dis =  "+valuesList[w]+" | "+disValue);
					LOVBean bean1 = new LOVBean(disValue, valuesList[w]);
					beanList.add(bean1);
				}
			}
		} catch (Exception e) {

		}
		return beanList;

	}

	public HX3_BZGX(TCComponent arg0) throws Exception {
		super(arg0);
		this.form = (TCComponentForm) arg0;
		initUI();
	}

	@Override
	public void loadRendering() throws TCException {
		System.out.println("loadRendering............");
	}

	@Override
	public void saveRendering() {
		try {
			setPropS();
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	@Override
	public void initializeRenderingDisplay() {
		// TODO Auto-generated method stub
		System.out.println("initializeRenderingDisplay ...");
		super.initializeRenderingDisplay();
	}

	@Override
	public IEditableSwingComp refreshPanel(JPanel arg0, TCComponent arg1)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("refreshPanel ...");
		return super.refreshPanel(arg0, arg1);
	}

	@Override
	public void setRefreshAfterSaveFlag(boolean arg0) {
		// TODO Auto-generated method stub
		System.out.println("setRefreshAfterSaveFlag ...");
		super.setRefreshAfterSaveFlag(arg0);
	}

	@Override
	public void updateRendering() {
		// TODO Auto-generated method stub
		System.out.println("updateRendering ...");
		super.updateRendering();
	}

	/**
	 * 监听事件
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object sourceObj = arg0.getSource();
		int index = this.tabbedPane.getSelectedIndex();
		
		String titleName = this.tabbedPane.getTitleAt(index);
		JTable table = this.tableList.get(index);
		
		int selectIndex = table.getSelectedRow();
		
		if(sourceObj.equals(this.addButton)){
			 Object[] addRowObjS ={"","",titleName , false,false,false,false,false};
			 ((DefaultTableModel)table.getModel()).addRow(addRowObjS);
		}else if(sourceObj.equals(this.addUpButton)){
			if(selectIndex !=-1){
				
				Object[] addRowObjS ={"","",titleName , false,false,false,false,false};
				((DefaultTableModel)table.getModel()).insertRow(selectIndex, addRowObjS);
			}else{
				MessageBox.post("请选择需要插入的行","错误",MessageBox.WARNING);
			}
		}else if(sourceObj.equals(this.delButton)){
			if(selectIndex !=-1){
				((DefaultTableModel)table.getModel()).removeRow(selectIndex);
			}
		}
		
	}

}

class MyComboBoxRenderer extends JComboBox implements TableCellRenderer {
	public MyComboBoxRenderer(Object[] items) {
		super(items);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			super.setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		setSelectedItem(value);
		return this;
	}
}

class MyComboBoxEditor extends DefaultCellEditor {
	
	public MyComboBoxEditor(Object[] items) {
		super(new JComboBox(items));
	}
}