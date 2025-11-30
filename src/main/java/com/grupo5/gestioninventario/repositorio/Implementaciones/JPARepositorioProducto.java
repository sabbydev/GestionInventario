package com.grupo5.gestioninventario.repositorio.Implementaciones;

import com.grupo5.gestioninventario.modelo.Producto;
import com.grupo5.gestioninventario.repositorio.IRepositorioProducto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.*;

public class JPARepositorioProducto implements IRepositorioProducto {

    private final EntityManagerFactory emf;

    public JPARepositorioProducto(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Producto save(Producto entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        em.close();
        return entity;
    }

    @Override
    public Producto update(Producto entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Producto merged = em.merge(entity);
        em.getTransaction().commit();
        em.close();
        return merged;
    }

    @Override
    public void deleteById(Integer id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Producto p = em.find(Producto.class, id);
        if (p != null) em.remove(p);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Optional<Producto> findById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try { return Optional.ofNullable(em.find(Producto.class, id)); }
        finally { em.close(); }
    }

    @Override
    public List<Producto> findAll() {
        EntityManager em = emf.createEntityManager();
        try { return em.createQuery("select p from Producto p", Producto.class).getResultList(); }
        finally { em.close(); }
    }

    @Override
    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "select p from Producto p where lower(p.nombre) like :q",
                Producto.class
            ).setParameter("q", "%" + nombre.toLowerCase() + "%").getResultList();
        } finally { em.close(); }
    }

    @Override
    public List<Producto> search(String q, int offset, int limit) {
        EntityManager em = emf.createEntityManager();
        try {
            String s = q == null ? "" : q.toLowerCase();
            return em.createQuery(
                "select p from Producto p left join fetch p.categoria left join fetch p.proveedor where lower(p.nombre) like :q",
                Producto.class
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
                "select count(p) from Producto p where lower(p.nombre) like :q",
                Long.class
            ).setParameter("q", "%" + s + "%")
             .getSingleResult();
        } finally { em.close(); }
    }
}
