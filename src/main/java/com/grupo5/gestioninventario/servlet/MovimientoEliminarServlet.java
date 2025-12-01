package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.modelo.Movimiento;
import com.grupo5.gestioninventario.modelo.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/movimientos/eliminar")
public class MovimientoEliminarServlet extends HttpServlet {

    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=sesion");
            return;
        }
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/movimientos");
            return;
        }
        Integer id = Integer.valueOf(idStr);

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Movimiento m = em.find(Movimiento.class, id);
            if (m == null) {
                em.getTransaction().rollback();
                session.setAttribute("flashError", "Movimiento no encontrado");
                response.sendRedirect(request.getContextPath() + "/movimientos");
                return;
            }
            Producto p = em.find(Producto.class, m.getProducto().getId());
            if (p == null) {
                em.getTransaction().rollback();
                session.setAttribute("flashError", "Producto no encontrado para el movimiento");
                response.sendRedirect(request.getContextPath() + "/movimientos");
                return;
            }

            if (null == m.getTipo()) {
                em.getTransaction().rollback();
                session.setAttribute("flashError", "Eliminación no disponible para transferencias");
                response.sendRedirect(request.getContextPath() + "/movimientos");
                return;
            } else switch (m.getTipo()) {
                case entrada:
                    if (p.getStock() < m.getCantidad()) {
                        em.getTransaction().rollback();
                        session.setAttribute("flashError", "No se puede revertir entrada: stock actual insuficiente");
                        response.sendRedirect(request.getContextPath() + "/movimientos");
                        return;
                    }   p.setStock(p.getStock() - m.getCantidad());
                    em.merge(p);
                    break;
                case salida:
                    p.setStock(p.getStock() + m.getCantidad());
                    em.merge(p);
                    break;
                default:
                    em.getTransaction().rollback();
                    session.setAttribute("flashError", "Eliminación no disponible para transferencias");
                    response.sendRedirect(request.getContextPath() + "/movimientos");
                    return;
            }

            Movimiento ref = em.find(Movimiento.class, id);
            if (ref != null) em.remove(ref);
            em.getTransaction().commit();
            session.setAttribute("flashSuccess", "Movimiento eliminado");
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            session.setAttribute("flashError", "Error eliminando movimiento");
        } finally {
            em.close();
        }
        response.sendRedirect(request.getContextPath() + "/movimientos");
    }
}
