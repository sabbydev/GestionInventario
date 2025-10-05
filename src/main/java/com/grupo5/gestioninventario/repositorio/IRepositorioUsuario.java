package com.grupo5.gestioninventario.repositorio;

import com.grupo5.gestioninventario.modelo.Usuario;

public interface IRepositorioUsuario extends IRepositorio<Usuario, Integer> {
    // Contrato para cualquier implementación de conexión con base de datos relacionada con la entidad Usuario
    Usuario findByCorreoAndContraseña(String correo, String contraseña);
}