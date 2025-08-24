package ru.Tim.ShiftCRM.annotation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.Tim.ShiftCRM.enums.PaymentType;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PaymentTypeValidator implements ConstraintValidator<IsPaymentType, String> {

    private Set<String> allowedValues;

    @Override
    public void initialize(IsPaymentType constraintAnnotation) {
        allowedValues = Arrays.stream(PaymentType.values())
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
