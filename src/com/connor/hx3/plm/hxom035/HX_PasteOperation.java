/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package com.connor.hx3.plm.hxom035;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.connor.hx3.plm.util.HxomMethodUtil;
import com.connor.hx3.plm.util.HxomStaticFinal;
import com.teamcenter.rac.aif.AIFClipboard;
import com.teamcenter.rac.aif.AIFDesktop;
import com.teamcenter.rac.aif.AIFPortal;
import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aif.kernel.AbstractAIFSession;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.commands.newproxylink.NewProxyLinkCommand;
import com.teamcenter.rac.commands.newproxylink.NewProxyLinkOperation;
import com.teamcenter.rac.commands.open.OpenCommand;
import com.teamcenter.rac.commands.paste.PasteOperation;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentDataset;
import com.teamcenter.rac.kernel.TCComponentForm;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentTcFile;
import com.teamcenter.rac.kernel.TCComponentTempProxyLink;
import com.teamcenter.rac.kernel.TCComponentTraceLink;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCComponentUser;
import com.teamcenter.rac.kernel.TCComponentWorkContext;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCPreferenceService;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.ConfirmationDialog;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.Registry;
import com.teamcenter.rac.util.log.Debug;

/**
 * 
 * 
 * @author hub
 * 
 */
public class HX_PasteOperation extends PasteOperation {

	public HX_PasteOperation(AbstractAIFUIApplication abstractaifuiapplication,
			TCComponent tccomponent,
			InterfaceAIFComponent interfaceaifcomponent, String s) {
		super(abstractaifuiapplication, tccomponent, interfaceaifcomponent, s);
		statusUse = false;
		cookieName = "PasteConfirm";
		openAfterPasteFlag = false;
		AIFComponentContext aifcomponentcontext = new AIFComponentContext(
				tccomponent, interfaceaifcomponent, s);
		contextArray = (new AIFComponentContext[] { aifcomponentcontext });
	}

	public HX_PasteOperation(AbstractAIFUIApplication abstractaifuiapplication,
			TCComponent tccomponent,
			InterfaceAIFComponent ainterfaceaifcomponent[], String s) {
		super(abstractaifuiapplication, tccomponent, ainterfaceaifcomponent, s);
		statusUse = false;
		cookieName = "PasteConfirm";
		openAfterPasteFlag = false;
		parentComp = tccomponent;
		childrenComps = ainterfaceaifcomponent;
		contextString = s;
	}

	public HX_PasteOperation(AbstractAIFUIApplication abstractaifuiapplication,
			TCComponent tccomponent,
			InterfaceAIFComponent ainterfaceaifcomponent[], String s,
			boolean flag) {
		super(abstractaifuiapplication, tccomponent, ainterfaceaifcomponent, s,
				flag);
		statusUse = false;
		cookieName = "PasteConfirm";
		openAfterPasteFlag = false;
		parentComp = tccomponent;
		childrenComps = ainterfaceaifcomponent;
		contextString = s;
		openAfterPasteFlag = flag;
	}

	public HX_PasteOperation(TCComponent tccomponent,
			InterfaceAIFComponent ainterfaceaifcomponent[], String s) {
		super(tccomponent, ainterfaceaifcomponent, s);
		statusUse = false;
		cookieName = "PasteConfirm";
		openAfterPasteFlag = false;
		parentComp = tccomponent;
		childrenComps = ainterfaceaifcomponent;
		contextString = s;
	}

	public HX_PasteOperation(AbstractAIFUIApplication abstractaifuiapplication,
			AIFComponentContext aifcomponentcontext) {
		super(abstractaifuiapplication, aifcomponentcontext);
		statusUse = false;
		cookieName = "PasteConfirm";
		openAfterPasteFlag = false;
		contextArray = (new AIFComponentContext[] { aifcomponentcontext });
	}

	public HX_PasteOperation(AIFComponentContext aifcomponentcontext) {
		super(aifcomponentcontext);
		statusUse = false;
		cookieName = "PasteConfirm";
		openAfterPasteFlag = false;
		contextArray = (new AIFComponentContext[] { aifcomponentcontext });
	}

