package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.repositorio.IRepositorioCategoria;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioCategoria;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/categorias/eliminar")
public class CategoriaEliminarServlet extends HttpServlet {

    private EntityManagerFactory emf;
    private IRepositorioCategoria repoCategoria;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        repoCategoria = new JPARepositorioCategoria(emf);
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
                repoCategoria.deleteById(Integer.valueOf(idStr));
                session.setAttribute("flashSuccess", "Categoría eliminada");
            } catch (Exception e) {
                session.setAttribute("flashError", "No se puede eliminar la categoría");
            }
        }
        response.sendRedirect(request.getContextPath() + "/categorias");
    }
}
