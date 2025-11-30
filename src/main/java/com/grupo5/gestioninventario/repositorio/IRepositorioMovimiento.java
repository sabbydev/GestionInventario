package com.grupo5.gestioninventario.repositorio;

import com.grupo5.gestioninventario.modelo.Movimiento;
import com.grupo5.gestioninventario.modelo.TipoMovimiento;
import java.util.List;
import java.util.Map;

public interface IRepositorioMovimiento extends IRepositorio<Movimiento, Integer> {
    long contarEntradas();
    long contarSalidas();
    long contarTotal();
    List<Movimiento> ultimos(int max);
    Map<Integer, Long> contarPorMes(TipoMovimiento tipo, int year);
}
