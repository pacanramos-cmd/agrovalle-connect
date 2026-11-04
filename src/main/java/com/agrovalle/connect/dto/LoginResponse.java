package com.agrovalle.connect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Respuesta al inicio de sesión exitoso (HU-13).
 *
 * @param token     token JWT que el cliente debe enviar en el encabezado {@code Authorization}
 * @param tipo      tipo de token; siempre {@code Bearer}
 * @param usuarioId identificador del usuario autenticado
 * @param rol       rol del usuario en la plataforma
 */
public record LoginResponse(
        String token,
        String tipo,
        @JsonProperty("usuario_id") Long usuarioId,
        String rol) {

    /**
     * Crea una respuesta con tipo Bearer por defecto.
     *
     * @param token     token JWT
     * @param usuarioId identificador del usuario
     * @param rol       rol del usuario
     * @return respuesta con tipo Bearer
     */
    public static LoginResponse bearer(String token, Long usuarioId, String rol) {
        return new LoginResponse(token, "Bearer", usuarioId, rol);
    }
}
