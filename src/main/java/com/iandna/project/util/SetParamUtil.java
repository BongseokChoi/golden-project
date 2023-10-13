/*
 * @(#)DateUtil.java	1.0	2009. 08. 30.
 * 
 * Copyright (c) 2009 TA Networks
 * All rights reserved.
 */
package com.iandna.project.util;

import com.iandna.project.config.model.MapData;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * 파라미터 정보를 DB insert 양식에 맞춰서 세팅하는 메서드 모음
 * @version 1.0
 * @since   2020.09.10
 * @author  dhkim
 */

public class SetParamUtil {
	private static final Log logger = LogFactory.getLog (SetParamUtil.class);
	/*
	 * att1, att2 : seperator를 기준으로 배열 요소가 value가 되는 값
	 * seperator : att1, att2를 split할 구분자
	 * att1_key, att2_key : return 되는 배열 요소의(맵) key
	 */
	public static ArrayList<HashMap<String, String>> stringToMapList(String att1, String att2, String seperator, String att1_key, String att2_key) {
		if(att1 == null) {
			att1 = "";
		}
		if(att2 == null) {
			att2 = "";
		}
		
		ArrayList<HashMap<String, String>> data_list = new ArrayList<HashMap<String, String>>();
		String[] att1_list = StringUtils.split(att1, seperator);
		String[] att2_list = StringUtils.split(att2, seperator);
		int limit = 0;
		int att1_length = att1_list.length;
		int att2_length = att2_list.length;
		String att1_element;
		String att2_element;
		HashMap<String, String> result = null;
		limit = (att1_list.length > att2_list.length) ? att1_list.length : att2_list.length;
		
		for(int i=0; i<limit; i++) {
			result = new HashMap<String, String>();
			if(i > att1_length-1) {
				att1_element = "";
			}else {
				att1_element = att1_list[i];
			}
			
			if(i > att2_length-1) {
				att2_element = "";
			}else {
				att2_element = att2_list[i];	
			}
			result.put(att1_key, att1_element);
			result.put(att2_key, att2_element);
			data_list.add(result);
		}
		return data_list;
	}
	
