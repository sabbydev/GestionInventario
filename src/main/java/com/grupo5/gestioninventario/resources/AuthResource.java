/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo5.gestioninventario.resources;

import com.grupo5.gestioninventario.modelo.Usuario;
import com.grupo5.gestioninventario.repositorio.jpa.JpaUsuarioRepository;
import com.grupo5.gestioninventario.servicio.AuthService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final AuthService authService;

    public AuthResource() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("invPU");
        this.authService = new AuthService(new JpaUsuarioRepository(emf));
    }

    public static class Credenciales {
        public String correo;    // ← ahora correo
        public String password;
    }
    public static class ErrorMsg {
        public String mensaje;
        public ErrorMsg() {}
        public ErrorMsg(String m){ this.mensaje = m; }
    }
    public static class UserView {
        public Integer id;
        public String nombre;
        public String correo;
        public String rol;
        public boolean estado;

        public UserView(Usuario u){
            this.id = u.getId();
            this.nombre = u.getNombre();   // ← mostramos nombre
            this.correo = u.getCorreo();   // ← y correo
            this.rol = (u.getRol()!=null ? u.getRol().getNombre() : null);
            this.estado = u.isEstado();
        }
    }

    @POST @Path("/login")
    public Response login(Credenciales c){
        if (c==null || c.correo==null || c.password==null) {
            return Response.status(400).entity(new ErrorMsg("correo y password son obligatorios")).build();
        }
        Usuario u = authService.login(c.correo, c.password);
        if (u == null) {
            return Response.status(401).entity(new ErrorMsg("Usuario o contraseña incorrectos")).build();
        }
        return Response.ok(new UserView(u)).build();
    }
}



