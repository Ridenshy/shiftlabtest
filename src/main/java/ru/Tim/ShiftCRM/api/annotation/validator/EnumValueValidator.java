package ru.Tim.ShiftCRM.api.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.Tim.ShiftCRM.api.annotation.IsEnum;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValueValidator implements ConstraintValidator<IsEnum, String> {

    private Class<? extends Enum<?>> enumClass;
    private Set<String> allowedValues;

    @Override
    public void initialize(IsEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
        this.allowedValues = Arrays
                .stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null){
            return true;
        }


        return allowedValues.contains(s);
    }
}
