package com.tuempresa.appventas.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.tuempresa.appventas.model.Usuario;
import com.tuempresa.appventas.service.UsuarioService;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // REGISTRO
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Usuario usuario = usuarioService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    // ✅ NUEVO: Verificar email con token
    @GetMapping("/verificar-email")
    public ResponseEntity<?> verificarEmail(@RequestParam String token) {
        try {
            Usuario usuario = usuarioService.verificarEmail(token);
            return ResponseEntity.ok("Email verificado correctamente. Tu cuenta está activa.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // CLASE AUXILIAR PARA LOGIN
    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // Reenviar email de verificacion
    @PostMapping("/reenviar-verificacion")
    public ResponseEntity<?> reenviarVerificacion(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            usuarioService.reenviarEmailVerificacion(email);
            return ResponseEntity.ok("Email de verificación reenviado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //NUEVO: Solicitar recuperación de contraseña
    @PostMapping("/recuperar-password")
    public ResponseEntity<?> recuperarPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            usuarioService.solicitarRecuperacionPassword(email);
            return ResponseEntity.ok("Código enviado a tu email");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //NUEVO: Verificar código de recuperación
    @PostMapping("/verificar-codigo")
    public ResponseEntity<?> verificarCodigo(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String codigo = request.get("codigo");
            boolean valido = usuarioService.verificarCodigoRecuperacion(email, codigo);

            if (valido) {
                return ResponseEntity.ok("Código válido");
            } else {
                return ResponseEntity.badRequest().body("Código inválido o expirado");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al verificar código");
        }
    }

    //NUEVO: Restablecer contraseña
    @PostMapping("/restablecer-password")
    public ResponseEntity<?> restablecerPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String codigo = request.get("codigo");
            String nuevaPassword = request.get("nuevaPassword");

            usuarioService.restablecerPassword(email, codigo, nuevaPassword);
            return ResponseEntity.ok("Contraseña actualizada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
