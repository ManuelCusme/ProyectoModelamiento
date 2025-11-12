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

    //ACTUALIZADO: 5 URLs de imágenes
    @Column(length = 1000)
    private String imagenUrl1;

    @Column(length = 1000)
    private String imagenUrl2;

    @Column(length = 1000)
    private String imagenUrl3;

    @Column(length = 1000)
    private String imagenUrl4;

    @Column(length = 1000)
    private String imagenUrl5;

    private String ubicacion;
    private Boolean disponibilidad = true;
    private String tipo;
    private String estado;

    private Integer cantidad;
    private String estadoProducto;

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
        this.cantidad = 1;
        this.codigo = "PROD-" + System.currentTimeMillis();
    }

    // Getters y Setters
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

    //NUEVO: Getters y Setters para las 5 imágenes
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