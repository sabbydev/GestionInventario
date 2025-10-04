/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.grupo5.gestioninventario.repositorio;

import com.grupo5.gestioninventario.modelo.Usuario;

public interface UsuarioRepository extends IRepository<Usuario, Integer> {
    // ahora autenticamos por correo
    Usuario findByCorreoAndPassword(String correo, String password);
}
