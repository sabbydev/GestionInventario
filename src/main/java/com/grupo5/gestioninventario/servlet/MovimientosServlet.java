package com.grupo5.gestioninventario.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/movimientos")
public class MovimientosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("movimientos.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Procesar datos de entrada o salida de inventario
        String producto = request.getParameter("producto");
        String tipo = request.getParameter("tipo");
        String cantidad = request.getParameter("cantidad");

        System.out.println("Movimiento: " + tipo + " - Producto: " + producto + " - Cantidad: " + cantidad);

        response.sendRedirect("movimientos.jsp?success=true");
    }
}
