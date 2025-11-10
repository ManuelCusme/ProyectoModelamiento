package com.tuempresa.appventas.repository;

import com.tuempresa.appventas.model.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface HistorialRepository extends JpaRepository<Historial, Long> {

    //Método para eliminar historial de un producto específico
    @Transactional
    void deleteByProductoId(Long productoId);

    //Método para obtener historial de un usuario ordenado por fecha
    List<Historial> findByUsuarioIdOrderByFechaVistoDesc(Long usuarioId);
}