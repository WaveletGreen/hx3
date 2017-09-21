package com.connor.hx3.plm.hxom012.test;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

public class CellRender extends JPanel implements TreeCellRenderer {

	private JCheckBox checkBox = new JCheckBox();

	public CellRender() {

		setLayout(new BorderLayout());
		setOpaque(false);
		// this.hx3Bean = hx3Bean;
		checkBox.setOpaque(false);
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		TreePath path = tree.getPathForRow(row);
		TreePath selectPath = tree.getSelectionPath();

		if (path.equals(selectPath)) {
			checkBox.setSelected(true);
		} else {

		}
		checkBox.setText(value.toString());
		// if (bean.getGxh() != null && !bean.getGxh().isEmpty()) {
		// checkBox.setSelected(true);
		// checkBox.setEnabled(false);
		// if (hx3Bean != null && hx3Bean.hx3_gxhh.equals(bean.getGxh())) {
		// checkBox.setEnabled(true);
		// }
		// } else {
		// checkBox.setEnabled(true);
		// checkBox.setSelected(false);
		// }
		// checkBox.setEnabled(true);
		removeAll();
		add(checkBox, BorderLayout.WEST);
		// add(this, BorderLayout.CENTER);

		return this;
	}
}
