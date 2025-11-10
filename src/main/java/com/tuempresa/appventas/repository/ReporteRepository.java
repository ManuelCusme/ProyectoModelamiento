package com.tuempresa.appventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tuempresa.appventas.model.Reporte;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByEstado(String estado);
    List<Reporte> findByProductoId(Long productoId);
}