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
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private ServicioAutenticacion servicioAutenticacion;

    @Override
    public void init() throws ServletException {
        // Contruir EntityManagerFactory en base a la configuración en Persistence.xml
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        // Inicialización del servicio de autenticación
        this.servicioAutenticacion = new ServicioAutenticacion(new JPARepositorioUsuario(emf));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Almacena datos enviados por el cliente
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");

        Usuario usuario = servicioAutenticacion.autenticar(correo, password);

        if (usuario != null) {
            request.getSession().setAttribute("usuario", usuario);
            response.sendRedirect("dashboard.jsp");
        } else {
            response.sendRedirect("login.html?error=true");
        }
    }
}


