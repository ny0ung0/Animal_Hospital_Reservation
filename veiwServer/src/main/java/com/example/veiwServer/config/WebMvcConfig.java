package com.example.veiwServer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Value("${spring.servlet.multipart.location}")
	String uploadPath;
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/images/user/**") // 웹 페이지에서 요청할 URL 경로
            .addResourceLocations("file:///" + uploadPath);// 로드할 이미지 파일의 실제 경로
    }
}
