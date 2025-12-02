package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.modelo.Producto;
import com.grupo5.gestioninventario.repositorio.IRepositorioProducto;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioProducto;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@WebServlet("/api/productos/documento")
public class DocumentoProductoServlet extends HttpServlet {

    private EntityManagerFactory emf;
    private IRepositorioProducto repoProducto;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        repoProducto = new JPARepositorioProducto(emf);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String idStr = request.getParameter("id");
        String tipo = request.getParameter("tipo");
        if (idStr == null || idStr.isBlank() || tipo == null || tipo.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Integer id = Integer.valueOf(idStr);
        Optional<Producto> op = repoProducto.findById(id);
        if (op.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Producto p = op.get();
        if ("imagen".equals(tipo)) {
            if (p.getImagenData() != null) {
                String ct = p.getImagenMime() != null ? p.getImagenMime() : "image/*";
                response.setContentType(ct);
                response.setHeader("Cache-Control", "private, max-age=120");
                response.setContentLength(p.getImagenData().length);
                response.getOutputStream().write(p.getImagenData());
                return;
            }
        }

        String url;
        switch (tipo) {
            case "imagen":
                url = p.getImagenUrl();
                break;
            case "ficha":
                url = p.getFichaTecnicaUrl();
                break;
            case "manual":
                url = p.getManualUrl();
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
        }
        if (url == null || url.isBlank()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String fileName = url.substring(url.lastIndexOf('/') + 1);
        String uploadDirReal = getServletContext().getRealPath("/uploads");
        Path dir;
        if (uploadDirReal == null) {
            dir = Paths.get(System.getProperty("java.io.tmpdir"), "uploads");
        } else {
            dir = Paths.get(uploadDirReal);
        }
        Path pth = dir.resolve(fileName);
        if (!Files.exists(pth)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String ct = Files.probeContentType(pth);
        if (ct == null) ct = "application/octet-stream";
        response.setContentType(ct);
        response.setHeader("Cache-Control", "private, max-age=120");
        response.setContentLengthLong(Files.size(pth));
        Files.copy(pth, response.getOutputStream());
    }
}