	public HX_PasteOperation(AIFComponentContext aifcomponentcontext,
			boolean flag) {
		super(aifcomponentcontext, flag);
		statusUse = false;
		cookieName = "PasteConfirm";
		openAfterPasteFlag = false;
		contextArray = (new AIFComponentContext[] { aifcomponentcontext });
		openAfterPasteFlag = flag;
	}

	public HX_PasteOperation(AbstractAIFUIApplication abstractaifuiapplication,
			AIFComponentContext aaifcomponentcontext[]) {
		this(aaifcomponentcontext);
	}

	public HX_PasteOperation(AIFComponentContext aaifcomponentcontext[]) {
		super(aaifcomponentcontext);
		statusUse = false;
		cookieName = "PasteConfirm";
		openAfterPasteFlag = false;
		contextArray = aaifcomponentcontext;
	}

	public HX_PasteOperation(TCComponent tccomponent,
			InterfaceAIFComponent ainterfaceaifcomponent[], String s,
			Object obj, boolean flag) {
		this(tccomponent, ainterfaceaifcomponent, s, obj, flag, false);
	}

	public HX_PasteOperation(TCComponent tccomponent,
			InterfaceAIFComponent ainterfaceaifcomponent[], String s,
			Object obj, boolean flag, boolean flag1) {
		super(tccomponent, ainterfaceaifcomponent, s, obj, flag, flag1);
		statusUse = false;
		cookieName = "PasteConfirm";
		openAfterPasteFlag = false;

		parentComp = tccomponent;

		childrenComps = ainterfaceaifcomponent;
		contextString = s;
		operationResultNew = obj;
		statusUse = flag;
		openAfterPasteFlag = flag1;
	}

