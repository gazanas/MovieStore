package com.movies.store.validators.handlers;

import com.movies.store.validators.PasswordFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordFormatValidator implements ConstraintValidator<PasswordFormat, String> {

    /**
     * A valid password should contain at least five letters, three digits and two special characters.
     * Defines regular expressions to check all three conditions mentioned above.
     *
     * @param password The passed password
     * @param constraintValidatorContext
     * @return The decision if the password is valid
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        String specialCharacters = Pattern.quote("!@#$%^&*(){}|[]/?><.,");

        boolean isValidNumeric = Pattern.compile(".*[0-9]{3,}.*").matcher(password).matches();
        // Checks if the password contains at least three digits
        if ( !isValidNumeric ) {
            return false;
        }

        boolean isValidLetters = Pattern.compile(".*[a-zA-Z]{5,}.*").matcher(password).matches();

        // Checks if the password contains at least five letters
        if ( !isValidLetters ) {
            return false;
        }

        boolean isValidSpecial = Pattern.compile(".*["+specialCharacters+"]{2,}.*").matcher(password).matches();

        // Checks if the password contains at least two special characters
        if ( !isValidSpecial ) {
            return false;
        }

        return true;
    }
}
