package io.github.imecuadorian.vitalmed.service;

import io.github.imecuadorian.vitalmed.model.*;

import java.util.*;

public interface SpecialtyService {
    Specialty createSpecialty(Specialty specialty);
    Optional<Specialty> getSpecialtyById(Integer id);
    Optional<Specialty> getSpecialtyByName(String name);
    List<Specialty> getAllSpecialties();
    Specialty updateSpecialty(Integer id, Specialty specialty);
    void deleteSpecialty(Integer id);
}
