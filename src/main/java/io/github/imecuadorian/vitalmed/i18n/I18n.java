package io.github.imecuadorian.vitalmed.i18n;

import java.util.*;

public class I18n {
    private static Locale currentLocale = Locale.of("es", "EC");
    private static ResourceBundle bundle = loadBundle(currentLocale);

    private static ResourceBundle loadBundle(Locale locale) {
        return ResourceBundle.getBundle("io.github.imecuadorian.vitalmed.i18n.messages", locale);
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = loadBundle(locale);
    }

    public static Locale getLocale() {
        return currentLocale;
    }

    public static String t(String key) {
        return bundle.getString(key);
    }
}
