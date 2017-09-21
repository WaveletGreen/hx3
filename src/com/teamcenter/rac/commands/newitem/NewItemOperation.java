/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 

package com.teamcenter.rac.commands.newitem;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Assert;

import com.teamcenter.rac.aif.AIFDesktop;
import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aif.ICommandListener;
import com.teamcenter.rac.aif.ICommandListenerEvent;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.commands.newalternateid.AdditionalIdInfoPanel;
import com.teamcenter.rac.commands.newalternateid.AdditionalRevInfoPanel;
import com.teamcenter.rac.commands.open.OpenCommand;
import com.teamcenter.rac.commands.paste.PasteCommand;
import com.teamcenter.rac.common.AbstractTCApplication;
import com.teamcenter.rac.common.Activator;
import com.teamcenter.rac.common.DownloadDatasetDialog;
import com.teamcenter.rac.common.create.ICreateInputProvider;
import com.teamcenter.rac.common.create.ICreateInstanceInput;
import com.teamcenter.rac.common.create.SOAGenericCreateHelper;
import com.teamcenter.rac.form.AbstractTCForm;
import com.teamcenter.rac.kernel.ItemValidationInfo;
import com.teamcenter.rac.kernel.SoaUtil;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentDataset;
import com.teamcenter.rac.kernel.TCComponentForm;
import com.teamcenter.rac.kernel.TCComponentIdentifier;
import com.teamcenter.rac.kernel.TCComponentIdentifierType;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCComponentProject;
import com.teamcenter.rac.kernel.TCComponentProjectType;
import com.teamcenter.rac.kernel.TCComponentPseudoFolder;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCComponentUnitOfMeasureType;
import com.teamcenter.rac.kernel.TCDateEncoder;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCReservationService;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.services.ICoreConstantService;
import com.teamcenter.rac.services.ISessionService;
import com.teamcenter.rac.stylesheet.AbstractRendering;
import com.teamcenter.rac.stylesheet.AutomaticRendering;
import com.teamcenter.rac.ui.commands.RACUICommandsActivator;
import com.teamcenter.rac.util.AdapterUtil;
import com.teamcenter.rac.util.ConfirmationDialog;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.OSGIUtil;
import com.teamcenter.rac.util.Registry;
import com.teamcenter.rac.util.log.Debug;
import com.teamcenter.rac.util.progress.ProgressIndicator;
import com.teamcenter.rac.util.wizard.AbstractControlWizardStepPanel;
import com.teamcenter.services.rac.core.DataManagementService;
import com.teamcenter.services.rac.core.DispatcherManagementService;
import com.teamcenter.services.rac.documentmanagement.DocumentControlService;
import com.teamcenter.services.rac.workflow.WorkflowService;
import com.teamcenter.soa.client.model.ErrorStack;
import com.teamcenter.soa.client.model.ErrorValue;

// Referenced classes of package com.teamcenter.rac.commands.newitem:
//            NewItemDialog, AbstractNewItemPanel, NewItemFinishPanel, NewItemCommand, 
//            IdUtils, ItemAttachFilesPanel, SubmitToWorkflowPanel, CreationResult, 
//            ItemInfoPanel, ItemMasterFormPanel, ItemRevMasterFormPanel, NewItemAlternateIdInfoPanel

/**
 * @deprecated Class NewItemOperation is deprecated
 */

public class NewItemOperation extends AbstractAIFOperation {
	private final class IC_NewItemDialogListener extends
			java.awt.event.WindowAdapter {

		public void windowClosed(java.awt.event.WindowEvent windowevent) {
			newItemDialogClosed = true;
			evaluateDialogsClosed();
			windowevent.getWindow().removeWindowListener(this);
		}

		final NewItemOperation this$0;

		private IC_NewItemDialogListener() {
			super();
			this$0 = NewItemOperation.this;

		}

		IC_NewItemDialogListener(
				IC_NewItemDialogListener ic_newitemdialoglistener) {
			this();
		}
	}

	protected final class IC_PasteCommandListener implements ICommandListener {

		public void commandStarting(ICommandListenerEvent icommandlistenerevent) {
		}

		public void commandDone(ICommandListenerEvent icommandlistenerevent) {
			pasteComplete = true;
			evaluateDialogsClosed();
			icommandlistenerevent.getCommand().removeCommandListener(this);
		}

		final NewItemOperation this$0;

		public IC_PasteCommandListener() {
			super();
			this$0 = NewItemOperation.this;

		}
	}

	private static final class IC_SendToClassification implements Runnable {

		public void run() {
			String s = "com.teamcenter.rac.classification.icm.ClassificationPerspective";
			Activator.getDefault().openPerspective(s);
			Activator.getDefault().openComponents(s, components);
		}

		InterfaceAIFComponent components[];

		public IC_SendToClassification(
				InterfaceAIFComponent ainterfaceaifcomponent[]) {
			components = ainterfaceaifcomponent;
		}
	}

	public NewItemOperation(NewItemDialog newitemdialog) {
		openOnCreateFlag = false;
		checkOutOnCreateFlag = false;
		classifyFlag = false;
		isCI = false;
		classifyType = "";
		successFlag = true;
		itemsQuantity = 1;
		useSOA = false;
		swingRunnables = new ArrayList();
		newItemDialogClosed = false;
		pasteComplete = false;
		m_pasteRevisionFlag = false;
		dialog = newitemdialog;
		dialog.addWindowListener(new IC_NewItemDialogListener(null));
		desktop = AIFDesktop.getActiveDesktop();
		itemName = newitemdialog.getItemName();
		itemId = newitemdialog.getItemId();
		revId = newitemdialog.getItemRev();
		itemDesc = newitemdialog.getItemDescription();
		itemType = newitemdialog.getItemType();
		isCI = newitemdialog.isConfigurationItem();
		uomComp = newitemdialog.getUOMComp();
		openOnCreateFlag = newitemdialog.getOpenOnCreateFlag();
		classifyFlag = newitemdialog.getClassifyFlag();
		classifyType = newitemdialog.getClassifyType();
		m_pasteRelation = newitemdialog.getPasteRelation();
		m_pasteRevisionFlag = newitemdialog.getRevisionFlag();
		try {
			pasteTargets = newitemdialog.getTargets();
		} catch (Exception exception) {
			MessageBox.post(dialog, exception);
		}
		newComp = newitemdialog.dialogPanel.newComponent;
		newIds = newitemdialog.dialogPanel.newIds;
		projects = newitemdialog.getSelectedProjects();
		processIndicator = newitemdialog.dialogPanel.finishPanel.pi;
		itemsQuantity = newitemdialog.getQuantity();
	}

	public NewItemOperation(NewItemCommand newitemcommand) {
		this();
		cmd = newitemcommand;
		desktop = null;
		try {
			pasteTargets = newitemcommand.getTargets();
		} catch (Exception exception) {
			MessageBox.post(exception);
		}
	}

	public NewItemOperation() {
		openOnCreateFlag = false;
		checkOutOnCreateFlag = false;
		classifyFlag = false;
		isCI = false;
		classifyType = "";
		successFlag = true;
		itemsQuantity = 1;
		useSOA = false;
		swingRunnables = new ArrayList();
		newItemDialogClosed = false;
		pasteComplete = false;
		m_pasteRevisionFlag = false;
	}

	public TCSession getSession() {
		return (TCSession) super.getSession();
	}

	public boolean getSuccessFlag() {
		return successFlag;
	}

	public TCComponent getNewComponent() {
		return newComp;
	}

	public TCComponentIdentifier[] getNewIds() {
		return newIds;
	}

	public void executeOperation() throws Exception {
		System.out.println("1111111111111");
		itemIds = new ArrayList();
		if (itemsQuantity > 1) {
			System.out.println("222222222222222");
			if (!dialog.isAutoExposureForCreateEnabled()) {
				IdUtils idutils = new IdUtils(getSession());
				for (int i = 0; i < itemsQuantity; i++)
					itemIds.add(idutils.generateId(itemType));

			}
			executeOperationInternal();
		} else {
			System.out.println("33333333333333333333");
			itemIds.add(itemId);
			executeOperationInternal();
			if (newComp instanceof TCComponentItem) {
				TCComponentItemRevision tccomponentitemrevision = ((TCComponentItem) newComp)
						.getLatestItemRevision();
				boolean flag = tccomponentitemrevision
						.getLogicalProperty("is_IRDC");
				if (!flag)
					return;
				System.out.println("444444444444444");
				if (dialog.dialogPanel.itemAttachFilesPanel != null
						&& dialog.dialogPanel.itemAttachFilesPanel.filesToAttach != null
						&& !dialog.dialogPanel.itemAttachFilesPanel.filesToAttach
								.isEmpty()) {
					System.out.println("55555555555555");
					processPostCreate();
				}
				System.out.println("66666666666666");
				if (dialog.dialogPanel.submitToWorkflowPanel != null
						&& dialog.dialogPanel.submitToWorkflowPanel.templateList != null) {
					String s = dialog.dialogPanel.submitToWorkflowPanel.templateList
							.getTextField().getText();
					if (s != null && !s.isEmpty())
						createProcessForItemRev(tccomponentitemrevision);
				}
			}
		}
	}

