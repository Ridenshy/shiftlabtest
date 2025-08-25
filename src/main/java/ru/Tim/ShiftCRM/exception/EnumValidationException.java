package ru.Tim.ShiftCRM.exception;

import lombok.Getter;

@Getter
public class EnumValidationException extends RuntimeException {

    private final String errorMessage;

    public EnumValidationException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;

    }

}