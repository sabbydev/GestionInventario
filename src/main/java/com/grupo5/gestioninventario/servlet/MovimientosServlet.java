package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.repositorio.IRepositorioMovimiento;
import com.grupo5.gestioninventario.repositorio.IRepositorioProducto;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioMovimiento;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioProducto;
import com.grupo5.gestioninventario.repositorio.IRepositorioUsuario;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioUsuario;
import com.grupo5.gestioninventario.modelo.TipoMovimiento;
import com.grupo5.gestioninventario.modelo.Usuario;
import com.grupo5.gestioninventario.servicio.InventarioFacade;
import com.grupo5.gestioninventario.servicio.LoggerSistema;
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
    private InventarioFacade inventarioFacade;
    private IRepositorioUsuario repoUsuario;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        repoMovimiento = new JPARepositorioMovimiento(emf);
        repoProducto = new JPARepositorioProducto(emf);
        inventarioFacade = new InventarioFacade(emf);
        repoUsuario = new JPARepositorioUsuario(emf);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=sesion");
            return;
        }

        String fi = request.getParameter("fechaInicio");
        String ff = request.getParameter("fechaFin");
        String tipoParam = request.getParameter("tipo");
        java.sql.Timestamp desde = null;
        java.sql.Timestamp hasta = null;
        try { if (fi != null && !fi.isBlank()) desde = java.sql.Timestamp.valueOf(fi + " 00:00:00"); } catch (Exception ignored) {}
        try { if (ff != null && !ff.isBlank()) hasta = java.sql.Timestamp.valueOf(ff + " 23:59:59"); } catch (Exception ignored) {}
        TipoMovimiento tipoFiltro = null;
        if (tipoParam != null) {
            if ("entrada".equalsIgnoreCase(tipoParam)) tipoFiltro = TipoMovimiento.entrada;
            else if ("salida".equalsIgnoreCase(tipoParam)) tipoFiltro = TipoMovimiento.salida;
            else if ("transferencia".equalsIgnoreCase(tipoParam)) tipoFiltro = TipoMovimiento.transferencia;
        }

        request.setAttribute("vistaDinamica", "movimientos");
        request.setAttribute("productos", repoProducto.findAll());

        long entradasCnt = ((JPARepositorioMovimiento) repoMovimiento).contar(TipoMovimiento.entrada, desde, hasta);
        long salidasCnt = ((JPARepositorioMovimiento) repoMovimiento).contar(TipoMovimiento.salida, desde, hasta);
        long totalCnt = ((JPARepositorioMovimiento) repoMovimiento).contar(null, desde, hasta);
        request.setAttribute("resumenEntradas", entradasCnt);
        request.setAttribute("resumenSalidas", salidasCnt);
        request.setAttribute("resumenTotal", totalCnt);

        java.util.List<com.grupo5.gestioninventario.modelo.Movimiento> lista = ((JPARepositorioMovimiento) repoMovimiento).buscar(desde, hasta, tipoFiltro);
        if (lista.size() > 50) lista = lista.subList(0, 50);
        request.setAttribute("movimientos", lista);

        java.util.Map<Integer, Long> entradasMes = ((JPARepositorioMovimiento) repoMovimiento).contarPorMesRango(TipoMovimiento.entrada, desde, hasta);
        java.util.Map<Integer, Long> salidasMes = ((JPARepositorioMovimiento) repoMovimiento).contarPorMesRango(TipoMovimiento.salida, desde, hasta);
        String[] etiquetas = {"'Ene'","'Feb'","'Mar'","'Abr'","'May'","'Jun'","'Jul'","'Ago'","'Sep'","'Oct'","'Nov'","'Dic'"};
        StringBuilder ent = new StringBuilder();
        StringBuilder sal = new StringBuilder();
        for (int i = 1; i <= 12; i++) {
            ent.append(entradasMes.getOrDefault(i, 0L));
            if (i < 12) ent.append(",");
            sal.append(salidasMes.getOrDefault(i, 0L));
            if (i < 12) sal.append(",");
        }
        StringBuilder lbl = new StringBuilder();
        for (int i = 0; i < etiquetas.length; i++) {
            lbl.append(etiquetas[i]);
            if (i < etiquetas.length - 1) lbl.append(",");
        }
        request.setAttribute("labelsMeses", lbl.toString());
        request.setAttribute("dataEntradasMes", ent.toString());
        request.setAttribute("dataSalidasMes", sal.toString());

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
        String idProductoStr = request.getParameter("id_producto");
        String idProductoDestinoStr = request.getParameter("id_producto_destino");
        String tipoStr = request.getParameter("tipo");
        String cantidadStr = request.getParameter("cantidad");
        int cantidad;
        try { cantidad = Integer.parseInt(cantidadStr); } catch (Exception e) { cantidad = -1; }
        if (cantidad <= 0) {
            session.setAttribute("flashError", "Cantidad inválida");
            response.sendRedirect(request.getContextPath() + "/movimientos");
            return;
        }
        try {
            Integer usuarioId = (Integer) session.getAttribute("usuarioId");
            Usuario u = repoUsuario.findById(usuarioId).orElse(null);
            if ("transferencia".equals(tipoStr)) {
                Integer idOrigen = Integer.valueOf(idProductoStr);
                Integer idDestino = Integer.valueOf(idProductoDestinoStr);
                inventarioFacade.registrarTransferencia(idOrigen, idDestino, cantidad, u);
                LoggerSistema.getInstance().info("Transferencia realizada por usuarioId=" + usuarioId + " origen=" + idOrigen + " destino=" + idDestino + " cantidad=" + cantidad);
            } else if ("entrada".equals(tipoStr)) {
                inventarioFacade.registrarEntrada(Integer.valueOf(idProductoStr), cantidad, u);
                LoggerSistema.getInstance().info("Entrada registrada por usuarioId=" + usuarioId + " producto=" + idProductoStr + " cantidad=" + cantidad);
            } else {
                inventarioFacade.registrarSalida(Integer.valueOf(idProductoStr), cantidad, u);
                LoggerSistema.getInstance().info("Salida registrada por usuarioId=" + usuarioId + " producto=" + idProductoStr + " cantidad=" + cantidad);
            }
            session.setAttribute("flashSuccess", "Movimiento registrado");
        } catch (RuntimeException ex) {
            LoggerSistema.getInstance().error("Error registrando movimiento: " + ex.getMessage(), ex);
            session.setAttribute("flashError", ex.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/movimientos");
    }
}
