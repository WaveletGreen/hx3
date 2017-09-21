package com.connor.hx3.plm.hxom011;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.kernel.TCPreferenceService;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PropertyLayout;

public class Hxom011Dialog extends AbstractAIFDialog implements ActionListener,
		ItemListener {

	private JButton okBtn;
	private JButton celBtn;
	private Hxom011Bean firstBean;
	private AbstractAIFApplication app;
	private TCSession session;
	private int index;
	private List<JComboBox> comboxList ;
	private List<Hxom011Bean> comboxBeanList ;
	private Hxom011Bean selectParentBean;
	private List<Hxom011OptionBean> allowWGYQList;
	private List<Hxom011OptionBean> optionValueList;
	private Boolean isNull = false;
	private int selec0 = 0;

	public Hxom011Dialog(Hxom011Bean firstBean, AbstractAIFApplication app,
			TCSession session) {
		super(false);
		this.firstBean = firstBean;
		this.app = app;
		this.session = session;
		this.index = 1;
		this.comboxList = new ArrayList<>();
		this.comboxBeanList = new ArrayList<>();
		this.selectParentBean = firstBean.getMaterialRevS().size()==0?null:firstBean.getMaterialRevS().get(0);
		this.optionValueList = new ArrayList<>();
		this.allowWGYQList = new ArrayList<>();
		getOptionValues();
		getWGYQList(selectParentBean);
	}
	/**
	 * 获取客户要求首选项
	 */
	public void getOptionValues(){
		TCPreferenceService service = this.session.getPreferenceService();
		String[] valuesS = service.getStringArray(TCPreferenceService.TC_preference_site, "HX3WLkehuyaoqiu");
		if(valuesS!=null){
			for(String value:valuesS){
				List<String> wgList = new ArrayList<>();
				Hxom011OptionBean bean = new Hxom011OptionBean();
				String[] values = value.split("\\|");
				if(values.length!=3){
					continue;
				}
				String[] wgS = values[2].split(",");
				for(String wg :wgS){
						wgList.add(wg);
				}
				bean.no =values[0];
				bean.customNo = values[1];
				bean.wgList = wgList;
				this.optionValueList.add(bean);
					
				
			}
		}
	}
	/**
	 * 获取选中的父
	 * @param selectBean
	 */
	public void getWGYQList(Hxom011Bean selectBean){
		if(selectBean == null){
			return;
		}
		this.allowWGYQList.clear();
		if(selectBean.getCustomerNo() == null 
				||selectBean.getCustomerNo().equals("")
				||selectBean.getCustomerNo().trim().isEmpty()){
			this.isNull = true;
		}else{
			this.isNull = false;
			for(Hxom011OptionBean value : this.optionValueList){
				if(value.customNo.equals(selectBean.getCustomerNo())){
					this.allowWGYQList.add(value);
					System.out.println(value);
				}
			}
		}
	}

	@Override
	public void run() {
		initUI();
	}

	public void initUI() {
		this.setTitle("设计BOM转物料BOM");
		this.setSize(new Dimension(500, 300));
		JPanel midJPanel = new JPanel(new PropertyLayout());
		setMidPanel(midJPanel, this.firstBean);
		JPanel botomJPanel = new JPanel(new FlowLayout());
		okBtn = new JButton("确定");
		celBtn = new JButton("退出");
		botomJPanel.add(okBtn);
		botomJPanel.add(new JLabel("    "));
		botomJPanel.add(celBtn);
		JPanel rootJPanel = new JPanel(new BorderLayout());

		rootJPanel.add(new JScrollPane(midJPanel), BorderLayout.CENTER);
		rootJPanel.add(botomJPanel, BorderLayout.SOUTH);

		this.add(rootJPanel);
		// this.pack();
		addActionListenEvent();
		this.centerToScreen();
		this.showDialog();
	}
	/**
	 * 匹配首字母，和外观
	 * @param bean
	 * @return
	 */
	public boolean isContent(Hxom011Bean bean){
		boolean isContent = false;
		boolean isListContent = false;
		System.out.println(">>>>>>>> bean >>"+bean.toString() +"<<>>"+bean.getWGYQ_EN());
		for(Hxom011OptionBean  opBean :this.allowWGYQList){
			System.out.print(opBean);
			if(bean.toString().startsWith(opBean.no)){
				isListContent = true;
				if(opBean.wgList.contains(bean.getWGYQ_EN())){
			
					isContent = true;
					System.out.println(" content");
				}
			}else{
				System.out.println(" not content");
			}
		}
		if(!isListContent){
			isContent = true;
		}
		return isContent;
	}

	public void setMidPanel(JPanel midJPanel, Hxom011Bean bean) {
		if (bean == null) {
			return;
		}
		if (bean.getMaterialRevS() != null && bean.getMaterialRevS().size() > 1) {
			midJPanel.add(this.index + ".1.left.top.preferred.preferred",
					new JLabel("     "));
			this.index++;
			midJPanel.add(this.index + ".1.left.top.preferred.preferred",
					new JLabel("     "));
			midJPanel.add(this.index + ".2.left.top.preferred.preferred",
					new JLabel("图纸："));
			midJPanel.add(this.index + ".3.left.top.preferred.preferred",
					new JLabel(bean.toString()));
			midJPanel.add(this.index + ".4.left.top.preferred.preferred",
					new JLabel("     "));
			midJPanel.add(this.index + ".5.left.top.preferred.preferred",
					new JLabel("物料："));
			JComboBox comBox = new JComboBox(
					new DefaultComboBoxModel<Hxom011Bean>());
			DefaultComboBoxModel model =	((DefaultComboBoxModel)comBox.getModel());
			
			comboxBeanList.add(bean);
			if(comboxList.size()!=0){
				bean.setSelectRev(null);
				selec0 = 0;
				for(Hxom011Bean mbean :bean
						.getMaterialRevS()){
					if(isNull || isContent(mbean)){
						model.addElement( mbean);
						if(selec0 == 0){
							bean.setSelectRev(mbean.getRev());
						}
						selec0 ++;
					}
				}
			}else{
				
				for(Hxom011Bean mbean :bean
						.getMaterialRevS()){
					model.addElement( mbean);
				}
			}
			comboxList.add(comBox);
			midJPanel.add(this.index + ".6.left.top.preferred.preferred",
					comBox);
			comBox.addItemListener(this);
			midJPanel.add(this.index + ".7.left.top.preferred.preferred",
					new JLabel("      "));
			this.index++;
		} else {
			midJPanel.add("1.1.left.top.preferred.preferred",
					new JLabel("    "));
			midJPanel.add("1.2.left.top.preferred.preferred", new JLabel(
					"是否执行设计BOM转物料BOM?"));
			midJPanel
					.add("1.3.left.top.preferred.preferred", new JLabel("   "));
		}

		List<Hxom011Bean> beanList = bean.getchildBeanS();
		if (beanList != null)
		{
			for (int i = 0; i < beanList.size(); i++)
			{
				Hxom011Bean beanT = beanList.get(i);
				setMidPanel(midJPanel, beanT);
			}
		}
	}

	public void addActionListenEvent() {
		okBtn.addActionListener(this);
		celBtn.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object sourceObj = arg0.getSource();
		if (sourceObj.equals(okBtn)) {
			for(int i = 0;i < this.comboxList.size();i++){
				DefaultComboBoxModel model =	((DefaultComboBoxModel)comboxList.get(i).getModel());
				if(model.getSize() ==0){
					MessageBox.post(this,"存在没有关联物料的图纸，请仔细核对后再执行！","错误",MessageBox.WARNING);
					return;
				}
			}
			this.setVisible(false);
			Hxom011Operation operation = new Hxom011Operation(firstBean, this);
			this.session.queueOperation(operation);

		} else if (sourceObj.equals(celBtn)) {
			this.disposeDialog();
			this.dispose();
		}

	}

	public static Hxom011Bean createBean(int j) {
		Hxom011Bean bean = new Hxom011Bean(j + "_TTT");

		List<Hxom011Bean> mList = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			Hxom011Bean bean1 = new Hxom011Bean(i + "_MMMMM");
			mList.add(bean1);
		}
		bean.setMaterialRevS(mList);
		return bean;
	}

	public static void main(String[] args) {
		// List<Hxom011Bean> beanList = new ArrayList<>();
		Hxom011Bean bean = createBean(0);
		List<Hxom011Bean> mList = new ArrayList<>();
		for (int i = 1; i < 10; i++) {
			Hxom011Bean bean1 = createBean(i);
			mList.add(bean1);
		}

		bean.setchildBeanS(mList);

		new Hxom011Dialog(bean, null, null).initUI();

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		System.out.println("选择【" + e.getItem().toString() + "】");
		
		if (e.getStateChange() == ItemEvent.SELECTED) {
			Hxom011Bean item = (Hxom011Bean) e.getItem();
			if(e.getSource().equals(this.comboxList.get(0))){
				System.out.println(">>>><<<<<<");
				this.selectParentBean = item;
				getWGYQList(item);
				for(int i =1;i <this.comboxList.size();i++ ){
					
					DefaultComboBoxModel model =	((DefaultComboBoxModel)comboxList.get(i).getModel());
					model.removeAllElements();
					Hxom011Bean cBean = comboxBeanList.get(i);
					
					cBean.setSelectRev(null);
					selec0 = 0;
					for(Hxom011Bean mBean : cBean.getMaterialRevS()){
						if(isNull || isContent(mBean)){
							model.addElement(mBean);
							if(selec0 == 0){
								cBean.setSelectRev(mBean.getRev());
							}
							selec0 ++;
						}
					}
				}
			}
			item.getDesignBean().setSelectRev(item.getRev());
		}
	}
}
