<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard | Autorepuestos Per�</title>
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
                
                <li><a href="clientes.jsp"><i class="fa fa-users"></i> Inventario</a></li>
                <li><a href="proveedores.jsp"><i class="fa fa-truck"></i> Proveedores</a></li>
                <li><a href="categorias.jsp"><i class="fa fa-tags"></i> Categor�as</a></li>
                <li><a href="reportes.jsp"><i class="fa fa-chart-bar"></i> Reportes</a></li>
                <li><a href="admin.jsp"><i class="fa fa-cogs"></i> Administraci�n</a></li>
                <li><a href="LogoutServlet"><i class="fa fa-sign-out-alt"></i> Cerrar sesi�n</a></li>
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
            <h1>Bienvenido, </h1>
            <div class="datetime">
                <i class="fa fa-clock"></i> <span id="hora"></span>
                <i class="fa fa-calendar"></i> <span id="fecha"></span>
            </div>
        </header>

        <!-- Tarjetas de acceso r�pido -->
        <section class="cards">

            <a href="clientes.jsp" class="card">
                <i class="fa fa-users"></i>
                <h3>inventario</h3>
                <p>Ir a inventario ></p>
            </a>
            <a href="proveedores.jsp" class="card">
                <i class="fa fa-truck"></i>
                <h3>Proveedores</h3>
                <p>Ir a proveedores ></p>
            </a>
            <a href="categorias.jsp" class="card">
                <i class="fa fa-tags"></i>
                <h3>Categor�as</h3>
                <p>Ir a categor�as ></p>
            </a>
        </section>

        <!-- Secci�n de alertas -->
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

        // Toggle sidebar en m�viles
        document.getElementById("menu-toggle").addEventListener("click", function () {
            document.querySelector(".sidebar").classList.toggle("active");
        });
    </script>
</body>
</html>
