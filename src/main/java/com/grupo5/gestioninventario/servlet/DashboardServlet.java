package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=sesion");
            return;
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        request.setAttribute("usuario", usuario);
        request.setAttribute("vistaDinamica", "dashboard");
        
        request.getRequestDispatcher("/WEB-INF/vista/layout.jsp").forward(request, response);
    }
}