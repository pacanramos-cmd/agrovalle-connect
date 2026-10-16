package com.agrovalle.connect.mapper;

import com.agrovalle.connect.dto.AgricultorResponse;
import com.agrovalle.connect.dto.RegistroAgricultorRequest;
import com.agrovalle.connect.model.Agricultor;
import com.agrovalle.connect.model.Municipio;
import com.agrovalle.connect.model.Rol;
import org.springframework.stereotype.Component;

/**
 * Convierte entre los DTO de la API y las entidades de dominio, y centraliza la construcción de un
 * {@link Agricultor} válido (patrones Data Mapper y Factory Method).
 */
@Component
public class AgricultorMapper {

    /**
     * Construye la entidad a partir de la solicitud y de los datos ya resueltos por el servicio.
     *
     * @param solicitud datos recibidos
     * @param correo correo ya normalizado a minúsculas
     * @param contrasenaHash contraseña ya cifrada
     * @param municipio municipio existente en el catálogo
     * @param rol rol asignado
     * @return el agricultor listo para guardar
     */
    public Agricultor aEntidad(RegistroAgricultorRequest solicitud, String correo, String contrasenaHash,
            Municipio municipio, Rol rol) {
        return new Agricultor(solicitud.nombre().trim(), solicitud.cedula().trim(), correo, contrasenaHash,
                municipio, rol);
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
