package io.github.imecuadorian.vitalmed.controller;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.service.*;

import java.util.*;
import java.util.concurrent.*;


public record LoginController(UserService userService) {

    public CompletableFuture<Optional<User>> login(String email, String password) {
        return userService.authenticate(email, password);
    }

}

