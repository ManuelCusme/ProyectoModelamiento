package com.tuempresa.appventas.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.tuempresa.appventas.model.Reporte;
import com.tuempresa.appventas.service.ReporteService;
import com.tuempresa.appventas.service.UsuarioService;
import com.tuempresa.appventas.service.ProductoService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    private final ReporteService reporteService;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public ReporteController(ReporteService reporteService, UsuarioService usuarioService, ProductoService productoService) {
        this.reporteService = reporteService;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<?> crearReporte(@RequestBody Map<String, Object> request) {
        try {
            Long usuarioId = Long.parseLong(request.get("usuarioId").toString());
            Long productoId = Long.parseLong(request.get("productoId").toString());
            String motivo = request.get("motivo").toString();
            String descripcion = request.get("descripcion").toString();

            var usuario = usuarioService.obtenerPorId(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            var producto = productoService.obtenerPorId(productoId);

            Reporte reporte = reporteService.crearReporte(usuario, producto, motivo, descripcion);
            return ResponseEntity.ok(reporte);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<Reporte>> obtenerReportesPendientes() {
        try {
            List<Reporte> reportes = reporteService.obtenerReportesPendientes();
            return ResponseEntity.ok(reportes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}