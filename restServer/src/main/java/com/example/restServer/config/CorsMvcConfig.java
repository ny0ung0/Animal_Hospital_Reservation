package com.example.restServer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer{

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		 registry.addMapping("/**")
         .allowedOrigins("http://localhost:8093", "https://192.168.0.229:8093")
         .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
         .allowedHeaders("*")
         .exposedHeaders("msg","Authorization","MemberId","role","msgLogin")
         .allowCredentials(true)
         //.maxAge(3600) // 설정을 캐시하는 시간 (초 단위) 한시간 단위로 cors설정을 브라우저가 저장(캐시) 최적화를 위해 사용됨.시간동안 협상하지 않음 
         ;
	}
}
