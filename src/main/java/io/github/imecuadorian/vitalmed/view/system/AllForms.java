package io.github.imecuadorian.vitalmed.view.system;

import org.jetbrains.annotations.*;
import org.slf4j.*;

import javax.swing.*;
import java.lang.reflect.*;
import java.util.*;

public class AllForms {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllForms.class);
    private static AllForms instance;

    private final Map<Class<? extends Form>, Form> formsMap;

    private static AllForms getInstance() {
        if (instance == null) {
            instance = new AllForms();
        }
        return instance;
    }

    private AllForms() {
        formsMap = new HashMap<>();
    }

    public static @Nullable Form getForm(Class<? extends Form> cls) {
        if (getInstance().formsMap.containsKey(cls)) {
            return getInstance().formsMap.get(cls);
        }
        try {
            Form form = cls.getDeclaredConstructor().newInstance();
            getInstance().formsMap.put(cls, form);
            formInit(form);
            return form;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            LOGGER.error( "Error initializing form: {}", cls.getName(), e);
        }
        return null;
    }

    public static void formInit(@NotNull Form form) {
        SwingUtilities.invokeLater(form::formInit);
    }
}
