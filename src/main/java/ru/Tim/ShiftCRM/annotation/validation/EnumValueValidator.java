package ru.Tim.ShiftCRM.annotation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValueValidator implements ConstraintValidator<IsEnum, String> {

    private Class<? extends Enum<?>> enumClass;
    private boolean ignoreCase;
    private Set<String> allowedValues;

    @Override
    public void initialize(IsEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
        this.ignoreCase = constraintAnnotation.ignoreCase();
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
        if(ignoreCase){
            return allowedValues.stream()
                    .anyMatch(allowedValue -> allowedValue.equalsIgnoreCase(s));
        }
        return allowedValues.contains(s);
    }
}
