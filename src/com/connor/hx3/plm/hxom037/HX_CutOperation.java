package com.connor.hx3.plm.hxom037;

//public class HX_HX_CutOperation {
//
//}
/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;

import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aif.kernel.AbstractAIFSession;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.commands.cut.CutOperation;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentCba0PartProxy;
import com.teamcenter.rac.kernel.TCComponentDataset;
import com.teamcenter.rac.kernel.TCComponentDrawingProxy;
import com.teamcenter.rac.kernel.TCComponentForm;
import com.teamcenter.rac.kernel.TCComponentProcess;
import com.teamcenter.rac.kernel.TCComponentPseudoFolder;
import com.teamcenter.rac.kernel.TCComponentTask;
import com.teamcenter.rac.kernel.TCComponentTcFile;
import com.teamcenter.rac.kernel.TCComponentTraceLink;
import com.teamcenter.rac.kernel.TCComponentUser;
import com.teamcenter.rac.kernel.TCComponentWorkContext;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.Registry;
import com.teamcenter.rac.util.log.Debug;

public class HX_CutOperation extends CutOperation {

	public HX_CutOperation(TCComponent tccomponent, TCComponent tccomponent1,
			String s) {
		super(tccomponent, tccomponent1, s);
		m_pendingCut = false;
		m_parentCmp = tccomponent;
		m_childCmps = (new TCComponent[] { tccomponent1 });
		m_contextString = s;
	}

	public HX_CutOperation(AIFComponentContext aifcomponentcontext, boolean flag) {
		super(aifcomponentcontext, flag);
		m_pendingCut = false;
		m_parentCmp = (TCComponent) aifcomponentcontext.getParentComponent();
		m_childCmps = (new TCComponent[] { (TCComponent) aifcomponentcontext
				.getComponent() });
		m_contextString = (String) aifcomponentcontext.getContext();
		m_pendingCut = flag;
	}

	public HX_CutOperation(TCComponent tccomponent,
			AIFComponentContext aaifcomponentcontext[], boolean flag) {
		super(tccomponent, aaifcomponentcontext, flag);
		m_pendingCut = false;
		m_parentCmp = tccomponent;
		if (aaifcomponentcontext != null) {
			ArrayList arraylist = new ArrayList();
			for (int i = 0; i < aaifcomponentcontext.length; i++)
				arraylist.add((TCComponent) aaifcomponentcontext[i]
						.getComponent());

			m_childCmps = (TCComponent[]) arraylist
					.toArray(new TCComponent[arraylist.size()]);
			m_contextString = (String) aaifcomponentcontext[0].getContext();
		}
		m_pendingCut = flag;
	}

