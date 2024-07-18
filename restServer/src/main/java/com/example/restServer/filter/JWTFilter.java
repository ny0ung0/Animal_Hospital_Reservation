
package com.example.restServer.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.restServer.entity.Login;
import com.example.restServer.security.CustomUserDetails;
import com.example.restServer.security.JWTUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }
    
    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/api/v1/near-vet-list",
            "/api/v1/vet-list",
            "/api/v1/common/**"
        );


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		System.out.println("JWT필터 들어옴");		
				//request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");
				
				//Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            //System.out.println("token null");
            filterChain.doFilter(request, response);
						
						//조건이 해당되면 메소드 종료 (필수)
            return;
        }
        // 공용 엔드포인트에 대해 토큰 검사를 생략합니다.
       
			
        //System.out.println("authorization now");
				//Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];
        try {
            // 토큰 소멸 시간 검증 은 try/catch에서 catch로 잡아냄


            // 토큰에서 username과 role 획득
            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);

            // userEntity를 생성하여 값 set
            Login user = new Login();
            user.setUsername(username);
            user.setPassword("temppassword");
            user.setRole(role);

            // UserDetails에 회원 정보 객체 담기
            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            // 스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            // 세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (ExpiredJwtException e) {
            // 만료된 토큰 처리
        	String encodedMsg = URLEncoder.encode("토큰기한만기", StandardCharsets.UTF_8.toString());
        	response.addHeader("msg", encodedMsg);
        	//response.addHeader("Access-Control-Expose-Headers", "msg");
            System.out.println("ExpiredJwtException: Token expired"); // 콘솔 출력
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Token expired\"}");
            response.getWriter().flush();
            response.getWriter().close();
            return;
        } catch (Exception e) {
            // 기타 오류 처리
            System.out.println("Exception: Invalid token"); // 콘솔 출력
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Invalid token\"}");
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }

        filterChain.doFilter(request, response);
    }
}