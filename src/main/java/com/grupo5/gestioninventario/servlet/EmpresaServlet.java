package com.grupo5.gestioninventario.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import com.grupo5.gestioninventario.modelo.EmpresaInfo;
import java.io.IOException;

@WebServlet("/empresa")
public class EmpresaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=sesion");
            return;
        }
        EmpresaInfo info = getOrInitEmpresa(request.getServletContext());
        request.setAttribute("empresa", info);
        request.setAttribute("vistaDinamica", "empresa");
        String ok = request.getParameter("ok");
        if ("1".equals(ok)) request.setAttribute("exitoEmpresa", "Información de la empresa actualizada");
        request.getRequestDispatcher("/WEB-INF/vista/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=sesion");
            return;
        }

        String nombre = request.getParameter("nombre");
        String ruc = request.getParameter("ruc");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String correo = request.getParameter("correo");
        String representante = request.getParameter("representante");

        EmpresaInfo info = getOrInitEmpresa(request.getServletContext());
        if (nombre != null) info.setNombre(nombre);
        if (ruc != null) info.setRuc(ruc);
        if (direccion != null) info.setDireccion(direccion);
        if (telefono != null) info.setTelefono(telefono);
        if (correo != null) info.setCorreo(correo);
        if (representante != null) info.setRepresentante(representante);
        request.getServletContext().setAttribute("empresaInfo", info);

        response.sendRedirect(request.getContextPath() + "/empresa?ok=1");
    }

    private EmpresaInfo getOrInitEmpresa(ServletContext ctx) {
        Object o = ctx.getAttribute("empresaInfo");
        if (o instanceof EmpresaInfo) return (EmpresaInfo) o;
        EmpresaInfo def = EmpresaInfo.defaults();
        ctx.setAttribute("empresaInfo", def);
        return def;
    }
}






