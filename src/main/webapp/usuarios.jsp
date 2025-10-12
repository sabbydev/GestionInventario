<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%
    com.grupo5.gestioninventario.modelo.Usuario usuario =
        (com.grupo5.gestioninventario.modelo.Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.html?error=sesion");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Usuarios | Autorepuestos Perú</title>

    <!-- dashboard.css primero, luego este -->
    <link rel="stylesheet" href="css/dashboard.css">
    <link rel="stylesheet" href="css/usuarios.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<body>
    <!-- Sidebar (igual que dashboard) -->
    <aside class="sidebar">
        <h2>Autorepuestos</h2>
        <nav>
            <ul>
                <li><a href="dashboard.jsp"><i class="fa fa-home"></i> Inicio</a></li>
                <li><a href="inventario.jsp"><i class="fa fa-box"></i> Inventario</a></li>
                <li><a href="proveedores.jsp"><i class="fa fa-truck"></i> Proveedores</a></li>
                <li><a href="movimientos.jsp"><i class="fa fa-exchange-alt"></i> Movimientos</a></li>
                <li><a href="categorias.jsp"><i class="fa fa-tags"></i> Categorías</a></li>
                <li><a href="reportes.jsp"><i class="fa fa-chart-bar"></i> Reportes</a></li>

                <li class="submenu">
                    <a href="#"><i class="fa fa-cogs"></i> Configuración</a>
                    <ul class="submenu-content">
                        <li><a href="empresa"><i class="fa fa-building"></i> Empresa</a></li>
                        <li><a href="usuarios" class="active"><i class="fa fa-users"></i> Usuarios</a></li>
                    </ul>
                </li>

                <li><a href="LogoutServlet"><i class="fa fa-sign-out-alt"></i> Cerrar sesión</a></li>
            </ul>
        </nav>
    </aside>

    <!-- Contenido principal -->
    <main class="content">
        <header class="header-main">
            <button class="menu-toggle" id="menu-toggle"><i class="fa fa-bars"></i></button>
            <h1>Gestión de Usuarios</h1>
            <div class="datetime">
                <i class="fa fa-clock"></i> <span id="hora"></span>
                <i class="fa fa-calendar"></i> <span id="fecha"></span>
            </div>
        </header>

        <!-- Botón NUEVO USUARIO (A LA IZQUIERDA) -->
        <section class="summary-actions">
            <button class="btn-nuevo"><i class="fa fa-user-plus"></i> Nuevo Usuario</button>
        </section>

        <!-- Tabla de usuarios -->
        <section class="tabla-container">
            <table class="tabla-usuarios">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Correo</th>
                        <th>Rol</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>1</td>
                        <td>Elvis Arqueros</td>
                        <td>admin@demo.local</td>
                        <td>Administrador</td>
                        <td><span class="estado activo">Activo</span></td>
                        <td class="acciones">
                            <button class="btn-editar"><i class="fa fa-pen"></i></button>
                            <button class="btn-eliminar"><i class="fa fa-trash"></i></button>
                        </td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td>Liz</td>
                        <td>operador1@demo.local</td>
                        <td>Vendedor</td>
                        <td><span class="estado activo">Activo</span></td>
                        <td class="acciones">
                            <button class="btn-editar"><i class="fa fa-pen"></i></button>
                            <button class="btn-eliminar"><i class="fa fa-trash"></i></button>
                        </td>
                    </tr>
                    <tr>
                        <td>3</td>
                        <td>Juan Carlos</td>
                        <td>gerente1@demo.local</td>
                        <td>Almacén</td>
                        <td><span class="estado inactivo">Inactivo</span></td>
                        <td class="acciones">
                            <button class="btn-editar"><i class="fa fa-pen"></i></button>
                            <button class="btn-eliminar"><i class="fa fa-trash"></i></button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </section>
    </main>

    <script>
        // reloj simple
        function actualizarFechaHora() {
            const fecha = new Date();
            const fechaEl = document.getElementById("fecha");
            const horaEl = document.getElementById("hora");
            if (fechaEl) fechaEl.textContent = fecha.toLocaleDateString("es-PE", { day: "2-digit", month: "2-digit", year: "numeric" });
            if (horaEl) horaEl.textContent = fecha.toLocaleTimeString("es-PE");
        }
        setInterval(actualizarFechaHora, 1000);
        actualizarFechaHora();

        // toggle sidebar
        const toggle = document.getElementById("menu-toggle");
        if (toggle) {
            toggle.addEventListener("click", () => {
                const sb = document.querySelector(".sidebar");
                if (sb) sb.classList.toggle("active");
            });
        }
    </script>
</body>
</html>




