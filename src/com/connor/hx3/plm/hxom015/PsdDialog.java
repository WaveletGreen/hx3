package com.connor.hx3.plm.hxom015;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.connor.hx3.plm.hxom016.MethodUtil;
import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentDataset;
import com.teamcenter.rac.kernel.TCComponentDatasetType;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentTcFile;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCPreferenceService;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.kernel.TCTypeService;
import com.teamcenter.rac.util.FileUtility;
import com.teamcenter.rac.util.MessageBox;

public class PsdDialog {
	private TCComponentDataset dataset;
	private static String name;
	private AbstractAIFUIApplication app;
	private static TCSession session;
	private static InterfaceAIFComponent comment;
//	private String folderUid;
	private String cxkey;
	private String searchName;
	private String[] keys;
	private String[] values;
	private InterfaceAIFComponent[] com;
	private TCComponentFolder folder;
//	private ArrayList<TCComponentFolder> folderList;
	private String lclxName;
	private TCComponentFolder compFolder;
	private ArrayList<PsdBean> beanList;
	private Map<String, List<PsdBean>> typeMap;
	private Map<String, Map<String, List<PsdBean>>> jfjdNameMap;
	private String projectNameString = null;
	private String folderName;
	//private TCComponent comp;
	private TCComponent[] em;
	private File dirFile;
	private String newpath;
	private static TCComponentDataset dataset2;
	private static File file;

	public PsdDialog(String name, AbstractAIFUIApplication app,
			TCSession session) throws Exception {
		this.name = name;
		this.app = app;
		this.session = session;
		this.beanList = new ArrayList<PsdBean>();
		this.typeMap = new HashMap<>();
		this.jfjdNameMap = new HashMap<String, Map<String, List<PsdBean>>>();

		init();

	}

