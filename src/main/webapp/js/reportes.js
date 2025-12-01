// === MOVIMIENTOS POR CATEGORÍA ===
const barChart = new Chart(document.getElementById("barChart"), {
    type: "bar",
    data: {
        labels: window.REPORTES_DATA.labelsCategorias,
        datasets: [
            {
                label: "Entradas",
                data: window.REPORTES_DATA.entradas,
                backgroundColor: "#007bff"
            },
            {
                label: "Salidas",
                data: window.REPORTES_DATA.salidas,
                backgroundColor: "#ffc107"
            },
            {
                label: "Transferencias",
                data: window.REPORTES_DATA.transferencias,
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
        labels: window.REPORTES_DATA.labelsCategorias,
        datasets: [{
            data: window.REPORTES_DATA.stockPorCategoria,
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
        labels: window.REPORTES_DATA.bajoStockLabels,
        datasets: [{
            label: "Stock disponible",
            data: window.REPORTES_DATA.bajoStockValores,
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

(function(){
  const actualizarBtn = document.getElementById('btnActualizar');
  const exportarBtn = document.getElementById('btnExportar');
  const fechaInicio = document.getElementById('fechaInicio');
  const fechaFin = document.getElementById('fechaFin');
  const tipoSel = document.getElementById('tipoMovimiento');
  function applyTipo(){
    const v = tipoSel ? tipoSel.value : 'todos';
    barChart.data.datasets.forEach(ds => ds.hidden = false);
    if (v === 'entrada') {
      barChart.data.datasets[1].hidden = true;
      barChart.data.datasets[2].hidden = true;
    } else if (v === 'salida') {
      barChart.data.datasets[0].hidden = true;
      barChart.data.datasets[2].hidden = true;
    } else if (v === 'transferencia') {
      barChart.data.datasets[0].hidden = true;
      barChart.data.datasets[1].hidden = true;
    }
    barChart.update();
  }
  if (tipoSel) tipoSel.addEventListener('change', applyTipo);
  applyTipo();
  if (actualizarBtn) {
    actualizarBtn.addEventListener('click', function(){
      const params = new URLSearchParams();
      if (fechaInicio && fechaInicio.value) params.set('fechaInicio', fechaInicio.value);
      if (fechaFin && fechaFin.value) params.set('fechaFin', fechaFin.value);
      const url = window.location.pathname + (params.toString() ? ('?' + params.toString()) : '');
      window.location.assign(url);
    });
  }
  if (exportarBtn) {
    exportarBtn.addEventListener('click', function(){
      const params = new URLSearchParams();
      if (fechaInicio && fechaInicio.value) params.set('fechaInicio', fechaInicio.value);
      if (fechaFin && fechaFin.value) params.set('fechaFin', fechaFin.value);
      const base = window.location.pathname.endsWith('/reportes') ? window.location.pathname : (window.location.pathname.replace(/\/?$/, '') + '/reportes');
      const url = base + '/exportar' + (params.toString() ? ('?' + params.toString()) : '');
      window.location.assign(url);
    });
  }
})();
