package com.miskevich.movieland.web.util;

import com.miskevich.movieland.entity.User;
import com.miskevich.movieland.service.impl.UserSecurityService;
import com.miskevich.movieland.web.security.SecurityHttpRequestWrapper;
import com.miskevich.movieland.web.security.UserPrincipal;
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
            Optional<User> userFromCache = userSecurityService.getFromCache(uuid);
            if (userFromCache.isPresent()) {
                User user = userFromCache.get();
                ((SecurityHttpRequestWrapper) request)
                        .setPrincipal(new UserPrincipal(user.getId(),
                                user.getNickname()));
                nickname = user.getNickname();
                MDC.put("nickname", nickname);
            }
        } else {
            nickname = GUEST_NICKNAME;
            LOG.info("No \"uuid\" header in request");
            MDC.put("nickname", nickname);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.clear();
    }

}
