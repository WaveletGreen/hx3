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
				// // 为当前Cell时
				// // isSelected为true时行选中,hasFocus为true时table获得光标
				// if (isSelected && hasFocus
				// && row == table.getSelectedRow()
				// && column == table.getSelectedColumn()) {
				// // 2.设置当前Cell的颜色
				// Component c = super
				// .getTableCellRendererComponent(table,
				// value, isSelected, hasFocus,
				// row, column);
				// c.setBackground(Color.yellow);// 设置背景色
				// c.setForeground(Color.red);// 设置前景色
				// return c;
				// } else {
				// // 3.设置单数行，偶数行的颜色
				// if (row % 2 == 0) {// 偶数行时的颜色
				// setBackground(Color.blue);
				// } else if (row % 2 == 1) {// 设置单数行的颜色
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
		// 计算当下行的最佳高度
		int maxPreferredHeight = 0;
		for (int i = 0; i < table.getColumnCount(); i++) {
			setText("" + table.getValueAt(row, i));
			setSize(table.getColumnModel().getColumn(column).getWidth(), 0);
			maxPreferredHeight = Math.max(maxPreferredHeight,
					getPreferredSize().height);
		}

		if (table.getRowHeight(row) != maxPreferredHeight) // 少了这行则处理器瞎忙
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
			// 2.设置当前Cell的颜色
			// Component c = super.getTableCellRendererComponent(table, value,
			// isSelected, hasFocus, row, column);
			this.setBackground(Color.yellow);// 设置背景色
			this.setForeground(Color.BLACK);// 设置前景色
			return this;
		} else {

			// 3.设置单数行，偶数行的颜色
			if (row % 2 == 0) {// 偶数行时的颜色
				setBackground(Color.green);
			} else if (row % 2 == 1) {// 设置单数行的颜色
				setBackground(Color.blue);
			}
			if (isSelected) {
				this.setBackground(Color.white);// 设置背景色
			}
			return this;
		}

		// return this;
	}
}
