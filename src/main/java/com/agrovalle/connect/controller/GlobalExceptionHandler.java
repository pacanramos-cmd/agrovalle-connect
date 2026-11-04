package com.agrovalle.connect.controller;

import com.agrovalle.connect.dto.ErrorResponse;
import com.agrovalle.connect.exception.AccesoDenegadoException;
import com.agrovalle.connect.exception.CredencialesInvalidasException;
import com.agrovalle.connect.exception.FechaCosechaInvalidaException;
import com.agrovalle.connect.exception.MunicipioNoEncontradoException;
import com.agrovalle.connect.exception.ProductoNoEncontradoException;
import com.agrovalle.connect.exception.RecursoDuplicadoException;
import com.agrovalle.connect.exception.TipoProductoNoEncontradoException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones que transforma los errores del dominio en respuestas HTTP
 * con un cuerpo JSON estructurado ({@link ErrorResponse}).
 *
 * <p>Principio SOLID: OCP — para agregar el manejo de una nueva excepción solo se añade
 * un método {@code @ExceptionHandler} sin modificar los existentes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_VALIDACION = "Datos invalidos";
    private static final String ERROR_DUPLICADO = "Recurso duplicado";
    private static final String ERROR_NO_ENCONTRADO = "No encontrado";
    private static final String ERROR_CREDENCIALES = "Credenciales incorrectas";
    private static final String ERROR_ACCESO = "Acceso denegado";

    /**
     * Maneja los errores de validación de Bean Validation (400 Bad Request).
     * Devuelve la lista de campos inválidos con su mensaje.
     *
     * @param ex excepción de argumento no válido lanzada por Spring MVC
     * @return respuesta 400 con detalles de cada campo inválido
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse manejarValidacion(MethodArgumentNotValidException ex) {
        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.toList());
        return ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ERROR_VALIDACION, detalles);
    }

    /**
     * Maneja los intentos de registro con cédula o correo duplicados (409 Conflict).
     *
     * @param ex excepción de recurso duplicado
     * @return respuesta 409 con el mensaje del problema
     */
    @ExceptionHandler(RecursoDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse manejarDuplicado(RecursoDuplicadoException ex) {
        return ErrorResponse.of(HttpStatus.CONFLICT.value(), ERROR_DUPLICADO,
                List.of(ex.getMessage()));
    }

    /**
     * Maneja los registros con un municipio fuera del catálogo del Valle (400 Bad Request).
     *
     * @param ex excepción de municipio no encontrado
     * @return respuesta 400 con el mensaje del problema
     */
    @ExceptionHandler(MunicipioNoEncontradoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse manejarMunicipioNoEncontrado(MunicipioNoEncontradoException ex) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ERROR_VALIDACION,
                List.of(ex.getMessage()));
    }

    /**
     * Maneja los intentos de inicio de sesión con credenciales inválidas (401 Unauthorized).
     *
     * @param ex excepción de credenciales inválidas
     * @return respuesta 401 sin revelar cuál campo es incorrecto
     */
    @ExceptionHandler(CredencialesInvalidasException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse manejarCredencialesInvalidas(CredencialesInvalidasException ex) {
        return ErrorResponse.of(HttpStatus.UNAUTHORIZED.value(), ERROR_CREDENCIALES);
    }

    /**
     * Maneja los intentos de acceso a un producto inexistente (404 Not Found).
     *
     * @param ex excepción de producto no encontrado
     * @return respuesta 404 con el mensaje del problema
     */
    @ExceptionHandler(ProductoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse manejarProductoNoEncontrado(ProductoNoEncontradoException ex) {
        return ErrorResponse.of(HttpStatus.NOT_FOUND.value(), ERROR_NO_ENCONTRADO,
                List.of(ex.getMessage()));
    }

    /**
     * Cuerpo ausente, JSON mal formado o valor de enumeración desconocido.
     *
     * @param ex error de lectura del cuerpo
     * @return respuesta 400
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse manejarCuerpoIlegible(HttpMessageNotReadableException ex) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ERROR_VALIDACION,
                List.of("El cuerpo de la solicitud no es válido"));
    }

    /**
     * Reglas de HU-02: fecha de cosecha anterior a hoy o tipo de producto fuera del catálogo.
     *
     * @param ex error de dominio
     * @return respuesta 400
     */
    @ExceptionHandler({FechaCosechaInvalidaException.class, TipoProductoNoEncontradoException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse manejarReglaDeProducto(RuntimeException ex) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ERROR_VALIDACION,
                List.of(ex.getMessage()));
    }

    /**
     * Usuario autenticado sin perfil de agricultor.
     *
     * @param ex error de autorización
     * @return respuesta 403
     */
    @ExceptionHandler(AccesoDenegadoException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse manejarAccesoDenegado(AccesoDenegadoException ex) {
        return ErrorResponse.of(HttpStatus.FORBIDDEN.value(), ERROR_ACCESO, List.of(ex.getMessage()));
    }
}
