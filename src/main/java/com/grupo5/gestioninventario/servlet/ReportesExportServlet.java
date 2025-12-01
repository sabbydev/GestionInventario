package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.modelo.Producto;
import com.grupo5.gestioninventario.modelo.TipoMovimiento;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioProducto;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioMovimiento;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet("/reportes/exportar")
public class ReportesExportServlet extends HttpServlet {

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

        Map<String, Long> entradas = repoMovimiento.sumarCantidadPorCategoria(TipoMovimiento.entrada, desde, hasta);
        Map<String, Long> salidas = repoMovimiento.sumarCantidadPorCategoria(TipoMovimiento.salida, desde, hasta);
        Map<String, Long> transferencias = repoMovimiento.sumarCantidadPorCategoria(TipoMovimiento.transferencia, desde, hasta);
        Map<String, Long> stockPorCat = ((JPARepositorioProducto) repoProducto).sumarStockPorCategoria();
        List<Object[]> bajoStock = ((JPARepositorioProducto) repoProducto).topBajoStock(10);
        List<Producto> productos = repoProducto.findAll();

        List<String> labels = new ArrayList<>(new LinkedHashSet<>());
        labels.addAll(entradas.keySet());
        for (String k : salidas.keySet()) if (!labels.contains(k)) labels.add(k);
        for (String k : transferencias.keySet()) if (!labels.contains(k)) labels.add(k);
        for (String k : stockPorCat.keySet()) if (!labels.contains(k)) labels.add(k);

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/csv; charset=UTF-8");
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        response.setHeader("Content-Disposition", "attachment; filename=reportes_" + ts + ".csv");

        try (PrintWriter out = response.getWriter()) {
            out.write("\uFEFF");
            out.println("Movimientos por categoría");
            out.println("Categoría,Entradas,Salidas,Transferencias");
            for (String cat : labels) {
                long e = entradas.getOrDefault(cat, 0L);
                long s = salidas.getOrDefault(cat, 0L);
                long t = transferencias.getOrDefault(cat, 0L);
                out.println(escape(cat) + "," + e + "," + s + "," + t);
            }

            out.println();
            out.println("Stock por categoría");
            out.println("Categoría,Stock");
            for (String cat : labels) {
                long st = stockPorCat.getOrDefault(cat, 0L);
                out.println(escape(cat) + "," + st);
            }

            out.println();
            out.println("Productos con bajo stock");
            out.println("Producto,Stock");
            for (Object[] r : bajoStock) {
                out.println(escape((String) r[0]) + "," + ((Number) r[1]).longValue());
            }

            out.println();
            out.println("Detalle de inventario");
            out.println("ID,Nombre,Categoría,Proveedor,Precio,Stock");
            for (Producto p : productos) {
                String cat = p.getCategoria() != null ? p.getCategoria().getNombre() : "";
                String prov = p.getProveedor() != null ? p.getProveedor().getNombre() : "";
                out.println(p.getId() + "," + escape(p.getNombre()) + "," + escape(cat) + "," + escape(prov) + "," + p.getPrecio() + "," + p.getStock());
            }
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        String v = s.replace("\"", "\"\"");
        if (v.contains(",") || v.contains("\n") || v.contains("\r")) {
            return "\"" + v + "\"";
        }
        return v;
    }
}
