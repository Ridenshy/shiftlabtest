package ru.Tim.ShiftCRM.exception;

public class ContactInfoAlreadyExistsException extends RuntimeException {
    public ContactInfoAlreadyExistsException(String message) {
        super(message);
    }
}
