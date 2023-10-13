package com.iandna.project.config.interceptor;

import com.iandna.project.config.exception.LoginException;
import com.iandna.project.config.model.CustomErrorProperties;
import com.iandna.project.config.model.MapData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class CommonInterceptor extends HandlerInterceptorAdapter {
	
	private static final Log logger = LogFactory.getLog (CommonInterceptor.class);
	private long curTimeMillis = 0L;
	
	@Autowired
	CustomErrorProperties eProps;
	@Autowired
	CommonFunction commonFunction;
	@Autowired
	InterceptorUtil interceptorUtil;
	
	public CommonInterceptor() {
		logger.debug("commonInterceptor created.");
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpServletRequest requestWrapper = new ContentCachingRequestWrapper(request);

		logger.debug(request.getRequestURI());
		curTimeMillis = System.currentTimeMillis();
		MapData paramData = commonFunction.getParameterMap(requestWrapper);
		paramData = interceptorUtil.checkCookies(paramData, requestWrapper);

		if(!paramData.getBoolean("procFlag")) {
			throw new LoginException("E902","로그인 토큰 검증에 실패하였습니다.");
		} else
			paramData.set("custNo", "C" + paramData.getString("frcsCustNo"));

		String reqUrl = new String(requestWrapper.getRequestURL());
		String os = paramData.getString("os");
		Map map = paramData;
		if(logger.isInfoEnabled()) {
			logger.info("req\t" + ((os == null || os.isEmpty()) ? "Q" : os) + "\t" + map.toString() + "\t" + paramData.getString("frcsCustNo") + "\t" + paramData.getString("login_token") + "\t" + reqUrl);
		}
		requestWrapper.setAttribute("paramData", paramData);
		logger.debug("preHandle...end");
		return super.preHandle(requestWrapper, response, handler);
//		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
		long endTimeMillis = System.currentTimeMillis();
		long elapsedTimeMillis = endTimeMillis - curTimeMillis;

		if(logger.isInfoEnabled()) {
			logger.info("### CommonInterceptor.postHandle:: Prosess Time [" + elapsedTimeMillis + "]ms");
		}

		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, @Nullable Exception arg3) throws Exception {
	}
}
