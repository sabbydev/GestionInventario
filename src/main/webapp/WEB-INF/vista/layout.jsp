<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="${sessionScope.lang}">
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

        <c:if test="${not empty param.layout}">
            <c:set var="layoutVersion" value="${param.layout}" scope="session"/>
        </c:if>
        <c:if test="${empty sessionScope.layoutVersion}">
            <c:set var="layoutVersion" value="v1" scope="session"/>
        </c:if>

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
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <c:choose>
            <c:when test="${vistaDinamica eq 'dashboard'}">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/card.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/alert.css">
            </c:when>
            <c:when test="${vistaDinamica eq 'inventario' || vistaDinamica eq 'producto-form'}">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/inventario.css">
            </c:when>
            <c:when test="${vistaDinamica eq 'proveedores' || vistaDinamica eq 'proveedor-form'}">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/proveedores.css">
            </c:when>
            <c:when test="${vistaDinamica eq 'usuarios' || vistaDinamica eq 'usuario-form'}">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/usuarios.css">
            </c:when>
            <c:when test="${vistaDinamica eq 'movimientos'}">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/movimientos.css">
            </c:when>
            <c:when test="${vistaDinamica eq 'categorias' || vistaDinamica eq 'categoria-form'}">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/categorias.css">
            </c:when>
            <c:when test="${vistaDinamica eq 'empresa'}">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/empresa.css">
            </c:when>
        </c:choose>
    </head>
    <body>
      <c:choose>
        <c:when test="${sessionScope.layoutVersion eq 'v1'}">
          <aside id="sidebar"><%@ include file="/WEB-INF/jspf/sidebar.jspf" %></aside>
          <div id="dynamic-content">
            <header id="header"><%@ include file="/WEB-INF/jspf/header.jspf" %></header>
            <section id="main">
                <%
                  String flashSuccess = (String) session.getAttribute("flashSuccess");
                  String flashError = (String) session.getAttribute("flashError");
                  if (flashSuccess != null) { request.setAttribute("flashSuccess", flashSuccess); session.removeAttribute("flashSuccess"); }
                  if (flashError != null) { request.setAttribute("flashError", flashError); session.removeAttribute("flashError"); }
                %>
                <c:if test="${not empty flashSuccess}">
                    <div class="alert success">${flashSuccess}</div>
                </c:if>
                <c:if test="${not empty flashError}">
                    <div class="alert error">${flashError}</div>
                </c:if>
                <c:if test="${not empty vistaDinamica}">
                    <%@ include file="/WEB-INF/jspf/contenido-dinamico.jspf" %>
                </c:if>
            </section>
          </div>
        </c:when>
        <c:otherwise>
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
        </c:otherwise>
      </c:choose>
    </body>
</html>
