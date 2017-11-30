package com.miskevich.movieland.web.util;

import com.miskevich.movieland.model.Role;
import com.miskevich.movieland.service.IUserService;
import com.miskevich.movieland.service.exception.AuthRequiredException;
import com.miskevich.movieland.service.impl.UserSecurityService;
import com.miskevich.movieland.service.security.UserPrincipal;
import com.miskevich.movieland.web.exception.InvalidAccessException;
import com.miskevich.movieland.web.security.RoleRequired;
import com.miskevich.movieland.web.security.SecurityHttpRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class UserInterceptor extends HandlerInterceptorAdapter {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private static final String GUEST_NICKNAME = "Guest";
    @Autowired
    private UserSecurityService userSecurityService;
    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uuid = request.getHeader("uuid");
        String nickname;
        Optional<Role[]> requiredRoles = isRoleRequired(handler);

        if (uuid != null) {
            Optional<UserPrincipal> userPrincipalFromCache = userSecurityService.getFromCache(uuid);
            if (userPrincipalFromCache.isPresent()) {
                UserPrincipal principal = userPrincipalFromCache.get();

                ((SecurityHttpRequestWrapper) request).setPrincipal(principal);
                nickname = principal.getUser().getNickname();
                MDC.put("nickname", nickname);
                MDC.put("requestId", uuid);

                requiredRoles.ifPresent(roleRequired -> validateRole(roleRequired, principal));
            }
        } else {
            if (requiredRoles.isPresent()) {
                String message = "Request header doesn't contain uuid";
                LOG.warn(message);
                throw new AuthRequiredException(message);
            }
            nickname = GUEST_NICKNAME;
            LOG.debug("No \"uuid\" header in request");
            MDC.put("nickname", nickname);
        }
        return true;
    }

    private Optional<Role[]> isRoleRequired(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RoleRequired annotation = method.getAnnotation(RoleRequired.class);
            if (annotation != null) {
                Role[] requiredRoles = annotation.value();
                return Optional.of(requiredRoles);
            }
        }
        return Optional.empty();
    }

    boolean validateRole(Role[] requiredRoles, UserPrincipal principal) {
        Role userRole = userService.getRole(principal.getUser().getId());
        for (Role requiredRole : requiredRoles) {
            if (userRole.equals(requiredRole)) {
                return true;
            }
        }
        String message = "Validation of user's role access type failed, required role: " + Arrays.toString(requiredRoles);
        LOG.warn(message);
        throw new InvalidAccessException(message);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.clear();
    }

}