	public void executeOperation() throws TCException {
		if (m_parentCmp == null)
			return;
		System.out.println("cut ====>  ");
		TCSession tcsession = m_parentCmp.getSession();
		Registry registry = Registry.getRegistry(this);
		boolean flag = false;
		StringBuilder stringbuilder = new StringBuilder();
		if (m_childCmps != null) {
			for (int i = 0; i < m_childCmps.length; i++) {
				stringbuilder.append((new StringBuilder(String
						.valueOf(i != 0 ? ", " : ""))).append(m_childCmps[i])
						.toString());
				if (!flag && (m_childCmps[i] instanceof TCComponentWorkContext))
					flag = true;
			}

		}
		tcsession.setStatus((new StringBuilder(String.valueOf(registry
				.getString("cutting")))).append(" ")
				.append(stringbuilder.toString()).append(" ")
				.append(registry.getString("from")).append(" ")
				.append(m_parentCmp.toString()).toString());
		if (!m_pendingCut && m_childCmps != null)
			try {
				TCComponent tccomponent = null;
				Object obj = null;
				if (m_parentCmp instanceof TCComponentPseudoFolder)
					tccomponent = ((TCComponentPseudoFolder) m_parentCmp)
							.getOwningComponent();
				if ((m_parentCmp instanceof TCComponentPseudoFolder)
						&& (tccomponent instanceof TCComponentCba0PartProxy)) {
					((TCComponentCba0PartProxy) tccomponent).cutOperation(
							m_contextString, m_childCmps);
					Object obj1 = null;
					for (int k = 0; k < m_childCmps.length; k++) {
						TCComponent tccomponent1 = m_childCmps[k];
						AIFComponentContext aaifcomponentcontext[] = tccomponent1
								.getChildren();
						Object obj3 = null;
						AIFComponentContext aaifcomponentcontext2[];
						int k1 = (aaifcomponentcontext2 = aaifcomponentcontext).length;
						for (int i1 = 0; i1 < k1; i1++) {
							AIFComponentContext aifcomponentcontext = aaifcomponentcontext2[i1];
							InterfaceAIFComponent interfaceaifcomponent = aifcomponentcontext
									.getComponent();
							if (interfaceaifcomponent instanceof TCComponentPseudoFolder)
								((TCComponent) interfaceaifcomponent).refresh();
						}

					}

					m_parentCmp.refresh();
					m_parentCmp.fireComponentChangeEvent();
				} else if ((m_parentCmp instanceof TCComponentPseudoFolder)
						&& (tccomponent instanceof TCComponentDrawingProxy)) {
					((TCComponentDrawingProxy) tccomponent).cutOperation(
							m_contextString, m_childCmps);
					Object obj2 = null;
					for (int l = 0; l < m_childCmps.length; l++) {
						TCComponent tccomponent2 = m_childCmps[l];
						AIFComponentContext aaifcomponentcontext1[] = tccomponent2
								.getChildren();
						Object obj4 = null;
						AIFComponentContext aaifcomponentcontext3[];
						int l1 = (aaifcomponentcontext3 = aaifcomponentcontext1).length;
						for (int j1 = 0; j1 < l1; j1++) {
							AIFComponentContext aifcomponentcontext1 = aaifcomponentcontext3[j1];
							InterfaceAIFComponent interfaceaifcomponent1 = aifcomponentcontext1
									.getComponent();
							if (interfaceaifcomponent1 instanceof TCComponentPseudoFolder)
								((TCComponent) interfaceaifcomponent1)
										.refresh();
						}

					}

					m_parentCmp.refresh();
					m_parentCmp.fireComponentChangeEvent();
				} else if ((m_parentCmp instanceof TCComponentPseudoFolder)
						&& (tccomponent instanceof TCComponentTask)
						&& m_parentCmp.toString().equals("Parent Processes"))
					try {
						TCComponentProcess tccomponentprocess = ((TCComponentTask) tccomponent)
								.getProcess();
						for (int j = 0; j < m_childCmps.length; j++) {
							TCComponentProcess tccomponentprocess1 = (TCComponentProcess) m_childCmps[j];
							TCComponent tccomponent3 = ((TCComponentPseudoFolder) m_parentCmp)
									.getOwningComponent();
							tccomponentprocess1.removeSubProcess(
									tccomponentprocess, tccomponent3);
						}

						m_parentCmp.refresh();
						m_parentCmp.fireComponentChangeEvent();
					} catch (Exception exception) {
						Debug.println(exception.getClass().getName(), exception);
					}
				else if ((m_parentCmp instanceof TCComponentPseudoFolder)
						&& (tccomponent instanceof TCComponentDataset)
						|| (m_parentCmp instanceof TCComponentDataset)
						&& m_contextString.equals("ref_list")) {
					removeDatasetNamedReference();
				} else {
					if ((m_parentCmp instanceof TCComponentTraceLink)
							&& m_childCmps[0]
									.isTypeOf("Fnd0CustomNoteRevision"))
						m_contextString = "fnd0CustomNotes";
					m_parentCmp.cutOperation(m_contextString, m_childCmps);
				}
				tcsession.setReadyStatus();
				if (flag)
					clearWorkContextProperty(tcsession);
			} catch (TCException tcexception) {
				tcsession.setStatus(tcexception.toString());
				throw tcexception;
			}
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

	private void removeDatasetNamedReference() {
		ArrayList arraylist = new ArrayList();
		ArrayList arraylist1 = new ArrayList();
		TCComponent tccomponent = null;
		if (m_parentCmp instanceof TCComponentPseudoFolder)
			tccomponent = ((TCComponentPseudoFolder) m_parentCmp)
					.getOwningComponent();
		TCComponentDataset tccomponentdataset;
		if (m_parentCmp instanceof TCComponentDataset)
			tccomponentdataset = (TCComponentDataset) m_parentCmp;
		else
			tccomponentdataset = (TCComponentDataset) tccomponent;
		try {
			TCProperty tcproperty = tccomponentdataset
					.getTCProperty("ref_list");
			TCComponent atccomponent[] = tcproperty.getReferenceValueArray();
			TCProperty tcproperty1 = tccomponentdataset
					.getTCProperty("ref_names");
			String as[] = tcproperty1.getStringValueArray();
			if (atccomponent != null && atccomponent.length != 0 && as != null
					&& as.length != 0) {
				for (int i = 0; i < m_childCmps.length; i++) {
					for (int j = 0; j < atccomponent.length; j++) {
						if (m_childCmps[i] != atccomponent[j])
							continue;
						if (m_childCmps[i] instanceof TCComponentTcFile) {
							arraylist.add(atccomponent[j].toString());
							arraylist1.add(as[j]);
							TCComponentTcFile tccomponenttcfile = (TCComponentTcFile) atccomponent[j];
							tccomponenttcfile
									.setRefDatasetNamedReferences(as[j]);
						} else {
							tccomponentdataset.removeNamedReference(as[j]);
							TCComponentForm tccomponentform = (TCComponentForm) atccomponent[j];
							tccomponentform.setRefDatasetNamedReferences(as[j]);
							m_parentCmp.refresh();
						}
						break;
					}

				}

			}
			if (arraylist.size() != 0) {
				String as1[] = (String[]) arraylist
						.toArray(new String[arraylist.size()]);
				String as2[] = (String[]) arraylist1
						.toArray(new String[arraylist1.size()]);
				tccomponentdataset.removeFiles(as1, as2);
				m_parentCmp.refresh();
			}
		} catch (Exception exception) {
			Logger.getLogger(HX_CutOperation.class).error(
					exception.getLocalizedMessage(), exception);
		}
	}

	public AIFComponentContext[] getTargetContexts() {
		return (new AIFComponentContext[] { new AIFComponentContext(null,
				m_parentCmp, "context") });
	}

	public Map getPartialFailedTargets() {
		return Collections.emptyMap();
	}

	private final TCComponent m_parentCmp;
	private TCComponent m_childCmps[];
	private String m_contextString;
	private boolean m_pendingCut;
}

/*
 * DECOMPILATION REPORT
 * 
 * Decompiled from:
 * F:\Teamcenter11ENV\JAVA\rac\plugins\com.teamcenter.rac.common_11000.2.0.jar
 * Total time: 127 ms Jad reported messages/errors: Exit status: 0 Caught
 * exceptions:
 */
