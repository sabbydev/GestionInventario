package com.grupo5.gestioninventario.repositorio.Implementaciones;

import com.grupo5.gestioninventario.modelo.Categoria;
import com.grupo5.gestioninventario.repositorio.IRepositorioCategoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.*;

public class JPARepositorioCategoria implements IRepositorioCategoria {

    private final EntityManagerFactory emf;

    public JPARepositorioCategoria(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Categoria save(Categoria entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        em.close();
        return entity;
    }

    @Override
    public Categoria update(Categoria entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Categoria merged = em.merge(entity);
        em.getTransaction().commit();
        em.close();
        return merged;
    }

    @Override
    public void deleteById(Integer id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Categoria c = em.find(Categoria.class, id);
        if (c != null) em.remove(c);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Optional<Categoria> findById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try { return Optional.ofNullable(em.find(Categoria.class, id)); }
        finally { em.close(); }
    }

    @Override
    public List<Categoria> findAll() {
        EntityManager em = emf.createEntityManager();
        try { return em.createQuery("select c from Categoria c", Categoria.class).getResultList(); }
        finally { em.close(); }
    }

    @Override
    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    @Override
    public Map<Integer, Long> obtenerConteoProductosPorCategoria() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Object[]> rows = em.createQuery(
                "select c.id, count(p) from Categoria c left join c.productos p group by c.id",
                Object[].class
            ).getResultList();
            Map<Integer, Long> r = new HashMap<>();
            for (Object[] row : rows) {
                r.put((Integer) row[0], (Long) row[1]);
            }
            return r;
        } finally { em.close(); }
    }

    @Override
    public List<Categoria> buscarPorNombre(String nombre) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "select c from Categoria c where lower(c.nombre) like :q",
                Categoria.class
            ).setParameter("q", "%" + nombre.toLowerCase() + "%").getResultList();
        } finally { em.close(); }
    }
}

