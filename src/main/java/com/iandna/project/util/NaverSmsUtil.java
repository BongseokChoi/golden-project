package com.iandna.project.util;

import com.iandna.project.config.model.MapData;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NaverSmsUtil {
	private static final Logger logger = LoggerFactory.getLogger(NaverSmsUtil.class);
	
	private NaverSmsUtil apu = null;
	private final String fcmUrl = "https://api-sens.ncloud.com/v1/sms/services/";
	private final String acsKey = "bQ5UWlXeEoHB0h1ecUkA";
	private final String secKey = "4c4085bbb7d74764b6d13dd0b92efed4";
	private final String serviceId = "ncp:sms:kr:255525415653:i-vory";
	private final String path = "/messages";
	private final String callingNumber = "0262051315";
	public NaverSmsUtil getApu() {
		apu = new NaverSmsUtil();
		return apu;
	}


	public void setApu(NaverSmsUtil apu) {
		this.apu = apu;
	}


//	네이버 클라우드 커넥션 생성
	private HttpURLConnection pushConnection(String fcmUrl, String serviceId, String acsKey, String secKey, String path) {
		HttpURLConnection conn;
		
		try {
			URL url = new URL(fcmUrl+serviceId+path);
			conn = (HttpURLConnection) url.openConnection();
			
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("accept", "application/json; charset=utf-8");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			conn.setRequestProperty("x-ncp-auth-key", acsKey);
			conn.setRequestProperty("x-ncp-service-secret", secKey);
			
		} catch (Exception e) {
			conn = null;
		}

		return conn;
	}
	
	
	public void executeShell(String linuxCmd, String windowCmd) {
		Process process = null;
		Runtime runtime = Runtime.getRuntime();
		StringBuffer successOutput = new StringBuffer();
		StringBuffer errorOutput = new StringBuffer();
		BufferedReader successBufferReader = null;
		BufferedReader errorBufferReader = null;
		String msg = null;

		List<String> cmdList = new ArrayList<String>();

		// 운영체제 구분 (window, window 가 아니면 무조건 linux 로 판단)
		if (System.getProperty("os.name").indexOf("Windows") > -1) {
			cmdList.add("cmd");
			cmdList.add("/c");
			cmdList.add(windowCmd);
		} else {
			cmdList.add("/bin/sh");
			cmdList.add("-c");
			cmdList.add(linuxCmd);
		}
		// 명령어 셋팅
		String[] array = cmdList.toArray(new String[cmdList.size()]);

		try {

			// 명령어 실행
			process = runtime.exec(array);

			// shell 실행이 정상 동작했을 경우
			successBufferReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "EUC-KR"));

			while ((msg = successBufferReader.readLine()) != null) {
				successOutput.append(msg + System.getProperty("line.separator"));
			}

			// shell 실행시 에러가 발생했을 경우
			errorBufferReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "EUC-KR"));
			while ((msg = errorBufferReader.readLine()) != null) {
				errorOutput.append(msg + System.getProperty("line.separator"));
			}

			// 프로세스의 수행이 끝날때까지 대기
			process.waitFor();

			// shell 실행이 정상 종료되었을 경우
			if (process.exitValue() == 0) {
				logger.info("ExcuteSuccess");
				logger.info(successOutput.toString());
			} else {
				// shell 실행이 비정상 종료되었을 경우
				logger.error("ExcuteFail");
				logger.error(successOutput.toString());
			}

			// shell 실행시 에러가 발생
			// if (CommonUtil.notEmpty(errorOutput.toString())) {
			// // shell 실행이 비정상 종료되었을 경우
			// logger.debug("오류");
			// logger.debug(successOutput.toString());
			// }

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				process.destroy();
				if (successBufferReader != null)
					successBufferReader.close();
				if (errorBufferReader != null)
					errorBufferReader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}
	
	
	
	
	
	public int sendNaverSms(MapData param) {
		
		String type = param.getString("type");
		String contentType = param.getString("contentType");
		String countryCode = param.getString("countryCode");
		String from = param.getString("from");
		String subject = param.getString("subject");
		String content = param.getString("content");
		
		
		ArrayList<String> to = new ArrayList<String>();
		to.add(param.getString("to"));
		JSONObject infoJson = new JSONObject();

		int status = 0;
		
		infoJson.put("type", type);
		infoJson.put("contentType", contentType);
		infoJson.put("countryCode", countryCode);
		infoJson.put("from", from);
		infoJson.put("subject", subject);
		infoJson.put("content", content);
		infoJson.put("to", to);
		HttpURLConnection conn = pushConnection(fcmUrl, serviceId, acsKey, secKey, path);
		try {
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

			ArrayList<String> toList = new ArrayList<String>();
			
			wr.write(infoJson.toString());
			wr.flush();
			wr.close();
			if (null != conn) {
				status = conn.getResponseCode();
				logger.info("*** ResponseCode : " + conn.getResponseCode() + " ***");
				logger.info("*** ResponseMessage : " + conn.getResponseMessage() + " ***");
			}

			if (status == 202) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				// 푸시 못받는 에러 처리(앱삭제)
				JSONParser parser = new JSONParser();
				String tset = response.toString();
				Object obj = parser.parse(response.toString());
				JSONObject jsonObj = (JSONObject) obj;
				// push_cnt += Integer.parseInt(jsonObj.get("success").toString());
				// cntFailure += Integer.parseInt(jsonObj.get("failure").toString());

				// JSONArray jsonArr = (JSONArray)jsonObj.get("results");
				// appDeleteCommit( sendListUnit, jsonArr );

				// print result
				// logger.debug("status200 :: "+response.toString());
			} else if (status >= 401) {
				// 전송오류 업데이트
			}
			
		} catch (Exception e) {
			logger.error("*** 네이버 문자전송 오류 ***", e);
		} finally {
			conn.disconnect();
		}
		return status;
	}
	
	public static void main(String[] args) {
		NaverSmsUtil nsu = new NaverSmsUtil();
		MapData smsParam = new MapData();
		smsParam.set("type", "LMS");
		smsParam.set("contentType", "COMM");
		smsParam.set("countryCode", "82");
		smsParam.set("from", "0262051315");
		smsParam.set("subject", "[아이보리 케어팩 가입 안내]");
		smsParam.set("content", "아이보리 케어팩 주문이 완료되었습니다.\r\n\r\n"
				+ "- 베베짤 유료서비스\r\n"
				+ "- 하이키즈 상해무료보험\r\n"
				+ "로타바이러스/RS바이러스 감염증\r\n"
				+ "진단금 10만원 (보험 보장기간 서비스팩 가입 신청 00시 부터 30일간)\r\n"
				+ "- 벨레다 카렌듈라 3종세트\r\n\r\n"
				+ "*베베짤 사용은 안드로이드 유저만 가능합니다.\r\n"
				+ "*보험관련 자세한 내용은 상품설명서와 약관을 참조하시기 바랍니다.\r\n"
				+ "*벨레다 3종세트는 고시일로부터 4-6일이내 발송됩니다.");
		smsParam.set("to", "01092884247");
		nsu.sendNaverSms(smsParam);
	}
}