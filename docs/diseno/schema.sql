-- =============================================================================
-- AgroValle Connect - Esquema relacional completo (3FN) para PostgreSQL 15/16+
-- Cobertura: HU-01 a HU-15 del Product Backlog (Sprint 0).
-- Base: agrovalle_db  (valor de la variable de entorno DB_NAME)
--
-- Uso (una sola vez sobre una base vacia):
--   1) CREATE DATABASE agrovalle_db ENCODING 'UTF8';
--   2) psql -U <usuario> -d agrovalle_db -f docs/diseno/schema.sql
--
-- application.properties usa spring.jpa.hibernate.ddl-auto=validate: Hibernate NO
-- crea tablas, solo comprueba que las entidades JPA coincidan con este esquema.
--
-- Orden de las tablas: catalogos -> usuarios -> oferta -> pedidos -> comunicacion.
-- =============================================================================

-- ---------------------------------------------------------------------------
-- 1. CATALOGOS
-- ---------------------------------------------------------------------------

-- rol: el JWT de HU-13 lleva el rol del usuario (AGRICULTOR o COMPRADOR).
CREATE TABLE rol (
    id      BIGINT GENERATED ALWAYS AS IDENTITY,
    nombre  VARCHAR(30) NOT NULL,
    CONSTRAINT pk_rol        PRIMARY KEY (id),
    CONSTRAINT uq_rol_nombre UNIQUE (nombre)
);

-- municipio: municipios del Valle del Cauca (HU-01, HU-04, HU-06).
CREATE TABLE municipio (
    id      BIGINT GENERATED ALWAYS AS IDENTITY,
    nombre  VARCHAR(60) NOT NULL,
    CONSTRAINT pk_municipio        PRIMARY KEY (id),
    CONSTRAINT uq_municipio_nombre UNIQUE (nombre)
);

-- categoria: agrupa los tipos de producto (filtro de HU-04).
CREATE TABLE categoria (
    id      BIGINT GENERATED ALWAYS AS IDENTITY,
    nombre  VARCHAR(60) NOT NULL,
    CONSTRAINT pk_categoria        PRIMARY KEY (id),
    CONSTRAINT uq_categoria_nombre UNIQUE (nombre)
);

-- tipo_producto: cada tipo pertenece a una sola categoria (3FN: la categoria se
-- guarda aqui y no se repite en cada producto publicado). Base de HU-03 y HU-04.
CREATE TABLE tipo_producto (
    id            BIGINT GENERATED ALWAYS AS IDENTITY,
    nombre        VARCHAR(60) NOT NULL,
    categoria_id  BIGINT      NOT NULL,
    CONSTRAINT pk_tipo_producto        PRIMARY KEY (id),
    CONSTRAINT uq_tipo_producto_nombre UNIQUE (nombre),
    CONSTRAINT fk_tipo_producto_categoria FOREIGN KEY (categoria_id)
        REFERENCES categoria (id) ON DELETE RESTRICT
);

-- ---------------------------------------------------------------------------
-- 2. USUARIOS
-- ---------------------------------------------------------------------------

-- usuario: datos de acceso comunes a agricultores y compradores (HU-01, HU-13).
-- El perfil propio de cada rol vive en agricultor o comprador (misma llave primaria).
CREATE TABLE usuario (
    id               BIGINT GENERATED ALWAYS AS IDENTITY,
    nombre           VARCHAR(120) NOT NULL,
    correo           VARCHAR(150) NOT NULL,
    contrasena_hash  VARCHAR(100) NOT NULL,          -- BCrypt, nunca texto plano
    rol_id           BIGINT       NOT NULL,
    creado_en        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_usuario        PRIMARY KEY (id),
    CONSTRAINT uq_usuario_correo UNIQUE (correo),
    CONSTRAINT fk_usuario_rol    FOREIGN KEY (rol_id)
        REFERENCES rol (id) ON DELETE RESTRICT,
    CONSTRAINT ck_usuario_nombre CHECK (BTRIM(nombre) <> ''),
    CONSTRAINT ck_usuario_correo CHECK (correo = LOWER(correo))
);

