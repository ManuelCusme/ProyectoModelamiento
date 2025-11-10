package com.tuempresa.appventas.service;

import com.tuempresa.appventas.model.Favorito;
import com.tuempresa.appventas.model.Usuario;
import com.tuempresa.appventas.model.Producto;
import com.tuempresa.appventas.repository.FavoritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    public Favorito agregarFavorito(Usuario usuario, Producto producto) {
        // Verificar si ya existe
        var existente = favoritoRepository.findByUsuarioIdAndProductoId(usuario.getId(), producto.getId());
        if (existente.isPresent()) {
            throw new RuntimeException("El producto ya está en favoritos");
        }

        Favorito favorito = new Favorito(usuario, producto);
        return favoritoRepository.save(favorito);
    }

    @Transactional
    public void eliminarFavorito(Long usuarioId, Long productoId) {
        favoritoRepository.deleteByUsuarioIdAndProductoId(usuarioId, productoId);
    }

    public List<Favorito> obtenerFavoritosUsuario(Long usuarioId) {
        return favoritoRepository.findByUsuarioId(usuarioId);
    }

    public boolean esFavorito(Long usuarioId, Long productoId) {
        return favoritoRepository.findByUsuarioIdAndProductoId(usuarioId, productoId).isPresent();
    }
}