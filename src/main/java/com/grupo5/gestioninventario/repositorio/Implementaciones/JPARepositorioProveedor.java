package com.grupo5.gestioninventario.repositorio.Implementaciones;

import com.grupo5.gestioninventario.modelo.Proveedor;
import com.grupo5.gestioninventario.repositorio.IRepositorioProveedor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.*;

public class JPARepositorioProveedor implements IRepositorioProveedor {

    private final EntityManagerFactory emf;

    public JPARepositorioProveedor(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Proveedor save(Proveedor entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        em.close();
        return entity;
    }

    @Override
    public Proveedor update(Proveedor entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Proveedor merged = em.merge(entity);
        em.getTransaction().commit();
        em.close();
        return merged;
    }

    @Override
    public void deleteById(Integer id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Proveedor p = em.find(Proveedor.class, id);
        if (p != null) em.remove(p);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Optional<Proveedor> findById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try { return Optional.ofNullable(em.find(Proveedor.class, id)); }
        finally { em.close(); }
    }

    @Override
    public List<Proveedor> findAll() {
        EntityManager em = emf.createEntityManager();
        try { return em.createQuery("select p from Proveedor p", Proveedor.class).getResultList(); }
        finally { em.close(); }
    }

    @Override
    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    @Override
    public List<Proveedor> buscarPorNombre(String nombre) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "select p from Proveedor p where lower(p.nombre) like :q",
                Proveedor.class
            ).setParameter("q", "%" + nombre.toLowerCase() + "%").getResultList();
        } finally { em.close(); }
    }

    @Override
    public List<Proveedor> search(String q, int offset, int limit) {
        EntityManager em = emf.createEntityManager();
        try {
            String s = q == null ? "" : q.toLowerCase();
            return em.createQuery(
                "select p from Proveedor p where lower(p.nombre) like :q",
                Proveedor.class
            ).setParameter("q", "%" + s + "%")
             .setFirstResult(Math.max(0, offset))
             .setMaxResults(Math.max(1, limit))
             .getResultList();
        } finally { em.close(); }
    }

    @Override
    public long countSearch(String q) {
        EntityManager em = emf.createEntityManager();
        try {
            String s = q == null ? "" : q.toLowerCase();
            return em.createQuery(
                "select count(p) from Proveedor p where lower(p.nombre) like :q",
                Long.class
            ).setParameter("q", "%" + s + "%")
             .getSingleResult();
        } finally { em.close(); }
    }
}
