<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <!-- Manejo de idioma en login -->
    <c:if test="${not empty param.lang}">
        <c:set var="lang" value="${param.lang}" scope="session"/>
    </c:if>

    <c:if test="${empty sessionScope.lang}">
        <c:set var="lang" value="es" scope="session"/>
    </c:if>

    <fmt:setLocale value="${sessionScope.lang}" scope="session"/>
    <fmt:setBundle basename="messages" var="msg"/>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><fmt:message key="login.title" bundle="${msg}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css?v=2">
</head>
<body>
    <div class="login-container">
        <div class="login-card">
            <div class="logo-section">
                <img src="${pageContext.request.contextPath}/img/logo.png" alt="Logo Autorepuestos Perú">
                <h2><fmt:message key="app.title" bundle="${msg}"/></h2>
                <p><fmt:message key="login.subtitle" bundle="${msg}"/></p>
            </div>

            <!-- Formulario de login -->
            <form action="${pageContext.request.contextPath}/login" method="post" class="login-form">
                <label for="correo">
                    <fmt:message key="login.email.label" bundle="${msg}"/>
                </label>
                <input type="email" id="correo" name="correo" placeholder="ejemplo@correo.com" required>

                <label for="password">
                    <fmt:message key="login.password.label" bundle="${msg}"/>
                </label>
                <input type="password" id="password" name="password" placeholder="********" required>

                <button type="submit">
                    <fmt:message key="login.button" bundle="${msg}"/>
                </button>
            </form>

            <!-- Selector de idioma con banderas -->
            <div class="language-switcher">
                <a href="?lang=es" title="<fmt:message key='lang.es' bundle='${msg}'/>">
                    <img src="${pageContext.request.contextPath}/img/flags/es.png" alt="ES" class="flag-icon">
                </a>
                <a href="?lang=en" title="<fmt:message key='lang.en' bundle='${msg}'/>">
                    <img src="${pageContext.request.contextPath}/img/flags/en.png" alt="EN" class="flag-icon">
                </a>
                <a href="?lang=de" title="<fmt:message key='lang.de' bundle='${msg}'/>">
                    <img src="${pageContext.request.contextPath}/img/flags/de.png" alt="DE" class="flag-icon">
                </a>
                <a href="?lang=fr" title="<fmt:message key='lang.fr' bundle='${msg}'/>">
                    <img src="${pageContext.request.contextPath}/img/flags/fr.png" alt="FR" class="flag-icon">
                </a>
            </div>

            <!-- Mensajes de error dinámicos con JSTL -->
            <c:choose>
                <c:when test="${param.error == 'true'}">
                    <p style="color:red; text-align:center; margin-top:10px;">
                        <fmt:message key="login.error.badCredentials" bundle="${msg}"/>
                    </p>
                </c:when>
                <c:when test="${param.error == 'sesion'}">
                    <p style="color:red; text-align:center; margin-top:10px;">
                        <fmt:message key="login.error.session" bundle="${msg}"/>
                    </p>
                </c:when>
            </c:choose>
        </div>
    </div>
</body>
</html>

