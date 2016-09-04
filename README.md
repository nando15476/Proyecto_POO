# Proyecto de POO

Este es el repositorio central para el proyecto de Programación Orientada a Objetos.

## Estructura

`doc` contiene todos los documentos escritos, en Markdown. `javadoc` contiene todos los
Javadoc generados. Los demás directorios contienen el código fuente y los archivos
generados por el IDE.

## ¿Cómo manejar este repositorio?

Para evitar los inherentes problemas al trabajar con GIT en un proyecto en equipo, cada
integrante deberá trabajar sobre una rama (`branch`) local que nunca se publicará. **No
se debe trabajar directamente sobre `master` ni publicar la rama local**.

Cuando los cambios de la rama local estén listos para integrarse a `master`, se debe
comenzar por **sincronizar todos los cambios desde GitHub** en la rama `master` local.
Cuando la rama `master` esté al día, se procederá con el `rebase`.

No se utilizará `merge`, esto es porque `merge` obliga a publicar la rama en el
repositorio remoto, y eso es precisamente lo que se quiere evitar. **Nunca se debe
integrar la rama local si `master` no está actualizado**.

Java, por su paradigma orientado a objetos, hace al proyecto fácilmente modular. Si bien
está permitido que dos personas trabajen en un solo archivo, es preferible que no suceda.
Como se dice en Programación Orientada a Objetos : *Me importa solo lo que hacés, no cómo
lo hacés*.

La documentación se escribirá en MarkDown en vez de Word, esto es para que GIT pueda
llevar un registro completo de los cambios en la documentación (y eso hace más más fácil
después hacer esta tabla de tiempos que pide la guía).

### Importante

IntelliJ o Android Studio proveen una preciosa herramienta llamada `lint`, que en Eclipse
no es tan intuitiva. Este repositorio es tanto un proyecto de IntelliJ como de Eclipse.
IntelliJ se prefiere sobre Eclipse para escribir código, pues puede detectar bugs antes
de que se produzcan.

Además, IntelliJ es superior a Eclipse en todo sentido. Desde la compleción de código
hasta el formateo. IntelliJ formatea el código automáticamente y muestra alertas y
mensajes respecto al estándar de código.

Recomiendo instalar IntelliJ y Eclipse, pues IntelliJ es superior, ayudará a mantener el
código correcto y bien formateado, ayudará a detectar bugs, a manejar el repositorio, no
genera código basura, usa `lint` y es estricto respecto al estándar de código. Pero hace
falta Eclipse porque IntelliJ no ofrece (gratis :) un diseñador de ventanas.

Yo usaré IntelliJ en mi computadora, el problema que surge si no lo usan ustedes es que
IntelliJ modificará en mi nombre los archivos que ustedes hagan que no sigan el estricto
estándar de código del proyecto, haciendo que sea difícil que una persona trabaje en un
solo archivo a la vez.

Sin nada más que agregar, este archivo no se debe modificar por nadie excepto por mí.
Terminemos este proyect ¡Yá! :)
