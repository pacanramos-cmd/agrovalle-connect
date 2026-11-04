package com.agrovalle.connect.service;

import com.agrovalle.connect.dto.ProductoRequest;
import com.agrovalle.connect.dto.ProductoResponse;
import com.agrovalle.connect.event.ProductoPublicadoEvent;
import com.agrovalle.connect.exception.AccesoDenegadoException;
import com.agrovalle.connect.exception.FechaCosechaInvalidaException;
import com.agrovalle.connect.exception.TipoProductoNoEncontradoException;
import com.agrovalle.connect.mapper.ProductoMapper;
import com.agrovalle.connect.model.Agricultor;
import com.agrovalle.connect.model.Producto;
import com.agrovalle.connect.model.TipoProducto;
import com.agrovalle.connect.repository.AgricultorRepository;
import com.agrovalle.connect.repository.ProductoRepository;
import com.agrovalle.connect.repository.ProductoSpecifications;
import com.agrovalle.connect.repository.TipoProductoRepository;
import com.agrovalle.connect.service.comision.SelectorComision;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Reglas de negocio de la oferta de productos: publicación (HU-02) y filtro por municipio y categoría
 * (HU-04).
 */
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final AgricultorRepository agricultorRepository;
    private final TipoProductoRepository tipoProductoRepository;
    private final ProductoMapper mapper;
    private final SelectorComision selectorComision;
    private final ApplicationEventPublisher publicador;
    private final Clock reloj;

    /**
     * Crea el servicio con sus dependencias, todas inyectadas por constructor.
     *
     * @param productoRepository acceso a productos
     * @param agricultorRepository acceso a agricultores
     * @param tipoProductoRepository acceso al catálogo de tipos de producto
     * @param mapper conversor entre DTO y entidades
     * @param selectorComision cálculo de la comisión según el municipio
     * @param publicador publicador de eventos de dominio
     * @param reloj fuente de la fecha actual, reemplazable en las pruebas
     */
    @Autowired
    public ProductoService(ProductoRepository productoRepository, AgricultorRepository agricultorRepository,
            TipoProductoRepository tipoProductoRepository, ProductoMapper mapper,
            SelectorComision selectorComision, ApplicationEventPublisher publicador, Clock reloj) {
        this.productoRepository = productoRepository;
        this.agricultorRepository = agricultorRepository;
        this.tipoProductoRepository = tipoProductoRepository;
        this.mapper = mapper;
        this.selectorComision = selectorComision;
        this.publicador = publicador;
        this.reloj = reloj;
    }

    /**
     * Publica un producto del agricultor autenticado y notifica el evento a los observadores.
     *
     * @param usuarioId identificador del usuario autenticado
     * @param solicitud datos ya validados por la capa de presentación
     * @return el producto publicado
     * @throws AccesoDenegadoException si el usuario no es agricultor
     * @throws FechaCosechaInvalidaException si la fecha de cosecha es anterior a hoy
     * @throws TipoProductoNoEncontradoException si el tipo no existe en el catálogo
     */
    @Transactional
    public ProductoResponse publicar(Long usuarioId, ProductoRequest solicitud) {
        Agricultor agricultor = agricultorRepository.findById(usuarioId)
                .orElseThrow(() -> new AccesoDenegadoException("Solo los agricultores pueden publicar productos"));
        if (solicitud.fechaCosecha().isBefore(LocalDate.now(reloj))) {
            throw new FechaCosechaInvalidaException("La fecha de cosecha no puede ser anterior a la fecha actual");
        }
        TipoProducto tipo = tipoProductoRepository.findByNombreIgnoreCase(solicitud.tipo().trim())
                .orElseThrow(() -> new TipoProductoNoEncontradoException(
                        "El tipo de producto no existe en el catálogo"));

        Producto guardado = productoRepository.save(mapper.aEntidad(solicitud, agricultor, tipo));
        publicador.publishEvent(new ProductoPublicadoEvent(guardado.getId(), agricultor.getId(), tipo.getNombre()));
        return aRespuesta(guardado);
    }

    /**
     * Lista las ofertas activas, filtradas opcionalmente por municipio y categoría. Un filtro vacío o en
     * blanco se ignora. Sin coincidencias devuelve una lista vacía.
     *
     * @param municipio nombre del municipio, o {@code null}
     * @param categoria nombre de la categoría, o {@code null}
     * @return las ofertas que cumplen los filtros, las más recientes primero
     */
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(String municipio, String categoria) {
        Specification<Producto> criterio = ProductoSpecifications.activo();
        if (tieneTexto(municipio)) {
            criterio = criterio.and(ProductoSpecifications.deMunicipio(municipio.trim()));
        }
        if (tieneTexto(categoria)) {
            criterio = criterio.and(ProductoSpecifications.deCategoria(categoria.trim()));
        }
        return productoRepository.findAll(criterio, Sort.by(Sort.Direction.DESC, "creadoEn")).stream()
                .map(this::aRespuesta)
                .toList();
    }

    private boolean tieneTexto(String valor) {
        return valor != null && !valor.isBlank();
    }

    private ProductoResponse aRespuesta(Producto producto) {
        BigDecimal valorVenta = producto.getCantidadDisponible().multiply(producto.getPrecioUnitario());
        BigDecimal comision = selectorComision.calcular(
                producto.getAgricultor().getMunicipio().getNombre(), valorVenta);
        return mapper.aRespuesta(producto, comision);
    }
}
