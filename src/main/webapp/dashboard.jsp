<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %> <!-- habilita el uso de sesión -->
<%
    // Obtener el usuario almacenado en sesión (desde LoginServlet)
    com.grupo5.gestioninventario.modelo.Usuario usuario = 
        (com.grupo5.gestioninventario.modelo.Usuario) session.getAttribute("usuario");

    // Si no hay usuario logueado, redirigir al login
    if (usuario == null) {
        response.sendRedirect("login.html?error=sesion");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard | Autorepuestos Perú</title>
    <link rel="stylesheet" href="css/dashboard.css">
    <!-- Iconos -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<body>
    <!-- Sidebar -->
    <aside class="sidebar">
        <h2>Autorepuestos</h2>
        <nav>
            <ul>
                <li><a href="dashboard.jsp"><i class="fa fa-home"></i> Inicio</a></li>   
                <li><a href="inventario.jsp"><i class="fa fa-box"></i> Inventario</a></li>
                <li><a href="proveedores.jsp"><i class="fa fa-truck"></i> Proveedores</a></li>
                <li><a href="categorias.jsp"><i class="fa fa-tags"></i> Categorí­as</a></li>
                <li><a href="reportes.jsp"><i class="fa fa-chart-bar"></i> Reportes</a></li>
                <li><a href="admin.jsp"><i class="fa fa-cogs"></i> Administración</a></li>
                <li><a href="LogoutServlet"><i class="fa fa-sign-out-alt"></i> Cerrar sesión</a></li>
            </ul>
        </nav>
    </aside>

    <!-- Contenido principal -->
    <main class="content">
        <!-- Encabezado superior -->
        <header class="header-main">
            <button class="menu-toggle" id="menu-toggle">
                <i class="fa fa-bars"></i>
            </button>
            <!-- Mostramos el nombre del usuario desde la sesión -->
            <h1>Bienvenido, <%= usuario.getNombre() %></h1>
            <div class="datetime">
                <i class="fa fa-clock"></i> <span id="hora"></span>
                <i class="fa fa-calendar"></i> <span id="fecha"></span>
            </div>
        </header>

        <!-- Tarjetas de acceso rápido -->
        <section class="cards">
            <a href="inventario.jsp" class="card">
                <i class="fa fa-box"></i>
                <h3>Inventario</h3>
                <p>Ir a inventario ></p>
            </a>
            <a href="proveedores.jsp" class="card">
                <i class="fa fa-truck"></i>
                <h3>Proveedores</h3>
                <p>Ir a proveedores ></p>
            </a>
            <a href="categorias.jsp" class="card">
                <i class="fa fa-tags"></i>
                <h3>Categorí­as</h3>
                <p>Ir a categorí­as ></p>
            </a>
        </section>

        <!-- Sección de alertas -->
        <section class="alerts">
            <h2>Alertas de Inventario</h2>
            <p>No hay alertas por el momento.</p>
        </section>
    </main>

    <!-- Script fecha y hora -->
    <script>
        function actualizarFechaHora() {
            const fecha = new Date();
            document.getElementById("fecha").textContent = fecha.toLocaleDateString("es-PE", {
                day: "2-digit", month: "2-digit", year: "numeric"
            });
            document.getElementById("hora").textContent = fecha.toLocaleTimeString("es-PE");
        }
        setInterval(actualizarFechaHora, 1000);
        actualizarFechaHora();

        // Toggle sidebar en móviles
        document.getElementById("menu-toggle").addEventListener("click", function () {
            document.querySelector(".sidebar").classList.toggle("active");
        });
    </script>
</body>
</html>
