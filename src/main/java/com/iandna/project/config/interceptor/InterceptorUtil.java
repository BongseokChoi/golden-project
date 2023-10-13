package com.iandna.project.config.interceptor;


import com.iandna.project.config.exception.LoginException;
import com.iandna.project.config.model.MapData;
import com.iandna.project.logic.main.MainMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class InterceptorUtil {

	private static final Log logger = LogFactory.getLog (InterceptorUtil.class);
	private final MainMapper mainMapper;
//	private static final String svr_chk_pass_key = EncryptUtil.standardPBEStringEncryptor().decrypt("Gu0WiFomn3+JbMNzRRRm5uCWSNduyzqpH9uK/VtYWdo=");
	@Value("${svr.passkey}")
	private String svr_chk_pass_key;

	@Value("${spring.profiles.active}")
	private String env;

	@Autowired
	public InterceptorUtil(MainMapper mainMapper) {
		this.mainMapper = mainMapper;
	}

	public MapData checkCookies(MapData paramData, HttpServletRequest request) throws Exception {
		String os = null;
		Cookie[] cookies = request.getCookies();
		boolean procFlag = false;
		
		// 로컬 테스트 환경 설정 - Request Header / test-id에 'C' 안붙은 사용자 아이디를 담아서 넘기면 됨 
		if (env.equals("local")) {
			String custNo = request.getHeader("test-id");
			paramData.set("frcsCustNo", custNo);
			paramData.set("procFlag", true);

			return paramData;
		}
		
		// 이미 세팅 되어 있는건, 앞단 jwt filter -> CommonFunction.getParameterMap 과정에서 토큰 검증이 된 것이기 때문에 패스
		if(paramData.getString("frcsCustNo") != null && !paramData.getString("frcsCustNo").isEmpty()) {
			procFlag = true;
		}
		
		if(paramData.getString("login_token") != null && !paramData.getString("login_token").isEmpty()) {
			procFlag = true;
			setFrcsCustNo(paramData);
		}
		
		logger.debug("########## " + request.getRequestURL());
		if(cookies != null && cookies.length > 0) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals("svr_chk_pass_key")) {
					if(!cookie.getValue().equals(svr_chk_pass_key)) {
						
						if(request.getRequestURI().contains(".json"))
							throw new LoginException("E806","잘못 된 서버키입니다.");
						else if(request.getRequestURI().contains(".do"))
							paramData.set("svrChkFlag", false);
						
					}else {
						procFlag = true;
						paramData.set("svrChkFlag", true);
						break;
					}
				}
				logger.debug("##########" + cookie.getName() + " : " + cookie.getValue());
				if(cookie.getName().equals("login_token") && !procFlag) {
					if(cookie.getValue() == null || cookie.getValue().isEmpty()) {
						continue;
					}
					paramData.set("login_token", cookie.getValue());
					procFlag = true;
					setFrcsCustNo(paramData);
					if(paramData.getString("frcsCustNo") == null || paramData.getString("frcsCustNo").isEmpty()) {
						logger.debug("frcsCustNo is null");
						cookie.setValue(null);
						
						if(request.getRequestURI().contains(".json"))
							throw new LoginException("E902","로그인 토큰 검증에 실패하였습니다."); 
					}else {
						logger.debug("frcsCustNo : " + paramData.getString("frcsCustNo"));
					}
				}
				if(cookie.getName().equals("os")) {
					os = cookie.getValue();
					paramData.set("os", os == null || os.isEmpty() ? "W" : os);
				}
			}
		}else {
			logger.debug("########## cookie null");
		}
		
		paramData.set("procFlag", procFlag);
		
		return paramData;
	}
	
	public void setFrcsCustNo(MapData param) throws Exception {
		try {
			String frcsCustNo = null;
			if(param.getString("cust_gbn_cd").equals("2")) {
				frcsCustNo = param.getString("newFrcsCustNo");
			}else {
				MapData token_info = mainMapper.selectCustTokenInfo(param);
				if(token_info == null) return;
				frcsCustNo = (token_info.getString("cust_no") == null) ? 
						null : StringUtils.replace(token_info.getString("cust_no"), "C", "");
			}
			if(frcsCustNo == null) {
				throw new LoginException("E902","로그인 토큰 검증에 실패하였습니다.");
			}
			param.set("frcsCustNo", frcsCustNo);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
