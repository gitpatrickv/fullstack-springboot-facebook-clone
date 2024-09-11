package validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import validation.PasswordLengthValid;

public class PasswordLengthValidator implements ConstraintValidator<PasswordLengthValid, String> {


    @Override
    public void initialize(PasswordLengthValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {

        if(password == null) {
            return false;
        }
        return password.length() >= 8 && password.length() <= 20;
    }
}
