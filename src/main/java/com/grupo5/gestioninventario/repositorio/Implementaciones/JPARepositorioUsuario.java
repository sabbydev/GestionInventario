package com.grupo5.gestioninventario.repositorio.Implementaciones;

import jakarta.persistence.*;
import com.grupo5.gestioninventario.modelo.Usuario;
import java.util.List;
import java.util.Optional;
import com.grupo5.gestioninventario.repositorio.IRepositorioUsuario;

public class JPARepositorioUsuario implements IRepositorioUsuario {

    private final EntityManagerFactory emf;

    public JPARepositorioUsuario(EntityManagerFactory emf) {
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
    public Usuario findByCorreoAndContraseña(String correo, String contraseña) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "select u from Usuario u where u.correo=:correo and u.contraseña=:contraseña",
                    Usuario.class)
                .setParameter("correo", correo)
                .setParameter("contraseña", contraseña)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally { em.close(); }
    }
}