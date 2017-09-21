package com.connor.hx3.plm.hxom038;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.teamcenter.rac.aif.AIFDesktop;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.aifrcp.SelectionHelper;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.services.ISessionService;
import com.teamcenter.rac.ui.commands.Messages;
import com.teamcenter.rac.ui.commands.RACUICommandsActivator;
import com.teamcenter.rac.ui.commands.create.bo.NewBOModel;
import com.teamcenter.rac.ui.commands.create.bo.NewBOWizard;
import com.teamcenter.rac.util.OSGIUtil;
import com.teamcenter.rac.util.SWTUIUtilities;
import com.teamcenter.rac.util.UIUtilities;
import com.teamcenter.rac.util.wizard.extension.BaseExternalWizardDialog;
import com.teamcenter.rac.util.wizard.extension.WizardExtensionHelper;

/**
 * TC9及其以上版本的新建对象（包括零组件、文件夹、表单）的新的swt的界面 com.teamcenter.rac.ui.commands.handlers
 * 
 * 
 * @author hub
 * 
 */
public class CreateHandler extends AbstractHandler implements
		IExecutableExtension {
	private String opType = "";

	private class CreateNewBOSWTDialog implements Runnable {

		@Override
		public void run() {
			NewBOWizard newbowizard = (NewBOWizard) getWizard();
			if (newbowizard == null)
				newbowizard = new NewBOWizard(wizardId);
			newbowizard.setBOModel(m_boModel);
			newbowizard.setWindowTitle(getWizardTitle());
			newbowizard.setRevisionFlag(m_boModel.getRevisionFlag());
			newbowizard.setDefaultType(m_type);
			Shell shell = UIUtilities.getCurrentModalShell();
			dialog = new BaseExternalWizardDialog(m_shell, newbowizard);
			dialog.create();
			newbowizard.retrievePersistedDialogSettings(dialog);
			newbowizard.setWizardDialog(dialog);
			UIUtilities.setCurrentModalShell(dialog.getShell());
			dialog.open();
			dialog = null;

			InterfaceAIFComponent[] compS = m_boModel.getTargetArray();

			if (compS != null && compS.length != 0) {
				System.out.println(" length =" + compS.length);
				if (compS[0] instanceof TCComponentItem) {

					System.out.println("ITEM");
				} else if (compS[0] instanceof TCComponentFolder) {

					System.out.println("FOLDER");
				}
				TCComponent compT = (TCComponent) compS[0];
				try {
					System.out.println("object_name =>"
							+ compT.getStringProperty("object_name"));
				} catch (TCException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				System.out.println(" null ");
			}

			System.out.println("=====>");
			List<AIFComponentContext> contextList = m_boModel.getCompContext();
			if (contextList != null) {
				for (AIFComponentContext context : contextList) {
					InterfaceAIFComponent comp = context.getComponent();
					try {
						System.out.println(">"
								+ ((TCComponent) comp)
										.getStringProperty("object_name"));
					} catch (TCException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
			System.out.println("=====>");
			m_boModel = null;

			UIUtilities.setCurrentModalShell(shell);

			System.out.println("dialog ==>  " + opType + " | " + m_type);
		}

		private final Shell m_shell;
		private final String m_type;
		final CreateHandler this$0;

		private CreateNewBOSWTDialog(Shell shell, String s) {
			super();
			this$0 = CreateHandler.this;

			m_shell = shell;
			m_type = s;
		}

		CreateNewBOSWTDialog(Shell shell, String s,
				CreateNewBOSWTDialog createnewboswtdialog) {
			this(shell, s);
		}

	}

	@Override
	public Object execute(ExecutionEvent executionevent)
			throws ExecutionException {

		opType = "";// executionevent.getCommand().getId();

		if (executionevent == null)
			throw new IllegalArgumentException("Event can't be null");

		boolean flag = false;
		if (executionevent.getParameters() != null
				&& executionevent.getParameters().containsKey("selection")) {
			Object obj = executionevent.getParameters().get("selection");
			if (obj instanceof InterfaceAIFComponent[]) {
				selectedCmps = (InterfaceAIFComponent[]) obj;
				m_currentSelection = new StructuredSelection(selectedCmps);
				flag = true;
			}
		}
		if (!flag) {
			m_currentSelection = HandlerUtil
					.getCurrentSelection(executionevent);
			selectedCmps = SelectionHelper
					.getTargetComponents(m_currentSelection);
		}
		m_boModel = getBOModel();
		launchWizard(executionevent);
		return null;
	}

	@Override
	public void setInitializationData(
			IConfigurationElement iconfigurationelement, String s, Object obj)
			throws CoreException {
	}

	protected NewBOModel getBOModel() {
		if (m_boModel == null)
			m_boModel = new NewBOModel(this);
		return m_boModel;
	}

	public Wizard getWizard() {
		if (wizardId == null || wizardId.length() == 0)
			wizardId = "com.teamcenter.rac.ui.commands.create.bo.NewBOWizard";
		return WizardExtensionHelper.getWizard(wizardId);
	}

	public String getWizardTitle() {
		return Messages.getString("wizard.TITLE");
	}

	public void launchWizard() {
		launchWizard(null);
	}

	public void launchWizard(ExecutionEvent executionevent) {
		String s = this.opType;
		initWizardModel(executionevent);
		// if (executionevent != null)
		// s = (String) executionevent.getParameters().get("objectType");
		AIFDesktop aifdesktop = AIFUtility.getActiveDesktop();
		Shell shell = aifdesktop.getShell();
		if (shell != null)
			SWTUIUtilities.asyncExec(new CreateNewBOSWTDialog(shell, s, null));
	}

	protected void initWizardModel(ExecutionEvent executionevent) {
		boolean flag = false;
		Object obj = null;
		ISessionService isessionservice = (ISessionService) OSGIUtil
				.getService(RACUICommandsActivator.getDefault(),
						com.teamcenter.rac.services.ISessionService.class);
		try {
			session = (TCSession) isessionservice
					.getSession(com.teamcenter.rac.kernel.TCSession.class
							.getName());
		} catch (Exception _ex) {
			session = (TCSession) AIFUtility.getDefaultSession();
		}
		m_boModel.setSession(session);
		m_boModel.reInitializeTransientData();
		m_boModel.setFrame(AIFUtility.getActiveDesktop());
		if (executionevent != null) {
			if (executionevent.getParameters().containsKey("revisionFlag"))
				flag = ((Boolean) executionevent.getParameters().get(
						"revisionFlag")).booleanValue();
			if (executionevent.getParameters().containsKey("pasteRelation")) {
				String s = (String) executionevent.getParameters().get(
						"pasteRelation");
				if (s != null) {
					String as[] = null;
					as = s.split(",");
					m_boModel.setRelType(as[0]);
					m_boModel.setPreAssignedRelType(as);
				}
			}
			if (executionevent.getParameters().containsKey("parentComponents")) {
				InterfaceAIFComponent ainterfaceaifcomponent[] = (InterfaceAIFComponent[]) executionevent
						.getParameters().get("parentComponents");
				m_boModel.setTargetArray(ainterfaceaifcomponent);
			} else {
				m_boModel.setTargetArray(selectedCmps);
			}
		} else {
			m_boModel.setTargetArray(selectedCmps);
		}
		m_boModel.setRevisionFlag(flag);
		AIFDesktop aifdesktop = AIFUtility.getActiveDesktop();
		Shell shell = aifdesktop.getShell();
		m_boModel.setShell(shell);
		m_boModel.setCurrentSelection(m_currentSelection);
	}

	protected void readDisplayParameters(NewBOWizard newbowizard,
			WizardDialog wizarddialog) {
		newbowizard.retrievePersistedDialogSettings(wizarddialog);
	}

	protected InterfaceAIFComponent selectedCmps[];
	protected ISelection m_currentSelection;
	protected String wizardId;
	protected WizardDialog dialog;
	/**
	 * @deprecated Field session is deprecated
	 */
	@Deprecated
	protected TCSession session;
	protected NewBOModel m_boModel;
}

/*
 * DECOMPILATION REPORT
 * 
 * Decompiled from:
 * F:\Teamcenter11ENV\JAVA\rac\plugins\com.teamcenter.rac.ui.commands_11000
 * .2.0.jar Total time: 72 ms Jad reported messages/errors: Exit status: 0
 * Caught exceptions:
 */