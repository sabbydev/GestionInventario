/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo5.gestioninventario.repositorio;

import java.util.List;
import java.util.Optional;

public interface IRepository<T, ID> {
    T save(T entity);
    T update(T entity);
    void deleteById(ID id);
    Optional<T> findById(ID id);
    List<T> findAll();
    boolean existsById(ID id);
}