	public void init() throws Exception {
		// app = AIFUtility.getCurrentApplication();
		comment = app.getTargetComponent();
		if (comment instanceof TCComponentFolder) {
			TCComponentFolder tarFolder = (TCComponentFolder) comment;

			AIFComponentContext[] com1 = tarFolder.whereReferenced();
			
			//TCProperty pro1 = tarFolder.getTCProperty("fnd0MyWorkflowTasks");// fnd0MyWorkflowTasks
			//TCComponent[] com1 = pro1.getReferenceValueArray();
			//System.out.println("com1->1");

			for (int i = 0; i < com1.length; i++) {
				if(!(com1[i].getComponent() instanceof TCComponentFolder )){
					continue;	
				}
				if(!((TCComponentFolder)com1[i].getComponent()).getType().equals("HX3_GDJG")){
					continue;
				}
				AIFComponentContext[] contexts = ((TCComponentFolder)com1[i].getComponent()).whereReferenced();
//				TCProperty pro2 = com1[i]
//						.getTCProperty("project_task_attachments");// project_task_attachments
//				TCComponent[] com2 = pro2.getReferenceValueArray();
				//for (int j = 0; j < com2.length; j++) {
//					TCProperty pro3 = com2[j].getTCProperty("schedule_tag");// schedule_tag
//					TCComponent com3 = pro3.getReferenceValue();

				//	AIFComponentContext[] contexts;
//
				//	contexts = com3.whereReferenced();

//					System.out.println("2->" + com3.getType());
					System.out.println("123");
					System.out.println(contexts.length);

					if (contexts == null || contexts.length == 0) {
						MessageBox.post("该时间表没有归档在项目下", "提示", MessageBox.ERROR);
						// return;
					}
					for (int k = 0; k < contexts.length; k++) {
						InterfaceAIFComponent context = contexts[k]
								.getComponent();
						if (context instanceof TCComponentFolder
								&& (context.getType()).equals("HX3_GDJGSJ")) {

							//folderList = new ArrayList<TCComponentFolder>();
							//folderList.add((TCComponentFolder) context);
							folder = (TCComponentFolder) context;//folderList.get(0);
							//folderUid = folder.getUid();// 得到文件夹的uid
							//comp = session.stringToComponent(folderUid);// puid转换成对象
							TCProperty lclx = folder.getTCProperty("hx3_lclx");
							lclxName = lclx.getDisplayValue().toString();

							//System.out.println(folderUid);
							System.out.println(lclxName);

						}
					}
					// 首选项
					TCPreferenceService service = ((TCSession) AIFUtility
							.getCurrentApplication().getSession())
							.getPreferenceService();
					String[] ColVal = service.getStringArray(
							TCPreferenceService.TC_preference_site,
							"hx_PSDcheck");
					if (ColVal == null || ColVal.length == 0) {
						MessageBox.post("首选项：" + "hx_PSDcheck"
								+ "未设置值或未定义，请联系系统管理员！", "提示",
								MessageBox.WARNING);
						// this.disposeDialog();
					}
					System.out.println("=>" + this.name + "&" + lclxName);
					// 查询
					boolean isdianjian = false;
					for (int a = 0; a < ColVal.length; a++) {
						if (ColVal[a].contains(this.name + "&" + lclxName)) {
							isdianjian = true;
							cxkey = ColVal[a]
									.substring(ColVal[a].indexOf(":") + 1);// 得到首选项中"："后面的值

							System.out.println(":后面的值———>" + cxkey);

							searchName = "hx_searchTemp";
							keys = new String[] { "ID" };
							String val = cxkey;

							values = new String[] { val };
							com = MethodUtil.searchComponentsCollection(
									(TCSession) app.getSession(), searchName,
									keys, values);
							if (com == null || com.length == 0) {
								MessageBox.post("没有找到["+cxkey+"]的模板，请联系管理员","警告",MessageBox.WARNING);
								
								return;
							}

							System.out.println(com.length);

							for (int k = 0; k < com.length; k++) {
								if (com[k] instanceof TCComponentItemRevision) {
									TCComponentItemRevision rev1 = (TCComponentItemRevision) com[k];
									em = rev1
											.getReferenceListProperty("TC_Attaches");
									for (int d = 0; d < em.length; d++) {
										System.out.println(em[d].getType());
										if (em[d] instanceof TCComponentDataset) {
											dataset = (TCComponentDataset) em[d];
										}
									}
								}
							}
							downLoadFile(dataset);
							newpath = downLoadFile(dataset);
							getChildrenFolder(folder, 0);
							writemsg(jfjdNameMap);
						}else{
							
						}
					}
					if(!isdianjian){
						MessageBox.post("["+this.name+"]评审不需要进行点检","警告",MessageBox.WARNING);
					}
				//}
			}
		}
	}

	/**
	 * 下载dataset下的excel
	 * 
	 * @param dataset
	 * @param dirFile
	 * @return
	 * @throws TCException
	 * @throws IOException
	 */
	// private File expertFileToDir(TCComponentDataset dataset, File dirFile)
	// throws TCException, IOException {
	// TCComponentTcFile[] tcFiles = dataset.getTcFiles();
	// File file;
	// if (tcFiles != null && tcFiles.length > 0) {
	// file = tcFiles[0].getFmsFile();
	// if (file != null) {
	// String path = file.getAbsolutePath();
	// newpath = "D://已匹配数据工作清单副本.xlsx";
	// copyFile(path, newpath);
	// } else {
	// System.out.println("空的file");
	// }
	// }
	// return null;
	// }

