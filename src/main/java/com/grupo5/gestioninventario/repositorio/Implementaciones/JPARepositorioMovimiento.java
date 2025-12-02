package com.grupo5.gestioninventario.repositorio.Implementaciones;

import com.grupo5.gestioninventario.modelo.Movimiento;
import com.grupo5.gestioninventario.modelo.TipoMovimiento;
import com.grupo5.gestioninventario.repositorio.IRepositorioMovimiento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.*;

public class JPARepositorioMovimiento implements IRepositorioMovimiento {

    private final EntityManagerFactory emf;

    public JPARepositorioMovimiento(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Movimiento save(Movimiento entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        em.close();
        return entity;
    }

    @Override
    public Movimiento update(Movimiento entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Movimiento merged = em.merge(entity);
        em.getTransaction().commit();
        em.close();
        return merged;
    }

    @Override
    public void deleteById(Integer id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Movimiento m = em.find(Movimiento.class, id);
        if (m != null) em.remove(m);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Optional<Movimiento> findById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try { return Optional.ofNullable(em.find(Movimiento.class, id)); }
        finally { em.close(); }
    }

    @Override
    public List<Movimiento> findAll() {
        EntityManager em = emf.createEntityManager();
        try { return em.createQuery("select m from Movimiento m order by m.fecha desc", Movimiento.class).getResultList(); }
        finally { em.close(); }
    }

    @Override
    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    @Override
    public long contarEntradas() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("select count(m) from Movimiento m where m.tipo = :t", Long.class)
                .setParameter("t", TipoMovimiento.entrada)
                .getSingleResult();
        } finally { em.close(); }
    }

    @Override
    public long contarSalidas() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("select count(m) from Movimiento m where m.tipo = :t", Long.class)
                .setParameter("t", TipoMovimiento.salida)
                .getSingleResult();
        } finally { em.close(); }
    }

    @Override
    public long contarTotal() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("select count(m) from Movimiento m", Long.class)
                .getSingleResult();
        } finally { em.close(); }
    }

    @Override
    public List<Movimiento> ultimos(int max) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("select m from Movimiento m order by m.fecha desc", Movimiento.class)
                .setMaxResults(max)
                .getResultList();
        } finally { em.close(); }
    }

    @Override
    public Map<Integer, Long> contarPorMes(TipoMovimiento tipo, int year) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Object[]> rows = em.createQuery(
                "select FUNCTION('MONTH', m.fecha), count(m) from Movimiento m where FUNCTION('YEAR', m.fecha) = :y and m.tipo = :t group by FUNCTION('MONTH', m.fecha) order by FUNCTION('MONTH', m.fecha)",
                Object[].class
            ).setParameter("y", year).setParameter("t", tipo).getResultList();
            Map<Integer, Long> res = new HashMap<>();
            for (int i = 1; i <= 12; i++) res.put(i, 0L);
            for (Object[] r : rows) {
                Integer mes = ((Number) r[0]).intValue();
                Long cnt = ((Number) r[1]).longValue();
                res.put(mes, cnt);
            }
            return res;
        } finally { em.close(); }
    }

    public Map<String, Long> sumarCantidadPorCategoria(TipoMovimiento tipo, java.sql.Timestamp desde, java.sql.Timestamp hasta) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "select coalesce(c.nombre, 'Sin categoría'), sum(m.cantidad) " +
                    "from Movimiento m left join m.producto p left join p.categoria c " +
                    "where m.tipo = :t " +
                    (desde != null ? "and m.fecha >= :desde " : "") +
                    (hasta != null ? "and m.fecha <= :hasta " : "") +
                    "group by c.nombre";
            var q = em.createQuery(jpql, Object[].class).setParameter("t", tipo);
            if (desde != null) q.setParameter("desde", desde);
            if (hasta != null) q.setParameter("hasta", hasta);
            List<Object[]> rows = q.getResultList();
            Map<String, Long> res = new LinkedHashMap<>();
            for (Object[] r : rows) {
                String categoria = (String) r[0];
                Long total = ((Number) r[1]).longValue();
                res.put(categoria, total);
            }
            return res;
        } finally { em.close(); }
    }

    public List<Movimiento> buscar(java.sql.Timestamp desde, java.sql.Timestamp hasta, TipoMovimiento tipo) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "select m from Movimiento m where 1=1 " +
                    (desde != null ? "and m.fecha >= :desde " : "") +
                    (hasta != null ? "and m.fecha <= :hasta " : "") +
                    (tipo != null ? "and m.tipo = :tipo " : "") +
                    "order by m.fecha desc";
            var q = em.createQuery(jpql, Movimiento.class);
            if (desde != null) q.setParameter("desde", desde);
            if (hasta != null) q.setParameter("hasta", hasta);
            if (tipo != null) q.setParameter("tipo", tipo);
            return q.getResultList();
        } finally { em.close(); }
    }

    public long contar(TipoMovimiento tipo, java.sql.Timestamp desde, java.sql.Timestamp hasta) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "select count(m) from Movimiento m where 1=1 " +
                    (tipo != null ? "and m.tipo = :t " : "") +
                    (desde != null ? "and m.fecha >= :desde " : "") +
                    (hasta != null ? "and m.fecha <= :hasta " : "");
            var q = em.createQuery(jpql, Long.class);
            if (tipo != null) q.setParameter("t", tipo);
            if (desde != null) q.setParameter("desde", desde);
            if (hasta != null) q.setParameter("hasta", hasta);
            return q.getSingleResult();
        } finally { em.close(); }
    }

    public Map<Integer, Long> contarPorMesRango(TipoMovimiento tipo, java.sql.Timestamp desde, java.sql.Timestamp hasta) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "select FUNCTION('MONTH', m.fecha), count(m) from Movimiento m where 1=1 " +
                    (tipo != null ? "and m.tipo = :t " : "") +
                    (desde != null ? "and m.fecha >= :desde " : "") +
                    (hasta != null ? "and m.fecha <= :hasta " : "") +
                    "group by FUNCTION('MONTH', m.fecha) order by FUNCTION('MONTH', m.fecha)";
            var q = em.createQuery(jpql, Object[].class);
            if (tipo != null) q.setParameter("t", tipo);
            if (desde != null) q.setParameter("desde", desde);
            if (hasta != null) q.setParameter("hasta", hasta);
            List<Object[]> rows = q.getResultList();
            Map<Integer, Long> res = new LinkedHashMap<>();
            for (Object[] r : rows) {
                Integer mes = ((Number) r[0]).intValue();
                Long cnt = ((Number) r[1]).longValue();
                res.put(mes, cnt);
            }
            return res;
        } finally { em.close(); }
    }
}
