CREATE TABLE libro(
	isbn INTEGER PRIMARY KEY,
	editorial VARCHAR(50),
	titulo VARCHAR(50),
	pais_de_publicacion VARCHAR(50),
	precio REAL,
	numero_paginas INTEGER,
	numero_de_edicion INTEGER,
	# formato, # Si solo se permiten libros digitales no hay problemas, pero si se permiten lbros hay que diferenciar
	lengua VARCHAR(50),
	descricion VARCHAR(1000),
	titulo_original VARCHAR(50),
	fecha_de_publicacion DATE
);

CREATE TABLE pertenece_al_genero(
	libro INTEGER REFERENCES libro(isbn),
	genero VARCHAR(50) REFERENCES genero(nombre),
	PRIMARY KEY(libro,genero)
);

CREATE TABLE genero(
	nombre VARCHAR(50) PRIMARY KEY
);

CREATE TABLE libro_en_oferta(
	libro INTEGER REFERENCES libro(isbn) PRIMARY KEY ON DELETE CASCADE,
	porcentaje_rebaja REAL NOT NULL CHECK ((porcentaje_rebaja > 0.0) AND (porcentaje_rebaja < 100.0))
);

CREATE TABLE escrito_por(
	libro INTEGER REFERENCES libro(isbn),
	autor INTEGER REFERENCES autor(id),
	PRIMARY KEY(libro,autor)
);

CREATE TABLE autor(
	nombre_completo VARCHAR(150) PRIMARY KEY, # Suponemos que solo hay un autor del mismo nombre
	pais_de_nacimiento VARCHAR(50),
	descripcion VARCHAR(1000)
);

CREATE TABLE usuario( 
	# Datos de cuenta
	nombre_de_usuario VARCHAR(50) PRIMARY KEY,
	password VARCHAR(1024) NOT NULL, # Se guardará con un cifrado asimetrico para asegurar la seguridad

	 # Datos personales
	 # Los datos como el email, telefono, direccion y numero de cuenta se guardaran con cifrado simetrico
	 # por cuestiones de privacidad
	nombre VARCHAR(50) NOT NULL,
	apellidos VARCHAR(100) NOT NULL,
	fecha_de_nacimiento DATE,
	email VARCHAR(50) NOT NULL,
	direccion VARCHAR(50),
	numero_de_telefono INTEGER,
	numero_de_cuenta_bancaria VARCHAR(1024)
);

CREATE TABLE puntua(
	usuario VARCHAR(50) REFERENCES usuario(nombre_de_usuario),
	libro INTEGER REFERENCES libro(isbn),
	puntuacion INTEGER NOT NULL,
	fecha DATE NOT NULL,
	PRIMARY KEY(usuario,libro)
);

CREATE TABLE visita( 
# Esta tabla se actualizará muchas veces, hay que asegurar su rapidez de actualizacion.
# Posiblemente lo mejor sea durante la sesion gurardar en local los libros que se leen y 
# despues al salir de la sesion actualizar la tabla
	usuario VARCHAR(50) REFERENCES usuario(nombre_de_usuario),
	libro INTEGER REFERENCES libro(isbn),
	fecha DATE NOT NULL,
	PRIMARY KEY(usuario,libro,fecha)
);

CREATE TABLE compra(
	usuario VARCHAR(50) REFERENCES usuario(nombre_de_usuario),
	libro INTEGER REFERENCES libro(isbn),
	puntuacion INTEGER NOT NULL,
	fecha DATE NOT NULL,
	precio REAL NOT NULL,
	# Metodo de pago o mas info sobre la compra
	PRIMARY KEY(usuario,libro)
);

CREATE TABLE comenta( 
	usuario VARCHAR(50) REFERENCES usuario(nombre_de_usuario),
	libro INTEGER REFERENCES libro(isbn),
	fecha DATE NOT NULL,
	comentario VARCHAR(1000) NOT NULL,
	PRIMARY KEY(usuario,libro,fecha)
);