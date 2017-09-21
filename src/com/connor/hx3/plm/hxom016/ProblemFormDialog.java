package com.connor.hx3.plm.hxom016;

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
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentDataset;
import com.teamcenter.rac.kernel.TCComponentDatasetType;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentForm;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.kernel.TCTypeService;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PropertyLayout;

public class ProblemFormDialog extends AbstractAIFDialog {
	private AbstractAIFUIApplication app;
	private static TCSession session;
	private JPanel inputPanel;
	private static JTextField cpxh;
	private JButton okbutton;
	private JButton celbutton;
	private JPanel buttonPanel;
	private JPanel rootPanel;
	private String searchName;
	private String[] keys;
	private InterfaceAIFComponent[] com;
	private String[] values;
	private List<propBean> partBeanList;
	private static InterfaceAIFComponent comment;
	private FileOutputStream output;
	private String name;
	private SimpleDateFormat sdf;
	private static File file;
	private static Object dataset;

	public ProblemFormDialog(String name, AbstractAIFUIApplication app,
			TCSession session) throws Exception {
		super(false);
		this.name = name;
		this.app = app;
		ProblemFormDialog.session = session;
		comment = app.getTargetComponent();
		if (comment != null && (comment instanceof TCComponentFolder)) {
			String folderType = comment.getType();
			// if(folderType.equals("HX3_SJBWJJ")){
			System.out.println(folderType);
			init();
		} else {
			MessageBox.post("��ѡ����ȷ���ļ���", "��ʾ", MessageBox.ERROR);
		}
	}

	public void init() {
		setTitle("���-�����ͳ��");
		this.setAutoRequestFocus(true);
		this.setSize(new Dimension(400, 300));
		this.setResizable(false);
		cpxh = new JTextField(20);
		okbutton = new JButton("ȷ��");
		celbutton = new JButton("ȡ��");
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
		inputPanel.add("2.1.RIGHT.TOP", new JLabel(" ��Ʒ�ͺ�  "));
		inputPanel.add("2.2.RIGHT.TOP", cpxh);

		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(okbutton);
		buttonPanel.add(new JLabel("     "));
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
		// TODO Auto-generated method stub
		searchName = "check_WTD2";

		keys = new String[] { "hx3_cpxh" };
		String val = cpxh.getText();
		if (val == null || val.trim().isEmpty()) {
			MessageBox.post("��������ȷ�Ĳ�Ʒ�ͺ�", "����", MessageBox.ERROR);
			return;
		}
		values = new String[] { val };

		com = MethodUtil.searchComponentsCollection(
				(TCSession) app.getSession(), searchName, keys, values);
		if (com == null) {
			MessageBox.post("�ò�Ʒ�ͺŲ�ѯΪ��", "����", MessageBox.ERROR);
			System.out.println("ok");
			return;
		} else {
			System.out.println(com.length);
			if (com.length == 0) {
				MessageBox.post("û�и�ѡ��������", "��ʾ", MessageBox.INFORMATION);
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
		List<TCComponentForm> formList = new ArrayList<TCComponentForm>();
		partBeanList = new ArrayList<propBean>();

		for (TCComponentItemRevision rev : revList) {
			itemList.add(rev.getItem());
		}
		// this.partBeanList.clear();

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
						bean.setrelease_status("��");
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
						bean.setrelease_status("��");
					}
				} else {
					bean.setrelease_status("��");
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
						bean.sethx3_jjcs(propssForm[i][3].getStringValue());// �õ�String���͵�����
					} else {
						bean.sethx3_jjcs("");
					}
					if (propssForm[i][4] != null) {
						bean.sethx3_zrbm(propssForm[i][4].getDisplayableValue());
					} else {
						bean.sethx3_zrbm("");
					}
					if (propssForm[i][5] != null) {
						bean.sethx3_clzt(propssForm[i][5].getDisplayValue());// �õ����Ա�����ʾֵ
					} else {
						bean.sethx3_clzt("");
					}
					if (propssForm[i][6] != null) {
						bean.sethx3_yzjg(propssForm[i][6].getStringValue());
					} else {
						bean.sethx3_yzjg("");
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("����Խ�磡");
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
	 * д�����ݵ�excel����
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
		wb = new XSSFWorkbook(input);// 03��д��HSSFWorkbook;07�汾XSSFWorkbook

		MethodUtil util = new MethodUtil();
		Cell tCell = MethodUtil.getNamedCellValue1(wb, "��������");
		tCell.setCellValue(cpxh.getText() + "�����ͳ��");

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
				cell.setCellType(Cell.CELL_TYPE_STRING);// �ı���ʽ
				switch (cols) {

				case 0:
					cell.setCellValue(bean.getXUHAO());// д������
					break;
				case 1:
					cell.setCellValue(bean.getowning_user());// д������
					break;
				case 2:
					cell.setCellValue(bean.getitem_id());// д������
					break;
				case 3:
					cell.setCellValue(bean.getobject_name());// д������
					break;
				case 4:
					cell.setCellValue(bean.gethx3_wtms());// д������
					break;
				case 5:
					cell.setCellValue(bean.gethx3_zyd());// д������
					break;
				case 6:
					cell.setCellValue(bean.gethx3_yyfx());// д������
					break;
				case 7:
					cell.setCellValue(bean.gethx3_jjcs());// д������
					break;
				case 8:
					cell.setCellValue(bean.gethx3_yzjg());// д������
					break;
				case 9:
					cell.setCellValue(bean.gethx3_zrbm());// д������
					break;
				case 10:
					cell.setCellValue(bean.gethx3_clzt());// д������
					break;
				case 11:
					cell.setCellValue(bean.getrelease_status());// д������
					break;
				case 12:
					cell.setCellValue(bean.getdata_released());// д������
					break;
				}
			}
			Cell dateCell = MethodUtil.getNamedCellValue1(wb, "ͳ��ʱ��");
			dateCell.setCellValue(bean.getCurrent_date());
		}

