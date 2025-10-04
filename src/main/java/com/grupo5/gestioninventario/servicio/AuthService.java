/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.grupo5.gestioninventario.servicio;

import com.grupo5.gestioninventario.modelo.Usuario;
import com.grupo5.gestioninventario.repositorio.UsuarioRepository;

public class AuthService {

    private final UsuarioRepository usuarioRepo;

    public AuthService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    // ahora el login es por correo
    public Usuario login(String correo, String password) {
        Usuario u = usuarioRepo.findByCorreoAndPassword(correo, password);
        if (u != null && u.isEstado()) return u;  // activo = true
        return null;
    }
}





