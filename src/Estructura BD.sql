-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 19-10-2016 a las 14:26:37
-- Versión del servidor: 10.0.27-MariaDB
-- Versión de PHP: 7.0.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `UVGDB`
--
CREATE DATABASE IF NOT EXISTS `UVGDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `UVGDB`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Administradores`
--

CREATE TABLE `Administradores` (
  `SQLID` int(11) UNSIGNED NOT NULL COMMENT 'Clave principal con autoincremento.',
  `USUARIO` varbinary(64) NOT NULL COMMENT 'Nombre de usuario del Administrador',
  `CLAVE` varbinary(64) NOT NULL COMMENT 'HASH de la contraseña actual del Administrador'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Alumnos`
--

CREATE TABLE `Alumnos` (
  `SQLID` int(11) UNSIGNED NOT NULL COMMENT 'Clave principal con autoincremento.',
  `ID` int(11) UNSIGNED NOT NULL COMMENT 'Carnet o identificador de la Universidad.',
  `NOMBRES` varchar(40) NOT NULL COMMENT 'Nombres del Alumno.',
  `APELLIDO1` varchar(20) NOT NULL COMMENT 'Primer apellido del Alumno.',
  `APELLIDO2` varchar(20) NOT NULL COMMENT 'Segundo apellido del Alumno.',
  `CORREO_USR` varchar(50) NOT NULL COMMENT 'Nombre de usuario de correo electrónico personal del Alumno.',
  `CORREO_HST` varchar(25) NOT NULL COMMENT 'Host de correo electrónico personal del Alumno.',
  `CLAVE` varbinary(64) NOT NULL COMMENT 'HASH de la contraseña actual del Alumno',
  `CORREO_UVG` varchar(20) NOT NULL COMMENT 'Nombre de usuario de correo electrónico de la UVG.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Tabla que almacena todos los Alumnos.';

--
-- Volcado de datos para la tabla `Alumnos`
--

INSERT INTO `Alumnos` (`SQLID`, `ID`, `NOMBRES`, `APELLIDO1`, `APELLIDO2`, `CORREO_USR`, `CORREO_HST`, `CLAVE`, `CORREO_UVG`) VALUES
(48, 554, 'Manía', 'Canía', 'Había', 'lola', 'mail.com', '', 'can00554'),
(49, 3232, 'Manía', 'Canía', 'Había', 'tete', 'mail.com', '', 'can05534');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Alumnos-Cursos-Aprobados`
--

CREATE TABLE `Alumnos-Cursos-Aprobados` (
  `ALUMNO` int(11) UNSIGNED NOT NULL COMMENT 'El Alumno con su respectivo Curso aprobado.',
  `CURSO` varchar(8) NOT NULL COMMENT 'Identificador principal del curso.',
  `SECCION` tinyint(3) UNSIGNED NOT NULL COMMENT 'Sección del Curso.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Tabla que relaciona cada Alumno con sus respectivos Cursos aprobados.';

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Alumnos-Cursos-Corrientes`
--

CREATE TABLE `Alumnos-Cursos-Corrientes` (
  `ALUMNO` int(11) UNSIGNED NOT NULL COMMENT 'El Alumno con su respectivo Curso corriente.',
  `CURSO` varchar(8) NOT NULL COMMENT 'Identificador principal del curso.',
  `SECCION` tinyint(3) UNSIGNED NOT NULL COMMENT 'Sección del Curso.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Tabla que relaciona cada Alumno con sus respectivos Cursos corrientes.';

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Alumnos-Cursos-Reprobados`
--

CREATE TABLE `Alumnos-Cursos-Reprobados` (
  `ALUMNO` int(11) UNSIGNED NOT NULL COMMENT 'El Alumno con su respectivo Curso reprobado.',
  `CURSO` varchar(8) NOT NULL COMMENT 'Identificador principal del curso.',
  `SECCION` tinyint(3) UNSIGNED NOT NULL COMMENT 'Sección del Curso.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Tabla que relaciona cada Alumno con sus respectivos Cursos reprobados.';

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Auxiliares-Cursos`
--

CREATE TABLE `Auxiliares-Cursos` (
  `AUXILIAR` int(11) UNSIGNED NOT NULL COMMENT 'El Auxiliar con su respectivo Curso de auxiliatura.',
  `CURSO` varchar(8) NOT NULL COMMENT 'Identificador principal del curso.',
  `SECCION` tinyint(3) UNSIGNED NOT NULL COMMENT 'Sección del Curso.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Tabla que relaciona cada Auxiliar con sus respectivos Cursos.';

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Catedraticos`
--

CREATE TABLE `Catedraticos` (
  `SQLID` int(11) UNSIGNED NOT NULL COMMENT 'Clave principal con autoincremento.',
  `ID` mediumint(7) UNSIGNED NOT NULL COMMENT 'Carnet o identificador de la Universidad.',
  `NOMBRES` varchar(40) NOT NULL COMMENT 'Nombres del Catedrático.',
  `APELLIDO1` varchar(20) NOT NULL COMMENT 'Primer apellido del Catedrático.',
  `APELLIDO2` varchar(20) NOT NULL COMMENT 'Segundo apellido del Catedrático.',
  `CORREO_USR` varchar(50) NOT NULL COMMENT 'Nombre de usuario de correo electrónico personal del Catedrático.',
  `CORREO_HST` varchar(25) NOT NULL COMMENT 'Host de correo electrónico personal del Catedrático.',
  `CLAVE` varbinary(64) NOT NULL COMMENT 'HASH de la contraseña actual del Alumno',
  `CORREO_UVG` varchar(20) NOT NULL COMMENT 'Nombre de usuario de correo electrónico de la UVG.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Tabla que almacena todos los Catedráticos.';

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Cursos`
--

CREATE TABLE `Cursos` (
  `SQLID` int(11) UNSIGNED NOT NULL COMMENT 'Clave principal con autoincremento.',
  `ID` varchar(8) NOT NULL COMMENT 'Identificador principal del curso.',
  `SECCION` tinyint(3) UNSIGNED NOT NULL COMMENT 'Sección del Curso.',
  `CATEDRATICO` int(11) UNSIGNED NOT NULL COMMENT 'Catedrático que imparte el Curso.',
  `NOMBRE` varchar(100) NOT NULL COMMENT 'Nombre completo del Curso'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Tutores-Cursos`
--

CREATE TABLE `Tutores-Cursos` (
  `TUTOR` int(11) UNSIGNED NOT NULL COMMENT 'El Auxiliar con su respectivo Curso de auxiliatura.',
  `CURSO` varchar(8) NOT NULL COMMENT 'Identificador principal del curso.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Tabla que relaciona cada Tutor con sus respectivos Cursos de tutoría.';

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `Administradores`
--
ALTER TABLE `Administradores`
  ADD PRIMARY KEY (`SQLID`) COMMENT 'Clave principal que sirve como identificador único en SQL.',
  ADD UNIQUE KEY `UID` (`USUARIO`) COMMENT 'Identificador único del Administrador';

--
-- Indices de la tabla `Alumnos`
--
ALTER TABLE `Alumnos`
  ADD PRIMARY KEY (`SQLID`) COMMENT 'Clave principal que sirve como índice en SQL.',
  ADD UNIQUE KEY `CORREO` (`CORREO_USR`,`CORREO_HST`) COMMENT 'Ningún Alumno puede tener el mismo correo electrónico que otro Alumno.',
  ADD UNIQUE KEY `CORREO_UVG` (`CORREO_UVG`) COMMENT 'Ningún Alumno puede tener el mismo correo electrónico de la Universidad que otro Alumno.',
  ADD UNIQUE KEY `UID` (`ID`) COMMENT 'Identificador único de la Universidad.';

--
-- Indices de la tabla `Alumnos-Cursos-Aprobados`
--
ALTER TABLE `Alumnos-Cursos-Aprobados`
  ADD UNIQUE KEY `UNICO_CURSO` (`ALUMNO`,`CURSO`) COMMENT 'Evita la doble asignación a un curso en otra sección.',
  ADD UNIQUE KEY `UNICA_SECCION` (`ALUMNO`,`CURSO`,`SECCION`) COMMENT 'Evita la doble asignación a un curso en la misma sección.',
  ADD KEY `Sección a Curso/Alumno Aprobados` (`SECCION`),
  ADD KEY `Curso a Sección/Alumno Aprobados` (`CURSO`);

--
-- Indices de la tabla `Alumnos-Cursos-Corrientes`
--
ALTER TABLE `Alumnos-Cursos-Corrientes`
  ADD UNIQUE KEY `UNICO_CURSO` (`ALUMNO`,`CURSO`) COMMENT 'Evita la doble asignación a un curso en otra sección.',
  ADD UNIQUE KEY `UNICA_SECCION` (`ALUMNO`,`CURSO`,`SECCION`) COMMENT 'Evita la doble asignación a un curso en la misma sección.',
  ADD KEY `Sección a Curso/Alumno Corriente` (`SECCION`),
  ADD KEY `Curso a Sección/Alumno Corriente` (`CURSO`);

--
-- Indices de la tabla `Alumnos-Cursos-Reprobados`
--
ALTER TABLE `Alumnos-Cursos-Reprobados`
  ADD UNIQUE KEY `UNICO_CURSO` (`ALUMNO`,`CURSO`) COMMENT 'Evita la doble asignación a un curso en otra sección.',
  ADD UNIQUE KEY `UNICA_SECCION` (`ALUMNO`,`CURSO`,`SECCION`) COMMENT 'Evita la doble asignación a un curso en la misma sección.',
  ADD KEY `Sección a Curso/Alumno Reprobados` (`SECCION`),
  ADD KEY `Curso a Sección/Alumno Reprobados` (`CURSO`);

--
-- Indices de la tabla `Auxiliares-Cursos`
--
ALTER TABLE `Auxiliares-Cursos`
  ADD UNIQUE KEY `UNICO_CURSO` (`AUXILIAR`,`CURSO`) COMMENT 'Evita la doble asignación a un curso en otra sección.',
  ADD UNIQUE KEY `UNICA_SECCION` (`AUXILIAR`,`CURSO`,`SECCION`) COMMENT 'Evita la doble asignación a un curso en la misma sección.',
  ADD KEY `Sección a Curso/Auxiliar` (`SECCION`),
  ADD KEY `Curso a Sección/Auxiliar` (`CURSO`);

--
-- Indices de la tabla `Catedraticos`
--
ALTER TABLE `Catedraticos`
  ADD PRIMARY KEY (`SQLID`) COMMENT 'Clave principal que sirve como índice en SQL.',
  ADD UNIQUE KEY `CORREO` (`CORREO_USR`,`CORREO_HST`) COMMENT 'Ningún Catedrático puede tener el mismo correo electrónico que otro Catedrático.',
  ADD UNIQUE KEY `CORREO_UVG` (`CORREO_UVG`) COMMENT 'Ningún Catedrático puede tener el mismo correo electrónico de la Universidad que otro Catedrático.',
  ADD UNIQUE KEY `UID` (`ID`) COMMENT 'Identificador único de la Universidad.';

--
-- Indices de la tabla `Cursos`
--
ALTER TABLE `Cursos`
  ADD PRIMARY KEY (`SQLID`) COMMENT 'Identificador SQL del Curso.',
  ADD UNIQUE KEY `UID` (`ID`,`SECCION`) COMMENT 'Identificador único del curso.',
  ADD UNIQUE KEY `SQLID` (`SQLID`) COMMENT 'Identificador único de la Base de Datos',
  ADD KEY `CATEDRATICO` (`CATEDRATICO`),
  ADD KEY `SECCION` (`SECCION`);

--
-- Indices de la tabla `Tutores-Cursos`
--
ALTER TABLE `Tutores-Cursos`
  ADD UNIQUE KEY `UNICO_CURSO` (`TUTOR`,`CURSO`) COMMENT 'Evita la doble asignación de un tutor a un curso.',
  ADD KEY `Curso a Tutor` (`CURSO`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `Administradores`
--
ALTER TABLE `Administradores`
  MODIFY `SQLID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Clave principal con autoincremento.';
--
-- AUTO_INCREMENT de la tabla `Alumnos`
--
ALTER TABLE `Alumnos`
  MODIFY `SQLID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Clave principal con autoincremento.', AUTO_INCREMENT=52;
--
-- AUTO_INCREMENT de la tabla `Catedraticos`
--
ALTER TABLE `Catedraticos`
  MODIFY `SQLID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Clave principal con autoincremento.';
--
-- AUTO_INCREMENT de la tabla `Cursos`
--
ALTER TABLE `Cursos`
  MODIFY `SQLID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Clave principal con autoincremento.';
--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `Alumnos-Cursos-Aprobados`
--
ALTER TABLE `Alumnos-Cursos-Aprobados`
  ADD CONSTRAINT `Alumno a Sección/Curso Aprobados` FOREIGN KEY (`ALUMNO`) REFERENCES `Alumnos` (`ID`),
  ADD CONSTRAINT `Curso a Sección/Alumno Aprobados` FOREIGN KEY (`CURSO`) REFERENCES `Cursos` (`ID`),
  ADD CONSTRAINT `Sección a Curso/Alumno Aprobados` FOREIGN KEY (`SECCION`) REFERENCES `Cursos` (`SECCION`);

--
-- Filtros para la tabla `Alumnos-Cursos-Corrientes`
--
ALTER TABLE `Alumnos-Cursos-Corrientes`
  ADD CONSTRAINT `Alumno a Sección/Curso Corriente` FOREIGN KEY (`ALUMNO`) REFERENCES `Alumnos` (`ID`),
  ADD CONSTRAINT `Curso a Sección/Alumno Corriente` FOREIGN KEY (`CURSO`) REFERENCES `Cursos` (`ID`),
  ADD CONSTRAINT `Sección a Curso/Alumno Corriente` FOREIGN KEY (`SECCION`) REFERENCES `Cursos` (`SECCION`);

--
-- Filtros para la tabla `Alumnos-Cursos-Reprobados`
--
ALTER TABLE `Alumnos-Cursos-Reprobados`
  ADD CONSTRAINT `Alumno a Sección/Curso Reprobados` FOREIGN KEY (`ALUMNO`) REFERENCES `Alumnos` (`ID`),
  ADD CONSTRAINT `Curso a Sección/Alumno Reprobados` FOREIGN KEY (`CURSO`) REFERENCES `Cursos` (`ID`),
  ADD CONSTRAINT `Sección a Curso/Alumno Reprobados` FOREIGN KEY (`SECCION`) REFERENCES `Cursos` (`SECCION`);

--
-- Filtros para la tabla `Auxiliares-Cursos`
--
ALTER TABLE `Auxiliares-Cursos`
  ADD CONSTRAINT `Auxiliar a Sección/Curso` FOREIGN KEY (`AUXILIAR`) REFERENCES `Alumnos` (`ID`),
  ADD CONSTRAINT `Curso a Sección/Auxiliar` FOREIGN KEY (`CURSO`) REFERENCES `Cursos` (`ID`),
  ADD CONSTRAINT `Sección a Curso/Auxiliar` FOREIGN KEY (`SECCION`) REFERENCES `Cursos` (`SECCION`);

--
-- Filtros para la tabla `Cursos`
--
ALTER TABLE `Cursos`
  ADD CONSTRAINT `Catedráticos a Cursos` FOREIGN KEY (`CATEDRATICO`) REFERENCES `Catedraticos` (`SQLID`);

--
-- Filtros para la tabla `Tutores-Cursos`
--
ALTER TABLE `Tutores-Cursos`
  ADD CONSTRAINT `Curso a Tutor` FOREIGN KEY (`CURSO`) REFERENCES `Cursos` (`ID`),
  ADD CONSTRAINT `Tutor a Curso` FOREIGN KEY (`TUTOR`) REFERENCES `Alumnos` (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
