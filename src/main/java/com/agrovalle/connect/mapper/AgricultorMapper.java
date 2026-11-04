package com.agrovalle.connect.mapper;

import com.agrovalle.connect.dto.AgricultorResponse;
import com.agrovalle.connect.dto.RegistroAgricultorRequest;
import com.agrovalle.connect.model.Agricultor;
import com.agrovalle.connect.model.Municipio;
import com.agrovalle.connect.model.Rol;
import com.agrovalle.connect.model.Usuario;
import org.springframework.stereotype.Component;

/**
 * Convierte entre los DTO de la API y las entidades de dominio, y centraliza la construcción de un
 * {@link Usuario} y un {@link Agricultor} válidos (patrones Data Mapper y Factory Method).
 */
@Component
public class AgricultorMapper {

    /**
     * Construye el usuario a partir de la solicitud y de los datos ya resueltos por el servicio.
     *
     * @param solicitud datos recibidos
     * @param correo correo ya normalizado a minúsculas
     * @param contrasenaHash contraseña ya cifrada
     * @param rol rol asignado
     * @return el usuario listo para guardar
     */
    public Usuario aUsuario(RegistroAgricultorRequest solicitud, String correo, String contrasenaHash, Rol rol) {
        return new Usuario(solicitud.nombre().trim(), correo, contrasenaHash, rol);
    }

    /**
     * Construye el perfil de agricultor de un usuario ya guardado.
     *
     * @param solicitud datos recibidos
     * @param usuario usuario guardado
     * @param municipio municipio existente en el catálogo
     * @return el agricultor listo para guardar
     */
    public Agricultor aEntidad(RegistroAgricultorRequest solicitud, Usuario usuario, Municipio municipio) {
        return new Agricultor(usuario, solicitud.cedula().trim(), municipio);
    }

    /**
     * Convierte la entidad en la respuesta de la API, sin datos sensibles.
     *
     * @param agricultor entidad guardada
     * @return la respuesta pública
     */
    public AgricultorResponse aRespuesta(Agricultor agricultor) {
        return new AgricultorResponse(agricultor.getId(), agricultor.getNombre(), agricultor.getCedula(),
                agricultor.getCorreo(), agricultor.getMunicipio().getNombre());
    }
}
