package com.connor.hx3.plm.hxom027;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.connor.hx3.plm.hxom016.MethodUtil;
import com.connor.hx3.plm.hxom016.propBean;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.kernel.ListOfValuesInfo;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentForm;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentListOfValues;
import com.teamcenter.rac.kernel.TCComponentListOfValuesType;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.DateButton;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PropertyLayout;

public class ProblemStatisticsDialog extends AbstractAIFDialog {

	private AbstractAIFUIApplication app;
	private TCSession session;
	private JTextField cpxh, wtmc, yyfx, bblj;
	private JButton okbutton;
	private JButton celbutton;
	private DateButton datebutton;
	private DateButton datebutton2;
	private JButton filebutton;
	private JPanel buttonPanel;
	private JPanel inputPanel;
	private JPanel rootPanel;
	private JFileChooser filechooser;
	private String[] keys;
	private String[] values;
	private String path;
	private List<propBean> partBeanList;
	private JComboBox wtdlb, zyd, xmjd, clzt, zrbm;
	private String searchName;
	private InterfaceAIFComponent[] com;
	private String[] listValue;
	private String[] displayValue;
	private String[] listValue1;
	private String[] displayValue1;
	private String[] displayValue2;
	private String[] listValue2;
	private String[] listValue3;
	private String[] displayValue3;
	private String[] listValue4;
	private String[] displayValue4;
	private SimpleDateFormat sdf;

	public ProblemStatisticsDialog(String dialogName,
			AbstractAIFUIApplication app, TCSession session) throws Exception {
		super(false);
		this.app = app;
		this.session = session;
		init();
	}

	public void init() {
		setTitle("问题统计");
		this.setAutoRequestFocus(true);
		this.setSize(new Dimension(500, 500));
		this.setResizable(false);
		cpxh = new JTextField();
		cpxh.setPreferredSize(new Dimension(160, 25));
		wtmc = new JTextField();
		wtmc.setPreferredSize(new Dimension(160, 25));
		yyfx = new JTextField();
		yyfx.setPreferredSize(new Dimension(160, 25));
		bblj = new JTextField(25);
		bblj.setPreferredSize(new Dimension(250, 25));

		if (listValue == null) {
			try {
				TCComponentListOfValues lovListOfValue = TCComponentListOfValuesType
						.findLOVByName("HX3_zyd");
				ListOfValuesInfo lovListOfValueInfo;
				lovListOfValueInfo = lovListOfValue.getListOfValues();
				displayValue = lovListOfValueInfo.getLOVDisplayValues();
				listValue = new String[displayValue.length];
				for (int k = 0; k < displayValue.length; k++) {
					listValue[k] = new String(displayValue[k]);
				}
			} catch (TCException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			zyd = new JComboBox(listValue);
			zyd.setSelectedItem(null);
			zyd.setPreferredSize(new Dimension(160, 25));

		}
		if (listValue1 == null) {
			try {
				TCComponentListOfValues lovListOfValue1 = TCComponentListOfValuesType
						.findLOVByName("HX3_wtdlb");
				ListOfValuesInfo lovListOfValueInfo1;
				lovListOfValueInfo1 = lovListOfValue1.getListOfValues();
				displayValue1 = lovListOfValueInfo1.getLOVDisplayValues();
				listValue1 = new String[displayValue1.length];
				for (int k = 0; k < displayValue1.length; k++) {
					listValue1[k] = new String(displayValue1[k]);
				}
			} catch (TCException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			wtdlb = new JComboBox(listValue1);
			wtdlb.setSelectedItem(null);
			wtdlb.setPreferredSize(new Dimension(160, 25));
		}
		if (listValue2 == null) {
			try {
				TCComponentListOfValues lovListOfValue2 = TCComponentListOfValuesType
						.findLOVByName("HX3_clzt");
				ListOfValuesInfo lovListOfValueInfo2;
				lovListOfValueInfo2 = lovListOfValue2.getListOfValues();
				displayValue2 = lovListOfValueInfo2.getLOVDisplayValues();
				listValue2 = new String[displayValue2.length];
				for (int k = 0; k < displayValue2.length; k++) {
					listValue2[k] = new String(displayValue2[k]);
				}
			} catch (TCException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			clzt = new JComboBox(listValue2);
			clzt.setSelectedItem(null);
			clzt.setPreferredSize(new Dimension(160, 25));
		}
		if (listValue3 == null) {
			try {
				TCComponentListOfValues lovListOfValue3 = TCComponentListOfValuesType
						.findLOVByName("HX3_zrbm");
				ListOfValuesInfo lovListOfValueInfo3;
				lovListOfValueInfo3 = lovListOfValue3.getListOfValues();
				displayValue3 = lovListOfValueInfo3.getLOVDisplayValues();
				listValue3 = new String[displayValue3.length];
				for (int k = 0; k < displayValue3.length; k++) {
					listValue3[k] = new String(displayValue3[k]);
				}
			} catch (TCException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			zrbm = new JComboBox(listValue3);
			zrbm.setSelectedItem(null);
			zrbm.setPreferredSize(new Dimension(160, 25));
		}
		if (listValue4 == null) {
			try {
				TCComponentListOfValues lovListOfValue4 = TCComponentListOfValuesType
						.findLOVByName("HX3_xmjd");
				ListOfValuesInfo lovListOfValueInfo4;
				lovListOfValueInfo4 = lovListOfValue4.getListOfValues();
				displayValue4 = lovListOfValueInfo4.getLOVDisplayValues();
				listValue4 = new String[displayValue4.length];
				for (int k = 0; k < displayValue4.length; k++) {
					listValue4[k] = new String(displayValue4[k]);
				}
			} catch (TCException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			xmjd = new JComboBox(listValue4);
			xmjd.setSelectedItem(null);
			xmjd.setPreferredSize(new Dimension(160, 25));
		}

		SimpleDateFormat sd = session.askTCDateFormat().askDefaultDateFormat(); // 获得系统的时间格式
		datebutton = new DateButton(sd);
		datebutton2 = new DateButton(sd);
		datebutton.setPreferredSize(new Dimension(160, 25));
		datebutton2.setPreferredSize(new Dimension(160, 25));
		filechooser = new JFileChooser();
		filechooser.setCurrentDirectory(new File("c:\\"));
		filebutton = new JButton(" 选择 ");
		filebutton.setPreferredSize(new Dimension(100, 25));
		okbutton = new JButton("生成报表");
		celbutton = new JButton("退出");
		filebutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				fileEvents(e);
			}
		});

		okbutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					okEvents(e);
				} catch (TCException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		celbutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				celEvents(e);
			}
		});

