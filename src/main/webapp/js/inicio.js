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