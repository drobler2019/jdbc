package org.example.repository;

import java.util.Collection;
import java.util.Optional;

public interface Repository<T> {

    Collection<T> findAll();
    Optional<T> findById(Long id);
    void save(T t);

}
