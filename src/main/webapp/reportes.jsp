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
    <title>Reportes | Autorepuestos Perú</title>
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
                <li><a class="active" href="reportes.jsp"><i class="fa fa-chart-bar"></i> Reportes</a></li>
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
            <h1><i class="fa fa-chart-line"></i> Reportes del Sistema</h1>
            <div class="datetime">
                <i class="fa fa-clock"></i> <span id="hora"></span>
                <i class="fa fa-calendar"></i> <span id="fecha"></span>
            </div>
        </header>

        <!-- Tarjetas resumen -->
        <section class="summary-cards">
            <div class="card resumen ventas">
                <i class="fa fa-cash-register"></i>
                <div>
                    <h3>S/ 32,540</h3>
                    <p>Ventas del mes</p>
                </div>
            </div>
            <div class="card resumen compras">
                <i class="fa fa-shopping-cart"></i>
                <div>
                    <h3>S/ 18,220</h3>
                    <p>Compras registradas</p>
                </div>
            </div>
            <div class="card resumen stock">
                <i class="fa fa-boxes-stacked"></i>
                <div>
                    <h3>1,245</h3>
                    <p>Productos en stock</p>
                </div>
            </div>
            <div class="card resumen usuarios">
                <i class="fa fa-user-check"></i>
                <div>
                    <h3>3</h3>
                    <p>Usuarios activos</p>
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
            <button class="btn-accion"><i class="fa fa-refresh"></i> Actualizar</button>
            <button class="btn-accion"><i class="fa fa-file-excel"></i> Exportar Excel</button>
        </section>

        <!-- Gráficos -->
        <section class="report-container">
            <div class="chart-box">
                <h2><i class="fa fa-chart-column"></i> Movimientos por Categoría</h2>
                <canvas id="barChart"></canvas>
            </div>

            <div class="chart-box">
                <h2><i class="fa fa-chart-pie"></i> Distribución de Inventario</h2>
                <canvas id="pieChart"></canvas>
            </div>
        </section>

        <!-- Nueva sección: Detalle de Inventario -->
        <section class="detalle-inventario">
            <div class="tabla">
                <h2><i class="fa fa-box"></i> Detalle de Inventario</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Nombre</th>
                            <th>Categoría</th>
                            <th>Marca</th>
                            <th>Precio</th>
                            <th>Stock</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr><td>Batería 45Ah</td><td>Eléctrico</td><td>Panasonic</td><td>S/ 350.00</td><td>35</td></tr>
                        <tr><td>Filtro de aceite</td><td>Lubricantes</td><td>Shell</td><td>S/ 25.90</td><td>75</td></tr>
                        <tr><td>Llave inglesa</td><td>Herramientas</td><td>Bosch</td><td>S/ 99.90</td><td>12</td></tr>
                        <tr><td>Aceite sintético</td><td>Lubricantes</td><td>Castrol</td><td>S/ 120.00</td><td>54</td></tr>
                        <tr><td>Amortiguador</td><td>Suspensión</td><td>KYB</td><td>S/ 480.00</td><td>8</td></tr>
                    </tbody>
                </table>
            </div>

            <div class="grafico">
                <h2><i class="fa fa-chart-pie"></i> Productos con bajo stock</h2>
                <canvas id="stockChart"></canvas>
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

        // === Gráfico de barras ===
        const ctxBar = document.getElementById('barChart').getContext('2d');
        new Chart(ctxBar, {
            type: 'bar',
            data: {
                labels: ['Repuestos', 'Herramientas', 'Accesorios', 'Lubricantes', 'Filtros', 'Baterías'],
                datasets: [{
                    label: 'Cantidad',
                    data: [120, 80, 60, 40, 70, 55],
                    backgroundColor: ['#f0ad4e', '#62BCAD', '#56A0C9', '#485563', '#20c997', '#ffc107']
                }]
            },
            options: {
                responsive: true,
                scales: { y: { beginAtZero: true } },
                plugins: { legend: { display: false } }
            }
        });

        // === Gráfico circular ===
        const ctxPie = document.getElementById('pieChart').getContext('2d');
        new Chart(ctxPie, {
            type: 'doughnut',
            data: {
                labels: ['Stock Disponible', 'Stock Reservado', 'Stock Agotado'],
                datasets: [{
                    data: [65, 25, 10],
                    backgroundColor: ['#62BCAD', '#56A0C9', '#f0ad4e']
                }]
            },
            options: {
                cutout: '70%',
                plugins: { legend: { position: 'bottom' } }
            }
        });

        // === Gráfico bajo stock ===
        const ctxStock = document.getElementById('stockChart').getContext('2d');
        new Chart(ctxStock, {
            type: 'bar',
            data: {
                labels: ['Llave inglesa', 'Amortiguador', 'Filtro de aire'],
                datasets: [{
                    label: 'Stock actual',
                    data: [12, 8, 5],
                    backgroundColor: ['#dc3545', '#ffc107', '#007bff']
                }]
            },
            options: {
                responsive: true,
                scales: { y: { beginAtZero: true } },
                plugins: { legend: { display: false } }
            }
        });
    </script>
</body>
</html>









