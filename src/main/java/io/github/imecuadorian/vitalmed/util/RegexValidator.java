package io.github.imecuadorian.vitalmed.util;

public class RegexValidator {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    private static final String CEDULA_REGEX = "^(?:[0-1]\\d|2[0-4]|30)[0-5]\\d{6}-?\\d$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[@\\-/])[A-Za-z\\d@\\-/]{8,}$";
    private static final String NAMES_SURNAMES_REGEX = "^([A-ZÁÉÍÓÚÑ][a-záéíóúñ]+)( [A-ZÁÉÍÓÚÑ][a-záéíóúñ]+){0,3}$";

    private RegexValidator(){
        throw new UnsupportedOperationException("RegexValidator is a utility class and cannot be instantiated.");
    }
    public static boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
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
