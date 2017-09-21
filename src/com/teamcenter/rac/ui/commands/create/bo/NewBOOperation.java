// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 

package com.teamcenter.rac.ui.commands.create.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.aif.ICommandListener;
import com.teamcenter.rac.aif.common.AIFDataBean;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.common.create.IBOCreateDefinition;
import com.teamcenter.rac.common.create.SOAGenericCreateHelper;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentTask;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.commands.Messages;
import com.teamcenter.rac.ui.commands.RACUICommandsActivator;
import com.teamcenter.rac.ui.commands.genericAttach.AttachFileOperation;
import com.teamcenter.rac.ui.commands.paste.PasteDataBean;
import com.teamcenter.rac.ui.commands.paste.PasteJob;
import com.teamcenter.rac.util.AdapterUtil;
import com.teamcenter.rac.util.PlatformHelper;
import com.teamcenter.rac.util.Registry;
import com.teamcenter.rac.util.SWTUIUtilities;
import com.teamcenter.rac.util.event.ClientEventDispatcher;

// Referenced classes of package com.teamcenter.rac.ui.commands.create.bo:
//            NewBOWizard, NewBOModel

public class NewBOOperation extends AbstractAIFOperation {

	public NewBOOperation() {
		super(Messages.getString("newBOOperation.NAME"));
		m_wizardId = "";
		successFlag = true;
		try {
			session = (TCSession) RACUICommandsActivator.getDefault()
					.getSession();
		} catch (Exception exception) {
			Logger.getLogger(NewBOOperation.class).error(
					exception.getLocalizedMessage(), exception);
		}
	}

	public NewBOOperation(NewBOWizard newbowizard, List list) {
		this();
		setWizard(newbowizard);
		setCreateInput(list);
	}

	public void setWizard(NewBOWizard newbowizard) {
		wizard = newbowizard;
		if (wizard != null) {
			setModel(wizard.getBOModel());
			m_wizardId = wizard.getWizardId();
			pasteTargets = wizard.getBOModel().getTargetArray();
		}
		SWTUIUtilities.asyncExec(new Runnable() {

			public void run() {
				m_Frame = SWTUIUtilities.getEmbeddedFrameForShell(wizard
						.getContainer());
				IShellProvider ishellprovider = (IShellProvider) AdapterUtil
						.getAdapter(wizard.getContainer(), IShellProvider.class);
				if (ishellprovider != null)
					ishellprovider.getShell().addDisposeListener(
							new DisposeListener() {

								public void widgetDisposed(
										DisposeEvent disposeevent) {
									m_Frame = SWTUIUtilities
											.getEmbeddedFrameForShell(PlatformHelper
													.getCurrentWorkbenchWindow());
								}

								// final _cls1 this$1;
								//
								// {
								// this$1 = _cls1.this;
								// super();
								// }
							});
			}

			final NewBOOperation this$0;

			{
				this$0 = NewBOOperation.this;

			}
		});
	}

	public void setModel(NewBOModel newbomodel) {
		m_modelObj = newbomodel;
	}

	public void setCreateInput(List list) {
		createInput = list;
	}

	public void setCustomPanelUserInput(Map map) {
		userCustomPanelInput = map;
	}

	public TCComponent getNewComponent() {
		if (m_createdCmps != null && !m_createdCmps.isEmpty())
			return (TCComponent) m_createdCmps.get(0);
		else
			return null;
	}

	public boolean getSuccessFlag() {
		return successFlag;
	}

	protected java.awt.Frame getFrame() {
		return m_Frame;
	}

	public void executeOperation() throws Exception {
		System.out.println("进入 executeOperation ");
		Registry registry = Registry.getRegistry(NewBOOperation.class);
		TCException tcexception = null;
		getMonitor().beginTask(Messages.getString("creatingBO.MSG"), 6);
		createComponent(getMonitor());
		if (m_operationBean != null && getNewComponent() != null) {
			try {
				System.out.println(" 进入 attachfileoperation  ");

				AttachFileOperation attachfileoperation = new AttachFileOperation(
						m_createdCmps, m_operationBean);
				getMonitor().setTaskName(
						registry.getString("AttachFile.MESSAGE"));
				attachfileoperation.executeOperation();
			} catch (TCException tcexception1) {
				if (tcexception1.getSeverity() == 1
						|| tcexception1.getSeverity() == 2)
					tcexception = tcexception1;
				else
					throw tcexception1;
			}
			ClientEventDispatcher
					.fireEventNow(
							m_wizardId,
							"com/teamcenter/rac/newObjectCreated",
							new Object[] {
									"com/teamcenter/rac/wizard/resultOfObjectCreation",
									getNewComponent(),
									"com/teamcenter/rac/wizard/sourceobject",
									null,
									"com/teamcenter/rac/wizard/wizardmodel",
									m_modelObj });
			updateMRUList();
			String s = null;
			if (m_modelObj != null)
				s = m_modelObj.getRelType();
			if (s != null && !s.isEmpty())
				pasteNewComponent(s, null);
			else
				pasteNewComponent();
			getMonitor().setTaskName(registry.getString("PasteObject.Message"));
			postCreate();
			if (tcexception != null)
				throw tcexception;
		} else {
			System.out.println("为空 ");
		}
	}

