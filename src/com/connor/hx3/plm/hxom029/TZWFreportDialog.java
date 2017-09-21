package com.connor.hx3.plm.hxom029;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.connor.hx3.plm.hxom016.MethodUtil;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentForm;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.DateButton;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PropertyLayout;

public class TZWFreportDialog extends AbstractAIFDialog {
	private AbstractAIFUIApplication app;
	private TCSession session;
	private JTextField wfsqz;
	private JTextField wfkh;
	private DateButton datebutton;
	private DateButton datebutton2;
	private JFileChooser filechooser;
	private JButton filebutton;
	private JButton okbutton;
	private JButton celbutton;
	private JPanel inputPanel;
	private JTextField bblj;
	private JPanel buttonPanel;
	private JPanel rootPanel;
	private String searchName;
	private InterfaceAIFComponent[] com;
	private List<TZWFBean> partBeanList;
	private TCComponentItemRevision revCom;
	private TCComponent[] ffjs;
	private TCProperty[][] propssForm;
	private SimpleDateFormat sdf;

	public TZWFreportDialog(String name, AbstractAIFUIApplication app,
			TCSession session) {
		super(false);
		this.app = app;
		this.session = session;
		init();

	}

	public void init() {
		setTitle("外发图纸统计报表");
		this.setAutoRequestFocus(true);
		this.setSize(new Dimension(500, 400));
		this.setResizable(false);
		wfsqz = new JTextField();
		wfsqz.setPreferredSize(new Dimension(160, 25));
		wfkh = new JTextField();
		wfkh.setPreferredSize(new Dimension(160, 25));
		bblj = new JTextField();
		bblj.setPreferredSize(new Dimension(160, 25));
		SimpleDateFormat sd = session.askTCDateFormat().askDefaultDateFormat();
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
				fileEvents(e);
			}
		});
		okbutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
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
				celEvents(e);
			}
		});

		inputPanel = new JPanel(new PropertyLayout());

		inputPanel.add("1.1.RIGHT.TOP", new JLabel("   "));

		inputPanel.add("2.1.RIGHT.TOP", new JLabel("外发申请者"));
		inputPanel.add("2.2.RIGHT.TOP", wfsqz);

		inputPanel.add("3.1.RIGHT.TOP", new JLabel("外发客户"));
		inputPanel.add("3.2.RIGHT.TOP", wfkh);

		inputPanel.add("4.1.RIGHT.TOP", new JLabel("外发时间"));
		inputPanel.add("4.2.RIGHT.TOP", datebutton);
		inputPanel.add("4.3.RIGHT.TOP", new JLabel("  "));
		inputPanel.add("4.4.RIGHT.TOP", new JLabel("到"));
		inputPanel.add("4.5.RIGHT.TOP", datebutton2);

		inputPanel.add("5.1.RIGHT.TOP", new JLabel("  "));
		inputPanel.add("5.2.RIGHT.TOP", new JLabel("报表路径"));
		inputPanel.add("5.3.RIGHT.TOP", bblj);
		inputPanel.add("5.4.RIGHT.TOP", filebutton);

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

	protected void okEvents(ActionEvent e) throws TCException, IOException {
		searchName = "外发图纸统计";

		List<String> keyList = new ArrayList<>();
		List<String> valueList = new ArrayList<>();

		String val = wfsqz.getText();
		if (!(val == null || val.isEmpty())) {
			keyList.add("姓名");
			valueList.add(val);
		}
		System.out.println(val);

		String val1 = wfkh.getText();
		if (!(val1 == null || val1.isEmpty())) {
			keyList.add("hx3_wfkh");
			valueList.add(val1);
		}

		String val2 = null;
		String val3 = null;
		if (datebutton.getDate() != null) {
			val2 = datebutton.getText();
			System.out.println("=======" + val2);
		} else {
			val2 = "1970-1-01 00:00";
		}
		if (!(val2 == null || val2.isEmpty())) {
			keyList.add("发布日期晚于");
			valueList.add(val2);
		}
		if (datebutton2.getDate() != null) {
			val3 = datebutton2.getText();

		} else {
			val3 = "2200-1-01 23:59";
		}
		if (!(val3 == null || val3.isEmpty())) {
			keyList.add("发布日期早于");
			valueList.add(val3);
		}
		com = MethodUtil.searchComponentsCollection(
				(TCSession) app.getSession(), searchName,
				keyList.toArray(new String[keyList.size()]),
				valueList.toArray(new String[valueList.size()]));
		if (com == null) {
			MessageBox.post("版本为空", "错误", MessageBox.ERROR);
			return;
		} else {
			System.out.println(com.length);
			if (com.length == 0) {
				MessageBox.post("没有该选项的外发图纸", "提示", MessageBox.INFORMATION);
				return;
			}
			List<TCComponentItemRevision> revList = new ArrayList<TCComponentItemRevision>();
			List<TCComponentItemRevision> revList2 = new ArrayList<TCComponentItemRevision>();
			for (int a = 0; a < com.length; a++) {
				revCom = (TCComponentItemRevision) com[a];
				ffjs = revCom.getRelatedComponents("HX3_ffsj");
				if (ffjs.length != 0) {
					for (int s = 0; ffjs.length > s; s++) {
						if (ffjs[s] instanceof TCComponentItemRevision) {
							revList.add((TCComponentItemRevision) com[a]);
							System.out.println(ffjs[s].getType());
							TCComponentItemRevision ffjsRev = (TCComponentItemRevision) ffjs[s];
							revList2.add(ffjsRev);
							System.out.println(ffjsRev
									.getTCProperty("object_name"));
						}
					}
				}
			}
			getRevMessage(revList, revList2);
			writeExcel(this.partBeanList);
		}

	}

	private void writeExcel(List<TZWFBean> beanListAll) throws IOException {
		InputStream is = TZWFreportDialog.class
				.getResourceAsStream("外发图纸统计.xlsx");
		XSSFWorkbook wb = null;
		wb = new XSSFWorkbook(is);
		MethodUtil util = new MethodUtil();

		Sheet sheet = wb.getSheetAt(0);
		for (int i = 0; i < beanListAll.size(); i++) {
			TZWFBean bean = beanListAll.get(i);
			Row row = null;
			Boolean isCreat = false;
			if ((i + 2) > sheet.getLastRowNum()) {
				row = sheet.createRow(i + 2);
				isCreat = true;
			} else {
				row = sheet.getRow(i + 2);
				isCreat = false;
			}
			for (int cols = 0; cols < 7; cols++) {
				Cell cell = null;
				if (isCreat) {
					cell = row.createCell(cols);
				} else {
					cell = row.getCell(cols);
				}
				cell.setCellType(Cell.CELL_TYPE_STRING);
				switch (cols) {
				case 0:
					cell.setCellValue(bean.getXUHAO());
					break;
				case 1:
					cell.setCellValue(bean.getDrawingsIndex());
					break;
				case 2:
					cell.setCellValue(bean.getDrawingsName());
					break;
				case 3:
					cell.setCellValue(bean.getDrawingsRev());
					break;
				case 4:
					cell.setCellValue(bean.getOwning_user());
					break;
				case 5:
					cell.setCellValue(bean.getDate_released());
					break;
				case 6:
					cell.setCellValue(bean.getWFKH());
					break;
				}
			}
		}
		FileOutputStream output = new FileOutputStream(new File(bblj.getText()
				+ "/" + TZWFBean.HX3_029_EXPORT_EXCEL));
		wb.write(output);
		output.close();
		is.close();
		System.out.println("-------WRITE EXCEL OVER-------");
		MessageBox.post("导出外发图纸统计报表成功", "提示", MessageBox.INFORMATION);
		this.disposeDialog();

	}

	private void getRevMessage(List<TCComponentItemRevision> revList,
			List<TCComponentItemRevision> revList2) throws TCException {
		if (revList == null) {
			System.out.println("revlist为空");
			return;
		}
		List<TCComponentForm> formList = new ArrayList<TCComponentForm>();
		partBeanList = new ArrayList<TZWFBean>();

		TCProperty[][] propss1 = TCComponentType.getTCPropertiesSet(revList,
				new String[] { "IMAN_master_form_rev" });

		for (TCProperty[] propss1_1 : propss1) {
			formList.add((TCComponentForm) (propss1_1[0]
					.getReferenceValueArray())[0]);
		}
		if (formList != null && formList.size() != 0) {
			propssForm = TCComponentType.getTCPropertiesSet(formList,
					new String[] { "hx3_wfkh" });
		}

		TCProperty[][] propsUser = TCComponentType.getTCPropertiesSet(revList,
				new String[] { "owning_user" });
		TCProperty[][] propsDate = TCComponentType.getTCPropertiesSet(revList,
				new String[] { "date_released" });

		TCProperty[][] propsFFSJ = TCComponentType.getTCPropertiesSet(revList2,
				new String[] { "object_name", "item_id", "item_revision_id" });

		for (int i = 0; i < propss1.length; i++) {
			TZWFBean bean = new TZWFBean();
			bean.setXUHAO("" + (i + 1));

			if (propsUser[i][0] != null) {
				TCComponent use = propsUser[i][0].getReferenceValue();
				if (use != null) {
					TCProperty useName = use.getTCProperty("user_name");
					bean.setOwning_user(useName.getStringValue());
				}
			} else {
				bean.setOwning_user("");
			}

			if (propssForm[i][0] != null) {
				bean.setWFKH(propssForm[i][0].getStringValue());
			} else {
				bean.setWFKH("");
			}

			if (propsDate[i][0] != null) {
				Date date = propsDate[i][0].getDateValue();
				if (date != null) {
					sdf = new SimpleDateFormat("yyyy-MM-dd");
					String st = sdf.format(date);
					bean.setDate_released(st);
				}
			} else {
				bean.setDate_released("");
			}
			try {
				if (propsFFSJ[i][0] != null) {
					bean.setDrawingsName(propsFFSJ[i][0].getStringValue());
				} else {
					bean.setDrawingsName("");
				}
				if (propsFFSJ[i][1] != null) {
					bean.setDrawingsIndex(propsFFSJ[i][1].getStringValue());
				} else {
					bean.setDrawingsIndex("");
				}
				if (propsFFSJ[i][2] != null) {
					bean.setDrawingsRev(propsFFSJ[i][2].getStringValue());
				} else {
					bean.setDrawingsRev("");
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("数组越界！");
				e.printStackTrace();
			}
			this.partBeanList.add(bean);
			System.out.println(this.partBeanList.size());
		}
	}

	protected void fileEvents(ActionEvent e) {
		filechooser.setFileSelectionMode(1);
		int t = filechooser.showOpenDialog(null);
		if (t == 1) {
			return;
		} else {
			File f = filechooser.getSelectedFile();
			bblj.setText(f.getAbsolutePath());
		}
	}

	protected void celEvents(ActionEvent e) {
		this.dispose();
	}
}
