package io.github.imecuadorian.vitalmed.controller;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.service.*;
import io.github.imecuadorian.vitalmed.util.*;


public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    public User login(String email, String password) {
        return userService.authenticate(email, password)
                .thenApply(optionalUser -> optionalUser.orElseThrow(() -> new RuntimeException("Invalid credentials")))
                .join();
    }

}

