package com.tuempresa.appventas.service;

import com.tuempresa.appventas.model.Historial;
import com.tuempresa.appventas.model.Usuario;
import com.tuempresa.appventas.model.Producto;
import com.tuempresa.appventas.repository.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HistorialService {

    @Autowired
    private HistorialRepository historialRepository;

    public Historial registrarVista(Usuario usuario, Producto producto) {
        Historial historial = new Historial(usuario, producto);
        return historialRepository.save(historial);
    }

    public List<Historial> obtenerHistorialUsuario(Long usuarioId) {
        return historialRepository.findByUsuarioIdOrderByFechaVistoDesc(usuarioId);
    }
}