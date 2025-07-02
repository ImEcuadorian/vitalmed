package io.github.imecuadorian.vitalmed.repository.interfaces;

import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.repository.*;

import java.util.*;

public interface RoomRepository extends CRUDRepository<String, Room> {

    List<Room> findBySpeciality(String speciality);
    List<Room> findByNumber(int number);
}
