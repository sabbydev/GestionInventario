package com.grupo5.gestioninventario.repositorio;

import com.grupo5.gestioninventario.modelo.Proveedor;
import java.util.List;

public interface IRepositorioProveedor extends IRepositorio<Proveedor, Integer> {
    List<Proveedor> buscarPorNombre(String nombre);
    List<Proveedor> search(String q, int offset, int limit);
    long countSearch(String q);
}
