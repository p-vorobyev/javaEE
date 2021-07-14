package ru.voroby.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserAgeValidator implements ConstraintValidator<UserAgeValidation, Integer> {
    @Override
    public void initialize(UserAgeValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value > 5;
    }
}
