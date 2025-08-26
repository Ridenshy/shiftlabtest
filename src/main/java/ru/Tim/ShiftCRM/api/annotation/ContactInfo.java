package ru.Tim.ShiftCRM.api.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.Tim.ShiftCRM.api.annotation.validator.ContactInfoValidator;

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
