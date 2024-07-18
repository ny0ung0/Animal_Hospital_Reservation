package com.example.restServer.filter;

import org.springframework.security.core.AuthenticationException;

public class UserNotApprovedException extends AuthenticationException {
   
	
	public UserNotApprovedException(String message) {
        super(message);
    }

	
	
	
}
