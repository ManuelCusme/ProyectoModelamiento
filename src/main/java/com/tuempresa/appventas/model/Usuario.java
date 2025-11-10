package com.tuempresa.appventas.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cedula;
    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String email;

    private String password;
    private String telefono;
    private String direccion;
    private String genero;
    private String tipoUsuario;
    private String estado;
    private boolean cuentaVerificada = false;

    // ✅ NUEVO: Token de verificación de email
    private String verificationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date tokenExpiracion;

    // Constructor vacío (OBLIGATORIO para JPA)
    public Usuario() {}

    // Constructor con campos básicos
    public Usuario(String cedula, String nombre, String apellido, String email, String password, String tipoUsuario) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.tipoUsuario = tipoUsuario;
        this.estado = "PENDIENTE_VERIFICACION";
        this.cuentaVerificada = false;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean isCuentaVerificada() { return cuentaVerificada; }
    public void setCuentaVerificada(boolean cuentaVerificada) { this.cuentaVerificada = cuentaVerificada; }

    // NUEVO: Getter y Setter para verificationToken
    public String getVerificationToken() { return verificationToken; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }

    public Date getTokenExpiracion() { return tokenExpiracion; }
    public void setTokenExpiracion(Date tokenExpiracion) { this.tokenExpiracion = tokenExpiracion; }
}