	/**
	 * 下载文件
	 * 
	 * @param comps
	 * @return
	 * @throws TCException
	 * @throws IOException
	 */
	public static String downLoadFile(TCComponent comp) {
		if (comp == null) {
			return "";
		}
		String value = "";
		String tempPath = System.getenv("TEMP");
		// MessageBox.post(" tempPath =
		// "+tempPath,"INFO",MessageBox.INFORMATION);
		if (tempPath == null) {
			tempPath = "";
		} else if (!tempPath.endsWith("\\")) {
			tempPath = tempPath + "\\";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		// for(TCComponent comp : comps){
		try {
			if (comp instanceof TCComponentDataset) {
				TCComponentTcFile[] tcFiles = ((TCComponentDataset) comp)
						.getTcFiles();
				File file = null;
				if (tcFiles != null && tcFiles.length != 0) {
					file = tcFiles[0].getFmsFile();
					String fileName = file.getName();
					String fileDix = fileName.substring(
							fileName.lastIndexOf("."), fileName.length());
					fileName = tempPath + sdf.format(new Date()) + fileDix;
					File dirFile = new File(fileName);
					FileUtility.copyFile(file, dirFile);

					return fileName;
				}
			}
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// }
		return value;
	}

	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				FileInputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.flush();
				fs.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 遍历文件夹并获取属性
	 * 
	 * @throws TCException
	 * @throws IOException
	 */
	public void getChildrenFolder(TCComponent comp, int index)
			throws TCException, IOException {

		if (comp == null) {
			MessageBox.post("该时间表的上级文件夹不存在", "失败", MessageBox.ERROR);
		}
		if (comp instanceof TCComponentFolder) {
			compFolder = (TCComponentFolder) comp;
			folderName = comp.getStringProperty("object_name");
			if (index == 0) {
				projectNameString = folderName;
			}
			PsdBean bean = new PsdBean();
			bean.setJFJD(projectNameString);
		} else {
			return;
		}
		AIFComponentContext[] contexts = compFolder.getChildren("contents");

		if (contexts != null && contexts.length != 0) {
			beanList = new ArrayList<PsdBean>();
			typeMap = new HashMap<>();
			for (AIFComponentContext context : contexts) {
				InterfaceAIFComponent component = context.getComponent();
				if (component instanceof TCComponentFolder) {
					getChildrenFolder((TCComponent) component, 1);// 1

					System.out.print("遍历文件夹");
				} else if (component instanceof TCComponentItem) {
					System.out.println("是ITEM对象");
					TCComponentItem item = (TCComponentItem) component;
					TCProperty prop = item.getTCProperty("object_type");
					TCComponentItemRevision revision = item
							.getLatestItemRevision();
					if (revision == null) {
						PsdBean bean = new PsdBean();
						beanList.add(bean);
						typeMap.put(prop.getDisplayableValue(), beanList);
						jfjdNameMap.put(folderName, typeMap);
					} else {
						PsdBean bean = new PsdBean();
						TCProperty[] props = revision
								.getTCProperties(new String[] { "object_name",
										"item_id", "owning_user",
										"date_released", "item_revision_id" });

						if (props[3].getDateValue() != null) {
							Date dateString = props[3].getDateValue();
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd");
							String st = sdf.format(dateString);
							bean.setDate_released(st);
						} else {
							bean.setDate_released("没有发布");
						}
						bean.setObject_name(props[0].getStringValue());
						bean.setItem_id(props[1].getStringValue());
						bean.setOwning_user(props[2]
								.getReferenceValueInString());
						bean.setRevision_id(props[4].getStringValue());

						if (typeMap.containsKey(prop.getDisplayableValue())) {
							beanList = (ArrayList<PsdBean>) typeMap.get(prop
									.getDisplayableValue());
						} else {
							beanList = new ArrayList<>();

						}
						beanList.add(bean);
						typeMap.put(prop.getDisplayableValue(), beanList);
						jfjdNameMap.put(folderName, typeMap);
						System.out.println("folderName =>" + folderName
								+ " count =" + typeMap.size());
					}
				} else if (component instanceof TCComponentItemRevision) {
					System.out.println("是rev对象");
					PsdBean bean = new PsdBean();
					TCComponentItemRevision revision = (TCComponentItemRevision) component;
					TCComponentItem item = revision.getItem();
					TCProperty prop = item.getTCProperty("object_type");
					TCProperty[] props = revision.getTCProperties(new String[] {
							"object_name", "item_id", "owning_user",
							"date_released", "item_revision_id" });

					if (props[3].getDateValue() != null) {
						Date dateString = props[3].getDateValue();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						String st = sdf.format(dateString);
						bean.setDate_released(st);
					} else {
						bean.setDate_released("没有发布");
					}
					bean.setObject_name(props[0].getStringValue());
					bean.setItem_id(props[1].getStringValue());
					bean.setOwning_user(props[2].getReferenceValueInString());
					bean.setRevision_id(props[4].getStringValue());

					if (typeMap.containsKey(prop.getDisplayableValue())) {
						beanList = (ArrayList<PsdBean>) typeMap.get(prop
								.getDisplayableValue());
					} else {
						beanList = new ArrayList<>();

					}
					beanList.add(bean);
					typeMap.put(prop.getDisplayableValue(), beanList);
					jfjdNameMap.put(folderName, typeMap);
					System.out.println("folderName =>" + folderName
							+ " count =" + typeMap.size());
				}
			}
		} else {
			// PsdBean bean = new PsdBean();
			// bean.setJFJD(projectNameString);
			// beanList.add(bean);
		}
		Set<Entry<String, Map<String, List<PsdBean>>>> setEntry = jfjdNameMap
				.entrySet();

		for (Entry<String, Map<String, List<PsdBean>>> entry1 : setEntry) {
			System.out.println(" Name =  " + entry1.getKey());
			Map<String, List<PsdBean>> map2 = entry1.getValue();
			if (map2 != null) {
				Set<Entry<String, List<PsdBean>>> setEntry2 = map2.entrySet();
				for (Entry<String, List<PsdBean>> entry2 : setEntry2) {
					System.out.println("Type = " + entry2.getKey());
					List<PsdBean> beanList = entry2.getValue();

					for (PsdBean bean : beanList) {
						System.out.println("Value = " + bean.toString());

					}
				}

			}
		}

	}

	/**
	 * 写入数据到excel报表
	 * 
	 * @throws TCException
	 * 
	 */
	public void writemsg(Map<String, Map<String, List<PsdBean>>> jfjdNameMap)
			throws IOException, TCException {

		// InputStream is =
		// PsdDialog.class.getResourceAsStream("OTS送样文件交付清单.xlsx");
		// class.getResourceAsStream默认是从此类所在的包下取资源

		file = new File(newpath);
		String outPutFile = file.getPath();
		outPutFile = outPutFile.replace(".xlsx", "_1.xlsx");
		InputStream is = new FileInputStream(file);

		Map<String, ModelBean> modelBeanMap = new HashMap<String, ModelBean>();
		ExcelUtil07.readModel(is, modelBeanMap);
		is.close();

		is = new FileInputStream(file);

		ExcelUtil07.writeExcel(jfjdNameMap, is, outPutFile, modelBeanMap,
				projectNameString);
		try {
			((TCComponentFolder) comment).add("contents",
					createDataSet(outPutFile));// 将数据集挂在目标文件夹下
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MessageBox.post("生成交付件点检报表成功", "提示", MessageBox.INFORMATION);
	}

	/**
	 * 导出的EXCEL命名引用到数据集中
	 * 
	 */
	public static TCComponentDataset createDataSet(String path) {
		dataset2 = null;
		TCTypeService type = session.getTypeService();
		TCComponentDatasetType datasettype;
		try {
			datasettype = (TCComponentDatasetType) type
					.getTypeComponent("Dataset");
			dataset2 = datasettype.create(name + "点检表.xlsx", "EXCEL IMPORT",
					"MSExcelX");
			// create( dataset_name , dataset_desc , dataset_type)；
			// create(excel文件名，描述，类型）；
			String p[] = new String[1];
			String n[] = new String[1];
			p[0] = path;// excel物理路径
			n[0] = "excel";// 命名的引用
			dataset2.setFiles(p, n);

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("数组越界！");
			e.printStackTrace();
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataset2;
	}

}
