# Identificación y Descripción de Clases necesarias

En este documento se detallan superficialmente las clases necesarias para modelar y
solucionar el problema. Se obvian y omiten los getters y setters sencillos (definidos como
aquellos que solo configuran y devuelven un atributo), los constructores vacíos, los
métodos privados (mas los atributos privados pueden inclurse media vez sean
significativos), clases de bibliotecas terceras, externas o del JRE y JDK, y clases
accesorias (como las de acceso a los datos y el controlador de bases de datos).

## Modelo
En la siguiente clase se describen los modelos en los que radica el funcionamiento del
sistema (y están implementados en las tablas de la base de datos). Las relaciones tienen
como eje central la clase Curso y la tabla Curso en la base de datos. Esto es porque Curso
es con lo que interactúan todos los herederos de Miembro.

### Clase Miembro
Contiene toda la información que todo miembro de la Universidad debe brindar a la misma
para pertenecer a ella y todas las funciones comunes que comparten todos los miembros. De
esta clase heredan tanto Alumno, como Auxiliar (que hereda de Alumno), Tutor y
Catedratico.

* Constructor
	* `Miembro(String nombre, String apellido, int id)`: El constructor se utiliza
solamente para crear nuevos miembros manualmente y para recuperarlos de la base de datos,
dado que la clase Miembro *es inmutable*.
* Atributos
	* `id: int`, identificador único tiene cualquier miembro de la universidad. En Alumno
es un carnet de estudiante mientras que en Catedratico es un ID de catedrático.
	* `nombres: String`, un String que contiene el nombre (sin apellidos) del Miembro.
	* `apellidos: String`, un String que contiene solo los apellidos del Miembro.
	* `correo: String`, correo personal (el setter debe utilizar REGEX para validar)
		* El correo de la Universidad no se incluye porque es distinto para Alumnos y
Catedráticos, por lo que no puede incluirse en una clase genérica.
	* `contraseniaSha: String`, un String que es el SHA256 de la  contraseña guardada
durante el proceso de registro.
	* `sqlId: long`, un número que corresponde a un valor generado automáticamente por la
base de datos y, junto con el `id` permite evitar cambios no autorizados.
* Métodos
	* `cambiarMailPersonal(@NotNull String mail)`, verifica el correo electrónico y una
vez verificado, cambia el correo en la base de datos y en el objeto.
	* `cambiarContasenia(String actual, @NotNull String shaNueva, @Nullable String
pista)`, cambia la contraseña actual del usuario a una nueva. El parámetro `actual` es la
contraseña en texto plano, esto es porque no se confía en terceras partes para calcular el
HASH. La contraseña actual, sin embargo, se convierte en HASH tan pronto como se recibe
para evitar mantenerla en memoria más tiempo del necesario.
	* `<static>> notificar(String mensaje, String)`, envía una notificación al Miembro.


### Clase Alumno (hereda de Miembro)
Contiene, además de lo mismo que la clase Miembro, información concerniente al Alumno,
como por ejemplo, la lista de cursos asignados.

* Constructor
	* `Alumno(String nombre, String apellido, int id)`: El constructor se utiliza
solamente para crear nuevos alumno manualmente y para recuperarlos de la base de datos,
dado que la clase Miembro *es inmutable*.
* Atributos
	* Heredados de Miembro
	* `ucorreo: String`, correo de la Universidad (el setter debe utilizar REGEX para
validar)
	* `ArrayList cursos: Curso`, una lista con todos los cursos del estudiante.
* Métodos
	* Heredado de Miembro
	* `asignarCurso(Curso curso)`, agrega un nuevo curso al Alumno.
	* `retirarCurso(Curso curso)`, retira un curso de la lista de Cursos del Alumno.

### Clase Catedratico (hereda de Miembro)
Contiene, además de lo mismo que la clase Miembro, información concerniente al
Catedratico, como por ejemplo, la lista de cursos asignados.

* Constructor
	* `Catedratico(String nombre, String apellido, int id)`: El constructor se utiliza
solamente para crear nuevos catedrático manualmente y para recuperarlos de la base de
datos,
dado que la clase Miembro *es inmutable*.
* Atributos
	* Heredados de Miembro
	* `ucorreo: String`, correo de la Universidad (el setter debe utilizar REGEX para
validar)
	* `ArrayList cursos: Curso`, una lista con todos los cursos que imparte el
catedrático.
* Métodos
	* Heredado de Miembro
	* `asignarCurso(Curso curso)`, agrega un nuevo Curso al Catedratico.
	* `retirarCurso(Curso curso)`, retira un Curso de la lista de cursos del Catedratico.

