package com.tuempresa.appventas.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.tuempresa.appventas.model.Producto;
import com.tuempresa.appventas.service.ProductoService;
import com.tuempresa.appventas.service.UsuarioService;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;
    private final UsuarioService usuarioService;

    public ProductoController(ProductoService productoService, UsuarioService usuarioService) {
        this.productoService = productoService;
        this.usuarioService = usuarioService;
    }

    // OBTENER PRODUCTOS ACTIVOS
    @GetMapping
    public ResponseEntity<List<Producto>> listarProductosActivos() {
        try {
            List<Producto> productos = productoService.obtenerProductosActivos();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // CREAR PRODUCTO
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        try {
            if (producto.getVendedor() == null || producto.getVendedor().getId() == null) {
                return ResponseEntity.badRequest().body("El vendedorId es obligatorio");
            }

            Long vendedorId = producto.getVendedor().getId();
            var vendedor = usuarioService.obtenerPorId(vendedorId)
                    .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

            Producto nuevoProducto = productoService.crearProducto(producto, vendedor);
            return ResponseEntity.ok(nuevoProducto);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // OBTENER PRODUCTO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProducto(@PathVariable Long id) {
        try {
            Producto producto = productoService.obtenerPorId(id);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // OBTENER PRODUCTOS POR VENDEDOR
    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<Producto>> obtenerPorVendedor(@PathVariable Long vendedorId) {
        try {
            List<Producto> productos = productoService.obtenerProductosPorVendedor(vendedorId);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ⭐ ACTUALIZAR PRODUCTO (CON VALIDACIÓN DE PROPIEDAD)
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Producto productoActualizado,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Obtener el producto actual
            Producto productoActual = productoService.obtenerPorId(id);

            // Validar que el usuario es el dueño
            Long vendedorIdDelProducto = productoActual.getVendedor().getId();
            Long vendedorIdEnviado = productoActualizado.getVendedor() != null ?
                    productoActualizado.getVendedor().getId() : null;

            if (vendedorIdEnviado == null) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "ID de vendedor no proporcionado");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            if (!vendedorIdDelProducto.equals(vendedorIdEnviado)) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "No tienes permiso para editar este producto");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }

            // Actualizar producto
            Producto producto = productoService.actualizarProducto(id, productoActualizado);
            return ResponseEntity.ok(producto);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ⭐ NUEVO: ELIMINAR PRODUCTO (físicamente)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            // Verificar que el producto existe
            Producto producto = productoService.obtenerPorId(id);

            // Eliminar definitivamente
            productoService.eliminarProductoDefinitivo(id);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Producto eliminado correctamente");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // FILTRAR POR PRECIO
    @GetMapping("/filtros/precio")
    public ResponseEntity<List<Producto>> filtrarPorPrecio(
            @RequestParam Double min,
            @RequestParam Double max) {
        try {
            List<Producto> productos = productoService.filtrarPorPrecio(min, max);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // BUSCAR POR NOMBRE
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        try {
            List<Producto> productos = productoService.buscarPorNombre(nombre);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // CAMBIAR ESTADO
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado) {
        try {
            Producto producto = productoService.cambiarEstadoProducto(id, nuevoEstado);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}