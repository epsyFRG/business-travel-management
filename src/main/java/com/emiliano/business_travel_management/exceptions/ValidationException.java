package com.emiliano.business_travel_management.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
    private List<String> errorsMessages;

    public ValidationException(List<String> errorsMessages) {
        super("ci sono errori di validazione");
        this.errorsMessages = errorsMessages;
    }
}