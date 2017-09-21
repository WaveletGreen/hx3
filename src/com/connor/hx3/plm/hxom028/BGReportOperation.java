package com.connor.hx3.plm.hxom028;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentForm;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.util.MessageBox;

/**
 * @copyRight ���ݿ��տƼ����޹�˾
 * @author ���� E-mail:hub@connor.net.cn
 * @date ����ʱ�䣺2016-10-27 ����12:05:54
 * @version v1.0
 * @parameter
 * @since
 * @return
 */

public class BGReportOperation extends AbstractAIFOperation {

	private List<TCComponentItemRevision> SQDSearchResultList;
	private HashMap<String, TCComponentItemRevision> TZDSearchResultList;
	private SimpleDateFormat sdf;
	// private List<BGReportBean> msgBeanList;
	private int opType;
	private String outPath;

	// private BGReportDialog dialog;

	public BGReportOperation(List<TCComponentItemRevision> SQDSearchResultList,
			HashMap<String, TCComponentItemRevision> TZDSearchResultList,
			int opType, String outPath) {
		this.SQDSearchResultList = SQDSearchResultList;
		this.TZDSearchResultList = TZDSearchResultList;
		this.sdf = new SimpleDateFormat("yyyy-M-dd");
		// this.msgBeanList = new ArrayList<>();
		this.opType = opType;
		this.outPath = outPath;
		// this.dialog = dialog;

	}

