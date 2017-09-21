package com.connor.hx3.plm.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.SoaUtil;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentDataset;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.services.ICoreConstantService;
import com.teamcenter.rac.ui.commands.RACUICommandsActivator;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.OSGIUtil;
import com.teamcenter.rac.util.PropertyLayout;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.rac.core.DataManagementService;
import com.teamcenter.services.rac.core.DispatcherManagementService;
import com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput;
import com.teamcenter.services.rac.documentmanagement.DocumentControlService;
import com.teamcenter.soa.client.model.ErrorStack;
import com.teamcenter.soa.client.model.ErrorValue;

/**
 * @copyRight 杭州康勒科技有限公司
 * @author 作者 E-mail:hub@connor.net.cn
 * @date 创建时间：2016-10-24 下午11:14:19
 * @version v1.0
 * @parameter
 * @since
 * @return
 */

public class TestDialog extends AbstractAIFDialog implements ActionListener {

	private JPanel centerJPanel;
	private JPanel rootJPanel;

	private JTextField nameField;
	private JTextField idField;

	private JTextField propNameField;
	private JTextField propValueField;

	private JButton okButton;
	private JButton celButton;
	private TCSession session;
	private AbstractAIFApplication app;

	public TestDialog() {
		super(false);
		this.app = AIFUtility.getCurrentApplication();
		this.session = (TCSession) this.app.getSession();

	}

	public void initUI() {
		this.setTitle("创建物料");

		this.setSize(new Dimension(500, 300));

		this.idField = new JTextField(16);
		this.nameField = new JTextField(16);
		this.propNameField = new JTextField(16);
		this.propValueField = new JTextField(16);

		this.centerJPanel = new JPanel(new PropertyLayout());
		this.centerJPanel.add("1.1.LEFT.TOP", new JLabel("ID:"));
		this.centerJPanel.add("1.2.LEFT.TOP", this.idField);
		this.centerJPanel.add("2.1.LEFT.TOP", new JLabel("NAME:"));
		this.centerJPanel.add("2.2.LEFT.TOP", this.nameField);
		this.centerJPanel.add("3.1.LEFT.TOP", new JLabel("PROPNAME:"));
		this.centerJPanel.add("3.2.LEFT.TOP", this.propNameField);
		this.centerJPanel.add("4.1.LEFT.TOP", new JLabel("PROPVALUE:"));
		this.centerJPanel.add("4.2.LEFT.TOP", this.propValueField);

		this.rootJPanel = new JPanel(new FlowLayout());
		this.okButton = new JButton("OK");
		this.okButton.addActionListener(this);

		this.celButton = new JButton("CEL");
		this.celButton.addActionListener(this);

		this.rootJPanel.add(okButton);
		this.rootJPanel.add(celButton);

		this.setLayout(new BorderLayout());
		this.add(this.centerJPanel, BorderLayout.CENTER);
		this.add(this.rootJPanel, BorderLayout.SOUTH);
		this.centerToScreen();
		this.pack();
		this.showDialog();

	}

