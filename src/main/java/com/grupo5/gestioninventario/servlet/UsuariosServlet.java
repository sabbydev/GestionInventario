package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.repositorio.IRepositorioUsuario;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioUsuario;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/usuarios")
public class UsuariosServlet extends HttpServlet {

    private EntityManagerFactory emf;
    private IRepositorioUsuario repoUsuario;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        repoUsuario = new JPARepositorioUsuario(emf);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=sesion");
            return;
        }
        
        request.setAttribute("vistaDinamica", "usuarios");
        request.setAttribute("usuarios", repoUsuario.findAll());
        
        request.getRequestDispatcher("/WEB-INF/vista/layout.jsp").forward(request, response);
    }
}








