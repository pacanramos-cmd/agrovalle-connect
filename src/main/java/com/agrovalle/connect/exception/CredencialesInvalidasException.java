package com.agrovalle.connect.exception;

/**
 * Excepción lanzada cuando las credenciales de inicio de sesión son inválidas (HU-13).
 *
 * <p>El mensaje intencionalmente no indica si el correo o la contraseña son incorrectos,
 * para evitar la enumeración de usuarios (protección de seguridad).
 */
public class CredencialesInvalidasException extends RuntimeException {

    /** Crea la excepción con un mensaje genérico. */
    public CredencialesInvalidasException() {
        super("Correo o contrasena incorrectos");
    }
}
