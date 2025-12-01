package com.grupo5.gestioninventario.servicio;

import com.grupo5.gestioninventario.modelo.Movimiento;
import com.grupo5.gestioninventario.modelo.Producto;
import com.grupo5.gestioninventario.modelo.TipoMovimiento;
import com.grupo5.gestioninventario.modelo.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import java.sql.Timestamp;

public class InventarioFacade {

    private final EntityManagerFactory emf;

    public InventarioFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void registrarEntrada(Integer idProducto, int cantidad, Usuario usuario) {
        ejecutarMovimientoSimple(idProducto, cantidad, usuario, TipoMovimiento.entrada);
    }

    public void registrarSalida(Integer idProducto, int cantidad, Usuario usuario) {
        ejecutarMovimientoSimple(idProducto, cantidad, usuario, TipoMovimiento.salida);
    }

    public void registrarTransferencia(Integer idProductoOrigen, Integer idProductoDestino, int cantidad, Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Producto origen = em.find(Producto.class, idProductoOrigen);
            Producto destino = em.find(Producto.class, idProductoDestino);
            if (origen == null || destino == null) {
                throw new PersistenceException("Producto origen/destino no encontrado");
            }
            if (cantidad <= 0) {
                throw new PersistenceException("Cantidad inválida");
            }
            if (origen.getStock() < cantidad) {
                throw new PersistenceException("Stock insuficiente en origen");
            }

            origen.setStock(origen.getStock() - cantidad);
            destino.setStock(destino.getStock() + cantidad);
            em.merge(origen);
            em.merge(destino);

            Movimiento movSalida = new Movimiento();
            movSalida.setProducto(origen);
            movSalida.setTipo(TipoMovimiento.transferencia);
            movSalida.setCantidad(cantidad);
            movSalida.setFecha(new Timestamp(System.currentTimeMillis()));
            movSalida.setUsuario(usuario);
            em.persist(movSalida);

            Movimiento movEntrada = new Movimiento();
            movEntrada.setProducto(destino);
            movEntrada.setTipo(TipoMovimiento.transferencia);
            movEntrada.setCantidad(cantidad);
            movEntrada.setFecha(new Timestamp(System.currentTimeMillis()));
            movEntrada.setUsuario(usuario);
            em.persist(movEntrada);

            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    private void ejecutarMovimientoSimple(Integer idProducto, int cantidad, Usuario usuario, TipoMovimiento tipo) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Producto producto = em.find(Producto.class, idProducto);
            if (producto == null) {
                throw new PersistenceException("Producto no encontrado");
            }
            if (cantidad <= 0) {
                throw new PersistenceException("Cantidad inválida");
            }
            if (tipo == TipoMovimiento.salida && producto.getStock() < cantidad) {
                throw new PersistenceException("Stock insuficiente");
            }

            int nuevoStock = tipo == TipoMovimiento.entrada
                    ? producto.getStock() + cantidad
                    : producto.getStock() - cantidad;
            producto.setStock(nuevoStock);
            em.merge(producto);

            Movimiento m = new Movimiento();
            m.setProducto(producto);
            m.setTipo(tipo);
            m.setCantidad(cantidad);
            m.setFecha(new Timestamp(System.currentTimeMillis()));
            m.setUsuario(usuario);
            em.persist(m);

            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}

