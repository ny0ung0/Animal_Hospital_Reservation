package com.example.restServer.filter;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.restServer.repository.MemberRepository;
import com.example.restServer.security.CustomUserDetails;
import com.example.restServer.security.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;

	
	
	public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {

		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		// 클라이언트 요청에서 username, password 추출
		String username = obtainUsername(request);
		//System.out.println("로그인필터 유저네임 : "+ username);
		String password = obtainPassword(request);

		// 스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password,
				null);

		// token에 담은 검증을 위한 AuthenticationManager로 전달
		return authenticationManager.authenticate(authToken);
	}

	// 로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) {
		//Authentication은 인증된 사용자의 세부 정보를 나타냄
		// UserDetails
		//System.out.println("successfulAuthentication메서드 들어옴");
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

		String username = customUserDetails.getUsername();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();

		String role = auth.getAuthority();

		String token = jwtUtil.createJwt(username, role, 10*60*60*1000L);
		
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("MemberId", customUserDetails.getMemberId()+"");
		response.addHeader("role", role);
		//위에 설정한 헤더 데이터를 클라이언트가 접근할 수 있도록 허용하는 코드
		response.addHeader("Access-Control-Expose-Headers", "Authorization, MemberId, role");
	}

	

	// 로그인 실패시 실행하는 메소드
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) {
		 response.setStatus(401);
	}
}