/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 

package com.teamcenter.rac.ui.commands.genericAttach;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.aif.common.AIFDataBean;
import com.teamcenter.rac.commands.newdataset.DatasetInfoContainer;
import com.teamcenter.rac.commands.newdataset.MultipleDatasetJob;
import com.teamcenter.rac.common.create.DisplayTypeInfo;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentDataset;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.services.ICoreConstantService;
import com.teamcenter.rac.ui.commands.RACUICommandsActivator;
import com.teamcenter.rac.util.AdapterUtil;
import com.teamcenter.rac.util.OSGIUtil;
import com.teamcenter.rac.util.Registry;
import com.teamcenter.services.rac.core.DispatcherManagementService;
import com.teamcenter.services.rac.documentmanagement.DocumentControlService;
import com.teamcenter.soa.client.model.ErrorStack;
import com.teamcenter.soa.client.model.ErrorValue;

// Referenced classes of package com.teamcenter.rac.ui.commands.genericAttach:
//            AttachFilePage, GenericDatasetContainer

public class AttachFileOperation extends AbstractAIFOperation {
	private static class CreationResult {

		public String[] getClientMessages() {
			return m_clientMessages;
		}

		private String m_clientMessages[];

		public CreationResult(TCComponent tccomponent, String as[]) {
			m_clientMessages = as;
		}
	}

	public AttachFileOperation(List list, AIFDataBean aifdatabean) {
		m_reg = Registry.getRegistry(AttachFileOperation.class);
		m_components = list;
		m_bean = aifdatabean;
	}

	public void executeOperation() throws Exception {
		System.out.println("hello 11111   AttachFileOperation");

		if (m_components != null && !m_components.isEmpty()) {
			Registry registry = Registry.getRegistry(AttachFileOperation.class);
			boolean flag = false;
			TCComponentItemRevision tccomponentitemrevision = null;
			AIFDataBean aifdatabean = (AIFDataBean) m_bean.getProperty(registry
					.getString("AttachFilePage.NAME"));
			if (aifdatabean != null) {
				System.out.println("11111111111");
				Collection collection = (Collection) aifdatabean
						.getProperty(AttachFilePage.class.toString());
				if (collection != null) {
					System.out.println("22222222222222");
					for (Iterator iterator = m_components.iterator(); iterator
							.hasNext();) {
						TCComponent tccomponent = (TCComponent) iterator.next();
						TCComponentItem tccomponentitem = (TCComponentItem) AdapterUtil
								.getAdapter(tccomponent, TCComponentItem.class);
						if (tccomponentitem != null) {
							System.out.println("333333333333");
							tccomponentitemrevision = tccomponentitem
									.getLatestItemRevision();
							flag = tccomponentitemrevision.getTCProperty(
									"is_IRDC").getLogicalValue();
							break;
						}
					}

					if (tccomponentitemrevision != null && flag) {
						System.out.println("444444444444444444");
						ArrayList arraylist = new ArrayList();
						GenericDatasetContainer genericdatasetcontainer;
						for (Iterator iterator1 = collection.iterator(); iterator1
								.hasNext(); arraylist
								.add(genericdatasetcontainer.getAbsolutePath()))
							genericdatasetcontainer = (GenericDatasetContainer) iterator1
									.next();

						if (!arraylist.isEmpty()) {
							System.out.println("5555555555555");
							createAndrelateIRDCDatasets(
									tccomponentitemrevision, arraylist);
						}
						final boolean checkedOut = tccomponentitemrevision
								.isCheckedOut();
						final TCComponentItemRevision itemRev = tccomponentitemrevision;
						if (checkedOut) {
							// Runnable runnable = new Runnable() {
							// public void run() {
							// DownloadDatasetDialog downloaddatasetdialog = new
							// DownloadDatasetDialog(
							// itemRev, checkedOut);
							// downloaddatasetdialog.setVisible(true);
							// }
							//
							// final AttachFileOperation this$0;
							// private final TCComponentItemRevision
							// val$itemRev;
							// private final boolean val$checkedOut;
							//
							// // {
							//
							// // this$0 = AttachFileOperation.this;
							// // itemRev = tccomponentitemrevision;
							// // checkedOut = flag;
							//
							// // }
							// };
							// SwingUtilities.invokeLater(runnable);
						}
					} else {
						System.out.println("66666666666666666666");

						ArrayList arraylist1 = new ArrayList();
						for (Iterator iterator2 = collection.iterator(); iterator2
								.hasNext();) {
							System.out.println("777777777777777");
							GenericDatasetContainer genericdatasetcontainer1 = (GenericDatasetContainer) iterator2
									.next();
							DatasetInfoContainer datasetinfocontainer = (DatasetInfoContainer) AdapterUtil
									.getAdapter(genericdatasetcontainer1,
											DatasetInfoContainer.class);
							if (datasetinfocontainer != null) {
								System.out.println("8888888888888888888");
								List list = genericdatasetcontainer1
										.getTargetTypeList();
								String s = (String) genericdatasetcontainer1
										.getProperty("targetSelectedValue");
								DisplayTypeInfo displaytypeinfo = genericdatasetcontainer1
										.getMatchedDisplayInfoGivenDisplayName(
												list,
												genericdatasetcontainer1
														.getTargetSelectedValue());
								if (displaytypeinfo != null)
									s = displaytypeinfo.getTypeName();
								Object obj = null;
								for (Iterator iterator3 = m_components
										.iterator(); iterator3.hasNext();) {
									TCComponent tccomponent1 = (TCComponent) iterator3
											.next();
									if (tccomponent1.getType().equals(s)) {
										obj = tccomponent1;
										break;
									}
								}

								datasetinfocontainer
										.setPasteTargets(((com.teamcenter.rac.aif.kernel.InterfaceAIFComponent) (obj)));
								arraylist1.add(datasetinfocontainer);
							}
						}

						MultipleDatasetJob multipledatasetjob = new MultipleDatasetJob(
								getName(), arraylist1);
						multipledatasetjob.run(null);
					}
				}
			}
		}
	}

