package com.connor.hx3.plm.hxom012.test;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class Main {
	public static void main(String[] args) {
		JTable table = new JTable();
		DefaultTableModel model = (DefaultTableModel) table.getModel();

		model.addColumn("A", new Object[] { "item1" });
		model.addColumn("B", new Object[] { "item2" });

		String[] values = { "item2", "item1", "item3" };
		
		TableColumn col = table.getColumnModel().getColumn(0);
		col.setCellEditor(new MyComboBoxEditor(values));
		col.setCellRenderer(new MyComboBoxRenderer(values));

		JFrame frame = new JFrame();
		frame.setContentPane(new JScrollPane(table));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

class MyComboBoxRenderer extends JComboBox implements TableCellRenderer {
	public MyComboBoxRenderer(String[] items) {
		super(items);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		System.out.println(value);
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
	public MyComboBoxEditor(String[] items) {
		super(new JComboBox(items));
	}
}
