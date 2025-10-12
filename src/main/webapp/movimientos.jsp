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
    <title>Movimientos | Autorepuestos Perú</title>
    <link rel="stylesheet" href="css/dashboard.css">
    <link rel="stylesheet" href="css/reportes.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
                <li><a href="movimientos.jsp"><i class="fa fa-exchange-alt"></i> Movimientos</a></li>
                <li><a href="categorias.jsp"><i class="fa fa-tags"></i> Categorías</a></li>
                <li><a href="reportes.jsp"><i class="fa fa-chart-bar"></i> Reportes</a></li>
                <li class="submenu">
                    <a href="#"><i class="fa fa-cogs"></i> Configuración</a>
                    <ul class="submenu-content">
                        <li><a href="empresa.jsp"><i class="fa fa-building"></i> Empresa</a></li>
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
            <h1><i class="fa fa-exchange-alt"></i> Movimientos de Inventario</h1>
            <div class="datetime">
                <i class="fa fa-clock"></i> <span id="hora"></span>
                <i class="fa fa-calendar"></i> <span id="fecha"></span>
            </div>
        </header>

        <!-- Tarjetas resumen -->
        <section class="summary-cards">
            <div class="card resumen entradas">
                <i class="fa fa-arrow-down"></i>
                <div>
                    <h3>125</h3>
                    <p>Entradas registradas</p>
                </div>
            </div>
            <div class="card resumen salidas">
                <i class="fa fa-arrow-up"></i>
                <div>
                    <h3>87</h3>
                    <p>Salidas realizadas</p>
                </div>
            </div>
            <div class="card resumen total">
                <i class="fa fa-list"></i>
                <div>
                    <h3>212</h3>
                    <p>Total de movimientos</p>
                </div>
            </div>
        </section>

        <!-- Barra de filtros -->
        <section class="report-toolbar">
            <label>Desde:</label>
            <input type="date" id="fechaInicio">
            <label>Hasta:</label>
            <input type="date" id="fechaFin">
            <label>Tipo:</label>
            <select id="tipoMovimiento">
                <option value="todos">Todos</option>
                <option value="entrada">Entrada</option>
                <option value="salida">Salida</option>
            </select>
            <button class="btn-accion"><i class="fa fa-search"></i> Filtrar</button>
            <button class="btn-accion"><i class="fa fa-file-pdf"></i> Exportar PDF</button>
        </section>

        <!-- Gráficos -->
        <section class="report-container">
            <div class="chart-box">
                <h2><i class="fa fa-chart-bar"></i> Entradas y Salidas por Mes</h2>
                <canvas id="movimientosBar"></canvas>
            </div>

            <div class="chart-box">
                <h2><i class="fa fa-chart-line"></i> Tendencia de Movimientos</h2>
                <canvas id="movimientosLine"></canvas>
            </div>
        </section>

        <!-- Tabla de Detalle -->
        <section class="detalle-inventario">
            <div class="tabla">
                <h2><i class="fa fa-list"></i> Detalle de Movimientos</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Fecha</th>
                            <th>Producto</th>
                            <th>Tipo</th>
                            <th>Cantidad</th>
                            <th>Usuario</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr><td>2025-10-01</td><td>Batería 45Ah</td><td>Entrada</td><td>20</td><td>admin</td></tr>
                        <tr><td>2025-10-02</td><td>Filtro de aceite</td><td>Salida</td><td>15</td><td>admin</td></tr>
                        <tr><td>2025-10-03</td><td>Llave inglesa</td><td>Entrada</td><td>10</td><td>admin</td></tr>
                        <tr><td>2025-10-05</td><td>Amortiguador</td><td>Salida</td><td>5</td><td>admin</td></tr>
                        <tr><td>2025-10-07</td><td>Aceite sintético</td><td>Entrada</td><td>25</td><td>admin</td></tr>
                    </tbody>
                </table>
            </div>
        </section>
    </main>

    <script>
        // Fecha y hora dinámica
        function actualizarFechaHora() {
            const fecha = new Date();
            document.getElementById("fecha").textContent = fecha.toLocaleDateString("es-PE", {
                day: "2-digit", month: "2-digit", year: "numeric"
            });
            document.getElementById("hora").textContent = fecha.toLocaleTimeString("es-PE");
        }
        setInterval(actualizarFechaHora, 1000);
        actualizarFechaHora();

        // === Gráfico de barras: Entradas y salidas ===
        const ctxBar = document.getElementById('movimientosBar').getContext('2d');
        new Chart(ctxBar, {
            type: 'bar',
            data: {
                labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre'],
                datasets: [
                    {
                        label: 'Entradas',
                        data: [25, 40, 35, 50, 42, 38, 44, 48, 53, 41],
                        backgroundColor: '#20c997'
                    },
                    {
                        label: 'Salidas',
                        data: [20, 30, 28, 40, 38, 35, 39, 42, 47, 37],
                        backgroundColor: '#dc3545'
                    }
                ]
            },
            options: {
                responsive: true,
                scales: { y: { beginAtZero: true } },
                plugins: { legend: { position: 'bottom' } }
            }
        });

        // === Gráfico de línea: Tendencia ===
        const ctxLine = document.getElementById('movimientosLine').getContext('2d');
        new Chart(ctxLine, {
            type: 'line',
            data: {
                labels: ['01 Oct', '02 Oct', '03 Oct', '04 Oct', '05 Oct', '06 Oct', '07 Oct'],
                datasets: [{
                    label: 'Movimientos diarios',
                    data: [10, 15, 8, 20, 18, 22, 25],
                    borderColor: '#56A0C9',
                    fill: false,
                    tension: 0.3
                }]
            },
            options: {
                responsive: true,
                plugins: { legend: { display: true, position: 'bottom' } }
            }
        });
    </script>
</body>
</html>






