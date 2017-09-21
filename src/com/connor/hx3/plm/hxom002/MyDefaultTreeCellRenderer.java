package com.connor.hx3.plm.hxom002;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.teamcenter.rac.kernel.TCComponentItem;

public class MyDefaultTreeCellRenderer extends DefaultTreeCellRenderer {
	/**
	 * ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		setText(value.toString());

		if (sel) {
			setForeground(getTextSelectionColor());
		} else {
			setForeground(getTextNonSelectionColor());
		}

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

		Object obj = node.getUserObject();

		if (obj != null && (obj instanceof ItemType)
				&& ((ItemType) obj).getType() != null) {

			TCComponentItem item = null;
			ImageIcon imageIcon = com.teamcenter.rac.common.TCTypeRenderer
					.getIcon(((ItemType) obj).getType());

			this.setIcon(imageIcon);

		}

		return this;
	}

}