package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.util.*;
import java.util.concurrent.*;

public interface UserService {
    CompletableFuture<User> createUser(User user);

    CompletableFuture<Optional<User>> getUserById(String userId);

    CompletableFuture<Optional<User>> findByCedula(String cedula);

    CompletableFuture<Optional<User>> findByEmail(String email);

    CompletableFuture<Optional<User>> findByCellphone(String cellphone);

    CompletableFuture<List<User>> getUsersByRole(Role role);

    CompletableFuture<List<User>> getAllUsers();

    CompletableFuture<User> updateUser(String userId, User user);

    CompletableFuture<Void> deleteUser(String userId);

    CompletableFuture<Optional<User>> authenticate(String email, String plainPassword);

    CompletableFuture<Void> resetPassword(String userId, String newPlainPassword);
}
