package com.example.restServer.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.restServer.entity.Login;

public class CustomUserDetails implements UserDetails {

    private final Login login;

    public CustomUserDetails(Login login) {

        this.login = login;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return login.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {

        return login.getPassword();
    }

    @Override
    public String getUsername() {

        return login.getUsername();
    }
    
    public String getRole() {
    	return login.getRole();
    }
    
    public Long getMemberId() {
    	return login.getMember().getId();
    	}

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }
}
