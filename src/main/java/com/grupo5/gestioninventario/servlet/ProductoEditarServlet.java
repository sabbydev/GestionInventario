package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.modelo.Categoria;
import com.grupo5.gestioninventario.modelo.Producto;
import com.grupo5.gestioninventario.repositorio.IRepositorioCategoria;
import com.grupo5.gestioninventario.repositorio.IRepositorioProducto;
import com.grupo5.gestioninventario.repositorio.IRepositorioProveedor;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioCategoria;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioProducto;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioProveedor;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/productos/editar")
@MultipartConfig
public class ProductoEditarServlet extends HttpServlet {

    private EntityManagerFactory emf;
    private IRepositorioCategoria repoCategoria;
    private IRepositorioProveedor repoProveedor;
    private IRepositorioProducto repoProducto;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        repoCategoria = new JPARepositorioCategoria(emf);
        repoProveedor = new JPARepositorioProveedor(emf);
        repoProducto = new JPARepositorioProducto(emf);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=sesion");
            return;
        }
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/inventario");
            return;
        }
        Optional<Producto> op = repoProducto.findById(Integer.valueOf(idStr));
        if (op.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/inventario");
            return;
        }
        request.setAttribute("vistaDinamica", "producto-form");
        request.setAttribute("producto", op.get());
        request.setAttribute("categorias", repoCategoria.findAll());
        request.setAttribute("proveedores", repoProveedor.findAll());
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
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/inventario");
            return;
        }
        Integer id = Integer.valueOf(idParam);
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String precioStr = request.getParameter("precio");
        String stockStr = request.getParameter("stock");
        String categoriaIdStr = request.getParameter("id_categoria");
        String proveedorIdStr = request.getParameter("id_proveedor");

        String error = null;
        java.math.BigDecimal precio = null;
        Integer stock = null;
        if (nombre == null || nombre.isBlank()) {
            error = "Nombre requerido";
        }
        if (error == null) {
            if (precioStr == null || precioStr.isBlank()) {
                error = "Precio requerido";
            } else {
                try { precio = new java.math.BigDecimal(precioStr); } catch (RuntimeException ex) { error = "Precio inválido"; }
            }
        }
        if (error == null) {
            if (stockStr == null || stockStr.isBlank()) {
                error = "Stock requerido";
            } else {
                try { stock = Integer.valueOf(stockStr); } catch (RuntimeException ex) { error = "Stock inválido"; }
            }
        }

        Optional<Producto> op = repoProducto.findById(id);
        if (op.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/inventario");
            return;
        }
        Producto p = op.get();
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        if (error != null) {
            request.setAttribute("flashError", error);
            request.setAttribute("producto", p);
            request.setAttribute("categorias", repoCategoria.findAll());
            request.setAttribute("proveedores", repoProveedor.findAll());
            request.setAttribute("vistaDinamica", "producto-form");
            request.getRequestDispatcher("/WEB-INF/vista/layout.jsp").forward(request, response);
            return;
        }
        p.setPrecio(precio);
        p.setStock(stock);
        if (categoriaIdStr != null && !categoriaIdStr.isBlank()) {
            Optional<Categoria> oc = repoCategoria.findById(Integer.valueOf(categoriaIdStr));
            p.setCategoria(oc.orElse(null));
        } else {
            p.setCategoria(null);
        }
        if (proveedorIdStr != null && !proveedorIdStr.isBlank()) {
            Optional<com.grupo5.gestioninventario.modelo.Proveedor> opv = repoProveedor.findById(Integer.valueOf(proveedorIdStr));
            p.setProveedor(opv.orElse(null));
        } else {
            p.setProveedor(null);
        }
        String uploadDirReal = getServletContext().getRealPath("/uploads");
        java.nio.file.Path dir;
        if (uploadDirReal == null) {
            dir = java.nio.file.Paths.get(System.getProperty("java.io.tmpdir"), "uploads");
        } else {
            dir = java.nio.file.Paths.get(uploadDirReal);
        }
        try { java.nio.file.Files.createDirectories(dir); } catch (IOException ignored) {}

        Part imgPart = request.getPart("imagen");
        if (imgPart != null && imgPart.getSize() > 0) {
            String ct = imgPart.getContentType();
            if (ct != null && ct.startsWith("image/")) {
                try (java.io.InputStream is = imgPart.getInputStream()) {
                    byte[] data = is.readAllBytes();
                    p.setImagenData(data);
                    p.setImagenMime(ct);
                    p.setImagenUrl(null);
                }
            }
        }
        Part fichaPart = request.getPart("ficha");
        if (fichaPart != null && fichaPart.getSize() > 0) {
            String ct = fichaPart.getContentType();
            if (ct != null && ct.equals("application/pdf")) {
                try (java.io.InputStream is = fichaPart.getInputStream()) {
                    byte[] data = is.readAllBytes();
                    p.setFichaTecnicaData(data);
                    p.setFichaTecnicaMime(ct);
                    p.setFichaTecnicaUrl(null);
                }
            }
        }
        Part manualPart = request.getPart("manual");
        if (manualPart != null && manualPart.getSize() > 0) {
            String ct = manualPart.getContentType();
            if (ct != null && ct.equals("application/pdf")) {
                try (java.io.InputStream is = manualPart.getInputStream()) {
                    byte[] data = is.readAllBytes();
                    p.setManualData(data);
                    p.setManualMime(ct);
                    p.setManualUrl(null);
                }
            }
        }

        repoProducto.update(p);
        session.setAttribute("flashSuccess", "Producto actualizado");
        response.sendRedirect(request.getContextPath() + "/inventario");
    }
}
