package com.iandna.project.util;

import com.iandna.project.config.model.MapData;
import com.iandna.project.logic.main.MainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Component
public class RequestHandler {
	private final MainMapper mainMapper;

	@Autowired
	public RequestHandler(MainMapper mainMapper) {
		this.mainMapper = mainMapper;
	}

	public MapData getRequestData(HttpServletRequest request) {

		MapData paramData = new MapData();
		Enumeration<String> ee = request.getParameterNames();
		while (ee.hasMoreElements()) {

			String key = ee.nextElement();
			String value = request.getParameter(key);
			if (value == null)
				value = "";
			if (key != null && key.equals("frcsCustNo")) {
				if( value != null && !value.isEmpty() && value.split("@").length > 2) {
					value = mainMapper.selectFrcsCustNoByShopId(value);
				}else {
					value = StringUtils.replace(value, "%20", "");
					value = StringUtils.replace(value, " ", "");
				}
				
				paramData.set("custNo", "C" + value);
			}
			paramData.set(key, value);
		}

		return paramData;
	}
}
