package ru.Tim.ShiftCRM.api.advice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.Tim.ShiftCRM.api.model.error.ErrorResponse;
import ru.Tim.ShiftCRM.api.exception.ContactInfoAlreadyExistsException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFound(Exception e) {
        log.warn("Не было найдено найдено данных. Сообщение: {}", e.getMessage());
        ErrorResponse error = ErrorResponse.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgument(IllegalArgumentException e){
        log.warn("Переданные данные не верны. Сообщение: {}", e.getMessage());
        ErrorResponse error = ErrorResponse.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ContactInfoAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> contactInfoAlreadyExists(ContactInfoAlreadyExistsException e){
        log.warn("Попытка создания нового продавца с существующей контактной информацией. Ошибка: {}", e.getMessage());
        ErrorResponse error = ErrorResponse.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
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
    public ResponseEntity<ErrorResponse> constraintViolation(ConstraintViolationException e){
        String errorMessage = e.getConstraintViolations().iterator().next().getMessage();
        ErrorResponse error = ErrorResponse.builder().message(errorMessage).build();
        log.warn("Параметр передаваемый клиентом в API не прошел валидацию. Ошибка {}", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
