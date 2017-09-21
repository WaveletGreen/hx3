package com.connor.hx3.plm.hxom012;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.util.ButtonLayout;

public class BomStructTree extends AbstractAIFDialog implements ActionListener {

	private BomStructBean bean;
	private JTree tree;
	private JButton okButton;
	private JButton celButton;
	private JButton addButton;
	private JButton delButton;
	private JList<BomStructBean> selectList;
	private BomStructBean selectStructBean;
	private JPanel listJPanel;
//	private HX3_GYLXRevisionFormStyleSheet sheet;
	//private HX3_GYLXRevisionFormPropBean hx3Bean;
	private DefaultListModel<BomStructBean> listModel;
	//20170426
	private JTable partsTable;
	private int partsTableSelectIndex;
	private String gylxGxhh;
	public BomStructTree(BomStructBean bean,
			JTable partsTable) {
		super(false);
		this.bean = bean;
		this.partsTable = partsTable;
		init();
	}

	public void init() {

		DefaultMutableTreeNode top = new DefaultMutableTreeNode(this.bean);
		try {
			getTypeOptionInfo(this.bean, top);
		} catch (TCException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		tree = new JTree(top);
		// tree.setCellRenderer(new CellRender(this.hx3Bean));
		tree.setCellRenderer(new BomStructTreeCellRenderer());
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();// 返回最后选定的节点
				// String sel = selectedNode.toString(); // 获取选中的节点文字
				if (selectedNode == null)
					return;
				selectStructBean = (BomStructBean) selectedNode.getUserObject();

			}
		});

		this.okButton = new JButton("确定");
		this.okButton.addActionListener(this);

		this.celButton = new JButton("取消");
		this.celButton.addActionListener(this);

		JPanel bottomJPanel = new JPanel(new FlowLayout());
		bottomJPanel.add(okButton);
		bottomJPanel.add(new JLabel("    "));
		bottomJPanel.add(celButton);
		JPanel treeJPanel = new JPanel(new BorderLayout());
		treeJPanel.add(new JScrollPane(tree), BorderLayout.CENTER);

		this.addButton = new JButton(">>");
		this.addButton.setToolTipText("关联物料");
		this.addButton.addActionListener(this);
		this.delButton = new JButton("<<");
		this.delButton.setToolTipText("移除物料");
		this.delButton.addActionListener(this);
		JPanel midJPanel = new JPanel(new ButtonLayout(ButtonLayout.VERTICAL));
		midJPanel.add(addButton);
		midJPanel.add(new JLabel("  "));
		midJPanel.add(delButton);
		treeJPanel.add(midJPanel, BorderLayout.EAST);

		listModel = new DefaultListModel<>();
		selectList = new JList<>(listModel);
		listJPanel = new JPanel(new BorderLayout());
		listJPanel.add(new JScrollPane(selectList), BorderLayout.CENTER);

		JPanel centerJPanel = new JPanel(new ButtonLayout(
				ButtonLayout.HORIZONTAL));
		centerJPanel.add(treeJPanel);
		// centerJPanel.add(midJPanel);
		centerJPanel.add(listJPanel);
		centerJPanel.setBorder(new TitledBorder(BorderFactory
				.createEtchedBorder()));

		this.setSize(new Dimension(900, 900));
		this.setLayout(new BorderLayout());
		this.add(centerJPanel, BorderLayout.CENTER);
		this.add(bottomJPanel, BorderLayout.SOUTH);

		// this.showDialog();
		this.centerToScreen();
		this.setAlwaysOnTop(true);
		this.validate();

	}

	public void getTypeOptionInfo(BomStructBean bean,
			DefaultMutableTreeNode node) throws TCException {

		if (bean != null && bean.getChlidLineS() != null
				&& bean.getChlidLineS().size() != 0) {

			List<BomStructBean> childBeanS = bean.getChlidLineS();
			for (int i = 0; i < childBeanS.size(); i++) {
				DefaultMutableTreeNode cTypeNode = new DefaultMutableTreeNode(
						childBeanS.get(i));
				getTypeOptionInfo(childBeanS.get(i), cTypeNode);
				node.add(cTypeNode);

			}
		}

	}

	public void getSelectedItem(BomStructBean bean) {
		if (bean == null) {
			return;
		}
		if(bean.getGxh()==null){
			return;
		}
		if (bean.getGxh().equals(this.gylxGxhh)){//(this.hx3Bean.hx3_gxhh)) {
			listModel.addElement(bean);
		}
		if (bean.getChlidLineS() != null) {
			for (BomStructBean child : bean.getChlidLineS()) {
				getSelectedItem(child);
			}

		}
	}

	public void showDialog(//HX3_GYLXRevisionFormPropBean bean,
			int partsTableSelectIndex,String gylxGxhh) {
		this.partsTableSelectIndex = partsTableSelectIndex;
		this.gylxGxhh = gylxGxhh;
		//this.hx3Bean = bean;
		this.listModel.removeAllElements();
		getSelectedItem(this.bean);
		this.listJPanel.validate();
		((DefaultTreeModel) this.tree.getModel()).reload();
		this.validate();

		super.showDialog();
	}

	@Override
	public void actionPerformed(ActionEvent actionevent) {
		// TODO Auto-generated method stub
		Object sorceObj = actionevent.getSource();
		if (sorceObj.equals(this.okButton)) {
			int size = this.listModel.getSize();
			StringBuffer gxhhSb = new StringBuffer();
			for (int i = 0; i < size; i++) {
				listModel.getElementAt(i).setGxh(this.gylxGxhh);//(this.hx3Bean.hx3_gxhh);
				gxhhSb.append(listModel.getElementAt(i).getItemID());
				if (i != (size - 1)) {
					gxhhSb.append(",");
				}
			}
			//hx3Bean.hx3_lywl = gxhhSb.toString();
			if(partsTableSelectIndex !=-1){
				((DefaultTableModel) partsTable.getModel())
						.setValueAt(gxhhSb.toString()//selectTableBean.hx3_lywl,
								,partsTableSelectIndex, 15);
			}
			this.disposeDialog();
		} else if (sorceObj.equals(this.celButton)) {
			this.disposeDialog();
			// this.dispose();
		} else if (sorceObj.equals(this.addButton)) {
			// selectStructBean
			if (selectStructBean.getGxh().trim().isEmpty()) {
				if (selectStructBean != null) {
					if (!listModel.contains(selectStructBean)){
						listModel.addElement(selectStructBean);
						selectStructBean.setGxh(this.gylxGxhh);//(hx3Bean.hx3_gxhh);
					}
				}
			}
			//this.validate();
			//this.setVisible(false);
			this.setVisible(true);
			// listModel.
		} else if (sorceObj.equals(this.delButton)) {
			int index = this.selectList.getSelectedIndex();
			if (index == -1) {
				return;
			}
			this.listModel.getElementAt(index).setGxh("");
			this.listModel.removeElementAt(index);
			//this.validate();
			//this.setVisible(false);
			this.setVisible(true);
		}

	}

	class BomStructTreeCellRenderer extends DefaultTreeCellRenderer {
		/**
		 * ID
		 */
		private static final long serialVersionUID = 2L;

		/**
		 * 
		 */
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, expanded,
					leaf, row, hasFocus);

			setText(value.toString());

			if (sel) {
				setForeground(getTextSelectionColor());
			} else {
				setForeground(getTextNonSelectionColor());
			}

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

			Object obj = node.getUserObject();

			if (obj != null && (obj instanceof BomStructBean)
					&& ((BomStructBean) obj).getBomLine() != null) {

				ImageIcon imageIcon = null;
				try {
					imageIcon = com.teamcenter.rac.common.TCTypeRenderer
							.getIcon(((BomStructBean) obj).getBomLine()
									.getItemRevision().getType());
				} catch (TCException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				this.setIcon(imageIcon);

			}

			return this;
		}
	}

}
