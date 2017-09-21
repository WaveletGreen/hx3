package com.connor.hx3.plm.hxom018;

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

import com.connor.hx3.plm.hxom015.ExcelUtil07;
import com.connor.hx3.plm.hxom015.ModelBean;
import com.connor.hx3.plm.hxom015.PsdBean;
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

public class QTXJCDialog {
	private TCComponentDataset dataset;
	// private static String name;
	private AbstractAIFUIApplication app;
	private static TCSession session;
	private static InterfaceAIFComponent comment;
	private String folderUid;
	private String cxkey;
	private String searchName;
	private String[] keys;
	private String[] values;
	private InterfaceAIFComponent[] com;
	private TCComponentFolder folder;
	private ArrayList<TCComponentFolder> folderList;
	private String lclxName;
	private TCComponentFolder compFolder;
	private ArrayList<PsdBean> beanList;
	private Map<String, List<PsdBean>> typeMap;
	private Map<String, Map<String, List<PsdBean>>> jfjdNameMap;
	private String projectNameString = null;
	private String folderName;
	private TCComponent comp;
	private TCComponent[] em;
	private File dirFile;
	private String newpath;
	private static TCComponentDataset dataset2;
	private static File file;

	public QTXJCDialog(AbstractAIFUIApplication app, TCSession session)
			throws Exception {
		// this.name = name;
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

			comp = (TCComponent) comment;
			folder = (TCComponentFolder) comment;
			TCProperty lclx = folder.getTCProperty("hx3_lclx");
			lclxName = lclx.getDisplayValue().toString();

			// ��ѡ��
			TCPreferenceService service = ((TCSession) AIFUtility
					.getCurrentApplication().getSession())
					.getPreferenceService();
			String[] ColVal = service.getStringArray(
					TCPreferenceService.TC_preference_site,
					"hxWorkflowToAPQPSubmitTemplateMapping");
			if (ColVal == null || ColVal.length == 0) {
				MessageBox.post("��ѡ�"
						+ "hxWorkflowToAPQPSubmitTemplateMapping"
						+ "δ����ֵ��δ���壬����ϵϵͳ����Ա��", "��ʾ", MessageBox.WARNING);
				// this.disposeDialog();
				return;
			}
			// ��ѯ
			for (int a = 0; a < ColVal.length; a++) {
				if (ColVal[a].contains(lclxName)) {
					cxkey = ColVal[a].substring(ColVal[a].indexOf(":") + 1);// �õ���ѡ����"��"�����ֵ

					System.out.println(":�����ֵ������>" + cxkey);

					searchName = "hx_searchTemp";
					keys = new String[] { "ID" };
					String val = cxkey;

					values = new String[] { val };
					com = MethodUtil.searchComponentsCollection(
							(TCSession) app.getSession(), searchName, keys,
							values);
					if (com == null || com.length == 0) {
						return;
					}

					System.out.println(com.length);

					for (int k = 0; k < com.length; k++) {
						if (com[k] instanceof TCComponentItemRevision) {
							TCComponentItemRevision rev1 = (TCComponentItemRevision) com[k];
							em = rev1.getReferenceListProperty("TC_Attaches");
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
					getChildrenFolder(comp, 0);
					writemsg(jfjdNameMap);
				}
			}
			// }
			// }
		}
	}

	/**
	 * ����dataset�µ�excel
	 * 
	 * @param dataset
	 * @param dirFile
	 * @return
	 * @throws TCException
	 * @throws IOException
	 */
	private File expertFileToDir(TCComponentDataset dataset, File dirFile)
			throws TCException, IOException {
		TCComponentTcFile[] tcFiles = dataset.getTcFiles();
		File file;
		if (tcFiles != null && tcFiles.length > 0) {
			file = tcFiles[0].getFmsFile();
			if (file != null) {
				String path = file.getAbsolutePath();
				newpath = "D://��ƥ�����ݹ����嵥����.xlsx";
				copyFile(path, newpath);
			} else {
				System.out.println("�յ�file");
			}
		}
		return null;
	}

	/**
	 * �����ļ�
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
			if (oldfile.exists()) { // �ļ�����ʱ
				FileInputStream inStream = new FileInputStream(oldPath); // ����ԭ�ļ�
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // �ֽ��� �ļ���С
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.flush();
				fs.close();
			}
		} catch (Exception e) {
			System.out.println("���Ƶ����ļ���������");
			e.printStackTrace();

		}

	}

	/**
	 * �����ļ��в���ȡ����
	 * 
	 * @throws TCException
	 * @throws IOException
	 */
	public void getChildrenFolder(TCComponent comp, int index)
			throws TCException, IOException {

		if (comp == null) {
			MessageBox.post("��ʱ�����ϼ��ļ��в�����", "ʧ��", MessageBox.ERROR);
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

					System.out.print("�����ļ���");
				} else if (component instanceof TCComponentItem) {
					System.out.println("��ITEM����");
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
							bean.setDate_released("û�з���");
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
					System.out.println("��rev����");
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
						bean.setDate_released("û�з���");
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
	 * д�����ݵ�excel����
	 * 
	 * @throws TCException
	 * 
	 */
	public void writemsg(Map<String, Map<String, List<PsdBean>>> jfjdNameMap)
			throws IOException, TCException {

		// InputStream is =
		// PsdDialog.class.getResourceAsStream("OTS�����ļ������嵥.xlsx");
		// class.getResourceAsStreamĬ���ǴӴ������ڵİ���ȡ��Դ

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
			(getFolder((TCComponentFolder) comment)).add("contents",
					createDataSet(outPutFile));// �����ݼ�����Ŀ���ļ�����
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MessageBox.post("���ɽ��������Լ���ɹ�", "��ʾ", MessageBox.INFORMATION);
	}

	public TCComponentFolder getFolder(TCComponentFolder pFolder)
			throws TCException {
		TCComponentFolder folder = pFolder;
		AIFComponentContext[] contextS = pFolder.getChildren();
		if (contextS != null) {
			for (AIFComponentContext content : contextS) {
				if (!(content.getComponent() instanceof TCComponentFolder)) {
					continue;
				}
				if (((TCComponentFolder) content.getComponent())
						.getStringProperty("object_name").contains("��")) {
					AIFComponentContext[] contextSS = ((TCComponentFolder) content
							.getComponent()).getChildren();// ���������Լ��
					for (AIFComponentContext content1 : contextSS) {
						if (!(content1.getComponent() instanceof TCComponentFolder)) {
							continue;
						}
						if (((TCComponentFolder) content1.getComponent())
								.getStringProperty("object_name").contains(
										"���������Լ��")) {
							folder = (TCComponentFolder) content1
									.getComponent();
						}

					}

				}

			}
		}

		return folder;

	}

	/**
	 * ������EXCEL�������õ����ݼ���
	 * 
	 */
	public static TCComponentDataset createDataSet(String path) {
		dataset2 = null;
		TCTypeService type = session.getTypeService();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		TCComponentDatasetType datasettype;
		try {
			datasettype = (TCComponentDatasetType) type
					.getTypeComponent("Dataset");
			dataset2 = datasettype.create(
					"APQP���������Լ��" + sdf.format(new Date()) + ".xlsx",
					"EXCEL IMPORT", "MSExcelX");
			// create( dataset_name , dataset_desc , dataset_type)��
			// create(excel�ļ��������������ͣ���
			String p[] = new String[1];
			String n[] = new String[1];
			p[0] = path;// excel����·��
			n[0] = "excel";// ����������
			dataset2.setFiles(p, n);

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("����Խ�磡");
			e.printStackTrace();
		} catch (TCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataset2;
	}

}
