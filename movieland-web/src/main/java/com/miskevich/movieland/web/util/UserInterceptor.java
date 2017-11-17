package com.miskevich.movieland.web.util;

import com.miskevich.movieland.service.impl.UserSecurityService;
import com.miskevich.movieland.service.security.UserPrincipal;
import com.miskevich.movieland.web.security.SecurityHttpRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class UserInterceptor extends HandlerInterceptorAdapter {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private static final String GUEST_NICKNAME = "Guest";
    @Autowired
    private UserSecurityService userSecurityService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uuid = request.getHeader("uuid");
        String nickname;
        if (uuid != null) {
            Optional<UserPrincipal> userPrincipalFromCache = userSecurityService.getFromCache(uuid);
            if (userPrincipalFromCache.isPresent()) {
                UserPrincipal principal = userPrincipalFromCache.get();

                ((SecurityHttpRequestWrapper) request).setPrincipal(principal);
                nickname = principal.getUser().getNickname();
                MDC.put("nickname", nickname);
                MDC.put("requestId", uuid);
            }
        } else {
            nickname = GUEST_NICKNAME;
            LOG.debug("No \"uuid\" header in request");
            MDC.put("nickname", nickname);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.clear();
    }

}