	public void executeOperationInternal() throws Exception {
		if (newComp == null) {
			createComponentWhenNewCompIsNull();
		} else {
			addToCache(newComp);
			createComponentWhenNewCompIsNotNull();
		}
		executeOperationInternalContinued();
	}

	protected void createComponentWhenNewCompIsNull() throws Exception {
		if (itemsQuantity == 1)
			checkItemIdAndRev();
		if (processIndicator != null)
			processIndicator.setNextStep();
		boolean flag = false;
		if (supportsBatchProcessing() && !isBatchProcessingEnabled()
				&& itemsQuantity > 1) {
			enableBatchProcessing(true);
			flag = true;
		}
		for (int i = 0; i < itemsQuantity; i++) {
			if (!dialog.isAutoExposureForCreateEnabled())
				itemId = (String) itemIds.get(i);
			createComponent(null);
			if (!getSuccessFlag())
				break;
		}

		if (isBatchProcessingEnabled()) {
			finishBatchProcessing();
			if (flag)
				enableBatchProcessing(false);
		}
		if (processIndicator != null)
			processIndicator.setNextStep();
	}

	protected boolean supportsBatchProcessing() {
		return false;
	}

	protected boolean isBatchProcessingEnabled() {
		return m_batchProcessingEnabled;
	}

	protected void enableBatchProcessing(boolean flag) {
		if (flag && !supportsBatchProcessing()) {
			logger.error((new StringBuilder(
					"Internal error: enableBatchProcessing() invoked for class "))
					.append(getClass())
					.append(" even though this class does not support batch processing")
					.toString());
			return;
		} else {
			m_batchProcessingEnabled = flag;
			return;
		}
	}

	protected void createComponentWhenNewCompIsNotNull() throws TCException {
		newComp.unlock();
		newComp.removeLockForMultiSite();
		if (newComp instanceof TCComponentItem) {
			TCComponentItem tccomponentitem = null;
			TCComponentItemRevision tccomponentitemrevision = null;
			tccomponentitem = (TCComponentItem) newComp;
			tccomponentitemrevision = tccomponentitem.getLatestItemRevision();
			tccomponentitemrevision.removeLockForMultiSite();
		}
		if (!dialog.getIsDescChanged())
			updateDescription(newComps, true);
		if (!dialog.getIsUOMChanged())
			updateUOM(newComps);
	}

	protected void executeOperationInternalContinued() throws Exception {
		if (projects != null && projects.length > 0) {
			for (int i = 0; i < newComps.size(); i++) {
				newComp = (TCComponent) newComps.get(i);
				assignToProjects();
			}

			if (processIndicator != null)
				processIndicator.setNextStep();
		}
		if (dialog != null && dialog.toCreateAlternateId()) {
			for (int j = 0; j < newComps.size(); j++) {
				newComp = (TCComponent) newComps.get(j);
				operationForAlternateIds();
			}

			if (processIndicator != null)
				processIndicator.setNextStep();
		}
		if (newComps != null) {
			for (int k = 0; k < newComps.size(); k++) {
				newComp = (TCComponent) newComps.get(k);
				checkOutItemRev();
			}

		}
		if (processIndicator != null)
			processIndicator.setNextStep();
		if (newComps != null) {
			for (int l = 0; l < newComps.size(); l++) {
				newComp = (TCComponent) newComps.get(l);
				setFormAttrs();
			}

		}
		if (processIndicator != null)
			processIndicator.setNextStep();
		if (newComps != null)
			pasteNewComponents();
		if (processIndicator != null)
			processIndicator.setNextStep();
		if (!isAbortRequested() && newComp != null) {
			if (!newInApp() && openOnCreateFlag) {
				for (int i1 = 0; i1 < newComps.size(); i1++) {
					newComp = (TCComponent) newComps.get(i1);
					openNewComponent();
				}

			}
			for (int j1 = 0; j1 < newComps.size(); j1++) {
				newComp = (TCComponent) newComps.get(j1);
				classifyNewComponent();
			}

		}
		if (processIndicator != null)
			processIndicator.setNextStep();
	}

	protected void updateDescription(ArrayList arraylist, boolean flag) {
		ArrayList arraylist1 = new ArrayList();
		for (Iterator iterator = arraylist.iterator(); iterator.hasNext();) {
			TCComponent tccomponent = (TCComponent) iterator.next();
			arraylist1.add(tccomponent);
			if (flag)
				try {
					if (tccomponent instanceof TCComponentItem)
						arraylist1.add(((TCComponentItem) tccomponent)
								.getLatestItemRevision());
					else if (tccomponent instanceof TCComponentItemRevision)
						arraylist1.add(((TCComponentItemRevision) tccomponent)
								.getItem());
				} catch (TCException tcexception1) {
					logger.error(tcexception1.getLocalizedMessage(),
							tcexception1);
				}
		}

		if (!arraylist1.isEmpty()) {
			String as[] = { "object_desc" };
			try {
				TCProperty atcproperty[][] = TCComponentType
						.getTCPropertiesSet(arraylist1, as);
				TCProperty atcproperty2[][];
				int j = (atcproperty2 = atcproperty).length;
				for (int i = 0; i < j; i++) {
					TCProperty atcproperty1[] = atcproperty2[i];
					atcproperty1[0].setStringValueData(itemDesc);
				}

				TCComponentType.setPropertiesSet((TCComponent[]) arraylist1
						.toArray(new TCComponent[arraylist1.size()]),
						atcproperty);
			} catch (TCException tcexception) {
				logger.error(tcexception.getLocalizedMessage(), tcexception);
				Registry registry = Registry.getRegistry(this);
				MessageBox.post(desktop, tcexception.getDetailsMessage(),
						registry.getString("error.TITLE"), 4);
			}
		}
	}

	private void updateUOM(List list) {
		StringBuilder stringbuilder = new StringBuilder();
		TCComponent tccomponent = (TCComponent) AdapterUtil.getAdapter(uomComp,
				TCComponent.class);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			TCComponent tccomponent1 = (TCComponent) iterator.next();
			try {
				TCComponent tccomponent2 = tccomponent1
						.getReferenceProperty("uom_tag");
				if (tccomponent2 != null && !tccomponent2.equals(tccomponent)
						|| tccomponent != null
						&& !tccomponent.equals(tccomponent2))
					tccomponent1.setReferenceProperty("uom_tag", tccomponent);
			} catch (TCException tcexception) {
				logger.error(tcexception.getLocalizedMessage(), tcexception);
				stringbuilder.append(tccomponent1.toString());
				stringbuilder.append(" : ");
				stringbuilder.append(tcexception.getDetailsMessage());
				stringbuilder.append("\n");
			}
		}

