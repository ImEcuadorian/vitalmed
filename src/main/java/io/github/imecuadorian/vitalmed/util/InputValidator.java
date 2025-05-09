// Clase utilitaria para validaciones
package io.github.imecuadorian.vitalmed.util;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

import raven.modal.*;
import raven.modal.toast.option.ToastLocation;

import static io.github.imecuadorian.vitalmed.util.Constants.*;

public class InputValidator {


    public static void markAsError(JComponent component) {
        component.putClientProperty("JComponent.outline", "error");
    }

    public static void clearError(JComponent component) {
        component.putClientProperty("JComponent.outline", null);
    }

    public static void applyNumericOnly(JTextField field, int maxLength) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                if (!Character.isDigit(ch) || field.getText().length() >= maxLength) {
                    e.consume();
                    markAsError(field);
                    Toast.show(field, Toast.Type.ERROR, "Solo se permiten números (máx. " + maxLength + ")", ToastLocation.TOP_TRAILING, Constants.getOption());
                } else {
                    clearError(field);
                }
            }
        });
    }

    public static void applyLettersOnly(JTextField field, int maxLength) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                if ((!Character.isLetter(ch) && ch != ' ') || field.getText().length() >= maxLength) {
                    e.consume();
                    markAsError(field);
                } else {
                    clearError(field);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                validateNameField(field);
            }
        });
    }

    private static void validateNameField(JTextField field) {
        Pattern pattern = Pattern.compile(NAMES_SURNAMES_REGEX);
        String text = field.getText().trim();
        if (pattern.matcher(text).matches()) {
            clearError(field);
        } else {
            markAsError(field);
        }
    }

    public static void applyRegexLimiter(JTextField field, String regex, int maxLength) {
        Pattern pattern = Pattern.compile(regex);

        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = field.getText();
                if (text.length() <= maxLength && pattern.matcher(text).matches()) {
                    clearError(field);
                } else {
                    markAsError(field);
                }
            }
        });
    }

    public static void applyCedulaValidation(JTextField field) {
        applyNumericOnly(field, 10);
        applyRegexLimiter(field, CEDULA_REGEX, 10);
    }

    public static boolean isValidCedula(JTextField field) {
        String id = field.getText();

        if (id.length() != 10 && !id.startsWith("30")) {
            markAsError(field);
            Toast.show(field, Toast.Type.ERROR, "La cédula debe tener 10 dígitos", ToastLocation.TOP_TRAILING, Constants.getOption());
            return false;
        }

        try {
            int provinceCode = Integer.parseInt(id.substring(0, 2));
            int thirdDigit = Integer.parseInt(id.substring(2, 3));
            int[] coefficients = {2, 1, 2, 1, 2, 1, 2, 1, 2};
            int sum = 0;

            if ((provinceCode < 0 || provinceCode > 24) && !id.startsWith("30")) {
                markAsError(field);
                Toast.show(field, Toast.Type.ERROR, "Código de provincia inválido", ToastLocation.TOP_TRAILING, Constants.getOption());
                return false;
            }

            if (thirdDigit >= 6 && !id.startsWith("30")) {
                markAsError(field);
                Toast.show(field, Toast.Type.ERROR, "El tercer dígito debe ser menor a 6", ToastLocation.TOP_TRAILING, Constants.getOption());
                return false;
            }

            for (int i = 0; i < 9; i++) {
                int digit = Character.getNumericValue(id.charAt(i));
                int product = digit * coefficients[i];
                if (product > 9) product -= 9;
                sum += product;
            }

            int expectedVerifier = (10 - (sum % 10)) % 10;
            int actualVerifier = Character.getNumericValue(id.charAt(9));

            if (expectedVerifier != actualVerifier) {
                markAsError(field);
                Toast.show(field, Toast.Type.ERROR, "El dígito verificador no es válido", ToastLocation.TOP_TRAILING, Constants.getOption());
                return false;
            }
        } catch (Exception e) {
            markAsError(field);
            Toast.show(field, Toast.Type.ERROR, "Error al validar la cédula", ToastLocation.TOP_TRAILING, Constants.getOption());
            return false;
        }

        clearError(field);
        return true;
    }



    public static void applyEmailValidation(JTextField field) {
        applyRegexLimiter(field, EMAIL_REGEX, 50);
    }

    public static void applyPasswordValidation(JTextField field) {
        applyRegexLimiter(field, PASSWORD_REGEX, 10);
    }

    public static void applyNameValidation(JTextField field) {
        applyLettersOnly(field, 30);
        applyRegexLimiter(field, NAMES_SURNAMES_REGEX, 30);
    }

    public static void applyPhoneValidation(JTextField field) {
        applyNumericOnly(field, 10);
        applyRegexLimiter(field, PHONE_REGEX, 10);
    }

    public static boolean isNotEmpty(JTextField field, String errorMessage) {
        if (field.getText().trim().isEmpty()) {
            markAsError(field);
            Toast.show(field, Toast.Type.ERROR, errorMessage, ToastLocation.TOP_TRAILING, Constants.getOption());
            return false;
        }
        clearError(field);
        return true;
    }

    public static boolean isValidPassword(JPasswordField field, String errorMessage) {
        String text = new String(field.getPassword());
        if (!text.matches(PASSWORD_REGEX)) {
            markAsError(field);
            Toast.show(field, Toast.Type.ERROR, errorMessage, ToastLocation.TOP_TRAILING, Constants.getOption());
            return false;
        }
        clearError(field);
        return true;
    }

    public static boolean isValidName(JTextField field, String errorMessage) {
        String text = field.getText().trim();
        if (!text.matches(NAMES_SURNAMES_REGEX)) {
            markAsError(field);
            Toast.show(field, Toast.Type.ERROR, errorMessage, ToastLocation.TOP_TRAILING, Constants.getOption());
            return false;
        }
        clearError(field);
        return true;
    }

    public static boolean isValidEmail(JTextField field, String errorMessage) {
        String text = field.getText().trim();
        if (!text.matches(EMAIL_REGEX)) {
            markAsError(field);
            Toast.show(field, Toast.Type.ERROR, errorMessage, ToastLocation.TOP_TRAILING, Constants.getOption());
            return false;
        }
        clearError(field);
        return true;
    }

    public static boolean isValidPhone(JTextField field, String errorMessage) {
        String text = field.getText().trim();
        if (!text.matches(PHONE_REGEX)) {
            markAsError(field);
            Toast.show(field, Toast.Type.ERROR, errorMessage, ToastLocation.TOP_TRAILING, Constants.getOption());
            return false;
        }
        clearError(field);
        return true;
    }


}
