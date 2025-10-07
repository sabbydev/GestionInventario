<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inicio | Autorepuestos Perú</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/inicio.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>

<body>
    <aside class="sidebar">
        <h2>Autorepuestos</h2>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/dashboard"><i class="fa fa-home"></i> Inicio</a></li>   
                <li><a href="${pageContext.request.contextPath}/inventario"><i class="fa fa-box"></i> Inventario</a></li>
                <li><a href="${pageContext.request.contextPath}/proveedores"><i class="fa fa-truck"></i> Proveedores</a></li>
                <li><a href="${pageContext.request.contextPath}/categorias"><i class="fa fa-tags"></i> Categorías</a></li>
                <li><a href="${pageContext.request.contextPath}/reportes"><i class="fa fa-chart-bar"></i> Reportes</a></li>
                <li><a href="${pageContext.request.contextPath}/admin"><i class="fa fa-cogs"></i> Administración</a></li>
                <li><a href="${pageContext.request.contextPath}/LogoutServlet"><i class="fa fa-sign-out-alt"></i> Cerrar sesión</a></li>
            </ul>
        </nav>
    </aside>

    <main class="content">
        <header class="header-main">
            <button class="menu-toggle" id="menu-toggle">
                <i class="fa fa-bars"></i>
            </button>
            <c:if test="${not empty usuario}">
                <h1>Bienvenido, ${usuario.nombre}</h1>
            </c:if>
            <c:if test="${empty usuario}">
                <h1>Bienvenido, invitado</h1>
            </c:if>
            <div class="datetime">
                <i class="fa fa-clock"></i> <span id="hora"></span>
                <i class="fa fa-calendar"></i> <span id="fecha"></span>
            </div>
        </header>

        <section class="cards">
            <a href="${pageContext.request.contextPath}/inventario" class="card">
                <i class="fa fa-box"></i>
                <h3>Inventario</h3>
                <p>Ir a inventario ></p>
            </a>
            <a href="${pageContext.request.contextPath}/proveedores" class="card">
                <i class="fa fa-truck"></i>
                <h3>Proveedores</h3>
                <p>Ir a proveedores ></p>
            </a>
            <a href="${pageContext.request.contextPath}/categorias" class="card">
                <i class="fa fa-tags"></i>
                <h3>Categorías</h3>
                <p>Ir a categorías ></p>
            </a>
        </section>

        <section class="alerts">
            <h2>Alertas de Inventario</h2>
            <p>No hay alertas por el momento.</p>
        </section>
    </main>

    <script src="${pageContext.request.contextPath}/js/inicio.js"></script>
</body>
</html>
