package com.agrovalle.connect.controller;

import com.agrovalle.connect.dto.ProductoRequest;
import com.agrovalle.connect.dto.ProductoResponse;
import com.agrovalle.connect.service.ProductoService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Capa de presentación de la oferta de productos: publicación (HU-02) y filtro (HU-04). Solo valida la
 * entrada y delega en {@link ProductoService}; la autenticación y el rol los resuelve Spring Security antes
 * de llegar aquí.
 */
@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    /**
     * Crea el controlador.
     *
     * @param productoService servicio de productos
     */
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Publica un producto del agricultor autenticado.
     *
     * @param usuario   usuario autenticado; su nombre es el identificador tomado del token JWT
     * @param solicitud datos del producto
     * @return el producto publicado, con código 201
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponse publicar(Principal usuario, @RequestBody @Valid ProductoRequest solicitud) {
        return productoService.publicar(Long.valueOf(usuario.getName()), solicitud);
    }

    /**
     * Lista las ofertas activas con filtros opcionales.
     *
     * @param municipio municipio del agricultor, opcional
     * @param categoria categoría del producto, opcional
     * @return arreglo JSON de ofertas, vacío si no hay coincidencias
     */
    @GetMapping
    public List<ProductoResponse> listar(
            @RequestParam(name = "municipio", required = false) String municipio,
            @RequestParam(name = "categoria", required = false) String categoria) {
        return productoService.listar(municipio, categoria);
    }
}
