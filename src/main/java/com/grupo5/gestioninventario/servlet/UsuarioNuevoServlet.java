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
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/usuarios/nuevo")
public class UsuarioNuevoServlet extends HttpServlet {

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
        request.setAttribute("vistaDinamica", "usuario-form");
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
        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String contraseña = request.getParameter("contraseña");
        String rolIdStr = request.getParameter("id_rol");
        String estadoStr = request.getParameter("estado");

        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setCorreo(correo);
        u.setContraseña(contraseña);
        u.setEstado("activo".equalsIgnoreCase(estadoStr));
        if (rolIdStr != null && !rolIdStr.isBlank()) {
            Optional<Rol> or = repoRol.findById(Integer.valueOf(rolIdStr));
            u.setRol(or.orElse(null));
        }
        repoUsuario.save(u);
        session.setAttribute("flashSuccess", "Usuario creado");
        response.sendRedirect(request.getContextPath() + "/usuarios");
    }
}
