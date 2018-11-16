package com.bk.todos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TodoException extends RuntimeException {

    public TodoException(String message) {
        super(message);
    }

}
