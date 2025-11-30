package com.grupo5.gestioninventario.repositorio;

import com.grupo5.gestioninventario.modelo.Categoria;
import java.util.List;
import java.util.Map;

public interface IRepositorioCategoria extends IRepositorio<Categoria, Integer> {
    Map<Integer, Long> obtenerConteoProductosPorCategoria();
    List<Categoria> buscarPorNombre(String nombre);
}

