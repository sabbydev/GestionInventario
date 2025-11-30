package com.grupo5.gestioninventario.servlet;

import com.grupo5.gestioninventario.modelo.Categoria;
import com.grupo5.gestioninventario.modelo.Producto;
import com.grupo5.gestioninventario.repositorio.IRepositorioCategoria;
import com.grupo5.gestioninventario.repositorio.IRepositorioProveedor;
import com.grupo5.gestioninventario.repositorio.IRepositorioProducto;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioCategoria;
import com.grupo5.gestioninventario.repositorio.Implementaciones.JPARepositorioProveedor;
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
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet("/productos/nuevo")
public class ProductoNuevoServlet extends HttpServlet {

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
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=sesion");
            return;
        }
        request.setAttribute("vistaDinamica", "producto-form");
        request.setAttribute("categorias", repoCategoria.findAll());
        request.setAttribute("proveedores", repoProveedor.findAll());
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

        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String precioStr = request.getParameter("precio");
        String stockStr = request.getParameter("stock");
        String categoriaIdStr = request.getParameter("id_categoria");
        String proveedorIdStr = request.getParameter("id_proveedor");

        BigDecimal precio = new BigDecimal(precioStr);
        Integer stock = Integer.valueOf(stockStr);

        Categoria categoria = null;
        if (categoriaIdStr != null && !categoriaIdStr.isBlank()) {
            Optional<Categoria> oc = repoCategoria.findById(Integer.valueOf(categoriaIdStr));
            if (oc.isPresent()) categoria = oc.get();
        }

        com.grupo5.gestioninventario.modelo.Proveedor proveedor = null;
        if (proveedorIdStr != null && !proveedorIdStr.isBlank()) {
            Optional<com.grupo5.gestioninventario.modelo.Proveedor> opv = repoProveedor.findById(Integer.valueOf(proveedorIdStr));
            if (opv.isPresent()) proveedor = opv.get();
        }

        Producto p = new Producto();
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setCategoria(categoria);
        p.setProveedor(proveedor);

        repoProducto.save(p);
        session.setAttribute("flashSuccess", "Producto creado");
        response.sendRedirect(request.getContextPath() + "/inventario");
    }
}
