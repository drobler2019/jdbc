package org.example.repository;

import java.util.Collection;
import java.util.Optional;

public interface Repository<T,ID> {

    Collection<T> findAll();
    Optional<T> findById(ID id);
    void save(T t);
    void deleteById(ID id);

}
