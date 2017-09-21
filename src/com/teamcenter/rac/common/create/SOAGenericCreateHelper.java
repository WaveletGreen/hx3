// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 

package com.teamcenter.rac.common.create;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.teamcenter.rac.common.Activator;
import com.teamcenter.rac.kernel.BOCreatePropertyDescriptor;
import com.teamcenter.rac.kernel.SoaUtil;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.services.ISessionService;
import com.teamcenter.services.rac.core.DataManagementService;
import com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.Property;

// Referenced classes of package com.teamcenter.rac.common.create:
//            IBOCreateDefinition, ICreateInstanceInput

public class SOAGenericCreateHelper {

	public SOAGenericCreateHelper() {
	}

	public static void getINFO(
			com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput createinput) {
		// TODO 11111
		System.out.println("boName = " + createinput.boName);

		System.out.println("---------11111----------------");
		Map map = createinput.compoundCreateInput;
		if (map != null && !map.isEmpty()) {
			String[] keyS = (String[]) map.keySet().toArray(
					new String[map.size()]);
			for (String key : keyS) {
				System.out.println("compoundCreateInput | key =>" + key
						+ "|value=>" + map.get(key));

				com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput[] revisionIput = (CreateInput[]) map
						.get(key);
				if (revisionIput != null && revisionIput.length > 0) {
					System.out.println(revisionIput.length);
					for (CreateInput ci : revisionIput) {
						getINFO(ci);
					}

				}
			}
		}
		System.out.println("--------22222-----------------------");

		map = createinput.stringProps;
		if (map != null && !map.isEmpty()) {
			String[] keyS = (String[]) map.keySet().toArray(
					new String[map.size()]);
			for (String key : keyS) {
				System.out.println("stringProps | key =>" + key + "|value=>"
						+ map.get(key));
			}
		}

		map = createinput.tagProps;
		if (map != null && !map.isEmpty()) {
			String[] keyS = (String[]) map.keySet().toArray(
					new String[map.size()]);
			for (String key : keyS) {
				System.out.println("tagProps | key =>" + key + "|value=>"
						+ map.get(key));
			}
		}

		map = createinput.boolProps;
		if (map != null && !map.isEmpty()) {
			String[] keyS = (String[]) map.keySet().toArray(
					new String[map.size()]);
			for (String key : keyS) {
				System.out.println("boolProps | key =>" + key + "|value=>"
						+ map.get(key));
			}
		}

	}

	public static List create(TCSession tcsession,
			IBOCreateDefinition ibocreatedefinition, List list)
			throws TCException {
		try {
			ArrayList arraylist = null;
			if (tcsession != null && ibocreatedefinition != null
					&& list != null && !list.isEmpty()) {
				logCreateInputs(list);
				DataManagementService datamanagementservice = DataManagementService
						.getService(tcsession);
				if (datamanagementservice != null) {
					ArrayList arraylist1 = new ArrayList(list);
					com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput createinput = getCreateInput(
							ibocreatedefinition, arraylist1);
					getINFO(createinput);
					if (createinput != null) {
						com.teamcenter.services.rac.core._2008_06.DataManagement.CreateIn createin = new com.teamcenter.services.rac.core._2008_06.DataManagement.CreateIn();
						createin.clientId = "Test";
						createin.data = createinput;
						logger.debug("*** CreateInput structure: ***");
						logRootInput(createinput, "");
						com.teamcenter.services.rac.core._2008_06.DataManagement.CreateResponse createresponse = datamanagementservice
								.createObjects(new com.teamcenter.services.rac.core._2008_06.DataManagement.CreateIn[] { createin });
						SoaUtil.handlePartialErrors(createresponse.serviceData,
								null, null, true);
						com.teamcenter.services.rac.core._2008_06.DataManagement.CreateOut acreateout[] = createresponse.output;
						if (acreateout != null && acreateout.length > 0) {
							arraylist = new ArrayList(0);
							com.teamcenter.services.rac.core._2008_06.DataManagement.CreateOut acreateout1[];
							int j = (acreateout1 = acreateout).length;
							for (int i = 0; i < j; i++) {
								com.teamcenter.services.rac.core._2008_06.DataManagement.CreateOut createout = acreateout1[i];
								TCComponent atccomponent[] = createout.objects;
								if (atccomponent != null
										&& atccomponent.length > 0)
									arraylist.addAll(Arrays
											.asList(atccomponent));
							}

						}
					}
				}
			}
			return arraylist;
		} catch (Exception exception) {
			if (exception instanceof TCException)
				throw (TCException) exception;
			else
				throw new TCException(exception);
		}
	}

