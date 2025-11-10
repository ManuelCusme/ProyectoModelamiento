package com.tuempresa.appventas.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "favoritos")
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAgregado;

    public Favorito() {}

    public Favorito(Usuario usuario, Producto producto) {
        this.usuario = usuario;
        this.producto = producto;
        this.fechaAgregado = new Date();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Date getFechaAgregado() { return fechaAgregado; }
    public void setFechaAgregado(Date fechaAgregado) { this.fechaAgregado = fechaAgregado; }
}