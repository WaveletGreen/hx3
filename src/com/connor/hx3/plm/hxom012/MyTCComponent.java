package com.connor.hx3.plm.hxom012;

import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class MyTCComponent {
	public static TCSession session;
	static {
		if (session == null) {
			session = (TCSession) AIFUtility.getCurrentApplication()
					.getSession();
		}
	}
	private TCComponent comp;

	public MyTCComponent(TCComponent comp) {
		this.comp = comp;
	}

	public MyTCComponent(String id) {
		try {
			if (session != null) {

				comp = session.stringToComponent(id);
			}
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String toString() {
		try {
			if (comp != null)
				return comp.getStringProperty("object_string");
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NULL";
	}
}