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
    * `Token token`, un Token que determina la autorización del Miembro.
* Métodos
	* `cambiarMailPersonal(@NotNull String mail)`, verifica el correo electrónico y una
vez verificado, cambia el correo en la base de datos y en el objeto.
	* `cambiarContasenia(String actual, @NotNull String shaNueva, @Nullable String
pista)`, cambia la contraseña actual del usuario a una nueva. El parámetro `actual` es la
contraseña en texto plano, esto es porque no se confía en terceras partes para calcular el
HASH. La contraseña actual, sin embargo, se convierte en HASH tan pronto como se recibe
para evitar mantenerla en memoria más tiempo del necesario.
	* `<estático>> notificar(String mensaje, Miembro miembro)`, envía una notificación al Miembro.


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
    * `<<estático>> agregarEnBaseDeDatos(Administrador auth, Curso curso)`, agrega un
nuevo
curso a la base de datos.
    * `<<estático>> removerDeBaseDeDatos(Administrador auth, Curso curso)`, remueve un
curso
a la base de datos.
    * `<<estático>> getDesdeBaseDeDatos(Administrador auth, String uid, int seccion):
Curso`, recupera un Curso desde la base de datos.

### Clase Administrador (hereda de Miembro)
La clase Administrador es una clase PHONY, se usa para determinar el rol del
Administrador. Cualquier Miembro puede solicitar un cambio en información sensible, sin
embargo, este cambio no se realizará hasta que un Administrador autorizado lo haga
efectivo. El Administrador está almacenado en la base de datos, pero no es un Miembro.
Pueden existir más de un Administrador, cada uno con su UID y su contraseña HASH. Si bien
Administrador hereda de Miembro, tiene ciertas diferencias fundamentales.

* Constructor
    * `Administrador()`: Genera un Administrador vacío, el Administrador es inválido hasta
que se autoriza correctamente, por lo tanto, no tiene sentido comenzar creando un objeto
con información.
* Atributos
    * Heredados de Miembro, excepto por el Nombre y los Apellidos.
    * `String nombreUsuario`, nombre de usuario del Administrador.
* Métodos
    * `isAutorizado(): boolean`, determina si el Administrador está autorizado.
    * `autorice(String username, String password)`, autoriza a un Administrador con su
nombre de usuario y su contraseña.

## Controlador
Estas clases controladores son aquellas que regulan el acceso a las clases Modelo. Por lo
general, abstraen el Modelo hacia la Vista, aunque no es regla, es lo deseable. La Vista y
el Controlador, a vaces, pueden tener funcionamiento combinado, haciéndo difícil la
distinción con precisión arbitraria entre ambas. En esta sección se describen los
controladores utilitarios.

### Clase Token
La clase Token sirve como Entidad Central de Autorización. Cada que se autoriza un
usuario, el método estático `makeNuevoToken(String)` genera un nuevo Token y lo
agrega al mapa de la Autoridad Central. Dicho mapa es privado, es central y sirve para
determinar la validez de un Token. El Token tiene una validez de `TOKEN_TIEMPO_VALIDO`
segundos desde la última vez que se autorizó una transacción con dicho Token.

* Constructor
    * `<<privado>> Token(String usersha, String randomsha)`, crea un nuevo Token.
* Atributos
    * `shaUsuario: String`, el SHA calculado tomando los datos del usuario, usando el ID
de SQL como sal.
    * `shaRandom: String`, un SHA generado aleatoriamente, corresponde al ID del Token.
    * `<<estático>> Map<String, String>`, mapa de Tokens validados de la Autoridad
Central.
* Métodos
    * `<<estático>> makeNuevoToken(String userHash): Token`, autoriza un nuevo Token para
el usuario especificado.
    * `autenticate(): boolean`, autentica un Token contra la Autoridad Central.
    * `revokeToken()`, revoca un Token.

# Clase Universidad
La clase Universidad controla, indexa y supervisa el funcionamiento y la relaciones entre
los Cursos y los Miembros. La Universidad es la interfaz del Administrador, y funciona
como canal para las Notificaciones. Universidad, junto con Token y los demás controladores
mantienen la lógica de negocios, el juego de roles y la autenticación.

- La clase Universidad mantiene relaciones muy complejas con el resto de componentes,
antes de definirla correctamente, hace falta definir correctamente los modelos. No se
puede definir Universidad de momento.
