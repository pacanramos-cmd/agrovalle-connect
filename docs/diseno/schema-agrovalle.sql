-- =============================================================================
-- AgroValle Connect - Modelo de datos relacional (3FN)
-- Alcance: Sprint 1 -> HU-01 (registro de agricultores) y HU-13 (inicio de sesion)
-- Motor:   PostgreSQL 15/16
-- Base:    agrovalle_db  (nombre indicado en la guia; es el valor de DB_NAME)
--
-- Uso (una sola vez, con un usuario con permisos de creacion):
--   1) CREATE DATABASE agrovalle_db ENCODING 'UTF8';
--   2) psql -U <usuario> -d agrovalle_db -f docs/diseno/schema-agrovalle.sql
--
-- Nota: application.properties usa spring.jpa.hibernate.ddl-auto=validate, por lo
-- que Hibernate NO crea tablas: este script es quien las crea y las entidades JPA
-- deben coincidir con estos tipos y nombres.
-- =============================================================================

-- ---------------------------------------------------------------------------
-- rol: catalogo de roles (el JWT de HU-13 lleva el rol del usuario)
-- ---------------------------------------------------------------------------
CREATE TABLE rol (
    id      BIGINT GENERATED ALWAYS AS IDENTITY,
    nombre  VARCHAR(30) NOT NULL,
    CONSTRAINT pk_rol        PRIMARY KEY (id),
    CONSTRAINT uq_rol_nombre UNIQUE (nombre)
);

-- ---------------------------------------------------------------------------
-- municipio: catalogo de municipios del Valle del Cauca
-- (evita repetir el texto de la ubicacion en cada agricultor -> 3FN)
-- ---------------------------------------------------------------------------
CREATE TABLE municipio (
    id      BIGINT GENERATED ALWAYS AS IDENTITY,
    nombre  VARCHAR(60) NOT NULL,
    CONSTRAINT pk_municipio        PRIMARY KEY (id),
    CONSTRAINT uq_municipio_nombre UNIQUE (nombre)
);

-- ---------------------------------------------------------------------------
-- agricultor: usuario que se registra (HU-01) e inicia sesion (HU-13)
-- ---------------------------------------------------------------------------
CREATE TABLE agricultor (
    id               BIGINT GENERATED ALWAYS AS IDENTITY,
    nombre           VARCHAR(120) NOT NULL,
    cedula           VARCHAR(10)  NOT NULL,
    correo           VARCHAR(150) NOT NULL,
    contrasena_hash  VARCHAR(100) NOT NULL,          -- BCrypt, nunca texto plano
    municipio_id     BIGINT       NOT NULL,
    rol_id           BIGINT       NOT NULL,
    creado_en        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_agricultor           PRIMARY KEY (id),
    CONSTRAINT uq_agricultor_cedula    UNIQUE (cedula),
    CONSTRAINT uq_agricultor_correo    UNIQUE (correo),
    CONSTRAINT fk_agricultor_municipio FOREIGN KEY (municipio_id)
        REFERENCES municipio (id) ON DELETE RESTRICT,
    CONSTRAINT fk_agricultor_rol       FOREIGN KEY (rol_id)
        REFERENCES rol (id) ON DELETE RESTRICT,
    CONSTRAINT ck_agricultor_nombre    CHECK (BTRIM(nombre) <> ''),
    CONSTRAINT ck_agricultor_cedula    CHECK (cedula ~ '^[0-9]{6,10}$'),
    CONSTRAINT ck_agricultor_correo    CHECK (correo = LOWER(correo))
);

-- PostgreSQL no indexa las llaves foraneas automaticamente.
CREATE INDEX idx_agricultor_municipio ON agricultor (municipio_id);
CREATE INDEX idx_agricultor_rol       ON agricultor (rol_id);

-- ---------------------------------------------------------------------------
-- Datos iniciales (catalogos)
-- ---------------------------------------------------------------------------
INSERT INTO rol (nombre) VALUES ('AGRICULTOR');

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
