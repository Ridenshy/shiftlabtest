package ru.Tim.ShiftCRM.annotation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ContactInfoValidator implements ConstraintValidator<ContactInfo, String> {
    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private static final String PHONE_PATTERN =
            "^\\+?[1-9]\\d{1,14}$"; // E.164 format

    private Pattern emailPattern;
    private Pattern phonePattern;

    @Override
    public void initialize(ContactInfo constraintAnnotation) {
        emailPattern = Pattern.compile(EMAIL_PATTERN);
        phonePattern = Pattern.compile(PHONE_PATTERN);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null || value.trim().isEmpty()) {
            String trimmedValue = value.trim();

            if (emailPattern.matcher(trimmedValue).matches()) {
                return true;
            }

            String digitsOnly = trimmedValue.replaceAll("[^+0-9]", "");
            return phonePattern.matcher(digitsOnly).matches();
        }
        return true;
    }
}
