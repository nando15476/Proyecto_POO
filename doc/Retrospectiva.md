# Retrospectiva

## Descripción del problema

Los alumnos siempre tienen dudas que consultar con sus catedráticos y auxiliares, sin
embargo, la Universidad no provee una forma fácil, sencilla y estandarizada de establecer
comunicación con ellos por parte de los alumnos. Blackboard no es intuitivo y además
muchos catedráticos ni siquiera lo utilizan.

Dichos catedráticos tienden a brindar su información de contacto por escrito, en sus guías
de curso o sencillamente no lo hacen. Otros omiten por completo la información de sus
auxiliares. Para solucionar este problema, este proyecto propone un consultor de cursos
fácil de usar e intuitivo.

Este incluirá una lista de los cursos corrientes que tiene asignado un alumno, información
de los catedráticos que imparten dichos cursos, información de los auxiliares y de
posibles tutores. Para cada uno de ellos, se dispondrá de información personal y de
contacto, horarios de atención y un sistema de notificaciones para agilizar la
comunicación.

Un alumno puede optar para ser tutor en cualquiera de los cursos que ya haya aprobado, de
esta forma se aumenta la posibilidad de obtener y brindar ayuda. El administrador del
sistema deberá aprobar cada una de las transacciones sensibles, por ejemplo, la asignación
de cursos, la aprobación de los tutores y la verificación y validación de la información
de contacto.

## Necesidades detectadas

Muchos alumnos se ven en la necesidad de contactar con alguno de sus catedráticos ya sea
por dudas o por cualquier gestión. Pero actualmente en la Universidad no es tan sencillo,
porque a veces el catedrático no se encuentra a la hora de su consulta o tiene reunión
etc. En base lo anterior las necesidades que nosotros detectamos fueron:

   * No existe una plataforma común que todos los catedráticos de la Universidad utilicen
para publicar información de contacto. Hay catedráticos que utilizan Blackboard, que
brindan su información en clase, que la colocan en sus guías y que no la comparten.
   * En muchos cursos no se sabe quien es el auxiliar y no hay forma de contactarlo o
contactarla. Esto hace difícil los casos en los que una nota debe ser corregida y la
comunicación en general.
   * Los tutores utilizan mecanismos no ideales para ofrecer su ayuda, por ejemplo,
escribir su información de contacto en el pizarrón del edificio A. Se necesita un mejor
mecanismo para ofrecer tutorías y para encontrarlas.
   * La mayoría de alumnos no resuelven sus dudas porque no saben donde encontrar a quien
los ayude, a sus catedráticos o auxiliares, en un sistema complicado y nada intuitivo.
Hace falta un sistema fácil y dedicado exclusivamente a solucionar este problema.

## Oportunidades para cada necesidad

Para poder darle una solución a las necesidades detectadas previamente nuestro proyecto se
encargará de que todas las personas en la Universidad tengan una misma plataforma por
donde puedan comunicarse, esto ayudará a que exista una mejor comunicación no solo entre
los mismos alumnos sino que también entre maestros y alumnos. De esta manera los maestros
pueden tener un lugar en donde colocar qué actividades tienen, a qué hora para que los
alumnos puedan coordinarse bien.

Esto mismo también con los auxiliares, ya que su información se encontrará enlazada con la
de los maestros, entonces será mucho mas sencillo para los alumnos identificar quien es el
auxiliar. También podría incluirse la información de los tutores de la materia que imparte
el maestro, así si el alumno necesita de ayuda en la clase, solo revisa la información que
se encuentre en la plataforma y el alumno obtendrá la información de contacto del tutor.

## Descripción de la fase de ideación

Gran parte de las ideas de esta fase se obtuvieron del documento hecho en Algoritmos y
Programación Básica. En dicho documento se describen los problemas y sus soluciones.
Además, en esta etapa se tomaron en cuenta ideas inherentes a las diferencias
fundamentales entre Java y Python.

La idea nace del hecho de que el sistema de la Universidad no provee un espacio dedicado
exclusivamente a fomentar la comunicación de los alumnos con sus catedráticos. Si bien
existe uno, es parte de otros sistemas, lo que la hace un complemento y no un sistema como
tal.

Esto agrega dificultad innecesaria cuando un alumno tiene necesidad de contactar a sus
catedráticos y auxiliares. Muchos alumnos se arrepienten ante la más mínima dificultad de
contactar para solucionar sus dudas y prefieren no hacerlo.

Otros alumnos prefieren recibir ayuda de compañeros que hayan recibido el curso
anteriormente con el mismo catedrático, pero se encuentran en la dificultad de que no hay
forma de encontrar a quién esté dispuesto a ofrecer sus tutorías.

Actualmente, el mecanismo más común es publicar sus servicios en el pizarrón del edificio
A. También, es común encontrar carteles en los muros de la Universidad donde los
catedráticos de física publican su horario de atención.

Ambos sistemas son arcaicos, muy ineficientes y para nada intuitivos, hace falta unificar
toda esa información y ponerla a disponibilidad inmediata de los alumnos. Nuestra idea es
crear un sistema que solucione estos problemas.

El modelo de este sistema, con relaciones de distintas clases de distintas formas, accesos
restringidos y autorizaciones lo hace ideal para ser modelado con bases de datos y con
programación orientada a objetos.

## Descripción breve de las ideas más votadas

1. **Buscador de aulas disponibles en la UVG**:

    * Esta idea consistía en una aplicación que mostraba las aulas disponibles dependiendo
del día y la hora. Esto surgió por la necesidad que tienen los estudiantes de estudiar en
la Universidad y la falta de información acerca de qué clases están disponibles para uso.
    * El usuario ingresaba el día y la hora que quisiese y la aplicación desplegaba una
lista de clases disponibles para uso. Todo esto con el fin de evitar perder tiempo
buscando un lugar para estudiar.

2. **Tutorías de programación orientada a objetos**:
    * Esta idea consistía en pequeñas explicaciones y exámenes para que el usuario pudiera
aprender conceptos básicos de la materia. El usuario puede guardar su nombre y contraseña,
ya que conforme iba aprobando las pruebas impartidas por la plataforma, este podía ir
subiendo de nivel y por consiguiente tenía acceso a temas más complicados.
    * El programa funcionaba con un sistema de estrellas. Este daba cierta cantidad de
estrellas en dependencia del puntaje que obtiene el usuario, cuando este llegaba a una
cierta cantidad de estrellas, se le daba acceso a lecciones más complicadas, luego toda
esta información era guardada en las bases de datos de los usuarios, con el nombre y
contraseña respectiva de cada persona

## Descripción breve de los prototipos

En las siguientes imágenes se muestra el prototipo del proyecto, basado en las capturas de
pantalla del proyecto original de Algoritmos.

