package ru.Tim.ShiftCRM.api.advice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.Tim.ShiftCRM.api.exception.ContactInfoAlreadyExistsException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFound(Exception e) {
        log.warn("Не было найдено найдено данных. Сообщение: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgument(IllegalArgumentException e){
        log.warn("Переданные данные не верны. Сообщение: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ContactInfoAlreadyExistsException.class)
    public ResponseEntity<String> contactInfoAlreadyExists(ContactInfoAlreadyExistsException e){
        log.warn("Попытка создания нового продавца с существующей контактной информацией. Ошибка: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValid(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->{
                    String field = error.getField();
                    String message = error.getDefaultMessage();
                    log.warn("Ошибка валидации поля принимаемого DTO: {} - {}", field, message);
                    errors.put(field, message);
                }
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> constraintViolation(ConstraintViolationException e){
        String error = e.getConstraintViolations().iterator().next().getMessage();
        log.warn("Параметр передаваемый клиентом в API не прошел валидацию. Ошибка {}", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
