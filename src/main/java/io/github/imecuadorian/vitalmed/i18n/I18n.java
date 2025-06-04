package io.github.imecuadorian.vitalmed.i18n;

import org.jetbrains.annotations.*;

import java.util.*;

public class I18n {
    private static Locale currentLocale = Locale.of("en", "US");
    private static ResourceBundle bundle = loadBundle(currentLocale);
    private static final List<LanguageChangeListener> LISTENERS = new ArrayList<>();

    private I18n() {
    }

    private static ResourceBundle loadBundle(Locale locale) {
        return ResourceBundle.getBundle("io.github.imecuadorian.vitalmed.i18n.messages", locale);
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = loadBundle(locale);
        notifyListeners();
    }

    public static Locale getLocale() {
        return currentLocale;
    }

    public static @NotNull String t(String key) {
        return bundle.getString(key);
    }

    public static void addListener(LanguageChangeListener listener) {
        LISTENERS.add(listener);
        listener.onLanguageChanged(bundle);
    }

    private static void notifyListeners() {
        for (LanguageChangeListener listener : LISTENERS) {
            listener.onLanguageChanged(bundle);
        }
    }
}