	/*
	 * 위와같음 변수만 하나 더 많음
	 */
	public static ArrayList<HashMap<String, String>> stringToMapList(String att1, String att2, String att3, String seperator, String att1_key, String att2_key, String att3_key) {
		if(att1 == null) {
			att1 = "";
		}
		if(att2 == null) {
			att2 = "";
		}
		if(att3 == null) {
			att3 = "";
		}
		
		ArrayList<HashMap<String, String>> data_list = new ArrayList<HashMap<String, String>>();
		String[] att1_list = StringUtils.split(att1, seperator);
		String[] att2_list = StringUtils.split(att2, seperator);
		String[] att3_list = StringUtils.split(att3, seperator);
		int limit = 0;
		int att1_length = att1_list.length;
		int att2_length = att2_list.length;
		int att3_length = att3_list.length;
		String att1_element;
		String att2_element;
		String att3_element;
		HashMap<String, String> result = null;
		limit = getGreatestNumber(att1_length, att2_length, att3_length);
		
		for(int i=0; i<limit; i++) {
			result = new HashMap<String, String>();
			if(i > att1_length-1) {
				att1_element = "";
			}else {
				att1_element = att1_list[i];
			}
			
			if(i > att2_length-1) {
				att2_element = "";
			}else {
				att2_element = att2_list[i];	
			}

			if(i > att3_length-1) {
				att3_element = "";
			}else {
				att3_element = att3_list[i];	
			}
			result.put(att1_key, att1_element);
			result.put(att2_key, att2_element);
			result.put(att3_key, att3_element);
			data_list.add(result);
		}
		return data_list;
	}
	
	
	/*
	 * param에서 key+i를 꺼내서 value1^value2^value3^... 형태의 String으로 변환하여 return함.
	 * seperator : value1, value2...를 구분할 구분자( '^', ',' 등..)
	 */
	public static String paramToString(MapData param, String custom_key, String seperator) {
		StringBuffer result = new StringBuffer();
		String key = null;
		ArrayList<String> target_key_list = new ArrayList<String>();
		Set set = param.keySet();

		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			key = iterator.next().toString();
			if (key.startsWith(custom_key) && !key.contains("total") && !key.contains("capacity")) {
				target_key_list.add(key);
			}
		}
		Collections.sort(target_key_list);
		for(String value : target_key_list) {
			value = param.getString(value);
			if(value.isEmpty())
				value = " ";
			result.append(value + seperator);
		}
		if (result.length() > 0)
			result.deleteCharAt(result.length() - 1);
		return result.toString();
	}

	/*
	 * String 배열을 index1^index2^index3^...형태의 String으로 변환
	 */
	public static String stringArrToString(String[] string_arr, String seperater) {
		StringBuffer result = new StringBuffer();
		for(String s : string_arr) {
			result.append(s);
			result.append("^");
		}
		result.deleteCharAt(result.length()-1);
		return result.toString();
	}

	public static int getGreatestNumber(int a, int b, int c) {
		int greatest = 0;
		if(a >= b) {
			if( a >= c) {
				greatest = a;
			}
		}
		
		if(b >= a) {
			if(b >= c) {
				greatest = b;
			}
		}
		
		if(c >= a){
			if(c >= b) {
				greatest = c;
			}
		}
		return greatest;
	}
	
	public static boolean checkSnsReview(MapData param, int[] req_sns_array, String indiv_sel_chk) {
		boolean check_flag = true;				
		switch(indiv_sel_chk) {
		case "1":
			check_flag = false;
			for(int i = 0; i < req_sns_array.length; i++) {
				if(i == 0) {
					if( req_sns_array[i] == 1 &&
						(param.getString("sns_review_url") != null && !param.getString("sns_review_url").isEmpty()) ) {
						check_flag = true;
						break;
					}
				}
				else {
					if( req_sns_array[i] == 1 &&
							(param.getString("sns_review_url"+(i+1)) != null && !param.getString("sns_review_url"+(i+1)).isEmpty()) ) {
						check_flag = true;
						break;
					}
				}
			}
			break;
		case "2":
			check_flag = true;
			for(int i = 0; i < req_sns_array.length; i++) {
				if(i == 0) {
					if( req_sns_array[i] == 1 &&
						(param.getString("sns_review_url") == null || param.getString("sns_review_url").isEmpty()) ) {
						check_flag = false;
						break;
					}
				}
				else {
					if( req_sns_array[i] == 1 &&
							(param.getString("sns_review_url"+(i+1)) == null || param.getString("sns_review_url"+(i+1)).isEmpty()) ) {
						check_flag = false;
						break;
					}
				}
			}
			break;
		default:
			check_flag = false;
			for(int i = 0; i < req_sns_array.length; i++) {
				if(i == 0) {
					if( req_sns_array[i] == 1 &&
						(param.getString("sns_review_url") != null && !param.getString("sns_review_url").isEmpty()) ) {
						check_flag = true;
						break;
					}
				}
				else {
					if( req_sns_array[i] == 1 &&
							(param.getString("sns_review_url"+(i+1)) != null && !param.getString("sns_review_url"+(i+1)).isEmpty()) ) {
						check_flag = true;
						break;
					}
				}
			}
		}
		
		logger.info("####" + check_flag);
		
		return check_flag;
	}
	
	public static void main(String[] args) {
		//logger.debug(stringToMapDataList("1^2^3^4", "아^구^바^부^바", "^", "int", "char"));
		
		//logger.debug(getGreatestNumber(9,15,11));
		
		MapData param = new MapData();
		param.set("sns_review_url", "dasd");
		int[] req_sns_array = 
			{1
			,1
			,0
			,0};
		
		System.out.println(checkSnsReview(param, req_sns_array, "2"));
	}
}
