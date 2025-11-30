package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioProducto;
import com.grupo5.gestioninventario.repositorio.IRepositorioProducto;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/inventario")
public class InventarioServlet extends HttpServlet {

    private EntityManagerFactory emf;
    private IRepositorioProducto repoProducto;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        repoProducto = new JPARepositorioProducto(emf);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=sesion");
            return;
        }

        String q = request.getParameter("q");
        int page = 1;
        int size = 10;
        try { page = Integer.parseInt(request.getParameter("page")); } catch (Exception ignored) {}
        try { size = Integer.parseInt(request.getParameter("size")); } catch (Exception ignored) {}
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;
        long total = repoProducto.countSearch(q);
        int pages = (int) Math.max(1, Math.ceil(total / (double) size));

        request.setAttribute("vistaDinamica", "inventario");
        request.setAttribute("productos", repoProducto.search(q, offset, size));
        request.setAttribute("q", q);
        request.setAttribute("page", page);
        request.setAttribute("size", size);
        request.setAttribute("total", total);
        request.setAttribute("pages", pages);

        request.getRequestDispatcher("/WEB-INF/vista/layout.jsp").forward(request, response);
    }
}
