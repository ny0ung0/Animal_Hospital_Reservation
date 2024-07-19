package com.example.veiwServer.filter;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAjaxFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("커스텀 필터 초기화....");
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        //CustomHttpServletResponseWrapper customResponse = new CustomHttpServletResponseWrapper((HttpServletResponse) response);
		HttpServletResponse httpResponse =(HttpServletResponse) response;
        System.out.println("필터들어옴");
        // 요청을 필터 체인으로 넘김
        chain.doFilter(request, response);
        System.out.println("필터들어옴2");
        // 응답 헤더 검사 및 처리
        String msg = httpResponse.getHeader("Msg");
              	
        	System.out.println("msg=" + msg);
      
        

			/*
			 * // 응답을 클라이언트로 전달 byte[] responseArray = httpResponse.toByteArray();
			 * response.getOutputStream().write(responseArray);
			 */
    
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		Filter.super.destroy();
	}
	
	

}
