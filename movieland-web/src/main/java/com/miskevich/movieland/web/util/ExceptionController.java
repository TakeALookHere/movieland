package com.miskevich.movieland.web.util;

import com.miskevich.movieland.web.dto.ErrorDto;
import com.miskevich.movieland.web.exception.InvalidAccessException;
import com.miskevich.movieland.web.exception.InvalidUserException;
import com.miskevich.movieland.web.json.JsonConverter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ExceptionController extends DefaultHandlerExceptionResolver {

    private static final String ILLEGAL_ARGUMENT = "Invalid Query Parameter";
    private static final String INVALID_USER = "Invalid User Details";
    private static final String INVALID_ACCESS = "Invalid Access Rights";

    @ExceptionHandler({IllegalArgumentException.class, InvalidUserException.class, InvalidAccessException.class})
    void handler(HttpServletResponse response, RuntimeException exception) throws IOException {
        ErrorDto errorDto = new ErrorDto();
        if (exception instanceof IllegalArgumentException) {
            errorDto.setTitle(ILLEGAL_ARGUMENT);
        } else if (exception instanceof InvalidUserException) {
            errorDto.setTitle(INVALID_USER);
        } else if (exception instanceof InvalidAccessException) {
            errorDto.setTitle(INVALID_ACCESS);
        }
        errorDto.setDetail(exception.getMessage());

        String errorJson = JsonConverter.toJson(errorDto);

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(errorJson);
    }
}
