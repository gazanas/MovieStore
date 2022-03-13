package com.movies.store.validators.handlers;

import com.movies.store.validators.PasswordMatch;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {

    private String field;
    private String match;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.match = constraintAnnotation.match();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        String field = (String) PropertyAccessorFactory.forBeanPropertyAccess(o).getPropertyValue(this.field);
        String match = (String) PropertyAccessorFactory.forBeanPropertyAccess(o).getPropertyValue(this.match);

        if (field.equals(match)) {
            return true;
        }

        return false;
    }
}
