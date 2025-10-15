package com.grupo5.gestioninventario.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/proveedores")
public class ProveedoresServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=sesion");
            return;
        }

        request.setAttribute("vistaDinamica", "proveedores");

        request.getRequestDispatcher("/WEB-INF/vista/layout.jsp").forward(request, response);
    }
}
