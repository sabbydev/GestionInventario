package com.grupo5.gestioninventario.filtro;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AutorizacionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();
        String ctx = request.getContextPath();

        // Rutas públicas
        if (uri.startsWith(ctx + "/login") || uri.startsWith(ctx + "/resources")
                || uri.startsWith(ctx + "/css") || uri.startsWith(ctx + "/js")
                || uri.startsWith(ctx + "/img")) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = request.getSession(false);
        Integer usuarioId = session != null ? (Integer) session.getAttribute("usuarioId") : null;
        if (usuarioId == null) {
            response.sendRedirect(ctx + "/login?error=sesion");
            return;
        }
        String rol = session.getAttribute("usuarioRol") != null ? ((String) session.getAttribute("usuarioRol")) : "";

        // Autorización por ruta
        boolean permitido = true;
        if (uri.startsWith(ctx + "/usuarios")) {
            permitido = "administrador".equalsIgnoreCase(rol);
        } else if (uri.startsWith(ctx + "/reportes")) {
            permitido = "administrador".equalsIgnoreCase(rol) || "gerente".equalsIgnoreCase(rol);
        } else if (uri.startsWith(ctx + "/movimientos") || uri.startsWith(ctx + "/inventario")
                || uri.startsWith(ctx + "/proveedores") || uri.startsWith(ctx + "/productos")
                || uri.startsWith(ctx + "/categorias")) {
            permitido = "administrador".equalsIgnoreCase(rol) || "operador".equalsIgnoreCase(rol) || "gerente".equalsIgnoreCase(rol);
        }

        if (!permitido) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(req, res);
    }
}
