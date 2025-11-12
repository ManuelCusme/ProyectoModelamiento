package com.tuempresa.appventas.controller;

import com.tuempresa.appventas.model.Producto;
import com.tuempresa.appventas.model.Usuario;
import com.tuempresa.appventas.service.ProductoService;
import com.tuempresa.appventas.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Controller de productos.
 * - DELETE /api/productos/{id} -> BORRADO DEFINITIVO (elimina reportes e historial primero).
 * - POST /api/productos/{id}/ocultar -> cambia estado a OCULTO (no borra físicamente).
 *
 * NOTAS:
 * - Asegúrate de que ProductoService.eliminarProductoDefinitivo sea @Transactional y borre reportes/historial antes del producto.
 * - Si usas autenticación (JWT/Security), reemplaza los TODOs por checks usando el principal/claims para validar permisos.
 */
@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;
    private final UsuarioService usuarioService; // opcional: para obtener usuario actual / validar permisos

    public ProductoController(ProductoService productoService, UsuarioService usuarioService) {
        this.productoService = productoService;
        this.usuarioService = usuarioService;
    }

    // Listar productos (puedes extender con paginación/filtros por query params)
    @GetMapping
    public ResponseEntity<List<Producto>> listar(@RequestParam(required = false) String estado) {
        if (estado != null && !estado.isBlank()) {
            // Si quieres filtrar por estado añade método al servicio/repo
            // Por ahora devolvemos todos activos si no se pide estado
        }
        return ResponseEntity.ok(productoService.obtenerProductosActivos());
    }

    // Obtener producto por id
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtener(@PathVariable Long id) {
        Producto p = productoService.obtenerPorId(id);
        return ResponseEntity.ok(p);
    }

    // Crear producto
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Producto producto) {
        try {
            // Se espera que cliente envíe vendedor.id en el body o que asignes vendedor desde el token.
            Usuario vendedor = producto.getVendedor();
            if (vendedor == null || vendedor.getId() == null) {
                // Si no viene vendedor, intentar obtener usuario actual desde servicio de autenticación
                // TODO: obtener usuario actual desde token si tu app lo soporta
                return ResponseEntity.badRequest().body("El vendedor (vendedor.id) es obligatorio");
            }
            Producto creado = productoService.crearProducto(producto, vendedor);
            return ResponseEntity.ok(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            // Validación de permisos: solo dueño o admin
            // TODO: validar que el usuario que hace la petición sea producto.vendedor.id o tenga rol ADMIN
            Producto actualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cambiar estado (ej. OCULTO) — endpoint aparte si prefieres no usar DELETE para ocultar
    @PostMapping("/{id}/ocultar")
    public ResponseEntity<?> ocultar(@PathVariable Long id) {
        try {
            Producto oculto = productoService.eliminarProducto(id); // cambia estado a OCULTO
            return ResponseEntity.ok(oculto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE definitivo (físico). Borrar reportes e historial antes de borrar producto.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDefinitivo(@PathVariable Long id) {
        try {
            // Opcional: validar permiso del usuario que solicita el borrado
            // TODO: validar propietario o ADMIN aquí

            productoService.eliminarProductoDefinitivo(id);
            return ResponseEntity.ok(Map.of("message", "Producto eliminado definitivamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", "No se pudo eliminar producto", "details", e.getMessage()));
        }
    }

    // Obtener productos por vendedor
    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<Producto>> porVendedor(@PathVariable Long vendedorId) {
        return ResponseEntity.ok(productoService.obtenerProductosPorVendedor(vendedorId));
    }

    // Búsqueda por nombre (ejemplo)
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscar(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }
}