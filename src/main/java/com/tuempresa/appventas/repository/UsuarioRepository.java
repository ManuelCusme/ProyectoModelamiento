package com.tuempresa.appventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tuempresa.appventas.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
    Usuario findByCedula(String cedula);

    // NUEVO: Buscar por token de verificación
    Usuario findByVerificationToken(String token);
}