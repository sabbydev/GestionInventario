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
    <title>Proveedores | Autorepuestos Perú</title>

    <!-- CSS principal -->
    
    <link rel="stylesheet" href="css/proveedores.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<body>
    <!-- Sidebar fija -->
    <aside class="sidebar">
        <h2>Autorepuestos</h2>
        <nav>
            <ul>
                <li><a href="dashboard.jsp"><i class="fa fa-home"></i> Inicio</a></li>
                <li><a href="inventario.jsp"><i class="fa fa-box"></i> Inventario</a></li>
                <li><a href="proveedores.jsp" class="active"><i class="fa fa-truck"></i> Proveedores</a></li>
                <li><a href="movimientos.jsp"><i class="fa fa-exchange-alt"></i> Movimientos</a></li>
                <li><a href="categorias.jsp"><i class="fa fa-tags"></i> Categorías</a></li>
                <li><a href="reportes.jsp"><i class="fa fa-chart-bar"></i> Reportes</a></li>

                <!-- Submenú Configuración -->
                <li class="submenu">
                    <a href="#"><i class="fa fa-cogs"></i> Configuración</a>
                    <ul class="submenu-content">
                        <li><a href="empresa"><i class="fa fa-building"></i> Empresa</a></li>
                        <li><a href="usuarios"><i class="fa fa-users"></i> Usuarios</a></li>
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
            <h1>Gestión de Proveedores</h1>
            <div class="datetime">
                <i class="fa fa-clock"></i> <span id="hora"></span>
                <i class="fa fa-calendar"></i> <span id="fecha"></span>
            </div>
        </header>

        <!-- Sección principal -->
        <section class="proveedores-container">
            <div class="toolbar">
                <button class="btn-nuevo"><i class="fa fa-plus"></i> Nuevo Proveedor</button>
                <input type="text" class="buscador" placeholder="Buscar proveedor...">
            </div>

            <table class="tabla-proveedores">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>RUC</th>
                        <th>Teléfono</th>
                        <th>Correo</th>
                        <th>Dirección</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>01</td>
                        <td>Repuestos del Norte S.A.C.</td>
                        <td>20451234789</td>
                        <td>(01) 567-2345</td>
                        <td>ventas@repuestosnorte.com</td>
                        <td>Av. Universitaria 156 - Lima</td>
                        <td class="acciones">
                            <button class="btn-editar"><i class="fa fa-pen"></i></button>
                            <button class="btn-eliminar"><i class="fa fa-trash"></i></button>
                        </td>
                    </tr>
                    <tr>
                        <td>02</td>
                        <td>Autopartes Andinas</td>
                        <td>20569874521</td>
                        <td>(01) 458-8765</td>
                        <td>contacto@autopartesandinas.pe</td>
                        <td>Jr. Los Olivos 450 - Ate</td>
                        <td class="acciones">
                            <button class="btn-editar"><i class="fa fa-pen"></i></button>
                            <button class="btn-eliminar"><i class="fa fa-trash"></i></button>
                        </td>
                    </tr>
                    <tr>
                        <td>03</td>
                        <td>Motores del Sur E.I.R.L.</td>
                        <td>20647893215</td>
                        <td>(01) 478-2234</td>
                        <td>info@motoresdelsur.pe</td>
                        <td>Av. Arequipa 980 - Surquillo</td>
                        <td class="acciones">
                            <button class="btn-editar"><i class="fa fa-pen"></i></button>
                            <button class="btn-eliminar"><i class="fa fa-trash"></i></button>
                        </td>
                    </tr>
                    <tr>
                        <td>04</td>
                        <td>Lubricantes Premium S.A.C.</td>
                        <td>20458962347</td>
                        <td>(01) 589-3345</td>
                        <td>ventas@lubricantespremium.pe</td>
                        <td>Av. Colonial 550 - Callao</td>
                        <td class="acciones">
                            <button class="btn-editar"><i class="fa fa-pen"></i></button>
                            <button class="btn-eliminar"><i class="fa fa-trash"></i></button>
                        </td>
                    </tr>
                    <tr>
                        <td>05</td>
                        <td>Distribuidora Liz</td>
                        <td>20687451236</td>
                        <td>(01) 426-7789</td>
                        <td>contacto@distribuidoraliz.com</td>
                        <td>Jr. Paruro 789 - Lima</td>
                        <td class="acciones">
                            <button class="btn-editar"><i class="fa fa-pen"></i></button>
                            <button class="btn-eliminar"><i class="fa fa-trash"></i></button>
                        </td>
                    </tr>
                    <tr>
                        <td>06</td>
                        <td>Refacciones del Pacífico</td>
                        <td>20745896321</td>
                        <td>(01) 765-2211</td>
                        <td>ventas@refapacifico.pe</td>
                        <td>Av. La Marina 1100 - San Miguel</td>
                        <td class="acciones">
                            <button class="btn-editar"><i class="fa fa-pen"></i></button>
                            <button class="btn-eliminar"><i class="fa fa-trash"></i></button>
                        </td>
                    </tr>
                    <tr>
                        <td>07</td>
                        <td>Importadora Autopartes</td>
                        <td>20896354712</td>
                        <td>(01) 458-3322</td>
                        <td>info@autopartes.pe</td>
                        <td>Av. Venezuela 1350 - Breña</td>
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
        // Fecha y hora
        function actualizarFechaHora() {
            const fecha = new Date();
            document.getElementById("fecha").textContent = fecha.toLocaleDateString("es-PE", {
                day: "2-digit", month: "2-digit", year: "numeric"
            });
            document.getElementById("hora").textContent = fecha.toLocaleTimeString("es-PE");
        }
        setInterval(actualizarFechaHora, 1000);
        actualizarFechaHora();

        // Sidebar responsive
        document.getElementById("menu-toggle").addEventListener("click", function () {
            document.querySelector(".sidebar").classList.toggle("active");
        });
    </script>
</body>
</html>

