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

    public Usuario login(String username, String password) {
        Usuario u = usuarioRepo.findByUsernameAndPassword(username, password);
        // Si tu campo "estado" es boolean:
        if (u != null && u.isEstado()) return u;

        // Si fuera Integer (1/0), usa esta condición en su lugar:
        // if (u != null && u.getEstado() != null && u.getEstado() == 1) return u;

        return null;
    }
}




