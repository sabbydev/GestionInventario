package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.modelo.TipoMovimiento;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioProducto;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioMovimiento;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/reportes")
public class ReportesServlet extends HttpServlet {

    private EntityManagerFactory emf;
    private JPARepositorioProducto repoProducto;
    private JPARepositorioMovimiento repoMovimiento;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        repoProducto = new JPARepositorioProducto(emf);
        repoMovimiento = new JPARepositorioMovimiento(emf);
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
        java.sql.Timestamp desde = null;
        java.sql.Timestamp hasta = null;
        try { if (fi != null && !fi.isBlank()) desde = java.sql.Timestamp.valueOf(fi + " 00:00:00"); } catch (Exception ignored) {}
        try { if (ff != null && !ff.isBlank()) hasta = java.sql.Timestamp.valueOf(ff + " 23:59:59"); } catch (Exception ignored) {}

        java.util.Map<String, Long> entradas = repoMovimiento.sumarCantidadPorCategoria(TipoMovimiento.entrada, desde, hasta);
        java.util.Map<String, Long> salidas = repoMovimiento.sumarCantidadPorCategoria(TipoMovimiento.salida, desde, hasta);
        java.util.Map<String, Long> transferencias = repoMovimiento.sumarCantidadPorCategoria(TipoMovimiento.transferencia, desde, hasta);
        java.util.Map<String, Long> stockPorCat = ((JPARepositorioProducto) repoProducto).sumarStockPorCategoria();
        java.util.List<Object[]> bajoStock = ((JPARepositorioProducto) repoProducto).topBajoStock(10);

        java.util.List<String> labels = new java.util.ArrayList<>(new java.util.LinkedHashSet<>());
        labels.addAll(entradas.keySet());
        for (String k : salidas.keySet()) if (!labels.contains(k)) labels.add(k);
        for (String k : transferencias.keySet()) if (!labels.contains(k)) labels.add(k);
        for (String k : stockPorCat.keySet()) if (!labels.contains(k)) labels.add(k);

        String labelsJson = toJsonArray(labels);
        String entradasJson = toJsonArrayLong(mapToList(labels, entradas));
        String salidasJson = toJsonArrayLong(mapToList(labels, salidas));
        String transferJson = toJsonArrayLong(mapToList(labels, transferencias));
        String stockJson = toJsonArrayLong(mapToList(labels, stockPorCat));

        java.util.List<String> lowLabels = new java.util.ArrayList<>();
        java.util.List<Long> lowValues = new java.util.ArrayList<>();
        for (Object[] r : bajoStock) { lowLabels.add((String) r[0]); lowValues.add(((Number) r[1]).longValue()); }

        request.setAttribute("vistaDinamica", "reportes");
        request.setAttribute("productos", repoProducto.findAll());
        request.setAttribute("labelsCategoriasJson", labelsJson);
        request.setAttribute("entradasJson", entradasJson);
        request.setAttribute("salidasJson", salidasJson);
        request.setAttribute("transferenciasJson", transferJson);
        request.setAttribute("stockCategoriasJson", stockJson);
        request.setAttribute("bajoStockLabelsJson", toJsonArray(lowLabels));
        request.setAttribute("bajoStockValoresJson", toJsonArrayLong(lowValues));

        request.getRequestDispatcher("/WEB-INF/vista/layout.jsp").forward(request, response);
    }

    private String toJsonArray(java.util.List<String> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("\"").append(items.get(i).replace("\"", "\\\"")).append("\"");
        }
        sb.append("]");
        return sb.toString();
    }

    private String toJsonArrayLong(java.util.List<Long> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(items.get(i));
        }
        sb.append("]");
        return sb.toString();
    }

    private java.util.List<Long> mapToList(java.util.List<String> labels, java.util.Map<String, Long> map) {
        java.util.List<Long> list = new java.util.ArrayList<>();
        for (String l : labels) list.add(map.getOrDefault(l, 0L));
        return list;
    }
}
