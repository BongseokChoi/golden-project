package com.iandna.project.util;

import com.iandna.project.config.model.MapData;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public interface CommonUtil {
	/*
	 * album invite code 발급 메서드
	 * E12345678 형태의 코드 발급됨
	 */
	public static String createAlbumCode() {
		int a = (int) ((Math.random() * 26) + 65);
		char c = (char) a;
		Random rand = new Random();
		String rst = Integer.toString(rand.nextInt(8) + 1);
		for (int j = 0; j < 7; j++) {
			rst += Integer.toString(rand.nextInt(9));
		}
		return c + rst;
	}

	/*
	 * att1, att2 : seperator를 기준으로 배열 요소가 value가 되는 값
	 * seperator : att1, att2를 split할 구분자
	 * att1_key, att2_key : return 되는 배열 요소의(맵) key
	 */
	public static ArrayList<HashMap<String, String>> stringToMapList(String att1, String att2, String seperator, String att1_key, String att2_key) {
		if (att1 == null) {
			att1 = "";
		}
		if (att2 == null) {
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

		for (int i = 0; i < limit; i++) {
			result = new HashMap<String, String>();
			if (i > att1_length - 1) {
				att1_element = "";
			} else {
				att1_element = att1_list[i];
			}

			if (i > att2_length - 1) {
				att2_element = "";
			} else {
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
		if (att1 == null) {
			att1 = "";
		}
		if (att2 == null) {
			att2 = "";
		}
		if (att3 == null) {
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

		for (int i = 0; i < limit; i++) {
			result = new HashMap<String, String>();
			if (i > att1_length - 1) {
				att1_element = "";
			} else {
				att1_element = att1_list[i];
			}

			if (i > att2_length - 1) {
				att2_element = "";
			} else {
				att2_element = att2_list[i];
			}

			if (i > att3_length - 1) {
				att3_element = "";
			} else {
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
		for (String value : target_key_list) {
			value = param.getString(value);
			if (value.isEmpty())
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
		for (String s : string_arr) {
			result.append(s);
			result.append("^");
		}
		result.deleteCharAt(result.length() - 1);
		return result.toString();
	}

	public static int getGreatestNumber(int a, int b, int c) {
		int greatest = 0;
		if (a >= b) {
			if (a >= c) {
				greatest = a;
			}
		}

		if (b >= a) {
			if (b >= c) {
				greatest = b;
			}
		}

		if (c >= a) {
			if (c >= b) {
				greatest = c;
			}
		}
		return greatest;
	}

	public static boolean checkSnsReview(MapData param, int[] req_sns_array, String indiv_sel_chk) {
		boolean check_flag = true;
		switch (indiv_sel_chk) {
			case "1":
				check_flag = false;
				for (int i = 0; i < req_sns_array.length; i++) {
					if (i == 0) {
						if (req_sns_array[i] == 1 &&
								(param.getString("sns_review_url") != null && !param.getString("sns_review_url").isEmpty())) {
							check_flag = true;
							break;
						}
					} else {
						if (req_sns_array[i] == 1 &&
								(param.getString("sns_review_url" + (i + 1)) != null && !param.getString("sns_review_url" + (i + 1)).isEmpty())) {
							check_flag = true;
							break;
						}
					}
				}
				break;
			case "2":
				check_flag = true;
				for (int i = 0; i < req_sns_array.length; i++) {
					if (i == 0) {
						if (req_sns_array[i] == 1 &&
								(param.getString("sns_review_url") == null || param.getString("sns_review_url").isEmpty())) {
							check_flag = false;
							break;
						}
					} else {
						if (req_sns_array[i] == 1 &&
								(param.getString("sns_review_url" + (i + 1)) == null || param.getString("sns_review_url" + (i + 1)).isEmpty())) {
							check_flag = false;
							break;
						}
					}
				}
				break;
			default:
				check_flag = false;
				for (int i = 0; i < req_sns_array.length; i++) {
					if (i == 0) {
						if (req_sns_array[i] == 1 &&
								(param.getString("sns_review_url") != null && !param.getString("sns_review_url").isEmpty())) {
							check_flag = true;
							break;
						}
					} else {
						if (req_sns_array[i] == 1 &&
								(param.getString("sns_review_url" + (i + 1)) != null && !param.getString("sns_review_url" + (i + 1)).isEmpty())) {
							check_flag = true;
							break;
						}
					}
				}
		}

		return check_flag;
	}


	public static String getNextPk(String currentPk) {

		String prefix = StringUtils.substring(currentPk, 0, 1);
		String seq = StringUtils.substring(currentPk, 1);
		int length = seq.length();
		for (int i = 0; i < length; i++) {
			if (seq.charAt(i) != '0') {
				continue;
			} else {
				seq = seq.substring(i, length);
				break;
			}
		}
		String nextSeq = (Integer.parseInt(seq) + 1) + "";
		int seqCheck = length - nextSeq.length();
		for (int i = 0; i < seqCheck; i++) {
			nextSeq = "0" + nextSeq;
		}
		return prefix + nextSeq;
	}

	public static boolean isDomestic(String location) {
		boolean result = false;
		String[] domesticLocations = {"서울", "종로", "중구", "용산", "성동", "광진", "동대문", "중랑", "성북", "강북", "도봉", "노원", "은평", "서대문", "마포", "양천", "강서", "구로", "금천", "영등포", "동작", "관악", "서초", "강남", "송파", "강동", "중구", "서구", "동구", "영도", "부산", "동래", "남구", "북구", "해운대", "사하", "금정", "강서", "연제", "수영", "사상", "기장", "중구", "동구", "서구", "남구", "북구", "수성", "달서", "달성", "중구", "동구", "미추홀", "연수", "남동", "부평", "계양", "서구", "강화", "옹진", "동구", "서구", "남구", "북구", "광산", "동구", "중구", "서구", "유성", "대덕", "중구", "남구", "동구", "북구", "울주", "세종", "수원", "장안", "권선", "팔달", "영통", "성남", "수정", "중원", "분당", "의정부", "안양", "만안", "동안", "부천", "광명", "평택", "동두천", "안산", "상록", "단원", "고양", "덕양", "일산", "동구", "서구", "과천", "구리", "남양주", "오산", "시흥", "군포", "의왕", "하남", "용인", "처인", "기흥", "수지", "파주", "이천", "안성", "김포", "화성", "광주", "양주", "포천", "여주", "연천", "가평", "양평", "춘천", "원주", "강릉", "동해", "태백", "속초", "삼척", "홍천", "횡성", "영월", "평창", "정선", "철원", "화천", "양구", "인제", "고성", "양양", "청주", "상당", "서원", "흥덕", "청원", "충주", "제천", "보은", "옥천", "영동", "증평", "진천", "괴산", "음성", "단양", "천안", "동남", "서북", "공주", "보령", "아산", "서산", "논산", "계룡", "당진", "금산", "부여", "서천", "청양", "홍성", "예산", "태안", "전주", "완산", "덕진", "군산", "익산", "정읍", "남원", "김제", "완주", "진안", "무주", "장수", "임실", "순창", "고창", "부안", "목포", "여수", "순천", "나주", "광양", "담양", "곡성", "구례", "고흥", "보성", "화순", "장흥", "강진", "해남", "영암", "무안", "함평", "영광", "장성", "완도", "진도", "신안", "포항", "남구", "북구", "경주", "김천", "안동", "구미", "영주", "영천", "상주", "문경", "경산", "군위", "의성", "청송", "영양", "영덕", "청도", "고령", "성주", "칠곡", "예천", "봉화", "울진", "울릉", "창원", "의창", "성산", "마산", "합포", "회원", "진해", "진주", "통영", "사천", "김해", "밀양", "거제", "양산", "의령", "함안", "창녕", "고성", "남해", "하동", "산청", "함양", "거창", "합천", "제주", "서귀포", "대전", "인천", "대구", "울산", "경기", "전라", "경북", "경남", "경상", "충청", "충북", "충남", "전남", "전북"};
		for (String dl : domesticLocations) {
			result = location.contains(dl);
			if (result) return result;
		}
		return result;
	}

	public static String runShellCommand(String[] commands) {
		String s = null;
		StringBuffer result = new StringBuffer();
		Process p;
		try {
			//이 변수에 명령어를 넣어주면 된다.
//            String[] cmd = {"/bin/sh","-c","ps -ef | grep tomcat"};
			p = Runtime.getRuntime().exec(commands);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((s = br.readLine()) != null) {
				System.out.println(s);
				result.append(s);
			}
			p.waitFor();
			p.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public static MapData paging(int page, int size) {
		MapData paging = new MapData();
		page = page == 1 ? 0 : ((page - 1) * size);

		paging.set("page", page);
		paging.set("size", size);

		return paging;
	}

	public static void main(String[] args) {
		String[] cmd = {"ls"};
		System.out.println(runShellCommand(cmd));
	}

	public static List<MapData> pictureFileList(List<MapData> fileList) {
		String[] picTypeArr = { ".BMP", ".RLE", "DIB", ".JPEG", ".JPG"
				,".GIF", ".PNG", ".TIF", ".TIFF", ".RAW", ".WEBP" };

		// imgUrl의 확장자 체크해서 이미지인지 동영상인지 확인
		for (MapData file : fileList) {
			String imgUrl = (String) file.get("imgUrl");
			imgUrl = imgUrl.substring(imgUrl.lastIndexOf("."));
			if(Arrays.asList(picTypeArr).contains(imgUrl.toUpperCase())) {
				file.set("picYN", "Y");
			} else {
				file.set("picYN", "N");
			}
		}

		// stream으로 짠건데 stream 중복으로 생성 한 것 같아서 주석처리
//			fileList.stream().forEach(file -> {
//				String imgUrl = (String) file.get("imgUrl");
//				imgUrl = imgUrl.substring(imgUrl.lastIndexOf("."));
//				if(Arrays.asList(picTypeArr).contains(imgUrl.toUpperCase())) {
//					file.set("picYN", "Y");
//				} else {
//					file.set("picYN", "N");
//				}
//			});

		return fileList.stream().filter(file -> file.get("picYN").toString().equals("Y")).collect(Collectors.toList());
	}

	public static String uploadRstMsg(int stCd, boolean isImage) {
		String rstMsg = "";

		switch (stCd) {
			case 0:
				rstMsg = null;
				break;
			case 1:
				if (isImage) {
					rstMsg = "사진 업로드 성공";
				} else {
					rstMsg = "동영상 업로드 성공";
				}
				break;
			case 5:
				rstMsg = "사진 개수 초과.";
				break;
			case 6:
				rstMsg = "동영상 개수 초과";
				break;
			case 7:
				rstMsg = "동영상 길이 초과";
				break;
			case 8:
				rstMsg = "앨범 용량 초과";
				break;
			case 9:
				if (isImage) {
					rstMsg = "사진 업로드 실패";
				} else {
					rstMsg = "동영상 업로드 실패";
				}
				break;
			default:
				break;
		}

		return rstMsg;
	}

}