-- agricultor: perfil del productor, 1 a 1 con usuario (comparte su identificador).
CREATE TABLE agricultor (
    usuario_id    BIGINT      NOT NULL,
    cedula        VARCHAR(10) NOT NULL,
    municipio_id  BIGINT      NOT NULL,
    CONSTRAINT pk_agricultor           PRIMARY KEY (usuario_id),
    CONSTRAINT uq_agricultor_cedula    UNIQUE (cedula),
    CONSTRAINT fk_agricultor_usuario   FOREIGN KEY (usuario_id)
        REFERENCES usuario (id) ON DELETE RESTRICT,
    CONSTRAINT fk_agricultor_municipio FOREIGN KEY (municipio_id)
        REFERENCES municipio (id) ON DELETE RESTRICT,
    CONSTRAINT ck_agricultor_cedula    CHECK (cedula ~ '^[0-9]{6,10}$')
);

-- comprador: perfil del comprador, 1 a 1 con usuario (HU-05, HU-08, HU-10, HU-14).
-- direccion_entrega es el destino que usa el agricultor al programar el despacho.
CREATE TABLE comprador (
    usuario_id         BIGINT       NOT NULL,
    municipio_id       BIGINT       NOT NULL,
    direccion_entrega  VARCHAR(200) NOT NULL,
    CONSTRAINT pk_comprador           PRIMARY KEY (usuario_id),
    CONSTRAINT fk_comprador_usuario   FOREIGN KEY (usuario_id)
        REFERENCES usuario (id) ON DELETE RESTRICT,
    CONSTRAINT fk_comprador_municipio FOREIGN KEY (municipio_id)
        REFERENCES municipio (id) ON DELETE RESTRICT,
    CONSTRAINT ck_comprador_direccion CHECK (BTRIM(direccion_entrega) <> '')
);

-- finca: lugar de produccion del agricultor (HU-06).
CREATE TABLE finca (
    id             BIGINT GENERATED ALWAYS AS IDENTITY,
    agricultor_id  BIGINT       NOT NULL,
    nombre         VARCHAR(120) NOT NULL,
    municipio_id   BIGINT       NOT NULL,
    ubicacion      VARCHAR(200) NOT NULL,            -- vereda, corregimiento o direccion
    CONSTRAINT pk_finca             PRIMARY KEY (id),
    CONSTRAINT uq_finca_agricultor_nombre UNIQUE (agricultor_id, nombre),
    -- Permite que producto verifique que su finca pertenece al mismo agricultor.
    CONSTRAINT uq_finca_id_agricultor UNIQUE (id, agricultor_id),
    CONSTRAINT fk_finca_agricultor  FOREIGN KEY (agricultor_id)
        REFERENCES agricultor (usuario_id) ON DELETE RESTRICT,
    CONSTRAINT fk_finca_municipio   FOREIGN KEY (municipio_id)
        REFERENCES municipio (id) ON DELETE RESTRICT,
    CONSTRAINT ck_finca_nombre      CHECK (BTRIM(nombre) <> '')
);

-- ---------------------------------------------------------------------------
-- 3. OFERTA DE PRODUCTOS
-- ---------------------------------------------------------------------------

-- producto: cosecha publicada por un agricultor (HU-02). Su inventario
-- (cantidad_disponible) alimenta HU-07; su precio, HU-15; sus filtros, HU-04.
CREATE TABLE producto (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY,
    agricultor_id       BIGINT        NOT NULL,
    finca_id            BIGINT,                       -- opcional (HU-06 es independiente)
    tipo_producto_id    BIGINT        NOT NULL,
    cantidad_disponible NUMERIC(10,2) NOT NULL,
    unidad_medida       VARCHAR(10)   NOT NULL,
    precio_unitario     NUMERIC(12,2) NOT NULL,       -- pesos colombianos (COP)
    fecha_cosecha       DATE          NOT NULL,
    activo              BOOLEAN       NOT NULL DEFAULT TRUE,
    creado_en           TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_producto               PRIMARY KEY (id),
    CONSTRAINT fk_producto_agricultor    FOREIGN KEY (agricultor_id)
        REFERENCES agricultor (usuario_id) ON DELETE RESTRICT,
    -- Llave compuesta: si hay finca, debe ser del mismo agricultor del producto.
    -- Con finca_id nulo la restriccion no se evalua (MATCH SIMPLE).
    CONSTRAINT fk_producto_finca         FOREIGN KEY (finca_id, agricultor_id)
        REFERENCES finca (id, agricultor_id) ON DELETE RESTRICT,
    CONSTRAINT fk_producto_tipo_producto FOREIGN KEY (tipo_producto_id)
        REFERENCES tipo_producto (id) ON DELETE RESTRICT,
    CONSTRAINT ck_producto_cantidad      CHECK (cantidad_disponible >= 0),
    CONSTRAINT ck_producto_precio        CHECK (precio_unitario > 0),
    CONSTRAINT ck_producto_unidad        CHECK (unidad_medida IN ('KG', 'LIBRA', 'TONELADA', 'LITRO', 'UNIDAD'))
);