		// /////////////////////////////////////////////////////////////////
		file = new File(System.getenv("TEMP") + "\\" + cpxh.getText()
				+ propBean.HX3_EXPORT_EXCEL);
		output = new FileOutputStream(file);
		wb.write(output);
		output.close();
		input.close();
		System.out.println("-------WRITE EXCEL OVER-------");
		try {
			((TCComponentFolder) comment).add("contents", createDataSet());// �����ݼ�����Ŀ���ļ�����
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MessageBox.post("��������㱨��ɹ�", "��ʾ", MessageBox.INFORMATION);
		this.disposeDialog();
	}

	/**
	 * ������EXCEL�������õ����ݼ���
	 * 
	 */
	public static TCComponentDataset createDataSet() {
		dataset = null;
		TCTypeService type = session.getTypeService();
		TCComponentDatasetType datasettype;
		try {
			String wjjName = ((TCComponentFolder) comment)
					.getProperty("object_name");
			datasettype = (TCComponentDatasetType) type
					.getTypeComponent("Dataset");
			dataset = datasettype.create(cpxh.getText()
					+ propBean.HX3_EXPORT_EXCEL, "EXCEL IMPORT", "MSExcelX");
			// create( dataset_name , dataset_desc , dataset_type)��
			// create(excel�ļ��������������ͣ���
			String p[] = new String[1];
			String n[] = new String[1];
			p[0] = file.getAbsolutePath();// excel����·��
			n[0] = "excel";// ����������
			((TCComponentDataset) dataset).setFiles(p, n);

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("����Խ�磡");
			e.printStackTrace();
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (TCComponentDataset) dataset;
	}

	protected void celEvents(ActionEvent e) {
		// TODO Auto-generated method stub
		this.dispose();
	}

}
