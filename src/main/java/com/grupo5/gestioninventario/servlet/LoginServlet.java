package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.modelo.Usuario;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioUsuario;
import com.grupo5.gestioninventario.servicio.ServicioAutenticacion;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private ServicioAutenticacion servicioAutenticacion;

    @Override
    public void init() throws ServletException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        this.servicioAutenticacion = new ServicioAutenticacion(new JPARepositorioUsuario(emf));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String password = request.getParameter("password");

        Usuario usuario = servicioAutenticacion.autenticar(correo, password);

        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);
            response.sendRedirect(request.getContextPath() + "/inicio");
        } else {
            // Redirigir al servlet que sirve la vista de login, con parámetro de error
            response.sendRedirect(request.getContextPath() + "/login?error=true");
        }
    }
}