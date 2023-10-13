package com.iandna.project.util;

import com.iandna.project.config.model.MapData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;






public class KakaoLocationUtil {
	private static final Logger logger = LoggerFactory.getLogger(KakaoLocationUtil.class);
	
	private final static String restApiKey = "af415d8e387738c3afa3c5ca79369aac";
//	private final String restApiKey = "dfb8c149759f45edae7ba2b322ede4c6";
	private final static String kakaoApiUrl = "https://dapi.kakao.com";
	private final String path = "/v2/local/search/address.json";
	
	public HttpURLConnection pushConnection(String param) {
		HttpURLConnection conn;
		
		try {
			URL url = new URL(kakaoApiUrl+path+"?"+param);
			conn = (HttpURLConnection) url.openConnection();
			
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
//			conn.setRequestProperty("accept", "application/json; charset=utf-8");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			conn.setRequestProperty("Authorization", "KakaoAK "+restApiKey);
		} catch (Exception e) {
			conn = null;
		}
		return conn;
	}
	
	public static HttpURLConnection getConnection(String path) {
		HttpURLConnection conn;
		
		try {
			URL url = new URL(kakaoApiUrl + path);
			System.out.println(url);
			conn = (HttpURLConnection) url.openConnection();
			
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
//			conn.setRequestProperty("accept", "application/json; charset=utf-8");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			conn.setRequestProperty("Authorization", "KakaoAK "+restApiKey);
		} catch (Exception e) {
			conn = null;
		}
		return conn;
	}
	
	public MapData getMapXy(MapData param) {
		String query = param.get("address").toString();
		String parameter = null;
		try {
			parameter = String.format("query=%s", URLEncoder.encode(query, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		KakaoLocationUtil testLoUtil = new KakaoLocationUtil();
		HttpURLConnection conn = testLoUtil.pushConnection(parameter);
		
		try {
			int status = 0;
			if (null != conn) {
				status = conn.getResponseCode();
				logger.info("*** ResponseCode : " + conn.getResponseCode() + " ***");
				logger.info("*** ResponseMessage : " + conn.getResponseMessage() + " ***");
			}
			if (status == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				// net.sf.json.JSONObject job = new JSONObject(response.toString());
				logger.info(response.toString());
				JSONParser jsonParser = new JSONParser(); 
				try {
					JSONObject job = (JSONObject) jsonParser.parse(response.toString());
					JSONArray docuArray = (JSONArray) job.get("documents");
					if(docuArray.size() != 0) {
						JSONObject docuObject = (JSONObject) docuArray.get(0); 
						String mapX = docuObject.get("x").toString();
						String mapY = docuObject.get("y").toString();
						param.set("mapX", mapX);
						param.set("mapY", mapY);
					}else {
						logger.error("mapX && mapY search Failed.");
						return param;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				in.close();
			} else if (status >= 401) {
				logger.error("error status : "+status);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return param;
    }
	
	public static MapData getLocation(MapData param) throws IOException {
		String path = "/v2/local/geo/coord2address.json?x=" + param.getString("x") + "&y=" + param.getString("y") + "&input_coord="+param.getString("input_coord");
		HttpURLConnection conn = getConnection(path);
		int status = 0;
		if (null != conn) {
			status = conn.getResponseCode();
			logger.info("*** ResponseCode : " + conn.getResponseCode() + " ***");
			logger.info("*** ResponseMessage : " + conn.getResponseMessage() + " ***");
		}
		
		if (status == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			// net.sf.json.JSONObject job = new JSONObject(response.toString());
			logger.info(response.toString());
			/*
			JSONParser jsonParser = new JSONParser(); 
			try {
				JSONObject job = (JSONObject) jsonParser.parse(response.toString());
				JSONArray docuArray = (JSONArray) job.get("documents");
				if(docuArray.size() != 0) {
					JSONObject docuObject = (JSONObject) docuArray.get(0); 
					String mapX = docuObject.get("x").toString();
					String mapY = docuObject.get("y").toString();
					param.set("mapX", mapX);
					param.set("mapY", mapY);
				}else {
					logger.error("mapX && mapY search Failed.");
					return param;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			*/
			in.close();
		} else if (status >= 401) {
			logger.error("error status : "+status);
		}
		return null;
	}
	
	public static void main(String[] args) throws IOException {
		MapData param = new MapData();
//		param.set("x", 37/1 + (60 * 30/1 + 36/1) / 3600);
//		param.set("y", 127/1 + (60 * 3/1 + 19/1) / 3600);
		
//		param.set("x", 37.51);
//		param.set("y", 127.0552777777778);
		param.set("input_coord", "CONGNAMUL");
		param.set("x", 123.9325);
		param.set("y", 10.255278);
		getLocation(param);
	}
}