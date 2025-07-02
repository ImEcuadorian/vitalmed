package io.github.imecuadorian.vitalmed.repository.interfaces;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.*;

import java.util.*;

public interface SpecialtyRepository extends CRUDRepository<Integer, Specialty> {
    Optional<Specialty> findByName(String name);
}
