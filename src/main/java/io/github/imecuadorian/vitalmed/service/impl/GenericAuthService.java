package io.github.imecuadorian.vitalmed.service.impl;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.*;
import io.github.imecuadorian.vitalmed.service.*;
import org.slf4j.*;

import java.util.*;

public class GenericAuthService<T extends User> implements AuthService<T> {

    private final Repository<String, T> repository;
    private static final Logger logger = LoggerFactory.getLogger(GenericAuthService.class);

    public GenericAuthService(Repository<String, T> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<T> login(String email, String password) {
        Optional<T> result = repository.findAll().stream()
                .filter(user -> user.validateLogin(email, password))
                .findFirst();

        if (result.isPresent()) {
            logger.info("Login is successful for user {}", email);
        } else {
            logger.warn("Login failed for user {}", email);
        }

        return result;
    }
}