-- ---------------------------------------------------------------------------
-- 4. PEDIDOS
-- ---------------------------------------------------------------------------

-- orden_compra: pedido de un comprador a UN agricultor (HU-08, HU-09, HU-11, HU-14).
-- Regla de negocio: todos los items de una orden son del agricultor indicado.
CREATE TABLE orden_compra (
    id              BIGINT GENERATED ALWAYS AS IDENTITY,
    comprador_id    BIGINT      NOT NULL,
    agricultor_id   BIGINT      NOT NULL,
    estado          VARCHAR(25) NOT NULL DEFAULT 'PENDIENTE',
    creado_en       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_orden_compra            PRIMARY KEY (id),
    CONSTRAINT fk_orden_compra_comprador  FOREIGN KEY (comprador_id)
        REFERENCES comprador (usuario_id) ON DELETE RESTRICT,
    CONSTRAINT fk_orden_compra_agricultor FOREIGN KEY (agricultor_id)
        REFERENCES agricultor (usuario_id) ON DELETE RESTRICT,
    CONSTRAINT ck_orden_compra_estado     CHECK (estado IN
        ('PENDIENTE', 'ALISTADA', 'DESPACHO_PROGRAMADO', 'ENTREGADA', 'CANCELADA')),
    CONSTRAINT ck_orden_compra_partes     CHECK (comprador_id <> agricultor_id)
);

-- orden_item: resuelve la relacion N:M entre orden y producto. El precio se copia
-- al momento de la compra para conservar el historial (HU-03, HU-14).
CREATE TABLE orden_item (
    orden_id         BIGINT        NOT NULL,
    producto_id      BIGINT        NOT NULL,
    cantidad         NUMERIC(10,2) NOT NULL,
    precio_unitario  NUMERIC(12,2) NOT NULL,
    CONSTRAINT pk_orden_item          PRIMARY KEY (orden_id, producto_id),
    CONSTRAINT fk_orden_item_orden    FOREIGN KEY (orden_id)
        REFERENCES orden_compra (id) ON DELETE RESTRICT,
    CONSTRAINT fk_orden_item_producto FOREIGN KEY (producto_id)
        REFERENCES producto (id) ON DELETE RESTRICT,
    CONSTRAINT ck_orden_item_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_orden_item_precio   CHECK (precio_unitario > 0)
);

-- despacho: programacion de entrega de una orden, 0..1 por orden (HU-10).
CREATE TABLE despacho (
    orden_id                BIGINT NOT NULL,
    fecha_hora_programada   TIMESTAMP WITH TIME ZONE NOT NULL,
    creado_en               TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_despacho       PRIMARY KEY (orden_id),
    CONSTRAINT fk_despacho_orden FOREIGN KEY (orden_id)
        REFERENCES orden_compra (id) ON DELETE RESTRICT
);

-- ---------------------------------------------------------------------------
-- 5. COMUNICACION
-- ---------------------------------------------------------------------------

-- mensaje_contacto: intencion de compra del comprador hacia el agricultor (HU-05).
CREATE TABLE mensaje_contacto (
    id            BIGINT GENERATED ALWAYS AS IDENTITY,
    producto_id   BIGINT        NOT NULL,
    comprador_id  BIGINT        NOT NULL,
    mensaje       VARCHAR(1000) NOT NULL,
    creado_en     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_mensaje_contacto           PRIMARY KEY (id),
    CONSTRAINT fk_mensaje_contacto_producto  FOREIGN KEY (producto_id)
        REFERENCES producto (id) ON DELETE RESTRICT,
    CONSTRAINT fk_mensaje_contacto_comprador FOREIGN KEY (comprador_id)
        REFERENCES comprador (usuario_id) ON DELETE RESTRICT,
    CONSTRAINT ck_mensaje_contacto_mensaje   CHECK (BTRIM(mensaje) <> '')
);

