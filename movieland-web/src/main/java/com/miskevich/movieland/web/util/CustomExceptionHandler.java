package com.miskevich.movieland.web.util;

import com.miskevich.movieland.service.exception.AuthRequiredException;
import com.miskevich.movieland.service.exception.UuidExpirationException;
import com.miskevich.movieland.web.dto.ErrorDto;
import com.miskevich.movieland.web.exception.InvalidAccessException;
import com.miskevich.movieland.web.exception.InvalidUserException;
import com.miskevich.movieland.web.json.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class CustomExceptionHandler extends DefaultHandlerExceptionResolver {

    private static final String ILLEGAL_ARGUMENT = "Invalid Query Parameter";
    private static final String INVALID_USER = "Invalid User Details";
    private static final String INVALID_ACCESS = "Invalid Access Rights";
    private static final String AUTHORIZATION_REQUIRED = "Authorization Required";
    private static final String UUID_EXPIRED = "Uuid Expired";

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(IllegalArgumentException.class)
    void handlerIllegalArgument(HttpServletResponse response, IllegalArgumentException exception) {
        populateErrorData(ILLEGAL_ARGUMENT, HttpStatus.BAD_REQUEST.value(), response, exception);
    }

    @ExceptionHandler(InvalidUserException.class)
    void handlerInvalidUser(HttpServletResponse response, InvalidUserException exception) {
        populateErrorData(INVALID_USER, HttpStatus.BAD_REQUEST.value(), response, exception);
    }

    @ExceptionHandler(InvalidAccessException.class)
    void handlerInvalidAccess(HttpServletResponse response, InvalidAccessException exception) {
        populateErrorData(INVALID_ACCESS, HttpStatus.BAD_REQUEST.value(), response, exception);
    }

    @ExceptionHandler(AuthRequiredException.class)
    void handlerAuthRequeired(HttpServletResponse response, AuthRequiredException exception) {
        populateErrorData(AUTHORIZATION_REQUIRED, HttpStatus.UNAUTHORIZED.value(), response, exception);
    }

    @ExceptionHandler(UuidExpirationException.class)
    void handlerUuidExpired(HttpServletResponse response, UuidExpirationException exception) {
        populateErrorData(UUID_EXPIRED, HttpStatus.UNAUTHORIZED.value(), response, exception);
    }

    private void populateErrorData(String errorTitle, int httpStatusCode,
                                   HttpServletResponse response, RuntimeException exception) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setTitle(errorTitle);
        errorDto.setDetail(exception.getMessage());

        String errorJson = JsonConverter.toJson(errorDto);
        System.out.println(errorJson);
        response.setStatus(httpStatusCode);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try {
            response.getWriter().write(errorJson);
        } catch (IOException e) {
            LOG.warn("Error during sending error response: ", e);
            throw new RuntimeException(e);
        }
    }
}
