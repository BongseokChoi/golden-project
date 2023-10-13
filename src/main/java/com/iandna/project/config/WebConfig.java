package com.iandna.project.config;

import com.iandna.project.config.interceptor.CommonInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.*;

import java.util.*;

@Configuration
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{ 
	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
	@Autowired
	private CommonInterceptor commInterceptor;
	
	// CommonInterceptor 태울 URL 정의
	private static final List<String> COMMON_URL_PATTERNS = Arrays.asList("/api/**/*".split("\\,"));
	
	// 인터셉테 제외 URL 정의
//	private static final List<String> EXCLUDE_URL_PATTERNS = Arrays.asList("/gateway/**/*");
	
	// 정적 리소스 정의
	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
	  	      "classpath:/META-INF/resources/",
	  	      "classpath:/static/",
	  	      "classpath:/public/",
	  	      "classpath:/META-INF/resources/webjars/",
	  	      "classpath:/resources/"
	  	      //"/resources/"
	};
    
	// 인터셉터별 check url 기술
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(commInterceptor).addPathPatterns(COMMON_URL_PATTERNS)
						.excludePathPatterns("/api/health/*")
						.excludePathPatterns("/**/*error")
		;
	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/resources/**").addResourceLocations("resources/");
	    registry.addResourceHandler("/resource/**").addResourceLocations("WEB-INF/resources/");
		if (!registry.hasMappingForPattern("/**")) {
		      registry.addResourceHandler("/**").addResourceLocations(
		          CLASSPATH_RESOURCE_LOCATIONS);
		}

        registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("gateway/swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**").exposedHeaders("authToken");
            	registry.addMapping("/**").allowedOrigins("*")
            	.allowedMethods(HttpMethod.GET.name(),
	            			HttpMethod.HEAD.name(),
	            	    	HttpMethod.POST.name(),
	            	    	HttpMethod.PUT.name(),
	            	    	HttpMethod.DELETE.name(),
											HttpMethod.PATCH.name()
            	);
            }
        };
    }
}
