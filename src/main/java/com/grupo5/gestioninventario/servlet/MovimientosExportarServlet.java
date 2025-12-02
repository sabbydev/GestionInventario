package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.modelo.Movimiento;
import com.grupo5.gestioninventario.modelo.TipoMovimiento;
import com.grupo5.gestioninventario.repositorio.IRepositorioMovimiento;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/movimientos/exportar")
public class MovimientosExportarServlet extends HttpServlet {

    private EntityManagerFactory emf;
    private IRepositorioMovimiento repoMovimiento;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
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

        List<Movimiento> lista = ((JPARepositorioMovimiento) repoMovimiento).buscar(desde, hasta, tipoFiltro);
        long entradasCnt = ((JPARepositorioMovimiento) repoMovimiento).contar(TipoMovimiento.entrada, desde, hasta);
        long salidasCnt = ((JPARepositorioMovimiento) repoMovimiento).contar(TipoMovimiento.salida, desde, hasta);
        long totalCnt = ((JPARepositorioMovimiento) repoMovimiento).contar(null, desde, hasta);

        org.apache.pdfbox.pdmodel.PDDocument doc = new org.apache.pdfbox.pdmodel.PDDocument();
        org.apache.pdfbox.pdmodel.PDPage page = new org.apache.pdfbox.pdmodel.PDPage(org.apache.pdfbox.pdmodel.common.PDRectangle.LETTER);
        doc.addPage(page);
        org.apache.pdfbox.pdmodel.font.PDFont font = org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
        org.apache.pdfbox.pdmodel.PDPageContentStream cs = new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page);

        float margin = 50;
        float y = page.getMediaBox().getHeight() - margin;
        float leading = 16;

        cs.beginText();
        cs.setFont(font, 14);
        cs.newLineAtOffset(margin, y);
        cs.showText("Reporte de Movimientos");
        cs.setFont(font, 10);
        y -= leading;
        cs.newLineAtOffset(0, -leading);
        String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        cs.showText("Generado: " + nowStr);
        y -= leading;
        cs.newLineAtOffset(0, -leading);
        String filtroStr = "Filtros: " + (fi != null ? ("desde " + fi + " ") : "") + (ff != null ? ("hasta " + ff + " ") : "") + (tipoParam != null ? ("tipo " + tipoParam) : "todos");
        cs.showText(filtroStr);
        y -= leading;
        cs.newLineAtOffset(0, -leading);
        cs.showText("Resumen: Entradas=" + entradasCnt + " Salidas=" + salidasCnt + " Total=" + totalCnt);

        y -= leading * 2;
        cs.setFont(font, 10);
        cs.newLineAtOffset(0, -leading);
        cs.showText("Fecha            Tipo        Producto                       Cantidad    Usuario");
        y -= leading;

        int count = 0;
        for (Movimiento m : lista) {
            String fecha = String.valueOf(m.getFecha());
            String tipo = String.valueOf(m.getTipo());
            String prod = m.getProducto() != null ? m.getProducto().getNombre() : "";
            if (prod.length() > 30) prod = prod.substring(0, 30);
            String cant = String.valueOf(m.getCantidad());
            String usr = m.getUsuario() != null ? m.getUsuario().getNombre() : "";
            String line = String.format("%-16s %-10s %-30s %-10s %-15s", fecha, tipo, prod, cant, usr);
            cs.newLineAtOffset(0, -leading);
            cs.showText(line);
            y -= leading;
            count++;
            if (y < margin + leading * 4) {
                cs.endText();
                cs.close();
                page = new org.apache.pdfbox.pdmodel.PDPage(org.apache.pdfbox.pdmodel.common.PDRectangle.LETTER);
                doc.addPage(page);
                cs = new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page);
                y = page.getMediaBox().getHeight() - margin;
                cs.beginText();
                cs.setFont(font, 10);
                cs.newLineAtOffset(margin, y);
            }
            if (count >= 200) break;
        }
        cs.endText();
        cs.close();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=movimientos.pdf");
        java.io.OutputStream os = response.getOutputStream();
        doc.save(os);
        doc.close();
        os.flush();
    }
}
