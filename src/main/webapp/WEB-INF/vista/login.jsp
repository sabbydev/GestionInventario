<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | Autorepuestos Perú</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
    <div class="login-container">
        <div class="login-card">
            <div class="logo-section">
                <img src="${pageContext.request.contextPath}/img/logo.png" alt="Logo Autorepuestos Perú">
                <h2>Autorepuestos Perú</h2>
                <p>Gestión de inventario rápido y seguro</p>
            </div>

            <!-- Formulario de login -->
            <form action="${pageContext.request.contextPath}/login" method="post" class="login-form">
                <label for="correo">Correo electrónico</label>
                <input type="email" id="correo" name="correo" placeholder="ejemplo@correo.com" required>

                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password" placeholder="Ingrese su contraseña" required>

                <button type="submit">Ingresar</button>
            </form>

            <!-- Mensajes de error dinámicos con JSTL -->
            <c:choose>
                <c:when test="${param.error == 'true'}">
                    <p style="color:red; text-align:center; margin-top:10px;">
                        ❌ Usuario o contraseña incorrectos
                    </p>
                </c:when>
                <c:when test="${param.error == 'sesion'}">
                    <p style="color:red; text-align:center; margin-top:10px;">
                        ❌ Debes iniciar sesión para acceder
                    </p>
                </c:when>
            </c:choose>
        </div>
    </div>
</body>
</html>