-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Server version: 10.1.17-MariaDB
-- PHP Version: 5.5.36

SET
  FOREIGN_KEY_CHECKS = 0;
SET
  SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";


--
-- Database: `UVGDB`
--
CREATE DATABASE IF NOT EXISTS `UVGDB` DEFAULT CHARACTER SET utf8;
USE
  `UVGDB`;


-- --------------------------------------------------------

--
-- Table structure for table `Administradores`
--

CREATE TABLE `Administradores`(
  `SQLID` INT(11) UNSIGNED NOT NULL COMMENT 'Clave principal con autoincremento.',
  `USUARIO` VARBINARY(64) NOT NULL COMMENT 'Nombre de usuario del Administrador',
  `CLAVE` VARBINARY(64) NOT NULL COMMENT 'HASH de la contraseña actual del Administrador'
) ENGINE = InnoDB DEFAULT CHARSET = utf8;


-- --------------------------------------------------------

--
-- Table structure for table `Alumnos`
--

CREATE TABLE `Alumnos`(
  `SQLID` INT(11) UNSIGNED NOT NULL COMMENT 'Clave principal con autoincremento.',
  `ID` INT(11) UNSIGNED NOT NULL COMMENT 'Carnet o identificador de la Universidad.',
  `NOMBRES` VARCHAR(40) NOT NULL COMMENT 'Nombres del Alumno.',
  `APELLIDO1` VARCHAR(20) NOT NULL COMMENT 'Primer apellido del Alumno.',
  `APELLIDO2` VARCHAR(20) NOT NULL COMMENT 'Segundo apellido del Alumno.',
  `CORREO_USR` VARCHAR(50) NOT NULL COMMENT 'Nombre de usuario de correo electrónico personal del Alumno.',
  `CORREO_HST` VARCHAR(25) NOT NULL COMMENT 'Host de correo electrónico personal del Alumno.',
  `CLAVE` VARBINARY(64) NOT NULL COMMENT 'HASH de la contraseña actual del Alumno',
  `CORREO_UVG` VARCHAR(20) NOT NULL COMMENT 'Nombre de usuario de correo electrónico de la UVG.'
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 'Tabla que almacena todos los Alumnos.';


-- --------------------------------------------------------

--
-- Table structure for table `Alumnos-Cursos-Aprobados`
--

CREATE TABLE `Alumnos-Cursos-Aprobados`(
  `ALUMNO` INT(11) UNSIGNED NOT NULL COMMENT 'El Alumno con su respectivo Curso aprobado.',
  `CURSO` VARCHAR(8) NOT NULL COMMENT 'Identificador principal del curso.',
  `SECCION` TINYINT(3) UNSIGNED NOT NULL COMMENT 'Sección del Curso.'
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 'Tabla que relaciona cada Alumno con sus respectivos Cursos aprobados.';


-- --------------------------------------------------------

--
-- Table structure for table `Alumnos-Cursos-Corrientes`
--

CREATE TABLE `Alumnos-Cursos-Corrientes`(
  `ALUMNO` INT(11) UNSIGNED NOT NULL COMMENT 'El Alumno con su respectivo Curso corriente.',
  `CURSO` VARCHAR(8) NOT NULL COMMENT 'Identificador principal del curso.',
  `SECCION` TINYINT(3) UNSIGNED NOT NULL COMMENT 'Sección del Curso.'
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 'Tabla que relaciona cada Alumno con sus respectivos Cursos corrientes.';


-- --------------------------------------------------------

--
-- Table structure for table `Alumnos-Cursos-Reprobados`
--

CREATE TABLE `Alumnos-Cursos-Reprobados`(
  `ALUMNO` INT(11) UNSIGNED NOT NULL COMMENT 'El Alumno con su respectivo Curso reprobado.',
  `CURSO` VARCHAR(8) NOT NULL COMMENT 'Identificador principal del curso.',
  `SECCION` TINYINT(3) UNSIGNED NOT NULL COMMENT 'Sección del Curso.'
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 'Tabla que relaciona cada Alumno con sus respectivos Cursos reprobados.';


-- --------------------------------------------------------

--
-- Table structure for table `Auxiliares-Cursos`
--

CREATE TABLE `Auxiliares-Cursos`(
  `AUXILIAR` INT(11) UNSIGNED NOT NULL COMMENT 'El Auxiliar con su respectivo Curso de auxiliatura.',
  `CURSO` VARCHAR(8) NOT NULL COMMENT 'Identificador principal del curso.',
  `SECCION` TINYINT(3) UNSIGNED NOT NULL COMMENT 'Sección del Curso.'
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 'Tabla que relaciona cada Auxiliar con sus respectivos Cursos.';


-- --------------------------------------------------------

--
-- Table structure for table `Catedraticos`
--

CREATE TABLE `Catedraticos`(
  `SQLID` INT(11) UNSIGNED NOT NULL COMMENT 'Clave principal con autoincremento.',
  `ID` MEDIUMINT(7) UNSIGNED NOT NULL COMMENT 'Carnet o identificador de la Universidad.',
  `NOMBRES` VARCHAR(40) NOT NULL COMMENT 'Nombres del Catedrático.',
  `APELLIDO1` VARCHAR(20) NOT NULL COMMENT 'Primer apellido del Catedrático.',
  `APELLIDO2` VARCHAR(20) NOT NULL COMMENT 'Segundo apellido del Catedrático.',
  `CORREO_USR` VARCHAR(50) NOT NULL COMMENT 'Nombre de usuario de correo electrónico personal del Catedrático.',
  `CORREO_HST` VARCHAR(25) NOT NULL COMMENT 'Host de correo electrónico personal del Catedrático.',
  `CLAVE` VARBINARY(64) NOT NULL COMMENT 'HASH de la contraseña actual del Alumno',
  `CORREO_UVG` VARCHAR(20) NOT NULL COMMENT 'Nombre de usuario de correo electrónico de la UVG.'
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 'Tabla que almacena todos los Catedráticos.';


-- --------------------------------------------------------

--
-- Table structure for table `Cursos`
--

CREATE TABLE `Cursos`(
  `SQLID` INT(11) UNSIGNED NOT NULL COMMENT 'Clave principal con autoincremento.',
  `ID` VARCHAR(8) NOT NULL COMMENT 'Identificador principal del curso.',
  `SECCION` TINYINT(3) UNSIGNED NOT NULL COMMENT 'Sección del Curso.',
  `CATEDRATICO` INT(11) UNSIGNED NOT NULL COMMENT 'Catedrático que imparte el Curso.',
  `NOMBRE` VARCHAR(100) NOT NULL COMMENT 'Nombre completo del Curso'
) ENGINE = InnoDB DEFAULT CHARSET = utf8;


-- --------------------------------------------------------

--
-- Table structure for table `Tutores-Cursos`
--

CREATE TABLE `Tutores-Cursos`(
  `TUTOR` INT(11) UNSIGNED NOT NULL COMMENT 'El Auxiliar con su respectivo Curso de auxiliatura.',
  `CURSO` VARCHAR(8) NOT NULL COMMENT 'Identificador principal del curso.'
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 'Tabla que relaciona cada Tutor con sus respectivos Cursos de tutoría.';


--
-- Indexes for dumped tables
--

--
-- Indexes for table `Administradores`
--
ALTER TABLE
  `Administradores` ADD PRIMARY KEY(`SQLID`) COMMENT 'Clave principal que sirve como identificador único en SQL.',
  ADD UNIQUE KEY `UID`(`USUARIO`) COMMENT 'Identificador único del Administrador';


--
-- Indexes for table `Alumnos`
--
ALTER TABLE
  `Alumnos` ADD PRIMARY KEY(`SQLID`) COMMENT 'Clave principal que sirve como índice en SQL.',
  ADD UNIQUE KEY `CORREO`(`CORREO_USR`, `CORREO_HST`) COMMENT 'Ningún Alumno puede tener el mismo correo electrónico que otro Alumno.',
  ADD UNIQUE KEY `CORREO_UVG`(`CORREO_UVG`) COMMENT 'Ningún Alumno puede tener el mismo correo electrónico de la Universidad que otro Alumno.',
  ADD UNIQUE KEY `UID`(`ID`) COMMENT 'Identificador único de la Universidad.';


--
-- Indexes for table `Alumnos-Cursos-Aprobados`
--
ALTER TABLE
  `Alumnos-Cursos-Aprobados` ADD UNIQUE KEY `UNICO_CURSO`(`ALUMNO`, `CURSO`) COMMENT 'Evita la doble asignación a un curso en otra sección.',
  ADD UNIQUE KEY `UNICA_SECCION`(`ALUMNO`, `CURSO`, `SECCION`) COMMENT 'Evita la doble asignación a un curso en la misma sección.',
  ADD KEY `Sección a Curso/Alumno Aprobados`(`SECCION`),
  ADD KEY `Curso a Sección/Alumno Aprobados`(`CURSO`);


--
-- Indexes for table `Alumnos-Cursos-Corrientes`
--
ALTER TABLE
  `Alumnos-Cursos-Corrientes` ADD UNIQUE KEY `UNICO_CURSO`(`ALUMNO`, `CURSO`) COMMENT 'Evita la doble asignación a un curso en otra sección.',
  ADD UNIQUE KEY `UNICA_SECCION`(`ALUMNO`, `CURSO`, `SECCION`) COMMENT 'Evita la doble asignación a un curso en la misma sección.',
  ADD KEY `Sección a Curso/Alumno Corriente`(`SECCION`),
  ADD KEY `Curso a Sección/Alumno Corriente`(`CURSO`);


--
-- Indexes for table `Alumnos-Cursos-Reprobados`
--
ALTER TABLE
  `Alumnos-Cursos-Reprobados` ADD UNIQUE KEY `UNICO_CURSO`(`ALUMNO`, `CURSO`) COMMENT 'Evita la doble asignación a un curso en otra sección.',
  ADD UNIQUE KEY `UNICA_SECCION`(`ALUMNO`, `CURSO`, `SECCION`) COMMENT 'Evita la doble asignación a un curso en la misma sección.',
  ADD KEY `Sección a Curso/Alumno Reprobados`(`SECCION`),
  ADD KEY `Curso a Sección/Alumno Reprobados`(`CURSO`);


--
-- Indexes for table `Auxiliares-Cursos`
--
ALTER TABLE
  `Auxiliares-Cursos` ADD UNIQUE KEY `UNICO_CURSO`(`AUXILIAR`, `CURSO`) COMMENT 'Evita la doble asignación a un curso en otra sección.',
  ADD UNIQUE KEY `UNICA_SECCION`(`AUXILIAR`, `CURSO`, `SECCION`) COMMENT 'Evita la doble asignación a un curso en la misma sección.',
  ADD KEY `Sección a Curso/Auxiliar`(`SECCION`),
  ADD KEY `Curso a Sección/Auxiliar`(`CURSO`);


--
-- Indexes for table `Catedraticos`
--
ALTER TABLE
  `Catedraticos` ADD PRIMARY KEY(`SQLID`) COMMENT 'Clave principal que sirve como índice en SQL.',
  ADD UNIQUE KEY `CORREO`(`CORREO_USR`, `CORREO_HST`) COMMENT 'Ningún Catedrático puede tener el mismo correo electrónico que otro Catedrático.',
  ADD UNIQUE KEY `CORREO_UVG`(`CORREO_UVG`) COMMENT 'Ningún Catedrático puede tener el mismo correo electrónico de la Universidad que otro Catedrático.',
  ADD UNIQUE KEY `UID`(`ID`) COMMENT 'Identificador único de la Universidad.';


--
-- Indexes for table `Cursos`
--
ALTER TABLE
  `Cursos` ADD PRIMARY KEY(`SQLID`) COMMENT 'Identificador SQL del Curso.',
  ADD UNIQUE KEY `UID`(`ID`, `SECCION`) COMMENT 'Identificador único del curso.',
  ADD UNIQUE KEY `SQLID`(`SQLID`) COMMENT 'Identificador único de la Base de Datos',
  ADD KEY `CATEDRATICO`(`CATEDRATICO`),
  ADD KEY `SECCION`(`SECCION`);


--
-- Indexes for table `Tutores-Cursos`
--
ALTER TABLE
  `Tutores-Cursos` ADD UNIQUE KEY `UNICO_CURSO`(`TUTOR`, `CURSO`) COMMENT 'Evita la doble asignación de un tutor a un curso.',
  ADD KEY `Curso a Tutor`(`CURSO`);


--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Administradores`
--
ALTER TABLE
  `Administradores` MODIFY `SQLID` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Clave principal con autoincremento.';

--
-- AUTO_INCREMENT for table `Alumnos`
--
ALTER TABLE
  `Alumnos` MODIFY `SQLID` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Clave principal con autoincremento.';

--
-- AUTO_INCREMENT for table `Catedraticos`
--
ALTER TABLE
  `Catedraticos` MODIFY `SQLID` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Clave principal con autoincremento.';

--
-- AUTO_INCREMENT for table `Cursos`
--
ALTER TABLE
  `Cursos` MODIFY `SQLID` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Clave principal con autoincremento.';

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Alumnos-Cursos-Aprobados`
--
ALTER TABLE
  `Alumnos-Cursos-Aprobados` ADD CONSTRAINT `Alumno a Sección/Curso Aprobados` FOREIGN KEY(`ALUMNO`) REFERENCES `Alumnos`(`ID`),
  ADD CONSTRAINT `Curso a Sección/Alumno Aprobados` FOREIGN KEY(`CURSO`) REFERENCES `Cursos`(`ID`),
  ADD CONSTRAINT `Sección a Curso/Alumno Aprobados` FOREIGN KEY(`SECCION`) REFERENCES `Cursos`(`SECCION`);


--
-- Constraints for table `Alumnos-Cursos-Corrientes`
--
ALTER TABLE
  `Alumnos-Cursos-Corrientes` ADD CONSTRAINT `Alumno a Sección/Curso Corriente` FOREIGN KEY(`ALUMNO`) REFERENCES `Alumnos`(`ID`),
  ADD CONSTRAINT `Curso a Sección/Alumno Corriente` FOREIGN KEY(`CURSO`) REFERENCES `Cursos`(`ID`),
  ADD CONSTRAINT `Sección a Curso/Alumno Corriente` FOREIGN KEY(`SECCION`) REFERENCES `Cursos`(`SECCION`);


--
-- Constraints for table `Alumnos-Cursos-Reprobados`
--
ALTER TABLE
  `Alumnos-Cursos-Reprobados` ADD CONSTRAINT `Alumno a Sección/Curso Reprobados` FOREIGN KEY(`ALUMNO`) REFERENCES `Alumnos`(`ID`),
  ADD CONSTRAINT `Curso a Sección/Alumno Reprobados` FOREIGN KEY(`CURSO`) REFERENCES `Cursos`(`ID`),
  ADD CONSTRAINT `Sección a Curso/Alumno Reprobados` FOREIGN KEY(`SECCION`) REFERENCES `Cursos`(`SECCION`);


--
-- Constraints for table `Auxiliares-Cursos`
--
ALTER TABLE
  `Auxiliares-Cursos` ADD CONSTRAINT `Auxiliar a Sección/Curso` FOREIGN KEY(`AUXILIAR`) REFERENCES `Alumnos`(`ID`),
  ADD CONSTRAINT `Curso a Sección/Auxiliar` FOREIGN KEY(`CURSO`) REFERENCES `Cursos`(`ID`),
  ADD CONSTRAINT `Sección a Curso/Auxiliar` FOREIGN KEY(`SECCION`) REFERENCES `Cursos`(`SECCION`);


--
-- Constraints for table `Cursos`
--
ALTER TABLE
  `Cursos` ADD CONSTRAINT `Catedráticos a Cursos` FOREIGN KEY(`CATEDRATICO`) REFERENCES `Catedraticos`(`SQLID`);


--
-- Constraints for table `Tutores-Cursos`
--
ALTER TABLE
  `Tutores-Cursos` ADD CONSTRAINT `Curso a Tutor` FOREIGN KEY(`CURSO`) REFERENCES `Cursos`(`ID`),
  ADD CONSTRAINT `Tutor a Curso` FOREIGN KEY(`TUTOR`) REFERENCES `Alumnos`(`ID`);
SET
  FOREIGN_KEY_CHECKS = 1;
