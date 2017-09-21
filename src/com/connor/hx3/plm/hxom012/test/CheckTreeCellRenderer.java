package com.connor.hx3.plm.hxom012.test;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

public class CheckTreeCellRenderer extends JPanel implements TreeCellRenderer {
	private CheckTreeSelectionModel selectionModel;
	private TreeCellRenderer delegate;
	// private TristateCheckBox checkBox = new TristateCheckBox();
	private JCheckBox checkBox = new JCheckBox();

	public CheckTreeCellRenderer(TreeCellRenderer delegate,
			CheckTreeSelectionModel selectionModel) {
		this.delegate = delegate;
		this.selectionModel = selectionModel;
		setLayout(new BorderLayout());
		setOpaque(false);
		checkBox.setOpaque(false);
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		Component renderer = delegate.getTreeCellRendererComponent(tree, value,
				selected, expanded, leaf, row, hasFocus);

		TreePath path = tree.getPathForRow(row);
		if (path != null) {
			System.out.println(path);
			if (selectionModel.isPathSelected(path, true))
				checkBox.setSelected(true);
			else {
				System.out.println(selectionModel.isPartiallySelected(path));
				checkBox.setSelected(selectionModel.isPartiallySelected(path) ? true
						: false);
			}
		}
		removeAll();
		add(checkBox, BorderLayout.WEST);
		add(renderer, BorderLayout.CENTER);
		return this;
	}
}