package com.soft.app.spring.security;

import org.springframework.security.core.GrantedAuthority;


public class AuthorityInfo implements GrantedAuthority {

    private static final long serialVersionUID = -8288989207224943091L;
    /**
     * 权限CODE
     */
    private String authority;

    public AuthorityInfo(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
