package com.tuempresa.appventas.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    private double precio;

    // 5 URLs de imágenes (hasta 1000 chars cada una)
    @Column(name = "imagen_url1", length = 1000)
    private String imagenUrl1;

    @Column(name = "imagen_url2", length = 1000)
    private String imagenUrl2;

    @Column(name = "imagen_url3", length = 1000)
    private String imagenUrl3;

    @Column(name = "imagen_url4", length = 1000)
    private String imagenUrl4;

    @Column(name = "imagen_url5", length = 1000)
    private String imagenUrl5;

    private String ubicacion;
    private Boolean disponibilidad = true;
    private String tipo; // Electrónicos, Ropa, Hogar, etc.
    private String estado; // ACTIVO, OCULTO, PROHIBIDO, EN_REVISION

    private Integer cantidad; // Stock disponible
    private String estadoProducto; // Nuevo, Semi nuevo, Usado, etc.

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPublicacion;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaExpiracion;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Usuario vendedor;

    // Constructor vacío
    public Producto() {}

    // Constructor con campos básicos
    public Producto(String nombre, String descripcion, double precio, Usuario vendedor) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.vendedor = vendedor;
        this.estado = "ACTIVO";
        this.disponibilidad = true;
        this.fechaPublicacion = new Date();
        this.cantidad = 1; // Por defecto 1
        // Generar código automático
        this.codigo = "PROD-" + System.currentTimeMillis();
    }

    // Getters / Setters (incluyendo las 5 imágenes)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getImagenUrl1() { return imagenUrl1; }
    public void setImagenUrl1(String imagenUrl1) { this.imagenUrl1 = imagenUrl1; }

    public String getImagenUrl2() { return imagenUrl2; }
    public void setImagenUrl2(String imagenUrl2) { this.imagenUrl2 = imagenUrl2; }

    public String getImagenUrl3() { return imagenUrl3; }
    public void setImagenUrl3(String imagenUrl3) { this.imagenUrl3 = imagenUrl3; }

    public String getImagenUrl4() { return imagenUrl4; }
    public void setImagenUrl4(String imagenUrl4) { this.imagenUrl4 = imagenUrl4; }

    public String getImagenUrl5() { return imagenUrl5; }
    public void setImagenUrl5(String imagenUrl5) { this.imagenUrl5 = imagenUrl5; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public Boolean getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(Boolean disponibilidad) { this.disponibilidad = disponibilidad; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getEstadoProducto() { return estadoProducto; }
    public void setEstadoProducto(String estadoProducto) { this.estadoProducto = estadoProducto; }

    public Date getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(Date fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public Date getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(Date fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }

    public Usuario getVendedor() { return vendedor; }
    public void setVendedor(Usuario vendedor) { this.vendedor = vendedor; }
}