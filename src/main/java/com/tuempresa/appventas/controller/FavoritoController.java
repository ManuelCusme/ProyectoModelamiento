package com.tuempresa.appventas.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.tuempresa.appventas.model.Favorito;
import com.tuempresa.appventas.service.FavoritoService;
import com.tuempresa.appventas.service.UsuarioService;
import com.tuempresa.appventas.service.ProductoService;
import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
@CrossOrigin(origins = "*")
public class FavoritoController {

    private final FavoritoService favoritoService;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public FavoritoController(FavoritoService favoritoService, UsuarioService usuarioService, ProductoService productoService) {
        this.favoritoService = favoritoService;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<?> agregarFavorito(@RequestParam Long usuarioId, @RequestParam Long productoId) {
        try {
            var usuario = usuarioService.obtenerPorId(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            var producto = productoService.obtenerPorId(productoId);

            Favorito favorito = favoritoService.agregarFavorito(usuario, producto);
            return ResponseEntity.ok(favorito);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> eliminarFavorito(@RequestParam Long usuarioId, @RequestParam Long productoId) {
        try {
            favoritoService.eliminarFavorito(usuarioId, productoId);
            return ResponseEntity.ok("Favorito eliminado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Favorito>> obtenerFavoritos(@PathVariable Long usuarioId) {
        try {
            List<Favorito> favoritos = favoritoService.obtenerFavoritosUsuario(usuarioId);
            return ResponseEntity.ok(favoritos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> esFavorito(@RequestParam Long usuarioId, @RequestParam Long productoId) {
        boolean esFav = favoritoService.esFavorito(usuarioId, productoId);
        return ResponseEntity.ok(esFav);
    }
}