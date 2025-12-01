package com.grupo5.gestioninventario.repositorio.Implementaciones;

import com.grupo5.gestioninventario.modelo.Rol;
import com.grupo5.gestioninventario.repositorio.IRepositorioRol;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

public class JPARepositorioRol implements IRepositorioRol {

    private final EntityManagerFactory emf;

    public JPARepositorioRol(EntityManagerFactory emf) { this.emf = emf; }

    @Override
    public Rol save(Rol entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        em.close();
        return entity;
    }

    @Override
    public Rol update(Rol entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Rol merged = em.merge(entity);
        em.getTransaction().commit();
        em.close();
        return merged;
    }

    @Override
    public void deleteById(Integer id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Rol r = em.find(Rol.class, id);
        if (r != null) em.remove(r);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Optional<Rol> findById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try { return Optional.ofNullable(em.find(Rol.class, id)); }
        finally { em.close(); }
    }

    @Override
    public List<Rol> findAll() {
        EntityManager em = emf.createEntityManager();
        try { return em.createQuery("select r from Rol r", Rol.class).getResultList(); }
        finally { em.close(); }
    }

    @Override
    public boolean existsById(Integer id) { return findById(id).isPresent(); }
}

