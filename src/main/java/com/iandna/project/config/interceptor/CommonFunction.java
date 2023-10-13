package com.iandna.project.config.interceptor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iandna.project.config.model.MapData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Map;

@Component
public class CommonFunction {

	private static final Log logger = LogFactory.getLog(CommonFunction.class);


	@Autowired
	InterceptorUtil interceptorUtil;

	// 파라미터 Value가 String 일 경우
	protected MapData getParameterMap(HttpServletRequest request) {
		String cust_gbn_cd = (request.getAttribute("cust_gbn_cd") == null) ? 
				"1" : request.getAttribute("cust_gbn_cd").toString();
		String newFrcsCustNo = (request.getAttribute("newFrcsCustNo") == null) ? null : request.getAttribute("newFrcsCustNo").toString();
		// 파라미터 이름
		Enumeration<?> paramNames = request.getParameterNames();
		// 저장할 맵
		MapData paramMap = new MapData();
		paramMap.set("cust_gbn_cd", cust_gbn_cd);
		if(cust_gbn_cd.equals("2")) {
			paramMap.set("frcsCustNo", newFrcsCustNo);
			paramMap.set("newFrcsCustNo", newFrcsCustNo);
		}
		
		// 요청 타입 분기용 인덱스
		int paramIndex = 0;
		// form-urlencoded type request 처리용
		while (paramNames.hasMoreElements()) {
			String name = paramNames.nextElement().toString();
			String value = request.getParameter(name);
			if (value == null)
				value = "";
			if (name != null && name.equals("frcsCustNo")) {
				value = StringUtils.replace(value, "%20", "");
				value = StringUtils.replace(value, " ", "");
			}
			logger.debug("####### " + name + " : " + value);
			paramMap.set(name, value);
			paramIndex++;
		}
		// application/json type request 처리용
		if(paramIndex == 0) {
			try {
				String body = null;
		        StringBuilder stringBuilder = new StringBuilder();
		        BufferedReader bufferedReader = null;
		        try {
		            InputStream inputStream = request.getInputStream();
		            if (inputStream != null) {
		                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		                char[] charBuffer = new char[128];
		                int bytesRead = -1;
		                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
		                    stringBuilder.append(charBuffer, 0, bytesRead);
		                }
		            }
		        } catch (IOException ex) {
		            throw ex;
		        } finally {
		            if (bufferedReader != null) {
		                try {
		                    bufferedReader.close();
		                } catch (IOException ex) {
		                    throw ex;
		                }
		            }
		        }
		        body = stringBuilder.toString();
		        ObjectMapper mapper = new ObjectMapper();
		        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		        if(body != null && !body.isEmpty()) {
		        	Map<String, String> map = mapper.readValue(body, Map.class);
			        paramMap.putAll(map);	
		        }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// 결과반환
		return paramMap;
	}

	// 파라미터 Value가 String[] 일 경우
	protected MapData getParameterMapArray(HttpServletRequest request) {

		// 파라미터 이름
		Enumeration<?> paramNames = request.getParameterNames();

		// 저장할 맵
		MapData paramMap = new MapData();

		// 맵에 저장
		while (paramNames.hasMoreElements()) {
			String name = paramNames.nextElement().toString();
			String[] value = request.getParameterValues(name);
			paramMap.set(name, value);
		}
		// 결과반환
		return paramMap;
	}
}
