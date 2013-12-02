package com.pstu.dtl.server.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/** Достаёт информацию об авторизованном спрингом пользователе из сесси */
public class CurrentUser {

    public final static String ROLE_USER = "ROLE_USER";
    public final static String ROLE_ADMIN = "ROLE_ADMIN";

    static public Boolean isAuthenticated() {
        Authentication token = SecurityContextHolder.getContext().getAuthentication();
        return token != null;
    }

    static public Long getId() {
        Authentication token = SecurityContextHolder.getContext() == null ? null : SecurityContextHolder.getContext().getAuthentication();
        if (token != null && token.getPrincipal() != null && token.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails principal = (CustomUserDetails) token.getPrincipal();
            return principal.getId();
        }
        return null;
    }

    static public String getLogin() {
        try {
            Authentication token = SecurityContextHolder.getContext() == null ? null : SecurityContextHolder.getContext().getAuthentication();
            if (token != null && token.getPrincipal() != null && token.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails principal = (CustomUserDetails) token.getPrincipal();
                return principal.getUsername();
            }
        }
        catch (Exception e) {
            System.out.println("CurrentUser.getLogin() " + e.getMessage());
        }
        return null;
    }

    static public Boolean hasRole(String role) {
        Authentication token = SecurityContextHolder.getContext() == null ? null : SecurityContextHolder.getContext().getAuthentication();
        if (token != null && token.getPrincipal() != null && token.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails principal = (CustomUserDetails) token.getPrincipal();
            for (GrantedAuthority authority : principal.getAuthorities())
                if (authority.getAuthority().equalsIgnoreCase(role)) return true;
        }
        return false;
    }
}