	public static List createRelateAndSubmitToWorkflow(TCSession tcsession,
			IBOCreateDefinition ibocreatedefinition, List list, Map map,
			Map map1, TCComponent tccomponent, String s) throws TCException {
		try {
			ArrayList arraylist = null;
			if (tcsession != null && ibocreatedefinition != null
					&& list != null && !list.isEmpty()) {
				logCreateInputs(list);
				DataManagementService datamanagementservice = DataManagementService
						.getService(tcsession);
				if (datamanagementservice != null) {
					ArrayList arraylist1 = new ArrayList(list);
					com.teamcenter.services.rac.core._2015_07.DataManagement.CreateInput2 createinput2 = getCreateInput2(
							ibocreatedefinition, arraylist1);
					if (createinput2 != null) {
						com.teamcenter.services.rac.core._2015_07.DataManagement.CreateIn2 createin2 = new com.teamcenter.services.rac.core._2015_07.DataManagement.CreateIn2();
						createin2.clientId = "RAC_Client";
						createin2.createData = createinput2;
						if (s != null)
							createin2.pasteProp = s;
						if (tccomponent != null)
							createin2.targetObject = tccomponent;
						if (map != null)
							createin2.dataToBeRelated = map;
						if (map1 != null)
							createin2.workflowData = map1;
						com.teamcenter.services.rac.core._2008_06.DataManagement.CreateResponse createresponse = datamanagementservice
								.createRelateAndSubmitObjects2(new com.teamcenter.services.rac.core._2015_07.DataManagement.CreateIn2[] { createin2 });
						SoaUtil.handlePartialErrors(createresponse.serviceData,
								null, null, true);
						com.teamcenter.services.rac.core._2008_06.DataManagement.CreateOut acreateout[] = createresponse.output;
						if (acreateout != null && acreateout.length > 0) {
							arraylist = new ArrayList(0);
							com.teamcenter.services.rac.core._2008_06.DataManagement.CreateOut acreateout1[];
							int j = (acreateout1 = acreateout).length;
							for (int i = 0; i < j; i++) {
								com.teamcenter.services.rac.core._2008_06.DataManagement.CreateOut createout = acreateout1[i];
								TCComponent atccomponent[] = createout.objects;
								if (atccomponent != null
										&& atccomponent.length > 0)
									arraylist.addAll(Arrays
											.asList(atccomponent));
							}

						}
					}
				}
			}
			return arraylist;
		} catch (TCException tcexception) {
			throw tcexception;
		} catch (Exception exception) {
			throw new TCException(exception);
		}
	}

	private static com.teamcenter.services.rac.core._2015_07.DataManagement.CreateInput2 getCreateInput2(
			IBOCreateDefinition ibocreatedefinition, List list)
			throws NumberFormatException {
		com.teamcenter.services.rac.core._2015_07.DataManagement.CreateInput2 createinput2 = new com.teamcenter.services.rac.core._2015_07.DataManagement.CreateInput2();
		createinput2.boName = ibocreatedefinition.getTypeName();
		walkAndCreateInputStructure(createinput2, ibocreatedefinition, list);
		return createinput2;
	}

	public static com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput getCreateInput(
			IBOCreateDefinition ibocreatedefinition, List list)
			throws NumberFormatException {
		com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput createinput = new com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput();
		createinput.boName = ibocreatedefinition.getTypeName();
		walkAndCreateInputStructure(createinput, ibocreatedefinition, list);
		return createinput;
	}

