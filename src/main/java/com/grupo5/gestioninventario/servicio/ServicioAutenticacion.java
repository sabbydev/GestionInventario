package com.grupo5.gestioninventario.servicio;

import com.grupo5.gestioninventario.modelo.Usuario;
import com.grupo5.gestioninventario.repositorio.IRepositorioUsuario;

public class ServicioAutenticacion {

    private final IRepositorioUsuario repositorioUsuario;

    public ServicioAutenticacion(IRepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }
    
    public Usuario autenticar(String correo, String contraseña) {
        Usuario usuario = repositorioUsuario.findByCorreoAndContraseña(correo, contraseña);
        return (usuario != null && usuario.isEstado()) ? usuario : null;
    }
}