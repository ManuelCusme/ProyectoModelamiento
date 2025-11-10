package com.tuempresa.appventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.tuempresa.appventas.model.Producto;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByEstado(String estado);

    List<Producto> findByVendedorId(Long vendedorId);

    // Búsqueda por nombre
    @Query("SELECT p FROM Producto p WHERE p.nombre LIKE %:nombre% AND p.estado = 'ACTIVO'")
    List<Producto> buscarPorNombre(@Param("nombre") String nombre);

    // Búsqueda por rango de precios
    @Query("SELECT p FROM Producto p WHERE p.precio BETWEEN :precioMin AND :precioMax AND p.estado = 'ACTIVO'")
    List<Producto> buscarPorRangoPrecio(@Param("precioMin") Double precioMin,
                                        @Param("precioMax") Double precioMax);
}