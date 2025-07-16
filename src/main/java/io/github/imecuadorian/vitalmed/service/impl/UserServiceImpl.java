package io.github.imecuadorian.vitalmed.service.impl;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import io.github.imecuadorian.vitalmed.service.*;
import io.github.imecuadorian.vitalmed.thread.*;
import io.github.imecuadorian.vitalmed.util.*;
import org.jetbrains.annotations.*;
import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;

public record UserServiceImpl(UserRepository userRepository) implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Contract("_ -> new")
    public @NotNull CompletableFuture<User> createUser(@NotNull User u) {
        CompletableFuture<Optional<User>> emailFut = findByEmail(u.email());
        CompletableFuture<Optional<User>> cedulaFut = findByCedula(u.cedula());
        CompletableFuture<Optional<User>> cellFut = findByCellphone(u.cell());

        return CompletableFuture
                .allOf(emailFut, cedulaFut, cellFut)
                .thenCompose(_ -> {
                    if (emailFut.join().isPresent()) {
                        LOGGER.error("Email already registered: {}", u.email());
                        return CompletableFuture.failedFuture(
                                new IllegalStateException("Email already registered"));
                    }
                    if (cedulaFut.join().isPresent()) {
                        LOGGER.error("Cedula already registered: {}", u.cedula());
                        return CompletableFuture.failedFuture(
                                new IllegalStateException("Cedula already registered"));
                    }
                    if (cellFut.join().isPresent()) {
                        LOGGER.error("Cellphone already registered: {}", u.cell());
                        return CompletableFuture.failedFuture(
                                new IllegalStateException("Cellphone already registered"));
                    }

                    return CompletableFuture.supplyAsync(() -> {
                        String hashed = PasswordUtil.hash(u.passwordHash());
                        User userWithHash = u.withPasswordHash(hashed);
                        userRepository.save(userWithHash);
                        return userWithHash;
                    }, AppExecutors.db());
                });
    }


    @Contract("_ -> new")
    @Override
    public @NotNull CompletableFuture<Optional<User>> findByCedula(String cedula) {
        return CompletableFuture.supplyAsync(() -> userRepository.findByCedula(cedula), AppExecutors.db());
    }

    @Contract("_ -> new")
    @Override
    public @NotNull CompletableFuture<Optional<User>> findByEmail(String email) {
        return CompletableFuture.supplyAsync(() -> userRepository.findByEmail(email), AppExecutors.db());
    }

    @Contract("_ -> new")
    @Override
    public @NotNull CompletableFuture<Optional<User>> findByCellphone(String cellphone) {
        return CompletableFuture.supplyAsync(() -> userRepository.findByCellphone(cellphone), AppExecutors.db());
    }

    @Override
    public @NotNull CompletableFuture<User> updateUser(String userId, User u) {
        return CompletableFuture.runAsync(() -> userRepository.update(userId, u), AppExecutors.db())
                .thenApply(_ -> u);
    }

    @Contract("_ -> new")
    @Override
    public @NotNull CompletableFuture<Void> deleteUser(String userId) {
        return CompletableFuture.runAsync(() -> userRepository.delete(userId), AppExecutors.db());
    }

    @Contract("_, _ -> new")
    @Override
    public @NotNull CompletableFuture<Optional<User>> authenticate(
            String email,
            String plainPassword
    ) {
        return findByEmail(email)
                .thenApply(optUser ->
                        optUser.filter(user ->
                                PasswordUtil.verify(plainPassword, user.passwordHash())
                        )
                );
    }


    @Contract("_, _ -> new")
    @Override
    public @NotNull CompletableFuture<Void> resetPassword(String userId, String newPlainPassword) {
        return CompletableFuture.runAsync(() -> {
            var hash = PasswordUtil.hash(newPlainPassword);
            userRepository.updatePassword(userId, hash);
        }, AppExecutors.db());
    }
}
