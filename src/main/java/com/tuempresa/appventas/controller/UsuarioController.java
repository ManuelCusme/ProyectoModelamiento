package com.tuempresa.appventas.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.tuempresa.appventas.model.Usuario;
import com.tuempresa.appventas.service.UsuarioService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // OBTENER TODOS LOS USUARIOS
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.obtenerTodos();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // OBTENER USUARIO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Long id) {
        try {
            Optional<Usuario> usuario = usuarioService.obtenerPorId(id);
            return usuario.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ACTUALIZAR USUARIO
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id,
                                               @RequestBody Usuario usuarioActualizado) {
        try {
            Usuario usuario = usuarioService.actualizarUsuario(id, usuarioActualizado);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // CAMBIAR ESTADO (para moderadores)
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id,
                                           @RequestParam String nuevoEstado) {
        try {
            Usuario usuario = usuarioService.cambiarEstadoUsuario(id, nuevoEstado);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // VERIFICAR CUENTA
    @PutMapping("/{id}/verificar")
    public ResponseEntity<?> verificarCuenta(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.verificarCuenta(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}