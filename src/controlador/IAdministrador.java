package controlador;

import modelo.Administrador;

/**
 * Una interfaz que permite comunicarse con cualquiera que implemente sus métodos. Esta interfaz
 * existe para promover el <a href="https://en.wikipedia.org/wiki/Loose_coupling">Acoplamiento
 * flojo</a>. Todos los herederos de {@link Administrador} implementan esta interfaz.
 * <p>
 * Además utilizar la interfaz en lugar de la clase concreta <b>prohibe</b> exitosamente la
 * creación arbitraria de objetos {@link Administrador}.
 */
public interface IAdministrador {
    boolean isAutorizado();
}
