package io.github.imecuadorian.vitalmed.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.*;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnectionPool {
    private static final HikariDataSource DATA_SOURCE;

    static {
        Dotenv dotenv = Dotenv.configure().load();
        HikariConfig config = new HikariConfig();

        String url = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String pass = dotenv.get("DB_PASSWORD");
        String poolSize = dotenv.get("DB_POOL_SIZE");

        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(pass);
        config.setMaximumPoolSize(poolSize != null ? Integer.parseInt(poolSize) : 10);

        config.setConnectionTimeout(3000);
        config.setIdleTimeout(10000);
        config.setMaxLifetime(300000);

        DATA_SOURCE = new HikariDataSource(config);
    }

    private MySQLConnectionPool() {}

    public static HikariDataSource getDataSource() {
        return DATA_SOURCE;
    }
}
