<%-- 
    Document   : layout
    Created on : 11 oct. 2025, 21:48:38
    Author     : Sebastian
--%>

<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <%
        String vistaDinamicaJSP = (String) request.getAttribute("vistaDinamica");
        String titulo = "";
        if (vistaDinamicaJSP != null && !vistaDinamicaJSP.isEmpty()) {
            titulo = vistaDinamicaJSP.substring(0, 1).toUpperCase() + vistaDinamicaJSP.substring(1);
        }
      %>
      <title><%= titulo %> | Autorepuestos Perú</title>
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
    </head>
    <body>
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
    </body>
</html>