	private static void walkAndCreateInputStructure(Object obj,
			IBOCreateDefinition ibocreatedefinition, List list)
			throws NumberFormatException {
		if (ibocreatedefinition != null && list != null && !list.isEmpty()) {
			List list1 = findCreateInputsForCreateDef(ibocreatedefinition, list);
			if (list1 != null && !list1.isEmpty()) {
				updateInputStructure(obj, list1);
				ICreateInstanceInput icreateinstanceinput;
				for (Iterator iterator = list1.iterator(); iterator.hasNext(); walkAndCreateNestedInputStructure(
						obj, icreateinstanceinput, false)) {
					icreateinstanceinput = (ICreateInstanceInput) iterator
							.next();
					list.remove(icreateinstanceinput);
				}

			}
			Map map = ibocreatedefinition.getSecondaryCreateDefinitions();
			if (map != null && !map.isEmpty() && !list.isEmpty()) {
				String as[] = (String[]) map.keySet().toArray(
						new String[map.size()]);
				String as1[];
				int j = (as1 = as).length;
				for (int i = 0; i < j; i++) {
					String s = as1[i];
					List list2 = (List) map.get(s);
					if (list2 != null && !list2.isEmpty()) {
						ArrayList arraylist = new ArrayList(list2.size());
						for (Iterator iterator1 = list2.iterator(); iterator1
								.hasNext();) {
							IBOCreateDefinition ibocreatedefinition1 = (IBOCreateDefinition) iterator1
									.next();
							if (createInstanceInputAvailableForCreateDef(
									ibocreatedefinition1, list)) {
								com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput createinput = new com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput();
								createinput.boName = ibocreatedefinition1
										.getTypeName();
								arraylist.add(createinput);
								walkAndCreateInputStructure(createinput,
										ibocreatedefinition1, list);
							}
						}

						if (obj instanceof com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput)
							updateCompoundCreateInput(
									(com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput) obj,
									arraylist, s);
						else if (obj instanceof com.teamcenter.services.rac.core._2015_07.DataManagement.CreateInput2)
							updateCompoundCreateInput(
									(com.teamcenter.services.rac.core._2015_07.DataManagement.CreateInput2) obj,
									arraylist, s);
					}
				}

			}
		}
	}

	private static void updateCompoundCreateInput(
			com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput createinput,
			List list, String s) {
		if (createinput.compoundCreateInput == null)
			createinput.compoundCreateInput = new HashMap();
		if (createinput.compoundCreateInput.containsKey(s)) {
			com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput acreateinput[] = (com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput[]) createinput.compoundCreateInput
					.get(s);
			if (acreateinput != null && acreateinput.length > 0)
				list.addAll(Arrays.asList(acreateinput));
		}
		if (list != null && !list.isEmpty()) {
			com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput acreateinput1[] = (com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput[]) list
					.toArray(new com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput[list
							.size()]);
			createinput.compoundCreateInput.put(s, acreateinput1);
		}
	}

	private static void updateCompoundCreateInput(
			com.teamcenter.services.rac.core._2015_07.DataManagement.CreateInput2 createinput2,
			List list, String s) {
		if (createinput2.compoundCreateInput == null)
			createinput2.compoundCreateInput = new HashMap();
		if (createinput2.compoundCreateInput.containsKey(s)) {
			com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput acreateinput[] = (com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput[]) createinput2.compoundCreateInput
					.get(s);
			if (acreateinput != null && acreateinput.length > 0)
				list.addAll(Arrays.asList(acreateinput));
		}
		if (list != null && !list.isEmpty()) {
			com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput acreateinput1[] = (com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput[]) list
					.toArray(new com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput[list
							.size()]);
			createinput2.compoundCreateInput.put(s, acreateinput1);
		}
	}

	private static void walkAndCreateNestedInputStructure(Object obj,
			ICreateInstanceInput icreateinstanceinput, boolean flag)
			throws NumberFormatException {
		if (flag)
			updateInputStructure(
					obj,
					Arrays.asList(new ICreateInstanceInput[] { icreateinstanceinput }));
		if (icreateinstanceinput != null
				&& icreateinstanceinput.hasSecondaryCreateInputs()) {
			Map map = icreateinstanceinput.getSecondaryCreateInputs();
			for (Iterator iterator = map.entrySet().iterator(); iterator
					.hasNext();) {
				java.util.Map.Entry entry = (java.util.Map.Entry) iterator
						.next();
				String s = (String) entry.getKey();
				List list = (List) entry.getValue();
				if (list != null && !list.isEmpty()) {
					ArrayList arraylist = new ArrayList(0);
					ICreateInstanceInput icreateinstanceinput1;
					com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput createinput;
					for (Iterator iterator1 = list.iterator(); iterator1
							.hasNext(); walkAndCreateNestedInputStructure(
							createinput, icreateinstanceinput1, true)) {
						icreateinstanceinput1 = (ICreateInstanceInput) iterator1
								.next();
						createinput = new com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput();
						createinput.boName = icreateinstanceinput1
								.getCreateDefinition().getTypeName();
						arraylist.add(createinput);
					}

					if (obj instanceof com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput)
						updateCompoundCreateInput(
								(com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput) obj,
								arraylist, s);
					else if (obj instanceof com.teamcenter.services.rac.core._2015_07.DataManagement.CreateInput2)
						updateCompoundCreateInput(
								(com.teamcenter.services.rac.core._2015_07.DataManagement.CreateInput2) obj,
								arraylist, s);
				}
			}

		}
	}

