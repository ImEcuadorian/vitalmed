package io.github.imecuadorian.vitalmed.i18n;

import java.util.*;

public class I18n {
    private static Locale currentLocale = Locale.of("es", "EC");
    private static ResourceBundle bundle = loadBundle(currentLocale);
    private static final List<LanguageChangeListener> listeners = new ArrayList<>();

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

    public static String t(String key) {
        return bundle.getString(key);
    }

    public static void addListener(LanguageChangeListener listener) {
        listeners.add(listener);
        listener.onLanguageChanged(bundle); // inicializa textos
    }

    public static void removeListener(LanguageChangeListener listener) {
        listeners.remove(listener);
    }

    private static void notifyListeners() {
        for (LanguageChangeListener listener : listeners) {
            listener.onLanguageChanged(bundle);
        }
    }
}

