package io.github.imecuadorian.vitalmed.util;

import static io.github.imecuadorian.vitalmed.util.Constants.*;

public class RegexValidator {

    private RegexValidator(){
        throw new UnsupportedOperationException("RegexValidator is a utility class and cannot be instantiated.");
    }
    public static boolean isValidEmail(String email) {
        return !email.matches(EMAIL_REGEX);
    }

    public static boolean isValidCedula(String cedula) {
        return cedula.matches(CEDULA_REGEX);
    }

    public static boolean isValidPassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }

    public static boolean isValidNamesOrSurnames(String namesOrSurnames) {
        return namesOrSurnames.matches(NAMES_SURNAMES_REGEX);
    }
}
