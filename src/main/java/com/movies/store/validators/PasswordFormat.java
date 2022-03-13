package com.movies.store.validators;

import com.movies.store.validators.handlers.PasswordFormatValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordFormatValidator.class)
public @interface PasswordFormat {

    String message() default "Password must contain at least five letters, three digits and two special characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
