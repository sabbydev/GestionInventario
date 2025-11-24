<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Idioma: se toma de ?lang=xx o se deja 'es' por defecto -->
        <c:if test="${not empty param.lang}">
            <c:set var="lang" value="${param.lang}" scope="session"/>
        </c:if>

        <c:if test="${empty sessionScope.lang}">
            <c:set var="lang" value="es" scope="session"/>
        </c:if>

        <fmt:setLocale value="${sessionScope.lang}" scope="session"/>
        <fmt:setBundle basename="messages" var="msg"/>

        <% 
            // Solo UNA vez declaramos estas variables
            String vistaDinamicaJSP = (String) request.getAttribute("vistaDinamica");
            String titulo = "";
            if (vistaDinamicaJSP != null && !vistaDinamicaJSP.isEmpty()) {
                titulo = vistaDinamicaJSP.substring(0, 1).toUpperCase() + vistaDinamicaJSP.substring(1);
            }
        %>

        <!-- Título de la página: texto del bundle + nombre de la vista -->
        <title>
            <fmt:message key="app.title" bundle="${msg}"/> 
            <c:if test="<%= !titulo.isEmpty() %>"> - <%= titulo %></c:if>
        </title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
    </head>
    <body>
        <aside id="sidebar">
            <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>
        </aside>

        <div id="dynamic-content">
            <header id="header">
                <%@ include file="/WEB-INF/jspf/header.jspf" %>
            </header>

            <section id="main">
                <c:if test="${not empty vistaDinamica}">
                    <%@ include file="/WEB-INF/jspf/contenido-dinamico.jspf" %>
                </c:if>
            </section>
        </div>
    </body>
</html>
