package com.iandna.project.config.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class FilterConfig {
	@Value("${security.oauth2.jwt.signkey}")
    private String signKey;
	@Bean
    public FilterRegistrationBean jwtVerifyFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new JwtVerifyFilter(signKey));
        bean.setUrlPatterns(Arrays.asList("/gateway/ivory/resources/test")); 
        return bean;
    } 
}