	private void createAndrelateIRDCDatasets(
			TCComponentItemRevision tccomponentitemrevision, ArrayList arraylist)
			throws TCException {
		DocumentControlService documentcontrolservice = DocumentControlService
				.getService((TCSession) getSession());
		com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.PostCreateResponse postcreateresponse = null;
		com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.PostCreateInputs apostcreateinputs[] = new com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.PostCreateInputs[1];
		apostcreateinputs[0] = new com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.PostCreateInputs();
		apostcreateinputs[0].clientId = Integer.toString(1);
		apostcreateinputs[0].itemRevision = tccomponentitemrevision;
		Object aobj[] = arraylist.toArray();
		String as[] = new String[aobj.length];
		System.out.println("size---->" + as.length);
		for (int i = 0; i < aobj.length; i++) {
			as[i] = aobj[i].toString();
			System.out.println("---->" + as[i]);
		}

		apostcreateinputs[0].fileNames = as;
		postcreateresponse = documentcontrolservice
				.postCreate(apostcreateinputs);
		com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.PostCreateInfo apostcreateinfo[] = postcreateresponse.output;
		if (apostcreateinfo.length <= 0)
			throw new TCException(new int[] { 1 }, new int[1],
					new String[] { m_reg.getString("failAttachLocalFiles") });
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
							new String[] { m_reg
									.getString("failUploadLocalFiles") });
				}
			}

			if (acommitdatasetinfo.length < as.length && tcexception == null)
				tcexception = new TCException(
						new int[] { 1 },
						new int[1],
						new String[] { m_reg.getString("failAttachLocalFiles") });
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
				throw new TCException(
						m_reg.getString("CreateRender.NoProviderService"));
			String s2 = icoreconstantservice.getTypeConstant("ItemRevision",
					"RenderProviderName");
			if (s2 == null)
				throw new TCException(
						m_reg.getString("CreateRender.NoProviderService"));
			TCComponent atccomponent[] = new TCComponent[1];
			atccomponent[0] = tccomponentitemrevision;
			HashMap hashmap = new HashMap();
			hashmap.put("ETSUpdOvr", DISP_REQ_ARG_FALSE_VAL);
			hashmap.put("RAC_IRDC_ATTACH_FILE", "RAC_IRDC_ATTACH_FILE");
			CreationResult creationresult = createRequest(s2, s, 2,
					atccomponent, atccomponent, null, 0, null,
					"CREATEITEM_RENDER", hashmap);
			if (creationresult == null)
				Logger.getLogger(AttachFileOperation.class).warn(
						m_reg.getString("CreateRender.RequestCreationError"));
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

				Logger.getLogger(AttachFileOperation.class).warn(
						stringbuilder.toString());
			}
		} catch (Exception exception) {
			String s1 = exception.getMessage();
			if (!s1.isEmpty())
				Logger.getLogger(AttachFileOperation.class).warn(s1);
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

	private Registry m_reg;
	private AIFDataBean m_bean;
	private List m_components;
	private static final String TS_SERVICE_TYPE = "CREATEITEM_RENDER";
	private static final String TS_SERVICE_VAR = "RenderTSServiceName";
	private static final String TS_PROVIDER_VAR = "RenderProviderName";
	private static final String DISP_REQ_ARG = "ETSUpdOvr";
	private static final String DISP_REQ_ARG_FALSE_VAL;
	private static final String RAC_IRDC_ATTACHE_FILE_KEY_VAL = "RAC_IRDC_ATTACH_FILE";
	private static final int IRDC_NOT_REQUIRED_RENDER_ERR_NUM = 260044;

	static {
		DISP_REQ_ARG_FALSE_VAL = Boolean.FALSE.toString();
	}
}

/*
 * DECOMPILATION REPORT
 * 
 * Decompiled from:
 * F:\Teamcenter11ENV\JAVA\rac\plugins\com.teamcenter.rac.ui.commands_11000
 * .2.0.jar Total time: 110 ms Jad reported messages/errors: Exit status: 0
 * Caught exceptions:
 */