package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.modelo.Categoria;
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
import java.util.Optional;

@WebServlet("/categorias/editar")
public class CategoriaEditarServlet extends HttpServlet {

    private EntityManagerFactory emf;
    private IRepositorioCategoria repoCategoria;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        repoCategoria = new JPARepositorioCategoria(emf);
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
        Optional<Categoria> oc = repoCategoria.findById(Integer.valueOf(idStr));
        if (oc.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/categorias");
            return;
        }
        request.setAttribute("vistaDinamica", "categoria-form");
        request.setAttribute("categoria", oc.get());
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
        Optional<Categoria> oc = repoCategoria.findById(id);
        if (oc.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/categorias");
            return;
        }
        Categoria c = oc.get();
        c.setNombre(request.getParameter("nombre"));
        c.setDescripcion(request.getParameter("descripcion"));
        repoCategoria.update(c);
        session.setAttribute("flashSuccess", "Categoría actualizada");
        response.sendRedirect(request.getContextPath() + "/categorias");
    }
}
