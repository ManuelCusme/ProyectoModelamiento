package com.tuempresa.appventas.service;

import com.tuempresa.appventas.model.Producto;
import com.tuempresa.appventas.model.Usuario;
import com.tuempresa.appventas.repository.ProductoRepository;
import com.tuempresa.appventas.repository.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private HistorialRepository historialRepository;

    // CREAR PRODUCTO
    public Producto crearProducto(Producto producto, Usuario vendedor) {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del producto es obligatorio");
        }

        if (producto.getPrecio() <= 0) {
            throw new RuntimeException("El precio debe ser mayor a 0");
        }

        producto.setVendedor(vendedor);
        producto.setEstado("ACTIVO");
        producto.setDisponibilidad(true);
        producto.setFechaPublicacion(new Date());
        producto.setCodigo("PROD-" + System.currentTimeMillis());

        return productoRepository.save(producto);
    }

    // OBTENER PRODUCTOS ACTIVOS
    public List<Producto> obtenerProductosActivos() {
        return productoRepository.findByEstado("ACTIVO");
    }

    // OBTENER PRODUCTOS POR VENDEDOR
    public List<Producto> obtenerProductosPorVendedor(Long vendedorId) {
        return productoRepository.findByVendedorId(vendedorId);
    }

    // OBTENER PRODUCTO POR ID
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // ACTUALIZAR PRODUCTO
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setDescripcion(productoActualizado.getDescripcion());
                    producto.setPrecio(productoActualizado.getPrecio());
                    producto.setTipo(productoActualizado.getTipo());
                    producto.setUbicacion(productoActualizado.getUbicacion());
                    producto.setDisponibilidad(productoActualizado.getDisponibilidad());
                    producto.setImagenUrl(productoActualizado.getImagenUrl());
                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // CAMBIAR ESTADO DE PRODUCTO
    public Producto cambiarEstadoProducto(Long id, String nuevoEstado) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setEstado(nuevoEstado);
                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // ELIMINAR PRODUCTO (cambiar estado a OCULTO)
    public Producto eliminarProducto(Long id) {
        return cambiarEstadoProducto(id, "OCULTO");
    }

    // ✅ ELIMINAR PRODUCTO DEFINITIVO (físicamente de la BD)
    public void eliminarProductoDefinitivo(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // ✅ PRIMERO: Eliminar historial relacionado
        historialRepository.deleteByProductoId(id);

        // ✅ SEGUNDO: Eliminar el producto
        productoRepository.delete(producto);
    }

    // FILTRAR PRODUCTOS POR PRECIO
    public List<Producto> filtrarPorPrecio(Double precioMin, Double precioMax) {
        List<Producto> productosActivos = obtenerProductosActivos();
        return productosActivos.stream()
                .filter(p -> p.getPrecio() >= precioMin && p.getPrecio() <= precioMax)
                .toList();
    }

    // BUSCAR PRODUCTOS POR NOMBRE
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> productosActivos = obtenerProductosActivos();
        return productosActivos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }
}