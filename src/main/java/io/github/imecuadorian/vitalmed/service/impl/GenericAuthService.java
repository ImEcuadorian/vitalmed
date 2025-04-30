package io.github.imecuadorian.vitalmed.service.impl;

import io.github.imecuadorian.vitalmed.model.User;
import io.github.imecuadorian.vitalmed.repository.Repository;
import io.github.imecuadorian.vitalmed.service.AuthService;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenericAuthService<T extends User> implements AuthService<T> {

    private final Repository<String, T> repository;
    private final Logger logger;

    public GenericAuthService(Repository<String, T> repository, Logger logger) {
        this.repository = repository;
        this.logger = logger;
    }

    @Override
    public Optional<T> login(String email, String password) {
        Optional<T> result = repository.findAll().stream()
                .filter(user -> user.validateLogin(email, password))
                .findFirst();

        if (logger.isLoggable(Level.INFO)) {
            logger.info(result.isPresent()
                    ? String.format("Login successful for %s", email)
                    : String.format("Login failed for %s", email));
        }

        return result;
    }
}
