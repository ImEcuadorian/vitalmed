package io.github.imecuadorian.vitalmed.repository.interfaces;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.*;

import java.util.*;

public interface UserRepository
        extends CRUDRepository<String, User> {
    Optional<User> findByCedula(String cedula);
    Optional<User> findByEmail(String email);
    Optional<User> findByCellphone(String cellphone);
    void updatePassword(String userId, String hash);
}