		inputPanel = new JPanel(new PropertyLayout());

		inputPanel.add("1.1.RIGHT.TOP", new JLabel("   "));

		inputPanel.add("2.1.RIGHT.TOP", new JLabel("产品型号"));
		inputPanel.add("2.2.RIGHT.TOP", cpxh);
		inputPanel.add("2.3.RIGHT.TOP", new JLabel("     "));
		inputPanel.add("2.4.RIGHT.TOP", new JLabel("问题名称"));
		inputPanel.add("2.5.RIGHT.TOP", wtmc);

		inputPanel.add("3.1.RIGHT.TOP", new JLabel("原因分析"));
		inputPanel.add("3.2.RIGHT.TOP", yyfx);
		inputPanel.add("3.3.RIGHT.TOP", new JLabel("     "));
		inputPanel.add("3.4.RIGHT.TOP", new JLabel("项目阶段"));
		inputPanel.add("3.5.RIGHT.TOP", xmjd);

		inputPanel.add("4.1.RIGHT.TOP", new JLabel("问题类别"));
		inputPanel.add("4.2.RIGHT.TOP", wtdlb);
		inputPanel.add("4.3.RIGHT.TOP", new JLabel("     "));
		inputPanel.add("4.4.RIGHT.TOP", new JLabel("处理状态"));
		inputPanel.add("4.5.RIGHT.TOP", clzt);

		inputPanel.add("5.1.RIGHT.TOP", new JLabel("责任部门"));
		inputPanel.add("5.2.RIGHT.TOP", zrbm);
		inputPanel.add("5.3.RIGHT.TOP", new JLabel("     "));
		inputPanel.add("5.4.RIGHT.TOP", new JLabel("   重要度"));
		inputPanel.add("5.5.RIGHT.TOP", zyd);

		inputPanel.add("6.1.RIGHT.TOP", new JLabel(" 发起时间起"));
		inputPanel.add("6.2.RIGHT.TOP", datebutton);
		inputPanel.add("6.3.RIGHT.TOP", new JLabel("  "));
		inputPanel.add("6.4.RIGHT.TOP", new JLabel("发起时间止"));
		inputPanel.add("6.5.RIGHT.TOP", datebutton2);

