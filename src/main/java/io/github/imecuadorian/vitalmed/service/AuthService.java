package io.github.imecuadorian.vitalmed.service;

import java.util.Optional;

public interface AuthService<T> {
    Optional<T> login(String email, String password);
}
