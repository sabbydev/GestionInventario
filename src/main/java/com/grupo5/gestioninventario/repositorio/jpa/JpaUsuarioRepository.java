/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.grupo5.gestioninventario.repositorio.jpa;

import com.grupo5.gestioninventario.modelo.Usuario;
import com.grupo5.gestioninventario.repositorio.UsuarioRepository;
import com.grupo5.gestioninventario.repositorio.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

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
        Usuario u = em.find(Usuario.class, id);
        em.close();
        return Optional.ofNullable(u);
    }

    @Override
    public List<Usuario> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Usuario> list = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        em.close();
        return list;
    }

    @Override
    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    @Override
    public Usuario findByUsernameAndPassword(String username, String password) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.username = :user AND u.password = :pass", Usuario.class)
                     .setParameter("user", username)
                     .setParameter("pass", password)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}