	public static List findCreateInputsForCreateDef(
			IBOCreateDefinition ibocreatedefinition, List list) {
		return findCreateInputsForCreateDef2(ibocreatedefinition, list, false);
	}

	public static List findCreateInputsForCreateDef2(
			IBOCreateDefinition ibocreatedefinition, List list, boolean flag) {
		ArrayList arraylist = new ArrayList(0);
		if (ibocreatedefinition != null && list != null && !list.isEmpty()) {
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				ICreateInstanceInput icreateinstanceinput = (ICreateInstanceInput) iterator
						.next();
				IBOCreateDefinition ibocreatedefinition1 = icreateinstanceinput
						.getCreateDefinition();
				if (ibocreatedefinition1 == ibocreatedefinition)
					arraylist.add(icreateinstanceinput);
				else if (ibocreatedefinition1.getTypeName().equals(
						ibocreatedefinition.getTypeName())) {
					String s = ibocreatedefinition1.getParentContext();
					String s1 = ibocreatedefinition.getParentContext();
					if (s == null && s1 == null || s != null && s1 != null
							&& s.equals(s1)) {
						String s3 = ibocreatedefinition1.getContext();
						String s4 = ibocreatedefinition.getContext();
						if (s3 == null && s4 == null || s3 != null
								&& s4 != null && s3.equals(s4))
							arraylist.add(icreateinstanceinput);
					}
				} else if (flag) {
					Map map = ibocreatedefinition
							.getSecondaryCreateDefinitions();
					if (map != null && !map.isEmpty() && !list.isEmpty()) {
						String s2 = ibocreatedefinition1.getContext();
						String as[] = (String[]) map.keySet().toArray(
								new String[map.size()]);
						String as1[];
						int j = (as1 = as).length;
						for (int i = 0; i < j; i++) {
							String s5 = as1[i];
							if (s5 != null && s5.equals(s2))
								arraylist.add(icreateinstanceinput);
						}

					}
				}
			}

		}
		return arraylist;
	}

	private static boolean createInstanceInputAvailableForCreateDef(
			IBOCreateDefinition ibocreatedefinition, List list) {
		List list1 = findCreateInputsForCreateDef2(ibocreatedefinition, list,
				true);
		return list1 != null && !list1.isEmpty();
	}

	private static void updateInputStructure(Object obj, List list)
			throws NumberFormatException {
		ICreateInstanceInput icreateinstanceinput = null;
		String s = null;
		Object obj1 = null;
		try {
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				ICreateInstanceInput icreateinstanceinput1 = (ICreateInstanceInput) iterator
						.next();
				icreateinstanceinput = icreateinstanceinput1;
				Map map = icreateinstanceinput1.getCreateInputs();
				if (map != null && !map.isEmpty()) {
					BOCreatePropertyDescriptor abocreatepropertydescriptor[] = (BOCreatePropertyDescriptor[]) map
							.keySet().toArray(
									new BOCreatePropertyDescriptor[map.size()]);
					if (abocreatepropertydescriptor != null
							&& abocreatepropertydescriptor.length > 0) {
						BOCreatePropertyDescriptor abocreatepropertydescriptor1[];
						int j = (abocreatepropertydescriptor1 = abocreatepropertydescriptor).length;
						for (int i = 0; i < j; i++) {
							BOCreatePropertyDescriptor bocreatepropertydescriptor = abocreatepropertydescriptor1[i];
							obj1 = map.get(bocreatepropertydescriptor);
							if (obj != null
									&& bocreatepropertydescriptor != null) {
								s = bocreatepropertydescriptor
										.getPropertyDescriptor().getName();
								int k = bocreatepropertydescriptor
										.getPropertyDescriptor().getType();
								boolean flag = bocreatepropertydescriptor
										.getPropertyDescriptor().isArray();
								if (obj instanceof com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput) {
									Map map1 = getInputMap(
											k,
											flag,
											(com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput) obj);
									if (map1 != null) {
										Object obj2 = marshallValue(k, flag,
												obj1);
										if (obj2 != null)
											map1.put(s, obj2);
									}
								} else if (obj instanceof com.teamcenter.services.rac.core._2015_07.DataManagement.CreateInput2) {
									Map map2 = getInputMap(
											k,
											flag,
											(com.teamcenter.services.rac.core._2015_07.DataManagement.CreateInput2) obj);
									if (map2 != null) {
										List list1 = marshallToStringValues(k,
												flag, obj1);
										String as[] = (String[]) list1
												.toArray(new String[0]);
										if (list1 != null)
											map2.put(s, as);
									}
								}
							}
						}

					}
				}
			}

		} catch (NumberFormatException numberformatexception) {
			if (icreateinstanceinput != null) {
				icreateinstanceinput.setErrorCreateInput(true);
				icreateinstanceinput.setErrorPropName(s);
			}
			if (obj1 != null)
				Logger.getLogger(SOAGenericCreateHelper.class).error(
						(new StringBuilder(String.valueOf(obj1.toString())))
								.append(" : ")
								.append("Invalid Input for Property")
								.append(" ").append(s).toString());
			throw numberformatexception;
		}
	}

	private static Map getInputMap(
			int i,
			boolean flag,
			com.teamcenter.services.rac.core._2015_07.DataManagement.CreateInput2 createinput2) {
		Map map = null;
		switch (i) {
		case 1: // '\001'
		case 2: // '\002'
		case 3: // '\003'
		case 4: // '\004'
		case 5: // '\005'
		case 6: // '\006'
		case 7: // '\007'
		case 8: // '\b'
		case 9: // '\t'
		case 10: // '\n'
		case 11: // '\013'
		case 12: // '\f'
		case 13: // '\r'
		case 14: // '\016'
			map = createinput2.propertyNameValues;
			break;
		}
		return map;
	}

	private static Map getInputMap(
			int i,
			boolean flag,
			com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput createinput) {
		Map map = null;
		switch (i) {
		case 2: // '\002'
			map = flag ? createinput.dateArrayProps : createinput.dateProps;
			break;

		case 3: // '\003'
			map = flag ? createinput.doubleArrayProps : createinput.doubleProps;
			break;

		case 4: // '\004'
			map = flag ? createinput.floatArrayProps : createinput.floatProps;
			break;

		case 5: // '\005'
		case 7: // '\007'
			map = flag ? createinput.intArrayProps : createinput.intProps;
			break;

		case 6: // '\006'
			map = flag ? createinput.boolArrayProps : createinput.boolProps;
			break;

		case 1: // '\001'
		case 8: // '\b'
		case 12: // '\f'
			map = flag ? createinput.stringArrayProps : createinput.stringProps;
			break;

		case 9: // '\t'
		case 10: // '\n'
		case 11: // '\013'
		case 13: // '\r'
		case 14: // '\016'
			map = flag ? createinput.tagArrayProps : createinput.tagProps;
			break;
		}
		return map;
	}

	public static Object marshallValue(int i, boolean flag, Object obj)
			throws NumberFormatException {
		Object obj1 = null;
		switch (i) {
		case 2: // '\002'
			obj1 = flag ? toDateValueArray(obj) : toDateValue(obj);
			break;

		case 3: // '\003'
			obj1 = flag ? toDoubleValueArray(obj) : toDoubleValue(obj);
			break;

		case 4: // '\004'
			obj1 = flag ? toFloatValueArray(obj) : toFloatValue(obj);
			break;

		case 5: // '\005'
		case 7: // '\007'
			obj1 = flag ? toIntegerValueArray(obj) : toIntegerValue(obj);
			break;

		case 6: // '\006'
			obj1 = flag ? toBooleanValueArray(obj) : toBooleanValue(obj);
			break;

		case 1: // '\001'
		case 8: // '\b'
		case 12: // '\f'
			obj1 = flag ? toStringValueArray(obj, i) : toStringValue(obj, i);
			break;

		case 9: // '\t'
		case 10: // '\n'
		case 11: // '\013'
		case 13: // '\r'
		case 14: // '\016'
			obj1 = flag ? toReferenceValueArray(obj) : toReferenceValue(obj);
			break;
		}
		return obj1;
	}

	public static List marshallToStringValues(int i, boolean flag, Object obj)
			throws NumberFormatException {
		ArrayList arraylist = new ArrayList();
		if (!flag) {
			arraylist.add(marshaltoStringValue(i, obj));
			return arraylist;
		}
		switch (i) {
		case 1: // '\001'
		case 2: // '\002'
		case 3: // '\003'
		case 4: // '\004'
		case 5: // '\005'
		case 6: // '\006'
		case 7: // '\007'
		case 8: // '\b'
		case 9: // '\t'
		case 10: // '\n'
		case 11: // '\013'
		case 12: // '\f'
		case 13: // '\r'
		case 14: // '\016'
			Object aobj[] = getArray(obj);
			for (int j = 0; j < aobj.length; j++)
				arraylist.add(marshaltoStringValue(i, aobj[j]));

			break;
		}
		return arraylist;
	}

	private static String marshaltoStringValue(int i, Object obj) {
		if (obj == null)
			return null;
		String s = null;
		switch (i) {
		default:
			break;

		case 6: // '\006'
			if (obj instanceof Boolean)
				s = Property.toBooleanString(((Boolean) obj).booleanValue());
			break;

		case 2: // '\002'
			if (obj instanceof Date)
				s = Property.toDateString((Date) obj);
			break;

		case 1: // '\001'
		case 3: // '\003'
		case 4: // '\004'
		case 5: // '\005'
		case 7: // '\007'
		case 8: // '\b'
		case 12: // '\f'
			if (obj instanceof String) {
				s = (String) obj;
				break;
			}
			if (obj != null)
				s = obj.toString().trim();
			break;

		case 9: // '\t'
		case 10: // '\n'
		case 11: // '\013'
		case 13: // '\r'
		case 14: // '\016'
			if (obj instanceof ModelObject)
				s = Property.toModelObjectString((ModelObject) obj);
			break;
		}
		return s;
	}

	private static Object toIntegerValue(Object obj)
			throws NumberFormatException {
		Object obj1 = null;
		if (obj instanceof Integer)
			obj1 = obj;
		else if (obj != null) {
			String s = obj.toString().trim();
			if (!s.isEmpty())
				obj1 = Integer.valueOf(s);
		}
		if (obj1 != null)
			return BigInteger.valueOf(((Integer) obj1).intValue());
		else
			return obj1;
	}

	private static Object toIntegerValueArray(Object obj)
			throws NumberFormatException {
		if (obj instanceof int[])
			return obj;
		Object aobj[] = getArray(obj);
		if (aobj != null && aobj.length > 0) {
			ArrayList arraylist = new ArrayList(aobj.length);
			Object aobj1[];
			int k = (aobj1 = aobj).length;
			for (int i = 0; i < k; i++) {
				Object obj1 = aobj1[i];
				Object obj2 = toIntegerValue(obj1);
				if (obj2 != null)
					arraylist.add((BigInteger) obj2);
			}

			if (!arraylist.isEmpty()) {
				int ai[] = new int[arraylist.size()];
				int j = 0;
				for (Iterator iterator = arraylist.iterator(); iterator
						.hasNext();) {
					BigInteger biginteger = (BigInteger) iterator.next();
					ai[j++] = biginteger.intValue();
				}

				return ai;
			}
		}
		return null;
	}

	private static Object toFloatValue(Object obj) {
		Object obj1 = null;
		if (obj instanceof Float)
			obj1 = obj;
		else if (obj != null) {
			String s = obj.toString().trim();
			if (!s.isEmpty())
				obj1 = Float.valueOf(s);
		}
		return obj1;
	}

	private static Object toFloatValueArray(Object obj) {
		if (obj instanceof float[])
			return obj;
		Object aobj[] = getArray(obj);
		if (aobj != null && aobj.length > 0) {
			ArrayList arraylist = new ArrayList(aobj.length);
			Object aobj1[];
			int k = (aobj1 = aobj).length;
			for (int i = 0; i < k; i++) {
				Object obj1 = aobj1[i];
				Object obj2 = toFloatValue(obj1);
				if (obj2 != null)
					arraylist.add((Float) obj2);
			}

			if (!arraylist.isEmpty()) {
				float af[] = new float[arraylist.size()];
				int j = 0;
				for (Iterator iterator = arraylist.iterator(); iterator
						.hasNext();) {
					Float float1 = (Float) iterator.next();
					af[j++] = float1.floatValue();
				}

				return af;
			}
		}
		return null;
	}

	private static Object toDoubleValue(Object obj) {
		Object obj1 = null;
		if (obj instanceof Double)
			obj1 = obj;
		else if (obj != null) {
			String s = obj.toString().trim();
			if (!s.isEmpty())
				obj1 = Double.valueOf(s);
		}
		return obj1;
	}

	private static Object toDoubleValueArray(Object obj) {
		if (obj instanceof double[])
			return obj;
		Object aobj[] = getArray(obj);
		if (aobj != null && aobj.length > 0) {
			ArrayList arraylist = new ArrayList(aobj.length);
			Object aobj1[];
			int k = (aobj1 = aobj).length;
			for (int i = 0; i < k; i++) {
				Object obj1 = aobj1[i];
				Object obj2 = toDoubleValue(obj1);
				if (obj2 != null)
					arraylist.add((Double) obj2);
			}

			if (!arraylist.isEmpty()) {
				double ad[] = new double[arraylist.size()];
				int j = 0;
				for (Iterator iterator = arraylist.iterator(); iterator
						.hasNext();) {
					Double double1 = (Double) iterator.next();
					ad[j++] = double1.doubleValue();
				}

				return ad;
			}
		}
		return null;
	}

	private static Object toBooleanValue(Object obj) {
		Object obj1 = null;
		if (obj instanceof Boolean)
			obj1 = obj;
		else if (obj != null) {
			String s = obj.toString().trim();
			if (!s.isEmpty())
				obj1 = Boolean.valueOf(s);
		}
		return obj1;
	}

	private static Object toBooleanValueArray(Object obj) {
		if (obj instanceof boolean[])
			return obj;
		Object aobj[] = getArray(obj);
		if (aobj != null && aobj.length > 0) {
			ArrayList arraylist = new ArrayList(aobj.length);
			Object aobj1[];
			int k = (aobj1 = aobj).length;
			for (int i = 0; i < k; i++) {
				Object obj1 = aobj1[i];
				Object obj2 = toBooleanValue(obj1);
				if (obj2 != null)
					arraylist.add((Boolean) obj2);
			}

			if (!arraylist.isEmpty()) {
				boolean aflag[] = new boolean[arraylist.size()];
				int j = 0;
				for (Iterator iterator = arraylist.iterator(); iterator
						.hasNext();) {
					Boolean boolean1 = (Boolean) iterator.next();
					aflag[j++] = boolean1.booleanValue();
				}

				return aflag;
			}
		}
		return null;
	}

	private static Object toStringValue(Object obj, int i) {
		Object obj1 = null;
		if (obj instanceof String) {
			if (i == 8 || i == 1 || i == 12 || !obj.toString().isEmpty())
				obj1 = obj;
		} else if (obj != null)
			obj1 = obj.toString().trim();
		return obj1;
	}

	private static Object toStringValueArray(Object obj, int i) {
		Object aobj[];
		if (obj instanceof char[]) {
			char ac[] = (char[]) obj;
			aobj = new Object[ac.length];
			for (int j = 0; j < ac.length; j++)
				aobj[j] = Character.valueOf(ac[j]);

		} else {
			aobj = getArray(obj);
		}
		if (aobj != null && aobj.length > 0) {
			ArrayList arraylist = new ArrayList(aobj.length);
			Object aobj1[];
			int l = (aobj1 = aobj).length;
			for (int k = 0; k < l; k++) {
				Object obj1 = aobj1[k];
				Object obj2 = toStringValue(obj1, i);
				if (obj2 != null)
					arraylist.add((String) obj2);
			}

			if (!arraylist.isEmpty())
				return ((Object) (arraylist
						.toArray(new String[arraylist.size()])));
		}
		return null;
	}

	private static Object toReferenceValue(Object obj) {
		Object obj1 = null;
		if (obj instanceof TCComponent)
			obj1 = obj;
		else if (obj instanceof String)
			obj1 = convertUIDToTCComponent((String) obj);
		return obj1;
	}

	private static Object toReferenceValueArray(Object obj) {
		Object aobj[] = getArray(obj);
		if (aobj != null && aobj.length > 0) {
			ArrayList arraylist = new ArrayList(aobj.length);
			Object aobj1[];
			int j = (aobj1 = aobj).length;
			for (int i = 0; i < j; i++) {
				Object obj1 = aobj1[i];
				Object obj2 = toReferenceValue(obj1);
				if (obj2 != null)
					arraylist.add((TCComponent) obj2);
			}

			if (!arraylist.isEmpty())
				return ((Object) (arraylist.toArray(new TCComponent[arraylist
						.size()])));
		}
		return null;
	}

	private static TCComponent convertUIDToTCComponent(String s) {
		TCComponent tccomponent = null;
		if (s != null && !s.isEmpty()) {
			ISessionService isessionservice = Activator.getDefault()
					.getSessionService();
			if (isessionservice != null)
				try {
					TCSession tcsession = (TCSession) isessionservice
							.getSession(TCSession.class.getName());
					tccomponent = tcsession.stringToComponent(s);
				} catch (Exception exception) {
					logger.error(exception.getLocalizedMessage(), exception);
				}
		}
		return tccomponent;
	}

	private static Object toDateValue(Object obj) {
		Object obj1 = null;
		if (obj instanceof Calendar)
			obj1 = obj;
		else if (obj instanceof Date) {
			GregorianCalendar gregoriancalendar = new GregorianCalendar();
			gregoriancalendar.setTime((Date) obj);
			obj1 = gregoriancalendar;
		}
		return obj1;
	}

	private static Object toDateValueArray(Object obj) {
		Object aobj[] = getArray(obj);
		if (aobj != null && aobj.length > 0) {
			ArrayList arraylist = new ArrayList(aobj.length);
			Object aobj1[];
			int j = (aobj1 = aobj).length;
			for (int i = 0; i < j; i++) {
				Object obj1 = aobj1[i];
				Object obj2 = toDateValue(obj1);
				if (obj2 != null)
					arraylist.add((Calendar) obj2);
			}

			if (!arraylist.isEmpty())
				return ((Object) (arraylist.toArray(new Calendar[arraylist
						.size()])));
		}
		return null;
	}

	private static Object[] getArray(Object obj) {
		Object aobj[] = null;
		if (obj instanceof Object[])
			aobj = (Object[]) obj;
		else if (obj instanceof Collection)
			aobj = ((Collection) obj).toArray();
		else
			aobj = (new Object[] { obj });
		return aobj;
	}

	private static void logCreateInputs(List list) {
		logger.debug("*** Delegating to Generic Create API with the below information ***");
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ICreateInstanceInput icreateinstanceinput = (ICreateInstanceInput) iterator
					.next();
			IBOCreateDefinition ibocreatedefinition = icreateinstanceinput
					.getCreateDefinition();
			if (ibocreatedefinition != null) {
				Map map = icreateinstanceinput.getCreateInputs();
				if (map != null && !map.isEmpty()) {
					logger.debug((new StringBuilder("BO Name : ")).append(
							ibocreatedefinition.getTypeName()).toString());
					String s;
					String s1;
					for (Iterator iterator1 = map.entrySet().iterator(); iterator1
							.hasNext(); logger.debug((new StringBuilder("["))
							.append(s).append("] - [").append(s1).append("]")
							.toString())) {
						java.util.Map.Entry entry = (java.util.Map.Entry) iterator1
								.next();
						s = ((BOCreatePropertyDescriptor) entry.getKey())
								.getPropertyDescriptor().getName();
						Object obj = entry.getValue();
						s1 = obj == null || obj.toString().isEmpty() ? "<No Value>"
								: obj.toString();
					}

				}
			}
		}

	}

	private static void logRootInput(
			com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput createinput,
			String s) {
		logger.debug((new StringBuilder(String.valueOf(s)))
				.append("  BO Name : ").append(createinput.boName).toString());
		HashMap hashmap = (HashMap) createinput.compoundCreateInput;
		if (hashmap != null && !hashmap.isEmpty()) {
			Set set = hashmap.keySet();
			Object aobj[] = set.toArray();
			Object aobj1[];
			int j = (aobj1 = aobj).length;
			for (int i = 0; i < j; i++) {
				Object obj = aobj1[i];
				Object obj1 = hashmap.get(obj);
				Object aobj2[] = null;
				if (obj1 instanceof Object[])
					aobj2 = (Object[]) obj1;
				if (aobj2 != null && aobj2.length != 0) {
					Object aobj3[];
					int l = (aobj3 = aobj2).length;
					for (int k = 0; k < l; k++) {
						Object obj2 = aobj3[k];
						if (obj2 instanceof com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput) {
							logger.debug((new StringBuilder(String.valueOf(s)))
									.append("   -secondary object for : ")
									.append(createinput.boName).toString());
							logRootInput(
									(com.teamcenter.services.rac.core._2008_06.DataManagement.CreateInput) obj2,
									(new StringBuilder(String.valueOf(s)))
											.append("    ").toString());
						}
					}

				}
			}

		}
	}

	private static Logger logger = Logger
			.getLogger(SOAGenericCreateHelper.class);

}

/*
 * DECOMPILATION REPORT
 * 
 * Decompiled from:
 * F:\Teamcenter11ENV\JAVA\rac\plugins\com.teamcenter.rac.common_11000.2.0.jar
 * Total time: 391 ms Jad reported messages/errors: Exit status: 0 Caught
 * exceptions:
 */