	private void getMsg() throws TCException {
		if (parentComp.getType().equals(HxomStaticFinal.HX3_PASTE_PARENT_TYPE)) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			TCComponent[] excelDatasetS = parentComp
					.getReferenceListProperty(HxomStaticFinal.TC_Attaches);
			if (excelDatasetS.length != 0
					&& excelDatasetS[0] instanceof TCComponentDataset) {

				TCProperty[] pPropS = parentComp.getTCProperties(new String[] {
						"owning_user", "date_released", "owning_group" });
				TCProperty pProp = parentComp
						.getReferenceListProperty("IMAN_master_form_rev")[0]
						.getTCProperty("hx3_wfkh");
				String owning_user = pPropS[0].getReferenceValue()
						.getStringProperty("user_name");
				String date_released = pPropS[1].getDateValue() == null ? ""
						: sdf.format(pPropS[1].getDateValue());
				String hx3_wfkh = pProp == null ? "" : pProp.getDisplayValue();
				String owning_group = pPropS[2].getReferenceValue()
						.getStringProperty("display_name");

				TCComponentDataset excelComponentDataset = (TCComponentDataset) excelDatasetS[0];
				String path = HxomMethodUtil
						.downLoadFile(excelComponentDataset);

				TCComponent[] designComponents = parentComp
						.getReferenceListProperty(HxomStaticFinal.hx3_PASTE_GRM);

				List<TCComponent> designList = new ArrayList<>();
				for (TCComponent comp : designComponents) {
					if (comp instanceof TCComponentItem) {
						designList.add(((TCComponentItem) comp)
								.getLatestItemRevision());
					} else if (comp instanceof TCComponentItemRevision) {
						designList.add(comp);
					}
				}
				for (InterfaceAIFComponent comp : childrenComps) {
					if (comp instanceof TCComponentItem) {
						designList.add(((TCComponentItem) comp)
								.getLatestItemRevision());
					} else if (comp instanceof TCComponentItemRevision) {
						designList.add((TCComponentItemRevision) comp);
					}
				}
				TCProperty[][] propS = TCComponentType.getTCPropertiesSet(
						designList, new String[] { "item_id",
								"item_revision_id", "object_name" });
				List<PasteBean> beanList = new ArrayList<>();

				for (int i = 0; i < propS.length; i++) {
					PasteBean bean = new PasteBean("" + i,
							propS[i][0] == null ? "" : propS[i][0]
									.getStringValue(),
							propS[i][1] == null ? "" : propS[i][1]
									.getStringValue(), propS[i][2] == null ? ""
									: propS[i][2].getStringValue(),
							owning_user, date_released, hx3_wfkh, owning_group);

					beanList.add(bean);

				}
				//
				if (path.endsWith(".xlsx")) {
					try {
						String pathOut = path.replace(".xlsx", "_1.xlsx");
						ExcelUtil.writeEmptyPasteMSG(path);
						ExcelUtil.writePasteMSG(path, pathOut, beanList);

						HxomMethodUtil.changeDataSet(excelComponentDataset,
								"excel", "MSExcelX", pathOut);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

		}

	}

	public void executeOperation() throws TCException {

		System.out.println("paste");

		try {
			getMsg();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TCSession tcsession = (TCSession) getSession();
		Registry registry = Registry.getRegistry(this);
		if (parentComp != null && childrenComps != null) {
			try {
				executeByChunk();
			} catch (TCException tcexception) {
				throw tcexception;
			}
			if (openAfterPasteFlag)
				try {
					OpenCommand opencommand = (OpenCommand) registry
							.newInstanceForEx("openCommand", new Object[] {
									AIFUtility.getActiveDesktop(),
									childrenComps });
					opencommand.executeModeless();
				} catch (Exception exception) {
					logger.error(exception.getLocalizedMessage(), exception);
				}
			return;
		}
		Object obj = null;
		Object obj1 = null;
		ArrayList arraylist = new ArrayList();
		boolean flag = false;
		try {
			ArrayList arraylist1 = new ArrayList();
			for (int i = 0; i < contextArray.length; i++) {
				TCComponent tccomponent2 = (TCComponent) contextArray[i]
						.getParentComponent();
				if (contextArray[i].getComponent() instanceof TCComponentTempProxyLink) {
					TCComponentTempProxyLink tccomponenttempproxylink = (TCComponentTempProxyLink) contextArray[i]
							.getComponent();
					setStatus(MessageFormat.format(
							registry.getString("pastingInto"), new Object[] {
									tccomponenttempproxylink.toString(),
									tccomponent2.toString() }));
					NewProxyLinkCommand newproxylinkcommand = new NewProxyLinkCommand(
							tccomponenttempproxylink.getSession(),
							AIFDesktop.getActiveDesktop(),
							tccomponenttempproxylink.getAppId(),
							new String[] { tccomponenttempproxylink.getObjId() },
							new InterfaceAIFComponent[] { tccomponent2 },
							Boolean.FALSE);
					newproxylinkcommand.executeModal();
					NewProxyLinkOperation newproxylinkoperation = newproxylinkcommand
							.getOperation();
					if (newproxylinkoperation != null) {
						TCComponent atccomponent[] = newproxylinkoperation
								.getNewObjectLinks();
						if (atccomponent != null && atccomponent.length > 0) {
							TCComponent tccomponent = atccomponent[0];
							String s = tccomponent2
									.getPreferredPasteRelation(tccomponent);
							contextArray[i] = new AIFComponentContext(
									tccomponent2, tccomponent, s);
						} else {
							contextArray[i] = null;
						}
					} else {
						contextArray[i] = null;
					}
				} else {
					TCComponent tccomponent1 = (TCComponent) contextArray[i]
							.getComponent();
					setStatus(MessageFormat.format(
							registry.getString("pastingInto"),
							new Object[] { tccomponent1.toString(),
									tccomponent2.toString() }));
					contextArray[i] = processPaste(contextArray[i], arraylist1);
					arraylist.add(tccomponent1);
					if (!flag
							&& (tccomponent1 instanceof TCComponentWorkContext))
						flag = true;
				}
			}

			pasteInChunk(arraylist1);
		} catch (Exception exception1) {
			if (Debug.isOn("action,copy,cut,paste"))
				Debug.printStackTrace(exception1);
			throw new TCException(exception1);
		}
		if (flag)
			clearWorkContextProperty(tcsession);
		if (openAfterPasteFlag && !arraylist.isEmpty()) {
			InterfaceAIFComponent ainterfaceaifcomponent[] = (InterfaceAIFComponent[]) arraylist
					.toArray(new InterfaceAIFComponent[arraylist.size()]);
			try {
				OpenCommand opencommand1 = (OpenCommand) registry
						.newInstanceForEx("openCommand", new Object[] {
								AIFUtility.getActiveDesktop(),
								ainterfaceaifcomponent });
				opencommand1.executeModeless();
			} catch (Exception exception2) {
				logger.error(exception2.getLocalizedMessage(), exception2);
			}
		}
	}

	private void executeByChunk() throws TCException {
		if (parentComp == null || childrenComps == null)
			return;
		TCSession tcsession = parentComp.getSession();
		Registry registry = Registry.getRegistry(this);
		AIFComponentContext aaifcomponentcontext[] = new AIFComponentContext[childrenComps.length];
		boolean flag = false;
		try {
			StringBuilder stringbuilder = new StringBuilder();
			for (int i = 0; i < childrenComps.length; i++) {
				stringbuilder.append(i != 0 ? ", " : "");
				stringbuilder.append(childrenComps[i]);
			}

			setStatus(MessageFormat.format(
					registry.getString("pastingInto"),
					new Object[] { stringbuilder.toString(),
							parentComp.toString() }));
			for (int j = 0; j < childrenComps.length; j++) {
				aaifcomponentcontext[j] = new AIFComponentContext(parentComp,
						childrenComps[j], contextString);
				if (!flag
						&& (childrenComps[j] instanceof TCComponentWorkContext))
					flag = true;
			}

			boolean flag1 = isConfirmPasteEnabled();
			boolean flag2 = false;
			if (flag1)
				flag2 = isExplicitPasteOperation();
			if (flag2) {
				int k = ConfirmationDialog.post(AIFDesktop.getActiveDesktop(),
						registry.getString("pasteConfirmTitle"),
						registry.getString("pasteConfirmText"), false,
						cookieName);
				if (k == 2)
					if ((parentComp instanceof TCComponentTraceLink)
							&& ((TCComponent) childrenComps[0])
									.isTypeOf("Fnd0CustomNoteRevision"))
						pasteToNoteProperty();
					else
						storeOperationResult(parentComp
								.pasteOperation(aaifcomponentcontext));
			} else if ((parentComp instanceof TCComponentTraceLink)
					&& ((TCComponent) childrenComps[0])
							.isTypeOf("Fnd0CustomNoteRevision"))
				pasteToNoteProperty();
			else if ((parentComp instanceof TCComponentDataset)
					&& contextString.equals("ref_list")) {
				TCComponentDataset tccomponentdataset = (TCComponentDataset) parentComp;
				for (int l = 0; l < childrenComps.length; l++) {
					TCComponent tccomponent = (TCComponent) childrenComps[l];
					if (tccomponent instanceof TCComponentTcFile) {
						TCComponentTcFile tccomponenttcfile = (TCComponentTcFile) tccomponent;
						tccomponentdataset.addNamedReference(tccomponent,
								tccomponenttcfile
										.getRefDatasetNamedReferences());
					} else if (tccomponent instanceof TCComponentForm) {
						TCComponentForm tccomponentform = (TCComponentForm) tccomponent;
						tccomponentdataset.addNamedReference(tccomponent,
								tccomponentform.getRefDatasetNamedReferences());
					}
					parentComp.refresh();
				}

			} else {
				storeOperationResult(parentComp
						.pasteOperation(aaifcomponentcontext));
			}
		} catch (TCException tcexception) {
			int ai[] = tcexception.getErrorCodes();
			String s = contextString;
			if (ai[0] == 38015) {
				if (s == null || s.isEmpty())
					s = parentComp.getDefaultPasteRelation();
				String s1 = parentComp.getType();
				Object aobj[] = { s1, s };
				String s2 = MessageFormat.format(registry
						.getString("formattedAttachmentPanelPasteError"), aobj);
				MessageBox.post(s2.trim(), registry.getString("warning.TITLE"),
						4);
			} else {
				throw tcexception;
			}
		}
		if (flag)
			clearWorkContextProperty(tcsession);
	}

	private void clearWorkContextProperty(AbstractAIFSession abstractaifsession) {
		if (abstractaifsession != null
				&& (abstractaifsession instanceof TCSession)) {
			TCSession tcsession = (TCSession) abstractaifsession;
			TCComponentUser tccomponentuser = tcsession.getUser();
			if (tccomponentuser != null)
				tccomponentuser.clearCache("work_contexts");
		}
	}

	private boolean isConfirmPasteEnabled() {
		TCPreferenceService tcpreferenceservice = ((TCSession) AIFUtility
				.getCurrentApplication().getSession()).getPreferenceService();
		Boolean boolean1 = tcpreferenceservice
				.getLogicalValue("TC_confirm_paste_operation");
		return boolean1 != null && boolean1.booleanValue();
	}

	private boolean isExplicitPasteOperation() {
		Vector vector = null;
		boolean flag = false;
		AIFClipboard aifclipboard = AIFPortal.getClipboard();
		java.awt.datatransfer.Transferable transferable = aifclipboard
				.getContents(this);
		try {
			if (transferable != null)
				vector = (Vector) transferable
						.getTransferData(new java.awt.datatransfer.DataFlavor(
								java.util.Vector.class, "AIF Vector"));
		} catch (Exception exception) {
			logger.error(exception.getLocalizedMessage(), exception);
			vector = null;
		}
		if (vector != null) {
			flag = true;
			for (int i = 0; i < childrenComps.length; i++) {
				int j = 0;
				for (int k = 0; k < vector.size(); k++) {
					if (((TCComponent) vector.get(k)).equals(childrenComps[i]))
						break;
					j++;
				}

				if (j != vector.size())
					continue;
				flag = false;
				break;
			}

			vector.clear();
			vector = null;
		}
		return flag;
	}

	private void pasteToNoteProperty() throws TCException {
		TCProperty tcproperty = parentComp.getTCProperty("fnd0CustomNotes");
		if (tcproperty != null) {
			TCComponent atccomponent[] = tcproperty.getReferenceValueArray();
			ArrayList arraylist = new ArrayList();
			for (int i = 0; i < atccomponent.length; i++)
				arraylist.add(atccomponent[i]);

			for (int j = 0; j < childrenComps.length; j++)
				arraylist.add((TCComponent) childrenComps[j]);

			TCComponent atccomponent1[] = new TCComponent[arraylist.size()];
			atccomponent1 = (TCComponent[]) arraylist
					.toArray(new TCComponent[arraylist.size()]);
			tcproperty.setReferenceValueArray(atccomponent1);
		}
	}

	public AIFComponentContext[] getTargetContexts() {
		return contextArray;
	}

	public Map getPartialFailedTargets() {
		return Collections.EMPTY_MAP;
	}

	private AIFComponentContext contextArray[];
	private InterfaceAIFComponent childrenComps[];
	private TCComponent parentComp;
	private String contextString;
	protected Object operationResultNew;
	protected boolean statusUse;
	private String cookieName;
	private boolean openAfterPasteFlag;
	public static final int relationNotFoundErrorCode = 38015;
	private static final Logger logger = Logger
			.getLogger(com.teamcenter.rac.commands.paste.PasteOperation.class);

}

/*
 * DECOMPILATION REPORT
 * 
 * Decompiled from:
 * F:\Teamcenter11ENV\JAVA\rac\plugins\com.teamcenter.rac.common_11000.2.0.jar
 * Total time: 90 ms Jad reported messages/errors: Exit status: 0 Caught
 * exceptions:
 */