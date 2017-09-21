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
 * @copyRight 杭州康勒科技有限公司
 * @author 作者 E-mail:hub@connor.net.cn
 * @date 创建时间：2016-10-27 上午12:05:54
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
	 * 从查询对象中提取信息
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
								"hx3_bgfl",// 更改类别 0
										"hx3_ggyy", // 更改原因
										"hx3_lbjth1",// 零部件图号
										"hx3_lbjmc",// 零部件名称
										"hx3_tztbgg", // 图纸同步更改
										"hx3_tzsm", // 图纸说明 5
										"hx3_gytbgg",// 工艺同步更改
										"hx3_mjtbgg", // 模具同步更改
										"hx3_jj", // 检具同步调整
										"hx3_sbcs",// 设备参数调整
										"hx3_jgdyqwjtbgx",// 激光打印区文件同步更新 10
										"hx3_wftztbgx",// 外发图纸同步更新
										"hx3_djtztbgx", // 外协加工图同步更新
										"hx3_dfmea", // DFMEA同步更改
										"hx3_pfmea",// PFMEA同步更改
										"hx3_kzjh", // 控制计划同步更改15
										"hx3_bz",// 包装同步更改
										"hx3_erptbgg",// ERP同步调整
										"hx3_gdtbtz" // 计划部ERP同步调整
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
						tzdBean.lbjth = tzdPropSList[w][2].getDisplayValue();// 零部件图号
						tzdBean.lbjmc = tzdPropSList[w][3].getDisplayValue();// 零部件名称
						tzdBean.tztbgg = tzdPropSList[w][4].getDisplayValue();// 图纸同步更改
																				// 10
						tzdBean.tzsm = tzdPropSList[w][5].getDisplayValue();// 图纸说明
						tzdBean.gytbgg = tzdPropSList[w][6].getDisplayValue();// 工艺同步更改
						tzdBean.mjtbgg = tzdPropSList[w][7].getDisplayValue();// 模具同步更改
						tzdBean.jjtbtz = tzdPropSList[w][8].getDisplayValue();// 检具同步调整
						tzdBean.sbcstz = tzdPropSList[w][9].getDisplayValue();// 设备参数调整
																				// 15
						tzdBean.jgdywjtbgx = tzdPropSList[w][10]
								.getDisplayValue();// 激光打印区文件同步更新
						tzdBean.wftztbgx = tzdPropSList[w][11]
								.getDisplayValue();// 外发图纸同步更新
						tzdBean.wxjgttbgx = tzdPropSList[w][12]
								.getDisplayValue();// 外协加工图同步更新
						tzdBean.dfmeatbgg = tzdPropSList[w][13]
								.getDisplayValue();// DFMEA同步更改
						tzdBean.pfmeagg = tzdPropSList[w][14].getDisplayValue();// PFMEA同步更改
																				// 20
						tzdBean.kzjhtbgg = tzdPropSList[w][15]
								.getDisplayValue();// 控制计划同步更改
						tzdBean.bztbgg = tzdPropSList[w][16].getDisplayValue();// 包装同步更改
						tzdBean.erptbtz = tzdPropSList[w][17].getDisplayValue();// ERP同步调整
						tzdBean.jhberptbtz = tzdPropSList[w][18]
								.getDisplayValue();// 计划部ERP同步调整
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
								"hx3_bgfl",// 更改类别 0
										"hx3_ggyy", // 更改原因
										"hx3_lbjth1",// 零部件图号
										"hx3_lbjmc",// 零部件名称
										"hx3_tztbgg", // 图纸同步更改
										"hx3_tzsm", // 图纸说明 5
										"hx3_gytbgg",// 工艺同步更改
										"hx3_mjtbgg", // 模具同步更改
										"hx3_jj", // 检具同步调整
										"hx3_sbcs",// 设备参数调整
										"hx3_jgdyqwjtbgx",// 激光打印区文件同步更新 10
										"hx3_wftztbgx",// 外发图纸同步更新
										"hx3_djtztbgx", // 外协加工图同步更新
										"hx3_dfmea", // DFMEA同步更改
										"hx3_pfmea",// PFMEA同步更改
										"hx3_kzjh", // 控制计划同步更改15
										"hx3_bz",// 包装同步更改
										"hx3_erptbgg",// ERP同步调整
										"hx3_gdtbtz" // 计划部ERP同步调整
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
						tzdBean.lbjth = tzdPropSList[w][2].getDisplayValue();// 零部件图号
						tzdBean.lbjmc = tzdPropSList[w][3].getDisplayValue();// 零部件名称
						tzdBean.tztbgg = tzdPropSList[w][4].getDisplayValue();// 图纸同步更改
																				// 10
						tzdBean.tzsm = tzdPropSList[w][5].getDisplayValue();// 图纸说明
						tzdBean.gytbgg = tzdPropSList[w][6].getDisplayValue();// 工艺同步更改
						tzdBean.mjtbgg = tzdPropSList[w][7].getDisplayValue();// 模具同步更改
						tzdBean.jjtbtz = tzdPropSList[w][8].getDisplayValue();// 检具同步调整
						tzdBean.sbcstz = tzdPropSList[w][9].getDisplayValue();// 设备参数调整
																				// 15
						tzdBean.jgdywjtbgx = tzdPropSList[w][10]
								.getDisplayValue();// 激光打印区文件同步更新
						tzdBean.wftztbgx = tzdPropSList[w][11]
								.getDisplayValue();// 外发图纸同步更新
						tzdBean.wxjgttbgx = tzdPropSList[w][12]
								.getDisplayValue();// 外协加工图同步更新
						tzdBean.dfmeatbgg = tzdPropSList[w][13]
								.getDisplayValue();// DFMEA同步更改
						tzdBean.pfmeagg = tzdPropSList[w][14].getDisplayValue();// PFMEA同步更改
																				// 20
						tzdBean.kzjhtbgg = tzdPropSList[w][15]
								.getDisplayValue();// 控制计划同步更改
						tzdBean.bztbgg = tzdPropSList[w][16].getDisplayValue();// 包装同步更改
						tzdBean.erptbtz = tzdPropSList[w][17].getDisplayValue();// ERP同步调整
						tzdBean.jhberptbtz = tzdPropSList[w][18]
								.getDisplayValue();// 计划部ERP同步调整
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
					"hx3_bgfl",// 更改类别 0
							"hx3_ggyy", // 更改原因
							"hx3_lbjth1",// 零部件图号
							"hx3_lbjmc",// 零部件名称
							"hx3_tztbgg", // 图纸同步更改
							"hx3_tzsm", // 图纸说明 5
							"hx3_gytbgg",// 工艺同步更改
							"hx3_mjtbgg", // 模具同步更改
							"hx3_jj", // 检具同步调整
							"hx3_sbcs",// 设备参数调整
							"hx3_jgdyqwjtbgx",// 激光打印区文件同步更新 10
							"hx3_wftztbgx",// 外发图纸同步更新
							"hx3_djtztbgx", // 外协加工图同步更新
							"hx3_dfmea", // DFMEA同步更改
							"hx3_pfmea",// PFMEA同步更改
							"hx3_kzjh", // 控制计划同步更改15
							"hx3_bz",// 包装同步更改
							"hx3_erptbgg",// ERP同步调整
							"hx3_gdtbtz" // 计划部ERP同步调整
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
					tzdBean.lbjth = tzdPropSList[w][2].getDisplayValue();// 零部件图号
					tzdBean.lbjmc = tzdPropSList[w][3].getDisplayValue();// 零部件名称
					tzdBean.tztbgg = tzdPropSList[w][4].getDisplayValue();// 图纸同步更改
																			// 10
					tzdBean.tzsm = tzdPropSList[w][5].getDisplayValue();// 图纸说明
					tzdBean.gytbgg = tzdPropSList[w][6].getDisplayValue();// 工艺同步更改
					tzdBean.mjtbgg = tzdPropSList[w][7].getDisplayValue();// 模具同步更改
					tzdBean.jjtbtz = tzdPropSList[w][8].getDisplayValue();// 检具同步调整
					tzdBean.sbcstz = tzdPropSList[w][9].getDisplayValue();// 设备参数调整
																			// 15
					tzdBean.jgdywjtbgx = tzdPropSList[w][10].getDisplayValue();// 激光打印区文件同步更新
					tzdBean.wftztbgx = tzdPropSList[w][11].getDisplayValue();// 外发图纸同步更新
					tzdBean.wxjgttbgx = tzdPropSList[w][12].getDisplayValue();// 外协加工图同步更新
					tzdBean.dfmeatbgg = tzdPropSList[w][13].getDisplayValue();// DFMEA同步更改
					tzdBean.pfmeagg = tzdPropSList[w][14].getDisplayValue();// PFMEA同步更改
																			// 20
					tzdBean.kzjhtbgg = tzdPropSList[w][15].getDisplayValue();// 控制计划同步更改
					tzdBean.bztbgg = tzdPropSList[w][16].getDisplayValue();// 包装同步更改
					tzdBean.erptbtz = tzdPropSList[w][17].getDisplayValue();// ERP同步调整
					tzdBean.jhberptbtz = tzdPropSList[w][18].getDisplayValue();// 计划部ERP同步调整
					msgBeanList.add(tzdBean);

				}

			}

			break;
		default:

			break;

		}
		System.out.println("msgBeanList.size ===> " + msgBeanList.size());
		InputStream is = BGReportOperation.class
				.getResourceAsStream("变更统计报表_模板.xlsx");
		BGReportExcelUtil07.writeBGReportMsg(is, outPath + "\\变更统计报表.xlsx",
				msgBeanList);
		// return msgBeanList;

	}

	@Override
	public void executeOperation() throws Exception {
		// TODO Auto-generated method stub

		try {
			getMsg(this.opType);
			MessageBox.post("导出报表成功", "成功", MessageBox.INFORMATION);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			MessageBox.post("导出报表失败", "失败", MessageBox.ERROR);
			e.printStackTrace();
		}

	}

}