### Clase Auxiliar (hereda de Alumno)
Contiene, además de lo mismo que la clase Alumno, información concerniente al Auxilar. Un
auxiliar puede tomar rol de Alumno gracias al **polimorfismo**.

* Constructor
	* `Auxiliar(String nombre, String apellido, int id)`: El constructor se utiliza
solamente para crear nuevos auxiliares manualmente y para recuperarlos de la base de
datos, dado que la clase Miembro *es inmutable*.
* Atributos
	* Heredados de Alumno
	* `ArrayList cursosAux: Curso`, una lista de cursos que este Alumno auxilia.
* Métodos
	* Heredado de Alumno
	* `asignarCursoAuxiliar(Curso curso)`, agrega un nuevo Curso al Catedratico.
	* `retirarCursoAuxiliar(Curso curso)`, retira un Curso de la lista de cursos del
Catedratico.

### Clase Tutor (hereda de Alumno)
Contiene, además de lo mismo que la clase Alumno, información concerniente al Tutor. Un
tutor puede tomar rol de Alumno gracias al **polimorfismo**. El Tutor podrá asignarse
cualquier Curso que desee para tutoría, media vez ya lo haya aprobado. En cualquier
momento puede optar por retirarse el Curso.

* Constructor
	* `Tutor(String nombre, String apellido, int id)`: El constructor se utiliza
solamente para crear nuevos tutores manualmente y para recuperarlos de la base de
datos, dado que la clase Miembro *es inmutable*.
* Atributos
	* Heredados de Alumno
	* `ArrayList cursosTut: Curso`, una lista de cursos que este Alumno auxilia.
* Métodos
	* Heredado de Alumno
	* `asignarCursoTutor(Curso curso)`, agrega un nuevo Curso al Catedratico.
	* `retirarCursoTutor(Curso curso)`, retira un Curso de la lista de cursos del
Catedratico.

### Clase Curso
Esta clase contiene un Curso particular. La relación de la base de datos entre Curso y
Alumno es de muchos a muchos: un alumno puede asignarse cero, uno o más cursos media vez
el Curso no sea el mismo. Un curso se define como "el mismo" si su código único es el
mismo y la sección es la misma o diferente. Lo mismo aplica para la relación Curso y
Catedratico. En el diagrama UML, la relación entre Alumno o Catedratico y Curso es de
dependencia, es decir, a cada Alumno le tocará una instancia particular del Curso, aún si
muchos alumnos comparten el Curso. Esto se debe a que Tutor no tiene porqué tener asignado
un curso para dar tutoría. Esto obliga a hacer la distinción.

* Constructor
    * `Curso(String UID, String Nombre, int seccion)`: para que un Curso quede
plenamente definido como único, debe conocerse su identificador único y su nombre de
curso.
* Atributos
    * `Alumno[] alumnos`, una lista con todos los Alumnos que tienen asignado el Curso en
la sección específica.
    * `Catedratico catedratico`, el Catedrático que imparte el curso.
    * `Tutor[] tutores`, una lista con todos los tutores disponibles para este Curso. En
el caso particular de Tutores, no se hace distinción por sección, mas esto lo maneja la
base de datos.
    * `Auxiliar[] auxiliares`, una lista con los auxiliares del curso de esa sección.
    * `nombre: String`, nombre del curso.
    * `uId: String`, identificador único del curso de la Universidad.
    * `sqlId: long`, identificador único en la base de datos.
La mayoría de métodos son para administración de la base de datos por parte del
Administrador.
* Métodos
    * `agregarAlumno(Administrador auth, Alumno alumno)`, teniendo un Administrador
autorizando, permite agregar un Alumno al Curso.
    * `removerAlumno(Administrador auth, Alumno alumno)`, teniendo un Administrador
autorizando, permite remover un Alumno al Curso.
    * `cambiarCatedratico(Administrador auth, Catedratico anterior, Catedratico nuevo)`,
cambia de catedrático a un Curso.
    * `agregarAuxiliar(Administrador auth, Auxilar auxiliar)`, teniendo un Administrador
autorizando, permite agregar un Auxilar al Curso.
    * `removerAuxiliar(Administrador auth, Auxilar auxiliar)`, teniendo un Administrador
autorizando, permite remover un Auxilar al Curso.
    * `<<static>> agregarEnBaseDeDatos(Administrador auth, Curso curso)`, agrega un nuevo
curso a la base de datos.
    * `<<static>> removerDeBaseDeDatos(Administrador auth, Curso curso)`, remueve un curso
a la base de datos.
    * `<<static>> getDesdeBaseDeDatos(Administrador auth, String uid, int seccion):
Curso`, recupera un Curso desde la base de datos.