		String s = stringbuilder.toString();
		if (s.length() > 0) {
			Registry registry = Registry.getRegistry(this);
			MessageBox.post(desktop, s, registry.getString("error.TITLE"), 4);
		}
	}

	public void processPostCreate() {
		System.out.println("7777777777777777");
		if (newComp == null) {
			dialog.dialogPanel.itemAttachFilesPanel.resetAttachPanel();
			return;
		}
		try {
			System.out.println("88888888888888888");
			TCComponentItem tccomponentitem = (TCComponentItem) newComp;
			TCComponentItemRevision tccomponentitemrevision = tccomponentitem
					.getLatestItemRevision();
			DocumentControlService documentcontrolservice = DocumentControlService
					.getService(getSession());
			com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.PostCreateResponse postcreateresponse = null;
			com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.PostCreateInputs apostcreateinputs[] = new com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.PostCreateInputs[1];
			apostcreateinputs[0] = new com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.PostCreateInputs();
			apostcreateinputs[0].clientId = Integer.toString(1);
			apostcreateinputs[0].itemRevision = tccomponentitemrevision;
			List list = dialog.dialogPanel.itemAttachFilesPanel.filesToAdd;
			Object aobj[] = list.toArray();
			String as[] = new String[aobj.length];
			for (int i = 0; i < aobj.length; i++) {
				System.out.println("=========>" + as[i]);
				as[i] = aobj[i].toString();
			}

			apostcreateinputs[0].fileNames = as;
			postcreateresponse = documentcontrolservice
					.postCreate(apostcreateinputs);
			com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.PostCreateInfo apostcreateinfo[] = postcreateresponse.output;
			if (apostcreateinfo.length <= 0) {
				Registry registry = Registry.getRegistry(this);
				MessageBox.post(desktop,
						registry.getString("failAttachLocalFiles"),
						registry.getString("error.TITLE"), 4);
			} else {
				int j = 0;
				for (int k = 0; k < apostcreateinfo.length; k++) {
					com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.CommitDatasetInfo acommitdatasetinfo[] = apostcreateinfo[k].commitInfos;
					TCComponentItemRevision tccomponentitemrevision1 = apostcreateinfo[k].itemRevision;
					for (int l = 0; l < acommitdatasetinfo.length; l++) {
						TCComponentDataset tccomponentdataset = acommitdatasetinfo[l].dataset;
						com.teamcenter.services.rac.documentmanagement._2008_06.DocumentControl.FileTicketInfo fileticketinfo = acommitdatasetinfo[l].fileTicketInfo;
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
							Registry registry2 = Registry.getRegistry(this);
							MessageBox
									.post(desktop, registry2
											.getString("failUploadLocalFiles"),
											registry2.getString("error.TITLE"),
											4);
						}
					}

					if (acommitdatasetinfo.length < as.length) {
						Registry registry1 = Registry.getRegistry(this);
						MessageBox.post(desktop,
								registry1.getString("failAttachLocalFiles"),
								registry1.getString("error.TITLE"), 4);
					}
					if (j == as.length)
						processRender(tccomponentitemrevision1);
				}

			}

		} catch (TCException tcexception) {
			MessageBox.post(desktop, tcexception);
		}
		dialog.dialogPanel.itemAttachFilesPanel.resetAttachPanel();
		return;

	}

	public void processRender(TCComponentItemRevision tccomponentitemrevision) {
		Registry registry = Registry.getRegistry(this);
		ICoreConstantService icoreconstantservice;
		String s;
		String s2;
		TCComponent atccomponent[];
		HashMap hashmap;
		CreationResult creationresult = null;
		String as[];
		StringBuffer stringbuffer;
		String s3;
		int i;
		int j;
		String as1[];
		try {
			icoreconstantservice = (ICoreConstantService) OSGIUtil.getService(
					RACUICommandsActivator.getDefault(),
					ICoreConstantService.class);
			// icoreconstantservice =
			// (ICoreConstantService)OSGIUtil.getService(Activator.getDefault(),
			// ICoreConstantService.class);
			s = icoreconstantservice.getTypeConstant("ItemRevision",
					"RenderTSServiceName");
			if (s == null) {
				MessageBox.post(desktop,
						registry.getString("CreateRender.NoProviderService"),
						registry.getString("CreateRender.DIALOG.TITLE"), 4);
				return;
			}
		} catch (Exception exception) {
			String s1 = exception.getMessage();
			if (!s1.isEmpty())
				MessageBox.post(desktop, s1,
						registry.getString("CreateRender.DIALOG.TITLE"), 4);
			return;
		}
		s2 = icoreconstantservice.getTypeConstant("ItemRevision",
				"RenderProviderName");
		if (s2 == null) {
			MessageBox.post(desktop,
					registry.getString("CreateRender.NoProviderService"),
					registry.getString("CreateRender.DIALOG.TITLE"), 4);
			return;
		}
		atccomponent = new TCComponent[1];
		atccomponent[0] = tccomponentitemrevision;
		hashmap = new HashMap();
		hashmap.put("ETSUpdOvr", "false");
		hashmap.put("RAC_IRDC_ATTACH_FILE", "RAC_IRDC_ATTACH_FILE");
		try {
			creationresult = createRequest(s2, s, 2, atccomponent,
					atccomponent, null, 0, null, "CREATEITEM_RENDER", hashmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (creationresult == null) {
			MessageBox.post(desktop,
					registry.getString("CreateRender.RequestCreationError"),
					registry.getString("CreateRender.DIALOG.TITLE"), 4);
			return;
		}
		if (creationresult.getClientMessages() != null) {
			as = creationresult.getClientMessages();
			stringbuffer = new StringBuffer();
			j = (as1 = as).length;
			for (i = 0; i < j; i++) {
				s3 = as1[i];
				stringbuffer.append(s3);
				stringbuffer.append('\n');
			}

			MessageBox.post(desktop, stringbuffer.toString(),
					registry.getString("CreateRender.DIALOG.TITLE"), 4);
			return;
		}
	}

	public void createProcessForItemRev(
			TCComponentItemRevision tccomponentitemrevision) {
		try {
			com.teamcenter.services.rac.workflow._2008_06.Workflow.ContextData contextdata = new com.teamcenter.services.rac.workflow._2008_06.Workflow.ContextData();
			String s = dialog.dialogPanel.submitToWorkflowPanel.templateList
					.getTextField().getText();
			contextdata.processTemplate = s != null ? s : "";
			String s1 = dialog.dialogPanel.submitToWorkflowPanel.assignmentList
					.getTextField().getText();
			contextdata.processAssignmentList = s1 != null ? s1 : "";
			contextdata.attachmentCount = 1;
			String as[] = new String[1];
			as[0] = tccomponentitemrevision.getUid();
			contextdata.attachments = as;
			int ai[] = new int[1];
			ai[0] = 1;
			contextdata.attachmentTypes = ai;
			WorkflowService workflowservice = WorkflowService
					.getService(getSession());
			workflowservice.createInstance(false, "",
					tccomponentitemrevision.getStringProperty("object_name"),
					"", "", contextdata);
		} catch (TCException tcexception) {
			MessageBox.post(desktop, tcexception);
			return;
		}
	}

	public void checkItemIdAndRev() throws TCException {
		logger.debug("NewItemOperation::checkItemIdAndRev()");
		if (dialog.dialogPanel.itemInfoPanel != null
				&& dialog.dialogPanel.itemInfoPanel.valObj != null)
			return;
		Registry registry = Registry.getRegistry(this);
		setStatus((new StringBuilder(String.valueOf(registry
				.getString("checkingIds")))).append("...").toString());
		try {
			TCComponentItemType tccomponentitemtype = (TCComponentItemType) getSession()
					.getTypeComponent(itemType);
			ItemValidationInfo itemvalidationinfo = tccomponentitemtype
					.validateId(itemId, revId, itemType);
			if (itemvalidationinfo.getModificationStatus() == 4) {
				itemId = itemvalidationinfo.getModifiedItemId();
				revId = itemvalidationinfo.getModifiedItemRevId();
			} else {
				if (itemvalidationinfo.getModificationStatus() == 2) {
					TCException tcexception1 = new TCException(
							registry.getString("invalidID.MESSAGE"));
					throw tcexception1;
				}
				if (itemvalidationinfo.getModificationStatus() == 5) {
					TCException tcexception2 = new TCException(
							registry.getString("sameAsOldId"));
					throw tcexception2;
				}
				if (itemvalidationinfo.getModificationStatus() == 3) {
					int i = ConfirmationDialog.post(
							desktop,
							registry.getString("modifiedID.TITLE"),
							(new StringBuilder(String.valueOf(itemId)))
									.append("/")
									.append(revId)
									.append(" ")
									.append(registry
											.getString("modifiedID.MESSAGE"))
									.append(" ")
									.append(itemvalidationinfo
											.getModifiedItemId())
									.append("/")
									.append(itemvalidationinfo
											.getModifiedItemRevId())
									.append("?").toString(), true);
					if (i == 2) {
						itemId = itemvalidationinfo.getModifiedItemId();
						revId = itemvalidationinfo.getModifiedItemRevId();
					} else if (i != 1) {
						TCException tcexception3 = new TCException(
								registry.getString("cancelModifiedID.MESSAGE"));
						throw tcexception3;
					}
				}
			}
		} catch (TCException tcexception) {
			MessageBox.post(dialog, tcexception);
			successFlag = false;
		}
	}

	public void createComponent(Object obj) throws Exception {
		logger.debug("NewItemOperation::createComponent()");
		Registry registry = Registry.getRegistry(this);
		setStatus((new StringBuilder(String.valueOf(registry
				.getString("creatingNewComponent")))).append(" ")
				.append(itemName).append(" ...").toString());
		List list = null;
		try {
			if (dialog.viDialog == null) {
				System.out.println("pppppppppppp");
				pasteTargets = PasteCommand.getParentsFromPreference(
						pasteTargets, getSession());
				if (dialog.isAutoExposureForCreateEnabled()) {
					System.out.println("aaaaaaaaaaaaaaa");
					list = getCreateInputs();
					if (list != null && !list.isEmpty())
						if (isBatchProcessingEnabled()) {
							System.out.println("a1");
							collectBatchProcessingInputForPerformCreate(list);
						} else {
							System.out.println("a2");
							newComp = performCreate(list);
						}
					if (newComp instanceof TCComponentItem) {
						int len = ((TCComponentItem) newComp)
								.getLatestItemRevision()
								.getReferenceListProperty("IMAN_specification").length;
						System.out.println("len =" + len);
					}
				} else if (useSOA()) {
					System.out.println("bbbbbbbbbbbbb");
					DataManagementService datamanagementservice = DataManagementService
							.getService(getSession());
					com.teamcenter.services.rac.core._2006_03.DataManagement.CreateItemsResponse createitemsresponse = null;
					com.teamcenter.services.rac.core._2006_03.DataManagement.ItemProperties aitemproperties[] = new com.teamcenter.services.rac.core._2006_03.DataManagement.ItemProperties[1];
					aitemproperties[0] = new com.teamcenter.services.rac.core._2006_03.DataManagement.ItemProperties();
					aitemproperties[0].clientId = Integer.toString(1);
					aitemproperties[0].description = itemDesc;
					aitemproperties[0].itemId = itemId;
					aitemproperties[0].name = itemName;
					aitemproperties[0].revId = revId;
					aitemproperties[0].type = itemType;
					aitemproperties[0].uom = uomComp == null ? "" : uomComp
							.toString();
					setFormAttrs();
					ArrayList arraylist = new ArrayList();
					if (imExtendedAttributes != null) {
						com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes extendedattributes2 = new com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes();
						String as2[] = dialog.dialogPanel.formTypes;
						if (as2 != null && as2[0] != null)
							extendedattributes2.objectType = as2[0];
						else
							extendedattributes2.objectType = "ItemMaster";
						extendedattributes2.attributes = parseAttributeValue(imExtendedAttributes);
						arraylist.add(extendedattributes2);
					}
					if (irmExtendedAttributes != null) {
						com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes extendedattributes3 = new com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes();
						extendedattributes3 = new com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes();
						String as3[] = dialog.dialogPanel.formTypes;
						if (as3 != null && as3[1] != null)
							extendedattributes3.objectType = as3[1];
						else
							extendedattributes3.objectType = "ItemRevision Master";
						extendedattributes3.attributes = parseAttributeValue(irmExtendedAttributes);
						arraylist.add(extendedattributes3);
					}
					if (arraylist.size() != 0)
						aitemproperties[0].extendedAttributes = (com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes[]) arraylist
								.toArray(aitemproperties[0].extendedAttributes);
					TCComponent tccomponent1 = null;
					String s1 = "";
					createitemsresponse = datamanagementservice.createItems(
							aitemproperties, tccomponent1, s1);
					if (createitemsresponse.serviceData.sizeOfPartialErrors() == 0) {
						newComp = createitemsresponse.output[0].item;
					} else {
						successFlag = false;
						SoaUtil.checkPartialErrors(createitemsresponse.serviceData);
					}
				} else {
					System.out.println("cccccccccccccccc");
					AbstractTCForm abstracttcform = dialog
							.getItemMasterFormPanel();
					AbstractRendering abstractrendering = dialog
							.getItemMasterFormStyleSheetPanel();
					TCComponentForm tccomponentform = null;
					if (abstracttcform != null)
						tccomponentform = abstracttcform.getFormComponent();
					else if (abstractrendering != null)
						tccomponentform = (TCComponentForm) abstractrendering
								.getComponent();
					AbstractTCForm abstracttcform1 = dialog
							.getItemRevMasterFormPanel();
					AbstractRendering abstractrendering1 = dialog
							.getItemRevMasterFormStyleSheetPanel();
					TCComponentForm tccomponentform2 = null;
					if (abstracttcform1 != null)
						tccomponentform2 = abstracttcform1.getFormComponent();
					else if (abstractrendering1 != null)
						tccomponentform2 = (TCComponentForm) abstractrendering1
								.getComponent();
					TCComponent tccomponent2 = null;
					if (uomComp instanceof String) {
						if (!((String) uomComp).isEmpty()) {
							TCComponentUnitOfMeasureType tccomponentunitofmeasuretype = (TCComponentUnitOfMeasureType) getSession()
									.getTypeComponent("UnitOfMeasure");
							TCComponent atccomponent[] = tccomponentunitofmeasuretype
									.extent();
							if (atccomponent != null && atccomponent.length > 0) {
								for (int j = 0; j < atccomponent.length; j++) {
									if (!atccomponent[j].toString().equals(
											uomComp))
										continue;
									tccomponent2 = atccomponent[j];
									break;
								}

							}
						}
					} else {
						tccomponent2 = (TCComponent) uomComp;
					}
					TCComponentItemType tccomponentitemtype = (TCComponentItemType) getSession()
							.getTypeComponent(itemType);
					newComp = tccomponentitemtype.create(itemId, revId,
							itemType, itemName, itemDesc, tccomponent2,
							tccomponentform, tccomponentform2);
				}
				if (isCI && newComp != null)
					newComp.setLogicalProperty("is_configuration_item", true);
			} else {
				System.out.println("dddddddddddd");
				setFormAttrs();
				newComp = null;
				com.teamcenter.rac.kernel.TCComponentBOMLineType.BOMLineCreateVIHelperObject bomlinecreatevihelperobject = new com.teamcenter.rac.kernel.TCComponentBOMLineType.BOMLineCreateVIHelperObject();
				bomlinecreatevihelperobject.clientId = Integer.toString(1);
				bomlinecreatevihelperobject.genericBomLine = dialog.viDialog
						.getBOMLine();
				bomlinecreatevihelperobject.itemDesc = itemDesc;
				bomlinecreatevihelperobject.itemId = itemId;
				bomlinecreatevihelperobject.itemName = itemName;
				bomlinecreatevihelperobject.revisionId = revId;
				bomlinecreatevihelperobject.itemType = itemType;
				bomlinecreatevihelperobject.uomComponent = (TCComponent) uomComp;
				bomlinecreatevihelperobject.extAttribs = new ArrayList();
				if (imExtendedAttributes != null) {
					com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes extendedattributes = new com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes();
					String as[] = dialog.dialogPanel.formTypes;
					if (as != null && as[0] != null)
						extendedattributes.objectType = as[0];
					else
						extendedattributes.objectType = "ItemMaster";
					extendedattributes.attributes = parseAttributeValue(imExtendedAttributes);
					bomlinecreatevihelperobject.extAttribs
							.add(extendedattributes);
				}
				if (irmExtendedAttributes != null) {
					com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes extendedattributes1 = new com.teamcenter.services.rac.core._2006_03.DataManagement.ExtendedAttributes();
					String as1[] = dialog.dialogPanel.formTypes;
					if (as1 != null && as1[1] != null)
						extendedattributes1.objectType = as1[1];
					else
						extendedattributes1.objectType = "ItemRevision Master";
					extendedattributes1.attributes = parseAttributeValue(irmExtendedAttributes);
					bomlinecreatevihelperobject.extAttribs
							.add(extendedattributes1);
				}
				TCComponentItemRevision tccomponentitemrevision = dialog.viDialog
						.getBOMLine().createVi(bomlinecreatevihelperobject);
				if (tccomponentitemrevision != null)
					newComp = tccomponentitemrevision.getItem();
				if (itemDesc != null && !itemDesc.isEmpty()) {
					newComp.setStringProperty("object_desc", itemDesc);
					tccomponentitemrevision.setStringProperty("object_desc",
							itemDesc);
					newComp.setStringProperty("object_desc", itemDesc);
				}
				TCComponent tccomponent = (TCComponent) dialog.getUOMComp();
				Assert.isNotNull(newComp);
				if (tccomponent != null)
					newComp.setReferenceProperty("uom_tag", tccomponent);
				TCComponentForm tccomponentform1 = (TCComponentForm) newComp
						.getRelatedComponent("IMAN_master_form");
				AbstractTCForm abstracttcform2 = dialog
						.getItemMasterFormPanel();
				AbstractRendering abstractrendering2 = dialog
						.getItemMasterFormStyleSheetPanel();
				Object obj1 = null;
				Object aobj1[] = null;
				if (abstracttcform2 != null) {
					TCComponentForm tccomponentform3 = abstracttcform2
							.getFormComponent();
					aobj1 = tccomponentform3.getAllFormProperties();
				} else if (abstractrendering2 != null) {
					TCComponentForm tccomponentform4 = (TCComponentForm) abstractrendering2
							.getComponent();
					Map map = abstractrendering2.getRenderingModified();
					if (map != null && !map.isEmpty()) {
						Object aobj2[] = map.values().toArray();
						aobj1 = new TCProperty[aobj2.length];
						System.arraycopy(((Object) (aobj2)), 0,
								((Object) (aobj1)), 0, aobj2.length);
					}
				}
				if (tccomponentform1 != null)
					tccomponentform1.setTCProperties(((TCProperty[]) (aobj1)));
				TCComponentForm tccomponentform5 = null;
				Assert.isNotNull(tccomponentitemrevision);
				AIFComponentContext aaifcomponentcontext[] = tccomponentitemrevision
						.getRelated("IMAN_master_form_rev");
				if (aaifcomponentcontext != null
						&& aaifcomponentcontext.length > 0)
					tccomponentform5 = (TCComponentForm) aaifcomponentcontext[0]
							.getComponent();
				AbstractTCForm abstracttcform3 = dialog
						.getItemRevMasterFormPanel();
				AbstractRendering abstractrendering3 = dialog
						.getItemRevMasterFormStyleSheetPanel();
				if (abstracttcform3 != null)
					aobj1 = abstracttcform3.getFormComponent()
							.getFormTCProperties();
				else if (abstractrendering3 != null) {
					Map map1 = abstractrendering3.getRenderingModified();
					if (map1 != null && !map1.isEmpty()) {
						Object aobj3[] = map1.values().toArray();
						aobj1 = new TCProperty[aobj3.length];
						System.arraycopy(((Object) (aobj3)), 0,
								((Object) (aobj1)), 0, aobj3.length);
					}
				}
				if (tccomponentform5 != null)
					tccomponentform5.setTCProperties(((TCProperty[]) (aobj1)));
			}
		} catch (TCException tcexception) {
			if (tcexception.getErrorCode() == 48043) {
				AbstractNewItemPanel abstractnewitempanel = dialog.dialogPanel;
				abstractnewitempanel.setPanel(abstractnewitempanel
						.getItemInfoStep());
				MessageBox.post(dialog, registry.getString("sameAsOldId"),
						registry.getString("error.TITLE"), 4);
			} else if (tcexception.getCause() instanceof NumberFormatException) {
				AbstractNewItemPanel abstractnewitempanel1 = dialog.dialogPanel;
				int i = getErrorPanel(list);
				String s = getErrorProp(list);
				if (i > 0)
					abstractnewitempanel1.setPanel(i);
				Object aobj[] = { s };
				String s2 = MessageFormat.format(
						registry.getString("formatException"), aobj);
				MessageBox.post(dialog, s2, tcexception,
						registry.getString("error.TITLE"), 4);
			} else {
				MessageBox.post(dialog, tcexception);
			}
			successFlag = false;
		}
		if (newComp != null)
			addToCache(newComp);
	}

	protected void collectBatchProcessingInputForPerformCreate(List list) {
		if (batchProcessingInputsForPerformCreate == null)
			batchProcessingInputsForPerformCreate = new ArrayList();
		ArrayList arraylist = new ArrayList(list);
		batchProcessingInputsForPerformCreate.add(arraylist);
	}

	protected void finishBatchProcessing() {
		newComp = null;
		if (batchProcessingInputsForPerformCreate == null
				|| batchProcessingInputsForPerformCreate.isEmpty())

			try {
				TCComponent atccomponent[] = performBatchCreate(batchProcessingInputsForPerformCreate);
				if (atccomponent != null) {
					TCComponent atccomponent1[];
					int j = (atccomponent1 = atccomponent).length;
					for (int i = 0; i < j; i++) {
						TCComponent tccomponent = atccomponent1[i];
						addToCache(tccomponent);
						if (isCI)
							tccomponent.setLogicalProperty(
									"is_configuration_item", true);
					}

				}

			} catch (TCException tcexception) {
				Registry registry = Registry.getRegistry(this);
				if (tcexception.getErrorCode() == 48043) {
					AbstractNewItemPanel abstractnewitempanel = dialog.dialogPanel;
					abstractnewitempanel.setPanel(abstractnewitempanel
							.getItemInfoStep());
					MessageBox.post(dialog, registry.getString("sameAsOldId"),
							registry.getString("error.TITLE"), 4);
				} else if (tcexception.getCause() instanceof NumberFormatException) {
					AbstractNewItemPanel abstractnewitempanel1 = dialog.dialogPanel;
					List list = null;
					int k = getErrorPanel(list);
					String s = getErrorProp(list);
					if (k > 0)
						abstractnewitempanel1.setPanel(k);
					Object aobj[] = { s };
					String s1 = MessageFormat.format(
							registry.getString("formatException"), aobj);
					MessageBox.post(dialog, s1, tcexception,
							registry.getString("error.TITLE"), 4);
				} else {
					MessageBox.post(dialog, tcexception);
				}
				successFlag = false;
			}
		batchProcessingInputsForPerformCreate = null;

	}

	protected TCComponent[] performBatchCreate(List list) throws TCException {
		logger.error((new StringBuilder("Internal error: "))
				.append(NewItemOperation.class).append(".performBatchCreate")
				.append(" called for class ").append(getClass())
				.append(": must be overridden to support batck processing")
				.toString());
		return null;
	}

	public void addToCache(TCComponent tccomponent) {
		if (tccomponent != null) {
			if (newComps == null)
				newComps = new ArrayList(0);
			if (!newComps.contains(tccomponent))
				newComps.add(tccomponent);
		}
	}

	protected boolean useSOA() {
		if (dialog.getDialogPanel().isSOASupported()) {
			AbstractRendering abstractrendering = dialog
					.getItemMasterFormStyleSheetPanel();
			if (abstractrendering != null) {
				boolean flag = abstractrendering.isRenderingModified();
				if (!flag && !(abstractrendering instanceof AutomaticRendering)) {
					useSOA = false;
					return useSOA;
				}
			}
			AbstractRendering abstractrendering1 = dialog
					.getItemRevMasterFormStyleSheetPanel();
			if (abstractrendering1 != null) {
				boolean flag1 = abstractrendering1.isRenderingModified();
				if (!flag1
						&& !(abstractrendering1 instanceof AutomaticRendering)) {
					useSOA = false;
					return useSOA;
				}
			}
			useSOA = true;
			return useSOA;
		} else {
			useSOA = false;
			return useSOA;
		}
	}

	public void checkOutItemRev() throws TCException {
		TCComponentItemRevision tccomponentitemrevision;
		if (Debug.isOn("NewItem"))
			System.out.println("NewItemOperation::checkOutItemRev()");
		if (!dialog.getCheckOutOnCreateFlag() || newComp == null)

			setCheckOutOnCreateFlag(true);
		Object obj = null;
		tccomponentitemrevision = null;
		if (newComp instanceof TCComponentItem) {
			TCComponentItem tccomponentitem = (TCComponentItem) newComp;
			tccomponentitemrevision = tccomponentitem.getLatestItemRevision();
		} else if (newComp instanceof TCComponentItemRevision)
			tccomponentitemrevision = (TCComponentItemRevision) newComp;
		boolean flag = false;
		if (tccomponentitemrevision == null)

			flag = tccomponentitemrevision.getLogicalProperty("is_IRDC");
		if (!flag) {
			TCReservationService tcreservationservice = getSession()
					.getReservationService();
			tcreservationservice.reserve(tccomponentitemrevision);
			return;
		}
		try {
			if (checkOutIRDCItemRev(tccomponentitemrevision, flag)) {
				DownloadDatasetDialog downloaddatasetdialog = new DownloadDatasetDialog(
						dialog, newComp, isCheckOutOnCreateFlag());
				downloaddatasetdialog.setVisible(true);
			}
		} catch (Exception exception) {
			MessageBox.post(desktop, exception);
		}
		// break MISSING_BLOCK_LABEL_176;
		setCheckOutOnCreateFlag(false);
	}

	protected boolean checkOutIRDCItemRev(
			TCComponentItemRevision tccomponentitemrevision, boolean flag)
			throws TCException {
		if (flag
				&& dialog.dialogPanel.itemAttachFilesPanel != null
				&& dialog.dialogPanel.itemAttachFilesPanel.filesToAttach != null
				&& !dialog.dialogPanel.itemAttachFilesPanel.filesToAttach
						.isEmpty()) {
			setCheckOutOnCreateFlag(false);
			return false;
		}
		if (flag
				&& dialog.dialogPanel.submitToWorkflowPanel != null
				&& dialog.dialogPanel.submitToWorkflowPanel.templateList != null
				&& dialog.dialogPanel.submitToWorkflowPanel.templateList
						.getTextField().getText() != null
				&& !dialog.dialogPanel.submitToWorkflowPanel.templateList
						.getTextField().getText().isEmpty()) {
			setCheckOutOnCreateFlag(false);
			return false;
		}
		String s;
		TCReservationService tcreservationservice = getSession()
				.getReservationService();
		tcreservationservice.reserve(tccomponentitemrevision);
		s = tccomponentitemrevision.getTCProperty("fms_tickets").toString();
		if (s != null && !s.isEmpty())
			return true;
		// break MISSING_BLOCK_LABEL_198;
		// TCException tcexception;

		// MessageBox.post(desktop, tcexception);
		return false;
	}

	public void pasteNewComponent() throws Exception {
		logger.debug("NewItemOperation::pasteNewComponent()");
		Registry registry = Registry.getRegistry(this);
		setStatus((new StringBuilder(String.valueOf(registry
				.getString("pastingNewItem")))).append(" ").append(itemName)
				.append(" ...").toString());
		if (cmd != null)
			pasteTargets = cmd.getTargets();
		else if (dialog != null)
			pasteTargets = dialog.getTargets();
		if (pasteTargets != null
				&& pasteTargets.length == 1
				&& (pasteTargets[0] instanceof TCComponentPseudoFolder)
				&& (((TCComponentPseudoFolder) pasteTargets[0])
						.getOwningComponent() instanceof TCComponentItemRevision)
				&& ((TCComponentPseudoFolder) pasteTargets[0])
						.getOwningComponent().getType()
						.equals("EngChange Revision")) {
			TCComponent atccomponent1[] = { ((TCComponentItem) newComp)
					.getLatestItemRevision() };
			PasteCommand pastecommand1 = (PasteCommand) registry
					.newInstanceForEx("pasteCommand", new Object[] {
							atccomponent1, pasteTargets, desktop });
			pastecommand1.setFailBackFlag(true);
			pastecommand1.addPropertyChangeListener(this);
			pastecommand1.executeModal();
			pasteTargets = null;
		}
		TCComponent atccomponent[];
		if (m_pasteRevisionFlag)
			atccomponent = (new TCComponent[] { ((TCComponentItem) newComp)
					.getLatestItemRevision() });
		else
			atccomponent = (new TCComponent[] { newComp });
		PasteCommand pastecommand;
		if (m_pasteRelation != null)
			pastecommand = (PasteCommand) registry.newInstanceForEx(
					"pasteCommand", new Object[] { atccomponent, pasteTargets,
							m_pasteRelation, desktop });
		else
			pastecommand = (PasteCommand) registry.newInstanceForEx(
					"pasteCommand", new Object[] { atccomponent, pasteTargets,
							desktop });
		pastecommand.setFailBackFlag(true);
		pastecommand.addPropertyChangeListener(this);
		pastecommand.addCommandListener(new IC_PasteCommandListener());
		pastecommand.executeModal();
	}

	public void pasteNewComponents() throws Exception {
		for (int i = 0; i < newComps.size(); i++) {
			newComp = (TCComponent) newComps.get(i);
			pasteNewComponent();
		}

	}

	public void setFormAttrs() throws Exception {
		logger.debug("NewItemOperation::setFormAttrs()");
		setItemMasterFormAttrs();
		setItemRevMasterFormAttrs();
	}

	public void setItemMasterFormAttrs() throws Exception {
		if (dialog == null)
			return;
		AbstractTCForm abstracttcform = dialog.getItemMasterFormPanel();
		AbstractRendering abstractrendering = dialog
				.getItemMasterFormStyleSheetPanel();
		if (abstracttcform != null) {
			logger.debug("NewItemOperation::setIMFormAttrs(): save item master form.");
			abstracttcform.saveForm();
		} else if (abstractrendering != null) {
			logger.debug("NewItemOperation::setIMFormAttrs(): save item master form style sheet.");
			if (!useSOA) {
				abstractrendering.saveRendering();
				ItemMasterFormPanel itemmasterformpanel = dialog.dialogPanel.itemMasterPanel;
				if (itemmasterformpanel != null
						&& itemmasterformpanel.getAdditionalPropsPage() != null
						&& (newComp instanceof TCComponentItem))
					itemmasterformpanel.getAdditionalPropsPage().saveInputs(
							newComp);
			} else {
				imExtendedAttributes = abstractrendering.getRenderingModified();
			}
		}
	}

	public void setItemRevMasterFormAttrs() throws Exception {
		if (dialog == null)
			return;
		AbstractTCForm abstracttcform = dialog.getItemRevMasterFormPanel();
		AbstractRendering abstractrendering = dialog
				.getItemRevMasterFormStyleSheetPanel();
		if (abstracttcform != null) {
			logger.debug("NewItemOperation::setIRMFormAttrs(): save item rev master form.");
			abstracttcform.saveForm();
		} else if (abstractrendering != null) {
			logger.debug("NewItemOperation::setIRMFormAttrs(): save item rev master form style sheet.");
			if (!useSOA) {
				abstractrendering.saveRendering();
				ItemRevMasterFormPanel itemrevmasterformpanel = dialog.dialogPanel.itemRevMasterPanel;
				TCComponentItemRevision tccomponentitemrevision;
				if (newComp instanceof TCComponentItem)
					tccomponentitemrevision = ((TCComponentItem) newComp)
							.getLatestItemRevision();
				else
					tccomponentitemrevision = (TCComponentItemRevision) newComp;
				if (itemrevmasterformpanel != null
						&& itemrevmasterformpanel.getAdditionalPropsPage() != null)
					itemrevmasterformpanel.getAdditionalPropsPage().saveInputs(
							tccomponentitemrevision);
			} else {
				irmExtendedAttributes = abstractrendering
						.getRenderingModified();
			}
		}
	}

	public void copyFormAttributes(boolean flag) throws Exception {
		LinkedList linkedlist = new LinkedList();
		AbstractRendering abstractrendering = null;
		TCComponentForm tccomponentform = null;
		if (flag) {
			abstractrendering = dialog.getItemMasterFormStyleSheetPanel();
			TCComponentItem tccomponentitem = ((TCComponentItemRevision) pasteTargets[0])
					.getItem();
			tccomponentform = (TCComponentForm) tccomponentitem
					.getRelatedComponent("IMAN_master_form");
		} else {
			abstractrendering = dialog.getItemRevMasterFormStyleSheetPanel();
			tccomponentform = (TCComponentForm) ((TCComponentItemRevision) pasteTargets[0])
					.getRelatedComponent("IMAN_master_form_rev");
		}
		if (abstractrendering == null
				|| abstractrendering.getComponent() == null
				|| tccomponentform == null)
			return;
		TCComponentForm tccomponentform1 = (TCComponentForm) abstractrendering
				.getComponent();
		TCProperty atcproperty[] = tccomponentform1.getFormTCProperties();
		for (int i = 0; i < atcproperty.length; i++) {
			Object obj = null;
			if (!atcproperty[i].isEnabled()) {
				TCProperty tcproperty;
				if (atcproperty[i].isNotArray())
					tcproperty = setPropData(atcproperty[i], tccomponentform
							.getTCProperty(atcproperty[i].getPropertyName())
							.getPropertyData());
				else
					tcproperty = setPropData(atcproperty[i], tccomponentform
							.getTCProperty(atcproperty[i].getPropertyName())
							.getPropertyValue());
				if (tcproperty != null
						&& atcproperty[i].isModifiable()
						&& (!atcproperty[i].isEnabled() || !atcproperty[i]
								.isDisplayable()))
					linkedlist.add(atcproperty[i]);
			}
		}

		TCProperty atcproperty1[] = (TCProperty[]) linkedlist
				.toArray(new TCProperty[linkedlist.size()]);
		tccomponentform1.setTCProperties(atcproperty1);
	}

	private TCProperty setPropData(TCProperty tcproperty, Object obj)
			throws Exception {
		Object obj1 = null;
		switch (tcproperty.getPropertyType()) {
		case 5: // '\005'
			if (obj instanceof Integer) {
				obj1 = obj;
			} else {
				if (obj instanceof int[]) {
					tcproperty.setIntValueArrayData((int[]) obj);
					return tcproperty;
				}
				if (obj != null) {
					String s = obj.toString().trim();
					if (!s.isEmpty())
						obj1 = Integer.getInteger(s);
				}
			}
			if (obj1 != null) {
				tcproperty.setIntValueData(((Integer) obj1).intValue());
				return tcproperty;
			} else {
				return null;
			}

		case 3: // '\003'
			if (obj instanceof Double) {
				obj1 = obj;
			} else {
				if (obj instanceof double[]) {
					tcproperty.setDoubleValueArrayData((double[]) obj);
					return tcproperty;
				}
				if (obj != null) {
					String s1 = obj.toString().trim();
					if (!s1.isEmpty())
						obj1 = new Double(s1);
				}
			}
			if (obj1 != null) {
				tcproperty.setDoubleValueData(((Double) obj1).doubleValue());
				return tcproperty;
			} else {
				return null;
			}

		case 8: // '\b'
			if (obj instanceof String) {
				obj1 = obj;
			} else {
				if (obj instanceof String[]) {
					tcproperty.setStringValueArrayData((String[]) obj);
					return tcproperty;
				}
				if (obj != null)
					obj1 = obj.toString();
			}
			if (obj1 != null) {
				tcproperty.setStringValueData((String) obj1);
				return tcproperty;
			} else {
				return null;
			}

		case 12: // '\f'
			if (obj instanceof String) {
				obj1 = obj;
			} else {
				if (obj instanceof String[]) {
					tcproperty.setNoteValueArrayData((String[]) obj);
					return tcproperty;
				}
				if (obj != null)
					obj1 = obj.toString();
			}
			if (obj1 != null) {
				tcproperty.setNoteValueData((String) obj1);
				return tcproperty;
			} else {
				return null;
			}

		case 6: // '\006'
			if (obj instanceof Boolean) {
				obj1 = obj;
			} else {
				if (obj instanceof boolean[]) {
					tcproperty.setLogicalValueArrayData((boolean[]) obj);
					return tcproperty;
				}
				if (obj != null) {
					String s2 = obj.toString().trim();
					if (!s2.isEmpty())
						obj1 = Boolean.valueOf(s2);
				}
			}
			if (obj1 != null) {
				tcproperty.setLogicalValueData(((Boolean) obj1).booleanValue());
				return tcproperty;
			} else {
				return null;
			}

		case 4: // '\004'
			if (obj instanceof Float) {
				obj1 = obj;
			} else {
				if (obj instanceof float[]) {
					tcproperty.setFloatValueArrayData((float[]) obj);
					return tcproperty;
				}
				if (obj != null) {
					String s3 = obj.toString().trim();
					if (!s3.isEmpty())
						obj1 = new Float(s3);
				}
			}
			if (obj1 != null) {
				tcproperty.setFloatValueData(((Float) obj1).floatValue());
				return tcproperty;
			} else {
				return null;
			}

		case 2: // '\002'
			if (obj instanceof Date) {
				obj1 = obj;
			} else {
				if (obj instanceof Date[]) {
					tcproperty.setDateValueArrayData((Date[]) obj);
					return tcproperty;
				}
				if (obj != null) {
					String s4 = obj.toString().trim();
					if (!s4.isEmpty())
						obj1 = TCDateEncoder.getDate(s4);
				}
			}
			if (obj1 != null) {
				tcproperty.setDateValueData((Date) obj1);
				return tcproperty;
			} else {
				return null;
			}

		case 1: // '\001'
			if (obj instanceof Character) {
				obj1 = obj;
			} else {
				if (obj instanceof char[]) {
					tcproperty.setCharValueArrayData((char[]) obj);
					return tcproperty;
				}
				if (obj != null) {
					String s5 = obj.toString().trim();
					if (!s5.isEmpty())
						obj1 = Character.valueOf(s5.charAt(0));
				}
			}
			if (obj1 != null) {
				tcproperty.setCharValueData(((Character) obj1).charValue());
				return tcproperty;
			} else {
				return null;
			}

		case 7: // '\007'
			if (obj instanceof Short) {
				obj1 = obj;
			} else {
				if (obj instanceof short[]) {
					tcproperty.setShortValueArrayData((short[]) obj);
					return tcproperty;
				}
				if (obj != null) {
					String s6 = obj.toString().trim();
					if (!s6.isEmpty())
						obj1 = Short.valueOf(s6);
				}
			}
			if (obj1 != null) {
				tcproperty.setShortValueData(((Short) obj1).shortValue());
				return tcproperty;
			} else {
				return null;
			}

		case 9: // '\t'
		case 10: // '\n'
		case 11: // '\013'
		case 13: // '\r'
		case 14: // '\016'
			if (obj instanceof TCComponent)
				obj1 = obj;
			else if (obj instanceof TCComponent[]) {
				tcproperty.setReferenceValueArrayData((TCComponent[]) obj);
				return tcproperty;
			}
			if (obj1 != null) {
				tcproperty.setReferenceValueData((TCComponent) obj1);
				return tcproperty;
			} else {
				return null;
			}
		}
		return null;
	}

	public void assignToProjects() throws Exception {
		logger.debug("NewItemOperation::assignToProjects()");
		try {
			if (projects != null && projects.length > 0) {
				TCComponentProjectType tccomponentprojecttype = (TCComponentProjectType) getSession()
						.getTypeComponent("TC_Project");
				tccomponentprojecttype.assignToProjects(projects, newComp);
			}
		} catch (TCException tcexception) {
			successFlag = false;
			MessageBox.post(dialog, tcexception);
		}
	}

	public void openNewComponent() throws Exception {
		logger.debug("NewItemOperation::openNewComponent()");
		Registry registry = Registry.getRegistry(this);
		OpenCommand opencommand = (OpenCommand) registry.newInstanceForEx(
				"openCommand", new Object[] { desktop, newComp });
		opencommand.executeModeless();
	}

	public void classifyNewComponent() throws Exception {
		if (classifyFlag && newComp != null) {
			if (classifyType.equalsIgnoreCase("ItemRevision")
					&& (newComp instanceof TCComponentItem)) {
				logger.debug("NewItemOperation::classifyNewComponent() - classify item rev");
				Object obj = null;
				try {
					InterfaceAIFComponent ainterfaceaifcomponent[] = { ((TCComponentItem) newComp)
							.getLatestItemRevision() };
					IC_SendToClassification ic_sendtoclassification = new IC_SendToClassification(
							ainterfaceaifcomponent);
					swingRunnables.add(ic_sendtoclassification);
				} catch (TCException tcexception) {
					logger.error(
							"Problem Occurred when trying to classify an ItemRevision",
							tcexception);
				}
			} else {
				logger.debug("NewItemOperation::classifyNewComponent() - classify");
				InterfaceAIFComponent ainterfaceaifcomponent1[] = { newComp };
				IC_SendToClassification ic_sendtoclassification1 = new IC_SendToClassification(
						ainterfaceaifcomponent1);
				swingRunnables.add(ic_sendtoclassification1);
			}
			if (dialog != null)
				dialog.dialogPanel.closeOnClassify();
		}
	}

	public void createNewIds() throws TCException {
		logger.debug("NewItemOperation::createNewIds()");
		TCComponentIdentifierType tccomponentidentifiertype = (TCComponentIdentifierType) getSession()
				.getTypeService().getTypeComponent("Identifier");
		TCComponentItem tccomponentitem;
		TCComponentItemRevision tccomponentitemrevision;
		if (newComp instanceof TCComponentItem) {
			tccomponentitem = (TCComponentItem) newComp;
			tccomponentitemrevision = tccomponentitem.getLatestItemRevision();
		} else {
			tccomponentitemrevision = (TCComponentItemRevision) newComp;
			tccomponentitem = tccomponentitemrevision.getItem();
		}
		newIds = tccomponentidentifiertype.createAltIdentifier(
				dialog.getIdType(), tccomponentitem, tccomponentitemrevision,
				dialog.getContext());
	}

	public void operationForAlternateIds() throws Exception {
		try {
			setAndSaveAltIds();
			TCComponentIdentifier atccomponentidentifier[];
			int k = (atccomponentidentifier = newIds).length;
			for (int i = 0; i < k; i++) {
				TCComponentIdentifier tccomponentidentifier = atccomponentidentifier[i];
				if (tccomponentidentifier != null)
					tccomponentidentifier.unlock();
			}

		} catch (TCException tcexception) {
			int j = dialog.dialogPanel.getAltIdInfoStep();
			AbstractControlWizardStepPanel abstractcontrolwizardsteppanel = (AbstractControlWizardStepPanel) dialog.dialogPanel.stepPanels
					.get(j);
			if (abstractcontrolwizardsteppanel instanceof NewItemAlternateIdInfoPanel) {
				NewItemAlternateIdInfoPanel newitemalternateidinfopanel = (NewItemAlternateIdInfoPanel) abstractcontrolwizardsteppanel;
				boolean flag = newitemalternateidinfopanel.isRequired(itemType);
				if (flag)
					successFlag = false;
				logger.debug(tcexception.getLocalizedMessage(), tcexception);
				Registry registry = Registry.getRegistry(this);
				MessageBox.post(dialog,
						registry.getString("failToCreateAltId"), tcexception,
						registry.getString("error.TITLE"), 1);
				if (flag)
					throw tcexception;
			}
			if (newIds != null)
				newIds[0].delete();
			return;
		}
		TCComponentItem tccomponentitem;
		TCComponentItemRevision tccomponentitemrevision;
		if (newComp instanceof TCComponentItem) {
			tccomponentitem = (TCComponentItem) newComp;
			tccomponentitemrevision = tccomponentitem.getLatestItemRevision();
		} else {
			tccomponentitemrevision = (TCComponentItemRevision) newComp;
			tccomponentitem = tccomponentitemrevision.getItem();
		}
		if (dialog.getIdDisplayFlag())
			tccomponentitem.setReferenceProperty("id_dispdefault", newIds[0]);
		if (dialog.getRevDisplayFlag())
			tccomponentitemrevision.setReferenceProperty("id_dispdefault",
					newIds[1]);
	}

	public void setAndSaveAltIds() throws Exception {
		logger.debug("NewItemOperation::setAndSaveAltIds()");
		if (newIds == null)
			createNewIds();
		int i = dialog.dialogPanel.getAltIdInfoStep();
		AbstractControlWizardStepPanel abstractcontrolwizardsteppanel = (AbstractControlWizardStepPanel) dialog.dialogPanel.stepPanels
				.get(i);
		if (abstractcontrolwizardsteppanel instanceof NewItemAlternateIdInfoPanel) {
			NewItemAlternateIdInfoPanel newitemalternateidinfopanel = (NewItemAlternateIdInfoPanel) abstractcontrolwizardsteppanel;
			newitemalternateidinfopanel.saveProperties(newIds[0], 0);
			int j = dialog.dialogPanel.getAdditionalAltIdInfoStep();
			AdditionalIdInfoPanel additionalidinfopanel = (AdditionalIdInfoPanel) dialog.dialogPanel.stepPanels
					.get(j);
			additionalidinfopanel.saveAdditionalProperty(newIds[0]);
			if (newIds[1] != null) {
				newitemalternateidinfopanel.saveProperties(newIds[1], 1);
				int k = dialog.dialogPanel.getAdditionalAltRevInfoStep();
				AdditionalRevInfoPanel additionalrevinfopanel = (AdditionalRevInfoPanel) dialog.dialogPanel.stepPanels
						.get(k);
				additionalrevinfopanel.saveAdditionalProperty(newIds[1]);
			}
		}
		if (newIds[1] != null)
			newIds[1].save();
		else
			newIds[0].save();
	}

	public boolean newInApp() {
		AbstractAIFUIApplication abstractaifuiapplication;
		abstractaifuiapplication = AIFDesktop.getActiveDesktop()
				.getCurrentApplication();
		if (abstractaifuiapplication == null
				|| abstractaifuiapplication.getApplicationId().indexOf(
						"PSEApplication") <= 0)
			return false;
		AbstractTCApplication abstracttcapplication;
		abstracttcapplication = (AbstractTCApplication) abstractaifuiapplication;
		if (abstracttcapplication.getBOMWindow() == null)
			return abstracttcapplication.open(newComp);
		try {
			if (pasteTargets == null || pasteTargets.length == 0)
				pasteTargets = abstracttcapplication.getTargetComponents();
		} catch (Exception exception) {
			Registry registry = Registry.getRegistry(this);
			String s = (new StringBuilder(String.valueOf(registry
					.getString("failToOpen")))).append(" ")
					.append(newComp.toString()).toString();
			MessageBox.post(dialog, s, exception,
					registry.getString("error.TITLE"), 1, true);
		}
		return false;
	}

	protected List getCreateInputs() {
		ArrayList arraylist = new ArrayList(0);
		int i = dialog.dialogPanel.getNumberOfSteps();
		for (int j = 0; j < i; j++) {
			Object obj = dialog.dialogPanel.getStepPanel(j);
			if (obj instanceof ICreateInputProvider) {
				logger.debug((new StringBuilder("STEP PANEL : "))
						.append(obj.getClass().getName())
						.append(" is a create input provider").toString());
				List list = ((ICreateInputProvider) obj).getCreateInputs();
				if (list != null && !list.isEmpty()) {
					((ICreateInputProvider) obj).setStoredCreateInputs(list);
					arraylist.addAll(list);
				}
			}
		}

		return arraylist;
	}

	protected int getErrorPanel(List list) {
		int i = -1;
		int j = dialog.dialogPanel.getNumberOfSteps();
		for (int k = 0; k < j; k++) {
			Object obj = dialog.dialogPanel.getStepPanel(k);
			if (obj instanceof ICreateInputProvider) {
				List list1 = ((ICreateInputProvider) obj)
						.getStoredCreateInputs();
				if (list1 != null && !list1.isEmpty()) {
					for (Iterator iterator = list1.iterator(); iterator
							.hasNext();) {
						ICreateInstanceInput icreateinstanceinput = (ICreateInstanceInput) iterator
								.next();
						if (icreateinstanceinput.getErrorCreateInput()) {
							i = k;
							return i;
						}
					}

				}
			}
		}

		return i;
	}

	protected String getErrorProp(List list) {
		String s = null;
		int i = dialog.dialogPanel.getNumberOfSteps();
		for (int j = 0; j < i; j++) {
			Object obj = dialog.dialogPanel.getStepPanel(j);
			if (obj instanceof ICreateInputProvider) {
				List list1 = ((ICreateInputProvider) obj)
						.getStoredCreateInputs();
				if (list1 != null && !list1.isEmpty()) {
					for (Iterator iterator = list1.iterator(); iterator
							.hasNext();) {
						ICreateInstanceInput icreateinstanceinput = (ICreateInstanceInput) iterator
								.next();
						if (icreateinstanceinput.getErrorCreateInput())
							return s = icreateinstanceinput.getErrorPropName();
					}

				}
			}
		}

		return s;
	}

	protected TCComponent performCreate(List list) throws TCException {
		TCComponent tccomponent = null;
		List list1 = SOAGenericCreateHelper.create(getSession(),
				dialog.dialogPanel.getCreateDefinition(), list);
		if (list1 != null && !list1.isEmpty()) {
			logger.debug((new StringBuilder("Number of created components : "))
					.append(list1.size()).toString());
			for (Iterator iterator = list1.iterator(); iterator.hasNext();) {
				TCComponent tccomponent1 = (TCComponent) iterator.next();
				logger.debug((new StringBuilder("Component : "))
						.append(tccomponent1.toString()).append(" - Type : ")
						.append(tccomponent1.getType()).toString());
				if (tccomponent1 instanceof TCComponentItem) {
					tccomponent = tccomponent1;
					break;
				}
			}

		}
		return tccomponent;
	}

	private Map parseAttributeValue(Map map) {
		HashMap hashmap = new HashMap();
		Iterator iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
			String s = null;
			if (entry.getValue() instanceof TCProperty) {
				TCProperty tcproperty = (TCProperty) entry.getValue();
				String as[] = SoaUtil.marshallTCProperty(tcproperty);
				if (tcproperty.getPropertyType() != 8 ? as == null
						|| as[0] == null || as[0].length() == 0 : as == null
						|| as[0] == null)
					continue;
				StringBuilder stringbuilder = new StringBuilder();
				for (int i = 0; i < as.length; i++) {
					stringbuilder.append(as[i]);
					if (i + 1 != as.length)
						stringbuilder.append("::");
				}

				s = stringbuilder.toString();
			}
			System.out.println((String) entry.getKey() + "<=====>" + s);
			hashmap.put((String) entry.getKey(), s);
		}
		return hashmap;
	}

	public boolean isCheckOutOnCreateFlag() {
		return checkOutOnCreateFlag;
	}

	public void setCheckOutOnCreateFlag(boolean flag) {
		checkOutOnCreateFlag = flag;
	}

	private void evaluateDialogsClosed() {
		if (newItemDialogClosed && pasteComplete && swingRunnables != null
				&& swingRunnables.size() != 0) {
			Runnable runnable;
			for (Iterator iterator = swingRunnables.iterator(); iterator
					.hasNext(); SwingUtilities.invokeLater(runnable))
				runnable = (Runnable) iterator.next();

		}
	}

	public CreationResult createRequest(String s, String s1, int i,
			TCComponent atccomponent[], TCComponent atccomponent1[], String s2,
			int j, String s3, String s4, Map map) throws Exception {
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
			ISessionService isessionservice = Activator.getDefault()
					.getSessionService();
			TCSession tcsession = (TCSession) isessionservice
					.getSession(TCSession.class.getName());
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
								StringBuffer stringbuffer = new StringBuffer();
								if (i1 == 260044) {
									stringbuffer.append("");
								} else {
									String as1[];
									int k1 = (as1 = as).length;
									for (int j1 = 0; j1 < k1; j1++) {
										String s5 = as1[j1];
										stringbuffer.append(s5);
										stringbuffer.append('\n');
									}

								}
								throw new Exception(stringbuffer.toString());
							}

					}
				}
			}
			creationresult = new CreationResult(
					createdispatcherrequestresponse.requestsCreated[0], as);
		} catch (Exception exception) {
			throw exception;
		}
		return creationresult;
	}

	private static final Logger logger;
	protected String itemName;
	protected String itemId;
	protected List itemIds;
	protected String revId;
	protected String itemDesc;
	protected String itemType;
	protected Object uomComp;
	protected boolean openOnCreateFlag;
	protected boolean checkOutOnCreateFlag;
	protected boolean classifyFlag;
	protected boolean isCI;
	protected String classifyType;
	protected InterfaceAIFComponent pasteTargets[];
	protected TCComponent newComp;
	protected ArrayList newComps;
	protected AIFDesktop desktop;
	public boolean successFlag;
	public ProgressIndicator processIndicator;
	public NewItemDialog dialog;
	public NewItemCommand cmd;
	public TCComponentIdentifier newIds[];
	public int itemsQuantity;
	protected TCComponentProject projects[];
	protected Map imExtendedAttributes;
	protected Map irmExtendedAttributes;
	protected boolean useSOA;
	protected static final int ITEM_DUPLICATE_ID_ERROR = 48043;
	private List swingRunnables;
	private static final String TS_SERVICE_TYPE = "CREATEITEM_RENDER";
	private boolean newItemDialogClosed;
	private static final String TS_SERVICE_VAR = "RenderTSServiceName";
	protected boolean pasteComplete;
	private static final String TS_PROVIDER_VAR = "RenderProviderName";
	private static final String TYPE_NAME = "ItemRevision";
	private static final String DISP_REQ_ARG = "ETSUpdOvr";
	private static final String DISP_REQ_ARG_FALSE_VAL = "false";
	private static final String RAC_IRDC_ATTACHE_FILE_KEY_VAL = "RAC_IRDC_ATTACH_FILE";
	private static final int IRDC_NOT_REQUIRED_RENDER_ERR_NUM = 260044;
	private String m_pasteRelation;
	private boolean m_pasteRevisionFlag;
	private boolean m_batchProcessingEnabled;
	protected List batchProcessingInputsForPerformCreate;

	static {
		logger = Logger.getLogger(NewItemOperation.class);
		if (!logger.isDebugEnabled() && Debug.isOn("NewItem"))
			logger.setLevel(Level.DEBUG);
	}

}

/*
 * DECOMPILATION REPORT
 * 
 * Decompiled from:
 * F:\Teamcenter11ENV\JAVA\rac\plugins\com.teamcenter.rac.common_11000.2.0.jar
 * Total time: 153 ms Jad reported messages/errors: Couldn't fully decompile
 * method processPostCreate Couldn't resolve all exception handlers in method
 * processPostCreate Couldn't resolve all exception handlers in method
 * processRender Couldn't fully decompile method finishBatchProcessing Couldn't
 * resolve all exception handlers in method finishBatchProcessing Couldn't
 * resolve all exception handlers in method checkOutItemRev Couldn't fully
 * decompile method checkOutIRDCItemRev Couldn't resolve all exception handlers
 * in method checkOutIRDCItemRev Couldn't resolve all exception handlers in
 * method newInApp Exit status: 0 Caught exceptions:
 */