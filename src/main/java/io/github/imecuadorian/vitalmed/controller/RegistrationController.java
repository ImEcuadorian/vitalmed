package io.github.imecuadorian.vitalmed.controller;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.service.*;

import java.util.concurrent.*;

public record RegistrationController(UserService userService) {
    public CompletableFuture<User> registerUser(User user) {
        return userService.createUser(user);
    }
}
