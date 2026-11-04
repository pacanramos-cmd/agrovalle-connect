package com.agrovalle.connect.controller;

import com.agrovalle.connect.dto.AgricultorResponse;
import com.agrovalle.connect.dto.LoginRequest;
import com.agrovalle.connect.dto.LoginResponse;
import com.agrovalle.connect.dto.RegistroAgricultorRequest;
import com.agrovalle.connect.service.AgricultorService;
import com.agrovalle.connect.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST de autenticación.
 *
 * <p>Expone dos endpoints públicos (sin JWT):
 * <ul>
 *   <li>{@code POST /api/v1/auth/register} — HU-01: registro de agricultores.</li>
 *   <li>{@code POST /api/v1/auth/login}    — HU-13: inicio de sesión con JWT.</li>
 * </ul>
 *
 * <p>Principio SOLID: SRP — este controlador gestiona únicamente las operaciones
 * de autenticación y registro.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AgricultorService agricultorService;
    private final AuthService authService;

    /**
     * Construye el controlador con sus servicios (inyección por constructor — DIP).
     *
     * @param agricultorService servicio de registro de agricultores
     * @param authService       servicio de autenticación JWT
     */
    public AuthController(AgricultorService agricultorService, AuthService authService) {
        this.agricultorService = agricultorService;
        this.authService = authService;
    }

    /**
     * Registra un agricultor nuevo en la plataforma.
     *
     * <p>BDD Escenario 1 — Registro exitoso: responde 201 Created con los datos del agricultor.
     * BDD Escenario 2 — Datos inválidos: Bean Validation lanza {@code MethodArgumentNotValidException}
     * que el {@code GlobalExceptionHandler} convierte en 400 Bad Request.
     *
     * @param solicitud datos del agricultor validados por Bean Validation
     * @return respuesta con los datos públicos del agricultor registrado
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AgricultorResponse registrar(@RequestBody @Valid RegistroAgricultorRequest solicitud) {
        return agricultorService.registrar(solicitud);
    }

    /**
     * Autentica al usuario y devuelve un token JWT.
     *
     * <p>BDD Escenario 1 — Inicio exitoso: 200 OK con el token.
     * BDD Escenario 2 — Credenciales inválidas: 401 Unauthorized.
     *
     * @param solicitud credenciales validadas por Bean Validation
     * @return token JWT con el identificador y el rol del usuario
     */
    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest solicitud) {
        return authService.login(solicitud);
    }
}
