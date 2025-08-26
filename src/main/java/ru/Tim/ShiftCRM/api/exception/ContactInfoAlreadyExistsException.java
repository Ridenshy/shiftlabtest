package ru.Tim.ShiftCRM.api.exception;

public class ContactInfoAlreadyExistsException extends RuntimeException {
    public ContactInfoAlreadyExistsException(String message) {
        super(message);
    }
}
