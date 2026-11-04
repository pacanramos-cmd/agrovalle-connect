package com.agrovalle.connect.service;

import com.agrovalle.connect.dto.AgricultorResponse;
import com.agrovalle.connect.dto.RegistroAgricultorRequest;
import com.agrovalle.connect.exception.MunicipioNoEncontradoException;
import com.agrovalle.connect.exception.RecursoDuplicadoException;
import com.agrovalle.connect.mapper.AgricultorMapper;
import com.agrovalle.connect.model.Agricultor;
import com.agrovalle.connect.model.Municipio;
import com.agrovalle.connect.model.Rol;
import com.agrovalle.connect.model.Usuario;
import com.agrovalle.connect.repository.AgricultorRepository;
import com.agrovalle.connect.repository.MunicipioRepository;
import com.agrovalle.connect.repository.RolRepository;
import com.agrovalle.connect.repository.UsuarioRepository;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Reglas de negocio del registro de agricultores (HU-01).
 */
@Service
public class AgricultorService {

    private static final String ROL_AGRICULTOR = "AGRICULTOR";

    private final UsuarioRepository usuarioRepository;
    private final AgricultorRepository agricultorRepository;
    private final MunicipioRepository municipioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AgricultorMapper mapper;

    /**
     * Crea el servicio con sus dependencias, todas abstracciones inyectadas por constructor.
     *
     * @param usuarioRepository acceso a usuarios
     * @param agricultorRepository acceso a agricultores
     * @param municipioRepository acceso al catálogo de municipios
     * @param rolRepository acceso al catálogo de roles
     * @param passwordEncoder cifrador de contraseñas
     * @param mapper conversor entre DTO y entidades
     */
    @Autowired
    public AgricultorService(UsuarioRepository usuarioRepository, AgricultorRepository agricultorRepository,
            MunicipioRepository municipioRepository, RolRepository rolRepository,
            PasswordEncoder passwordEncoder, AgricultorMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.agricultorRepository = agricultorRepository;
        this.municipioRepository = municipioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    /**
     * Registra un agricultor nuevo: rechaza cédula o correo ya usados, valida el municipio, cifra la
     * contraseña con BCrypt y guarda el usuario y su perfil en una sola transacción.
     *
     * @param solicitud datos ya validados por la capa de presentación
     * @return el agricultor registrado, sin datos sensibles
     * @throws RecursoDuplicadoException si la cédula o el correo ya existen
     * @throws MunicipioNoEncontradoException si el municipio no está en el catálogo
     */
    @Transactional
    public AgricultorResponse registrar(RegistroAgricultorRequest solicitud) {
        String cedula = solicitud.cedula().trim();
        String correo = solicitud.correo().trim().toLowerCase(Locale.ROOT);

        if (agricultorRepository.existsByCedula(cedula)) {
            throw new RecursoDuplicadoException("La cédula ya está registrada");
        }
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new RecursoDuplicadoException("El correo ya está registrado");
        }

        Municipio municipio = municipioRepository.findByNombreIgnoreCase(solicitud.ubicacionValle().trim())
                .orElseThrow(() -> new MunicipioNoEncontradoException(
                        "El municipio no pertenece al catálogo del Valle del Cauca"));
        Rol rol = rolRepository.findByNombre(ROL_AGRICULTOR)
                .orElseThrow(() -> new IllegalStateException("El rol AGRICULTOR no está configurado"));

        String contrasenaHash = passwordEncoder.encode(solicitud.contrasena());
        Usuario usuario = usuarioRepository.save(mapper.aUsuario(solicitud, correo, contrasenaHash, rol));
        Agricultor agricultor = agricultorRepository.save(mapper.aEntidad(solicitud, usuario, municipio));
        return mapper.aRespuesta(agricultor);
    }
}
