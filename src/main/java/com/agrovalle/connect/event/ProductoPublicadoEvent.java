package com.agrovalle.connect.event;

/**
 * Evento de dominio que se publica cuando un agricultor publica un producto (patrón Observer con
 * {@code ApplicationEventPublisher}). Base de la notificación de HU-12.
 *
 * @param productoId identificador del producto publicado
 * @param agricultorId identificador del agricultor que lo publicó
 * @param tipoProducto nombre del tipo de producto
 */
public record ProductoPublicadoEvent(Long productoId, Long agricultorId, String tipoProducto) {
}