		inputPanel.add("7.1.RIGHT.TOP", new JLabel("  "));
		inputPanel.add("7.2.RIGHT.TOP", new JLabel("报表路径"));
		inputPanel.add("7.3.RIGHT.TOP", bblj);
		inputPanel.add("7.4.RIGHT.TOP", filebutton);

		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(okbutton);
		buttonPanel.add(new JLabel("       "));
		buttonPanel.add(celbutton);

		rootPanel = new JPanel(new BorderLayout());
		rootPanel.add(inputPanel, BorderLayout.NORTH);
		rootPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.add(rootPanel, "Center");
		this.pack();
		this.centerToScreen(1.0D, 1.0D);
		this.showDialog();

	}

	/**
	 * 选择路径
	 * 
	 */
	protected void fileEvents(ActionEvent e) {
		// TODO Auto-generated method stub
		filechooser.setFileSelectionMode(1);
		int t = filechooser.showOpenDialog(null);
		if (t == 1) {
			return;
		} else {
			File f = filechooser.getSelectedFile();
			bblj.setText(f.getAbsolutePath());
		}
	}

	protected void okEvents(ActionEvent e) throws TCException, IOException {
		// TODO Auto-generated method stub
		searchName = "check_WTD2";

		List<String> keyList = new ArrayList<>();
		List<String> valueList = new ArrayList<>();
		Map<List, List> map = new HashMap<>();
		// String keys = new
		// String[]{"hx3_cpxh","名称","hx3_yyfx","hx3_zyd","hx3_wtdlb"
		// ,"hx3_clzt","hx3_zrbm","hx3_xmjd","发起时间早于","发起时间晚于"};
		// -----------------------------------------
		String val = cpxh.getText();
		if (!(val == null || val.isEmpty())) {
			keyList.add("hx3_cpxh");
			valueList.add(val);
		}
		System.out.println(val);
		// //

		String val1 = wtmc.getText();
		if (!(val1 == null || val1.isEmpty())) {
			keyList.add("名称");
			valueList.add(val1);

		}
		String val2 = yyfx.getText();
		if (!(val2 == null || val2.isEmpty())) {
			keyList.add("hx3_yyfx");
			valueList.add(val2);
		}

		if (!(zyd.getSelectedItem() == null)) {
			String val3 = zyd.getSelectedItem().toString();
			if (!(val3 == null || val3.isEmpty())) {
				keyList.add("hx3_zyd");
				valueList.add(val3);
			}
		}
		if (!(wtdlb.getSelectedItem() == null)) {
			String val4 = wtdlb.getSelectedItem().toString();
			if (!(val4 == null || val4.isEmpty())) {
				keyList.add("hx3_wtdlb");
				valueList.add(val4);
			}
		}
		if (!(clzt.getSelectedItem() == null)) {
			String val5 = clzt.getSelectedItem().toString();
			if (!(val5 == null || val5.isEmpty())) {
				keyList.add("hx3_clzt");
				valueList.add(val5);

			}
		}
		if (!(zrbm.getSelectedItem() == null)) {
			String val6 = zrbm.getSelectedItem().toString();
			if (!(val6 == null || val6.isEmpty())) {
				keyList.add("hx3_zrbm1");
				valueList.add(val6);

			}
		}
		if (!(xmjd.getSelectedItem() == null)) {
			String val7 = xmjd.getSelectedItem().toString();
			if (!(val7 == null || val7.isEmpty())) {
				keyList.add("hx3_xmjd");
				valueList.add(val7);
			}
		}
		String val8 = null;
		String val9 = null;// datebutton2.getText();
		// 如果早于的时间为空，那么就把时间默认为"1900-1-01 00:00";
		// 而不能用“*”代替，因为时间格式的属性比较特殊，有一定的格式
		// 要么为空的时候不添加查询条目，要么就用一个特别早的时间来代替。

		if (datebutton.getDate() != null) {
			val8 = datebutton.getText();
			System.out.println("=======" + val8);

		} else {

			val8 = "1970-1-01 00:00";
		}
		if (!(val8 == null || val8.isEmpty())) {
			keyList.add("发起时间早于");
			valueList.add(val8);

		}
		// 如果晚于的时间为空，就把晚于的时间默认为"3900-1-01 00:00"
		if (datebutton2.getDate() != null) {
			val9 = datebutton2.getText();

		} else {
			val9 = "2200-1-01 23:59";
		}
		// 发起时间晚于
		if (!(val9 == null || val9.isEmpty())) {
			keyList.add("发起时间晚于");
			valueList.add(val9);
		}

		// /---------------------------
		com = MethodUtil.searchComponentsCollection(
				(TCSession) app.getSession(), searchName,
				keyList.toArray(new String[keyList.size()]),
				valueList.toArray(new String[valueList.size()]));

		if (com == null) {
			MessageBox.post("版本为空", "错误", MessageBox.ERROR);
			System.out.println("ok");
			return;
		} else {
			System.out.println(com.length);
			if (com.length == 0) {
				MessageBox.post("没有该选项的问题点", "提示", MessageBox.INFORMATION);
				return;
			}
			List<TCComponentItemRevision> revList = new ArrayList<TCComponentItemRevision>();
			for (int a = 0; a < com.length; a++) {
				revList.add((TCComponentItemRevision) com[a]);
			}
			getRevMessage(revList);
			writeExcel(this.partBeanList);

		}
	}

	/**
	 * 获取属性
	 * 
	 * @param revList
	 * @throws TCException
	 */
	public void getRevMessage(List<TCComponentItemRevision> revList)
			throws TCException {
		if (revList == null) {
			System.out.println("revlist为空");
			return;
		}
		List<TCComponentItem> itemList = new ArrayList<TCComponentItem>();
		List<TCComponentForm> formList = new ArrayList<TCComponentForm>();
		partBeanList = new ArrayList<propBean>();

		for (TCComponentItemRevision rev : revList) {
			itemList.add(rev.getItem());
		}

		TCProperty[][] propss1 = TCComponentType.getTCPropertiesSet(revList,
				new String[] { "IMAN_master_form_rev" });
		for (TCProperty[] propss1_1 : propss1) {
			formList.add((TCComponentForm) (propss1_1[0]
					.getReferenceValueArray())[0]);
		}
		TCProperty[][] propsStatus = TCComponentType.getTCPropertiesSet(
				revList, new String[] { "release_status_list" });

		TCProperty[][] propss = TCComponentType.getTCPropertiesSet(revList,
				new String[] { "date_released" });

		TCProperty[][] propsUser = TCComponentType.getTCPropertiesSet(revList,
				new String[] { "owning_user" });

		TCProperty[][] propssItem = TCComponentType.getTCPropertiesSet(
				itemList, new String[] { "item_id", "object_name" });

		if (formList != null && formList.size() != 0) {
			TCProperty[][] propssForm = TCComponentType.getTCPropertiesSet(
					formList, new String[] { "hx3_wtms", "hx3_zyd", "hx3_yyfx",
							"hx3_jjcs", "hx3_zrbm1", "hx3_clzt", "hx3_yzjg" });

			for (int i = 0; i < propss1.length; i++) {
				propBean bean = new propBean();
				bean.setXUHAO("" + (i + 1));

				if (propsStatus[i][0] != null) {
					TCComponent[] stat = propsStatus[i][0]
							.getReferenceValueArray();
					if (stat != null && stat.length != 0) {
						bean.setrelease_status("是");
						if (propss[i][0] != null) {
							Date date = propss[i][0].getDateValue();
							if (date != null) {
								sdf = new SimpleDateFormat("yyyy-MM-dd");
								String st = sdf.format(date);
								bean.setdata_released(st);
							}
						} else {
							bean.setdata_released("");
						}
					} else {
						bean.setrelease_status("否");
					}
				} else {
					bean.setrelease_status("否");
				}
				if (propsUser[i][0] != null) {
					TCComponent use = propsUser[i][0].getReferenceValue();
					if (use != null) {
						TCProperty useName = use.getTCProperty("user_name");

						bean.setowning_user(useName.getStringValue());
					}
				} else {
					bean.setowning_user("");
				}

				if (propssItem[i][0] != null) {
					bean.setitem_id(propssItem[i][0].getStringValue());
				} else {
					bean.setitem_id("");
				}
				if (propssItem[i][1] != null) {
					bean.setobject_name(propssItem[i][1].getStringValue());
				} else {
					bean.setobject_name("");
				}
				try {
					if (propssForm[i][0] != null) {
						bean.sethx3_wtms(propssForm[i][0].getStringValue());
					} else {
						bean.sethx3_wtms("");
					}
					if (propssForm[i][1] != null) {
						bean.sethx3_zyd(propssForm[i][1].getStringValue());
					} else {
						bean.sethx3_zyd("");
					}
					if (propssForm[i][2] != null) {
						bean.sethx3_yyfx(propssForm[i][2].getStringValue());
					} else {
						bean.sethx3_yyfx("");
					}
					if (propssForm[i][3] != null) {
						bean.sethx3_jjcs(propssForm[i][3].getStringValue());
					} else {
						bean.sethx3_jjcs("");
					}
					if (propssForm[i][4] != null) {
						bean.sethx3_zrbm(propssForm[i][4].getDisplayValue());
					} else {
						bean.sethx3_zrbm("");
					}
					if (propssForm[i][5] != null) {
						bean.sethx3_clzt(propssForm[i][5].getDisplayValue());
					} else {
						bean.sethx3_clzt("");
					}
					if (propssForm[i][6] != null) {
						bean.sethx3_yzjg(propssForm[i][6].getStringValue());
					} else {
						bean.sethx3_yzjg("");
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("数组越界！");
					e.printStackTrace();
				}
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date1 = new Date();
				String st1 = sdf.format(date1);
				bean.setCurrent_date(st1);
				this.partBeanList.add(bean);
				System.out.println(this.partBeanList.size());
			}
		}
	}

	/**
	 * 检查对象是否已经发布
	 * 
	 * @param comp
	 * @return
	 * @throws TCException
	 */
	public static boolean isCompReleased(TCComponent comp) throws TCException {
		TCComponent[] comps = comp.getRelatedComponents("release_status_list");
		if (comps != null && comps.length > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 写入数据到excel报表
	 * 
	 * @param partBeanList
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void writeExcel(List<propBean> beanListAll) throws IOException {
		InputStream input = propBean.class
				.getResourceAsStream(propBean.HX3_EXPORT_EXCEL);
		// FileInputStream input = new
		// FileInputStream(propBean.HX3_EXPORT_EXCEL);
		XSSFWorkbook wb = null;
		wb = new XSSFWorkbook(input);

		MethodUtil util = new MethodUtil();
		Cell tCell = MethodUtil.getNamedCellValue1(wb, "报表名称");
		tCell.setCellValue(cpxh.getText() + "问题点统计");

		Sheet sheet = wb.getSheetAt(0);
		for (int i = 0; i < beanListAll.size(); i++) {
			propBean bean = beanListAll.get(i);
			Row row = null;
			Boolean isCreat = false;
			if ((i + 3) > sheet.getLastRowNum()) {
				row = sheet.createRow(i + 3);
				isCreat = true;
			} else {
				row = sheet.getRow(i + 3);
				isCreat = false;
			}
			for (int cols = 0; cols < 13; cols++) {
				Cell cell = null;
				if (isCreat) {
					cell = row.createCell(cols);
				} else {
					cell = row.getCell(cols);
				}
				cell.setCellType(Cell.CELL_TYPE_STRING);// 文本格式
				switch (cols) {

				case 0:
					cell.setCellValue(bean.getXUHAO());// 写入内容
					break;
				case 1:
					cell.setCellValue(bean.getowning_user());// 写入内容
					break;
				case 2:
					cell.setCellValue(bean.getitem_id());// 写入内容
					break;
				case 3:
					cell.setCellValue(bean.getobject_name());// 写入内容
					break;
				case 4:
					cell.setCellValue(bean.gethx3_wtms());// 写入内容
					break;
				case 5:
					cell.setCellValue(bean.gethx3_zyd());// 写入内容
					break;
				case 6:
					cell.setCellValue(bean.gethx3_yyfx());// 写入内容
					break;
				case 7:
					cell.setCellValue(bean.gethx3_jjcs());// 写入内容
					break;
				case 8:
					cell.setCellValue(bean.gethx3_yzjg());// 写入内容
					break;
				case 9:
					cell.setCellValue(bean.gethx3_zrbm());// 写入内容
					break;
				case 10:
					cell.setCellValue(bean.gethx3_clzt());// 写入内容
					break;
				case 11:
					cell.setCellValue(bean.getrelease_status());// 写入内容
					break;
				case 12:
					cell.setCellValue(bean.getdata_released());// 写入内容
					break;
				}
			}
			Cell dateCell = MethodUtil.getNamedCellValue1(wb, "统计时间");
			dateCell.setCellValue(bean.getCurrent_date());
		}
		FileOutputStream output = new FileOutputStream(new File(bblj.getText()
				+ "/" + cpxh.getText() + propBean.HX3_EXPORT_EXCEL));
		wb.write(output);
		output.close();
		input.close();
		System.out.println("-------WRITE EXCEL OVER-------");
		MessageBox.post("导出问题点报表成功", "提示", MessageBox.INFORMATION);
		this.disposeDialog();
	}

	/**
	 * 退出
	 * 
	 */
	protected void celEvents(ActionEvent e) {
		// TODO Auto-generated method stub
		this.dispose();
	}
}
