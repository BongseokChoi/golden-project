package com.iandna.project.config.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component(value="eprops")
@ConfigurationProperties(prefix="error")
@PropertySource(value = {"classpath:/errorMessage.properties"})
@Getter
@Setter
public class CustomErrorProperties {
	private static final Log logger = LogFactory.getLog (CustomErrorProperties.class);
	public CustomErrorProperties() {
		logger.debug("customErrorProperties created.");
	}
	private String E999;
	
	private String abcd = "ddd";
	
	
}
