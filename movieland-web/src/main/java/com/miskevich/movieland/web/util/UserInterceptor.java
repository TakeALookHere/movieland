package com.miskevich.movieland.web.util;

import com.miskevich.movieland.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

public class UserInterceptor extends HandlerInterceptorAdapter {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        //if (request.getMethod().equals(RequestMethod.POST.name())) {
            LOG.info("User sent request with email and password for login");
        //}
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){

            User principal = (User) authentication.getPrincipal();
            if(principal != null){
                LOG.info("PRINCIPAL: " + principal.getNickname());
            }
        }


        Principal userPrincipal = request.getUserPrincipal();
        String remoteUser = request.getRemoteUser();
        //request.login("ronald.reynolds66@example.com", "paco");
        if(remoteUser != null){
            LOG.info("REMOTEUser: " + remoteUser);
        }
        if(userPrincipal != null){
            LOG.info("PRINCIPAL: " + userPrincipal.getName());
        }

        //if ((request.getMethod().equals(RequestMethod.POST.name()) && (response.getStatus() == 200))) {
            LOG.info("Successful signing in for user");
       // }
    }
}
