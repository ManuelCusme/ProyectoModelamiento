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

            System.out.println("✅ Email enviado a: " + emailDestino);

        } catch (Exception e) {
            System.err.println("❌ Error al enviar email: " + e.getMessage());
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
                            "¡Tu cuenta ha sido verificada! ✅\n\n" +
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
}