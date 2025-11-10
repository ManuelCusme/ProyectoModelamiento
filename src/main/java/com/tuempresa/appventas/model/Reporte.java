package com.tuempresa.appventas.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; // Quien reporta

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private String motivo; // SPAM, CONTENIDO_INAPROPIADO, FRAUDE, PRECIO_INCORRECTO, OTRO

    @Column(length = 1000)
    private String descripcion;

    private String estado; // PENDIENTE, REVISADO, RESUELTO

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReporte;

    public Reporte() {}

    public Reporte(Usuario usuario, Producto producto, String motivo, String descripcion) {
        this.usuario = usuario;
        this.producto = producto;
        this.motivo = motivo;
        this.descripcion = descripcion;
        this.estado = "PENDIENTE";
        this.fechaReporte = new Date();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Date getFechaReporte() { return fechaReporte; }
    public void setFechaReporte(Date fechaReporte) { this.fechaReporte = fechaReporte; }
}