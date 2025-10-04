/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.grupo5.gestioninventario.servlets;

import com.grupo5.gestioninventario.modelo.Usuario;
import com.grupo5.gestioninventario.repositorio.jpa.JpaUsuarioRepository;
import com.grupo5.gestioninventario.servicio.AuthService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() throws ServletException {
        // Crear el EMF y pasar el repositorio al servicio (constructor requerido)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("invPU");
        this.authService = new AuthService(new JpaUsuarioRepository(emf));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");     // si tu login es por username, usa "correo" como username
        String password = request.getParameter("password");

        // Usa el método que sí existe en AuthService
        Usuario usuario = authService.login(correo, password);

        if (usuario != null) {
            request.getSession().setAttribute("usuario", usuario);
            response.sendRedirect("dashboard.html");
        } else {
            response.sendRedirect("login.html?error=true");
        }
    }
}


