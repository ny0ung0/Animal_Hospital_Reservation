package com.example.restServer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class Securityconfig {
	
	private final AuthenticationConfiguration authenticationConfiguration;
	//JWTUtil 주입
	//private final JWTUtil jwtUtil;
	
	public Securityconfig(AuthenticationConfiguration authenticationConfiguration) {

        this.authenticationConfiguration = authenticationConfiguration;
		//this.jwtUtil = jwtUtil;
    }
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		/*		//config 클래스로 따로 빼놨음 그래서 @CrossOrigin을 안써도 됨
		http
        .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:9001/"));
                configuration.setAllowedMethods(Collections.singletonList("*"));
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setMaxAge(36000000L);

				configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                return configuration;
            }
        })));
		
		*/
		//csrf 비활성
		http.csrf(auth -> auth.disable());
		//basic 로그인 비활성
		http.httpBasic(auth -> auth.disable());
		//form 로그인 비활성
		http.formLogin(auth -> auth.disable());
		
		//인가 설정
		
		http
			.authorizeHttpRequests(auth -> auth
					//.requestMatchers("/", "/login", "/join", "/images/**").permitAll()
					//.requestMatchers("/v3/**","/swagger-ui/**").permitAll()
					//.requestMatchers("/manager/**").hasRole("MANAGER")
					//.requestMatchers("/reporter/**", "/api/**").hasAnyRole("REPORTER", "MANAGER")
					.anyRequest().permitAll());
		/*	
		//세션 설정 : Stateless
		http
			.sessionManagement((session) -> session
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		//token 사용을 위한 Filter 적용(JWTFilter, LoginFilter)
		http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
		
		 http
         	.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
		*/	
		return http.build();
	}
}