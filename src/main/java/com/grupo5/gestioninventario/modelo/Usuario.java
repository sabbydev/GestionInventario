/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Clase que representa la entidad Usuario mapeada a la tabla "usuarios"
 * en la base de datos MySQL mediante JPA / Hibernate.
 */
package com.grupo5.gestioninventario.modelo;

import jakarta.persistence.*;  // ✅ Importa las anotaciones necesarias de JPA

// ✅ Marca la clase como una entidad JPA
@Entity
// ✅ Indica el nombre de la tabla en la base de datos
@Table(name = "usuarios")
public class Usuario {

    // ✅ Clave primaria autogenerada
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // usa AUTO_INCREMENT en MySQL
    @Column(name = "id_usuario") // nombre de la columna en la BD
    private Integer id;  // REVISAR

    // ✅ Campo para el login (correo o usuario)
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username; 
    // 🔹 CAMBIO: antes era "email", ahora es "username" para autenticación

    // ✅ Campo de contraseña (se recomienda cifrar)
    @Column(name = "user_password", nullable = false, length = 255)
    private String password; 
    // 🔹 CAMBIO: el nombre coincide con la BD: "user_password"

    // ✅ Campo booleano que indica si el usuario está activo
    @Column(name = "estado")
    private boolean estado;

    // ✅ Relación muchos-a-uno con la tabla roles (FK id_rol)
    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false) // crea la FK
    private Rol rol; 
    // 🔹 CAMBIO: antes no existía la relación, ahora se añade para manejar roles

    // 🔹 Constructor vacío obligatorio para JPA
    public Usuario() {}

    // ✅ Getters y Setters: necesarios para acceder a los campos desde Hibernate y controladores

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}
