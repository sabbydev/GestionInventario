-- ============================================================================
-- RESET: crear base desde cero
-- ============================================================================
DROP DATABASE IF EXISTS inventario_automotriz;
CREATE DATABASE inventario_automotriz
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
USE inventario_automotriz;

-- ============================================================================
-- TABLA: roles
-- ============================================================================
CREATE TABLE roles (
  id_rol        INT AUTO_INCREMENT PRIMARY KEY,
  nombre        VARCHAR(50) NOT NULL UNIQUE,
  descripcion   VARCHAR(150)
) ENGINE=InnoDB;

-- ============================================================================
-- TABLA: usuarios
-- ============================================================================
CREATE TABLE usuarios (
  id_usuario        INT AUTO_INCREMENT PRIMARY KEY,
  nombre            VARCHAR(100) NOT NULL,
  correo            VARCHAR(120) NOT NULL UNIQUE,
  contraseña        VARCHAR(255) NOT NULL,
  id_rol            INT NOT NULL,
  estado            TINYINT NOT NULL DEFAULT 1,       -- 1=activo, 0=inactivo
  fecha_creacion    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_usuarios_roles
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
      ON UPDATE CASCADE
      ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX idx_usuarios_estado ON usuarios (estado);
CREATE INDEX idx_usuarios_idrol  ON usuarios (id_rol);

-- ============================================================================
-- TABLA: categorias
-- ============================================================================
CREATE TABLE categorias (
  id_categoria   INT AUTO_INCREMENT PRIMARY KEY,
  nombre         VARCHAR(50) NOT NULL UNIQUE,
  descripcion    VARCHAR(150)
) ENGINE=InnoDB;

-- ============================================================================
-- TABLA: proveedores
-- ============================================================================
CREATE TABLE proveedores (
  id_proveedor    INT AUTO_INCREMENT PRIMARY KEY,
  nombre          VARCHAR(100) NOT NULL,
  contacto        VARCHAR(100),
  direccion       VARCHAR(255),
  telefono        VARCHAR(20),
  correo          VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- ============================================================================
-- TABLA: productos
-- ============================================================================
CREATE TABLE productos (
  id_producto   INT AUTO_INCREMENT PRIMARY KEY,
  nombre        VARCHAR(100) NOT NULL,
  descripcion   VARCHAR(255),
  precio        DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  stock         INT NOT NULL DEFAULT 0,
  imagen_url    VARCHAR(255),
  imagen_data   LONGBLOB,
  imagen_mime   VARCHAR(100),
  ficha_tecnica_url VARCHAR(255),
  ficha_tecnica_data LONGBLOB,
  ficha_tecnica_mime VARCHAR(100),
  manual_url    VARCHAR(255),
  manual_data   LONGBLOB,
  manual_mime   VARCHAR(100),
  id_categoria  INT,
  id_proveedor  INT,
  CONSTRAINT fk_productos_categorias
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
      ON UPDATE CASCADE
      ON DELETE SET NULL,
  CONSTRAINT fk_productos_proveedores
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor)
      ON UPDATE CASCADE
      ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE INDEX idx_productos_categoria ON productos (id_categoria);
CREATE INDEX idx_productos_nombre    ON productos (nombre);
CREATE INDEX idx_productos_proveedor ON productos (id_proveedor);

-- ============================================================================
-- TABLA: movimientos (kardex simple)
-- ============================================================================
CREATE TABLE movimientos (
  id_movimiento   INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario      INT NOT NULL,
  id_producto     INT NOT NULL,
  tipo            ENUM('entrada','salida','transferencia') NOT NULL,
  fecha           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  cantidad        INT NOT NULL,
  CONSTRAINT chk_mov_cantidad CHECK (cantidad > 0),
  CONSTRAINT fk_mov_usuarios
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
      ON UPDATE CASCADE
      ON DELETE RESTRICT,
  CONSTRAINT fk_mov_productos
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
      ON UPDATE CASCADE
      ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX idx_movimientos_producto ON movimientos (id_producto);
CREATE INDEX idx_movimientos_usuario  ON movimientos (id_usuario);

-- ============================================================================
-- DATOS INICIALES
-- ============================================================================
INSERT INTO roles (nombre, descripcion) VALUES
  ('administrador', 'Control total del sistema'),
  ('operador',      'Gestiona productos y movimientos'),
  ('gerente',       'Accede a reportes y análisis');

-- Usuarios (contraseñas en texto plano, NO seguro en producción)
INSERT INTO usuarios (nombre, correo, contraseña, id_rol, estado) VALUES
  ('Administrador', 'admin@demo.local', '1234', 1, 1),
  ('Operador Uno',  'operador1@demo.local','abcd', 2, 1),
  ('Gerente Uno',   'gerente1@demo.local', 'qwerty', 3, 1);

INSERT INTO categorias (nombre, descripcion) VALUES
  ('motor',      'Repuestos relacionados al motor'),
  ('frenos',     'Componentes del sistema de frenos'),
  ('suspension', 'Partes de la suspensión y dirección');

-- Inserción de proveedores
INSERT INTO proveedores (nombre, contacto, direccion, telefono, correo) VALUES
  ('Proveedor A', 'Juan Pérez', 'Av. Los Olivos 123, Lima', '987654321', 'proveedora@demo.local'),
  ('Proveedor B', 'Carlos Sánchez', 'Calle Sol 456, Lima', '982345678', 'proveedorb@demo.local');

-- Inserción de productos con asignación de proveedor
INSERT INTO productos (nombre, descripcion, precio, stock, id_categoria, id_proveedor) VALUES
  ('filtro de aceite',   'Filtro para motor 1.6L',            35.00, 50, 1, 1),
  ('pastillas de freno', 'Juego delantero estándar',         120.00, 30, 2, 2),
  ('amortiguador',       'Amortiguador hidráulico delantero',250.00, 20, 3, 1);

-- Inserción de movimientos
INSERT INTO movimientos (id_usuario, id_producto, tipo, cantidad) VALUES
  (1, 1, 'entrada', 20),
  (2, 2, 'salida',  5),
  (3, 3, 'entrada', 10);
