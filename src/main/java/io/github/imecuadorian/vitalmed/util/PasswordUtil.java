package io.github.imecuadorian.vitalmed.util;

import at.favre.lib.crypto.bcrypt.*;
import org.jetbrains.annotations.*;

public final class PasswordUtil {
    private static final int COST = 12;

    private PasswordUtil() {
    }
    @Contract("_ -> new")
    public static @NotNull String hash(@NotNull String plainPassword) {
        return BCrypt.withDefaults()
                .hashToString(COST, plainPassword.toCharArray());
    }
    public static boolean verify(@NotNull String plainPassword, String bcryptHash) {
        var result = BCrypt.verifyer()
                .verify(plainPassword.toCharArray(), bcryptHash);
        return result.verified;
    }
}
