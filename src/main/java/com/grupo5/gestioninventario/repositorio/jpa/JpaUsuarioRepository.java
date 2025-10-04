/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.grupo5.gestioninventario.repositorio.jpa;

import com.grupo5.gestioninventario.modelo.Usuario;
import com.grupo5.gestioninventario.repositorio.UsuarioRepository;
import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;

public class JpaUsuarioRepository implements UsuarioRepository {

    private final EntityManagerFactory emf;

    public JpaUsuarioRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Usuario save(Usuario entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        em.close();
        return entity;
    }

    @Override
    public Usuario update(Usuario entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Usuario merged = em.merge(entity);
        em.getTransaction().commit();
        em.close();
        return merged;
    }

    @Override
    public void deleteById(Integer id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Usuario u = em.find(Usuario.class, id);
        if (u != null) em.remove(u);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try { return Optional.ofNullable(em.find(Usuario.class, id)); }
        finally { em.close(); }
    }

    @Override
    public List<Usuario> findAll() {
        EntityManager em = emf.createEntityManager();
        try { return em.createQuery("select u from Usuario u", Usuario.class).getResultList(); }
        finally { em.close(); }
    }

    @Override
    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    @Override
    public Usuario findByCorreoAndPassword(String correo, String password) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "select u from Usuario u where u.correo=:c and u.password=:p",
                    Usuario.class)
                .setParameter("c", correo)
                .setParameter("p", password)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally { em.close(); }
    }
}

