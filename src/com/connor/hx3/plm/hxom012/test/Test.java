package com.connor.hx3.plm.hxom012.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;

import com.teamcenter.rac.kernel.TCComponentItemRevision;

public class Test extends JFrame {

	public static void main(String[] args) {
		Test t = new Test();
		t.init();
	}

	public void init() {
		TCComponentItemRevision comp = null;

		DefaultMutableTreeNode node = new DefaultMutableTreeNode("1111");
		DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("22");
		DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("33");
		DefaultMutableTreeNode node3 = new DefaultMutableTreeNode("44");
		node.add(node1);
		node.add(node2);
		node.add(node3);

		JTree tree = new JTree(node);
		tree.setCellRenderer(new CellRender());
		CheckTreeManager manager = new CheckTreeManager(tree);
		DefaultTableModel dtm = null;

		JTable table = null;
		table = getjTable(table, dtm, new Object[] { "Nmae", "Age" },
				new Object[][] { new Object[] { "Z", "1" },
						new Object[] { "B", "2" }, new Object[] { "C", "3" } });

		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(tree), BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);

	}

	public JTable getjTable(JTable partsTable, DefaultTableModel dtm,
			Object[] titleNames, Object[][] values) {
		int simpleLen = 105;
		int totleLen = 900;
		if (partsTable == null) {
			partsTable = new JTable(getTableModel(dtm, titleNames, values)) {
				@Override
				public boolean isCellEditable(int row, int column) {

					if (column == 15)
						return false;
					else
						return true;
				}

				// @Override
				// public TableCellRenderer getCellRenderer(int row, int column)
				// {
				// // TODO Auto-generated method stub
				//
				// return new DefaultTableCellRenderer() {
				//
				// @Override
				// public Component getTableCellRendererComponent(
				// JTable table, Object value, boolean isSelected,
				// boolean hasFocus, int row, int column) {
				//
				// // Ϊ��ǰCellʱ
				// // isSelectedΪtrueʱ��ѡ��,hasFocusΪtrueʱtable��ù��
				// if (isSelected && hasFocus
				// && row == table.getSelectedRow()
				// && column == table.getSelectedColumn()) {
				// // 2.���õ�ǰCell����ɫ
				// Component c = super
				// .getTableCellRendererComponent(table,
				// value, isSelected, hasFocus,
				// row, column);
				// c.setBackground(Color.yellow);// ���ñ���ɫ
				// c.setForeground(Color.red);// ����ǰ��ɫ
				// return c;
				// } else {
				// // 3.���õ����У�ż���е���ɫ
				// if (row % 2 == 0) {// ż����ʱ����ɫ
				// setBackground(Color.blue);
				// } else if (row % 2 == 1) {// ���õ����е���ɫ
				// setBackground(Color.gray);
				// }
				// return super.getTableCellRendererComponent(
				// table, value, isSelected, hasFocus,
				// row, column);
				// }
				// }
				// };
				// }

			};

			partsTable.setDefaultRenderer(Object.class,
					new TableCellTextAreaRenderer());

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

}

class TableCellTextAreaRenderer extends JTextArea implements TableCellRenderer {
	public TableCellTextAreaRenderer() {
		setLineWrap(true);
		setWrapStyleWord(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		// ���㵱���е���Ѹ߶�
		int maxPreferredHeight = 0;
		for (int i = 0; i < table.getColumnCount(); i++) {
			setText("" + table.getValueAt(row, i));
			setSize(table.getColumnModel().getColumn(column).getWidth(), 0);
			maxPreferredHeight = Math.max(maxPreferredHeight,
					getPreferredSize().height);
		}

		if (table.getRowHeight(row) != maxPreferredHeight) // ��������������Ϲæ
			table.setRowHeight(row, maxPreferredHeight);

		setText(value == null ? "" : value.toString());
		// table.setSelectionBackground(Color.yellow);
		Font fp = new Font("Menu.font", Font.PLAIN, 14);
		Font fb = new Font("Menu.font", Font.BOLD, 14);
		if (isSelected) {
			this.setFont(fb);

		} else {
			this.setFont(fp);
		}

		if (isSelected && hasFocus && row == table.getSelectedRow()
				&& column == table.getSelectedColumn()) {
			System.out.println("Row =" + row + " Column=" + column + "   =>"
					+ value);
			// 2.���õ�ǰCell����ɫ
			// Component c = super.getTableCellRendererComponent(table, value,
			// isSelected, hasFocus, row, column);
			this.setBackground(Color.yellow);// ���ñ���ɫ
			this.setForeground(Color.BLACK);// ����ǰ��ɫ
			return this;
		} else {

			// 3.���õ����У�ż���е���ɫ
			if (row % 2 == 0) {// ż����ʱ����ɫ
				setBackground(Color.green);
			} else if (row % 2 == 1) {// ���õ����е���ɫ
				setBackground(Color.blue);
			}
			if (isSelected) {
				this.setBackground(Color.white);// ���ñ���ɫ
			}
			return this;
		}

		// return this;
	}
}
