package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.modelo.Proveedor;
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

@WebServlet("/proveedores/nuevo")
public class ProveedorNuevoServlet extends HttpServlet {

    private EntityManagerFactory emf;
    private IRepositorioProveedor repoProveedor;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        repoProveedor = new JPARepositorioProveedor(emf);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=sesion");
            return;
        }
        request.setAttribute("vistaDinamica", "proveedor-form");
        request.getRequestDispatcher("/WEB-INF/vista/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=sesion");
            return;
        }

        Proveedor p = new Proveedor();
        p.setNombre(request.getParameter("nombre"));
        p.setContacto(request.getParameter("contacto"));
        p.setTelefono(request.getParameter("telefono"));
        p.setCorreo(request.getParameter("correo"));
        p.setDireccion(request.getParameter("direccion"));
        repoProveedor.save(p);
        session.setAttribute("flashSuccess", "Proveedor creado");
        response.sendRedirect(request.getContextPath() + "/proveedores");
    }
}
