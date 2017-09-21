package com.connor.hx3.plm.hxom030;

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
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.connor.hx3.plm.hxom016.MethodUtil;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.kernel.TCComponentForm;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PropertyLayout;

public class StatisticalReportDialog extends AbstractAIFDialog {
	private AbstractAIFUIApplication app;
	private TCSession session;
	private JRadioButton radioButton1;
	private JRadioButton radioButton2;
	private JTextField chooseText;
	private JButton okButton;
	private JButton celButton;
	private JTextField fileText1;
	private JTextField fileText2;
	private JFileChooser filechooser;
	private JButton filebutton;
	private String searchName;
	private InterfaceAIFComponent[] com;
	private ArrayList<Hxom030Bean> partBeanList;
	private String searchName2;
	private InterfaceAIFComponent[] com2;

	public StatisticalReportDialog(String name, AbstractAIFUIApplication app,
			TCSession session) {
		super(false);
		this.app = app;
		this.session = session;
		init();
	}

	private void init() {
		setTitle("�ͻ�����/�����������ͳ��");
		this.setAutoRequestFocus(true);
		this.setSize(new Dimension(500, 300));
		radioButton1 = new JRadioButton("�ɹ���˾��Ʒ���Ŷ��ձ�");
		radioButton2 = new JRadioButton("������ſͻ����Ŷ��ձ�");
		ButtonGroup bg = new ButtonGroup();
		bg.add(radioButton1);
		bg.add(radioButton2);
		radioButton1.setSelected(true);

		fileText1 = new JTextField(20);
		fileText2 = new JTextField(20);

		chooseText = new JTextField(32);
		filechooser = new JFileChooser();
		filechooser.setCurrentDirectory(new File("c:\\"));
		filebutton = new JButton(" ѡ�� ");
		filebutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				fileEvents(e);
			}
		});

		okButton = new JButton("ȷ��");
		celButton = new JButton("ȡ��");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					okEvents(e);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (TCException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		celButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				celEvents(e);
			}
		});

		JPanel rootPanel = new JPanel(new BorderLayout());
		JPanel topPanel = new JPanel(new PropertyLayout());
		JPanel filePanel = new JPanel(new PropertyLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout());

		topPanel.add("1.1.RIGHT.TOP", new JLabel(" "));
		topPanel.add("2.1.RIGHT.TOP", radioButton1);
		topPanel.add("3.1.RIGHT.TOP", new JLabel("�ɹ���˾����"));
		topPanel.add("3.2.RIGHT.TOP", fileText1);
		topPanel.add("4.1.RIGHT.TOP", radioButton2);
		topPanel.add("5.1.RIGHT.TOP", new JLabel("�������"));
		topPanel.add("5.2.RIGHT.TOP", fileText2);

		filePanel.add("1.1.RIGHT.TOP", new JLabel("   "));
		filePanel.add("2.1.RIGHT.TOP", new JLabel(" "));
		filePanel.add("2.2.RIGHT.TOP", new JLabel("ָ������·��"));
		filePanel.add("2.3.RIGHT.TOP", chooseText);
		filePanel.add("2.4.RIGHT.TOP", filebutton);

		buttonPanel.add(okButton);
		buttonPanel.add(new JLabel("   "));
		buttonPanel.add(celButton);

		rootPanel.add(topPanel, BorderLayout.NORTH);
		rootPanel.add(filePanel, BorderLayout.CENTER);
		rootPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.add(rootPanel, "Center");
		this.pack();
		this.centerToScreen();
		this.showDialog();

	}

	/**
	 * ѡ��·��
	 * 
	 */
	protected void fileEvents(ActionEvent e) {
		filechooser.setFileSelectionMode(1);
		int t = filechooser.showOpenDialog(null);
		if (t == 1) {
			return;
		} else {
			File f = filechooser.getSelectedFile();
			chooseText.setText(f.getAbsolutePath());
		}
	}

	protected void okEvents(ActionEvent e) throws IOException, TCException {
		// TODO Auto-generated method stub

		// getSource() ���صĵ�ǰ������ָ��Ķ��󣬰��������������Ϣ
		if (e.getSource().equals(this.okButton)) {
			// �������ѡ����ǲɹ���˾
			if (radioButton1.isSelected()) {
				if (this.fileText1.getText().trim().isEmpty()) {
					MessageBox.post("����Ĳɹ���˾���Ų���Ϊ�գ�", "����", MessageBox.ERROR);
					return;
				}
				searchName = "check_WL_sskh";
				List<String> keyList = new ArrayList<>();
				List<String> valueList = new ArrayList<>();

				String val = fileText1.getText();
				if (!(val == null || val.isEmpty())) {
					keyList.add("hx3_sskh");
					valueList.add(val);
				}
				com = MethodUtil.searchComponentsCollection(
						(TCSession) app.getSession(), searchName,
						keyList.toArray(new String[keyList.size()]),
						valueList.toArray(new String[valueList.size()]));

				if (com == null) {
					MessageBox.post("���ϰ汾Ϊ��", "����", MessageBox.ERROR);
					System.out.println("ok");
					return;
				} else {
					System.out.println(com.length);
					if (com.length == 0) {
						MessageBox.post("û�иÿͻ������ϰ汾", "��ʾ",
								MessageBox.INFORMATION);
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

			// �������ѡ����Ǳ������
			else if (radioButton2.isSelected()) {
				if (this.fileText2.getText().trim().isEmpty()) {
					MessageBox.post("����ı�����Ų���Ϊ�գ�", "����", MessageBox.ERROR);
					return;
				}
				searchName2 = "check_WL_ggxh";
				List<String> keyList = new ArrayList<>();
				List<String> valueList = new ArrayList<>();

				String val = fileText2.getText();
				if (!(val == null || val.isEmpty())) {
					keyList.add("ID");
					valueList.add(val);
				}
				com2 = MethodUtil.searchComponentsCollection(
						(TCSession) app.getSession(), searchName2,
						keyList.toArray(new String[keyList.size()]),
						valueList.toArray(new String[valueList.size()]));

				if (com2 == null) {
					MessageBox.post("���ϰ汾Ϊ��", "����", MessageBox.ERROR);
					System.out.println("ok");
					return;
				} else {
					System.out.println(com2.length);
					if (com2.length == 0) {
						MessageBox.post("û�иù���ͺŵ����ϰ汾", "��ʾ",
								MessageBox.INFORMATION);
						return;
					}
					List<TCComponentItemRevision> revList = new ArrayList<TCComponentItemRevision>();
					for (int a = 0; a < com2.length; a++) {
						revList.add((TCComponentItemRevision) com2[a]);
					}
					getRevMessage(revList);
					writeExcel2(this.partBeanList);
				}
			} else if (!(radioButton1.isSelected() && radioButton2.isSelected())) {
				MessageBox.post("��ѡ������ɹ���˾���Ż򱾳�����", "��ʾ", MessageBox.ERROR);
				return;
			}
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
		partBeanList = new ArrayList<Hxom030Bean>();

		for (TCComponentItemRevision rev : revList) {
			itemList.add(rev.getItem());
		}
		TCProperty[][] propssItem = TCComponentType.getTCPropertiesSet(
				itemList, new String[] { "item_id", "object_name" });
		TCProperty[][] propssRev = TCComponentType.getTCPropertiesSet(revList,
				new String[] { "IMAN_master_form_rev" });
		for (TCProperty[] propss1 : propssRev) {
			formList.add((TCComponentForm) (propss1[0].getReferenceValueArray())[0]);
		}
		if (formList != null && formList.size() != 0) {
			TCProperty[][] propssForm = TCComponentType.getTCPropertiesSet(
					formList, new String[] { "hx3_khdh", "hx3_kfz", "hx3_ggxh",
							"hx3_sskh" });

			for (int i = 0; i < propssRev.length; i++) {
				Hxom030Bean bean = new Hxom030Bean();
				bean.setXUHAO("" + (i + 1));

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

				if (propssForm[i][0] != null) {
					bean.sethx3_khdh(propssForm[i][0].getStringValue());
				} else {
					bean.sethx3_khdh("");
				}
				if (propssForm[i][1] != null) {
					bean.sethx3_kfz(propssForm[i][1].getStringValue());
				} else {
					bean.sethx3_kfz("");
				}
				if (propssForm[i][2] != null) {
					bean.sethx3_ggxh(propssForm[i][2].getStringValue());
				} else {
					bean.sethx3_ggxh("");
				}
				if (propssForm[i][3] != null) {
					bean.sethx3_sskh(propssForm[i][3].getStringValue());
				} else {
					bean.sethx3_sskh("");
				}
				this.partBeanList.add(bean);
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
	public void writeExcel(List<Hxom030Bean> beanListAll) throws IOException {
		InputStream input = Hxom030Bean.class
				.getResourceAsStream(Hxom030Bean.HX3_CGGSKHBHDZB);
		Workbook wb = null;
		wb = new XSSFWorkbook(input);
		Sheet sheet = wb.getSheetAt(0);
		Row row1 = sheet.getRow(0);
		Cell cell1 = row1.getCell((short) 0);
		if (fileText1.getText().trim().equals("*")) {
			MessageBox.post("��������*", "��ʾ", MessageBox.ERROR);
			return;
		} else {
			cell1.setCellValue(fileText1.getText() + "��˾��Ʒ���Ŷ��ձ�");
		}
		for (int i = 0; i < beanListAll.size(); i++) {
			Hxom030Bean bean = beanListAll.get(i);
			Row row = null;
			Boolean isCreat = false;
			if ((i + 3) > sheet.getLastRowNum()) {
				row = sheet.createRow(i + 3);
				isCreat = true;
			} else {
				row = sheet.getRow(i + 3);
				isCreat = false;
			}
			for (int cols = 0; cols < 7; cols++) {
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
					cell.setCellValue(bean.getobject_name());
					break;
				case 2:
					cell.setCellValue(bean.getitem_id());// д������
					break;
				case 3:
					cell.setCellValue(bean.gethx3_khdh());// д������
					break;
				case 4:
					cell.setCellValue(bean.gethx3_ggxh());// д������
					break;
				case 5:
					cell.setCellValue(bean.gethx3_kfz());// д������
					break;
				}
			}
		}
		File file = new File(chooseText.getText());
		FileOutputStream output = new FileOutputStream(file + "/"
				+ fileText1.getText() + Hxom030Bean.HX3_CGGSKHBHDZB);
		if (chooseText.getText().trim().isEmpty() || output == null) {
			MessageBox.post("����д������·��", "��ʾ", MessageBox.ERROR);
			return;
		} else {
			wb.write(output);
			output.close();
			input.close();
			System.out.println("-------WRITE EXCEL OVER-------");
			MessageBox.post("�����ɹ���˾�ͻ���Ŷ��ձ�ɹ�", "��ʾ", MessageBox.INFORMATION);

		}
		this.disposeDialog();
	}

	public void writeExcel2(List<Hxom030Bean> beanListAll) throws IOException {
		InputStream input = Hxom030Bean.class
				.getResourceAsStream(Hxom030Bean.HX3_KHBHDZB);
		Workbook wb = null;
		wb = new XSSFWorkbook(input);
		Sheet sheet = wb.getSheetAt(0);
		Row row1 = sheet.getRow(0);
		Cell cell1 = row1.getCell((short) 0);
		if (fileText2.getText().trim().equals("*")) {
			MessageBox.post("��������*", "��ʾ", MessageBox.ERROR);
			return;
		} else {
			cell1.setCellValue(fileText2.getText() + "�ͻ���Ŷ��ձ�");
		}
		for (int i = 0; i < beanListAll.size(); i++) {
			Hxom030Bean bean = beanListAll.get(i);
			Row row = null;
			Boolean isCreat = false;
			if ((i + 2) > sheet.getLastRowNum()) {
				row = sheet.createRow(i + 2);
				isCreat = true;
			} else {
				row = sheet.getRow(i + 2);
				isCreat = false;
			}
			for (int cols = 0; cols < 4; cols++) {
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
					cell.setCellValue(bean.gethx3_khdh());
					break;
				case 2:
					cell.setCellValue(bean.gethx3_sskh());// д������
					break;
				case 3:
					cell.setCellValue(bean.getobject_name());// д������
					break;

				}
			}
		}
		File file = new File(chooseText.getText());
		FileOutputStream output = new FileOutputStream(file + "/"
				+ fileText2.getText() + Hxom030Bean.HX3_KHBHDZB);
		if (chooseText.getText().trim().isEmpty() || output == null) {
			MessageBox.post("����д������·��", "��ʾ", MessageBox.ERROR);
			return;
		} else {
			wb.write(output);
			output.close();
			input.close();
			System.out.println("-------WRITE EXCEL OVER-------");
			MessageBox.post("�����ͻ���Ŷ��ձ�ɹ�", "��ʾ", MessageBox.INFORMATION);
		}
		this.disposeDialog();
	}

	/**
	 * �˳�
	 * 
	 */
	protected void celEvents(ActionEvent e) {
		// TODO Auto-generated method stub
		this.dispose();
	}

}
