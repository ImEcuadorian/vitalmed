package io.github.imecuadorian.vitalmed.util;

public class RegexValidator {

    private static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.\\w+$";
    private static final String CEDULA_REGEX = "^\\d{10}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[@\\-/])[A-Za-z\\d@\\-/]{8,}$";

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
}
