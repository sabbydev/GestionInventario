package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.repositorio.IRepositorioUsuario;
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

@WebServlet("/usuarios/eliminar")
public class UsuarioEliminarServlet extends HttpServlet {

    private EntityManagerFactory emf;
    private IRepositorioUsuario repoUsuario;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        repoUsuario = new JPARepositorioUsuario(emf);
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
                Integer id = Integer.valueOf(idStr);
                repoUsuario.deleteById(id);
                session.setAttribute("flashSuccess", "Usuario eliminado");
            } catch (Exception e) {
                session.setAttribute("flashError", "No se puede eliminar el usuario");
            }
        }
        response.sendRedirect(request.getContextPath() + "/usuarios");
    }
}

