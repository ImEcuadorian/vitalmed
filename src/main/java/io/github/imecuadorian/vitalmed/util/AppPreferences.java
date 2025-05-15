package io.github.imecuadorian.vitalmed.util;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.util.*;
import lombok.*;

import javax.swing.*;
import java.util.*;
import java.util.prefs.*;

public class AppPreferences {

    public static final String PREFERENCES_ROOT_PATH = "/vitalmed";
    public static final String KEY_LAF = "laf";
    public static final String KEY_LAF_THEME = "lafTheme";
    public static final String KEY_RECENT_SEARCH = "recentSearch";
    public static final String KEY_RECENT_SEARCH_FAVORITE = "recentSearchFavorite";
    public static final String THEME_UI_KEY = "__Vitalmed.flatlaf.theme";
    public static final String VERSION = "1.0.0";

    @Getter
    private static Preferences state;


    public static void init() {
        state = Preferences.userRoot().node(PREFERENCES_ROOT_PATH);
    }

    public static void setupLaf() {
        try {
            String lafClassName = state.get(KEY_LAF, FlatLightLaf.class.getName());
            if (IntelliJTheme.ThemeLaf.class.getName().equals(lafClassName)) {
                String theme = state.get(KEY_LAF_THEME, "");
                FlatDarkLaf.setup();
                if (!theme.isEmpty()) {
                    UIManager.getLookAndFeelDefaults().put(THEME_UI_KEY, theme);
                }
            } else {
                UIManager.setLookAndFeel(lafClassName);
            }
        } catch (Exception e) {
            LoggingFacade.INSTANCE.logSevere(null, e);
            FlatLightLaf.setup();
        }
        UIManager.addPropertyChangeListener(e -> {
            if (e.getPropertyName().equals("lookAndFeel")) {
                state.put(KEY_LAF, UIManager.getLookAndFeel().getClass().getName());
            }
        });
    }

    public static String[] getRecentSearch(boolean favorite) {
        String stringArr = state.get(favorite ? KEY_RECENT_SEARCH_FAVORITE : KEY_RECENT_SEARCH, null);
        if (stringArr == null || stringArr.trim().isEmpty()) return null;
        return stringArr.trim().split(",");
    }

    public static void addRecentSearch(String value, boolean favorite) {
        String[] oldRecent = getRecentSearch(false);
        String[] oldFavorite = getRecentSearch(true);
        if (favorite) {
            if (oldRecent != null) {
                List<String> list = new ArrayList<>(Arrays.asList(oldRecent));
                list.remove(value);
                state.put(KEY_RECENT_SEARCH, String.join(",", list));
            }
            if (oldFavorite != null) {
                List<String> list = new ArrayList<>(Arrays.asList(oldFavorite));
                list.remove(value);
                list.add(0, value);
                state.put(KEY_RECENT_SEARCH_FAVORITE, String.join(",", list));
            } else {
                state.put(KEY_RECENT_SEARCH_FAVORITE, value);
            }
        } else {
            if (oldFavorite != null) {
                List<String> list = new ArrayList<>(Arrays.asList(oldFavorite));
                if (list.contains(value)) {
                    return;
                }
            }
            if (oldRecent == null) {
                state.put(KEY_RECENT_SEARCH, value);
            } else {
                List<String> list = new ArrayList<>(Arrays.asList(oldRecent));
                list.remove(value);
                list.add(0, value);
                state.put(KEY_RECENT_SEARCH, String.join(",", list));
            }
        }
    }

    public static void removeRecentSearch(String value, boolean favorite) {
        String[] oldRecent = getRecentSearch(favorite);
        if (oldRecent != null) {
            List<String> list = new ArrayList<>(Arrays.asList(oldRecent));
            list.remove(value);
            state.put(favorite ? KEY_RECENT_SEARCH_FAVORITE : KEY_RECENT_SEARCH, String.join(",", list));
        }
    }
}
