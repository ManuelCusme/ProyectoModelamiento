package com.tuempresa.appventas.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.tuempresa.appventas.model.Historial;
import com.tuempresa.appventas.service.HistorialService;
import com.tuempresa.appventas.service.UsuarioService;
import com.tuempresa.appventas.service.ProductoService;
import java.util.List;

@RestController
@RequestMapping("/api/historial")
@CrossOrigin(origins = "*")
public class HistorialController {

    private final HistorialService historialService;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public HistorialController(HistorialService historialService, UsuarioService usuarioService, ProductoService productoService) {
        this.historialService = historialService;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<?> registrarVista(@RequestParam Long usuarioId, @RequestParam Long productoId) {
        try {
            var usuario = usuarioService.obtenerPorId(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            var producto = productoService.obtenerPorId(productoId);

            Historial historial = historialService.registrarVista(usuario, producto);
            return ResponseEntity.ok(historial);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Historial>> obtenerHistorial(@PathVariable Long usuarioId) {
        try {
            List<Historial> historial = historialService.obtenerHistorialUsuario(usuarioId);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}