	@Override
	public void run() {
		initUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object sourceObj = e.getSource();
		if (sourceObj.equals(this.okButton)) {
			try {
				createItemBOHelp(this.idField.getText(), "A", "Item",
						this.nameField.getText(), "");
			} catch (ServiceException | TCException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			this.dispose();
			this.disposeDialog();
		}

	}

	public void createItemBOHelp(String itemID, String itemRev,
			String itemType, String itemName, String itemDesc)
			throws ServiceException, TCException {

		com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput itemInput = new com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput();
		itemInput.boName = itemType;
		Map<String, String> itemStringMap = new HashMap();
		itemStringMap.put("object_name", itemName);
		itemStringMap.put("item_id", itemID);
		itemStringMap.put("object_desc", itemDesc);
		itemInput.stringProps = itemStringMap;

		Map<String, CreateInput[]> compMap = new HashMap();

		CreateInput[] compInputS2 = new CreateInput[1];
		compInputS2[0] = new CreateInput();
		compInputS2[0].boName = "Form";
		Map<String, String> compStringMap2 = new HashMap();
		compStringMap2.put("object_name", "12321312");
		compStringMap2.put("object_desc", "12321312");
		compInputS2[0].stringProps = compStringMap2;
		Map<String, CreateInput[]> compMap2 = new HashMap<>();
		compMap2.put("TC_Attaches", compInputS2);

		CreateInput[] compInputS = new CreateInput[1];
		compInputS[0] = new CreateInput();
		compInputS[0].boName = itemType + "Revision";
		Map<String, String> compStringMap = new HashMap();
		compStringMap.put("item_revision_id", "A");
		// compStringMap.put("hx3_ljttf", "A3");
		compInputS[0].stringProps = compStringMap;
		compInputS[0].compoundCreateInput = compMap2;

		compMap.put("revision", compInputS);
		//

		itemInput.compoundCreateInput = compMap;

		DataManagementService datamanagementservice = DataManagementService
				.getService(session);
		com.teamcenter.services.rac.core._2008_06.DataManagement.CreateIn createin = new com.teamcenter.services.rac.core._2008_06.DataManagement.CreateIn();
		createin.clientId = "Test";
		createin.data = itemInput;
		com.teamcenter.services.rac.core._2008_06.DataManagement.CreateResponse createresponse = datamanagementservice
				.createObjects(new com.teamcenter.services.rac.core._2008_06.DataManagement.CreateIn[] { createin });
		SoaUtil.handlePartialErrors(createresponse.serviceData, null, null,
				true);
		com.teamcenter.services.rac.core._2008_06.DataManagement.CreateOut acreateout[] = createresponse.output;
		if (acreateout != null && acreateout.length > 0) {
			ArrayList arraylist = new ArrayList(0);
			com.teamcenter.services.rac.core._2008_06.DataManagement.CreateOut acreateout1[];
			int j = (acreateout1 = acreateout).length;
			for (int i = 0; i < j; i++) {
				com.teamcenter.services.rac.core._2008_06.DataManagement.CreateOut createout = acreateout1[i];
				TCComponent atccomponent[] = createout.objects;
				if (atccomponent != null && atccomponent.length > 0) {
					arraylist.addAll(Arrays.asList(atccomponent));
					session.getUser().getNewStuffFolder()
							.add("contents", atccomponent[0]);
				}
			}

		}

	}

	/**
	 * 通过SOA创建对象
	 * 
	 * @param itemID
	 * @param itemRev
	 * @param itemType
	 * @param itemName
	 * @param itemDesc
	 */
	private void createNewItemSoa(String itemID, String itemRev,
			String itemType, String itemName, String itemDesc) {
		Map propMap = new HashMap<String, String>();
		Map propMap2 = new HashMap<String, String>();
		propMap2.put("object_desc", "描述");
		propMap.put(this.propNameField.getText(), this.propValueField.getText());

		System.out.println("NAME =" + this.propNameField.getText() + "/"
				+ this.propValueField.getText());

		DataManagementService datamanagementservice = DataManagementService
				.getService(session);

		com.teamcenter.services.rac.core._2006_03.DataManagement.CreateItemsResponse createitemsresponse = null;
		com.teamcenter.services.rac.core._2006_03.DataManagement.ItemProperties aitemproperties[] = new com.teamcenter.services.rac.core._2006_03.DataManagement.ItemProperties[1];
		aitemproperties[0] = new com.teamcenter.services.rac.core._2006_03.DataManagement.ItemProperties();
		aitemproperties[0].clientId = Integer.toString(1);
		aitemproperties[0].description = itemDesc;
		aitemproperties[0].itemId = itemID;
		aitemproperties[0].name = itemName;
		aitemproperties[0].revId = itemRev;
		aitemproperties[0].type = itemType;
		aitemproperties[0].uom = "";// == null ? "" : uomComp.toString();

		com.teamcenter.services.rac.core._2006_03.DataManagement.ReviseProperties is = null;

		com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes extendedattributes[] = new com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes[3];
		com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes extendedattributes3 = new com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes();
		extendedattributes3 = new com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes();
		extendedattributes3.objectType = itemType;
		extendedattributes3.attributes = propMap2;
		extendedattributes[0] = extendedattributes3;
		extendedattributes[1] = new com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes();
		extendedattributes[1].objectType = itemType + "Revision";
		extendedattributes[1].attributes = propMap;
		extendedattributes[2] = new com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes();
		extendedattributes[2].objectType = itemType + "RevisionMaster";
		extendedattributes[2].attributes = propMap2;

		aitemproperties[0].extendedAttributes = extendedattributes;

		TCComponent tccomponent1 = null;
		String s1 = "";
		createitemsresponse = datamanagementservice.createItems(
				aitemproperties, tccomponent1, s1);
		if (createitemsresponse.serviceData.sizeOfPartialErrors() == 0) {
			TCComponentItem newComp = createitemsresponse.output[0].item;
			try {
				newComp.getLatestItemRevision().setProperty("hx3_zpttf", "A3");
				ArrayList<String> nameList = new ArrayList<>();
				nameList.add(itemName);
				// createAndrelateIRDCDatasets(newComp.getLatestItemRevision(),
				// nameList);
			} catch (TCException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (newComp != null) {
				try {

					session.getUser().getNewStuffFolder()
							.add("contents", newComp);

				} catch (TCException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MessageBox.post("创建成功", "成功", MessageBox.INFORMATION);
			} else
				MessageBox.post("创建失败", "成功", MessageBox.INFORMATION);
		}
	}

	private void createAndrelateIRDCDatasets(

	TCComponentItemRevision tccomponentitemrevision, ArrayList arraylist)
			throws TCException {

		com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput input = null;

		TCSession session = (TCSession) AIFUtility.getCurrentApplication()
				.getSession();
		DocumentControlService documentcontrolservice = DocumentControlService
				.getService(session);
		com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.PostCreateResponse postcreateresponse = null;
		com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.PostCreateInputs apostcreateinputs[] = new com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.PostCreateInputs[1];
		apostcreateinputs[0] = new com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.PostCreateInputs();
		apostcreateinputs[0].clientId = Integer.toString(1);
		apostcreateinputs[0].itemRevision = tccomponentitemrevision;
		Object aobj[] = arraylist.toArray();
		String as[] = new String[aobj.length];
		for (int i = 0; i < aobj.length; i++)
			as[i] = aobj[i].toString();

		apostcreateinputs[0].fileNames = as;
		postcreateresponse = documentcontrolservice
				.postCreate(apostcreateinputs);
		com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.PostCreateInfo apostcreateinfo[] = postcreateresponse.output;
		if (apostcreateinfo.length <= 0)
			throw new TCException(new int[] { 1 }, new int[1],
					new String[] { "IRDC失败" });
		int j = 0;
		TCException tcexception = null;
		com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.PostCreateInfo apostcreateinfo1[];
		int l = (apostcreateinfo1 = apostcreateinfo).length;
		for (int k = 0; k < l; k++) {
			com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.PostCreateInfo postcreateinfo = apostcreateinfo1[k];
			com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.CommitDatasetInfo acommitdatasetinfo[] = postcreateinfo.commitInfos;
			com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.CommitDatasetInfo acommitdatasetinfo1[];
			int j1 = (acommitdatasetinfo1 = acommitdatasetinfo).length;
			for (int i1 = 0; i1 < j1; i1++) {
				com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.CommitDatasetInfo commitdatasetinfo = acommitdatasetinfo1[i1];
				TCComponentDataset tccomponentdataset = commitdatasetinfo.dataset;
				com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.FileTicketInfo fileticketinfo = commitdatasetinfo.fileTicketInfo;
				com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.FileInfo fileinfo = fileticketinfo.fileInfo;
				String s = fileinfo.fileName;
				String s1 = fileinfo.namedReferencedName;
				String s2 = "BINARY";
				if (fileinfo.isText)
					s2 = "TEXT";
				try {
					String as1[] = { s };
					String as2[] = { s1 };
					String as3[] = { s2 };
					String as4[] = { "Plain" };
					tccomponentdataset.setFiles(as1, as3, as4, as2);
					j++;
				} catch (TCException _ex) {
					throw new TCException(new int[] { 1 }, new int[1],
							new String[] { "IRDC失败2" });
				}
			}

			if (acommitdatasetinfo.length < as.length && tcexception == null)
				tcexception = new TCException(new int[] { 1 }, new int[1],
						new String[] { "IRDC失败3" });
			if (j == as.length)
				processRender(tccomponentitemrevision);
		}

		if (tcexception != null)
			throw tcexception;
		else
			return;
	}

	public void processRender(TCComponentItemRevision tccomponentitemrevision)
			throws TCException {
		try {
			ICoreConstantService icoreconstantservice = (ICoreConstantService) OSGIUtil
					.getService(RACUICommandsActivator.getDefault(),
							ICoreConstantService.class);
			String s = icoreconstantservice.getTypeConstant("ItemRevision",
					"RenderTSServiceName");
			if (s == null)
				throw new TCException("IRDC失败4");
			String s2 = icoreconstantservice.getTypeConstant("ItemRevision",
					"RenderProviderName");
			if (s2 == null)
				throw new TCException("IRDC失败5");
			TCComponent atccomponent[] = new TCComponent[1];
			atccomponent[0] = tccomponentitemrevision;
			HashMap hashmap = new HashMap();
			hashmap.put("ETSUpdOvr", DISP_REQ_ARG_FALSE_VAL);
			hashmap.put("RAC_IRDC_ATTACH_FILE", "RAC_IRDC_ATTACH_FILE");
			CreationResult creationresult = createRequest(s2, s, 2,
					atccomponent, atccomponent, null, 0, null,
					"CREATEITEM_RENDER", hashmap);
			if (creationresult == null)
				System.out.println("2222");
			else if (creationresult.getClientMessages() != null) {
				String as[] = creationresult.getClientMessages();
				StringBuilder stringbuilder = new StringBuilder();
				String as1[];
				int j = (as1 = as).length;
				for (int i = 0; i < j; i++) {
					String s3 = as1[i];
					stringbuilder.append(s3);
					stringbuilder.append('\n');
				}

			}
		} catch (Exception exception) {
			String s1 = exception.getMessage();
			if (!s1.isEmpty())
				System.out.println("3333");
		}
	}

	public CreationResult createRequest(String s, String s1, int i,
			TCComponent atccomponent[], TCComponent atccomponent1[], String s2,
			int j, String s3, String s4, Map map) throws TCException {
		com.teamcenter.services.rac.core._2008_06.DispatcherManagement.CreateDispatcherRequestArgs acreatedispatcherrequestargs[] = new com.teamcenter.services.rac.core._2008_06.DispatcherManagement.CreateDispatcherRequestArgs[1];
		acreatedispatcherrequestargs[0] = new com.teamcenter.services.rac.core._2008_06.DispatcherManagement.CreateDispatcherRequestArgs();
		acreatedispatcherrequestargs[0].providerName = s;
		acreatedispatcherrequestargs[0].serviceName = s1;
		acreatedispatcherrequestargs[0].priority = i;
		acreatedispatcherrequestargs[0].interval = j;
		if (atccomponent != null && atccomponent.length > 0)
			acreatedispatcherrequestargs[0].primaryObjects = atccomponent;
		if (atccomponent1 != null && atccomponent1.length > 0)
			acreatedispatcherrequestargs[0].secondaryObjects = atccomponent1;
		if (s2 != null)
			acreatedispatcherrequestargs[0].startTime = s2;
		if (s3 != null)
			acreatedispatcherrequestargs[0].endTime = s3;
		if (s4 != null)
			acreatedispatcherrequestargs[0].type = s4;
		if (map != null && !map.isEmpty()) {
			com.teamcenter.services.rac.core._2008_06.DispatcherManagement.KeyValueArguments akeyvaluearguments[] = new com.teamcenter.services.rac.core._2008_06.DispatcherManagement.KeyValueArguments[map
					.size()];
			Iterator iterator = map.entrySet().iterator();
			for (int k = 0; iterator.hasNext(); k++) {
				java.util.Map.Entry entry = (java.util.Map.Entry) iterator
						.next();
				akeyvaluearguments[k] = new com.teamcenter.services.rac.core._2008_06.DispatcherManagement.KeyValueArguments();
				akeyvaluearguments[k].key = (String) entry.getKey();
				akeyvaluearguments[k].value = (String) entry.getValue();
			}

			acreatedispatcherrequestargs[0].keyValueArgs = akeyvaluearguments;
		}
		CreationResult creationresult = null;
		try {
			TCSession tcsession = (TCSession) RACUICommandsActivator
					.getDefault().getSession();
			DispatcherManagementService dispatchermanagementservice = DispatcherManagementService
					.getService(tcsession);
			com.teamcenter.services.rac.core._2008_06.DispatcherManagement.CreateDispatcherRequestResponse createdispatcherrequestresponse = dispatchermanagementservice
					.createDispatcherRequest(acreatedispatcherrequestargs);
			String as[] = null;
			if (createdispatcherrequestresponse.svcData.sizeOfPartialErrors() > 0) {
				ErrorStack errorstack = createdispatcherrequestresponse.svcData
						.getPartialError(0);
				if (errorstack != null) {
					int ai[] = errorstack.getLevels();
					as = errorstack.getMessages();
					ErrorValue aerrorvalue[] = errorstack.getErrorValues();
					if (ai != null && ai.length > 0) {
						for (int l = 0; l < ai.length; l++)
							if (ai[l] == 3) {
								int i1 = aerrorvalue[l].getCode();
								StringBuilder stringbuilder = new StringBuilder();
								if (i1 == 260044) {
									stringbuilder.append("");
								} else {
									String as1[];
									int k1 = (as1 = as).length;
									for (int j1 = 0; j1 < k1; j1++) {
										String s5 = as1[j1];
										stringbuilder.append(s5);
										stringbuilder.append('\n');
									}

								}
								throw new Exception(stringbuilder.toString());
							}

					}
				}
			}
			creationresult = new CreationResult(
					createdispatcherrequestresponse.requestsCreated[0], as);
		} catch (Exception exception) {
			throw new TCException(exception);
		}
		return creationresult;
	}

	private static class CreationResult {

		public String[] getClientMessages() {
			return m_clientMessages;
		}

		private String m_clientMessages[];

		public CreationResult(TCComponent tccomponent, String as[]) {
			m_clientMessages = as;
		}
	}

	private static final String TS_SERVICE_TYPE = "CREATEITEM_RENDER";
	private static final String TS_SERVICE_VAR = "RenderTSServiceName";
	private static final String TS_PROVIDER_VAR = "RenderProviderName";
	private static final String DISP_REQ_ARG = "ETSUpdOvr";
	private static final String DISP_REQ_ARG_FALSE_VAL = Boolean.FALSE
			.toString();
	private static final String RAC_IRDC_ATTACHE_FILE_KEY_VAL = "RAC_IRDC_ATTACH_FILE";
	private static final int IRDC_NOT_REQUIRED_RENDER_ERR_NUM = 260044;
}
