package com.grupo5.gestioninventario.modelo;

public class EmpresaInfo {
    private String nombre;
    private String ruc;
    private String direccion;
    private String telefono;
    private String correo;
    private String representante;

    public static EmpresaInfo defaults() {
        EmpresaInfo e = new EmpresaInfo();
        e.setNombre("Autorepuestos Perú SAC");
        e.setRuc("20568472910");
        e.setDireccion("Av. la Paz - Lima - Peru");
        e.setTelefono("(01) 457-8219");
        e.setCorreo("contacto@autorepuestos.pe");
        e.setRepresentante("Autorepuestos Perú SAC");
        return e;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getRepresentante() { return representante; }
    public void setRepresentante(String representante) { this.representante = representante; }
}

