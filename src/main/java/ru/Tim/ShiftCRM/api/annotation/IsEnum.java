package ru.Tim.ShiftCRM.api.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.Tim.ShiftCRM.api.annotation.validator.EnumValueValidator;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = EnumValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsEnum {

    String message() default "Недопустимое значение";

    Class<? extends Enum<?>> enumClass();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
