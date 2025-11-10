package com.tuempresa.appventas.service;

import com.tuempresa.appventas.model.Usuario;
import com.tuempresa.appventas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // VALIDAR CÉDULA ECUATORIANA (Algoritmo oficial)
    private boolean validarCedulaEcuatoriana(String cedula) {
        if (cedula == null || cedula.length() != 10) {
            return false;
        }

        try {
            if (!cedula.matches("\\d+")) {
                return false;
            }

            int provincia = Integer.parseInt(cedula.substring(0, 2));
            if (provincia < 1 || provincia > 24) {
                return false;
            }

            int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
            if (tercerDigito > 5) {
                return false;
            }

            int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
            int suma = 0;

            for (int i = 0; i < 9; i++) {
                int digito = Integer.parseInt(cedula.substring(i, i + 1));
                int producto = digito * coeficientes[i];

                if (producto >= 10) {
                    producto -= 9;
                }

                suma += producto;
            }

            int digitoVerificador = 10 - (suma % 10);
            if (digitoVerificador == 10) {
                digitoVerificador = 0;
            }

            int ultimoDigito = Integer.parseInt(cedula.substring(9, 10));

            return digitoVerificador == ultimoDigito;

        } catch (Exception e) {
            return false;
        }
    }

    // REGISTRAR USUARIO
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            throw new RuntimeException("El email ya está registrado");
        }

        if (usuarioRepository.findByCedula(usuario.getCedula()) != null) {
            throw new RuntimeException("La cédula ya está registrada");
        }

        if (!validarCedulaEcuatoriana(usuario.getCedula())) {
            throw new RuntimeException("Cédula ecuatoriana inválida");
        }

        String passwordCifrada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordCifrada);

        usuario.setTipoUsuario("VENDEDOR");
        usuario.setEstado("PENDIENTE_VERIFICACION");
        usuario.setCuentaVerificada(false);

        String token = UUID.randomUUID().toString();
        usuario.setVerificationToken(token);

        // Token expira en 15 minutos
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 15);
        usuario.setTokenExpiracion(calendar.getTime());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        try {
            emailService.enviarEmailVerificacion(
                    usuario.getEmail(),
                    usuario.getNombre(),
                    token
            );
        } catch (Exception e) {
            System.err.println("⚠️ Usuario creado pero no se pudo enviar email: " + e.getMessage());
        }

        return usuarioGuardado;
    }

    // ✅ ARREGLADO: LOGIN - NO permite acceso sin verificar email
    public Usuario login(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // ✅ VALIDACIÓN: Debe estar verificado para poder iniciar sesión
        if (!usuario.isCuentaVerificada()) {
            throw new RuntimeException("Debes verificar tu email antes de iniciar sesión. Revisa tu correo.");
        }

        // ✅ VALIDACIÓN: Debe estar activo
        if (!"ACTIVO".equals(usuario.getEstado())) {
            throw new RuntimeException("Tu cuenta está suspendida o inactiva.");
        }

        return usuario;
    }

    // Verificar email con token
    public Usuario verificarEmail(String token) {
        Usuario usuario = usuarioRepository.findByVerificationToken(token);

        if (usuario == null) {
            throw new RuntimeException("Token de verificación inválido");
        }

        // Verificar si el token ha expirado
        if (usuario.getTokenExpiracion().before(new Date())) {
            throw new RuntimeException("El token ha expirado. Solicita uno nuevo desde el login.");
        }

        usuario.setCuentaVerificada(true);
        usuario.setEstado("ACTIVO");
        usuario.setVerificationToken(null);
        usuario.setTokenExpiracion(null);

        Usuario usuarioVerificado = usuarioRepository.save(usuario);

        try {
            emailService.enviarEmailBienvenida(usuario.getEmail(), usuario.getNombre());
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo enviar email de bienvenida");
        }

        return usuarioVerificado;
    }

    // Reenviar email de verificación
    public void reenviarEmailVerificacion(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        if (usuario.isCuentaVerificada()) {
            throw new RuntimeException("La cuenta ya está verificada");
        }

        String nuevoToken = UUID.randomUUID().toString();
        usuario.setVerificationToken(nuevoToken);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 15);
        usuario.setTokenExpiracion(calendar.getTime());

        usuarioRepository.save(usuario);

        emailService.enviarEmailVerificacion(
                usuario.getEmail(),
                usuario.getNombre(),
                nuevoToken
        );
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNombre(usuarioActualizado.getNombre());
                    usuario.setApellido(usuarioActualizado.getApellido());
                    usuario.setTelefono(usuarioActualizado.getTelefono());
                    usuario.setDireccion(usuarioActualizado.getDireccion());

                    if (usuarioActualizado.getPassword() != null &&
                            !usuarioActualizado.getPassword().isEmpty()) {
                        usuario.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
                    }

                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario cambiarEstadoUsuario(Long id, String nuevoEstado) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setEstado(nuevoEstado);
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario verificarCuenta(Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setCuentaVerificada(true);
                    usuario.setEstado("ACTIVO");
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    //NUEVO: Solicitar recuperación de contraseña
    public void solicitarRecuperacionPassword(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new RuntimeException("No existe una cuenta con ese email");
        }

        // Generar código de 6 dígitos
        String codigo = String.format("%06d", new Random().nextInt(999999));

        usuario.setResetPasswordToken(codigo);

        // Expira en 15 minutos
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 15);
        usuario.setResetPasswordExpiracion(calendar.getTime());

        usuarioRepository.save(usuario);

        // Enviar email con el código
        emailService.enviarCodigoRecuperacion(
                usuario.getEmail(),
                usuario.getNombre(),
                codigo
        );
    }

    //NUEVO: Verificar código de recuperación
    public boolean verificarCodigoRecuperacion(String email, String codigo) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return false;
        }

        if (usuario.getResetPasswordToken() == null) {
            return false;
        }

        // Verificar si el código coincide
        if (!usuario.getResetPasswordToken().equals(codigo)) {
            return false;
        }

        // Verificar si no ha expirado
        if (usuario.getResetPasswordExpiracion().before(new Date())) {
            return false;
        }

        return true;
    }

    //NUEVO: Restablecer contraseña
    public void restablecerPassword(String email, String codigo, String nuevaPassword) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // Verificar código
        if (!verificarCodigoRecuperacion(email, codigo)) {
            throw new RuntimeException("Código inválido o expirado");
        }

        // Actualizar contraseña
        String passwordCifrada = passwordEncoder.encode(nuevaPassword);
        usuario.setPassword(passwordCifrada);

        // Limpiar tokens de recuperación
        usuario.setResetPasswordToken(null);
        usuario.setResetPasswordExpiracion(null);

        usuarioRepository.save(usuario);

        // Enviar confirmación por email
        emailService.enviarConfirmacionCambioPassword(
                usuario.getEmail(),
                usuario.getNombre()
        );
    }
}