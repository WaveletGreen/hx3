package com.connor.hx3.plm.hxom002;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.sun.java.swing.plaf.motif.MotifComboBoxUI;
import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;

public class JTreeComboBox extends JComboBox {
	/**
	 * 显示用的树
	 */
	private JTree tree;

	public JTreeComboBox() {
		this(new JTree());
	}

	public JTreeComboBox(JTree tree) {
		this.setTree(tree);
	}

	/**
	 * 设置树
	 * 
	 * @param tree
	 *            JTree
	 */
	public void setTree(JTree tree) {
		this.tree = tree;
		if (tree != null) {
			this.setSelectedItem(tree.getSelectionPath());
			this.tree.setCellRenderer(new DefaultTreeCellRenderer() {

				@Override
				public Component getTreeCellRendererComponent(JTree tree,
						Object value, boolean sel, boolean expanded,
						boolean leaf, int row, boolean hasFocus) {
					// TODO Auto-generated method stub
					setText(value.toString());

					if (sel) {
						if (((TreeNode) value).getChildCount() == 0)
							setForeground(Color.RED);
						// setBackground(Color.GRAY);
					} else {
						setForeground(getTextNonSelectionColor());
					}
					// 取消图标
					this.setIcon(null);

					// return super.getTreeCellRendererComponent(tree, value,
					// sel,
					// expanded, leaf, row, hasFocus);
					return this;
				}

			});
			this.setRenderer(new JTreeComboBoxRenderer());
		}
		this.updateUI();
	}

	/**
	 * 取得树
	 * 
	 * @return JTree
	 */
	public JTree getTree() {
		return tree;
	}

	/**
	 * 设置当前选择的树路径
	 * 
	 * @param o
	 *            Object
	 */
	public void setSelectedItem(Object o) {
		tree.setSelectionPath((TreePath) o);
		getModel().setSelectedItem(o);
	}

	public void updateUI() {
		ComboBoxUI cui = (ComboBoxUI) UIManager.getUI(this);
		if (cui instanceof MetalComboBoxUI) {
			cui = new MetalJTreeComboBoxUI();
		} else if (cui instanceof MotifJTreeComboBoxUI) {
			cui = new MotifJTreeComboBoxUI();
		} else {
			cui = new WindowsJTreeComboBoxUI();
		}
		setUI(cui);
	}

	// UI Inner classes -- one for each supported Look and Feel
	class MetalJTreeComboBoxUI extends MetalComboBoxUI {
		protected ComboPopup createPopup() {
			return new TreePopup(comboBox);
		}
	}

	class WindowsJTreeComboBoxUI extends WindowsComboBoxUI {
		protected ComboPopup createPopup() {
			return new TreePopup(comboBox);
		}
	}

	class MotifJTreeComboBoxUI extends MotifComboBoxUI {
		protected ComboPopup createPopup() {
			return new TreePopup(comboBox);
		}
	}

	/**
	 * <p>
	 * Title: OpenSwing
	 * </p>
	 * <p>
	 * Description: 树形结构而来的DefaultListCellRenderer
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2004
	 * </p>
	 * <p>
	 * Company:
	 * </p>
	 * 
	 * @author <a href="mailto:sunkingxie@hotmail.com"
	 *         mce_href="mailto:sunkingxie@hotmail.com">SunKing</a>
	 * @version 1.0
	 */

	class JTreeComboBoxRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			if (value != null) {
				TreePath path = (TreePath) value;
				TreeNode node = (TreeNode) path.getLastPathComponent();
				value = node;
				TreeCellRenderer r = tree.getCellRenderer();

				JLabel lb = (JLabel) r.getTreeCellRendererComponent(tree,
						value, isSelected, false, node.isLeaf(), index,
						cellHasFocus);

