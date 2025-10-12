/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
function actualizarFechaHora() {
    const fecha = new Date();
    document.getElementById("fecha").textContent = fecha.toLocaleDateString("es-PE", {
        day: "2-digit", month: "2-digit", year: "numeric"
    });
    document.getElementById("hora").textContent = fecha.toLocaleTimeString("es-PE");
}

// Actualiza la hora cada segundo
setInterval(actualizarFechaHora, 1000);
actualizarFechaHora();

// Toggle del menú lateral
const menuToggleBtn = document.getElementById("menu-toggle");
const sidebar = document.querySelector(".sidebar");

if (menuToggleBtn && sidebar) {
    menuToggleBtn.addEventListener("click", function () {
        sidebar.classList.toggle("active");
    });
}

