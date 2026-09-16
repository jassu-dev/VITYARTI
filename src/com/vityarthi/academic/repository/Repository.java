package com.vityarthi.academic.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic Repository Interface demonstrating Generics, Abstraction, and DAO Pattern.
 *
 * @param <T>  The Domain Model Entity
 * @param <ID> The primary key / unique identifier type
 */
public interface Repository<T, ID> {

    T save(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    boolean deleteById(ID id);

    boolean existsById(ID id);

    long count();

    void flush();
}
