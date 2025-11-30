package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.repositorio.IRepositorioMovimiento;
import com.grupo5.gestioninventario.repositorio.IRepositorioProducto;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioMovimiento;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioProducto;
import com.grupo5.gestioninventario.modelo.Movimiento;
import com.grupo5.gestioninventario.modelo.TipoMovimiento;
import com.grupo5.gestioninventario.modelo.Producto;
import com.grupo5.gestioninventario.modelo.Usuario;
import java.sql.Timestamp;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/movimientos")
public class MovimientosServlet extends HttpServlet {

    private EntityManagerFactory emf;
    private IRepositorioMovimiento repoMovimiento;
    private IRepositorioProducto repoProducto;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        repoMovimiento = new JPARepositorioMovimiento(emf);
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

        request.setAttribute("vistaDinamica", "movimientos");
        request.setAttribute("resumenEntradas", repoMovimiento.contarEntradas());
        request.setAttribute("resumenSalidas", repoMovimiento.contarSalidas());
        request.setAttribute("resumenTotal", repoMovimiento.contarTotal());
        request.setAttribute("movimientos", repoMovimiento.ultimos(50));
        request.setAttribute("productos", repoProducto.findAll());

        java.time.LocalDate now = java.time.LocalDate.now();
        int year = now.getYear();
        java.util.Map<Integer, Long> entradasMes = repoMovimiento.contarPorMes(TipoMovimiento.entrada, year);
        java.util.Map<Integer, Long> salidasMes = repoMovimiento.contarPorMes(TipoMovimiento.salida, year);
        String[] etiquetas = {"'Ene'","'Feb'","'Mar'","'Abr'","'May'","'Jun'","'Jul'","'Ago'","'Sep'","'Oct'","'Nov'","'Dic'"};
        StringBuilder ent = new StringBuilder();
        StringBuilder sal = new StringBuilder();
        for (int i = 1; i <= 12; i++) {
            ent.append(entradasMes.getOrDefault(i, 0L));
            sal.append(salidasMes.getOrDefault(i, 0L));
            if (i < 12) { ent.append(","); sal.append(","); }
        }
        StringBuilder labs = new StringBuilder();
        for (int i = 0; i < etiquetas.length; i++) {
            labs.append(etiquetas[i]);
            if (i < etiquetas.length - 1) labs.append(",");
        }
        request.setAttribute("labelsMeses", labs.toString());
        request.setAttribute("dataEntradasMes", ent.toString());
        request.setAttribute("dataSalidasMes", sal.toString());

        request.getRequestDispatcher("/WEB-INF/vista/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=sesion");
            return;
        }
        String idProductoStr = request.getParameter("id_producto");
        String tipoStr = request.getParameter("tipo");
        String cantidadStr = request.getParameter("cantidad");
        int cantidad;
        try { cantidad = Integer.parseInt(cantidadStr); } catch (Exception e) { cantidad = -1; }
        if (cantidad <= 0) {
            session.setAttribute("flashError", "Cantidad inválida");
            response.sendRedirect(request.getContextPath() + "/movimientos");
            return;
        }
        Producto producto = repoProducto.findById(Integer.valueOf(idProductoStr)).orElse(null);
        if (producto == null) {
            session.setAttribute("flashError", "Producto no encontrado");
            response.sendRedirect(request.getContextPath() + "/movimientos");
            return;
        }
        if ("salida".equals(tipoStr) && producto.getStock() < cantidad) {
            session.setAttribute("flashError", "Stock insuficiente");
            response.sendRedirect(request.getContextPath() + "/movimientos");
            return;
        }
        if ("entrada".equals(tipoStr)) {
            producto.setStock(producto.getStock() + cantidad);
        } else {
            producto.setStock(producto.getStock() - cantidad);
        }
        repoProducto.update(producto);

        Movimiento m = new Movimiento();
        m.setProducto(producto);
        m.setTipo("entrada".equals(tipoStr) ? TipoMovimiento.entrada : TipoMovimiento.salida);
        m.setCantidad(cantidad);
        m.setFecha(new Timestamp(System.currentTimeMillis()));
        Usuario u = (Usuario) session.getAttribute("usuario");
        m.setUsuario(u);
        repoMovimiento.save(m);

        session.setAttribute("flashSuccess", "Movimiento registrado");
        response.sendRedirect(request.getContextPath() + "/movimientos");
    }
}
