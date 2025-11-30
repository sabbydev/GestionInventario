package com.grupo5.gestioninventario.repositorio;

import com.grupo5.gestioninventario.modelo.Producto;
import java.util.List;

public interface IRepositorioProducto extends IRepositorio<Producto, Integer> {
    List<Producto> buscarPorNombre(String nombre);
    List<Producto> search(String q, int offset, int limit);
    long countSearch(String q);
}
