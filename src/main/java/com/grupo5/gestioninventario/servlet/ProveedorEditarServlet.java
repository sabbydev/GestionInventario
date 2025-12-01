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
import java.util.Optional;

@WebServlet("/proveedores/editar")
public class ProveedorEditarServlet extends HttpServlet {

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
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=sesion");
            return;
        }
        String idStr = request.getParameter("id");
        Optional<Proveedor> op = repoProveedor.findById(Integer.valueOf(idStr));
        if (op.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/proveedores");
            return;
        }
        request.setAttribute("vistaDinamica", "proveedor-form");
        request.setAttribute("proveedor", op.get());
        request.getRequestDispatcher("/WEB-INF/vista/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=sesion");
            return;
        }
        Integer id = Integer.valueOf(request.getParameter("id"));
        Optional<Proveedor> op = repoProveedor.findById(id);
        if (op.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/proveedores");
            return;
        }
        Proveedor p = op.get();
        p.setNombre(request.getParameter("nombre"));
        p.setContacto(request.getParameter("contacto"));
        p.setTelefono(request.getParameter("telefono"));
        p.setCorreo(request.getParameter("correo"));
        p.setDireccion(request.getParameter("direccion"));
        repoProveedor.update(p);
        session.setAttribute("flashSuccess", "Proveedor actualizado");
        response.sendRedirect(request.getContextPath() + "/proveedores");
    }
}
