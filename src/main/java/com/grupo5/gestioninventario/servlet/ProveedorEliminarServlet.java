package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.repositorio.IRepositorioProveedor;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioProveedor;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/proveedores/eliminar")
public class ProveedorEliminarServlet extends HttpServlet {

    private EntityManagerFactory emf;
    private IRepositorioProveedor repoProveedor;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        repoProveedor = new JPARepositorioProveedor(emf);
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
        if (idStr != null && !idStr.isBlank()) {
            try {
                repoProveedor.deleteById(Integer.valueOf(idStr));
                session.setAttribute("flashSuccess", "Proveedor eliminado");
            } catch (Exception e) {
                session.setAttribute("flashError", "No se puede eliminar el proveedor");
            }
        }
        response.sendRedirect(request.getContextPath() + "/proveedores");
    }
}
