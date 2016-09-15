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

DROP TABLE IF EXISTS
  `Administradores`;
CREATE TABLE `Administradores`(
  `SQLID` TINYINT(3) UNSIGNED NOT NULL COMMENT 'Clave principal con autoincremento.',
  `USUARIO` VARBINARY(64) NOT NULL COMMENT 'Nombre de usuario del Administrador',
  `CLAVE` VARBINARY(64) NOT NULL COMMENT 'HASH de la contraseña actual del Administrador'
) ENGINE = InnoDB DEFAULT CHARSET = utf8;



-- --------------------------------------------------------

--
-- Table structure for table `Alumnos`
--

DROP TABLE IF EXISTS
  `Alumnos`;
CREATE TABLE `Alumnos`(
  `SQLID` MEDIUMINT(7) UNSIGNED NOT NULL COMMENT 'Clave principal con autoincremento.',
  `ID` MEDIUMINT(7) UNSIGNED NOT NULL COMMENT 'Carnet o identificador de la Universidad.',
  `NOMBRES` VARCHAR(40) NOT NULL COMMENT 'Nombres del Alumno.',
  `APELLIDO1` VARCHAR(20) NOT NULL COMMENT 'Primer apellido del Alumno.',
  `APELLIDO2` VARCHAR(20) NOT NULL COMMENT 'Segundo apellido del Alumno.',
  `CORREO_USR` INT(20) NOT NULL COMMENT 'Nombre de usuario de correo electrónico personal
del Alumno.',
  `CORREO_HST` INT(20) NOT NULL COMMENT 'Host de correo electrónico personal del Alumno.',
  `CLAVE` VARBINARY(64) NOT NULL COMMENT 'HASH de la contraseña actual del Alumno',
  `CORREO_UVG` VARCHAR(20) NOT NULL COMMENT 'Nombre de usuario de correo electrónico de la
UVG.'
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 'Tabla que almacena todos los
Alumnos.';



-- --------------------------------------------------------

--
-- Table structure for table `Alumnos-Cursos-Aprobados`
--

DROP TABLE IF EXISTS
  `Alumnos-Cursos-Aprobados`;
CREATE TABLE `Alumnos-Cursos-Aprobados`(
  `Alumno` MEDIUMINT(9) UNSIGNED NOT NULL COMMENT 'El Alumno con su respectivo Curso
aprobado.',
  `Curso` MEDIUMINT(9) UNSIGNED NOT NULL COMMENT 'El Curso aprobado que se relaciona con
el Alumno.'
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 'Tabla que relaciona cada Alumno con
sus respectivos Cursos aprobados.';



-- --------------------------------------------------------

--
-- Table structure for table `Alumnos-Cursos-Corrientes`
--

DROP TABLE IF EXISTS
  `Alumnos-Cursos-Corrientes`;
CREATE TABLE `Alumnos-Cursos-Corrientes`(
  `Alumno` MEDIUMINT(9) UNSIGNED NOT NULL COMMENT 'El Alumno con su respectivo Curso.',
  `Curso` MEDIUMINT(9) UNSIGNED NOT NULL COMMENT 'El Curso que se relaciona con el
Alumno.'
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 'Tabla que relaciona cada Alumno con
sus respectivos Cursos corrientes.';



-- --------------------------------------------------------

--
-- Table structure for table `Alumnos-Cursos-Reprobados`
--

DROP TABLE IF EXISTS
  `Alumnos-Cursos-Reprobados`;
CREATE TABLE `Alumnos-Cursos-Reprobados`(
  `Alumno` MEDIUMINT(9) UNSIGNED NOT NULL COMMENT 'El Alumno con su respectivo Curso
reprobado.',
  `Curso` MEDIUMINT(9) UNSIGNED NOT NULL COMMENT 'El Curso reprobado que se relaciona con
el Alumno.'
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 'Tabla que relaciona cada Alumno con
sus respectivos Cursos reprobados.';



-- --------------------------------------------------------

--
-- Table structure for table `Auxiliares-Cursos`
--

DROP TABLE IF EXISTS
  `Auxiliares-Cursos`;
CREATE TABLE `Auxiliares-Cursos`(
  `Auxiliar` MEDIUMINT(9) UNSIGNED NOT NULL COMMENT 'El Auxiliar con su respectivo
Curso.',
  `Curso` MEDIUMINT(9) UNSIGNED NOT NULL COMMENT 'El Curso que se relaciona con el
Auxiliar.'
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 'Tabla que relaciona cada Auxiliar con
sus respectivos Cursos.';



-- --------------------------------------------------------

--
-- Table structure for table `Catedraticos`
--

DROP TABLE IF EXISTS
  `Catedraticos`;
CREATE TABLE `Catedraticos`(
  `SQLID` MEDIUMINT(7) UNSIGNED NOT NULL COMMENT 'Clave principal con autoincremento.',
  `ID` MEDIUMINT(7) UNSIGNED NOT NULL COMMENT 'Carnet o identificador de la Universidad.',
  `NOMBRES` VARCHAR(40) NOT NULL COMMENT 'Nombres del Catedrático.',
  `APELLIDO1` VARCHAR(20) NOT NULL COMMENT 'Primer apellido del Catedrático.',
  `APELLIDO2` VARCHAR(20) NOT NULL COMMENT 'Segundo apellido del Catedrático.',
  `CORREO_USR` INT(20) NOT NULL COMMENT 'Nombre de usuario de correo electrónico personal
del Catedrático.',
  `CORREO_HST` INT(20) NOT NULL COMMENT 'Host de correo electrónico personal del
Catedrático.',
  `CLAVE` VARBINARY(64) NOT NULL COMMENT 'HASH de la contraseña actual del Alumno',
  `CORREO_UVG` VARCHAR(20) NOT NULL COMMENT 'Nombre de usuario de correo electrónico de la
UVG.'
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 'Tabla que almacena todos los
Catedráticos.';



-- --------------------------------------------------------

--
-- Table structure for table `Cursos`
--

DROP TABLE IF EXISTS
  `Cursos`;
CREATE TABLE `Cursos`(
  `SQLID` MEDIUMINT(8) UNSIGNED NOT NULL COMMENT 'Clave principal con autoincremento.',
  `ID` VARCHAR(8) NOT NULL COMMENT 'Identificador principal del curso.',
  `SECCION` TINYINT(3) UNSIGNED NOT NULL COMMENT 'Sección del Curso.',
  `CATEDRATICO` MEDIUMINT(8) UNSIGNED NOT NULL COMMENT 'Catedrático que imparte el
Curso.',
  `NOMBRE` VARCHAR(100) NOT NULL COMMENT 'Nombre completo del Curso'
) ENGINE = InnoDB DEFAULT CHARSET = utf8;



-- --------------------------------------------------------

--
-- Table structure for table `Tutores-Cursos`
--

DROP TABLE IF EXISTS
  `Tutores-Cursos`;
CREATE TABLE `Tutores-Cursos`(
  `Tutor` MEDIUMINT(9) UNSIGNED NOT NULL COMMENT 'El Tutor con su respectivo Curso.',
  `Curso` MEDIUMINT(9) UNSIGNED NOT NULL COMMENT 'El Curso que se relaciona con el Tutor.'
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = 'Tabla que relaciona cada Tutor con sus
respectivos Cursos de tutoría.';



--
-- Indexes for dumped tables
--

--
-- Indexes for table `Administradores`
--
ALTER TABLE
  `Administradores` ADD PRIMARY KEY(`SQLID`) COMMENT 'Clave principal que sirve como
índice en SQL.',
  ADD UNIQUE KEY `UID`(`USUARIO`) COMMENT 'Identificador único del Administrador',
  ADD UNIQUE KEY `SQLID`(`SQLID`) COMMENT 'Identificador único de la Base de Datos';



--
-- Indexes for table `Alumnos`
--
ALTER TABLE
  `Alumnos` ADD PRIMARY KEY(`SQLID`) COMMENT 'Clave principal que sirve como índice en
SQL.',
  ADD UNIQUE KEY `CORREO`(`CORREO_USR`, `CORREO_HST`) COMMENT 'Ningún Alumno puede tener
el mismo correo electrónico que otro Alumno.',
  ADD UNIQUE KEY `CORREO_UVG`(`CORREO_UVG`) COMMENT 'Ningún Alumno puede tener el mismo
correo electrónico de la Universidad que otro Alumno.',
  ADD UNIQUE KEY `UID`(`ID`) COMMENT 'Identificador único de la Universidad.',
  ADD UNIQUE KEY `SQLID`(`SQLID`) COMMENT 'Identificador único de la Base de Datos';



--
-- Indexes for table `Alumnos-Cursos-Aprobados`
--
ALTER TABLE
  `Alumnos-Cursos-Aprobados` ADD KEY `Curso`(`Curso`),
  ADD KEY `Alumno`(`Alumno`);



--
-- Indexes for table `Alumnos-Cursos-Corrientes`
--
ALTER TABLE
  `Alumnos-Cursos-Corrientes` ADD KEY `Alumno`(`Alumno`),
  ADD KEY `Curso`(`Curso`);



--
-- Indexes for table `Alumnos-Cursos-Reprobados`
--
ALTER TABLE
  `Alumnos-Cursos-Reprobados` ADD KEY `Alumno`(`Alumno`),
  ADD KEY `Curso`(`Curso`);



--
-- Indexes for table `Auxiliares-Cursos`
--
ALTER TABLE
  `Auxiliares-Cursos` ADD KEY `Auxiliar`(`Auxiliar`),
  ADD KEY `Curso`(`Curso`);



--
-- Indexes for table `Catedraticos`
--
ALTER TABLE
  `Catedraticos` ADD PRIMARY KEY(`SQLID`) COMMENT 'Clave principal que sirve como índice
en SQL.',
  ADD UNIQUE KEY `CORREO`(`CORREO_USR`, `CORREO_HST`) COMMENT 'Ningún Catedrático puede
tener el mismo correo electrónico que otro Catedrático.',
  ADD UNIQUE KEY `CORREO_UVG`(`CORREO_UVG`) COMMENT 'Ningún Catedrático puede tener el
mismo correo electrónico de la Universidad que otro Catedrático.',
  ADD UNIQUE KEY `UID`(`ID`) COMMENT 'Identificador único de la Universidad.',
  ADD UNIQUE KEY `SQLID`(`SQLID`) COMMENT 'Identificador único de la Base de Datos';



--
-- Indexes for table `Cursos`
--
ALTER TABLE
  `Cursos` ADD PRIMARY KEY(`SQLID`) COMMENT 'Identificador SQL del Curso.',
  ADD UNIQUE KEY `UID`(`ID`, `SECCION`) COMMENT 'Identificador único del curso.',
  ADD UNIQUE KEY `SQLID`(`SQLID`) COMMENT 'Identificador único de la Base de Datos',
  ADD KEY `CATEDRATICO`(`CATEDRATICO`);



--
-- Indexes for table `Tutores-Cursos`
--
ALTER TABLE
  `Tutores-Cursos` ADD KEY `Tutor`(`Tutor`),
  ADD KEY `Curso`(`Curso`);



--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Administradores`
--
ALTER TABLE
  `Administradores` MODIFY `SQLID` TINYINT(3) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT
'Clave principal con autoincremento.';


--
-- AUTO_INCREMENT for table `Alumnos`
--
ALTER TABLE
  `Alumnos` MODIFY `SQLID` MEDIUMINT(7) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Clave
principal con autoincremento.';


--
-- AUTO_INCREMENT for table `Catedraticos`
--
ALTER TABLE
  `Catedraticos` MODIFY `SQLID` MEDIUMINT(7) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT
'Clave principal con autoincremento.';


--
-- AUTO_INCREMENT for table `Cursos`
--
ALTER TABLE
  `Cursos` MODIFY `SQLID` MEDIUMINT(8) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Clave
principal con autoincremento.';


--
-- Constraints for dumped tables
--

--
-- Constraints for table `Alumnos-Cursos-Aprobados`
--
ALTER TABLE
  `Alumnos-Cursos-Aprobados` ADD CONSTRAINT `Alumnos-Cursos-Aprobados_ibfk_1` FOREIGN
KEY(`Curso`) REFERENCES `Cursos`(`SQLID`),
  ADD CONSTRAINT `Alumnos-Cursos-Aprobados_ibfk_2` FOREIGN KEY(`Alumno`) REFERENCES
`Alumnos`(`SQLID`);



--
-- Constraints for table `Alumnos-Cursos-Corrientes`
--
ALTER TABLE
  `Alumnos-Cursos-Corrientes` ADD CONSTRAINT `Alumnos-Cursos-Corrientes_ibfk_1` FOREIGN
KEY(`Curso`) REFERENCES `Cursos`(`SQLID`),
  ADD CONSTRAINT `Alumnos-Cursos-Corrientes_ibfk_2` FOREIGN KEY(`Alumno`) REFERENCES
`Alumnos`(`SQLID`);



--
-- Constraints for table `Alumnos-Cursos-Reprobados`
--
ALTER TABLE
  `Alumnos-Cursos-Reprobados` ADD CONSTRAINT `Alumnos-Cursos-Reprobados_ibfk_1` FOREIGN
KEY(`Curso`) REFERENCES `Cursos`(`SQLID`),
  ADD CONSTRAINT `Alumnos-Cursos-Reprobados_ibfk_2` FOREIGN KEY(`Alumno`) REFERENCES
`Alumnos`(`SQLID`);



--
-- Constraints for table `Auxiliares-Cursos`
--
ALTER TABLE
  `Auxiliares-Cursos` ADD CONSTRAINT `Auxiliares-Cursos_ibfk_1` FOREIGN KEY(`Curso`)
REFERENCES `Cursos`(`SQLID`),
  ADD CONSTRAINT `Auxiliares-Cursos_ibfk_2` FOREIGN KEY(`Auxiliar`) REFERENCES
`Alumnos`(`SQLID`);



--
-- Constraints for table `Cursos`
--
ALTER TABLE
  `Cursos` ADD CONSTRAINT `Cursos_ibfk_1` FOREIGN KEY(`CATEDRATICO`) REFERENCES
`Catedraticos`(`SQLID`);



--
-- Constraints for table `Tutores-Cursos`
--
ALTER TABLE
  `Tutores-Cursos` ADD CONSTRAINT `Tutores-Cursos_ibfk_1` FOREIGN KEY(`Curso`) REFERENCES
`Cursos`(`SQLID`),
  ADD CONSTRAINT `Tutores-Cursos_ibfk_2` FOREIGN KEY(`Tutor`) REFERENCES
`Alumnos`(`SQLID`) ON DELETE CASCADE ON UPDATE CASCADE;
SET
  FOREIGN_KEY_CHECKS = 1;