	/**
	 * �Ӳ�ѯ��������ȡ��Ϣ
	 * 
	 * @return
	 * @throws TCException
	 * @throws IOException
	 */
	public void getMsg(int opType) throws TCException, IOException {
		List<BGReportBean> msgBeanList = new ArrayList<>();
		switch (opType) {

		case 1:
			// BOTH
			TCProperty[][] propSList = TCComponentType.getTCPropertiesSet(
					SQDSearchResultList, new String[] { "item_id",
							"IMAN_master_form_rev", "HX3_gcggtz" });

			for (int i = 0; i < SQDSearchResultList.size(); i++) {
				BGReportBean bean = new BGReportBean();
				bean.sqdbh = propSList[i][0].getDisplayValue();
				TCProperty[] propS = propSList[i][1].getReferenceValueArray()[0]
						.getTCProperties(new String[] { "hx3_fqf", "hx3_cpxh" });
				bean.bgfqf = propS[0].getDisplayValue();
				bean.ggcpxh = propS[1].getDisplayValue();
				TCComponent[] tzdS = propSList[i][2].getReferenceValueArray();
				List<TCComponentForm> tzdList = new ArrayList<>();
				List<String> tzdIDList = new ArrayList<>();
				List<String> tzdReleaseList = new ArrayList<>();
				for (TCComponent tzdComp : tzdS) {
					TCComponentItemRevision rev = null;
					if (tzdComp instanceof TCComponentItem) {
						rev = ((TCComponentItem) tzdComp)
								.getLatestItemRevision();
					} else if (tzdComp instanceof TCComponentItemRevision) {
						rev = (TCComponentItemRevision) tzdComp;

					} else {
						continue;
					}
					if (TZDSearchResultList.containsKey(rev.getUid())) {
						TCProperty[] tzdPropS = rev
								.getTCProperties(new String[] { "item_id",
										"IMAN_master_form_rev", "date_released" });
						tzdIDList.add(tzdPropS[0].getDisplayValue());
						tzdList.add((TCComponentForm) tzdPropS[1]
								.getReferenceValueArray()[0]);
						if (tzdPropS[2].getDateValue() != null) {
							tzdReleaseList.add(sdf.format(tzdPropS[2]
									.getDateValue()));
						} else {
							tzdReleaseList.add("");
						}
					}

				}
				TCProperty[][] tzdPropSList = TCComponentType
						.getTCPropertiesSet(tzdList, new String[] { //
								"hx3_bgfl",// ������� 0
										"hx3_ggyy", // ����ԭ��
										"hx3_lbjth1",// �㲿��ͼ��
										"hx3_lbjmc",// �㲿������
										"hx3_tztbgg", // ͼֽͬ������
										"hx3_tzsm", // ͼֽ˵�� 5
										"hx3_gytbgg",// ����ͬ������
										"hx3_mjtbgg", // ģ��ͬ������
										"hx3_jj", // ���ͬ������
										"hx3_sbcs",// �豸��������
										"hx3_jgdyqwjtbgx",// �����ӡ���ļ�ͬ������ 10
										"hx3_wftztbgx",// �ⷢͼֽͬ������
										"hx3_djtztbgx", // ��Э�ӹ�ͼͬ������
										"hx3_dfmea", // DFMEAͬ������
										"hx3_pfmea",// PFMEAͬ������
										"hx3_kzjh", // ���Ƽƻ�ͬ������15
										"hx3_bz",// ��װͬ������
										"hx3_erptbgg",// ERPͬ������
										"hx3_gdtbtz" // �ƻ���ERPͬ������
								});

				if (tzdPropSList != null && tzdPropSList.length > 0) {
					for (int w = 0; w < tzdList.size(); w++) {
						BGReportBean tzdBean = new BGReportBean();
						tzdBean.sqdbh = bean.sqdbh;
						tzdBean.bgfqf = bean.bgfqf;
						tzdBean.ggcpxh = bean.ggcpxh;
						tzdBean.ggdbh = tzdIDList.get(w);
						tzdBean.ggsj = tzdReleaseList.get(w);
						tzdBean.bglb = tzdPropSList[w][0].getDisplayValue();
						tzdBean.ggyy = tzdPropSList[w][1].getDisplayValue();
						tzdBean.lbjth = tzdPropSList[w][2].getDisplayValue();// �㲿��ͼ��
						tzdBean.lbjmc = tzdPropSList[w][3].getDisplayValue();// �㲿������
						tzdBean.tztbgg = tzdPropSList[w][4].getDisplayValue();// ͼֽͬ������
																				// 10
						tzdBean.tzsm = tzdPropSList[w][5].getDisplayValue();// ͼֽ˵��
						tzdBean.gytbgg = tzdPropSList[w][6].getDisplayValue();// ����ͬ������
						tzdBean.mjtbgg = tzdPropSList[w][7].getDisplayValue();// ģ��ͬ������
						tzdBean.jjtbtz = tzdPropSList[w][8].getDisplayValue();// ���ͬ������
						tzdBean.sbcstz = tzdPropSList[w][9].getDisplayValue();// �豸��������
																				// 15
						tzdBean.jgdywjtbgx = tzdPropSList[w][10]
								.getDisplayValue();// �����ӡ���ļ�ͬ������
						tzdBean.wftztbgx = tzdPropSList[w][11]
								.getDisplayValue();// �ⷢͼֽͬ������
						tzdBean.wxjgttbgx = tzdPropSList[w][12]
								.getDisplayValue();// ��Э�ӹ�ͼͬ������
						tzdBean.dfmeatbgg = tzdPropSList[w][13]
								.getDisplayValue();// DFMEAͬ������
						tzdBean.pfmeagg = tzdPropSList[w][14].getDisplayValue();// PFMEAͬ������
																				// 20
						tzdBean.kzjhtbgg = tzdPropSList[w][15]
								.getDisplayValue();// ���Ƽƻ�ͬ������
						tzdBean.bztbgg = tzdPropSList[w][16].getDisplayValue();// ��װͬ������
						tzdBean.erptbtz = tzdPropSList[w][17].getDisplayValue();// ERPͬ������
						tzdBean.jhberptbtz = tzdPropSList[w][18]
								.getDisplayValue();// �ƻ���ERPͬ������
						msgBeanList.add(tzdBean);

					}

				} else {
					msgBeanList.add(bean);
				}

			}

			break;
		case 2:
			// SQD
			propSList = TCComponentType.getTCPropertiesSet(SQDSearchResultList,
					new String[] { "item_id", "IMAN_master_form_rev",
							"HX3_gcggtz" });

			for (int i = 0; i < SQDSearchResultList.size(); i++) {
				BGReportBean bean = new BGReportBean();
				bean.sqdbh = propSList[i][0].getDisplayValue();
				TCProperty[] propS = propSList[i][1].getReferenceValueArray()[0]
						.getTCProperties(new String[] { "hx3_fqf", "hx3_cpxh" });
				bean.bgfqf = propS[0].getDisplayValue();
				bean.ggcpxh = propS[1].getDisplayValue();
				TCComponent[] tzdS = propSList[i][2].getReferenceValueArray();
				List<TCComponentForm> tzdList = new ArrayList<>();
				List<String> tzdIDList = new ArrayList<>();
				List<String> tzdReleaseList = new ArrayList<>();
				for (TCComponent tzdComp : tzdS) {
					TCComponentItemRevision rev = null;
					if (tzdComp instanceof TCComponentItem) {
						rev = ((TCComponentItem) tzdComp)
								.getLatestItemRevision();
					} else if (tzdComp instanceof TCComponentItemRevision) {
						rev = (TCComponentItemRevision) tzdComp;

					} else {
						continue;
					}
					// if (TZDSearchResultList.containsKey(rev.getUid())) {
					TCProperty[] tzdPropS = rev
							.getTCProperties(new String[] { "item_id",
									"IMAN_master_form_rev", "date_released" });
					tzdIDList.add(tzdPropS[0].getDisplayValue());
					tzdList.add((TCComponentForm) tzdPropS[1]
							.getReferenceValueArray()[0]);
					if (tzdPropS[2].getDateValue() != null) {
						tzdReleaseList.add(sdf.format(tzdPropS[2]
								.getDateValue()));
					} else {
						tzdReleaseList.add("");
					}
					// }

				}
				TCProperty[][] tzdPropSList = TCComponentType
						.getTCPropertiesSet(tzdList, new String[] { //
								"hx3_bgfl",// ������� 0
										"hx3_ggyy", // ����ԭ��
										"hx3_lbjth1",// �㲿��ͼ��
										"hx3_lbjmc",// �㲿������
										"hx3_tztbgg", // ͼֽͬ������
										"hx3_tzsm", // ͼֽ˵�� 5
										"hx3_gytbgg",// ����ͬ������
										"hx3_mjtbgg", // ģ��ͬ������
										"hx3_jj", // ���ͬ������
										"hx3_sbcs",// �豸��������
										"hx3_jgdyqwjtbgx",// �����ӡ���ļ�ͬ������ 10
										"hx3_wftztbgx",// �ⷢͼֽͬ������
										"hx3_djtztbgx", // ��Э�ӹ�ͼͬ������
										"hx3_dfmea", // DFMEAͬ������
										"hx3_pfmea",// PFMEAͬ������
										"hx3_kzjh", // ���Ƽƻ�ͬ������15
										"hx3_bz",// ��װͬ������
										"hx3_erptbgg",// ERPͬ������
										"hx3_gdtbtz" // �ƻ���ERPͬ������
								});

				if (tzdPropSList != null && tzdPropSList.length > 0) {
					for (int w = 0; w < tzdList.size(); w++) {
						BGReportBean tzdBean = new BGReportBean();
						tzdBean.sqdbh = bean.sqdbh;
						tzdBean.bgfqf = bean.bgfqf;
						tzdBean.ggcpxh = bean.ggcpxh;
						tzdBean.ggdbh = tzdIDList.get(w);
						tzdBean.ggsj = tzdReleaseList.get(w);
						tzdBean.bglb = tzdPropSList[w][0] == null ? ""
								: tzdPropSList[w][0].getDisplayValue();
						tzdBean.ggyy = tzdPropSList[w][1] == null ? ""
								: tzdPropSList[w][1].getDisplayValue();
						tzdBean.lbjth = tzdPropSList[w][2].getDisplayValue();// �㲿��ͼ��
						tzdBean.lbjmc = tzdPropSList[w][3].getDisplayValue();// �㲿������
						tzdBean.tztbgg = tzdPropSList[w][4].getDisplayValue();// ͼֽͬ������
																				// 10
						tzdBean.tzsm = tzdPropSList[w][5].getDisplayValue();// ͼֽ˵��
						tzdBean.gytbgg = tzdPropSList[w][6].getDisplayValue();// ����ͬ������
						tzdBean.mjtbgg = tzdPropSList[w][7].getDisplayValue();// ģ��ͬ������
						tzdBean.jjtbtz = tzdPropSList[w][8].getDisplayValue();// ���ͬ������
						tzdBean.sbcstz = tzdPropSList[w][9].getDisplayValue();// �豸��������
																				// 15
						tzdBean.jgdywjtbgx = tzdPropSList[w][10]
								.getDisplayValue();// �����ӡ���ļ�ͬ������
						tzdBean.wftztbgx = tzdPropSList[w][11]
								.getDisplayValue();// �ⷢͼֽͬ������
						tzdBean.wxjgttbgx = tzdPropSList[w][12]
								.getDisplayValue();// ��Э�ӹ�ͼͬ������
						tzdBean.dfmeatbgg = tzdPropSList[w][13]
								.getDisplayValue();// DFMEAͬ������
						tzdBean.pfmeagg = tzdPropSList[w][14].getDisplayValue();// PFMEAͬ������
																				// 20
						tzdBean.kzjhtbgg = tzdPropSList[w][15]
								.getDisplayValue();// ���Ƽƻ�ͬ������
						tzdBean.bztbgg = tzdPropSList[w][16].getDisplayValue();// ��װͬ������
						tzdBean.erptbtz = tzdPropSList[w][17].getDisplayValue();// ERPͬ������
						tzdBean.jhberptbtz = tzdPropSList[w][18]
								.getDisplayValue();// �ƻ���ERPͬ������
						msgBeanList.add(tzdBean);

					}

				} else {
					msgBeanList.add(bean);
				}
			}

			// for (TCComponentItemRevision qddRev : SQDSearchResultList) {
			//
			// }

			break;
		case 3:
			// TZD
			Set<Entry<String, TCComponentItemRevision>> entrySet = TZDSearchResultList
					.entrySet();
			List<TCComponentForm> tzdList = new ArrayList<>();
			List<String> tzdIDList = new ArrayList<>();
			List<String> tzdReleaseList = new ArrayList<>();
			List<String> sqdbhList = new ArrayList<>();
			List<String> bgfqfList = new ArrayList<>();
			List<String> ggcpxhList = new ArrayList<>();
			for (Entry<String, TCComponentItemRevision> entry : entrySet) {
				AIFComponentContext[] context = entry
						.getValue()
						.getItem()
						.whereReferencedByTypeRelation(
								new String[] { "HX3_GCGGSQDRevision" },
								new String[] { "HX3_gcggtz" });
				if (context != null && context.length > 0) {
					TCProperty[] propS = ((TCComponentItemRevision) context[0]
							.getComponent()).getTCProperties(new String[] {
							"item_id", "IMAN_master_form_rev" });
					sqdbhList.add(propS[0].getDisplayValue());
					TCProperty[] propSS = propS[1].getReferenceValueArray()[0]
							.getTCProperties(new String[] { "hx3_fqf",
									"hx3_cpxh" });
					bgfqfList.add(propSS[0].getDisplayValue());
					ggcpxhList.add(propSS[1].getDisplayValue());
				} else {
					sqdbhList.add("");
					bgfqfList.add("");
					ggcpxhList.add("");
				}

				TCProperty[] tzdPropS = entry.getValue().getTCProperties(
						new String[] { "item_id", "IMAN_master_form_rev",
								"date_released" });
				tzdIDList.add(tzdPropS[0].getDisplayValue());
				tzdList.add((TCComponentForm) tzdPropS[1]
						.getReferenceValueArray()[0]);
				// tzdList.add(entry.getValue());
				if (tzdPropS[2].getDateValue() != null) {
					//
					tzdReleaseList.add(sdf.format(tzdPropS[2].getDateValue()));
				} else {
					//
					tzdReleaseList.add("");
				}

			}

			TCProperty[][] tzdPropSList = TCComponentType.getTCPropertiesSet(
					tzdList, new String[] { //
					"hx3_bgfl",// ������� 0
							"hx3_ggyy", // ����ԭ��
							"hx3_lbjth1",// �㲿��ͼ��
							"hx3_lbjmc",// �㲿������
							"hx3_tztbgg", // ͼֽͬ������
							"hx3_tzsm", // ͼֽ˵�� 5
							"hx3_gytbgg",// ����ͬ������
							"hx3_mjtbgg", // ģ��ͬ������
							"hx3_jj", // ���ͬ������
							"hx3_sbcs",// �豸��������
							"hx3_jgdyqwjtbgx",// �����ӡ���ļ�ͬ������ 10
							"hx3_wftztbgx",// �ⷢͼֽͬ������
							"hx3_djtztbgx", // ��Э�ӹ�ͼͬ������
							"hx3_dfmea", // DFMEAͬ������
							"hx3_pfmea",// PFMEAͬ������
							"hx3_kzjh", // ���Ƽƻ�ͬ������15
							"hx3_bz",// ��װͬ������
							"hx3_erptbgg",// ERPͬ������
							"hx3_gdtbtz" // �ƻ���ERPͬ������
					});

			if (tzdPropSList != null && tzdPropSList.length > 0) {
				for (int w = 0; w < tzdList.size(); w++) {
					BGReportBean tzdBean = new BGReportBean();
					tzdBean.sqdbh = sqdbhList.get(w);// bean.sqdbh;
					tzdBean.bgfqf = bgfqfList.get(w);// bean.bgfqf;
					tzdBean.ggcpxh = ggcpxhList.get(w);// bean.ggcpxh;
					tzdBean.ggdbh = tzdIDList.get(w);
					tzdBean.ggsj = tzdReleaseList.get(w);
					tzdBean.bglb = tzdPropSList[w][0].getDisplayValue();
					tzdBean.ggyy = tzdPropSList[w][1].getDisplayValue();
					tzdBean.lbjth = tzdPropSList[w][2].getDisplayValue();// �㲿��ͼ��
					tzdBean.lbjmc = tzdPropSList[w][3].getDisplayValue();// �㲿������
					tzdBean.tztbgg = tzdPropSList[w][4].getDisplayValue();// ͼֽͬ������
																			// 10
					tzdBean.tzsm = tzdPropSList[w][5].getDisplayValue();// ͼֽ˵��
					tzdBean.gytbgg = tzdPropSList[w][6].getDisplayValue();// ����ͬ������
					tzdBean.mjtbgg = tzdPropSList[w][7].getDisplayValue();// ģ��ͬ������
					tzdBean.jjtbtz = tzdPropSList[w][8].getDisplayValue();// ���ͬ������
					tzdBean.sbcstz = tzdPropSList[w][9].getDisplayValue();// �豸��������
																			// 15
					tzdBean.jgdywjtbgx = tzdPropSList[w][10].getDisplayValue();// �����ӡ���ļ�ͬ������
					tzdBean.wftztbgx = tzdPropSList[w][11].getDisplayValue();// �ⷢͼֽͬ������
					tzdBean.wxjgttbgx = tzdPropSList[w][12].getDisplayValue();// ��Э�ӹ�ͼͬ������
					tzdBean.dfmeatbgg = tzdPropSList[w][13].getDisplayValue();// DFMEAͬ������
					tzdBean.pfmeagg = tzdPropSList[w][14].getDisplayValue();// PFMEAͬ������
																			// 20
					tzdBean.kzjhtbgg = tzdPropSList[w][15].getDisplayValue();// ���Ƽƻ�ͬ������
					tzdBean.bztbgg = tzdPropSList[w][16].getDisplayValue();// ��װͬ������
					tzdBean.erptbtz = tzdPropSList[w][17].getDisplayValue();// ERPͬ������
					tzdBean.jhberptbtz = tzdPropSList[w][18].getDisplayValue();// �ƻ���ERPͬ������
					msgBeanList.add(tzdBean);

				}

			}

			break;
		default:

			break;

		}
		System.out.println("msgBeanList.size ===> " + msgBeanList.size());
		InputStream is = BGReportOperation.class
				.getResourceAsStream("���ͳ�Ʊ���_ģ��.xlsx");
		BGReportExcelUtil07.writeBGReportMsg(is, outPath + "\\���ͳ�Ʊ���.xlsx",
				msgBeanList);
		// return msgBeanList;

	}

	@Override
	public void executeOperation() throws Exception {
		// TODO Auto-generated method stub

		try {
			getMsg(this.opType);
			MessageBox.post("��������ɹ�", "�ɹ�", MessageBox.INFORMATION);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			MessageBox.post("��������ʧ��", "ʧ��", MessageBox.ERROR);
			e.printStackTrace();
		}

	}

}
