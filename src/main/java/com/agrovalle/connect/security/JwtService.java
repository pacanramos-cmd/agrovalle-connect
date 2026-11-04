package com.agrovalle.connect.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Genera y valida tokens JWT firmados con HMAC-SHA256.
 *
 * <p>El token incluye el identificador del usuario y su rol. La clave secreta y la vigencia
 * se configuran mediante variables de entorno ({@code JWT_SECRET} y {@code JWT_EXPIRATION_MS}).
 *
 * <p>Principio SOLID: SRP — esta clase tiene una única responsabilidad: la gestión de JWT.
 */
@Service
public class JwtService {

    private static final String CLAIM_ROL = "rol";

    private final SecretKey secretKey;
    private final long expirationMs;

    /**
     * Construye el servicio con la clave y vigencia configuradas en el entorno.
     *
     * @param secret      clave secreta en texto plano (mínimo 32 caracteres para HS256)
     * @param expirationMs vigencia del token en milisegundos
     */
    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /**
     * Genera un token JWT firmado con el identificador y el rol del usuario.
     *
     * @param usuarioId identificador del usuario en la base de datos
     * @param rol       nombre del rol (por ejemplo, {@code AGRICULTOR})
     * @return token JWT como cadena compacta
     */
    public String generarToken(Long usuarioId, String rol) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expirationMs);
        return Jwts.builder()
                .subject(String.valueOf(usuarioId))
                .claim(CLAIM_ROL, rol)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Extrae el identificador del usuario del token.
     *
     * @param token token JWT compacto
     * @return identificador del usuario como {@code Long}
     * @throws JwtException si el token es inválido o expiró
     */
    public Long extraerUsuarioId(String token) {
        return Long.valueOf(extraerClaims(token).getSubject());
    }

    /**
     * Extrae el rol del usuario del token.
     *
     * @param token token JWT compacto
     * @return nombre del rol
     * @throws JwtException si el token es inválido o expiró
     */
    public String extraerRol(String token) {
        return extraerClaims(token).get(CLAIM_ROL, String.class);
    }

    /**
     * Verifica que el token no haya expirado ni haya sido alterado.
     *
     * @param token token JWT compacto
     * @return {@code true} si el token es válido
     */
    public boolean esValido(String token) {
        try {
            extraerClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
