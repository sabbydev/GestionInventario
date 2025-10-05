package com.grupo5.gestioninventario.repositorio;

import java.util.List;
import java.util.Optional;

// Interfaz genérica para cualquier implementación de conexión con base de datos (JPA, MongoDB, JDBC, etc.)
public interface IRepositorio<T, ID> {
    T save(T entity);
    T update(T entity);
    void deleteById(ID id);
    Optional<T> findById(ID id);
    List<T> findAll();
    boolean existsById(ID id);
}


