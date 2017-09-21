package com.connor.hx3.plm.hxom002;

import java.util.List;

import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCException;

public class ZSK_Bean {
	
	public ZSK_Bean(){
		
	}
	
	public ZSK_Bean(TCComponentFolder folder){
		this.folder  = folder;
		if(folder!=null){
			try {
				name = folder.getStringProperty("object_name");
			} catch (TCException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public String name;
	public TCComponentFolder folder;
	public List<ZSK_Bean> childList;

}