	protected void createComponent(IProgressMonitor iprogressmonitor)
			throws TCException {
		if (createInput != null && createInput.size() > 0)
			try {
				m_createdCmps = performCreate(createInput);
			} catch (TCException tcexception) {
				successFlag = false;
				iprogressmonitor.setCanceled(true);
				throw tcexception;
			}
	}

	/**
	 * @deprecated Method pasteNewComponent is deprecated
	 */

	protected void pasteNewComponent() throws Exception {
		pasteNewComponent(null);
	}

	/**
	 * @deprecated Method pasteNewComponent is deprecated
	 */

	protected void pasteNewComponent(ICommandListener icommandlistener)
			throws Exception {
		pasteNewComponent(null, icommandlistener);
	}

	/**
	 * @deprecated Method pasteNewComponent is deprecated
	 */

	protected void pasteNewComponent(String s, ICommandListener icommandlistener)
			throws Exception {
		if (getNewComponent() == null)
			return;
		if (m_modelObj != null)
			pasteTargets = m_modelObj.getTargetArray();
		TCComponent atccomponent[] = { getNewComponent() };
		if (m_modelObj != null
				&& m_modelObj.getRevisionFlag()
				&& pasteTargets != null
				&& ((pasteTargets[0] instanceof TCComponentItemRevision) || (pasteTargets[0] instanceof TCComponentTask))
				&& (getNewComponent() instanceof TCComponentItem))
			atccomponent = (new TCComponent[] { ((TCComponentItem) getNewComponent())
					.getLatestItemRevision() });
		if (pasteTargets == null)
			pasteTargets = new InterfaceAIFComponent[0];
		PasteJob pastejob = null;
		PasteDataBean pastedatabean = null;
		StructuredSelection structuredselection = new StructuredSelection(
				pasteTargets);
		if (s != null)
			pastedatabean = new PasteDataBean(new AIFDataBean(session,
					structuredselection), atccomponent, s);
		else
			pastedatabean = new PasteDataBean(new AIFDataBean(session,
					structuredselection), atccomponent);
		pastejob = new PasteJob(pastedatabean);
		pastejob.setFailBackFlag(true);
		pastejob.executeOperation();
	}

	protected void postCreate() {
	}

	protected List performCreate(List list) throws TCException {
		IBOCreateDefinition ibocreatedefinition = m_modelObj.getCreateDef();
		return SOAGenericCreateHelper
				.create(session, ibocreatedefinition, list);
	}

	public void updateMRUList() {
		String as[] = null;
		if (m_modelObj != null)
			as = m_modelObj.getMruKeys();
		if (as == null || as.length != 2)
			return;
		String s = as[0];
		String s1 = as[1];
		if (s == null || s.length() == 0)
			return;
		String s2 = m_modelObj.getCreateDef().getTypeName();
		if (s2 == null || s2.length() == 0)
			return;
		int i = DEFAULT_MRU_MAX;
		if (s1 != null && s1.length() > 0) {
			Integer integer = session.getPreferenceService()
					.getIntegerValue(s1);
			i = integer == null ? DEFAULT_MRU_MAX : integer.intValue();
		}
		String as1[] = session.getPreferenceService().getStringValues(s);
		ArrayList arraylist = new ArrayList(as1 != null ? as1.length : 0);
		if (as1 != null) {
			String as3[];
			int l = (as3 = as1).length;
			for (int k = 0; k < l; k++) {
				String s3 = as3[k];
				arraylist.add(s3);
			}

		}
		if (arraylist.contains(s2))
			arraylist.remove(s2);
		arraylist.add(0, s2);
		if (arraylist.size() > i) {
			for (int j = i; j < arraylist.size(); j++)
				arraylist.remove(j);

		}
		String as2[] = (String[]) arraylist
				.toArray(new String[arraylist.size()]);
		try {
			session.getPreferenceService().setStringValues(s, as2);
		} catch (TCException tcexception) {
			Logger.getLogger(NewBOOperation.class).error(
					tcexception.getLocalizedMessage(), tcexception);
		}
	}

	public void setOperatinDataForPages(AIFDataBean aifdatabean) {
		m_operationBean = aifdatabean;
	}

	protected void setNewComponents(List list) {
		m_createdCmps = list;
	}

	/**
	 * @deprecated Field pasteTargets is deprecated
	 */
	protected InterfaceAIFComponent pasteTargets[];
	/**
	 * @deprecated Field session is deprecated
	 */
	protected TCSession session;
	protected NewBOWizard wizard;
	String m_wizardId;
	private NewBOModel m_modelObj;
	protected List createInput;
	protected Map userCustomPanelInput;
	protected boolean successFlag;
	private java.awt.Frame m_Frame;
	public static int DEFAULT_MRU_MAX = 5;
	private AIFDataBean m_operationBean;
	private List m_createdCmps;

}

/*
 * DECOMPILATION REPORT
 * 
 * Decompiled from:
 * F:\Teamcenter11ENV\JAVA\rac\plugins\com.teamcenter.rac.ui.commands_11000
 * .2.0.jar Total time: 296 ms Jad reported messages/errors: Exit status: 0
 * Caught exceptions:
 */