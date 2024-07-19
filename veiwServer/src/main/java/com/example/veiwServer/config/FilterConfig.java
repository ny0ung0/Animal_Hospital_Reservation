package com.example.veiwServer.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.veiwServer.filter.CustomAjaxFilter;

@Configuration
public class FilterConfig {

	
	@Bean
	public FilterRegistrationBean<CustomAjaxFilter> firstFilter(){
		FilterRegistrationBean<CustomAjaxFilter> registrationBean = new FilterRegistrationBean<>();
		CustomAjaxFilter customAjaxFilter = new CustomAjaxFilter();
		
		registrationBean.setFilter(customAjaxFilter);
		registrationBean.addUrlPatterns("/*"); //이러면 ajax로 보내는 요청으로 알아서 바뀜
		registrationBean.setOrder(1); //우선순위 지정, 숫자가 작을수록 높은 우선순위
		
		
		
		return registrationBean;
		
	}
	
}
