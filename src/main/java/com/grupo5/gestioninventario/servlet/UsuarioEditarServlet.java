package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.modelo.Rol;
import com.grupo5.gestioninventario.modelo.Usuario;
import com.grupo5.gestioninventario.repositorio.IRepositorioRol;
import com.grupo5.gestioninventario.repositorio.IRepositorioUsuario;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioRol;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioUsuario;
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

@WebServlet("/usuarios/editar")
public class UsuarioEditarServlet extends HttpServlet {

    private EntityManagerFactory emf;
    private IRepositorioRol repoRol;
    private IRepositorioUsuario repoUsuario;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        repoRol = new JPARepositorioRol(emf);
        repoUsuario = new JPARepositorioUsuario(emf);
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
        Optional<Usuario> ou = repoUsuario.findById(Integer.valueOf(idStr));
        if (ou.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/usuarios");
            return;
        }
        request.setAttribute("vistaDinamica", "usuario-form");
        request.setAttribute("usuario", ou.get());
        request.setAttribute("roles", repoRol.findAll());
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
        String idStr = request.getParameter("id");
        Optional<Usuario> ou = repoUsuario.findById(Integer.valueOf(idStr));
        if (ou.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/usuarios");
            return;
        }
        Usuario u = ou.get();

        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String contraseña = request.getParameter("password");
        String rolIdStr = request.getParameter("id_rol");
        String estadoStr = request.getParameter("estado");

        String error = null;
        if (nombre == null || nombre.isBlank()) error = "Nombre requerido";
        if (error == null && (correo == null || correo.isBlank())) error = "Correo requerido";
        if (error == null && (rolIdStr == null || rolIdStr.isBlank())) error = "Rol requerido";

        if (error != null) {
            request.setAttribute("flashError", error);
            request.setAttribute("usuario", u);
            request.setAttribute("roles", repoRol.findAll());
            request.setAttribute("vistaDinamica", "usuario-form");
            request.getRequestDispatcher("/WEB-INF/vista/layout.jsp").forward(request, response);
            return;
        }

        u.setNombre(nombre);
        u.setCorreo(correo);
        if (contraseña != null && !contraseña.isBlank()) {
            u.setContraseña(contraseña);
        }
        u.setEstado("activo".equalsIgnoreCase(estadoStr));
        Optional<Rol> or = repoRol.findById(Integer.valueOf(rolIdStr));
        u.setRol(or.orElse(null));

        repoUsuario.update(u);
        session.setAttribute("flashSuccess", "Usuario actualizado");
        response.sendRedirect(request.getContextPath() + "/usuarios");
    }
}

