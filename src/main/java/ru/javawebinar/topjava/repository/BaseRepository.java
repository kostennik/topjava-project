package ru.javawebinar.topjava.repository;

import java.util.List;

public interface BaseRepository<T> {
    /*@return null if not found, when updated */
    T save(T user);

    /*@return false if not found */
    boolean delete(int id);

    /*@return null if not found */
    T get(int id);

    /*@return list of values */
    List<T> getAll();

    /*@return null if not found */
    T getByEmail(String email);
}