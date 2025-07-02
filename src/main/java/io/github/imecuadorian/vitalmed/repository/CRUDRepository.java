package io.github.imecuadorian.vitalmed.repository;

import java.util.*;

public interface CRUDRepository<K, V> {

    void save(V value);
    Optional<V> findById(K id);
    List<V> findAll();
    void update(K id, V value);
    void delete(K id);
}
