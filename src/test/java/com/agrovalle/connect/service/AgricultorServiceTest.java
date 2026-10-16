package com.agrovalle.connect.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agrovalle.connect.dto.AgricultorResponse;
import com.agrovalle.connect.dto.RegistroAgricultorRequest;
import com.agrovalle.connect.exception.MunicipioNoEncontradoException;
import com.agrovalle.connect.exception.RecursoDuplicadoException;
import com.agrovalle.connect.mapper.AgricultorMapper;
import com.agrovalle.connect.model.Agricultor;
import com.agrovalle.connect.model.Municipio;
import com.agrovalle.connect.model.Rol;
import com.agrovalle.connect.repository.AgricultorRepository;
import com.agrovalle.connect.repository.MunicipioRepository;
import com.agrovalle.connect.repository.RolRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Pruebas unitarias de las reglas de negocio del registro (HU-01), con los repositorios simulados.
 */
@ExtendWith(MockitoExtension.class)
class AgricultorServiceTest {

    @Mock
    private AgricultorRepository agricultorRepository;

    @Mock
    private MunicipioRepository municipioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AgricultorService servicio;

    @BeforeEach
    void crearServicio() {
        servicio = new AgricultorService(agricultorRepository, municipioRepository, rolRepository,
                passwordEncoder, new AgricultorMapper());
    }

    private RegistroAgricultorRequest solicitud() {
        return new RegistroAgricultorRequest(
                "Pedro Perez", "Dagua", "1130456789", "Pedro@Correo.com", "clave12345");
    }

    @Test
    void registraAgricultorConContrasenaCifradaYCorreoEnMinusculas() {
        when(municipioRepository.findByNombreIgnoreCase("Dagua")).thenReturn(Optional.of(new Municipio("Dagua")));
        when(rolRepository.findByNombre("AGRICULTOR")).thenReturn(Optional.of(new Rol("AGRICULTOR")));
        when(passwordEncoder.encode("clave12345")).thenReturn("hash-bcrypt");
        when(agricultorRepository.save(any(Agricultor.class))).thenAnswer(llamada -> llamada.getArgument(0));

        AgricultorResponse respuesta = servicio.registrar(solicitud());

        ArgumentCaptor<Agricultor> guardado = ArgumentCaptor.forClass(Agricultor.class);
        verify(agricultorRepository).save(guardado.capture());
        assertEquals("hash-bcrypt", guardado.getValue().getContrasenaHash());
        assertEquals("pedro@correo.com", guardado.getValue().getCorreo());
        assertEquals("AGRICULTOR", guardado.getValue().getRol().getNombre());
        assertEquals("Pedro Perez", respuesta.nombre());
        assertEquals("1130456789", respuesta.cedula());
        assertEquals("pedro@correo.com", respuesta.correo());
        assertEquals("Dagua", respuesta.ubicacionValle());
    }

    @Test
    void rechazaCedulaYaRegistrada() {
        when(agricultorRepository.existsByCedula("1130456789")).thenReturn(true);

        assertThrows(RecursoDuplicadoException.class, () -> servicio.registrar(solicitud()));

        verify(agricultorRepository, never()).save(any(Agricultor.class));
    }

    @Test
    void rechazaCorreoYaRegistrado() {
        when(agricultorRepository.existsByCorreo("pedro@correo.com")).thenReturn(true);

        assertThrows(RecursoDuplicadoException.class, () -> servicio.registrar(solicitud()));

        verify(agricultorRepository, never()).save(any(Agricultor.class));
    }

    @Test
    void rechazaMunicipioFueraDelCatalogo() {
        when(municipioRepository.findByNombreIgnoreCase("Dagua")).thenReturn(Optional.empty());

        assertThrows(MunicipioNoEncontradoException.class, () -> servicio.registrar(solicitud()));

        verify(agricultorRepository, never()).save(any(Agricultor.class));
    }
}