				return lb;
			}
			return super.getListCellRendererComponent(list, value, index,
					isSelected, cellHasFocus);
		}
	}

	/**
	 * 测试
	 */
	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JFrame frame = new JFrame("JTreeComboBox");
		DefaultMutableTreeNode node0 = new DefaultMutableTreeNode("00");

		DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("11");
		DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("22");
		DefaultMutableTreeNode node3 = new DefaultMutableTreeNode("33");
		node0.add(node1);
		node0.add(node2);
		node0.add(node3);

		DefaultMutableTreeNode node4 = new DefaultMutableTreeNode("44");
		DefaultMutableTreeNode node5 = new DefaultMutableTreeNode("55");
		node3.add(node4);
		node3.add(node5);
		JTree tree = new JTree(new DefaultTreeModel(node0));

		final JTreeComboBox box = new JTreeComboBox(tree);
		box.setPreferredSize(new Dimension(300, 28));
		frame.getContentPane().add(box);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}

/**
 * @version 1.0
 */
class TreePopup extends JPopupMenu implements ComboPopup {
	protected JTreeComboBox comboBox;
	protected JScrollPane scrollPane;
	protected MouseMotionListener mouseMotionListener;
	protected MouseListener mouseListener;
	private MouseListener treeSelectListener = new MouseAdapter() {
		public void mouseReleased(MouseEvent e) {
			JTree tree = (JTree) e.getSource();
			TreePath tp = tree.getPathForLocation(e.getPoint().x,
					e.getPoint().y);
			if (tp == null) {
				return;
			}
			if (((TreeNode) tp.getLastPathComponent()).getChildCount() != 0) {
				System.out.println("选择叶子节点的时候才可以");
				return;
			}

			comboBox.setSelectedItem(tp);
			togglePopup();
			MenuSelectionManager.defaultManager().clearSelectedPath();
		}
	};

	public TreePopup(JComboBox comboBox) {
		this.comboBox = (JTreeComboBox) comboBox;
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(new BorderLayout());
		setLightWeightPopupEnabled(comboBox.isLightWeightPopupEnabled());
		JTree tree = this.comboBox.getTree();
		if (tree != null) {
			scrollPane = new JScrollPane(tree);
			scrollPane.setBorder(null);
			add(scrollPane, BorderLayout.CENTER);
			tree.addMouseListener(treeSelectListener);
		}
	}

	public void show() {
		updatePopup();
		show(comboBox, 0, comboBox.getHeight());
		comboBox.getTree().requestFocus();
	}

	public void hide() {
		setVisible(false);
		comboBox.firePropertyChange("popupVisible", true, false);
	}

	protected JList list = new JList();

	public JList getList() {
		return list;
	}

	public MouseMotionListener getMouseMotionListener() {
		if (mouseMotionListener == null) {
			mouseMotionListener = new MouseMotionAdapter() {
			};
		}
		return mouseMotionListener;
	}

	public KeyListener getKeyListener() {
		return null;
	}

	public void uninstallingUI() {
	}

	/**
	 * Implementation of ComboPopup.getMouseListener().
	 * 
	 * @return a <code>MouseListener</code> or null
	 * @see ComboPopup#getMouseListener
	 */
	public MouseListener getMouseListener() {
		if (mouseListener == null) {
			mouseListener = new InvocationMouseHandler();
		}
		return mouseListener;
	}

	protected void togglePopup() {
		if (isVisible()) {
			hide();
		} else {
			show();
		}
	}

	protected void updatePopup() {
		setPreferredSize(new Dimension(comboBox.getSize().width, 200));
		Object selectedObj = comboBox.getSelectedItem();
		if (selectedObj != null) {
			TreePath tp = (TreePath) selectedObj;
			((JTreeComboBox) comboBox).getTree().setSelectionPath(tp);
		}
	}

	protected class InvocationMouseHandler extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (!SwingUtilities.isLeftMouseButton(e) || !comboBox.isEnabled()) {
				return;
			}
			if (comboBox.isEditable()) {
				Component comp = comboBox.getEditor().getEditorComponent();
				if ((!(comp instanceof JComponent))
						|| ((JComponent) comp).isRequestFocusEnabled()) {
					comp.requestFocus();
				}
			} else if (comboBox.isRequestFocusEnabled()) {
				comboBox.requestFocus();
			}
			togglePopup();
		}
	}
}