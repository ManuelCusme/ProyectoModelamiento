package com.tuempresa.appventas.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String cedula;
    private String email;
    private String password;
    private String genero;
    private String telefono;
    private String direccion;
    private String tipoUsuario; // VENDEDOR, ADMIN
    private String estado; // ACTIVO, INACTIVO, SUSPENDIDO, PENDIENTE_VERIFICACION
    private boolean cuentaVerificada = false;

    // Token de verificación de email
    private String verificationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date tokenExpiracion;

    //NUEVO: Token de recuperación de contraseña
    private String resetPasswordToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date resetPasswordExpiracion;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;

    public Usuario() {
        this.fechaRegistro = new Date();
    }

    // Getters y Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean isCuentaVerificada() { return cuentaVerificada; }
    public void setCuentaVerificada(boolean cuentaVerificada) { this.cuentaVerificada = cuentaVerificada; }

    public String getVerificationToken() { return verificationToken; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }

    public Date getTokenExpiracion() { return tokenExpiracion; }
    public void setTokenExpiracion(Date tokenExpiracion) { this.tokenExpiracion = tokenExpiracion; }

    //NUEVO: Getters y Setters para recuperación de contraseña
    public String getResetPasswordToken() { return resetPasswordToken; }
    public void setResetPasswordToken(String resetPasswordToken) { this.resetPasswordToken = resetPasswordToken; }

    public Date getResetPasswordExpiracion() { return resetPasswordExpiracion; }
    public void setResetPasswordExpiracion(Date resetPasswordExpiracion) { this.resetPasswordExpiracion = resetPasswordExpiracion; }

    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}