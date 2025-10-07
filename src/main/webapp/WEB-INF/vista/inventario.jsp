<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventario | Autorepuestos Perú</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/inventario.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>

<body>
    <aside class="sidebar">
        <h2>Autorepuestos</h2>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/dashboard"><i class="fa fa-home"></i> Dashboard</a></li>   
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
        
         <!-- Barra de búsqueda -->
        <section class="search-bar">
            <form action="${pageContext.request.contextPath}/inventario" method="get">
                <input type="text" name="query" placeholder="Buscar producto..." value="${param.query}" />
                <button type="submit"><i class="fa fa-search"></i> Buscar</button>
            </form>
        </section>

        <!-- Tabla de inventario -->
        <section class="inventory-table">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Categoría</th>
                        <th>Proveedor</th>
                        <th>Stock</th>
                        <th>Precio</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="producto" items="${productos}">
                        <tr>
                            <td>${producto.id}</td>
                            <td>${producto.nombre}</td>
                            <td>${producto.categoria.nombre}</td>
                            <td>${producto.proveedor.nombre}</td>
                            <td>${producto.stock}</td>
                            <td>S/. ${producto.precio}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/editarProducto?id=${producto.id}" class="edit"><i class="fa fa-edit"></i></a>
                                <a href="${pageContext.request.contextPath}/eliminarProducto?id=${producto.id}" class="delete"><i class="fa fa-trash"></i></a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty productos}">
                        <tr>
                            <td colspan="7">No se encontraron productos.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </section>
    </main>

    <script src="${pageContext.request.contextPath}/js/fechaHora.js"></script>
</body>
</html>
