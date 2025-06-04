package io.github.imecuadorian.vitalmed.config;

import org.slf4j.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = AppConfig.class.getResourceAsStream("/config.properties")) {
            if (input == null) {
                LOGGER.error("No se encontró el archivo de configuración: config.properties");
            }
            PROPERTIES.load(input);
        } catch (IOException | RuntimeException e) {
            LOGGER.error("Error al cargar el archivo de configuración", e);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
