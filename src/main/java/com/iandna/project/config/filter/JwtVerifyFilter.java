package com.iandna.project.config.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class JwtVerifyFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(JwtVerifyFilter.class);
	private String secret;
	
	public JwtVerifyFilter(String secret) {
		// TODO Auto-generated constructor stub
		this.secret = secret;
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        Cookie userToken = WebUtils.getCookie(req, "UserToken");
        if(userToken != null && userToken.getValue() != null && 
        		!userToken.getValue().equals("") && 
        		userToken.getValue().length() > 0) {
        	String accessToken = userToken.getValue();
            accessToken = StringUtils.substring(accessToken, 
            		StringUtils.indexOf(accessToken, "=")+1, StringUtils.indexOf(accessToken, "&"));
            if(verifyToken(accessToken, secret)) {
        		request.setAttribute("cust_gbn_cd", "2");
        		try {
        		JSONParser parser = new JSONParser();
        		String[] ensa = StringUtils.split(accessToken, ".");
        		String str = ensa[1];
        		byte[] sbyte = Base64.getDecoder().decode(str);
        		
				Object obj = parser.parse(new String(sbyte));
				JSONObject jsonObj = (JSONObject) obj;
        		request.setAttribute("newFrcsCustNo", jsonObj.get("user_name"));
        		request.setAttribute("frcsCustNo", jsonObj.get("user_name"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					logger.error("jwt parse ERROR");
					e.printStackTrace();
				}
            }else {
            	logger.info("######## jwt Filter redirect ########");
            	Cookie login_token = WebUtils.getCookie(req, "login_token");
            	if(login_token != null) {
            		logger.info("login_token : " + login_token.getValue());
            	}
//            	res.sendRedirect("https://i-vory.net/oauth/login");
//            	res.sendRedirect(req.getre);
            }
        } else {
        	request.setAttribute("cust_gbn_cd", "1");
        }
		chain.doFilter(request, response);
	}
	
	public static boolean verifyToken(String token, String secret) {
		if(token == null) return false;
		logger.debug(System.getProperty("user.dir"));
		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new FileSystemResource("key/oauth2jwt.jks"), "oauth2jwtpass".toCharArray());
		
		RSAPublicKey uKey = null;
		RSAPrivateKey rKey = null;
		
		
		uKey = (RSAPublicKey) keyStoreKeyFactory.getKeyPair("oauth2jwt").getPublic();
		rKey = (RSAPrivateKey) keyStoreKeyFactory.getKeyPair("oauth2jwt").getPrivate();
        try {
	        String result2 = JWT.require(Algorithm.RSA256(uKey, rKey))
	        .build()
	        .verify(token.replace("Bearer ", ""))
	        .getSubject();
	        //result
	        logger.debug(result2);
	        return true;
        }catch(Exception e) {
        	logger.error("token값 인증 오류" + e.getMessage());
        	return false;
        }
	}
	
}
