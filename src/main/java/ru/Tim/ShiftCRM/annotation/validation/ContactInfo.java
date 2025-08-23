package ru.Tim.ShiftCRM.annotation.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ContactInfoValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContactInfo {

    String message() default "Контактная информация должна быть валидным email или номером телефона";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
