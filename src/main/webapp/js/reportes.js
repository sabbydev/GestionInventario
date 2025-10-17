// === MOVIMIENTOS POR CATEGORÍA ===
const barChart = new Chart(document.getElementById("barChart"), {
    type: "bar",
    data: {
        labels: ["Lubricantes", "Eléctrico", "Herramientas", "Suspensión", "Motor", "Accesorios"],
        datasets: [
            {
                label: "Entradas",
                data: [80, 60, 40, 20, 55, 35],
                backgroundColor: "#007bff"
            },
            {
                label: "Salidas",
                data: [40, 30, 20, 10, 25, 15],
                backgroundColor: "#ffc107"
            },
            {
                label: "Devoluciones",
                data: [15, 10, 5, 3, 8, 4],
                backgroundColor: "#28a745"
            }
        ]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { position: "bottom" } },
        scales: {
            x: {
                grid: { display: false },
                barThickness: 25,          // 🔹 más delgado
                categoryPercentage: 0.55,  // 🔹 menos separación entre grupos
                barPercentage: 0.6         // 🔹 proporción del ancho
            },
            y: { beginAtZero: true }
        },
        layout: {
            padding: { top: 10, bottom: 10 }
        }
    }
});

// === DISTRIBUCIÓN DE INVENTARIO (PIE) ===
const pieChart = new Chart(document.getElementById("pieChart"), {
    type: "pie",
    data: {
        labels: ["Lubricantes", "Eléctrico", "Herramientas", "Suspensión", "Motor", "Accesorios"],
        datasets: [{
            data: [30, 20, 15, 10, 15, 10],
            backgroundColor: ["#007bff", "#28a745", "#ffc107", "#dc3545", "#17a2b8", "#6f42c1"]
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                position: "bottom"
            }
        }
    }
});

// === PRODUCTOS CON BAJO STOCK ===
const stockChart = new Chart(document.getElementById("stockChart"), {
    type: "bar",
    data: {
        labels: ["Amortiguador", "Llave inglesa", "Filtro aceite", "Pastillas freno", "Bujías", "Correa alternador"],
        datasets: [{
            label: "Stock disponible",
            data: [8, 12, 15, 9, 10, 11],
            backgroundColor: "#dc3545"
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: {
            x: {
                grid: { display: false },
                barThickness: 25,         // 🔹 más delgadas
                categoryPercentage: 0.5,  // 🔹 más espacio entre grupos
                barPercentage: 0.6
            },
            y: { beginAtZero: true }
        },
        layout: {
            padding: { top: 10, bottom: 10 }
        }
    }
});
