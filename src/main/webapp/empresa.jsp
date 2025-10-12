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
    <title>Empresa | Autorepuestos Perú</title>

    <!-- dashboard.css primero -->
    <link rel="stylesheet" href="css/dashboard.css">
    <link rel="stylesheet" href="css/empresa.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<body>
    <!-- Sidebar fijo -->
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
                        <li><a href="empresa.jsp" class="active"><i class="fa fa-building"></i> Empresa</a></li>
                        <li><a href="usuarios.jsp"><i class="fa fa-users"></i> Usuarios</a></li>
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
            <h1>Datos de la Empresa</h1>
            <div class="datetime">
                <i class="fa fa-clock"></i> <span id="hora"></span>
                <i class="fa fa-calendar"></i> <span id="fecha"></span>
            </div>
        </header>

        <section class="form-container">
            <form class="form-empresa">
                <div class="form-group">
                    <label for="nombre">Nombre de la Empresa:</label>
                    <input type="text" id="nombre" value="Autorepuestos Perú SAC" readonly>
                </div>

                <div class="form-group">
                    <label for="ruc">RUC:</label>
                    <input type="text" id="ruc" value="20568472910" readonly>
                </div>

                <div class="form-group">
                    <label for="direccion">Dirección:</label>
                    <textarea id="direccion" rows="2" readonly>Av. Los Olivos 123 - Lima</textarea>
                </div>

                <div class="form-group">
                    <label for="telefono">Teléfono:</label>
                    <input type="text" id="telefono" value="(01) 457-8219" readonly>
                </div>

                <div class="form-group">
                    <label for="correo">Correo:</label>
                    <input type="email" id="correo" value="contacto@autorepuestos.pe" readonly>
                </div>

                <div class="form-group">
                    <label for="representante">Representante Legal:</label>
                    <input type="text" id="representante" value="Carlos Méndez" readonly>
                </div>

                <div class="form-group botones">
                    <button type="button" class="btn-editar"><i class="fa fa-pen"></i> Editar Información</button>
                </div>
            </form>
        </section>
    </main>

    <script>
        // reloj simple
        function actualizarFechaHora() {
            const fecha = new Date();
            document.getElementById("fecha").textContent = fecha.toLocaleDateString("es-PE", { day: "2-digit", month: "2-digit", year: "numeric" });
            document.getElementById("hora").textContent = fecha.toLocaleTimeString("es-PE");
        }
        setInterval(actualizarFechaHora, 1000);
        actualizarFechaHora();

        // toggle sidebar
        const toggle = document.getElementById("menu-toggle");
        toggle.addEventListener("click", () => {
            document.querySelector(".sidebar").classList.toggle("active");
        });
    </script>
</body>
</html>




