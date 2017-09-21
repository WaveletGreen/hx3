package com.connor.hx3.plm.hxom039;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentFolderType;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.MessageBox;

public class FileOperation extends AbstractAIFOperation {

	private TCSession session;
	private InterfaceAIFComponent targetComp;
	private File pathfile;
    private String xls = "xls";  
    private String xlsx = "xlsx";
    private String filename;
    private String[] filepath;
    private List<String> tempfilelist = new ArrayList<>();
	public FileOperation(String filename, TCSession session, InterfaceAIFComponent targetComp) {
		// TODO Auto-generated constructor stub
		this.session = session;
		this.targetComp = targetComp;
		this.filename = filename;
	}

	@Override
	public void executeOperation() throws Exception {
		// TODO Auto-generated method stub
		this.pathfile = new File(filename);
		List<List<String>> excelvaluelist = new ArrayList<List<String>>();
		
		if(!this.filename.endsWith(this.xls) && !this.filename.endsWith(this.xlsx)){  
            MessageBox.post("ѡ�еĲ���excel�ļ�����ѡ��excel�ļ�", "����", MessageBox.WARNING);
			return;
        } 
		
		if(this.filename.endsWith(this.xls)){
			ExcelUtil excelutil = new ExcelUtil();
			excelvaluelist = excelutil.readExcel(this.pathfile);
		}
		
		if(this.filename.endsWith(this.xlsx)){
			ExcelUtil07 excelutil = new ExcelUtil07();
			excelvaluelist = excelutil.readExcel(this.pathfile);
		}
		
	
		String item_id = null;
		String file = null;
		TCComponentItemType itemtype = (TCComponentItemType) session.getTypeComponent("Item");
		TCComponentItem item =null;
		List<String> tempitemlist = new ArrayList<>();
		List<String> tempitem_idlist = new ArrayList<>();
		TCComponent comp = null;
		if(excelvaluelist != null){
			for(int i = 0; i<excelvaluelist.size(); i++){
				
				item_id = excelvaluelist.get(i).get(0);
				file = excelvaluelist.get(i).get(1);
				if(item_id == null || item_id == ""){
					break;
				}
				item = itemtype.find(item_id);
				if(item == null){
					//item_id +"�����ڣ����������Ƿ�����
					tempitem_idlist.add(item_id);
				}
				if(item != null){
					if (file == null) {
						MessageBox.post("�鵵·������Ϊ�գ����鲢�������룡", "����", MessageBox.ERROR);
					}
					this.filepath = null;
					this.filepath = file.split("\\/");
					try {
						comp=null;
						comp = this.session.stringToComponent(filepath[0]);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(comp == null){
						this.tempfilelist.add(filepath[0]);
						//MessageBox.post("�鵵·�������ڣ����鲢�������룡", "����", MessageBox.ERROR);
					}
					if(comp != null){
						if(comp instanceof TCComponentFolder){
							TCComponentFolder comp1 = (TCComponentFolder) comp;
							if(filepath.length >1){
								for(int num = 1; num < filepath.length; num++){
									//System.out.println(filepath[num]+ " is filepath[num]");
									comp1 = ChildrenFolder(comp1,filepath[num]);
									if(comp1 == null){
										this.tempfilelist.add(filepath[num]);
										break;
									}
								}
								
							}
							
							System.out.println(comp1+" is comp1(1)");
							if(comp1 == null){
								continue;
							}
							//�ж�item�Ƿ����
							TCComponent[] temp = comp1.getReferenceListProperty("contents");
							String tempitem_id =null;
							if(temp != null){
								for(int it = 0; it < temp.length; it++){
									if(temp[it] instanceof TCComponentItem){
										if(temp[it].getStringProperty("item_id").equals(item_id)){
											tempitem_id = item_id;
											break;
										}
									}
								}
							}
							
							if(tempitem_id == null){
								comp1.add("contents", item);
								System.out.println(item_id+" �鵵�ɹ�");
							}else{
								tempitemlist.add(item_id);
							}
							
						}else{
							MessageBox.post("�鵵·�������ļ��У����鲢�������룡", "����", MessageBox.ERROR);
						}
					}
				}
			}
			StringBuffer sb = new StringBuffer();
			if(tempitemlist != null && tempitemlist.size() != 0){
				//MessageBox.post(tempitemlist+"�Ѵ���", "����", MessageBox.ERROR);
				tempitemlist.add("\n����Item�Ѵ��ڹ鵵��¼,���鲢�������룡");
				sb.append(tempitemlist);
				sb.append("\n");
			}
			if(tempfilelist != null && tempfilelist.size() != 0){
				//MessageBox.post(tempfilelist + "�����ڣ����鲢�������룡", "����", MessageBox.ERROR);
				tempfilelist.add("\n�����ļ��в����ڣ����鲢�������룡");
				sb.append(tempfilelist);
				sb.append("\n");
			}
			if(tempitem_idlist != null && tempitem_idlist.size() != 0){
				//MessageBox.post(tempitem_idlist +"�����ڣ����������Ƿ�����", "����", MessageBox.ERROR);
				tempitem_idlist.add("\n����item_id�����ڣ����鲢�������룡");
				sb.append(tempitem_idlist);
				//sb.append("\n");
			}
			String result = sb.toString();
				MessageBox.post("�鵵����\n" + result, "��Ϣ", MessageBox.INFORMATION);
			
		}else{
			MessageBox.post("�鵵����Ϊ�գ����鲢�������룡", "����", MessageBox.ERROR);
		}
		
	}
	
	public TCComponentFolder ChildrenFolder(TCComponentFolder comp1, String string){
		System.out.println(comp1+"comp1(4)");
		try {
			TCComponent[] childrenFolder = comp1.getReferenceListProperty("contents");
			//System.out.println(childrenFolder+" is childrenFolder");
			comp1=null;
			if(childrenFolder != null){
				for(int f = 0; f <childrenFolder.length; f++){
					if(childrenFolder[f] instanceof TCComponentFolder){
						String prop = ((TCComponentFolder)childrenFolder[f]).getProperty("object_name");
						//System.out.println(prop+" is prop");
						if( prop.equals(string)){
							comp1 = (TCComponentFolder)childrenFolder[f];
							break;
						}
						continue;
					}
				}
			}
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return comp1;
	}

}
 