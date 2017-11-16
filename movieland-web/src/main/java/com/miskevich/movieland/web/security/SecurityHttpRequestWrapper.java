package com.miskevich.movieland.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;

public class SecurityHttpRequestWrapper extends HttpServletRequestWrapper {

    public SecurityHttpRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    private Principal principal;

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }
}