package io.github.imecuadorian.vitalmed.service.impl;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import io.github.imecuadorian.vitalmed.service.*;
import io.github.imecuadorian.vitalmed.thread.*;
import io.github.imecuadorian.vitalmed.util.*;
import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;

public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CompletableFuture<User> createUser(User u) {
        return CompletableFuture.supplyAsync(() -> {
            if (userRepository.findByEmail(u.email()).isPresent()) {
                LOGGER.error("Email already registered: {}", u.email());
                return null;
            }
            if (userRepository.findByCedula(u.cedula()).isPresent()) {
                LOGGER.error("Cedula already registered: {}", u.cedula());
                return null;
            }
            String hashed = PasswordUtil.hash(u.passwordHash());
            User userWithHash = u.withPasswordHash(hashed);
            userRepository.save(userWithHash);
            return userWithHash;
        }, AppExecutors.db());
    }


    @Override
    public CompletableFuture<Optional<User>> getUserById(String userId) {
        return CompletableFuture.supplyAsync(() -> userRepository.findById(userId), AppExecutors.db());
    }

    @Override
    public CompletableFuture<Optional<User>> findByCedula(String cedula) {
        return CompletableFuture.supplyAsync(() -> userRepository.findByCedula(cedula), AppExecutors.db());
    }

    @Override
    public CompletableFuture<Optional<User>> findByEmail(String email) {
        return CompletableFuture.supplyAsync(() -> userRepository.findByEmail(email), AppExecutors.db());
    }

    @Override
    public CompletableFuture<Optional<User>> findByCellphone(String cellphone) {
        return CompletableFuture.supplyAsync(() -> userRepository.findByCellphone(cellphone), AppExecutors.db());
    }

    @Override
    public CompletableFuture<List<User>> getUsersByRole(Role role) {
        return CompletableFuture.supplyAsync(() -> userRepository.findByRole(role), AppExecutors.db());
    }

    @Override
    public CompletableFuture<List<User>> getAllUsers() {
        return CompletableFuture.supplyAsync(userRepository::findAll, AppExecutors.db());
    }

    @Override
    public CompletableFuture<User> updateUser(String userId, User u) {
        return CompletableFuture.runAsync(() -> userRepository.update(userId, u), AppExecutors.db())
                .thenApply(v -> u);
    }

    @Override
    public CompletableFuture<Void> deleteUser(String userId) {
        return CompletableFuture.runAsync(() -> userRepository.delete(userId), AppExecutors.db());
    }

    @Override
    public CompletableFuture<Optional<User>> authenticate(String email, String plainPassword) {
        return CompletableFuture.supplyAsync(() -> {
            var opt = userRepository.findByEmail(email);
            if (opt.isEmpty()) return Optional.empty();
            var user = opt.get();
            return PasswordUtil.verify(plainPassword, user.passwordHash())
                    ? Optional.of(user)
                    : Optional.empty();
        }, AppExecutors.db());
    }

    @Override
    public CompletableFuture<Void> resetPassword(String userId, String newPlainPassword) {
        return CompletableFuture.runAsync(() -> {
            var hash = PasswordUtil.hash(newPlainPassword);
            userRepository.updatePassword(userId, hash);
        }, AppExecutors.db());
    }
}
