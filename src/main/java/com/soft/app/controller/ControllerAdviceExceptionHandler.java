package com.soft.app.controller;

import com.soft.app.spring.configuration.CustomException;
import com.soft.app.spring.security.CustomUserDetailsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerAdviceExceptionHandler {
    private  final Logger LOGGER = LogManager.getLogger(this.getClass());

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(Exception e) {
        //must log exception msg or something useful
        LOGGER.error("Error : {}",e.getMessage(),e);
        return e.getMessage();
    }

    @ExceptionHandler({CustomException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleCustomException(CustomException ex) {
        //must log exception msg or something useful
        return ex.getCustomMessage();
    }

}
