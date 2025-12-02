package com.grupo5.gestioninventario.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "imagen_data")
    private byte[] imagenData;

    @Column(name = "imagen_mime", length = 100)
    private String imagenMime;

    @Column(name = "ficha_tecnica_url")
    private String fichaTecnicaUrl;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ficha_tecnica_data")
    private byte[] fichaTecnicaData;

    @Column(name = "ficha_tecnica_mime", length = 100)
    private String fichaTecnicaMime;

    @Column(name = "manual_url")
    private String manualUrl;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "manual_data")
    private byte[] manualData;

    @Column(name = "manual_mime", length = 100)
    private String manualMime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public byte[] getImagenData() { return imagenData; }
    public void setImagenData(byte[] imagenData) { this.imagenData = imagenData; }
    public String getImagenMime() { return imagenMime; }
    public void setImagenMime(String imagenMime) { this.imagenMime = imagenMime; }
    public String getFichaTecnicaUrl() { return fichaTecnicaUrl; }
    public void setFichaTecnicaUrl(String fichaTecnicaUrl) { this.fichaTecnicaUrl = fichaTecnicaUrl; }
    public byte[] getFichaTecnicaData() { return fichaTecnicaData; }
    public void setFichaTecnicaData(byte[] fichaTecnicaData) { this.fichaTecnicaData = fichaTecnicaData; }
    public String getFichaTecnicaMime() { return fichaTecnicaMime; }
    public void setFichaTecnicaMime(String fichaTecnicaMime) { this.fichaTecnicaMime = fichaTecnicaMime; }
    public String getManualUrl() { return manualUrl; }
    public void setManualUrl(String manualUrl) { this.manualUrl = manualUrl; }
    public byte[] getManualData() { return manualData; }
    public void setManualData(byte[] manualData) { this.manualData = manualData; }
    public String getManualMime() { return manualMime; }
    public void setManualMime(String manualMime) { this.manualMime = manualMime; }
}
