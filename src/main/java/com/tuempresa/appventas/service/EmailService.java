package com.tuempresa.appventas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.url}")
    private String appUrl;

    public void enviarEmailVerificacion(String emailDestino, String nombreUsuario, String token) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(fromEmail);
            mensaje.setTo(emailDestino);
            mensaje.setSubject("✅ Verifica tu cuenta - Sistema de Ventas");

            String cuerpoMensaje = String.format(
                    "Hola %s,\n\n" +
                            "¡Gracias por registrarte!\n\n" +
                            "Para verificar tu cuenta, haz clic aquí:\n\n" +
                            "%s/verificar?token=%s\n\n" +
                            "⚠️ Este enlace expira en 15 minutos.\n\n" +
                            "Saludos,\n" +
                            "Equipo de Sistema de Ventas",
                    nombreUsuario,
                    appUrl,
                    token
            );

            mensaje.setText(cuerpoMensaje);
            mailSender.send(mensaje);

            System.out.println("Email enviado a: " + emailDestino);

        } catch (Exception e) {
            System.err.println("Error al enviar email: " + e.getMessage());
            throw new RuntimeException("No se pudo enviar el email de verificación");
        }
    }

    public void enviarEmailBienvenida(String emailDestino, String nombreUsuario) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(fromEmail);
            mensaje.setTo(emailDestino);
            mensaje.setSubject("🎉 ¡Cuenta verificada!");

            String cuerpoMensaje = String.format(
                    "Hola %s,\n\n" +
                            "¡Tu cuenta ha sido verificada! \n\n" +
                            "Ya puedes iniciar sesión.\n\n" +
                            "Saludos,\n" +
                            "Equipo de Sistema de Ventas",
                    nombreUsuario
            );

            mensaje.setText(cuerpoMensaje);
            mailSender.send(mensaje);

        } catch (Exception e) {
            System.err.println("⚠️ Email de bienvenida no enviado");
        }
    }

    // NUEVO: Enviar código de recuperación de contraseña
    public void enviarCodigoRecuperacion(String emailDestino, String nombreUsuario, String codigo) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(fromEmail);
            mensaje.setTo(emailDestino);
            mensaje.setSubject("🔒 Código de recuperación de contraseña");

            String cuerpoMensaje = String.format(
                    "Hola %s,\n\n" +
                            "Recibimos una solicitud para restablecer tu contraseña.\n\n" +
                            "Tu código de verificación es:\n\n" +
                            "🔑 %s\n\n" +
                            "Este código expirará en 15 minutos.\n\n" +
                            "Si no solicitaste este cambio, ignora este mensaje.\n\n" +
                            "Saludos,\n" +
                            "Equipo de Sistema de Ventas",
                    nombreUsuario,
                    codigo
            );

            mensaje.setText(cuerpoMensaje);
            mailSender.send(mensaje);

            System.out.println(" Código de recuperación enviado a: " + emailDestino);

        } catch (Exception e) {
            System.err.println("❌ Error al enviar código: " + e.getMessage());
            throw new RuntimeException("No se pudo enviar el código de recuperación");
        }
    }

    //NUEVO: Confirmar cambio de contraseña
    public void enviarConfirmacionCambioPassword(String emailDestino, String nombreUsuario) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(fromEmail);
            mensaje.setTo(emailDestino);
            mensaje.setSubject("✅ Contraseña actualizada");

            String cuerpoMensaje = String.format(
                    "Hola %s,\n\n" +
                            "Tu contraseña ha sido actualizada exitosamente.\n\n" +
                            "Si no realizaste este cambio, contacta inmediatamente con soporte.\n\n" +
                            "Saludos,\n" +
                            "Equipo de Sistema de Ventas",
                    nombreUsuario
            );

            mensaje.setText(cuerpoMensaje);
            mailSender.send(mensaje);

        } catch (Exception e) {
            System.err.println("Email de confirmación no enviado");
        }
    }
}