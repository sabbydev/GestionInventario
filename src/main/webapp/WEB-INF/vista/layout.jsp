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
      <div id="alertModal" class="modal" style="display:none;">
        <div class="modal-content">
          <button type="button" class="modal-close">×</button>
          <div class="modal-body">
            <h3 id="alertTitle">Aviso</h3>
            <ul id="alertList" class="modal-list"></ul>
          </div>
        </div>
      </div>
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
                <c:if test="${not empty flashSuccess}"><div class="alert success" style="display:none;">${flashSuccess}</div></c:if>
                <c:if test="${not empty flashError}"><div class="alert error" style="display:none;">${flashError}</div></c:if>
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
      <script>
        (function(){
          var modal = document.getElementById('alertModal');
          var list = document.getElementById('alertList');
          var title = document.getElementById('alertTitle');
          var closeBtn = modal ? modal.querySelector('.modal-close') : null;
          function open(items){
            while(list.firstChild) list.removeChild(list.firstChild);
            items.forEach(function(it){
              var li = document.createElement('li');
              li.textContent = it.text;
              li.className = it.type ? ('item ' + it.type) : 'item';
              list.appendChild(li);
            });
            modal.style.display = 'flex';
          }
          function close(){ modal.style.display = 'none'; }
          document.addEventListener('click', function(e){ if (modal && e.target === modal) close(); });
          if (closeBtn) closeBtn.addEventListener('click', close);
          document.addEventListener('DOMContentLoaded', function(){
            var alerts = Array.prototype.slice.call(document.querySelectorAll('#main .alert'));
            var items = alerts.map(function(a){
              var t = a.classList.contains('error') ? 'error' : (a.classList.contains('success') ? 'success' : 'info');
              return { type: t, text: a.textContent.trim() };
            }).filter(function(it){ return it.text.length > 0; });
            alerts.forEach(function(a){ a.parentNode && a.parentNode.removeChild(a); });
            if (items.length > 0) open(items);
          });
        })();
      </script>
    </body>
</html>
