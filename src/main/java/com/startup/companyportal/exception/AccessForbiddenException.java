package com.startup.companyportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AccessForbiddenException extends Exception{

    private static final long serialVersionUID = 1L;

    public AccessForbiddenException(String message) {
        super(message);
    }
}
