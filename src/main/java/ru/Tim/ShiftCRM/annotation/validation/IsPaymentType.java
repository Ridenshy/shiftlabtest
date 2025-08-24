package ru.Tim.ShiftCRM.annotation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = PaymentTypeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsPaymentType {

    String message() default "Поле должно быть: CASH | CARD | TRANSFER";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
