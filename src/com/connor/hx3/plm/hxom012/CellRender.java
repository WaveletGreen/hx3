package com.connor.hx3.plm.hxom012;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

public class CellRender extends JPanel implements TreeCellRenderer {

	private JCheckBox checkBox = new JCheckBox();
	private HX3_GYLXRevisionFormPropBean hx3Bean;
	private boolean isSelect;

	public CellRender(HX3_GYLXRevisionFormPropBean hx3Bean) {

		setLayout(new BorderLayout());
		setOpaque(false);
		this.hx3Bean = hx3Bean;
		checkBox.setOpaque(false);
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		BomStructBean bean = (BomStructBean) ((DefaultMutableTreeNode) value)
				.getUserObject();

		TreePath path = tree.getPathForRow(row);
		checkBox.setText(bean.toString());
		if (bean.getGxh() != null && !bean.getGxh().isEmpty()) {
			// System.out.println("工序行号 = >" + bean.getGxh());
			checkBox.setSelected(true);
			checkBox.setEnabled(false);
			if (hx3Bean != null && hx3Bean.hx3_gxhh.equals(bean.getGxh())) {
				// System.out.println("工序行号 == hx3Bean.hx3_gxhh ");
				checkBox.setEnabled(true);
			} else {
				// System.out.println("工序行号 =/= hx3Bean.hx3_gxhh ");
			}
		} else {
			// System.out.println("工序行号 = NULL/\"\" >" + bean.getGxh());
			checkBox.setEnabled(true);

		}

		if (path.equals(tree.getSelectionPath()) && checkBox.isEnabled()) {
			System.out.println("选中 / " + checkBox.isSelected());
			if (checkBox.isSelected()) {
				checkBox.setSelected(false);
			} else {
				checkBox.setSelected(true);
			}
		}
		removeAll();
		add(checkBox, BorderLayout.WEST);
		// add(this, BorderLayout.CENTER);

		return this;
	}
}