-- notificacion: aviso al usuario destinatario por pedidos o contactos (HU-05, HU-12).
CREATE TABLE notificacion (
    id          BIGINT GENERATED ALWAYS AS IDENTITY,
    usuario_id  BIGINT       NOT NULL,
    orden_id    BIGINT,                              -- nulo si no se relaciona con un pedido
    tipo        VARCHAR(25)  NOT NULL,
    mensaje     VARCHAR(500) NOT NULL,
    leida       BOOLEAN      NOT NULL DEFAULT FALSE,
    creada_en   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_notificacion         PRIMARY KEY (id),
    CONSTRAINT fk_notificacion_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuario (id) ON DELETE RESTRICT,
    CONSTRAINT fk_notificacion_orden   FOREIGN KEY (orden_id)
        REFERENCES orden_compra (id) ON DELETE RESTRICT,
    CONSTRAINT ck_notificacion_tipo    CHECK (tipo IN ('NUEVA_ORDEN', 'ORDEN_ACTUALIZADA', 'CONTACTO'))
);

-- ---------------------------------------------------------------------------
-- 6. INDICES
-- PostgreSQL no indexa las llaves foraneas automaticamente; estos indices
-- cubren las consultas de los filtros y listados de las historias.
-- ---------------------------------------------------------------------------
CREATE INDEX idx_usuario_rol              ON usuario (rol_id);
CREATE INDEX idx_agricultor_municipio     ON agricultor (municipio_id);
CREATE INDEX idx_comprador_municipio       ON comprador (municipio_id);
CREATE INDEX idx_finca_municipio          ON finca (municipio_id);
CREATE INDEX idx_tipo_producto_categoria  ON tipo_producto (categoria_id);
CREATE INDEX idx_producto_agricultor      ON producto (agricultor_id);
CREATE INDEX idx_producto_tipo            ON producto (tipo_producto_id, activo);   -- HU-04
CREATE INDEX idx_producto_precio          ON producto (precio_unitario);            -- HU-15
CREATE INDEX idx_orden_compra_comprador   ON orden_compra (comprador_id);           -- HU-11, HU-14
CREATE INDEX idx_orden_compra_agricultor  ON orden_compra (agricultor_id, estado);  -- HU-09, HU-10
CREATE INDEX idx_orden_item_producto      ON orden_item (producto_id);
CREATE INDEX idx_mensaje_contacto_producto ON mensaje_contacto (producto_id);
CREATE INDEX idx_notificacion_usuario     ON notificacion (usuario_id, leida);      -- HU-12

-- ---------------------------------------------------------------------------
-- 7. DATOS INICIALES (catalogos)
-- Las categorias y tipos de producto son ejemplos: ajustarlos con el Product Owner.
-- ---------------------------------------------------------------------------
INSERT INTO rol (nombre) VALUES ('AGRICULTOR'), ('COMPRADOR');

INSERT INTO municipio (nombre) VALUES
    ('Alcalá'), ('Andalucía'), ('Ansermanuevo'), ('Argelia'), ('Bolívar'),
    ('Buenaventura'), ('Buga'), ('Bugalagrande'), ('Caicedonia'), ('Cali'),
    ('Calima'), ('Candelaria'), ('Cartago'), ('Dagua'), ('El Águila'),
    ('El Cairo'), ('El Cerrito'), ('El Dovio'), ('Florida'), ('Ginebra'),
    ('Guacarí'), ('Jamundí'), ('La Cumbre'), ('La Unión'), ('La Victoria'),
    ('Obando'), ('Palmira'), ('Pradera'), ('Restrepo'), ('Riofrío'),
    ('Roldanillo'), ('San Pedro'), ('Sevilla'), ('Toro'), ('Trujillo'),
    ('Tuluá'), ('Ulloa'), ('Versalles'), ('Vijes'), ('Yotoco'),
    ('Yumbo'), ('Zarzal');

INSERT INTO categoria (nombre) VALUES
    ('Frutas'), ('Verduras'), ('Tubérculos'), ('Granos y cereales'), ('Café');

INSERT INTO tipo_producto (nombre, categoria_id)
SELECT v.nombre, c.id
FROM (VALUES
    ('Mango', 'Frutas'), ('Aguacate', 'Frutas'), ('Banano', 'Frutas'), ('Piña', 'Frutas'),
    ('Lulo', 'Frutas'), ('Maracuyá', 'Frutas'), ('Papaya', 'Frutas'),
    ('Tomate', 'Verduras'), ('Cebolla', 'Verduras'), ('Zanahoria', 'Verduras'), ('Pimentón', 'Verduras'),
    ('Yuca', 'Tubérculos'), ('Papa', 'Tubérculos'),
    ('Maíz', 'Granos y cereales'), ('Fríjol', 'Granos y cereales'), ('Arroz', 'Granos y cereales'),
    ('Café', 'Café')
) AS v (nombre, categoria)
JOIN categoria c ON c.nombre = v.categoria;
